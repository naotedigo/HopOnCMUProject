package pt.ulisboa.tecnico.cmov.hoponcmuproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonumentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    HopOnCMUApplication mHopOnCMUApplication;

    private ListView mListView;
    private JSONArray monuments;
    protected Handler _handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument);

        mHopOnCMUApplication = (HopOnCMUApplication) getApplicationContext();

        //Cria o List view e o adaptador

        mListView = (ListView) findViewById(R.id.monument_list_id);
        ListView listview = (ListView) findViewById(R.id.monument_list_id);
        Intent intent = getIntent();
        monuments = mHopOnCMUApplication.getMonumentsList();
        String stringsSpinner[] = new String[monuments.length()];
        for (int i = 0; i < stringsSpinner.length; i++) {
            try {
                stringsSpinner[i] = monuments.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringsSpinner);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);

        //Handler
        _handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                ServerReply serverReply = ServerReply.values()[message.what];

                System.out.println(message.what);
                System.out.println(serverReply);
                switch (serverReply) {
                    case SUCESS:
                        JSONObject Reply = (JSONObject) message.obj;
                        try {
                            String monumentName = Reply.getString(NetworkKey.MONUMENT_NAME.toString());
                            JSONArray QuizInfo =  Reply.getJSONArray(NetworkKey.QUIZ_INFO.toString());
                            mHopOnCMUApplication.setQuizArray(monumentName, QuizInfo);
                            mHopOnCMUApplication.addQuizList(monumentName);
                            mHopOnCMUApplication.setQuestions(QuizInfo);

                            Intent intent = new Intent(MonumentActivity.this, MainActivity.class);
                            intent.putExtra(GlobalKey.USERNAME.toString(), mHopOnCMUApplication.getUsername());
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
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {


        Intent intent1 = getIntent();

        HashMap<String, JSONArray> quizList = mHopOnCMUApplication.getQuiz_array();
        monuments = mHopOnCMUApplication.getMonumentsList();
        try {
            if (quizList.containsKey(monuments.get(position))){
                Toast.makeText(this,"Quiz " + id + " already downloaded" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MonumentActivity.this, MainActivity.class);
                intent.putExtra(GlobalKey.USERNAME.toString(), mHopOnCMUApplication.getUsername());
                intent.putExtra(GlobalKey.CODE.toString(), mHopOnCMUApplication.getCode());
                startActivity(intent);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserRequest userRequest = UserRequest.GET_QUIZ;
        JSONObject message = new JSONObject();

        try {
            message.put(NetworkKey.REQUEST_TYPE.toString(), userRequest.ordinal());
            message.put(NetworkKey.MONUMENT_NAME.toString(), monuments.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ClientProxy clientProxy = new ClientProxy(userRequest, _handler ,NetworkMsg.GET_MONUMENT_LIST , message);
        new Thread(clientProxy).start();


    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
