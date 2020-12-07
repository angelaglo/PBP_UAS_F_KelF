package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_rawatJalan.DaftarDokter;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.menu_rawatJalan.DokterAdapter;
import com.tgsbesar.myapplication.model.Laboratorium;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class tampilLaboratorium extends AppCompatActivity {
    Integer no_booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_laboratorium);

        Laboratorium lab= (Laboratorium) getIntent().getSerializableExtra("Laboratorium");
        String jam = getIntent().getStringExtra("Jam");
        String tanggal = getIntent().getStringExtra("Tanggal");
     //    no_booking = Integer.valueOf(getIntent().getStringExtra("id"));



        TextView txt_PaketCheckUp = (TextView) findViewById(R.id.txtPaketCheckUp);
        TextView txt_Deskripsi = (TextView) findViewById(R.id.txtDeskripsi);
        TextView txt_harga = (TextView)findViewById(R.id.hargaPaket);
        TextView txt_tanggalCheckUp = (TextView) findViewById(R.id.txtTanggalCheckUp);
        TextView txt_jamCheckUp = (TextView) findViewById(R.id.txtJamCheckUp);
        TextView txt_noBooking = (TextView) findViewById(R.id.txtNoBooking);

        txt_PaketCheckUp.setText(lab.kategori);
        txt_Deskripsi.setText(lab.deskripsi);
        txt_harga.setText(String.valueOf(lab.harga_test));
        txt_tanggalCheckUp.setText(tanggal);
        txt_jamCheckUp.setText(jam);
//        txt_noBooking.setText(no_book);
//
//        no_booking=no_book;
        Button btn_save = (Button)findViewById(R.id.buttonSendLab);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilLaboratorium.this, MainActivity.class);
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
                .setContentTitle("Pendaftaran Check Up Sukses!")
                .setContentText("No Booking anda "+no_booking)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


}