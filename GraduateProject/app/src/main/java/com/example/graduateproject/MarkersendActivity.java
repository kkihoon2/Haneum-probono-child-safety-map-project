package com.example.graduateproject;
import java.net.HttpURLConnection;

import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.net.*;

import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MarkersendActivity extends AppCompatActivity {

    Button starttosend;
    marker sendmarker;
    JwtInform jwt;
    String json;
    String parsingNickName;
    boolean isLogin;
    public String setParsingNickName(String json)
    {
        try{
            JSONObject jsonObject =new JSONObject(json);

            String parsingNickName = jsonObject.getString("nickname");
            System.out.println(parsingNickName);
            return parsingNickName;

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "setParsingNickName error";
    }

    public String setJwt(JwtInform jwt)
    {
        HttpURLConnection hc;
        String result="";

        try {
            String authorization = "Bearer "+jwt.getAccessToken();
            URL url = new URL("http://10.210.8.130:8080/member/me");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //????????????
            connection.setDoOutput(false);       //???????????? ??? ??? ??????
            connection.setDoInput(true);

            connection.setRequestProperty("Authorization", authorization);//???????????? ???????????? ??????

            InputStream is = connection.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            while((result = br.readLine())!=null){
                sb.append(result+"\n");
            }

            result = sb.toString();

        }catch(MalformedURLException m){
            Log.e("MalformedURLException??????","MalformedURLException??????");
        }
        catch(IOException e)
        {
            Log.e("IOException??????","IOException??????");
            e.printStackTrace();
        }catch(NullPointerException e)
        {
            Log.e("NullPointerException??????","NullPointerException??????");
        }
        System.out.println("tokensendresult2"+result);
        return result;
    }
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_markersend);
        TextView poster_nickname = (TextView) findViewById(R.id.poster_nickname);
       // EditText is_danger = (EditText) findViewById(R.id.is_danger);

        Spinner categorySpinner = (Spinner)findViewById(R.id.categorySpinner);

        RadioButton safeButton = (RadioButton)findViewById(R.id.safeButton);
        RadioButton dangerButton = (RadioButton)findViewById(R.id.dangerButton);
        EditText marker_inform = (EditText) findViewById(R.id.marker_inform);

       // TextView  marker_latitude = (TextView) findViewById(R.id.marker_latitude);
       // TextView marker_longitude = (TextView) findViewById(R.id.marker_longitude);
       // EditText marker_category = (EditText) findViewById(R.id.marker_catergory);
        EditText image_url = (EditText) findViewById(R.id.image_url);

        starttosend = findViewById(R.id.starttosend);
        Intent intent = getIntent();
        JwtInform jwt = (JwtInform)intent.getSerializableExtra("jwt");
        Double intent_latitude = intent.getDoubleExtra("latitude", 0);
        Double intent_longitude = intent.getDoubleExtra("longitude", 0);
        boolean ismarkersend = true;

        json = setJwt(jwt);
        parsingNickName=setParsingNickName(json);
        System.out.println("json: "+json);
        poster_nickname.setText(parsingNickName);
        starttosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = poster_nickname.getText().toString();
                String category=categorySpinner.getSelectedItem().toString();
                String inform = marker_inform.getText().toString();
                String url = image_url.getText().toString();
                int isDanger = -1;
                if(dangerButton.isChecked()==true)
                {
                    isDanger = 1;
                }
                else if(safeButton.isChecked()==true)
                {
                    isDanger=0;
                }
                else{
                    Toast.makeText(MarkersendActivity.this,"?????? ???????????????",Toast.LENGTH_SHORT).show();
                }
//                sendmarker.setMarkerInform(inform);
//                sendmarker.setImageUrl(url);
//                sendmarker.setMarkerLatitude(intent_latitude);
//                sendmarker.setMarkerLongitude(intent_longitude);
//                sendmarker.setPosterNickName(nickname);
//                sendmarker.setMarkerCategory(category);
                sendmarker = new marker(isDanger,inform,url,intent_latitude,intent_longitude,parsingNickName,category);
               Intent intent2 = new Intent(MarkersendActivity.this,MainActivity.class);
               //intent2.putExtra("sendmarker",sendmarker);
               //intent2.putExtra("ismarkersend",ismarkersend);

               Map<String,Integer>categoryMap = new HashMap<>();
               categoryMap.put("?????????",1);
               categoryMap.put("????????????????????????",2);
               categoryMap.put("?????????????????????",3);
               categoryMap.put("?????????????????????",4);
               categoryMap.put("??????????????????",5);
               categoryMap.put("?????????",6);
               categoryMap.put("????????????",7);
               categoryMap.put("?????????",8);
                categoryMap.put("?????????",9);
                categoryMap.put("?????????",10);
                JSONObject mark = new JSONObject();

               try{
                   mark.put("markerCategory",categoryMap.get(sendmarker.getMarkerCategory().toString()));
                   mark.put("markerInform",sendmarker.getMarkerInform());
                   mark.put("isDanger",sendmarker.getIsDanger().toString());
                   mark.put("imageUrl",sendmarker.getImageUrl());
                   mark.put("markerLongitude",intent_longitude.toString());
                   mark.put("markerLatitude",intent_latitude.toString());
                   mark.put("posterNickName",sendmarker.getPosterNickName());

               }catch (JSONException e)
               {
                    System.out.println(e.getCause());
               }

               System.out.println("send"+mark);

                try {
                    //--------------------------
                    //   URL ???????????? ????????????
                    //--------------------------
                    URL url2 = new URL("http://10.210.8.130:8080/api/marker/");       // URL ??????
                    HttpURLConnection http = (HttpURLConnection) url2.openConnection();   // ??????
                    //--------------------------
                    //   ?????? ?????? ?????? - ???????????? ????????????
                    //--------------------------
                    http.setRequestMethod("POST");

                    http.setDoInput(true);                         // ???????????? ?????? ?????? ??????
                    http.setDoOutput(true);                       // ????????? ?????? ?????? ??????
                             // ?????? ????????? POST

                    // ???????????? ????????? <Form>?????? ?????? ????????? ?????? ?????? ???????????? ??????????????? ??? ????????????
                    http.setRequestProperty("content-type", "application/json");
                    http.setRequestProperty("Accept", " application/json");
                    //--------------------------
                    //   ????????? ??? ??????
                    //--------------------------
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(http.getOutputStream()));
                     bw.write(mark.toString());
                     bw.flush();
                     bw.close();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {       // ???????????? ??????????????? ????????? ???????????? ??????????????? ?????????
                        builder.append(str + "\n");                     // View??? ???????????? ?????? ?????? ????????? ??????
                    }
                    String myResult = builder.toString();                       // ??????????????? ?????? ????????? ??????
                    System.out.println(myResult);


                }
                catch (IOException ie)
                {
                    System.out.println("io"+ie.getCause());
                    ie.printStackTrace();
                }catch(Exception ee)
                {
                    System.out.println("e"+ee.getCause());
                    ee.printStackTrace();
                }
                isLogin=true;
                intent2.putExtra("isLogin",isLogin);
                intent2.putExtra("jwt",jwt);
                startActivity(intent2);

            }

        });









    }
}

