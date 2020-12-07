package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tgsbesar.myapplication.databinding.ActivityRecyclerViewDaftarInapBinding;
import com.tgsbesar.myapplication.databinding.ActivityRecyclerViewLaboratoriumBinding;
import com.tgsbesar.myapplication.model.Laboratorium;

import java.util.ArrayList;
import java.util.List;

public class recyclerViewLaboratorium extends RecyclerView.Adapter<recyclerViewLaboratorium.MyViewHolder> {

    private Context context;
    private List<Laboratorium> result;
    private ActivityRecyclerViewDaftarInapBinding binding;

    public recyclerViewLaboratorium(ArrayList<Laboratorium> result){
        this.result=result;
    }

    @NonNull
    @Override
    public recyclerViewLaboratorium.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ActivityRecyclerViewLaboratoriumBinding item = ActivityRecyclerViewLaboratoriumBinding.inflate(inflater,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ActivityRecyclerViewLaboratoriumBinding binding;
        CardView cv_ListLab;

        public MyViewHolder(@NonNull ActivityRecyclerViewLaboratoriumBinding binding){
            super(binding.getRoot());
            this.binding=binding;
            cv_ListLab=(CardView)binding.cardLabAdapter;
        }
        public void bind(Laboratorium lab){
            binding.setLab(lab);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewLaboratorium.MyViewHolder holder, final int position) {
        final Laboratorium lab = result.get(position);
        holder.bind(lab);

        holder.cv_ListLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), laboratoriumNextActivity.class);
                intent.putExtra("Laboratorium",lab);
                view.getContext().startActivity(intent);
            }
        });

    }
}