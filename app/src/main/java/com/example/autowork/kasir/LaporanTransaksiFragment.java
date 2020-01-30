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
import com.example.autowork.adapter.MemintaLaba;
import com.example.autowork.model.Laba;
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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaporanTransaksiFragment extends Fragment {


    public LaporanTransaksiFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database;

    private ArrayList<Laba> daftarReq;
    private MemintaLaba memintalaba;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    private TextView tv_totalLaba1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_laporan_transaksi, container, false);


        database = FirebaseDatabase.getInstance().getReference();
        tv_totalLaba1 = v.findViewById(R.id.tv_totalLaba1);

        rc_list_request = v.findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.Laba).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Wisata
                     * Dan juga menyimpan primary key pada object Wisata
                     * untuk keperluan Edit dan Delete data
                     */
                    Laba requests = noteDataSnapshot.getValue(Laba.class);
                    requests.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Wisata yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarReq.add(requests);

                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintalaba = new MemintaLaba(daftarReq, getActivity());
                rc_list_request.setAdapter(memintalaba);
                loading.dismiss();

                int totalPrice = 0;
                for (int i = 0; i<daftarReq.size(); i++)
                {
                    totalPrice +=  daftarReq.get(i).getLaba();
                }
                DecimalFormat decim = new DecimalFormat("#,###.##");
                tv_totalLaba1.setText("Rp. "+decim.format(totalPrice));

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
    //    ImageView imgdownload;
//    ConnectionClass connectionClass;
//    ArrayList<GiftitemPOJO> MyList1;
//    GiftitemPOJO giftitemPOJO;
//    Context context;
//    GiftitemPOJO name;
//    GiftitemPOJO price;
//    GiftitemPOJO url;
//    GiftitemPOJO type;
//    GiftitemPOJO date;

    Laba name;
    Laba jml;
    Laba laba;


//    class DoLOgin extends AsyncTask {
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            try {
//                Connection con = connectionClass.CONN();
//                if (con == null) {
//                } else {
//                    String query = "select * from youtable";
//                    Statement statement = con.createStatement();
//                    ResultSet rs = statement.executeQuery(query);
//                    MyList1 = new ArrayList<GiftitemPOJO>();
//                    while (rs.next()) {
//                        giftitemPOJO.setItem_name(rs.getString("item_name"));
//                        giftitemPOJO.setItem_price(rs.getString("item_price"));
//                        giftitemPOJO.setItem_URL(rs.getString("Item_URL"));
//                        giftitemPOJO.setItem_type_code(rs.getString("Item_type_code"));
//                        giftitemPOJO.setCreatedAt(rs.getString("CreatedAt"));
//                        MyList1.add(giftitemPOJO);
//
//                        giftitemPOJO = new GiftitemPOJO();
//                    }
//                }
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//
//
//        }
//    }

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

        String pdfname = "GiftItem.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{1, 1, 1});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(20);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Nama");
        table.addCell("Jumlah");
        table.addCell("Laba");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < daftarReq.size(); i++) {
            name = daftarReq.get(i);
            jml = daftarReq.get(i);
            laba = daftarReq.get(i);
            String namen = name.getNama();
            Integer jmln = jml.getJml();
            Integer laban = laba.getLaba();

            table.addCell(String.valueOf(namen));
            table.addCell(String.valueOf(jmln));
            table.addCell(String.valueOf(laban));

        }

//        System.out.println("Done");


        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL, BaseColor.GREEN);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.RED);

        PdfPTable tableHeader = new PdfPTable(new float[] { 1 });
        PdfPCell cellOne = new PdfPCell(new Phrase(new Paragraph("Laporan Laba \n\n", f)));
        cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOne.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellOne);

        PdfWriter.getInstance(document, output);
        document.open();

        document.add(tableHeader);
        document.add(new Paragraph("Pdf File Through Itext \n\n", g));
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
