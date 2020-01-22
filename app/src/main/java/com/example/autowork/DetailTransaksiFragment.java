package com.example.autowork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.adapter.MemintaDetailTransaksi;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTransaksiFragment extends Fragment {


    public DetailTransaksiFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database, fromPath, toPath, totalTopath;

    private ArrayList<Meminta> daftarReq;
    private MemintaDetailTransaksi memintatransaksikasir;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    private Integer totalTransaksi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_transaksi, container, false);

        database = FirebaseDatabase.getInstance().getReference();
        TextView tv_totalTransaksi;
        tv_totalTransaksi = v.findViewById(R.id.tv_totalTransaksi);
        TextView tv_Detail;
        tv_Detail = v.findViewById(R.id.text_NamaDetail);
        tv_Detail.setText(GlobalVariabel.NamaTransaksi);

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

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.child("transaksi").getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Wisata
                     * Dan juga menyimpan primary key pada object Wisata
                     * untuk keperluan Edit dan Delete data
                     */
                    Meminta requests = noteDataSnapshot.getValue(Meminta.class);
                    requests.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Wisata yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarReq.add(requests);

                    /**
                     * =============================================================================(STAR)
                     * MENAMPILKAN TOTAL HARGA KESELURUHAN
                     * */


                    if(dataSnapshot.child("totalTransaksi").exists()) {
                        totalTransaksi = dataSnapshot.child("totalTransaksi").getValue(Integer.class);
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        tv_totalTransaksi.setText("Rp. " + decim.format(totalTransaksi));

                    }
                    /**
                     * =============================================================================(END)
                     */
                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintatransaksikasir = new MemintaDetailTransaksi(daftarReq, getActivity());
                rc_list_request.setAdapter(memintatransaksikasir);
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


        /**
         * ========================================================================================================================================(STAR)
         *TOMBOL BAYAR UNTUK MELAKUKAN TRANSAKSI PEMBAYARAN OLEH KASIR
         */
        v.findViewById(R.id.btn_Submit).setOnClickListener((view) -> {


            fromPath = FirebaseDatabase.getInstance().getReference(GlobalVariabel.Toko+"/"+GlobalVariabel.TransaksiKaryawan+"/"+GlobalVariabel.NamaTransaksi);
            toPath = FirebaseDatabase.getInstance().getReference(GlobalVariabel.Toko+"/"+GlobalVariabel.Transaksi+"/"+GlobalVariabel.NamaTransaksi);


                copyRecord(fromPath,toPath);

            Toast.makeText(getActivity(), "Transaksi Berhasil !!", Toast.LENGTH_SHORT).show();


        });
        /**
         * ========================================================================================================================================(END)
         *TOMBOL BAYAR UNTUK MELAKUKAN TRANSAKSI PEMBAYARAN OLEH KASIR
         */

        return v;
    }

    /**
     *
     * @description Memindahkan dataq transaksi ke notaPembayaran (firebase)
     * @param fromPath key lokasi transaksi
     * @param toPath key lokasi notaPembayaran
     */
    public void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fromPath.removeValue();
    }

}
