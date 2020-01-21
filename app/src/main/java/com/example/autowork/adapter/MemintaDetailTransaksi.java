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

public class MemintaDetailTransaksi extends RecyclerView.Adapter<MemintaDetailTransaksi.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    AlertDialog.Builder dialog;

    public EditText txt_jumlahBarang;
    public TextView tv_namaBarang;

    private DatabaseReference database,databasePush;

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

    public MemintaDetailTransaksi(List<Meminta> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaDetailTransaksi.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_datatransaksi, parent, false);

        return new MemintaDetailTransaksi.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemintaDetailTransaksi.MyViewHolder holder, final int position) {
        final Meminta movie = moviesList.get(position);

        holder.tv_barkod.setText(movie.getBarkod());
        holder.tv_nama.setText(movie.getNama());
        holder.tv_jml.setText(String.valueOf(movie.getJml()));

        DecimalFormat decim = new DecimalFormat("#,###.##");
        if(movie.getTotal()!=null) {
            holder.tv_total.setText(decim.format(movie.getTotal()));
        }
        /**
         * ==============================================================================(STAR)
         * FUNGSI UNTUK MENAMPILKAN MENU POPUP MENUJU KE DETAIL SAAT ITEM DI PILIH
         */
        holder.tap_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                GlobalVariabel.barkod = movie.getBarkod();
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
                                dialog = new AlertDialog.Builder(mActivity);
                                View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.show_edit_dialog, null);

                                dialog.setView(dialogView);
                                dialog.setCancelable(true);
                                dialog.setIcon(R.mipmap.ic_launcher);

                                txt_jumlahBarang = dialogView.findViewById(R.id.txt_jumlahBarang);
                                tv_namaBarang = dialogView.findViewById(R.id.tv_namaBarang);

                                tv_namaBarang.setText(movie.getNama());

                                kosong();

                                dialog.setPositiveButton("UBAH", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (txt_jumlahBarang.getText().toString().equals("")) {
                                            txt_jumlahBarang.setError("Silahkan masukkan jumlah");
                                            txt_jumlahBarang.requestFocus();
                                        } else {

                                            String namaBarang = movie.getNama();
                                            String kodeBarang = movie.getBarkod();
                                            String timestampTransaksi = movie.getKey();

                                            Integer jumlahEdit = Integer.parseInt(txt_jumlahBarang.getText().toString());
                                            Integer jumlahAwal = movie.getJml();

                                            if (jumlahAwal != jumlahEdit) {

                                                EdtiTabelTransaksi((jumlahEdit - jumlahAwal), kodeBarang, timestampTransaksi, namaBarang);
                                            } else {
                                                Toast.makeText(mActivity, "OK", Toast.LENGTH_SHORT).show();
                                            }

                                        }

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

    private void EdtiTabelTransaksi(Integer jumlahEdit, String kodeBarang, String timestampTransaksi, String namaBarang){

        database = FirebaseDatabase.getInstance().getReference();

        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Gudang)
                .child(kodeBarang+"/jml").runTransaction(new Transaction.Handler() {

            String status;
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                Integer jumlahStok = mutableData.getValue(Integer.class);

                if(jumlahStok>=jumlahEdit){
                    mutableData.setValue(jumlahStok-jumlahEdit);
                    status = "Berhasil !!";
                    return Transaction.success(mutableData);

                } else {
                    status = "Stok Data Kurang !!";
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                if(databaseError != null){

                    status = "Error ! Ulangi !";

                } else if(status.equals("Berhasil !!")) {

                    databasePush = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko);
                    databasePush.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            if( dataSnapshot2.child(GlobalVariabel.Gudang).child(kodeBarang).child("hargaawal").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.Gudang).child(kodeBarang).child("hargajual").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi).child("totalLaba").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi).child("totalTransaksi").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("laba").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("total").exists() &&
                                    dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("jml").exists()){
                                //TABEL BARANG
                                Integer hargaAwal = dataSnapshot2.child(GlobalVariabel.Gudang).child(kodeBarang).child("hargaawal").getValue(Integer.class);
                                Integer hargaJual = dataSnapshot2.child(GlobalVariabel.Gudang).child(kodeBarang).child("hargajual").getValue(Integer.class);
                                //TABEL TRANSAKSI
                                Integer totalLaba = dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi).child("totalLaba").getValue(Integer.class);
                                Integer totalTransaksi = dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi).child("totalTransaksi").getValue(Integer.class);
                                Integer laba = dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("laba").getValue(Integer.class);
                                Integer total = dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("total").getValue(Integer.class);
                                Integer jumlah = dataSnapshot2.child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi).child("jml").getValue(Integer.class);

                                Integer updateJumlah = jumlah+jumlahEdit;

                                Integer LabaBarang = hargaJual - hargaAwal;

                                Integer updateLaba = laba+(jumlahEdit*LabaBarang);
                                Integer updateTotal = total+(jumlahEdit*hargaJual);
                                Integer updateTotalLaba = totalLaba+(jumlahEdit*LabaBarang);
                                Integer updateTotalTransaki = totalTransaksi+(jumlahEdit*hargaJual);

                                /**
                                 * DATA BARANG YANG MASUK TABEL TRANSAKSI 1
                                 */
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi)
                                        .child("jml")
                                        .setValue(updateJumlah);
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi)
                                        .child("laba")
                                        .setValue(updateLaba);
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi+"/transaksi/"+timestampTransaksi)
                                        .child("total")
                                        .setValue(updateTotal);

                                /**
                                 * DATA TOTAL PEMBAYARAN TABEL TRANSAKSI 1
                                 */
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi)
                                        .child("totalTransaksi")
                                        .setValue(updateTotalTransaki);

                                /**
                                 * DATA TOTAL LABA TABEL TRANSAKSI 1
                                 */
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi)
                                        .child("totalLaba")
                                        .setValue(updateTotalLaba);

                                /**
                                 * DATA LABA YANG MASUK TABEL LABA
                                 */
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.Laba)
                                        .child(timestampTransaksi)
                                        .child("laba")
                                        .setValue(updateLaba);
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.Laba)
                                        .child(timestampTransaksi)
                                        .child("jml")
                                        .setValue(updateJumlah);

                                Long timestampl = System.currentTimeMillis();
                                String timestamp = timestampl.toString();
                                /**
                                 * INPUT LOG TRANSAKSI KARYAWAN
                                 */
                                database.child(GlobalVariabel.Toko)
                                        .child(GlobalVariabel.Log)
                                        .child(timestamp)
                                        .setValue(new LogHistory(kodeBarang, namaBarang, jumlahEdit, "Edit Transaksi"));

                                databasePush.removeEventListener(this);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                Toast.makeText(mActivity,
                        status,
                        Toast.LENGTH_SHORT).show();

            }
        });

    }



}
