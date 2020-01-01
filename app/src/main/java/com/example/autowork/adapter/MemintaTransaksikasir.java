package com.example.autowork.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.MainActivity;
import com.example.autowork.R;
import com.example.autowork.kasir.KasirActivity;
import com.example.autowork.model.Meminta;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.List;

public class MemintaTransaksikasir extends RecyclerView.Adapter<MemintaTransaksikasir.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    AlertDialog.Builder dialog;

    public EditText txt_jumlahBarang;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_barkod, tv_nama, tv_jml, tv_total;
        public ImageView tap_edit;



        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layouttransaksi);
            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_jml = view.findViewById(R.id.tv_jml);
            tv_total = view.findViewById(R.id.tv_total);

            tap_edit = view.findViewById(R.id.tap_edit);
            txt_jumlahBarang = view.findViewById(R.id.txt_jumlahBarang);


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
        holder.tv_jml.setText(String.valueOf(movie.getJml()));

        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_total.setText(decim.format(movie.getTotal()));
//        holder.tv_total.setText(movie.getTotal());
//        holder.tv_totalBayar.setText(movie.getTotal());
//        holder.tap_edit.setText(" ");

        /**
         * ==============================================================================(STAR)
         * FUNGSI UNTUK MENAMPILKAN MENU POPUP MENUJU KE DETAIL SAAT ITEM DI PILIH
         */
        holder.tap_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalVariabel.barkod = movie.getBarkod();
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.tap_edit);
                //inflating menu from xml resource
                popup.inflate(R.menu.kasir_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                //handle menu1 click
                                dialog = new AlertDialog.Builder(mActivity);
                                View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.show_edit_dialog, null);

//                                inflater = getLayoutInflater();
//                                dialogView = inflater.inflate(R.layout.show_edit_dialog, null);
                                dialog.setView(dialogView);
                                dialog.setCancelable(true);
                                dialog.setIcon(R.mipmap.ic_launcher);
//                                dialog.setTitle("Form Edit");


                                txt_jumlahBarang = dialogView.findViewById(R.id.txt_jumlahBarang);

                                kosong();

                                dialog.setPositiveButton("UBAH", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String jumlahBarang = txt_jumlahBarang.getText().toString();

                                        Toast.makeText(mActivity, "???????????????", Toast.LENGTH_SHORT).show();

//                                        txt_hasil.setText("Nama : " + nama + "\n" + "Usia : " + usia + "\n" + "Alamat : " + alamat + "\n" + "Status : " + status);
                                        dialog.dismiss();
                                    }
                                });

                                dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

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


    private void kosong(){

        txt_jumlahBarang.setText(null);
    }

    private void DialogForm(View view) {


    }

}