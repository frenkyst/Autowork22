package com.example.autowork.kasir;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.adapter.MemintaNota;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotaPembayaranFragment extends Fragment {


    public NotaPembayaranFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;

    private ArrayList<Meminta> daftarReq;
    private MemintaNota memintaNota;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    private TextView tv_namaKasir,tv_kode,tv_tanggal,tv_totalNota;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nota_pembayaran, container, false);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = v.findViewById(R.id.rc_list_request);
        tv_namaKasir = v.findViewById(R.id.tv_nama);
        tv_kode = v.findViewById(R.id.tv_kodeTransaksi);
        tv_tanggal = v.findViewById(R.id.tv_tanggal);
        tv_totalNota = v.findViewById(R.id.tv_totalnota);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.NotaPembayaran).child(GlobalVariabel.timestamp).addValueEventListener(new ValueEventListener() {
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

                    String namaKasir1 = dataSnapshot.child("namaKasir").getValue(String.class);
                    String kode1 = dataSnapshot.getKey();

                    tv_namaKasir.setText(namaKasir1);
                    tv_kode.setText(kode1);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dateString = formatter.format(new Date(Long.parseLong(GlobalVariabel.timestamp)));
                    tv_tanggal.setText(dateString);


                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintaNota = new MemintaNota(daftarReq, getActivity());
                rc_list_request.setAdapter(memintaNota);
                loading.dismiss();


                int totalPrice = 0;
                for (int i = 0; i<daftarReq.size(); i++)
                {
                    totalPrice += Integer.parseInt( daftarReq.get(i).getTotal());
                }
                DecimalFormat decim = new DecimalFormat("#,###.##");
                tv_totalNota.setText("Rp. "+decim.format(totalPrice));
//                totalNota.setText(String.valueOf(totalPrice));
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
