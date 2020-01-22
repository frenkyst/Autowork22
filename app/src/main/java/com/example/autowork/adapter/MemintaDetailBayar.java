package com.example.autowork.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.autowork.R;
import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaDetailBayar extends RecyclerView.Adapter<MemintaDetailBayar.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    AlertDialog.Builder dialog;

    public EditText txt_jumlahBarang;
    public TextView tv_namaBarang;

    private DatabaseReference database,databasePush;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layouttransaksi;
        public TextView tv_barkod, tv_nama, tv_jml, tv_harga, tv_total;
        public ImageView tap_edit;

        public MyViewHolder(View view) {
            super(view);
            rl_layouttransaksi = view.findViewById(R.id.rl_layouttransaksi);
            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_jml = view.findViewById(R.id.tv_jml);
            tv_harga = view.findViewById(R.id.tv_harga);
            tv_total = view.findViewById(R.id.tv_total);

            tap_edit = view.findViewById(R.id.tap_edit);
            tap_edit.setVisibility(View.GONE);
            txt_jumlahBarang = view.findViewById(R.id.txt_jumlahBarang);
        }
    }

    public MemintaDetailBayar(List<Meminta> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaDetailBayar.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_datatransaksi, parent, false);

        return new MemintaDetailBayar.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemintaDetailBayar.MyViewHolder holder, final int position) {
        final Meminta movie = moviesList.get(position);

        holder.tv_barkod.setText(movie.getBarkod());
        holder.tv_nama.setText(movie.getNama());
        holder.tv_jml.setText(String.valueOf(movie.getJml()));
        holder.tv_harga.setText("Rp "+String.valueOf(movie.getHargajual()));

        DecimalFormat decim = new DecimalFormat("#,###.##");
        if(movie.getTotal()!=null) {
            holder.tv_total.setText("Rp "+decim.format(movie.getTotal()));
        }


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}