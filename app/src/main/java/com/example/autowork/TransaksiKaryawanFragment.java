package com.example.autowork;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//import com.google.zxing.Result;

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





    private DatabaseReference database, database1;

    private EditText etBarkod, etNama, etJml;
    private TextView tvtotal, tvtotaltransaksi;
    private ProgressDialog loading;
    private ViewConfiguration ketok;
    private Button btn_cancel, btn_tambahbarang, btn_barkod, btn_cencel1;

    private String jmlud, hasilbarkod,     totalUTS, Stotal;
    private Integer sjml, shrgjual, stotal, jmlu, jmludi, totalBayar, totalupdateTransaksi, stotal1, stotal2, totalTransaksi;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Data oohiugbub099876", Toast.LENGTH_SHORT).show();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
//        if (result != null){
//            if (result.getContents() == null){
//                Toast.makeText(this, "Data oohiugbub099876", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Data = "+result.getContents(), Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    public void scane(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(this);
        integrator.setCaptureActivity(Scaner.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Barkod e?");
//        integrator;
        integrator.initiateScan();



    }



    @Override
    public View onCreateView  (LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vt = inflater.inflate(R.layout.fragment_transaksi_karyawan, container, false);


        database1 = FirebaseDatabase.getInstance().getReference();

        etBarkod = vt.findViewById(R.id.et_barkod);
        etNama = vt.findViewById(R.id.et_nama);
        etJml = vt.findViewById(R.id.et_jml);
//        ettotal = vt.findViewById(R.id.et_total);
        tvtotal = vt.findViewById(R.id.tv_total);
        tvtotaltransaksi = vt.findViewById(R.id.tv_totaltransaksi);

        ambiltotal();


        // TOMBOL CENSEL
        vt.findViewById(R.id.btn_cancel).setOnClickListener((view) -> {
            scane();
        });

        vt.findViewById(R.id.btn_cancel1).setOnClickListener((view) -> {

            ambiltotal();

        });



        // TOMBOL BARKODE
        vt.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });

        // TOMBOL TAMBAH BARANG UNTUK INPUT KE TRANSAKSI YANG DI TERUSKAN KE KASIR
        vt.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

            String Sbarkod = etBarkod.getText().toString();
            String Snama = etNama.getText().toString();
            String Sjml = etJml.getText().toString();
            String logapa = "Transaksi Karyawan";

            String Stotal = tvtotal.getText().toString();

            String Stotaltransaksi = tvtotaltransaksi.getText().toString();
            //String Shrgawal = etHrgawal.getText().toString();
            //String Sjmlud = jmlud.toString();

            if (Sjml.equals("")) {
                etJml.setError("Silahkan masukkan jumlah");
                etJml.requestFocus();


            } else {

                // UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1
                stotal2 = Integer.parseInt(Stotal);

                totalupdateTransaksi = totalTransaksi + stotal;
                totalUTS = Integer.toString(totalupdateTransaksi);


                // end UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1 =========================

                inputDatabase(new Meminta(
                                Sbarkod.toLowerCase(),
                                Snama.toLowerCase(),
                                Sjml.toLowerCase(),
                                Stotal.toLowerCase()), //IKI VARIABEL MEMINTA || DATA TRANSAKSI BARANG

                        new LogHistory(
                                Sbarkod.toLowerCase(),
                                Snama.toLowerCase(),
                                Sjml.toLowerCase(),logapa), // IKI LOG KELUAR MASUK TRANSAKSI KARYAWAN

                        Sbarkod.toLowerCase(), jmlud, //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)
                        totalUTS); // HASIL TOTAL PEMBAYARAN

                etBarkod.setEnabled(true);
//                etBarkod.setText(Integer.toString(totalBayar));
                etNama.setText("");
                etJml.setText("");
//                tvtotaltransaksi.setText("--");

//                ettotal.setText("");

            }

            ambiltotal();

        });
//*/




        return vt;
    }


    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) {
            scane();
//                        etBarkod.setError("Silahkan masukkan code"); // MAUNE TES MOD
//                        etBarkod.requestFocus();

        } else {

            String etBarkod1 = etBarkod.getText().toString();
            etBarkod1.toLowerCase();

            database = FirebaseDatabase.getInstance().getReference().child("TOKO 1").child("Gudang 0").child(etBarkod1);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nama = dataSnapshot.child("nama").getValue(String.class);
                    String jml = dataSnapshot.child("jml").getValue(String.class);
                    String hrgjual = dataSnapshot.child("hargajual").getValue(String.class);
                    etNama.setText(nama);
                    //etJml.setText(jml);
                    //shrgjual1.setText(hrgjual);

                    String sNama = etNama.getText().toString();

                    if (sNama.equals("")) {
                        etBarkod.setError("Code Salah ??");
                        etBarkod.requestFocus();
                        etBarkod.selectAll();
                        dialogyesno();


                    } else {

                        // SETELAH DATA DITEMUKAN DAN ADA MAKA HARGA DI JUMLAHKAN
                        etBarkod.setEnabled(false);

                        etJml.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String sJml = etJml.getText().toString();

                                if (sJml.equals("")) {
                                    sjml=0;


                                } else {
                                    sjml = Integer.parseInt(sJml);
                                }

                                shrgjual = Integer.parseInt(hrgjual);
                                stotal = sjml * shrgjual;

                                stotal1 = stotal;
                                stotal2 = stotal;

                                jmlu = Integer.parseInt(jml);
                                jmludi = jmlu - sjml;
                                jmlud = Integer.toString(jmludi);



                                tvtotal.setText(Integer.toString(stotal));  // MENAMPILKAN TOTAL HARGA KE VIEW LAYOUT
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });



                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
    }

    // PROSES PUSH DATA KE FIREBASE
    private void inputDatabase(Meminta meminta, LogHistory log, String barkod, String ud, String udtr) {
        // DATA UPDATE JUMLAH STOK SAAT DILAKUKAN TRANSAKSI
        database1.child("TOKO 1")
                .child("Gudang 0")
                .child(barkod)
                .child("jml")
                .setValue(ud);


        Long timestampl = System.currentTimeMillis()/1000;
        String timestamp = timestampl.toString();

        // DATA BARANG YANG MASUK TABEL TRANSAKSI 1
        database1.child("TOKO 1")
                .child("Transaksi 1")
                .child(timestamp)
                .setValue(meminta);

        // DATA TOTAL PEMBAYARAN TABEL TRANSAKSI 1
        database1.child("TOKO 1")
                .child("Transaksi 1")
                .child("zzzzzzzzz").child("total")
                .setValue(udtr);

        database1.child("TOKO 1")
                .child("Log")
                .child(timestamp)
                .setValue(log);


//        etBarkod.setEnabled(true);
        Toast.makeText(getActivity(),
                "Data Berhasil Tambah",
                Toast.LENGTH_SHORT).show();

    }

    // KETIKA SCAN DATA TIDAK DITEMUKAN MUNCUL DIALOG INI
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

    // FUNGSI MENDAPATKAN NILAI TOTAL TRANSAKSI DARI TABEL TRANSAKSI
    private void ambiltotal(){
        database = FirebaseDatabase.getInstance().getReference().child("TOKO 1").child("Transaksi 1").child("zzzzzzzzz").child("total");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
//                        String totalbayar = dataSnapshot2.child("total").getValue(String.class);
                String totaltransaksi = dataSnapshot2.getValue(String.class);

                if (TextUtils.isEmpty(totaltransaksi)) {
                        totalTransaksi=0;
                    } else {
                        tvtotaltransaksi.setText(totaltransaksi);
                    }

                totalTransaksi = Integer.parseInt(tvtotaltransaksi.getText().toString());


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}

