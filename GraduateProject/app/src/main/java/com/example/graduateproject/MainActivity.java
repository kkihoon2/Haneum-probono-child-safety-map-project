package com.example.graduateproject;
import com.example.graduateproject.marker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private boolean isLogin;
    private Button loginbtn;
    private Button logoutbtn;
    private Button markersend;
    private Button markerget;
    private Button showallmarker;
    long expireInCompare;
    private boolean ismarkersend = false;

    JwtInform jwt;
//    double mLatitude , mLongitude;
//    GPSListener gpsListener;
//    LocationManager manager;


    ArrayList<marker> markerList= new ArrayList<marker>();

        public String run() {
            String result = "";
            try {
                URL url = new URL("http://10.210.8.130:8080/api/marker/");
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
            return result;
        }


    private void jsonParsing(String json)
    {
        try{
            JSONArray markerArray = new JSONArray(json);
            //JSONObject jsonObject = new JSONObject(json);
            Log.e("d","??????????????????22222");
            //JSONArray markerArray = jsonObject.getJSONArray(json);

            for(int i=0; i<markerArray.length(); i++)
            {
                JSONObject markerObject = markerArray.getJSONObject(i);

                marker marker = new marker();

                marker.setMarkerInform(markerObject.getString("markerInform"));
                marker.setMarkerLongitude(markerObject.getDouble("markerLongitude"));
                marker.setMarkerLatitude(markerObject.getDouble("markerLatitude"));
                marker.setIsDanger(markerObject.getInt("isDanger"));
                marker.setPosterNickName(markerObject.getString("posterNickName"));
                marker.setImageUrl(markerObject.getString("imageUrl"));
                marker.setMarkerCategory(markerObject.getString("markerCategory"));
                marker.setMarkerId(markerObject.getInt("markerId"));
                markerList.add(marker);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent getJwt = getIntent();

        isLogin = getIntent().getBooleanExtra("isLogin",false);
        jwt = (JwtInform) getJwt.getSerializableExtra("jwt");
        try {
            System.out.println("main jwt ??????"+jwt.getAccessToken());
        }catch(NullPointerException e)
        {
            System.out.println("main jwt token null");
        }
        long now = System.currentTimeMillis();
        Calendar cal = new GregorianCalendar();
        System.out.println(now);
        System.out.println(cal.getTimeInMillis());
        if(isLogin == true)
        {

            expireInCompare=jwt.getTokenExpiresIn();

        }
        System.out.println(cal.getTimeInMillis()+"vs"+expireInCompare);


        if(now>expireInCompare)
        {
            isLogin=false;
        }
        //markerget = findViewById(R.id.markerget);
        //markersend= findViewById(R.id.markersend);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //jsonParsing(getJsonString());
        //getJsonString();

        jsonParsing(run());


        loginbtn = findViewById(R.id.loginbtn);
        logoutbtn = findViewById(R.id.logoutbtn);
//        showallmarker=findViewById(R.id.showallmarker);
        if(isLogin == true){
            loginbtn.setVisibility(View.GONE);
            logoutbtn.setVisibility(View.VISIBLE);
        }else{
            logoutbtn.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        ismarkersend=intent.getBooleanExtra("ismarkersend",ismarkersend);
        if(ismarkersend==true){

            marker sendmarker = (marker)intent.getSerializableExtra("sendmarker");
            markerList.add(sendmarker);
            ismarkersend=false;
        }



        /*if(ismarkersend == true){
            //marker ????????? ?????? ??????
        }*/
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                startActivity(intent);//???????????? ??????

            }

        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jwt.setAccessToken(null);
                jwt.setGrantType(null);
                jwt.setTokenExpiresIn(0);
                isLogin = false;
                finish();//????????? ??????
                overridePendingTransition(0, 0);//????????? ?????? ?????????
                Intent intent = getIntent(); //?????????
                intent.putExtra("isLogin",isLogin);
                startActivity(intent); //???????????? ??????
                overridePendingTransition(0, 0);//????????? ?????? ?????????
            }

        });


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
//?????? ?????? ?????? for???

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.safemarker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap safemarker = Bitmap.createScaledBitmap(b, 80, 80, false);
        BitmapDrawable bitmapdraw1=(BitmapDrawable)getResources().getDrawable(R.drawable.dangermarker);
        Bitmap b1=bitmapdraw1.getBitmap();
        Bitmap dangermarker = Bitmap.createScaledBitmap(b1, 80, 80, false);
        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.mymarker);
        Bitmap b2=bitmapdraw2.getBitmap();
        Bitmap mymarker = Bitmap.createScaledBitmap(b2, 50, 50, false);
        for (int idx = 0; idx < markerList.size() ; idx++) {
            // 1.?????? ?????? ?????? (????????? ??????)
            MarkerOptions makerOptions = new MarkerOptions();
            if(markerList.get(idx).getIsDanger()==1)
            {
                makerOptions // LatLng??? ?????? ???????????? ???????????? ????????? ?????? ??????.
                        .position(new LatLng(markerList.get(idx).getMarkerLatitude(), markerList.get(idx).getMarkerLongitude()))
                        .title("??????" + idx).icon(BitmapDescriptorFactory.fromBitmap(dangermarker)); //?????????.
            }else{
                makerOptions // LatLng??? ?????? ???????????? ???????????? ????????? ?????? ??????.
                        .position(new LatLng(markerList.get(idx).getMarkerLatitude(), markerList.get(idx).getMarkerLongitude()))
                        .title("??????" + idx).icon(BitmapDescriptorFactory.fromBitmap(safemarker)); //?????????.
            }


            // 2.?????? ?????? (????????? ?????????)
            mMap.addMarker(makerOptions);
        }
        //??? ????????? ?????? ?????? ????????? ?????? //
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng point) {
                if(isLogin==true)
                {
                    MarkerOptions mOptions = new MarkerOptions();
//?????? ?????????
                    mOptions.title("?????? ??????");
                    Double latitude = point.latitude;//??????
                    Double longitude = point.longitude;//??????

                    Intent intent = new Intent(MainActivity.this,MarkersendActivity.class);
                    intent.putExtra("jwt",jwt);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("marker",markerList);
                    startActivity(intent);//???????????? ??????
                }else
                {
                    Toast.makeText(getApplicationContext(), "??????????????? ?????? ???????????? ???????????????.", Toast.LENGTH_LONG).show();
                }
            }
        });
        MarkerOptions myMarker = new MarkerOptions();

        LatLng suwonUniv = new LatLng(37.2087029980, 126.9760273437);
        myMarker.position(suwonUniv).title("my").icon(BitmapDescriptorFactory.fromBitmap(mymarker));
        mMap.addMarker(myMarker);
        CircleOptions circle500 = new CircleOptions().center(suwonUniv).radius(100).strokeWidth(4f).strokeColor(Color.parseColor("#6699FF"));
        mMap.addCircle(circle500);

/* for (int i = 0; i<mark.length;i++) {
            LatLng mark2dddd = new LatLng((double)mark[i].getMarkerLatitude(),(double)mark[i].getMarkerLongitude());
            if(mark[i].getIsDanger()==1) {
                mMap.addMarker(new MarkerOptions().position(mark2dddd));
            }else{
                mMap.addMarker(new MarkerOptions().position(mark2dddd).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }*/



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(suwonUniv,15));
        for(int i=0;i<markerList.size();i++)
        {
            marker markDistance = (marker)markerList.get(i);
            double distance=getDistanceByDegree(37.2087029980,126.9760273437,markDistance.getMarkerLatitude(),markDistance.getMarkerLongitude());
            if(distance<100&&markDistance.getIsDanger()==1)
            {
                alarm();

                System.out.println("??????"+distance);
            }
        }
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker markerClick ) {


        int count = 0;

        Intent intent = new Intent(MainActivity.this, MarkergetActivity.class);
        Double latitude = markerClick.getPosition().latitude;
        Double longitude = markerClick.getPosition().longitude;
        for (marker marker : markerList) {
            if (marker.getMarkerLongitude() == longitude && marker.getMarkerLatitude() == latitude) {
                intent.putExtra("marker", marker);
            } else {
                count++;
            }

        }
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("jwt",jwt);
/*for (int i = 0; i<mark.length;i++) {
            if(mark[i].getMarkerLatitude()==latitude&&mark[i].getMarkerLongitude()==longitude)
            {intent.putExtra("mark", mark[i]);}
        }*/
        startActivity(intent);//???????????? ??????
        return true;
    }
    public double getDistanceByDegree(double latitude1,double longitude1, double latitude2, double longitude2)
    {
        Location startpos = new Location("PointA");
        Location endpos = new Location("PointA");
        startpos.setLatitude(latitude1);
        startpos.setLongitude(longitude1);
        endpos.setLatitude(latitude2);
        endpos.setLongitude(longitude2);

        double distance = startpos.distanceTo(endpos);
        return distance;

    }
    public void alarm()
    {
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Notification ????????? ??????????????? ??????????????? ??????(AlertDialog ??? ??????)
        NotificationCompat.Builder builder= null;

        //Oreo ??????(API26 ??????)??????????????? ???????????? NotificationChannel ????????? ????????? ?????? ??????????????? ???.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID="channel_01"; //???????????? ?????????
            String channelName="MyChannel01"; //??????????????? ??????(??????)

            //???????????? ?????? ?????????
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

            //????????????????????? ?????? ????????? ????????? ??????
            notificationManager.createNotificationChannel(channel);

            //??????????????? ?????? ??????
            builder=new NotificationCompat.Builder(this, channelID);


        }else{
            //?????? ????????? ?????? ??????
//            builder= new NotificationCompat.Builder(this, null);
            System.out.println("?????? ???????");
        }

        //??????????????? ????????? ????????? ????????????
        builder.setSmallIcon(android.R.drawable.ic_menu_view);

        //???????????? ??????????????? ????????? ????????? ?????????
        //?????????(?????? ?????????)??? ??????
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.brand1);
        builder.setLargeIcon(bm);//??????????????? Bitmap??? ????????????.
        builder.setContentTitle("????????? ?????? ??????");//????????? ??????
        builder.setContentText("???????????? ?????? ????????? ???????????????");//????????? ??????

        //???????????? ??? ?????????
//        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.gametitle_09);
//        builder.setLargeIcon(bm);//??????????????? Bitmap??? ????????????.

        //??????????????? ?????? ?????? ???????????????
        Notification notification=builder.build();

        //????????????????????? ??????(Notify) ??????
        notificationManager.notify(1, notification);

        //?????? ???????????? ????????? ????????? ???????????? ??? ??? ??????.
        //notificationManager.cancel(1);

    }


}
