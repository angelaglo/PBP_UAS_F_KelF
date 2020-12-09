package com.tgsbesar.myapplication.menu_rawatInap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.tgsbesar.myapplication.databinding.ActivityRecyclerViewDaftarInapBinding;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.menu_rawatJalan.Input;
import com.tgsbesar.myapplication.model.KelasKamar;

import java.util.ArrayList;
import java.util.List;

public class recyclerViewDaftarInap extends RecyclerView.Adapter<recyclerViewDaftarInap.MyViewHolder> {

    private Context context;
    private List<KelasKamar> result;
    private ActivityRecyclerViewDaftarInapBinding binding;


    public recyclerViewDaftarInap(ArrayList<KelasKamar> result){
        this.result=result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ActivityRecyclerViewDaftarInapBinding item = ActivityRecyclerViewDaftarInapBinding.inflate(inflater,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ActivityRecyclerViewDaftarInapBinding binding;
        CardView cv_tipeKamar;

        public MyViewHolder(@NonNull ActivityRecyclerViewDaftarInapBinding binding){
            super(binding.getRoot());
            this.binding=binding;
            cv_tipeKamar=(CardView)binding.cardRawatInapAdapter;
        }
        public void bind(KelasKamar kls){
            binding.setKls(kls);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final KelasKamar kls = result.get(position);

        holder.bind(kls);
        holder.cv_tipeKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),daftarRawatInapNext.class);
                intent.putExtra("KelasKamar",kls);
                view.getContext().startActivity(intent);
            }
        });

    }






}