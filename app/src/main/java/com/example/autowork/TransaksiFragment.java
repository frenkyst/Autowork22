package com.example.autowork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autowork.R;
import com.example.autowork.adapter.MemintaTransaksi;
import com.example.autowork.kasir.DetailBayarFragment;
import com.example.autowork.model.Histori;
import com.example.autowork.model.Transaksi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiFragment extends Fragment {


    public TransaksiFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;

    private ArrayList<Transaksi> daftarReq;
    private MemintaTransaksi memintatransaksi;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaksi, container, false);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = v.findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.TransaksiKaryawan).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        if (noteDataSnapshot.child("namaKaryawan").exists()) {
                            if (noteDataSnapshot.child("namaKaryawan").getValue(String.class).equals(name)) {

                                Transaksi requests = noteDataSnapshot.getValue(Transaksi.class);
                                requests.setKey(noteDataSnapshot.getKey());

                                daftarReq.add(requests);
//                            break;
                            }
                        }
                    }

                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintatransaksi = new MemintaTransaksi(daftarReq, getActivity());
                rc_list_request.setAdapter(memintatransaksi);
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
                loading.dismiss();
            }
        });

        // TOMBOL TAMBAH TRANSAKSI
        v.findViewById(R.id.btn_TambahTransaksi).setOnClickListener((view) -> {
            GlobalVariabel.NamaTransaksi = "Auto";
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new TransaksiKaryawanFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();
        });

        return v;
    }

}
