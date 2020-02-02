package com.example.autowork.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.model.Laba;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MemintaLaba extends RecyclerView.Adapter<MemintaLaba.MyViewHolder> {

    private List<Laba> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layoutLaba;
        public TextView tv_kodeTransaksi, tv_namaKaryawan, tv_NamaKasir, tv_totalTransaksi, tv_totalLaba, tv_tanggal;
        public ImageView tap_edit;

        public MyViewHolder(View view) {
            super(view);
            rl_layoutLaba = view.findViewById(R.id.rl_layoutLaba);
            tv_kodeTransaksi = view.findViewById(R.id.tv_kodeTransaksi);
            tv_namaKaryawan = view.findViewById(R.id.tv_namaKaryawan);
            tv_NamaKasir = view.findViewById(R.id.tv_namaKasir);
            tv_totalTransaksi = view.findViewById(R.id.tv_totalTransaksi);
            tv_totalLaba = view.findViewById(R.id.tv_totalLaba);
            tv_tanggal = view.findViewById(R.id.tv_tanggal);

        }
    }

    public MemintaLaba(List<Laba> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaLaba.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_laporan, parent, false);


        return new MemintaLaba.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MemintaLaba.MyViewHolder holder, final int position) {
        final Laba movie = moviesList.get(position);

        holder.tv_kodeTransaksi.setText(movie.getKey());
        holder.tv_namaKaryawan.setText(movie.getNamaKaryawan());
        holder.tv_NamaKasir.setText(movie.getNamaKasir());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = formatter.format(new Date(Long.parseLong(movie.getTanggalTransaksi())));
        holder.tv_tanggal.setText(dateString);
        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_totalTransaksi.setText(decim.format(movie.getTotalTransaksi()));
        holder.tv_totalLaba.setText(decim.format(movie.getTotalLaba()));

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
