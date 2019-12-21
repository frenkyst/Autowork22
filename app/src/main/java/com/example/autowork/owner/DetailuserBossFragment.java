package com.example.autowork.owner;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailuserBossFragment extends Fragment {


    public DetailuserBossFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;
    private TextView tvNama, tvEmail, tvStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GlobalVariabel.VarFragmen="HomeBossFragment";

        View v = inflater.inflate(R.layout.fragment_detailuser_boss, container, false);

        tvNama = v.findViewById(R.id.tv_nama1);
        tvEmail = v.findViewById(R.id.tv_email1);
        tvStatus = v.findViewById(R.id.tv_status1);

        mengambil();

        /**
         * TOMBOL ANGKAT KARYAWAN DAN FUNGSI
         */
        v.findViewById(R.id.btn_karyawan).setOnClickListener((view) -> {


            submit("Karyawan",
                    "Ya");
            remove("Kasir");
            remove("PHK");

            mengambil();

            Toast.makeText(getActivity(), "Sukses Karyawan??",
                    Toast.LENGTH_SHORT).show();

        });

        /**
         * TOMBOL ANGKAT KASIR DAN FUNGSI
         */
        v.findViewById(R.id.btn_kasir).setOnClickListener((view) -> {

            remove("Karyawan");
            submit("Kasir",
                    "Ya");
            remove("PHK");

            mengambil();

            Toast.makeText(getActivity(), "Sukses Kasir??",
                    Toast.LENGTH_SHORT).show();

        });

        /**
         * TOMBOL ANGKAT PHK DAN FUNGSI
         */
        v.findViewById(R.id.btn_phk).setOnClickListener((view) -> {


            remove("Karyawan");
            remove("Kasir");
            submit("PHK",
                    "Ya");

            mengambil();

            Toast.makeText(getActivity(), "Sukses PHK??",
                    Toast.LENGTH_SHORT).show();


        });


        return v;

    }

    /**
     * MENGAMBIL DETAIL USER DARI FIREBASE
     */
    public void mengambil(){
        database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.UserMan).child(GlobalVariabel.uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("nama").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
//                String status = dataSnapshot.child("status").getValue(String.class);
                String status = null;

                if(dataSnapshot.child("Karyawan").exists()){
                    status = "Karyawan";
                } else if(dataSnapshot.child("Kasir").exists()){
                    status = "Kasir";
                } else if(dataSnapshot.child("PHK").exists()){
                    status = "PHK";
                }


                tvNama.setText(nama);
                tvEmail.setText(email);
                tvStatus.setText(status);

                Toast.makeText(getActivity(), nama+"  "+status+"  "+email, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * ============================================================================================= START
     * FUNGSI UNTUK MENGUBAH STATUS (KARYAWAN/KASIR/PHK)
     * @deprecated menginput data ke firebase berdasarkan param status
     * @param status Karyawan/Kasir/PHK
     * @param value nilai sembarang default = Ya
     */
    public void submit(String status, String value){
        database = FirebaseDatabase.getInstance().getReference();
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.UserMan)
                .child(GlobalVariabel.uid)
                .child(status)
                .setValue(value);
    }

    /**
     * JIKA STATUS DI RUBAH MAKA STATUS SEBELUMNYA AKAN DI HAPUS DENGAN FUNGSI BERIKUT
     * @deprecated menghapus data firebase berdasarkan param status
     * @param status Karyawan/Kasir/PHK
     */
    public void remove(String status){
        database = FirebaseDatabase.getInstance().getReference();
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.UserMan)
                .child(GlobalVariabel.uid)
                .child(status)
                .removeValue();
    }
    /**
     * ============================================================================================= END
     */


}
