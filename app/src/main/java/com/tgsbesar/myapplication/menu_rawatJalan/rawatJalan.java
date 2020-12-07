package com.tgsbesar.myapplication.menu_rawatJalan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.LaboratoriumAPI;
import com.tgsbesar.myapplication.API.RawatJalanAPI;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_rawatInap.daftarRawatInap;
import com.tgsbesar.myapplication.menu_rawatInap.recyclerViewDaftarInap;
import com.tgsbesar.myapplication.model.KelasKamar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

public class rawatJalan extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SpesialisAdapter adapter;
    private ArrayList<Dokter> spesialisArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawat_jalan);

        loadDaftarDokter();

    }

    public void loadDaftarDokter(){
        getKamar();
        setAdapter();

    }

    public void setAdapter(){

        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/
        final int col;
        //check orientation

        spesialisArrayList = new ArrayList<Dokter>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new SpesialisAdapter(spesialisArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setClick(new SpesialisAdapter.OnClickListener() {
            @Override
            public void setClick(String nama) {
                Intent intent = new Intent(rawatJalan.this, Tampil_Dokter.class);
                intent.putExtra("nama",nama);
                startActivity(intent);
            }
        });

    }
    //mengambil data laboratorium
    public void getKamar() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data laboratorium");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, RawatJalanAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!spesialisArrayList.isEmpty())
                        spesialisArrayList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                        String nama             = jsonObject.optString("nama_dokter");
                        String spesialis          = jsonObject.optString("spesialis");
                      ;


                        //Membuat objek user
                        Dokter dokter= new Dokter(nama,spesialis);

                        //Menambahkan objek user tadi ke list user
                        spesialisArrayList.add(dokter);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(rawatJalan.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(rawatJalan.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
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
}