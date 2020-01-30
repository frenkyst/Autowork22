package com.example.autowork.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.kasir.DetailBayarFragment;
import com.example.autowork.kasir.NotaPembayaranFragment;
import com.example.autowork.model.Histori;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaHistori extends RecyclerView.Adapter<MemintaHistori.MyViewHolder> {

    private List<Histori> moviesList;
    private Activity mActivity;


    private DatabaseReference database,databasePush;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_namaKaryawan, tv_NamaKasir, tv_totalTransaksi, tv_kodeTranaksi;
        public ImageView tap_edit;

        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layoutHistori);
            tv_namaKaryawan = view.findViewById(R.id.tv_namaKaryawan);
            tv_NamaKasir = view.findViewById(R.id.tv_namaKasir);
            tv_totalTransaksi = view.findViewById(R.id.tv_totalTransaksi);
            tv_kodeTranaksi = view.findViewById(R.id.tv_kodeTransaksi);

            tap_edit = view.findViewById(R.id.tap_edit);

        }
    }

    public MemintaHistori(List<Histori> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaHistori.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_histori, parent, false);

        return new MemintaHistori.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemintaHistori.MyViewHolder holder, final int position) {
        final Histori movie = moviesList.get(position);

        holder.tv_namaKaryawan.setText(movie.getNamaKaryawan());
        holder.tv_NamaKasir.setText(movie.getNamaKasir());
//        holder.tv_totalTransaksi.setText(String.valueOf(movie.getTotalTransaksi()));
        holder.tv_kodeTranaksi.setText(movie.getKey());

        DecimalFormat decim = new DecimalFormat("#,###.##");

        holder.tv_totalTransaksi.setText("Rp "+decim.format(movie.getTotalTransaksi()));


        /**
         * ============================================================================================================================================(STAR)
         * FUNGSI UNTUK MENAMPILKAN MENU POPUP MENUJU KE DETAIL SAAT ITEM DI PILIH
         */
        holder.tap_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.tap_edit);
                //inflating menu from xml resource
                popup.inflate(R.menu.detail_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_detail:
                                //handle menu1 click
                                GlobalVariabel.timestamp = movie.getKey();

                                AppCompatActivity activity1 = (AppCompatActivity) view.getContext();
                                Fragment myFragment1 = new NotaPembayaranFragment();
                                activity1.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment1).addToBackStack(null).commit();

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
         * ===============================================================================================================================================(END)
         */

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
