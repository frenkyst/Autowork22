package com.example.autowork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.example.autowork.model.UserMan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TambahDataFragment extends Fragment {


    public TambahDataFragment() {
        // Required empty public constructor
    }









    private DatabaseReference database;
    private EditText etbarkod, etnama, etjml, ethrgawal, ethrgjual;
    private ProgressDialog loading;
    private Button btn_tambahbarang, btn_cencel;




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


        //jajal = Integer.valueOf(jajal10);

        //*/

        v.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                            Sjml,
                                            Shrgawal,
                                            Shrgjual),
                                    new LogHistory(
                                            Sbarkod,
                                            Snama,
                                            Sjml, logapa),
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


        // Inflate the layout for this fragment
        //









        return v;



    }





    private void submit(Meminta meminta, LogHistory log,String id) {
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Gudang)
                .child(id)
                //.child(prikey)
                .setValue(meminta);

        Long timestampl = System.currentTimeMillis()/1000;
        String timestamp = timestampl.toString();

        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Log)
                .child(timestamp)
                //.child(prikey)
                .setValue(log);

//        loading.dismiss();

        etbarkod.setText("");
        etnama.setText("");
        etjml.setText("");
        ethrgawal.setText("");
        ethrgjual.setText("");

        //View v = inflater.inflate(R.layout.fragment_in, container, false);

        Toast.makeText(getActivity(),
                "Data Berhasil ditambahkan",
                Toast.LENGTH_SHORT).show();
    }







}
