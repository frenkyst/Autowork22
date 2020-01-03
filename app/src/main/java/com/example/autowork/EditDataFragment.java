package com.example.autowork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
public class EditDataFragment extends Fragment {


    public EditDataFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database, database1;

    private EditText etBarkod, etNama, etJml, etHargaAwal, etHargaJual;

    private String hasilbarkod;


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


    // PROSES INISIALISASI BARCODE CEK DARI DATABASE
    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) {
            scane();

        } else {

            String etBarkod1 = etBarkod.getText().toString();
//            etBarkod1.toLowerCase();

            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).child(etBarkod1);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if( dataSnapshot.child("nama").exists()){
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        Integer jml = dataSnapshot.child("jml").getValue(Integer.class);
                        Integer hargaawal = dataSnapshot.child("hargaawal").getValue(Integer.class);
                        Integer hargajual = dataSnapshot.child("hargajual").getValue(Integer.class);

                        etNama.setText(nama);
                        etJml.setText(String.valueOf(jml));
                        etHargaAwal.setText(String.valueOf(hargaawal));
                        etHargaJual.setText(String.valueOf(hargajual));
                        etBarkod.setEnabled(false);

                        Toast.makeText(getActivity(),
                                "GET DATA = "+nama+"/"+jml+"/"+hargaawal+"/"+hargajual+"/",
                                Toast.LENGTH_SHORT).show();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_data, container, false);

        etBarkod = v.findViewById(R.id.et_barkod);
        etNama = v.findViewById(R.id.et_nama);
        etJml = v.findViewById(R.id.et_jml);
        etHargaAwal= v.findViewById(R.id.et_hrgawal);
        etHargaJual = v.findViewById(R.id.et_hrgjual);

        // TOMBOL BARKODE
        v.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });

        // TOMBOL CENCEL
        v.findViewById(R.id.btn_cancel).setOnClickListener((view) -> {
            etBarkod.setEnabled(true);
            etNama.setText("");
            etJml.setText("");
            etHargaAwal.setText("");
            etHargaJual.setText("");
        });

        // TOMBOL TAMBAH BARANG UNTUK INPUT KE TRANSAKSI YANG DI TERUSKAN KE KASIR
        v.findViewById(R.id.btn_editBarang).setOnClickListener((view) -> {

            String Sbarkod = etBarkod.getText().toString();
            String Sjml = etJml.getText().toString();
            String Snama = etNama.getText().toString();
            String ShargaAwal = etHargaAwal.getText().toString();
            String ShargaJual = etHargaJual.getText().toString();

            if (Sjml.equals("")) {
                etJml.setError("Silahkan masukkan jumlah");
                etJml.requestFocus();

            } else if (Snama.equals("")) {
                etNama.setError("Silahkan masukkan Nama Barang");
                etNama.requestFocus();

            } else if (ShargaAwal.equals("")) {
                etHargaAwal.setError("Silahkan masukkan Harga Awal");
                etHargaAwal.requestFocus();

            } else if (ShargaJual.equals("")) {
                etHargaJual.setError("Silahkan masukkan Harga Jual");
                etHargaJual.requestFocus();

            }{

                pushData(new Meminta(
                                Sbarkod,
                                Snama,
                                Integer.parseInt(Sjml),
                        Integer.parseInt(ShargaAwal),
                        Integer.parseInt(ShargaJual)), //IKI VARIABEL CLAS LOGHISTORY

                        Sbarkod); //jmlud DARI  PENJUMLAHAN SETELAH MENGISI INPUT TEXT JML (BUTTON BARKODE)

            }

        });

        return v;
    }

    // PROSES PUSH DATA KE FIREBASE
    private void pushData(Meminta meminta, String kodebarang) {

        database = FirebaseDatabase.getInstance().getReference();

        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Gudang)
                .child(kodebarang)
                .setValue(meminta);

                Toast.makeText(getActivity(),
                        "Edit BERHASIL...!!!",
                        Toast.LENGTH_SHORT).show();


    }
}
