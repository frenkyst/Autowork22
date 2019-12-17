package com.example.autowork.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.R;
import com.example.autowork.model.Meminta;

import java.util.List;

public class MemintaTransaksikasir extends RecyclerView.Adapter<MemintaTransaksikasir.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_barkod, tv_nama, tv_jml, tv_total;

        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layout);
            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_jml = view.findViewById(R.id.tv_jml);
            tv_total = view.findViewById(R.id.tv_total);
        }
    }

    public MemintaTransaksikasir(List<Meminta> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaTransaksikasir.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_datatransaksi, parent, false);

        return new MemintaTransaksikasir.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemintaTransaksikasir.MyViewHolder holder, final int position) {
        final Meminta movie = moviesList.get(position);

        holder.tv_barkod.setText(movie.getBarkod());
        holder.tv_nama.setText(movie.getNama());
        holder.tv_jml.setText(movie.getJml());
        holder.tv_total.setText(movie.getTotal());

/*
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goDetail = new Intent(mActivity, MainActivity.class);
                goDetail.putExtra("id", movie.getKey());
                goDetail.putExtra("title", movie.getNama());
                goDetail.putExtra("email", movie.getEmail());
                goDetail.putExtra("desk", movie.getDesk());

                mActivity.startActivity(goDetail);


            }
        });
//*/

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}