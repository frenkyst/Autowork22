package com.example.autowork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.MainActivity;
import com.example.autowork.R;
import com.example.autowork.kasir.KasirActivity;
import com.example.autowork.model.Meminta;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaTransaksikasir extends RecyclerView.Adapter<MemintaTransaksikasir.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_barkod, tv_nama, tv_jml, tv_total, tv_taptransaksi;

        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layouttransaksi);
            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_jml = view.findViewById(R.id.tv_jml);
            tv_total = view.findViewById(R.id.tv_total);

            tv_taptransaksi = view.findViewById(R.id.tv_taptransaksi);
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

        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_total.setText(decim.format(Integer.parseInt(movie.getTotal())));
//        holder.tv_total.setText(movie.getTotal());
//        holder.tv_totalBayar.setText(movie.getTotal());
        holder.tv_taptransaksi.setText(" ");

        /**
         * ==============================================================================(STAR)
         * FUNGSI UNTUK MENAMPILKAN MENU POPUP MENUJU KE DETAIL SAAT ITEM DI PILIH
         */
        holder.tv_taptransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalVariabel.barkod = movie.getBarkod();
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.tv_taptransaksi);
                //inflating menu from xml resource
                popup.inflate(R.menu.kasir_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                //handle menu1 click
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();




            }
        });
        /**
         * ==============================================================================(END)
         */



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }



}