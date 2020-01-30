package com.example.autowork;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.autowork.model.Masuk;
import com.example.autowork.adapter.MemintaView;
import com.example.autowork.kasir.NotaPembayaranFragment;
import com.example.autowork.model.LogHistory;
import com.example.autowork.model.Meminta;
import com.example.autowork.MainActivity;
import com.example.autowork.model.TransaksiKaryawan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//import com.google.zxing.Result;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiKaryawanFragment extends Fragment{
    public static String SNamaTransaksi;

//    private ZXingScannerView zXingScannerView;

    public TransaksiKaryawanFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database0;

    private ArrayList<Meminta> daftarReq;
    private MemintaView memintaview;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    public static EditText etBarkod, etNamaTransaksi;

    private String hasilbarkod;
//    public  String SNamaTransaksi;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Data NULL", Toast.LENGTH_SHORT).show();
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
        integrator.initiateScan();
    }



    @Override
    public View onCreateView  (LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vt = inflater.inflate(R.layout.fragment_transaksi_karyawan, container, false);

        GlobalVariabel.GoneTapEdit = "tidak";


        etNamaTransaksi = vt.findViewById(R.id.et_NamaTransaksi);
        etBarkod = vt.findViewById(R.id.et_barkod);

        database0 = FirebaseDatabase.getInstance().getReference();

        rc_list_request = vt.findViewById(R.id.rc_list_request);
        //fab_add = findViewById(R.id.fab_add);


        recycleView();


        if (GlobalVariabel.NamaTransaksi!="null"){
            etNamaTransaksi.setText(GlobalVariabel.NamaTransaksi);
        }
        if (GlobalVariabel.KataKunci!="null"){
            etBarkod.setText(GlobalVariabel.KataKunci);
        }

//        etNamaTransaksi.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                SNamaTransaksi = etNamaTransaksi.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        // TOMBOL BARKODE
        vt.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });



        return vt;
    }


    /**
     * =====================================================================================================================================(STAR)
     * PROSES VERIFIKASI BARKOD APAKAH ADA DI DATABASE ATAU TIDAK
     */
    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) { /** JIKA BARCOD TIDAK DIISI MAKA AKAN MENGEKSEKUSI FUNGSI SCAN */
            scane();

        } else { /** JIKA DIISI MAKA AKAH DILAKUKAN PENCARIAN DATABASE */

            GlobalVariabel.KataKunci = sBarkod;

            recycleView();

        }
    }

    /**
     * =====================================================================================================================================(END)





    /**
     * ==============================================================================================(STAR)
     * KETIKA SCAN DATA TIDAK DITEMUKAN MUNCUL DIALOG INI
     */
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

    /**
     * ==============================================================================================(STAR)
     */


    private void recycleView(){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database0.child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */

                String sBarkod = etBarkod.getText().toString();

                if (sBarkod.equals("")){
                    daftarReq = new ArrayList<>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
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
                    }
                } else {

                    daftarReq = new ArrayList<>();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        if(noteDataSnapshot.getKey().equals(GlobalVariabel.KataKunci)){
                            daftarReq.clear();
                            daftarReq.add(dataSnapshot.child(GlobalVariabel.KataKunci).getValue(Meminta.class));
//                            break;
                        } else if(noteDataSnapshot.child("nama").getValue(String.class).toLowerCase().contains(GlobalVariabel.KataKunci)) {
//                            daftarReq.clear();
                            String Key;
                            Key = noteDataSnapshot.getKey();

                            daftarReq.add(dataSnapshot.child(Key).getValue(Meminta.class));
//                            break;
                        }
                    }

                }


                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintaview = new MemintaView(daftarReq, getActivity());
                rc_list_request.setAdapter(memintaview);
                loading.dismiss();

                if (memintaview.getItemCount() == 0){
                    Toast.makeText(getActivity(),
                            "Barang Tidak Ada ??",
                            Toast.LENGTH_SHORT).show();
                }
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
    }




}

