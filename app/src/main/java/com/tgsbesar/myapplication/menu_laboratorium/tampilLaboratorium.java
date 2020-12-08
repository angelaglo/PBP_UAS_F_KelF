package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.LaboratoriumAPI;
import com.tgsbesar.myapplication.API.transaksiLaboratoriumAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_rawatJalan.DaftarDokter;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.menu_rawatJalan.DokterAdapter;
import com.tgsbesar.myapplication.model.Laboratorium;
import com.tgsbesar.myapplication.model.transaksiLaboratorium;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.android.volley.Request.Method.GET;

public class tampilLaboratorium extends AppCompatActivity {
    Integer no_booking;
    String email="stevani@yy.com";
    private TextView txt_PaketCheckUp, txt_harga, txt_tanggalCheckUp, txt_jamCheckUp, txt_noBooking;

    List<transaksiLaboratorium> listTransaksi;
    transaksiLaboratorium transaksiLaboratorium;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_laboratorium);

     //   Laboratorium lab= (Laboratorium) getIntent().getSerializableExtra("Laboratorium");
     //   String jam = getIntent().getStringExtra("Jam");
     //   String tanggal = getIntent().getStringExtra("Tanggal");
     //    no_booking = Integer.valueOf(getIntent().getStringExtra("id"));


        getLaboratorium();
        txt_PaketCheckUp = (TextView) findViewById(R.id.txtPaketCheckUp);
        txt_harga = (TextView)findViewById(R.id.hargaPaket);
        txt_tanggalCheckUp = (TextView) findViewById(R.id.txtTanggalCheckUp);
        txt_jamCheckUp = (TextView) findViewById(R.id.txtJamCheckUp);
        txt_noBooking = (TextView)findViewById(R.id.txtNoBooking);

        Button btn_edit = (Button)findViewById(R.id.buttonEdit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilLaboratorium.this, editTransaksiLab.class);
                i.putExtra("id", txt_noBooking.getText().toString());
                startActivity(i);
            }
        });

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


    //mengambil data laboratorium
    public void getLaboratorium() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data laboratorium");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, transaksiLaboratoriumAPI.URL_SELECT +"email?email="+email
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONObject jsonArray = response.getJSONObject("data");



                        String paket_checkUp         = jsonArray.getString("paket_checkUp");
                        Double harga_checkUp         = Double.valueOf(jsonArray.getString("harga_paket"));
                        String jam_checkUp           = jsonArray.getString("jam_checkUp");
                        String tgl_checkUp           = jsonArray.getString("tgl_checkUp");
                        Integer id                   = Integer.valueOf(jsonArray.getString("id"));


                        transaksiLaboratorium = new transaksiLaboratorium(paket_checkUp,tgl_checkUp,harga_checkUp,jam_checkUp,email);

                        System.out.println(transaksiLaboratorium.getPaket_checkUp());

                    txt_PaketCheckUp.setText(transaksiLaboratorium.getPaket_checkUp());
                    txt_harga.setText(String.valueOf(transaksiLaboratorium.getHarga_checkUp()));
                    txt_tanggalCheckUp.setText(transaksiLaboratorium.getTgl_checkUp());
                    txt_jamCheckUp.setText(transaksiLaboratorium.getJam_checkUp());
                    txt_noBooking.setText(String.valueOf(id));

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(tampilLaboratorium.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(tampilLaboratorium.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}