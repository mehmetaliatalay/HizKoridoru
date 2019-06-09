package mehmetali.com.hizkoridoru;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mehmetali.com.hizkoridoru.Model.SeyahatBilgileri;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class DriveActivity extends RuntimePermissionsActivity {

    private TextView kalanSure;
    private static int PERMISSIONS = 50;
    private SpeedView speedView;
    private CountDownTimer countDownTimer = null;
    private Location mLastLocation = null;
    private long remainingTime = 0;
    private ProgressDialog dialog = null;
    private List<Float> speedList;
    private boolean isActivityRunning;
    private FusedLocationProviderClient locationProviderClient = null;
    private LocationCallback locationCallback = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        createNotificationChannel();
        defUIComponents();
        countDownTimer();
    }

    //Activity geri çağrıldığında yapılacak işlemler
    @Override
    protected void onResume() {
        super.onResume();
        isActivityRunning = true;
    }

    //Activity yok edildiğinde yapılacak işlemler.
    //GPS servisinin durdurulması
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("sadfgh", "OnDestroy");
        isActivityRunning = false;
        if (locationProviderClient != null) {
            locationProviderClient.removeLocationUpdates(locationCallback);
        }
    }


    //Geri butonuna basıldığında GPS servisinin durdurulması
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("sadfgh", "onBackPressed");
        isActivityRunning = false;
        if (locationProviderClient != null) {
            locationProviderClient.removeLocationUpdates(locationCallback);
        }
        finish();
    }

    //Sistem kaynaklarında erişim izni alındıktan sonra ortalama hız hesabının başlatılması
    @Override
    public void izinVerildi(int requestCode) {
        if (requestCode == PERMISSIONS) {
            getLocationManager();
            new ProgressBar().execute();
            new CheckAverageSpeed().execute();
        }
    }

    //Arayüz elemanlarının tanımlanması
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void defUIComponents() {

        speedView = findViewById(R.id.speedView);
        kalanSure = findViewById(R.id.txt_Kalan_Sure);
        TextView aracTipi = findViewById(R.id.txt_Arac_Tipi);
        TextView hizLimiti = findViewById(R.id.txt_Arac_Hiz_Limiti);
        TextView otoyol = findViewById(R.id.txt_Seyahat_Otoyol);
        TextView girisGise = findViewById(R.id.txt_Giris_Gise);
        TextView cikisGise = findViewById(R.id.txt_Cikis_Gise);
        TextView mesafe = findViewById(R.id.txt_Mesafe);
        TextView fiyat = findViewById(R.id.txt_Fiyat);
        final Button baslat = findViewById(R.id.btn_Baslat);
        speedList = new ArrayList<>();

        kalanSure.setText(getString(R.string.kalan_sure) + " " + TimeFormat.getFormat(SeyahatBilgileri.sure));
        aracTipi.setText(getString(R.string.arac_tipi) + " " + SeyahatBilgileri.aracTipi);
        hizLimiti.setText(getString(R.string.arac_hiz_limiti) + " " + String.valueOf(SeyahatBilgileri.hizLimiti) + " KM/H");
        otoyol.setText(getString(R.string.otoyol) + " " + SeyahatBilgileri.otoyol);
        girisGise.setText(getString(R.string.giris_gise) + " " + SeyahatBilgileri.girisGise);
        cikisGise.setText(getString(R.string.cikis_gise) + " " + SeyahatBilgileri.cikisGise);
        mesafe.setText(getString(R.string.mesafe) + " " + String.format("%.1f", SeyahatBilgileri.mesafe) + " KM");
        fiyat.setText(getString(R.string.fiyat) + " " + SeyahatBilgileri.fiyat + " TL");
        speedView.setMaxSpeed(SeyahatBilgileri.hizLimiti + 20);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };


        baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissions();
                baslat.setClickable(false);
                baslat.setBackground(getDrawable(R.drawable.button_pressed_background));

            }
        });


    }

    //Gerekli izinlerin istenmesi
    private void getPermissions() {

        String[] istenilenIzinler = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        DriveActivity.super.izinIste(istenilenIzinler, PERMISSIONS);
    }

    //GPS servisinin tanımlanması
    @SuppressLint("MissingPermission")
    private synchronized void getLocationManager() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000L);
        mLocationRequest.setFastestInterval(1000L);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        locationProviderClient = getFusedLocationProviderClient(this);


        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

    }

    //GPS servisinden veri alındığında hızın hesaplanması
    @SuppressLint("DefaultLocale")
    public synchronized void onLocationChanged(Location location) {

        if (location.hasSpeed()) {

            speedView.speedTo(location.getSpeed() * 3.6f, 1500L);
            Log.i("sadfgh", "hasSpeed" + "-------" + location.getSpeed() * 3.6f);
            Toast.makeText(this, "hasSpeed" + "-------" + location.getSpeed() * 3.6f, Toast.LENGTH_SHORT).show();
            speedList.add(location.getSpeed());

        } else if (mLastLocation != null) {

            long elapsedTime = TimeUnit.MILLISECONDS.toSeconds(location.getTime() - mLastLocation.getTime());
            float speed = mLastLocation.distanceTo(location) / (float) (elapsedTime);

            Log.i("sadfgh", "hesaplanan hız" + "-------" + speed * 3.6f);
            Toast.makeText(this, "hesaplanan hız" + "-------" + speed * 3.6f, Toast.LENGTH_SHORT).show();

            speedView.speedTo(speed * 3.6f, 1500L);
            speedList.add(speed);
        }

        this.mLastLocation = location;

    }

    //Sayacın başlatılması
    @SuppressLint("SetTextI18n")
    private void countDownTimer() {

        countDownTimer = new CountDownTimer(SeyahatBilgileri.sure, 1000) {
            @SuppressLint("SetTextI18n")
            public synchronized void onTick(long millisUntilFinished) {

                kalanSure.setText(getString(R.string.kalan_sure) + " " + TimeFormat.getFormat(millisUntilFinished));
                remainingTime = millisUntilFinished;
            }

            public synchronized void onFinish() {
                kalanSure.setText(getString(R.string.kalan_sure) + " " + TimeFormat.getFormat(0));
                remainingTime = 0L;
            }
        };
    }

    //Kullanıcıya uyarı gönderilmesi
    private void sendNotification(Float speed) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent intent = new Intent(this, DriveActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            @SuppressLint("DefaultLocale") NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_notifications_active)
                    .setContentTitle("DİKKAT!!!!!")
                    .setContentText("Ortalama hızınız :" + String.format("%.1f", speed * 3.6f) + " Lütfen yavaşlayınız.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, mBuilder.build());
        } else {

            Intent intent = new Intent(this, DriveActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            @SuppressLint("DefaultLocale") NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_notifications_active)
                    .setContentTitle("DİKKAT!!!!!")
                    .setContentText("Ortalama hızınız :" + String.format("%.1f", speed * 3.6f) + " Lütfen yavaşlayınız.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(2, mBuilder.build());
        }


    }

    //Uyarı gönderilecek kanalın tanımlanması
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //Proggres bar ile ilgili işlemler
    @SuppressLint("StaticFieldLeak")
    class ProgressBar extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DriveActivity.this);
            dialog.setMessage("Gps bilgileri alınıyor...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            countDownTimer.start();
        }
    }

    //Ortalama hızın hesaplanması ve limit kontrolü
    @SuppressLint("StaticFieldLeak")
    class CheckAverageSpeed extends AsyncTask<Void, Float, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    float averageSpeed = 0.0f;
                    while (isActivityRunning) {
                        try {
                            Thread.sleep(SeyahatBilgileri.sure / 4);

                            for (Float speed : speedList) {
                                averageSpeed = averageSpeed + speed;
                            }
                            averageSpeed = averageSpeed / speedList.size();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publishProgress(averageSpeed);

                        if (remainingTime == 0) {
                            break;
                        }
                    }
                }
            }).run();
            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {

            float coveredDistance = (float) (TimeUnit.MILLISECONDS.toSeconds(SeyahatBilgileri.sure - remainingTime)) * values[0];
            float distanceToCover = (float) (TimeUnit.MILLISECONDS.toSeconds(remainingTime)) * values[0];

            if (SeyahatBilgileri.mesafe < ((distanceToCover / 1000.0f) + (coveredDistance / 1000.0f))) {
                sendNotification(values[0]);
            }

        }
    }


}
