package com.example.graduateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {
    Button joinButton;
    Button checkId;
    Button checkNickName;
    Boolean checkIdbool;
    Boolean checkNickNamebool;
    String id , nickname , pw;
    CheckBox isChild;
    CheckBox isParent;
    int childparent;
    JwtInform jwt;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        joinButton = findViewById(R.id.join);
        checkId=findViewById(R.id.check_Id);
        checkNickName=findViewById(R.id.check_Nickname);

        checkIdbool = true;
        checkNickNamebool = true;
        EditText member_id = (EditText) findViewById(R.id.member_id);
        EditText member_pw = (EditText) findViewById(R.id.member_pw);
        EditText member_nickname = (EditText) findViewById(R.id.member_nickname);






        checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result ="";
                String str1 = "false";
                String str2 = "true";
                id = member_id.getText().toString();
                try {
                    String checkIdUrl="http://192.168.219.101:8080/auth/memberId/"+id+"/exists";
                    URL url = new URL("http://10.210.8.130:8080/auth/memberId/"+id+"/exists");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //????????????
                    connection.setDoOutput(false);       //???????????? ??? ??? ??????
                    connection.setDoInput(true);        //???????????? ???????????? ??????

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();
                    System.out.println("ID???????????? get:"+result);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkIdbool=Boolean.parseBoolean(result);
                if(checkIdbool==false)
                {
                    Toast.makeText(getApplicationContext(), "??????????????? ID?????????.", Toast.LENGTH_LONG).show();

                }else if(checkIdbool==true)
                {
                    Toast.makeText(getApplicationContext(), "?????? ???????????? ID????????????.", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_LONG).show();
                }

            }
        });
        checkNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = "";
                nickname = member_nickname.getText().toString();
                try {
                    String checkIdUrl="http://192.168.219.101:8080/auth/nickname/"+nickname+"/exists";
                    URL url = new URL("http://10.210.8.130:8080/auth/nickname/"+nickname+"/exists");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //????????????
                    connection.setDoOutput(false);       //???????????? ??? ??? ??????
                    connection.setDoInput(true);        //???????????? ???????????? ??????

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkNickNamebool=Boolean.getBoolean(result);
                if(checkNickNamebool == false)
                {

                    Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.", Toast.LENGTH_LONG).show();
                }else if(checkNickNamebool == true)
                {

                    Toast.makeText(getApplicationContext(), "?????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                }



            }
        });



        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // id test ?????????
                String result ="";
                String str1 = "false";
                String str2 = "true";
                id = member_id.getText().toString();
                try {
                    String checkIdUrl="http://192.168.219.101:8080/auth/memberId/"+id+"/exists";
                    URL url = new URL("http://10.210.8.130:8080/auth/memberId/"+id+"/exists");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //????????????
                    connection.setDoOutput(false);       //???????????? ??? ??? ??????
                    connection.setDoInput(true);        //???????????? ???????????? ??????

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();
                    System.out.println("ID???????????? get:"+result);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkIdbool=Boolean.parseBoolean(result);
                if(checkIdbool==false)
                {
//                    Toast.makeText(getApplicationContext(), "??????????????? ID?????????.", Toast.LENGTH_LONG).show();

                }else if(checkIdbool==true)
                {
                    Toast.makeText(getApplicationContext(), "?????? ???????????? ID????????????.", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_LONG).show();
                }
                //nickname test ?????????
                result ="";
                nickname = member_nickname.getText().toString();
                try {
                    String checkIdUrl="http://192.168.219.101:8080/auth/nickname/"+nickname+"/exists";
                    URL url = new URL("http://10.210.8.130:8080/auth/nickname/"+nickname+"/exists");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //????????????
                    connection.setDoOutput(false);       //???????????? ??? ??? ??????
                    connection.setDoInput(true);        //???????????? ???????????? ??????

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }

                    result = sb.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkNickNamebool=Boolean.getBoolean(result);
                if(checkNickNamebool == false)
                {

                    Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.", Toast.LENGTH_LONG).show();
                }else if(checkNickNamebool == true)
                {

                    Toast.makeText(getApplicationContext(), "?????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                }




                if(checkIdbool == false && checkNickNamebool == false) {
                    JSONObject member = new JSONObject();
                    isChild = (CheckBox) findViewById(R.id.ischild) ;
                    isParent = (CheckBox) findViewById(R.id.isparent);
                    if(isChild.isChecked()==true)
                    {
                        childparent = 1;
                    }else if (isParent.isChecked()==true)
                    {
                        childparent = 2;
                    }
                    pw = member_pw.getText().toString();
                    try {
                        member.put("memberId", id.toString());
                        member.put("password", pw.toString());
                        member.put("nickname", nickname.toString());
                        member.put("memberRole",childparent);
                        System.out.println("send" + member);


                    } catch (JSONException e) {
                        System.out.println(e.getCause());
                    }

                    try {
                        //--------------------------
                        //   URL ???????????? ????????????
                        //--------------------------
                        URL url2 = new URL("http://10.210.8.130:8080/auth/signup");       // URL ??????
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
                        bw.write(member.toString());
                        bw.flush();
                        bw.close();

                        InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuilder builder = new StringBuilder();
                        String str;
                        while ((str = reader.readLine()) != null) {       // ???????????? ??????????????? ????????? ???????????? ??????????????? ?????????
                            builder.append(str + "\n");                     // View??? ???????????? ?????? ?????? ????????? ??????
                        }
                        String myResult = builder.toString();                       // ??????????????? ?????? ????????? ??????
                        System.out.println(myResult);
                        Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(JoinActivity.this,MainActivity.class);
                        startActivity(intent2);



                    } catch (IOException ie) {
                        System.out.println("io" + ie.getCause());
                        ie.printStackTrace();
                    } catch (Exception ee) {
                        System.out.println("e" + ee.getCause());
                        ee.printStackTrace();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}