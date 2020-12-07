package com.tgsbesar.myapplication.menu_rawatJalan;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tgsbesar.myapplication.R;

import java.util.ArrayList;

public class Tampil_Dokter extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DokterAdapter adapter;
    private ArrayList<Dokter> dokterArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil__dokter);

        addData();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new DokterAdapter(this,dokterArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void addData(){
           String nama = getIntent().getStringExtra("nama");
           ArrayList<Dokter> data = (new DaftarDokter()).DOKTER;
           Log.d("MASUK",String.valueOf(data.size())+nama);
           dokterArrayList = new ArrayList<>();

           for(Dokter data2 : data){

               if(data2.getSpesialis().contains(nama)){
                   dokterArrayList.add(data2);
                   Log.d("MASUK",data2.getSpesialis() + nama );
               }
           }
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