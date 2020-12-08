package com.tgsbesar.myapplication.menu_rawatInap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.LaboratoriumAPI;
import com.tgsbesar.myapplication.API.RawatInapAPI;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.databinding.ActivityDaftarRawatInapBinding;
import com.tgsbesar.myapplication.menu_laboratorium.laboratoriumActivity;
import com.tgsbesar.myapplication.menu_laboratorium.recyclerViewLaboratorium;
import com.tgsbesar.myapplication.model.KelasKamar;
import com.tgsbesar.myapplication.model.Laboratorium;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

public class daftarRawatInap extends AppCompatActivity {

    private ArrayList<KelasKamar> listKamar;
    private RecyclerView recyclerView;
    private CardView card_clicked;
    private recyclerViewDaftarInap adapter;
    private static ActivityDaftarRawatInapBinding binding;
    private String tipe_kamar;
    private TextView tv_tipeKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_daftar_rawat_inap);





        loadDaftarKamar();



    }

    public void loadDaftarKamar(){
        setAdapter();
        getKamar();
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

        listKamar = new ArrayList<KelasKamar>();

        //recycler view
        RecyclerView recyclerView = binding.recyclerViewRawatInap;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new recyclerViewDaftarInap(listKamar);
        recyclerView.setAdapter(adapter);

    }

    //mengambil data kamar
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
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, RawatInapAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!listKamar.isEmpty())
                        listKamar.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                        String tipe_kamar             = jsonObject.optString("kelas_kamar");
                        String fasilitas            = jsonObject.optString("fasilitas_kamar");
                        Double harga_kamar           = jsonObject.optDouble("harga_kamar");



                        //Membuat objek user
                        KelasKamar kamar= new KelasKamar(tipe_kamar,fasilitas,harga_kamar);

                        //Menambahkan objek user tadi ke list user
                        listKamar.add(kamar);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(daftarRawatInap.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(daftarRawatInap.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}