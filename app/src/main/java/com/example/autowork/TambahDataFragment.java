package com.example.autowork;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.example.autowork.model.UserMan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class TambahDataFragment extends Fragment {


    public TambahDataFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;
    private EditText etbarkod, etnama, etjml, ethrgawal, ethrgjual;
//    private ProgressDialog loading;
//    private Button btn_tambahbarang, btn_cencel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tambah_data, container, false);

        database = FirebaseDatabase.getInstance().getReference();
//
        etbarkod = v.findViewById(R.id.et_barkod);
        etnama = v.findViewById(R.id.et_nama);
        etjml = v.findViewById(R.id.et_jml);
        ethrgawal = v.findViewById(R.id.et_hrgawal);
        ethrgjual = v.findViewById(R.id.et_hrgjual);


        /**
         * ==========================================================================================================================================================================(STAR)
         * TOMBOL PROSES INPUT DATA BARANG BARU KE FIREBASE
         */
        v.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

//            database = FirebaseDatabase.getInstance();
            database.child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String Sbarkod = etbarkod.getText().toString();
                    String Snama = etnama.getText().toString();
                    String Sjml = etjml.getText().toString();
                    String Shrgawal = ethrgawal.getText().toString();
                    String Shrgjual = ethrgjual.getText().toString();
                    String logapa  = "Barang Baru";

                    if (dataSnapshot.child(Sbarkod).exists()) {

                        Toast.makeText(getActivity(), "Data yang anda masukan sudah ada di gudang silahkan lakukan update stok untuk mengubah stok", Toast.LENGTH_SHORT).show();

                    } else {

                        if (Sbarkod.equals("")) {
                            etbarkod.setError("Silahkan masukkan code");
                            etbarkod.requestFocus();
                        } else if (Snama.equals("")) {
                            etnama.setError("Silahkan masukkan nama");
                            etnama.requestFocus();
                        } else if (Sjml.equals("")) {
                            etjml.setError("Silahkan masukkan jumlah");
                            etjml.requestFocus();
                        } else if (Shrgawal.equals("")) {
                            ethrgawal.setError("Silahkan masukkan harga awal");
                            ethrgawal.requestFocus();
                        } else if (Shrgjual.equals("")) {
                            ethrgjual.setError("Silahkan masukkan harga jual");
                            ethrgjual.requestFocus();
                        } else {


                            submit(new Meminta(
                                            Sbarkod,
                                            Snama,
                                            Integer.parseInt(Sjml),
                                            Integer.parseInt(Shrgawal),
                                            Integer.parseInt(Shrgjual)),
                                    new LogHistory(
                                            Sbarkod,
                                            Snama,
                                            Integer.parseInt(Sjml), logapa),
                                    Sbarkod);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "  CEK eror ?  ", Toast.LENGTH_SHORT).show();
                }
            });



        });

        /**
         * ==========================================================================================================================================================================(END)
         */


        // Inflate the layout for this fragment
        //


        v.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            scane();
        });






        return v;



    }


    /**
     * PROSES PUSH DATA KE FIREBASE
     * @deprecated push data barang baru
     * @param meminta data barang
     * @param log data log barang
     * @param id key lokasi penyimpanan sama dengan barkod
     */
    private void submit(Meminta meminta, LogHistory log,String id) {
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Gudang)
                .child(id)
                //.child(prikey)
                .setValue(meminta);

        Long timestampl = System.currentTimeMillis()/1000;
        String timestamp = timestampl.toString();

        /**
         * PUSH DATA LOG
         */
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Log)
                .child(timestamp)
                //.child(prikey)
                .setValue(log);

//        loading.dismiss();

        /**
         * SET TEXTVIEW MEJADI KOSONG
         */
        etbarkod.setText("");
        etnama.setText("");
        etjml.setText("");
        ethrgawal.setText("");
        ethrgjual.setText("");

        //View v = inflater.inflate(R.layout.fragment_in, container, false);

        /**
         * NOTIF DATA BERHASIL DI TAMBAHKAN KE FIREBASE
         */
        Toast.makeText(getActivity(),
                "Data Berhasil ditambahkan",
                Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Data Tidak Ada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Data = "+result.getContents(), Toast.LENGTH_SHORT).show();
                String hasilbarkod = result.getContents();
                etbarkod.setText(hasilbarkod); // MENAMPILKAN BARKOD HASIL SCAN KE VIEW LAYOUT
//                mencaribarkod();
            }

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



}
