package com.example.autowork.adapter;

//public class MemintaView { }

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.example.autowork.DetailTransaksiFragment;
import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.TransaksiKaryawanFragment;
import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.example.autowork.model.TransaksiKaryawan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class MemintaView extends RecyclerView.Adapter<MemintaView.MyViewHolder> {

    private List<Meminta> moviesList;
    private Activity mActivity;

    AlertDialog.Builder dialog;
    public EditText txt_JumlahBarang;
    public TextView tv_NamaBarang, tv_HargaBarang, tv_NamaTransaksi;

    private DatabaseReference database1, database2;
    private Integer totalLabaint=0, totalTransaksi=0;
    private TextView tvtotalTransaksi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layout;
        public TextView tv_barkod, tv_nama, tv_jml, tv_harga;
        public ImageView tap_edit;


        public MyViewHolder(View view) {
            super(view);
            rl_layout = view.findViewById(R.id.rl_layout);
            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_harga = view.findViewById(R.id.tv_harga);
            tv_jml = view.findViewById(R.id.tv_jml);

            tap_edit =  view.findViewById(R.id.tap_edit);

            if(GlobalVariabel.GoneTapEdit.equals("ya")){
                tap_edit.setVisibility(View.GONE);
            }

        }
    }

    public MemintaView(List<Meminta> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Meminta movie = moviesList.get(position);

        holder.tv_barkod.setText(movie.getBarkod());
        holder.tv_nama.setText(movie.getNama());

        DecimalFormat decim = new DecimalFormat("#,###.##");
        holder.tv_harga.setText("Rp "+decim.format(movie.getHargajual()));
//        holder.tv_harga.setText(movie.getHargajual());
        holder.tv_jml.setText(String.valueOf(movie.getJml()));

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
                popup.inflate(R.menu.tambah_transaksi_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_tambahTransaksi:
                                //handle menu1 click

                                totalTransaksi(TransaksiKaryawanFragment.SNamaTransaksi);

                                dialog = new AlertDialog.Builder(mActivity);
                                View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.show_transaksi_dialog, null);

                                dialog.setView(dialogView);
                                dialog.setCancelable(true);
                                dialog.setIcon(R.mipmap.ic_launcher);

                                txt_JumlahBarang = dialogView.findViewById(R.id.txt_JumlahBarang);
                                tv_NamaBarang = dialogView.findViewById(R.id.tv_NamaBarang);
                                tv_NamaTransaksi = dialogView.findViewById(R.id.tv_NamaTransaksi);
                                tv_HargaBarang = dialogView.findViewById(R.id.tv_HargaBarang);

                                tv_NamaBarang.setText(movie.getNama());
                                tv_NamaTransaksi.setText(TransaksiKaryawanFragment.SNamaTransaksi);
                                DecimalFormat decim = new DecimalFormat("#,###.##");
                                tv_HargaBarang.setText("Rp. " + decim.format(movie.getHargajual()));

                                kosong();

                                dialog.setPositiveButton("TAMBAH TRANSAKSI", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (txt_JumlahBarang.getText().toString().equals("")) {
                                            txt_JumlahBarang.setError("Silahkan masukkan jumlah");
                                            txt_JumlahBarang.requestFocus();
                                        } else {

                                            String namaBarang = movie.getNama();
                                            String kodeBarang = movie.getBarkod();
                                            Integer hargaJual = movie.getHargajual();
                                            Integer hargaAwal = movie.getHargaawal();

                                            Integer jumlahTransaksi = Integer.parseInt(txt_JumlahBarang.getText().toString());

                                            Integer subTotal = hargaJual * jumlahTransaksi;
                                            Integer Laba = subTotal - (hargaAwal * jumlahTransaksi);

                                            if (TransaksiKaryawanFragment.SNamaTransaksi.equals("")) {

                                                Toast.makeText(mActivity,
                                                        "Silahkan ISI data dengan BENAR!!!",
                                                        Toast.LENGTH_SHORT).show();

                                            } else {


                                                // UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1
                                                Integer totalUpdateTransaksi = totalTransaksi + subTotal; /** TOTALTRANSAKSI DARI FUNGSI AMBILTOTAL() DAN STOTAL DARI FUNGSI PENJUMLAHAN KETIKA  USER MENGINPUTKAN JUMLAH */
                                                Integer totalUpdateLaba = totalLabaint + Laba;

                                                Long timestampl = System.currentTimeMillis();
                                                String timestamp = timestampl.toString();


                                                // end UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1 =========================

                                                inputDatabase(new TransaksiKaryawan(
                                                                kodeBarang,
                                                                namaBarang,
                                                                jumlahTransaksi,
                                                                subTotal,
                                                                Laba,
                                                                hargaJual), //IKI VARIABEL MEMINTA || DATA TRANSAKSI BARANG

                                                        new LogHistory(
                                                                kodeBarang,
                                                                namaBarang,
                                                                jumlahTransaksi, "Transaksi Karyawan"), // IKI LOG KELUAR MASUK TRANSAKSI KARYAWAN

                                                        kodeBarang, jumlahTransaksi, //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)
                                                        totalUpdateTransaksi, totalUpdateLaba, timestamp,
                                                        new TransaksiKaryawan(
                                                                namaBarang,
                                                                jumlahTransaksi,
                                                                Laba), TransaksiKaryawanFragment.SNamaTransaksi
                                                ); // HASIL TOTAL PEMBAYARAN


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
         * ===============================================================================================================================================(END)
         */

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    private void kosong(){

        txt_JumlahBarang.setText(null);
    }

    private void transaksi(){



    }


    /**
     * ================================================================================================(STAR)
     * PROSES PUSH DATA KE FIREBASE
     * @deprecated melakukan input data transaksi log dan update barang setelah transaksi
     * @param transaksiKaryawan trasaksi data barang
     * @param log data log transaksi karyawan
     * @param barkod barkod untuk menentukan lokasi barang yang di update
     * @param updateStok value hasil update barang setelah transaksi
     * @param udtr  value total pembayaran
     */
    private void inputDatabase(TransaksiKaryawan transaksiKaryawan, LogHistory log, String barkod, Integer updateStok, Integer udtr, Integer udla, String timestamp, TransaksiKaryawan transaksiKaryawanLaba, String NamaTransaksi) {

        database1 = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String uid = user.getUid();

            database1.child(GlobalVariabel.Toko)
                    .child(GlobalVariabel.Gudang)
                    .child(barkod+"/jml").runTransaction(new Transaction.Handler() {

                String status;

                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                    Integer jumlahStok = mutableData.getValue(Integer.class);

                    if(jumlahStok>=updateStok){
                        mutableData.setValue(jumlahStok-updateStok);
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

                        /**
                         * DATA BARANG YANG MASUK TABEL TRANSAKSI 1
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child(GlobalVariabel.TransaksiKaryawan+"/"+NamaTransaksi+"/transaksi")
                                .child(timestamp)
                                .setValue(transaksiKaryawan);

                        /**
                         * DATA TOTAL PEMBAYARAN TABEL TRANSAKSI 1
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child(GlobalVariabel.TransaksiKaryawan+"/"+NamaTransaksi)
                                .child("totalTransaksi")
                                .setValue(udtr);

                        /**
                         * DATA TOTAL LABA TABEL TRANSAKSI 1
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child(GlobalVariabel.TransaksiKaryawan+"/"+NamaTransaksi)
                                .child("totalLaba")
                                .setValue(udla);

                        /**
                         * DATA KARYAWAN YANG MELAKUKAN TRANSAKSI
                         */
                        database1.child(GlobalVariabel.Toko+"/"+GlobalVariabel.TransaksiKaryawan+"/"+NamaTransaksi)
                                .child("namaKaryawan")
                                .setValue(name);
                        database1.child(GlobalVariabel.Toko+"/"+GlobalVariabel.TransaksiKaryawan+"/"+NamaTransaksi)
                                .child("uid")
                                .setValue(uid);

                        /**
                         * INPUT LOG TRANSAKSI KARYAWAN
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child(GlobalVariabel.Log)
                                .child(timestamp)
                                .setValue(log);

                        /**
                         * DATA LABA YANG MASUK TABEL LABA
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child(GlobalVariabel.Laba)
                                .child(timestamp)
                                .setValue(transaksiKaryawanLaba);

                    }

                    Toast.makeText(mActivity,
                            status,
                            Toast.LENGTH_SHORT).show();

                }
            });

        }

//        totalTransaksi(NamaTransaksi);

    }


    /**
     * ================================================================================================(END)
     */




    private void totalTransaksi(String NamaTransaksi){

        database2 = FirebaseDatabase.getInstance().getReference();

        database2.child(GlobalVariabel.Toko).child(GlobalVariabel.TransaksiKaryawan).child(NamaTransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("totalTransaksi").exists() && dataSnapshot.child("totalLaba").exists()) {

                    totalTransaksi = dataSnapshot.child("totalTransaksi").getValue(Integer.class);
                    totalLabaint = dataSnapshot.child("totalLaba").getValue(Integer.class);
//                    DecimalFormat decim = new DecimalFormat("#,###.##");
//                    tvtotalTransaksi.setText("Rp. " + decim.format(totalTransaksi));

                } else {

                    totalTransaksi = 0;
                    totalLabaint = 0;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}