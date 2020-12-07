package com.tgsbesar.myapplication.menu_rawatJalan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.databinding.ActivityListDoctorBinding;

import java.util.ArrayList;

public class SpesialisAdapter extends RecyclerView.Adapter<SpesialisAdapter.SpesialisViewHolder> {

    private ArrayList<Dokter> dataList;
    private ArrayList<Dokter> dokterList;
    ActivityListDoctorBinding binding;
    private OnClickListener onClickListener;

    public SpesialisAdapter(ArrayList<Dokter> dataList) {
        this.dataList=dataList;
    }

    @NonNull
    @Override
    public SpesialisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_list_spesialis,parent,false);
        return new SpesialisViewHolder(view);
    }

    public void setClick(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull SpesialisViewHolder holder, int position) {
        final String name = dataList.get(position).getSpesialis();
        holder.txtSpesialis.setText(dataList.get(position).getSpesialis());
        holder.cv_spesialis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.setClick(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList!=null)? dataList.size() : 0;
    }

    public class SpesialisViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSpesialis;
        private CardView cv_spesialis;

        public SpesialisViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSpesialis = (TextView) itemView.findViewById(R.id.txt_spesialis);
            cv_spesialis = (CardView) itemView.findViewById(R.id.card_spesialis);
        }
    }

    public interface OnClickListener{
        void setClick(String nama);
    }
}