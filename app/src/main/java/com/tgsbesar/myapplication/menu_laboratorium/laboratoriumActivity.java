package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.databinding.ActivityLaboratoriumBinding;
import com.tgsbesar.myapplication.menu_rawatJalan.SpesialisAdapter;
import com.tgsbesar.myapplication.menu_rawatJalan.Tampil_Dokter;
import com.tgsbesar.myapplication.menu_rawatJalan.rawatJalan;
import com.tgsbesar.myapplication.model.Laboratorium;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

public class laboratoriumActivity extends AppCompatActivity {
    public ArrayList<Laboratorium> listLab;
    private RecyclerView recyclerView;
    private recyclerViewLaboratorium adapter;
    private static ActivityLaboratoriumBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_laboratorium);

        loadDaftarLaboratorium();


    }
    public void loadDaftarLaboratorium(){
        setAdapter();
        getLaboratorium();
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

    public void setAdapter(){

        /*Buat tampilan untuk adapter jika potrait menampilkan 2 data dalam 1 baris,
        sedangakan untuk landscape 4 data dalam 1 baris*/
        final int col;
        //check orientation

        listLab = new ArrayList<Laboratorium>();

        //recycler view
        RecyclerView recyclerView = binding.recyclerViewLaboratorium;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new recyclerViewLaboratorium(listLab);
        recyclerView.setAdapter(adapter);

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
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, LaboratoriumAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("laboratorium");

                    if(!listLab.isEmpty())
                        listLab.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                        String kategori             = jsonObject.optString("paket_checkUp");
                        String deskripsi            = jsonObject.optString("deskripsi_checkUp");
                        Double harga_test           = jsonObject.optDouble("harga_paket");


                        //Membuat objek user
                        Laboratorium laboratorium= new Laboratorium(kategori,deskripsi,harga_test);

                        //Menambahkan objek user tadi ke list user
                        listLab.add(laboratorium);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(laboratoriumActivity.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(laboratoriumActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}