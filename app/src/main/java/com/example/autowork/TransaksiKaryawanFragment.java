package com.example.autowork;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.autowork.model.Masuk;
import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.example.autowork.MainActivity;
import com.example.autowork.model.TransaksiKaryawan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//import com.google.zxing.Result;

import java.text.DecimalFormat;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiKaryawanFragment extends Fragment{

//    private ZXingScannerView zXingScannerView;

    public TransaksiKaryawanFragment() {
        // Required empty public constructor
    }


    DecimalFormat decim = new DecimalFormat("#,###.##");

    private DatabaseReference database, database1;

    private EditText etBarkod, etNama, etJml, etNamaTransaksi;
    private TextView tvtotal, tvtotaltransaksi,tvtotaltransaksi1;
    private ProgressDialog loading;
    private ViewConfiguration ketok;
    private Button btn_cancel, btn_tambahbarang, btn_barkod, btn_cencel1;

    private String jmlud, hasilbarkod;
    private Integer sjml, HargaJual, stotal, totalupdateTransaksi, totalTransaksi=0;

    private Integer hargaAwalInt, totalLabaint=0, Laba, totalUpdateLaba;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Data NULL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Data = "+result.getContents(), Toast.LENGTH_SHORT).show();
                hasilbarkod = result.getContents();
                etBarkod.setText(hasilbarkod); // MENAMPILKAN BARKOD HASIL SCAN KE VIEW LAYOUT
                mencaribarkod();
            }

            // At this point we may or may not have a reference to the activity
//            displayToast();
        }
    }



    public void scane(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(this);
        integrator.setCaptureActivity(Scaner.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Barkod e?");
        integrator.initiateScan();
    }



    @Override
    public View onCreateView  (LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vt = inflater.inflate(R.layout.fragment_transaksi_karyawan, container, false);


        database1 = FirebaseDatabase.getInstance().getReference();

        etNamaTransaksi = vt.findViewById(R.id.et_NamaTransaksi);
        etBarkod = vt.findViewById(R.id.et_barkod);
        etNama = vt.findViewById(R.id.et_nama);
        etJml = vt.findViewById(R.id.et_jml);
        tvtotal = vt.findViewById(R.id.tv_total);
        tvtotaltransaksi = vt.findViewById(R.id.tv_totaltransaksi);
        tvtotaltransaksi1 = vt.findViewById(R.id.tv_totaltransaksi1);

        if (GlobalVariabel.NamaTransaksi!="null"){
            totalTransaksi(GlobalVariabel.NamaTransaksi);
            etNamaTransaksi.setText(GlobalVariabel.NamaTransaksi);
        }


        // TOMBOL BARKODE
        vt.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });

        // TOMBOL TAMBAH BARANG UNTUK INPUT KE TRANSAKSI YANG DI TERUSKAN KE KASIR
        vt.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

            String SNamaTransaksi = etNamaTransaksi.getText().toString();
//            GlobalVariabel.NamaTransaksi = SNamaTransaksi;
            String Sbarkod = etBarkod.getText().toString();
            String Snama = etNama.getText().toString();
            String Sjml = etJml.getText().toString();
            String logapa = "Transaksi Karyawan";

            totalTransaksi(SNamaTransaksi);

            if (SNamaTransaksi.equals("") || Snama.equals("") || Sjml.equals("")) {
                etJml.setError("Silahkan ISI data dengan BENAR!!!");
                etJml.requestFocus();


            } else {

                // UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1
                totalupdateTransaksi = totalTransaksi + stotal; /** TOTALTRANSAKSI DARI FUNGSI AMBILTOTAL() DAN STOTAL DARI FUNGSI PENJUMLAHAN KETIKA  USER MENGINPUTKAN JUMLAH */
                totalUpdateLaba = totalLabaint + Laba;

                Long timestampl = System.currentTimeMillis();
                String timestamp = timestampl.toString();


                // end UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1 =========================

                inputDatabase(new TransaksiKaryawan(
                                Sbarkod,
                                Snama,
                                Integer.parseInt(Sjml),
                                stotal,
                                Laba,
                                HargaJual), //IKI VARIABEL MEMINTA || DATA TRANSAKSI BARANG

                        new LogHistory(
                                Sbarkod,
                                Snama,
                                Integer.parseInt(Sjml), logapa), // IKI LOG KELUAR MASUK TRANSAKSI KARYAWAN

                        Sbarkod, Integer.parseInt(Sjml), //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)
                        totalupdateTransaksi, totalUpdateLaba, timestamp,
                        new TransaksiKaryawan(
                                Snama,
                                Integer.parseInt(Sjml),
                                Laba), SNamaTransaksi
                        ); // HASIL TOTAL PEMBAYARAN


                /**
                 * MENSET BARKOD MENJADI ENABLE LAGI KARENA JIKA BARANG DITEMUKAN DARI DATABASE EDTI TEXT BARKOD DI SET MENJADI DISABLE
                 * DAN MENSET NAMA DAN JUMLAH MENJADI KOSONG LAGI
                 */
                etBarkod.setEnabled(true);
                etJml.setEnabled(false);
                etNama.setText("");
                etJml.setText("");

            }

        });

        return vt;
    }


    /**
     * =====================================================================================================================================(STAR)
     * PROSES VERIFIKASI BARKOD APAKAH ADA DI DATABASE ATAU TIDAK
     */
    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) { /** JIKA BARCOD TIDAK DIISI MAKA AKAN MENGEKSEKUSI FUNGSI SCAN */
            scane();

        } else { /** JIKA DIISI MAKA AKAH DILAKUKAN PENCARIAN DATABASE */

            String etBarkod1 = etBarkod.getText().toString();

            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).child(etBarkod1);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    /**
                     * VERIFIKASI BARKOD APAKAH ADA DI DATABASE FIREBASE
                     * JIKA NAMA DARI DATABASE TIDAK ADA MAKA AKAN MUNCUL Code salah
                     */
                    if (dataSnapshot.child("hargaawal").exists() &&
                            dataSnapshot.child("nama").exists() &&
                            dataSnapshot.child("jml").exists() &&
                            dataSnapshot.child("hargajual").exists()) {

                        Integer hargaawal = dataSnapshot.child("hargaawal").getValue(Integer.class);
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        Integer jml = dataSnapshot.child("jml").getValue(Integer.class);
                        Integer hrgjual = dataSnapshot.child("hargajual").getValue(Integer.class);
                        HargaJual = hrgjual;

                        etNama.setText(nama); /** MEMUNCULKAN NAMA DARI DATABASE JIKA ADA */

                        /**
                         * JIKA DATA DITEMUKAN MAKA HARGA DI JUMLAHKAN
                         */
                        etBarkod.setEnabled(false);//MENSET EDIT TEXT MENJADI DISABLE TIDAK BISA DI RUBAH
                        etJml.setEnabled(true);

                        /**
                         * FUNSI PENJUMLAHAN DISAAT BERSAMAAN KITA MENGETIK DI EDIT TEXT
                         */
                        etJml.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                String sJml = etJml.getText().toString();  /** JUMLAH DIAMBIL DARI EDIT TEXT */

                                if (etJml.getText().toString().matches("^0") )
                                {
                                    // Not allowed
                                    Toast.makeText(getActivity(), "not allowed", Toast.LENGTH_LONG).show();
                                    etJml.setText("");
                                }

                                if (sJml.equals("")) { /** INISIALISASI MENGHINDARI EROR JIKA EDIT TEXT KOSONG ATAU USER MELAKUKAN PENGHAPUSAN JUMLAH DI EDIT TEXT */
                                    sjml = 0;


                                } else { /** JIKA EDIT TEXT JUMLAH TIDAK KOSONG MAKA DILAKUKAN KONVERSI DATA DARI STRING KE INTEGER */
                                    sjml = Integer.parseInt(sJml);
                                }

                                stotal = sjml * hrgjual; /** DILAKUKAN PENJUMLAHAN UNTUK MENDAPATKAN TOTAL HARGA BARANG DIKALI JUMLAH BARANG*/
                                Laba = stotal - (sjml * hargaawal);

//                                jmludi = jml - sjml; /** PENJUMLAHAN VALUE JUMLAH BARANG DARI DATABASE FIREBASE DIKURANGI JUMLAH TRANSAKSI DARI EDIT TEXT USER */

                                DecimalFormat decim = new DecimalFormat("#,###.##");
                                tvtotal.setText("Rp. " + decim.format(stotal));/** MENAMPILKAN TOTAL HARGA KE VIEW LAYOUT (KONVERSI VALUE TOTAL HARGA BARANG) */
//                                tvtotal.setText(Integer.toString(stotal));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                    } else {
                        etBarkod.setError("Code Salah ??");
                        etBarkod.requestFocus();
                        etBarkod.selectAll();
                        dialogyesno();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    /**
     * =====================================================================================================================================(END)


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

                    Toast.makeText(getActivity(),
                            status,
                            Toast.LENGTH_SHORT).show();

                }
            });

        }

        totalTransaksi(NamaTransaksi);

    }


    /**
     * ================================================================================================(END)
     */


    /**
     * ==============================================================================================(STAR)
     * KETIKA SCAN DATA TIDAK DITEMUKAN MUNCUL DIALOG INI
     */
    private void dialogyesno(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        scane();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Apakah ingin SCAN ulang?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /**
     * ==============================================================================================(STAR)
     */


    private void totalTransaksi(String NamaTransaksi){

//        String SnamaTransaksi = etNamaTransaksi.getText().toString();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String uid = user.getUid();

        database1.child(GlobalVariabel.Toko).child(GlobalVariabel.TransaksiKaryawan).child(NamaTransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("totalTransaksi").exists() && dataSnapshot.child("totalLaba").exists()) {

                    totalTransaksi = dataSnapshot.child("totalTransaksi").getValue(Integer.class);
                    totalLabaint = dataSnapshot.child("totalLaba").getValue(Integer.class);

                    tvtotaltransaksi.setText("Rp. " + decim.format(totalTransaksi));

                } else {

                    totalTransaksi = 0;
                    totalLabaint = 0;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        }

    }

}

