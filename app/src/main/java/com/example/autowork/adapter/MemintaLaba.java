package com.example.autowork.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.kasir.DetailBayarFragment;
import com.example.autowork.model.Laba;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaLaba extends RecyclerView.Adapter<MemintaLaba.MyViewHolder> {

    private List<Laba> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layoutLaba;
        public TextView tv_namaBarang,tv_jumlahBarang, tv_totalLaba;
        public ImageView tap_edit;

        public MyViewHolder(View view) {
            super(view);
            rl_layoutLaba = view.findViewById(R.id.rl_layoutLaba);
            tv_namaBarang = view.findViewById(R.id.tv_namaBarang);
            tv_jumlahBarang = view.findViewById(R.id.tv_jumlahBarang);
            tv_totalLaba = view.findViewById(R.id.tv_totalLaba);

        }
    }

    public MemintaLaba(List<Laba> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaLaba.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_laba, parent, false);


        return new MemintaLaba.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MemintaLaba.MyViewHolder holder, final int position) {
        final Laba movie = moviesList.get(position);

        holder.tv_namaBarang.setText(movie.getNama());
        holder.tv_jumlahBarang.setText(String.valueOf(movie.getJml()));
        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_totalLaba.setText(decim.format(movie.getLaba()));

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
