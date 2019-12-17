package com.example.autowork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
            String Sjmlplus = tvJmlplus.getText().toString();
            String Snama = etNama.getText().toString();
            String logapa = "Update Stok";

            if (Sjml.equals("")) {
                etJml.setError("Silahkan masukkan jumlah");
                etJml.requestFocus();


            } else {


                editUser(new LogHistory(
                                Sbarkod.toLowerCase(),
                                Snama.toLowerCase(),
                                Sjml.toLowerCase(),logapa), //IKI VARIABEL CLAS LOGHISTORY

                        Sbarkod.toLowerCase(), Sjmlplus.toLowerCase()); //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)

                etBarkod.setEnabled(true);
                etBarkod.setText("");
                etNama.setText("");
                etJml.setText("");
                etJmlstok.setText("");
                tvJmlplus.setText("");
//                ettotal.setText("");
                //ethrgjual.setText("");
            }

        });

        return vt;
    }

    // PROSES PUSH DATA KE FIREBASE
    private void editUser(LogHistory log, String id, String ud) {
        database1.child("TOKO 1")
                .child("Gudang 0")
                .child(id)
                .child("jml")
                .setValue(ud);


        Long timestampl = System.currentTimeMillis()/1000;
        String timestamp = timestampl.toString();

        database1.child("TOKO 1")
                .child("Log")
                .child(timestamp)
                .setValue(log);


//        etBarkod.setEnabled(true);
        Toast.makeText(getActivity(),
                "Data Berhasil Tambah",
                Toast.LENGTH_SHORT).show();

    }

    // PROSES INISIALISASI BARCODE CEK DARI DATABASE
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
//                    String hrgjual = dataSnapshot.child("hargajual").getValue(String.class);
                    etNama.setText(nama);
                    etJmlstok.setText(jml);
                    //shrgjual1.setText(hrgjual);

                    String sNama = etNama.getText().toString();

                    if (sNama.equals("")) {
                        etBarkod.setError("Code Salah ??");
                        etBarkod.requestFocus();
                        etBarkod.selectAll();
                        dialogyesno();


                    } else {

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
