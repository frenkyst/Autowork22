package com.example.autowork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autowork.adapter.MemintaHistori;
import com.example.autowork.adapter.MemintaView;
import com.example.autowork.model.Histori;
import com.example.autowork.model.Meminta;
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
public class HistoriTransaksiFragment extends Fragment {


    public HistoriTransaksiFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;

    private ArrayList<Histori> daftarReq;
    private MemintaHistori memintaview;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_histori_transaksi, container, false);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = v.findViewById(R.id.rc_list_request);
        //fab_add = findViewById(R.id.fab_add);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.NotaPembayaran).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarReq = new ArrayList<>();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        if(noteDataSnapshot.child("namaKaryawan").getValue(String.class).equals(name)){

                            Histori requests = noteDataSnapshot.getValue(Histori.class);
                            requests.setKey(noteDataSnapshot.getKey());

                            daftarReq.add(requests);
//                            break;
                        } else if(noteDataSnapshot.child("namaKasir").getValue(String.class).equals(name)){
                            Histori requests = noteDataSnapshot.getValue(Histori.class);
                            requests.setKey(noteDataSnapshot.getKey());

                            daftarReq.add(requests);
                        }



                    }

                }


                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintaview = new MemintaHistori(daftarReq, getActivity());
                rc_list_request.setAdapter(memintaview);
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



        return v;
    }



}
