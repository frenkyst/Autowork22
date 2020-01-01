package com.example.autowork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatestokdataFragment extends Fragment {


    public UpdatestokdataFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database, database1;

    private EditText etBarkod, etNama, etJml, etJmlstok;
    private TextView tvJmlplus;

    private String jmlud, hasilbarkod;
    private Integer sjml, sjmlstok, jmlupdate;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vt = inflater.inflate(R.layout.fragment_updatestokdata, container, false);



        database1 = FirebaseDatabase.getInstance().getReference();

        etBarkod = vt.findViewById(R.id.et_barkod);
        etNama = vt.findViewById(R.id.et_nama);
        etJml = vt.findViewById(R.id.et_jml);
        etJmlstok = vt.findViewById(R.id.et_jmlstok);
        tvJmlplus = vt.findViewById(R.id.tv_jmlplus);

        // TOMBOL BARKODE
        vt.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });

        // TOMBOL TAMBAH BARANG UNTUK INPUT KE TRANSAKSI YANG DI TERUSKAN KE KASIR
        vt.findViewById(R.id.btn_updatestok).setOnClickListener((view) -> {

            String Sbarkod = etBarkod.getText().toString();
            String Sjml = etJml.getText().toString();
//            String Sjmlplus = tvJmlplus.getText().toString();
            String Snama = etNama.getText().toString();
            String logapa = "Update Stok";

            if (Sjml.equals("")) {
                etJml.setError("Silahkan masukkan jumlah");
                etJml.requestFocus();


            } else {


               pushData(new LogHistory(
                                Sbarkod,
                                Snama,
                       Integer.parseInt(Sjml),logapa), //IKI VARIABEL CLAS LOGHISTORY

                        Sbarkod, Sjml); //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)

//                etBarkod.setEnabled(true);
//                etBarkod.setText("");
//                etNama.setText("");
//                etJml.setText("");
//                etJmlstok.setText("");
//                tvJmlplus.setText("");

            }

        });

        return vt;
    }

    // PROSES PUSH DATA KE FIREBASE
    private void pushData(LogHistory log, String kodebarang, String jumlahPenambahan) {


            database.child(GlobalVariabel.Toko)
                    .child(GlobalVariabel.Gudang)
                    .child(kodebarang+"/jml").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                    String jumlahStok = mutableData.getValue(String.class);

                    if (jumlahStok == null) {
                        return Transaction.success(mutableData);
                    }

                    String valueUpdate = String.valueOf (Integer.parseInt(jumlahPenambahan)+Integer.parseInt(jumlahStok));

                    mutableData.setValue("999");

                    Toast.makeText(getActivity(),
                            "MUTABLE = "+valueUpdate,
                            Toast.LENGTH_SHORT).show();

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    String status;

                    if(databaseError != null){
                        status = "eror";
                    } else {

//                        Long timestampl = System.currentTimeMillis();
//                        String timestamp = timestampl.toString();
//
//                        database1.child(GlobalVariabel.Toko)
//                                .child(GlobalVariabel.Log)
//                                .child(timestamp)
//                                .setValue(log);

                        status = "sukses";
                    }
                    Toast.makeText(getActivity(),
                            "status = "+status,
                            Toast.LENGTH_SHORT).show();

                }
            });

    }

    // PROSES INISIALISASI BARCODE CEK DARI DATABASE
    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) {
            scane();

        } else {

            String etBarkod1 = etBarkod.getText().toString();
//            etBarkod1.toLowerCase();

            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).child(etBarkod1);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if( dataSnapshot.child("nama").exists()){
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        Integer jml = dataSnapshot.child("jml").getValue(Integer.class);

                        etNama.setText(nama);
                        etJmlstok.setText(String.valueOf(jml));

                        etBarkod.setEnabled(false);

                        etJml.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String sJml = etJml.getText().toString();
                                String sJmlstok = etJmlstok.getText().toString();

                                if (sJml.equals("")) {
                                    sjml=0;
                                    sjmlstok=0;
                                } else {
                                    sjml = Integer.parseInt(sJml);
                                    sjmlstok = Integer.parseInt(sJmlstok);
                                }

                                jmlupdate = sjmlstok + sjml;

                                tvJmlplus.setText(Integer.toString(jmlupdate));  // MENAMPILKAN HASIL SETELAH DI UPDATE KE VIEW LAYOUT
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
}
