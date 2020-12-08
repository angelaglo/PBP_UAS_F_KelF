package com.tgsbesar.myapplication.menu_rawatInap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.transaksiLaboratoriumAPI;
import com.tgsbesar.myapplication.API.transaksiRInapAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.model.KelasKamar;
import com.tgsbesar.myapplication.model.transaksiLaboratorium;
import com.tgsbesar.myapplication.model.transaksiRawatInap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.android.volley.Request.Method.GET;

public class tampilRawatInap extends AppCompatActivity {
    String no_booking, email="stevani@tubes.com";
    private TextView txt_TipeKamar, txt_Harga, txt_Tanggal, txt_noBooking, txt_tanggal;
    private Integer id_transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rawat_inap);

        KelasKamar kmr = (KelasKamar) getIntent().getSerializableExtra("KelasKamar");
        String tanggal = getIntent().getStringExtra("Tanggal");
       // String no_book = getIntent().getStringExtra("no_book");

        txt_TipeKamar = (TextView) findViewById(R.id.txtKelasKamar);
       // txt_Fasilitas = (TextView) findViewById(R.id.txtFasilitas);
         txt_Harga = (TextView)findViewById(R.id.txtHarga) ;
         txt_tanggal = (TextView) findViewById(R.id.txtTanggal);
        txt_noBooking = (TextView) findViewById(R.id.txtNoBookingRI);


        getRawatInap();

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

        Button btn_edit = (Button)findViewById(R.id.buttonEdit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilRawatInap.this,editRawatInap.class);
                i.putExtra("id",id_transaksi);
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


    //mengambil data rawatInap
    public void getRawatInap() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data laboratorium");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, transaksiRInapAPI.URL_SELECT +"email?email="+email
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONObject jsonArray = response.getJSONObject("data");



                    String kelas_kamar         = jsonArray.getString("kelas_kamar");
                    Double harga_kamar         = Double.valueOf(jsonArray.getString("harga_kamar"));
                    String tgl_rinap           = jsonArray.getString("tgl_rinap");
                    Integer id                   = Integer.valueOf(jsonArray.getString("id"));

                    transaksiRawatInap transaksiRawatInap = new transaksiRawatInap(kelas_kamar,harga_kamar,tgl_rinap,id);


               //     System.out.println(transaksiLaboratorium.getPaket_checkUp());

                    txt_TipeKamar.setText(transaksiRawatInap.getKelas());
                    txt_noBooking.setText(String.valueOf(id));
                    txt_Harga.setText(String.valueOf(transaksiRawatInap.getHarga_kamar()));
                    txt_tanggal.setText(transaksiRawatInap.getTanggal_masuk());
                    id_transaksi=id;

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(tampilRawatInap.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(tampilRawatInap.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}