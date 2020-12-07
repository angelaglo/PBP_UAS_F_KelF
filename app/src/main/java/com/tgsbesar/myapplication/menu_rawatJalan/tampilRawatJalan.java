package com.tgsbesar.myapplication.menu_rawatJalan;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;

import java.util.Random;

public class tampilRawatJalan extends AppCompatActivity {
    private String nama_dr, jabatan_dr, jam_rj, tanggal_rj;
    private String no_antrian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rawat_jalan);

        Dokter dtr = (Dokter) getIntent().getSerializableExtra("Dokter");
        String jam = getIntent().getStringExtra("Jam");
        String tanggal = getIntent().getStringExtra("Tanggal");
        String no_urut = getIntent().getStringExtra("no_urut");

        TextView txt_spesialis = (TextView) findViewById(R.id.txtSpesialis);
        TextView txt_dokter = (TextView) findViewById(R.id.txtDokter);
        TextView txt_tanggal = (TextView) findViewById(R.id.txtTanggal);
        TextView txt_jam = (TextView) findViewById(R.id.txtJam);
        TextView txt_noBooking = (TextView) findViewById(R.id.txtNoUrut);

        txt_spesialis.setText(dtr.spesialis);
        txt_dokter.setText(dtr.nama);
        txt_tanggal.setText(tanggal);
        txt_jam.setText(jam);
        txt_noBooking.setText(no_urut);

        nama_dr=dtr.nama;
        jabatan_dr=dtr.spesialis;
        tanggal_rj=tanggal;
        jam_rj=jam;
        no_antrian=no_urut;
        Button btn_save = (Button)findViewById(R.id.buttonSendRJ);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilRawatJalan.this, MainActivity.class);
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
                .setContentTitle("Pendaftaran Rawat Jalan Sukses!")
                .setContentText("No Antrian: "+no_antrian+" Pemeriksaan dengan "+nama_dr)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


}