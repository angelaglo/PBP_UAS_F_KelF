package com.tgsbesar.myapplication.menu_rawatJalan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.transaksiRInapAPI;
import com.tgsbesar.myapplication.API.transaksiRJalanAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;
import com.tgsbesar.myapplication.menu_rawatInap.tampilRawatInap;
import com.tgsbesar.myapplication.model.transaksiRawatInap;
import com.tgsbesar.myapplication.model.transaksiRawatJalan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.android.volley.Request.Method.GET;

public class tampilRawatJalan extends AppCompatActivity {
    private String nama_dr, jabatan_dr, jam_rj, tanggal_rj, email;
    private String id_transaksi;
    private TextView txt_spesialis, txt_dokter, txt_tanggal, txt_jam, txt_noBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rawat_jalan);

        String email = getIntent().getStringExtra("email");

        txt_spesialis = (TextView) findViewById(R.id.txtSpesialis);
        txt_dokter = (TextView) findViewById(R.id.txtDokter);
        txt_tanggal = (TextView) findViewById(R.id.txtTanggal);
        txt_jam = (TextView) findViewById(R.id.txtJam);
        txt_noBooking = (TextView) findViewById(R.id.txtNoUrut);

        getRawatJalan(email);

        //untuk cetak pdf
        Button btn_cetak = (Button)findViewById(R.id.buttonSendRJ);
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilRawatJalan.this, MainActivity.class);
                createNotificationChannel();
                addNotification();
                startActivity(i);
            }
        });

        Button btn_edit = (Button)findViewById(R.id.buttonEditRJ);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(tampilRawatJalan.this,editRawatJalan.class);
                i.putExtra("id",id_transaksi);
                i.putExtra("email",email);
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
                .setContentText("No Antrian: "+id_transaksi+" Pemeriksaan dengan "+nama_dr)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    public void getRawatJalan(String email) {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data transaksi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, transaksiRJalanAPI.URL_SELECT +"email?email="+email
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONObject jsonArray = response.getJSONObject("data");

                    String nama_dokter         = jsonArray.getString("nama_dokter");
                    String spesialis         = jsonArray.getString("spesialis");
                    String tgl_rjalan           = jsonArray.getString("tgl_rjalan");
                    String jam_rjalan         = jsonArray.getString("jam_rjalan");
                    String id                  = jsonArray.getString("id");

                    transaksiRawatJalan transaksiRawatJalan = new transaksiRawatJalan(nama_dokter,spesialis,tgl_rjalan,jam_rjalan);


                    nama_dr=nama_dokter;
                    txt_spesialis.setText(transaksiRawatJalan.getSpesialis());
                    txt_dokter.setText(transaksiRawatJalan.getNama_dokter());
                    txt_tanggal.setText(transaksiRawatJalan.getTgl_rjalan());
                    txt_jam.setText(transaksiRawatJalan.getJam_rjalan());
                    txt_noBooking.setText(id);
                    id_transaksi=id;

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(tampilRawatJalan.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(tampilRawatJalan.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}