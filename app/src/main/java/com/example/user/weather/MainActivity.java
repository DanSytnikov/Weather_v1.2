package com.example.user.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.weather.respModel.Data;

import java.lang.ref.WeakReference;
import java.util.Date;


import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmCollection;
import io.realm.RealmResults;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    public RetroInterface retroInterface;
    public static WeakReference<MainActivity> activity;
    private Retrofit retrofit;
    public Example data;
    public java.util.List<Data> dataData;
    public double aLat;
    public double aLon;
    private LocationManager locationManager;
    private Realm realm;
    TextView tv;


    MyBroadcastReceiver receiver;


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("PERMISSION", "granted");    // permission granted
                } else {
                    Log.e("PERMISSION", "denied");
                    finish();   //permission denied
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        receiver = new MyBroadcastReceiver();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

//        retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.openweathermap.org") //Базовая часть адреса
//                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
//                .build();
//        retroInterface = retrofit.create(RetroInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        /*locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 10,
                locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 10, locationListener);
        checkEnabled();*/
        tv  = findViewById(R.id.textView);
        final RealmResults<Example> realmResult = realm.where(Example.class).findAll();
        realmResult.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Example>>(){

            @Override
            public void onChange(RealmResults<Example> examples, OrderedCollectionChangeSet changeSet) {
                tv.setText(realmResult.);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        locationManager.removeUpdates(locationListener);
    }

    /*private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            *//*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            showLocation(locationManager.getLastKnownLocation(provider));*//*
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                showToast("GPSStatus: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                showToast("NETStatus: " + String.valueOf(status));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                showLocation(locationManager.getLastKnownLocation(provider));
            }
        }
    };*/

   /* private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            TextView tv = findViewById(R.id.textView);
            tv.setText(formatLocation(location));
            aLat = location.getLatitude();
            aLon = location.getLongitude();
        }
    }*/

    @SuppressLint("DefaultLocale")
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    /*private void checkEnabled() {
        Log.e("GPS", "Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        Log.e("NETWORK", "Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }*/


    public void showToast(String tstmsg) {
        Toast toast = Toast.makeText(getApplicationContext(), tstmsg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void onClick(View view) {
        startService(new Intent(this, MyIntentService.class));
        tv = findViewById(R.id.textView);
        Example realmResults = realm.where(Example.class).findFirst();
        if(realmResults != null) {
            String cityname = realmResults.getCity().getName();
            if (cityname != null) {
                tv.setText(cityname);
            }
        }
        Log.e("AAAAAAAAAAAAAAAAAAAA", "CTIVITY STARTED");
//        GetOpenWeather gow = new GetOpenWeather(this);
//        gow.execute();
        Log.e("RESPONSE", "");

    }

//    @SuppressLint("StaticFieldLeak")
//    class GetOpenWeather extends Realm {
//        private WeakReference<MainActivity> activity;
//
//
//        public GetOpenWeather(MainActivity activity) {
//            this.activity = new WeakReference<>(activity);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            MainActivity main = activity.get();
//            if (main != null) {
//
//            }
//            ProgressBar pg = findViewById(R.id.pb);
//            pg.setVisibility(View.VISIBLE);
//
//        }
//
//
//        @Override
//        protected Response<Example> doInBackground(Void... voids) {
//            try {
//                Log.d("RUN", "Thread started.");
//                Call<Example> responseCall = retroInterface.getWeather(String.valueOf(46.469391), String.valueOf(30.740883), "a7c76f80c9580699351007ff55f2e86d");
//                Response<Example> res = responseCall.execute();
//                Log.d("RUN", "Resp result: " + res.code());
//                return res;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Response<Example> resp) {
//            super.onPostExecute(resp);
//            ProgressBar pg = findViewById(R.id.pb);
//            pg.setVisibility(View.INVISIBLE);
//            data = resp.body();
//            dataData = data.getData();
////            RecyclerView list = activity.get().findViewById(R.id.list);
////            list.setAdapter(new WeatherAdapter(v.getData()));
//            if (data.getCod().equals("200")) {
////                showToast(data.getCity().getName());
//                TextView tv = findViewById(R.id.textView);
//                tv.setText("" + data.getCity().getName() + "Temp: " + data.getData().get(0).getMain().getTemp());
//
//                {
//                    System.out.println(
//                            data.getCity().getName() + " " + dataData.get(0).getDtTxt()+""
//                    );
//                }
//
//            } else {
//                showToast("Lost Connection...");
//            }
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}