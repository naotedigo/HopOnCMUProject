package pt.ulisboa.tecnico.cmov.hoponcmuproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;



public class SignUpActivity extends AppCompatActivity {
    HopOnCMUApplication _hopOnApp;
    protected Handler _handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        _hopOnApp = (HopOnCMUApplication) getApplicationContext();




        Intent intent = getIntent();
        if(intent.hasExtra(LoginIntentKey.USERNAME.toString()) && intent.hasExtra(LoginIntentKey.CODE.toString())) {
            ((EditText) findViewById(R.id.username_txt)).setText(intent.getStringExtra(LoginIntentKey.USERNAME.toString()));
            ((EditText) findViewById(R.id.code_txt)).setText(intent.getStringExtra(LoginIntentKey.CODE.toString()));
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceKey.USERNAME.toString(), Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(SharedPreferenceKey.USERNAME.toString(), Constant.INVALID_VALUE),
                    password = sharedPreferences.getString(SharedPreferenceKey.CODE.toString(), Constant.INVALID_VALUE);

            if(username != null && password != null &&
                    !username.equals(Constant.INVALID_VALUE) &&
                    !password.equals(Constant.INVALID_VALUE)) {
                ((EditText) findViewById(R.id.new_username_txt)).setText(username);
                ((EditText) findViewById(R.id.new_code_txt)).setText(password);
            }
        }

        _handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message message){
                ServerReply serverReply = ServerReply.values()[message.what];
                System.out.println(message.what);
                EditText usernameEditText = (EditText) findViewById(R.id.new_username_txt),
                        passwordEditText = (EditText) findViewById(R.id.new_code_txt);
                String usernameValue = usernameEditText.getText().toString(),
                        passwordValue = passwordEditText.getText().toString();

                System.out.println(serverReply);
                switch (serverReply){
                    case SUCESS:
                        Toast.makeText(_hopOnApp, "Sign Up sucessfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.putExtra(LoginIntentKey.USERNAME.toString(), usernameValue);
                        intent.putExtra(LoginIntentKey.CODE.toString(), passwordValue);
                        startActivityForResult(intent, ApplicationOperationsCode.LOGIN.ordinal());
                        break;
                    case INVALID_PASS:
                        Toast.makeText(_hopOnApp, "Code already in use", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_USER:
                        Toast.makeText(_hopOnApp, "Username already in use", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return;
                }
            }
        };
    }

    public void SignUpBtnClicked(View view) throws JSONException {
        //Get sign up values
        EditText usernameEditText = (EditText) findViewById(R.id.new_username_txt),
                passwordEditText = (EditText) findViewById(R.id.new_code_txt);
        String usernameValue = usernameEditText.getText().toString(),
                passwordValue = passwordEditText.getText().toString();
        Spinner mySpinner = (Spinner) findViewById(R.id.country_id);
        String usercountry = mySpinner.getSelectedItem().toString();

        //Check if values exist
        if (usernameValue.equals("")){
            Toast.makeText(this," Nao colocou username",Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordValue.equals("")){
            Toast.makeText(this," Nao colocou password",Toast.LENGTH_SHORT).show();
            return;
        }


        // Request Message
        UserRequest userRequest = UserRequest.SIGN_UP;
        JSONObject message = new JSONObject();
        message.put(NetworkKey.REQUEST_TYPE.toString(), userRequest.ordinal());
        message.put(NetworkKey.USERNAME.toString(), usernameValue);
        message.put(NetworkKey.PASSWORD.toString(), passwordValue);
        message.put(NetworkKey.COUNTRY.toString(), usercountry);

        ClientProxy clientProxy = new ClientProxy(userRequest, _handler , NetworkMsg.SIGN_UP , message);
        new Thread(clientProxy).start();


        //Save login fields
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceKey.USERNAME.toString(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SharedPreferenceKey.USERNAME.toString(), usernameValue);
        editor.putString(SharedPreferenceKey.CODE.toString(), passwordValue);

        editor.apply();
    }

    public void LoginBtnClicked(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
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
