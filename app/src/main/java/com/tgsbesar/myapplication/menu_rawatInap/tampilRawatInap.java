package com.tgsbesar.myapplication.menu_rawatInap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.model.KelasKamar;

import java.util.Random;

public class tampilRawatInap extends AppCompatActivity {
    String no_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rawat_inap);

        KelasKamar kmr = (KelasKamar) getIntent().getSerializableExtra("KelasKamar");
        String tanggal = getIntent().getStringExtra("Tanggal");
       // String no_book = getIntent().getStringExtra("no_book");

        TextView txt_TipeKamar = (TextView) findViewById(R.id.txtKelasKamar);
        TextView txt_Fasilitas = (TextView) findViewById(R.id.txtFasilitas);
        TextView txt_Harga = (TextView)findViewById(R.id.txtHarga) ;
        TextView txt_tanggal = (TextView) findViewById(R.id.txtTanggal);
        TextView txt_noBooking = (TextView) findViewById(R.id.txtNoBookingRI);


        txt_TipeKamar.setText(kmr.tipe_kamar);
        txt_Fasilitas.setText(kmr.fasilitas_kamar);
        txt_Harga.setText(String.valueOf(kmr.harga_kamar));
        txt_tanggal.setText(tanggal);

        Button btn_save = (Button)findViewById(R.id.buttonSendRI);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilRawatInap.this, MainActivity.class);
                createNotificationChannel();
                addNotification();
                startActivity(i);
            }
        });

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private String CHANNEL_ID = "Channel 1";

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Channel 1";

            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void addNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_background)
                .setContentTitle("Pendaftaran Rawat Inap Sukses!")
                .setContentText("No Booking anda "+no_booking)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


}