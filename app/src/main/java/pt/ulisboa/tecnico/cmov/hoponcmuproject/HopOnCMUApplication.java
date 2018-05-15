package pt.ulisboa.tecnico.cmov.hoponcmuproject;


import android.app.Application;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by peolie on 13-04-2018.
 */

public class HopOnCMUApplication extends Application {


    private String  mUsername = "";
    private String mCode = "";

    private String mCountry = "";

    private JSONArray mtourList = new JSONArray();
    private JSONArray mMonumentsList = new JSONArray();
    private HashMap<String , JSONArray> tour_array = new HashMap<>();

    //Lista De Quizes e Mapa para as perguntas de cada Quiz
    private ArrayList<String> mQuizList = new ArrayList();
    private JSONArray mQuizInfo = new JSONArray();
    private HashMap<String, JSONArray> quiz_array = new HashMap<>();

    // Lista de Scores E Quizes Submetidos
    private HashMap<String, Integer> mQuiz_score = new HashMap<>();
    private HashMap<String, Integer> mTour_score = new HashMap<>();

    private ArrayList<String> mSubmitList = new ArrayList<>();

    public HashMap<String, Integer> getmQuiz_score() {
        return mQuiz_score;
    }public void addQuizscore(String key, int points ){
        mQuiz_score.put(key, points);
        return ;
    }public boolean checkIfSubmited(String monument){
        return mQuiz_score.keySet().contains(monument);
    }

    public void addTourScore(String key, int points){
        mQuiz_score.put(key, points);
        return;
    }

    public  JSONArray getTour_arrayKey(String Key){
        return tour_array.get(Key);
    }
    public void setTourArray(String key, JSONArray values){
        this.tour_array.put(key, values);
    }

    public void setQuizArray(String key, JSONArray values){
        this.quiz_array.put(key, values);
    }

    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getCode() {
        return mCode;
    }
    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public JSONArray getMonumentsList() { return mMonumentsList; }
    public void setMonumentList(JSONArray mMonumentsList) {
        this.mMonumentsList = mMonumentsList;    }


    public ArrayList<String> getQuizList() { return mQuizList; }
    public void addQuizList(String mQuizList) {
        this.mQuizList.add(mQuizList);    }


    public JSONArray getQuestions() { return mQuizInfo; }
    public void setQuestions(JSONArray mQuestions) {
        this.mQuizInfo = mQuestions;
    }

    public ArrayList<String> getSubmitList() { return mSubmitList; }
    public void setSubmitList(ArrayList<String> mSubmitList) { this.mSubmitList = mSubmitList;
    }

    public String getCountry(){return mCountry;}
    public void setCountry(String country) {
        this.mCountry = country;
    }

    public JSONArray getTourList(){return mtourList;}

    public void setTourList(JSONArray tourList) {
        this.mtourList = tourList;
    }


    public boolean checkTour_arrayKey(String key) {
        return this.tour_array.containsKey(key);
    }

    public HashMap<String,JSONArray> getQuiz_array() {
        return this.quiz_array;
    }

    public void removeTourScore(String tourName) {
        mTour_score.remove(tourName);
        return;
    }
}
