package com.example.autowork.kasir;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.adapter.MemintaTransaksikasir;
import com.example.autowork.model.LogHistory;
import com.example.autowork.model.LogKasir;
import com.example.autowork.model.Meminta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
public class DetailBayarFragment extends Fragment {


    public DetailBayarFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database, fromPath, toPath, totalTopath;
    Object Uid;

    private ArrayList<Meminta> daftarReq;
    private MemintaTransaksikasir memintatransaksikasir;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    String timestamp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_bayar, container, false);

        if(GlobalVariabel.invisible.equals("ya")){
            v.findViewById(R.id.btn_bayar).setVisibility(View.INVISIBLE);
        } else {
            v.findViewById(R.id.btn_bayar).setVisibility(View.VISIBLE);
        }



        database = FirebaseDatabase.getInstance().getReference();
        TextView tv_totalBayar;
        tv_totalBayar = v.findViewById(R.id.tv_totalBayar);

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

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.Transaksi+"/"+GlobalVariabel.uid).addValueEventListener(new ValueEventListener() {
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
                    String totalbayar = dataSnapshot.child("totalTransaksi").getValue(String.class);

                    DecimalFormat decim = new DecimalFormat("#,###.##");
                    tv_totalBayar.setText("Rp. "+decim.format(Integer.parseInt(totalbayar)));
//                    tv_totalBayar.setText("Rp. "+totalbayar);
                    /**
                     * =============================================================================(END)
                     */
                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintatransaksikasir = new MemintaTransaksikasir(daftarReq, getActivity());
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
        v.findViewById(R.id.btn_bayar).setOnClickListener((view) -> {

            Long timestampl = System.currentTimeMillis();
            timestamp = timestampl.toString();
            GlobalVariabel.timestamp = timestamp;
//            String childKasir = GlobalVariabel.Toko+"/"+GlobalVariabel.Kasir+"/"+timestamp;

            fromPath = FirebaseDatabase.getInstance().getReference(GlobalVariabel.Toko+"/"+GlobalVariabel.Transaksi+"/"+GlobalVariabel.uid);
            toPath = FirebaseDatabase.getInstance().getReference(GlobalVariabel.Toko+"/"+GlobalVariabel.NotaPembayaran+"/"+timestamp);
//            totalTopath = FirebaseDatabase.getInstance().getReference(childKasir+"/zzzzzzzzz");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String totalBayars;
                totalBayars = tv_totalBayar.getText().toString();

                copyRecord(fromPath,toPath,name,timestamp);

                pushData(new LogKasir(
                        name,
                        timestamp,
                        "Nota Pembayaran",
                        totalBayars));
            }


            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new NotaPembayaranFragment();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.framekasir, myFragment).addToBackStack(null).commit();



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
     * @param nama nama kasir yang melakukan transaksi
     * @param timeStamp waktu yang di konveersi ke angka
     */
    public void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath, String nama, String timeStamp) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
//                            fromPath.removeValue();
                            Toast.makeText(getActivity(), "copy sukses", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "copy failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                totalTopath.setValue(dataSnapshot.child("zzzzzzzzz").getValue());
//                toPath.child("detail").child("zzzzzzzzz").setValue(dataSnapshot.child("zzzzzzzzz").getValue());
//                toPath.child("totalTransaksi").setValue(dataSnapshot.child("zzzzzzzzz").child("total").getValue());
                toPath.child("namaKasir").setValue(nama);
                toPath.child("kodeTransaksi").setValue(timeStamp);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void pushData(LogKasir log) {
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.Log)
                .child(timestamp)
                .setValue(log);


//        etBarkod.setEnabled(true);
        Toast.makeText(getActivity(),
                "Pembayaran Berhasil",
                Toast.LENGTH_SHORT).show();

    }

}
