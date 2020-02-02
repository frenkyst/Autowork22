package com.example.autowork.kasir;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.adapter.MemintaNota;
import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private TextView tv_namaKasir, tv_namaKaryawan,tv_kode,tv_tanggal,tv_totalNota;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nota_pembayaran, container, false);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = v.findViewById(R.id.rc_list_request);
        tv_namaKasir = v.findViewById(R.id.tv_namaKasir);
        tv_namaKaryawan = v.findViewById(R.id.tv_namaKaryawan);
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

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.NotaPembayaran).child(GlobalVariabel.NamaTransaksi).addValueEventListener(new ValueEventListener() {
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
                    String namaKaryawan1 = dataSnapshot.child("namaKaryawan").getValue(String.class);
                    GlobalVariabel.timestamp = dataSnapshot.child("tanggalTransaksi").getValue(String.class);
                    String kode1 = dataSnapshot.getKey();

                    tv_namaKasir.setText(namaKasir1);
                    tv_namaKaryawan.setText(namaKaryawan1);
                    tv_kode.setText(kode1);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long.parseLong(GlobalVariabel.timestamp)*1000));
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
                    totalPrice +=  daftarReq.get(i).getTotal();
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


        v.findViewById(R.id.btn_pdf).setOnClickListener((view) -> {

            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }


        });

        return v;
    }

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;

    Meminta nama1;
    Meminta jumlah1;
    Meminta hargaJual1;
    Meminta total1;

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c);

        String pdfname = formattedDate+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{1, 1, 1, 1});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(20);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Nama Barang");
        table.addCell("Jumlah");
        table.addCell("Harga");
        table.addCell("Total");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < daftarReq.size(); i++) {
            nama1 = daftarReq.get(i);
            jumlah1 = daftarReq.get(i);
            hargaJual1 = daftarReq.get(i);
            total1 = daftarReq.get(i);
            DecimalFormat decim = new DecimalFormat("#,###.##");
            String naman = nama1.getNama();
            Integer jumlahn = jumlah1.getJml();
            String hargaJualn = "Rp. "+decim.format(hargaJual1.getHargajual());
            String totaln = "Rp. "+decim.format(total1.getTotal());

            table.addCell(String.valueOf(naman));
            table.addCell(String.valueOf(jumlahn));
            table.addCell(String.valueOf(hargaJualn));
            table.addCell(String.valueOf(totaln));

        }
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(tv_totalNota.getText().toString());
//        System.out.println("Done");


        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL, BaseColor.GRAY);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.RED);

        PdfPTable tableHeader = new PdfPTable(new float[] { 1, 1 });
        tableHeader.getDefaultCell().setFixedHeight(20);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        tableHeader.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        PdfPCell cellOne = new PdfPCell(new Phrase(new Paragraph("Nota Transaksi \n", f)));
        cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setColspan(2);
        PdfPCell cellTanggal = new PdfPCell(new Phrase(new Paragraph(tv_tanggal.getText().toString())));
        cellTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTanggal.setBorder(Rectangle.NO_BORDER);

//        tableHeader.addCell(" ");
        tableHeader.addCell(cellOne);
//        tableHeader.addCell(" ");
        tableHeader.addCell("Kode : "+tv_kode.getText().toString());
//        tableHeader.addCell(" ");
        tableHeader.addCell(cellTanggal);
        tableHeader.addCell("Nama Kasir : "+tv_namaKaryawan.getText().toString());
//        tableHeader.addCell(" ");
        tableHeader.addCell(" ");
        tableHeader.addCell("Nama Karyawan : "+tv_namaKasir.getText().toString());
//        tableHeader.addCell(" ");
        tableHeader.addCell(" ");
        tableHeader.addCell(" ");
        tableHeader.addCell(" ");

        PdfWriter.getInstance(document, output);
        document.open();

        document.add(tableHeader);
//        document.add(new Paragraph("Pdf File Through Itext \n\n", f));
        document.add(table);

//        for (int i = 0; i < MyList1.size(); i++) {
//            document.add(new Paragraph(String.valueOf(MyList1.get(i))));
//        }
        document.close();
        Log.e("safiya", daftarReq.toString());
        previewPdf();
    }

    private void previewPdf() {

        PackageManager packageManager = getActivity().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            getActivity().startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

}
