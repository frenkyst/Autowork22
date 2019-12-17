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
        BossActivity.FragmentVar="HomeBossFragment";



        View v = inflater.inflate(R.layout.fragment_detailuser_boss, container, false);

        tvNama = v.findViewById(R.id.tv_nama1);
        tvEmail = v.findViewById(R.id.tv_email1);
        tvStatus = v.findViewById(R.id.tv_status1);


        mengambil();






        // TOMBOL ANGKAT KARYAWAN DAN FUNGSI
        v.findViewById(R.id.btn_karyawan).setOnClickListener((view) -> {


            database = FirebaseDatabase.getInstance().getReference();
            database.child(GlobalVariabel.Toko)
                    .child(GlobalVariabel.UserMan)
                    .child(BossActivity.uid)
                    .child("status")
                    .setValue("Karyawan");

            mengambil();

            Toast.makeText(getActivity(), "Sukses??",
                    Toast.LENGTH_SHORT).show();

        });

        // TOMBOL ANGKAT KASIR DAN FUNGSI
        v.findViewById(R.id.btn_kasir).setOnClickListener((view) -> {

        });

        // TOMBOL ANGKAT PHK DAN FUNGSI
        v.findViewById(R.id.btn_phk).setOnClickListener((view) -> {

        });


        return v;

    }

    public void mengambil(){
        database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.UserMan).child(BossActivity.uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nama = dataSnapshot.child("nama").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String status = dataSnapshot.child("status").getValue(String.class);

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

}
