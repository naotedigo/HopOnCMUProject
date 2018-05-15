package pt.ulisboa.tecnico.cmov.hoponcmuproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    HopOnCMUApplication mHopOnCMUApplication;
    protected Handler _handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHopOnCMUApplication = (HopOnCMUApplication)getApplicationContext();


        Intent intent = getIntent();

        String usernameValue = intent.getStringExtra(LoginIntentKey.USERNAME.toString());
        mHopOnCMUApplication.setUsername(usernameValue);
        String passwordValue = intent.getStringExtra(LoginIntentKey.CODE.toString());
        mHopOnCMUApplication.setCode(passwordValue);

        // HardCoded Value


        ArrayList<String> results = mHopOnCMUApplication.getSubmitList();
        if (results.isEmpty()) {
            results.add("Final Tour Results");
            mHopOnCMUApplication.setSubmitList(results);
        }
        bindService(
                new Intent(MainActivity.this, HopOnService.class),
                wifiServiceConn,
                Context.BIND_AUTO_CREATE);



        JSONArray jsonStrings = mHopOnCMUApplication.getTourList();
        String stringsSpinner[] = new String[jsonStrings.length()];
        for(int i=0;i<stringsSpinner.length;i++) {
            try {
                stringsSpinner[i] = jsonStrings.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringsSpinner
        ); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        _handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message message){
                ServerReply serverReply = ServerReply.values()[message.what];

                System.out.println(message.what);
                System.out.println(serverReply);
                switch (serverReply){
                    case SUCESS:
                        JSONObject Reply = (JSONObject) message.obj;
                        try {
                            JSONArray monuments = Reply.getJSONArray(NetworkKey.MONUMENT_LIST.toString());

                            mHopOnCMUApplication.setTourArray(Reply.getString(NetworkKey.TOUR_NAME.toString()), monuments);
                            mHopOnCMUApplication.setMonumentList(monuments);
                            Intent intent = new Intent(MainActivity.this, MonumentActivity.class);
                            intent.putExtra(GlobalKey.USERNAME.toString(),mHopOnCMUApplication.getUsername());
                            intent.putExtra(GlobalKey.CODE.toString(), mHopOnCMUApplication.getCode());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        return;
                }
            }
        };
    }

    public void listMonumentsBtnClicked(View view){

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String selectedTour = spinner.getSelectedItem().toString();
        System.out.println(selectedTour);
        System.out.println(mHopOnCMUApplication.checkTour_arrayKey(selectedTour));

        // Check if tour already downloaded, if Yes goes to next Activity
        if (mHopOnCMUApplication.checkTour_arrayKey(selectedTour)){

            JSONArray monuments =mHopOnCMUApplication.getTour_arrayKey(selectedTour);
            mHopOnCMUApplication.setMonumentList(monuments);

            System.out.println(monuments);
            Intent intent = new Intent(MainActivity.this, MonumentActivity.class);
            intent.putExtra(GlobalKey.USERNAME.toString(),mHopOnCMUApplication.getUsername());
            intent.putExtra(GlobalKey.CODE.toString(), mHopOnCMUApplication.getCode());
            startActivity(intent);
            return ;
        }
        //If not requests the server for the selected Tour
        UserRequest userRequest = UserRequest.GET_MONUMENT_LIST;
        JSONObject message = new JSONObject();

        try {
            message.put(NetworkKey.REQUEST_TYPE.toString(), userRequest.ordinal());
            message.put(NetworkKey.TOUR_NAME.toString(), selectedTour);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ClientProxy clientProxy = new ClientProxy(userRequest, _handler ,NetworkMsg.GET_MONUMENT_LIST , message);
        new Thread(clientProxy).start();


    }

    public void QuizBtnClicked(View view){

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(GlobalKey.USERNAME.toString(),mHopOnCMUApplication.getUsername());
        intent.putExtra(GlobalKey.CODE.toString(), mHopOnCMUApplication.getCode());
        intent.putExtra(GlobalKey.SUBMIT_LIST.toString(), mHopOnCMUApplication.getSubmitList());
        startActivity(intent);
    }


    public void scoreBtnClicked(View view){
        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
        intent.putExtra(GlobalKey.USERNAME.toString(),mHopOnCMUApplication.getUsername());
        intent.putExtra(GlobalKey.CODE.toString(), mHopOnCMUApplication.getCode());
        intent.putExtra(GlobalKey.SUBMIT_LIST.toString(), mHopOnCMUApplication.getSubmitList());
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(wifiServiceConn);
    }

    private HopOnService wifiService = null;
    private HopOnService.WifiBinder wifiBinder = null;
    private ServiceConnection wifiServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            wifiBinder = (HopOnService.WifiBinder) service;
            wifiService = wifiBinder.getService();

            wifiService.setMainActivity(MainActivity.this);

            wifiService.setUsername(mHopOnCMUApplication.getUsername());


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


}
