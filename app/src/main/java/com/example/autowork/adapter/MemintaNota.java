package com.example.autowork.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.autowork.R;
import com.example.autowork.model.Meminta;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaNota extends RecyclerView.Adapter<MemintaNota.MyViewHolder>{
    private Context context;
    private List<Meminta> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layoutnota;
        public TextView tv_nama, tv_total, tv_jumlah, tv_hargaAwal;

        public MyViewHolder(View view) {
            super(view);
            rl_layoutnota = view.findViewById(R.id.rl_layout);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_jumlah = view.findViewById(R.id.tv_jml);
            tv_hargaAwal = view.findViewById(R.id.tv_hargaAwal);
            tv_total = view.findViewById(R.id.tv_total);
        }
    }

    public MemintaNota(List<Meminta> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaNota.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_nota, parent, false);


        return new MemintaNota.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MemintaNota.MyViewHolder holder, final int position) {
        final Meminta movie = moviesList.get(position);


        holder.tv_nama.setText(movie.getNama());
        holder.tv_jumlah.setText(movie.getJml());

//        holder.tv_total.setText(movie.getTotal());


        int hargaAwal, jumlah, total;

        total = Integer.parseInt( movie.getTotal());
        jumlah = Integer.parseInt(movie.getJml());
        hargaAwal = total/jumlah;

//        String hargaAwal1 = String.valueOf(hargaAwal);
//        DecimalFormat.format(hargaAwal1);
        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_hargaAwal.setText(decim.format(hargaAwal));



        holder.tv_total.setText(decim.format(total));

//        holder.tv_hargaAwal.setText(hargaAwal1);



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
