package com.example.autowork.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.autowork.kasir.HomeKasirFragment;
import com.example.autowork.kasir.KasirActivity;
import com.example.autowork.model.Meminta;
import com.example.autowork.model.Transaksi;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaTransaksi extends RecyclerView.Adapter<MemintaTransaksi.MyViewHolder> {

    private Context context;
    private List<Transaksi> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_nama, tv_total;
        public ImageView tap_edit;

        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layoutTransaksiKaryawan);
            tv_nama = view.findViewById(R.id.tv_namakaryawan);
            tv_total = view.findViewById(R.id.tv_totalkaryawan);

            tap_edit = view.findViewById(R.id.tap_edit);
        }
    }

    public MemintaTransaksi(List<Transaksi> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaTransaksi.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_transaksi, parent, false);


        return new MemintaTransaksi.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MemintaTransaksi.MyViewHolder holder, final int position) {
        final Transaksi movie = moviesList.get(position);

        holder.tv_nama.setText(movie.getNamakaryawan());
        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_total.setText(decim.format(movie.getTotalTransaksi()));

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
                                GlobalVariabel.uid = movie.getUid();
                                GlobalVariabel.invisible = "null";

                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Fragment myFragment = new DetailBayarFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framekasir, myFragment).addToBackStack(null).commit();

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
