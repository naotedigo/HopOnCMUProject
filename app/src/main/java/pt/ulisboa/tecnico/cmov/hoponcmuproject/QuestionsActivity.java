package pt.ulisboa.tecnico.cmov.hoponcmuproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionsActivity extends AppCompatActivity {
    HopOnCMUApplication mHopOnCMUApplication;
    protected Handler _handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        mHopOnCMUApplication = (HopOnCMUApplication) getApplicationContext();





        _handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message message){
                ServerReply serverReply = ServerReply.values()[message.what];

                System.out.println(message.what);
                System.out.println(serverReply);
                switch (serverReply) {
                    case SUCESS:
                        JSONObject Reply = (JSONObject) message.obj;
                        try {
                            String monumentName = Reply.getString(NetworkKey.MONUMENT_NAME.toString());
                            int QuizScore =  Reply.getInt(NetworkKey.MONUMENT_SCORE.toString());
                            String tourName = Reply.getString(NetworkKey.TOUR_NAME.toString());
                            int TourScore =  Reply.getInt(NetworkKey.TOUR_SCORE.toString());

                            mHopOnCMUApplication.addQuizscore(monumentName, QuizScore);
                            mHopOnCMUApplication.removeTourScore(tourName);
                            mHopOnCMUApplication.addTourScore(tourName , TourScore);

                            Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                            intent.putExtra(GlobalKey.SUBMIT_LIST.toString(), mHopOnCMUApplication.getSubmitList());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case QUIZ_SUBMITED:
                        Intent thisintent = getIntent();
                        Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                        startActivity(intent);
                        return;
                    default:
                        break;

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView question1 = (TextView) findViewById(R.id.question1_id);
        TextView question2 = (TextView) findViewById(R.id.question2_id);
        TextView question3 = (TextView) findViewById(R.id.question3_id);

        RadioButton radioButton1A = (RadioButton)findViewById(R.id.question_1A) ;
        RadioButton radioButton1B = (RadioButton)findViewById(R.id.question_1B) ;
        RadioButton radioButton1C = (RadioButton)findViewById(R.id.question_1C) ;

        RadioButton radioButton2A = (RadioButton)findViewById(R.id.question_2A) ;
        RadioButton radioButton2B = (RadioButton)findViewById(R.id.question_2B) ;
        RadioButton radioButton2C = (RadioButton)findViewById(R.id.question_2C) ;

        RadioButton radioButton3A = (RadioButton)findViewById(R.id.question_3A) ;
        RadioButton radioButton3B = (RadioButton)findViewById(R.id.question_3B) ;
        RadioButton radioButton3C = (RadioButton)findViewById(R.id.question_3C) ;

        Intent intent = getIntent();
        String QuizKey = intent.getStringExtra("Selected Value");
        HashMap<String, JSONArray> Quiz = mHopOnCMUApplication.getQuiz_array();

        JSONArray QuizInformation = Quiz.get(QuizKey);

        System.out.println(QuizInformation);


        try {
            question1.setText(QuizInformation.getString(0));
            radioButton1A.setText(QuizInformation.getString(1));
            radioButton1B.setText(QuizInformation.getString(2));
            radioButton1C.setText(QuizInformation.getString(3));

            question2.setText(QuizInformation.getString(4));
            radioButton2A.setText(QuizInformation.getString(5));
            radioButton2B.setText(QuizInformation.getString(6));
            radioButton2C.setText(QuizInformation.getString(7));

            question3.setText(QuizInformation.getString(8));
            radioButton3A.setText(QuizInformation.getString(9));
            radioButton3B.setText(QuizInformation.getString(10));
            radioButton3C.setText(QuizInformation.getString(11));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void SubmitBtnClicked(View view) throws JSONException {
        Intent thisintent = getIntent();
        String monumentName = thisintent.getStringExtra("Selected Value");

        JSONArray submitList = new JSONArray();

        RadioGroup question1 =(RadioGroup)findViewById(R.id.radio_group_question_1);
        RadioGroup question2 =(RadioGroup)findViewById(R.id.radio_group_question_2);
        RadioGroup question3 =(RadioGroup)findViewById(R.id.radio_group_question_3);

        RadioButton radioButton1A = (RadioButton)findViewById(R.id.question_1A) ;
        RadioButton radioButton1B = (RadioButton)findViewById(R.id.question_1B) ;
        RadioButton radioButton1C = (RadioButton)findViewById(R.id.question_1C) ;

        RadioButton radioButton2A = (RadioButton)findViewById(R.id.question_2A) ;
        RadioButton radioButton2B = (RadioButton)findViewById(R.id.question_2B) ;
        RadioButton radioButton2C = (RadioButton)findViewById(R.id.question_2C) ;

        RadioButton radioButton3A = (RadioButton)findViewById(R.id.question_3A) ;
        RadioButton radioButton3B = (RadioButton)findViewById(R.id.question_3B) ;
        RadioButton radioButton3C = (RadioButton)findViewById(R.id.question_3C) ;


        if(question1.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Falta Responder a pergunta 1 do Quiz de " + monumentName, Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (radioButton1A.isChecked()){
                submitList.put(0 , radioButton1A.getText().toString());
            }
            if (radioButton1B.isChecked()){
                submitList.put(0 , radioButton1B.getText().toString());
            }
            if (radioButton1C.isChecked()){
                submitList.put(0 , radioButton1C.getText().toString());
            }
        }
        if(question2.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Falta Responder a pergunta 2 do Quiz de " + monumentName, Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (radioButton2A.isChecked()){
                submitList.put(1 , radioButton2A.getText().toString());
            }
            if (radioButton2B.isChecked()){
                submitList.put(1 , radioButton2B.getText().toString());
            }
            if (radioButton2C.isChecked()){
                submitList.put(1 , radioButton2C.getText().toString());
            }
        }
        if(question3.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Falta Responder a pergunta 3 do Quiz de " + monumentName, Toast.LENGTH_SHORT).show();
            return;
        }else{
            if (radioButton3A.isChecked()){
                submitList.put(2 , radioButton3A.getText().toString());
            }
            if (radioButton3B.isChecked()){
                submitList.put(2 , radioButton3B.getText().toString());
            }
            if (radioButton3C.isChecked()){
                submitList.put(2 , radioButton3C.getText().toString());
            }
        }

        Toast.makeText(this, "A Submeter Quiz de " + monumentName, Toast.LENGTH_SHORT).show();
        System.out.println(submitList);

        UserRequest userRequest = UserRequest.SUBMIT_QUIZ;
        JSONObject message = new JSONObject();

        try {
            message.put(NetworkKey.REQUEST_TYPE.toString(), userRequest.ordinal());
            message.put(NetworkKey.USERNAME.toString(), mHopOnCMUApplication.getUsername());
            message.put(NetworkKey.MONUMENT_NAME.toString(), monumentName);
            message.put(NetworkKey.USER_ANSWERS.toString(), submitList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ClientProxy clientProxy = new ClientProxy(userRequest, _handler ,NetworkMsg.GET_MONUMENT_LIST , message);
        new Thread(clientProxy).start();

        //  }
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
