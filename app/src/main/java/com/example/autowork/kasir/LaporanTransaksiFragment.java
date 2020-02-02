package com.example.autowork.kasir;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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

    private TextView tv_totalLaba, tv_totalTransaksi, tv_date1, tv_date2;
    private String TotalTransaksi,TotalLaba, timestamp1, timestamp2;
    private Integer timestamp11, timestamp22;

    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_laporan_transaksi, container, false);

                database = FirebaseDatabase.getInstance().getReference();
        tv_totalLaba = v.findViewById(R.id.tv_totalLaba);
        tv_totalTransaksi = v.findViewById(R.id.tv_totalTransaksi);
        tv_date1 = v.findViewById(R.id.tv_tanggal1);
        tv_date2 = v.findViewById(R.id.tv_tanggal2);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        tv_date1.setText(formattedDate);
        tv_date2.setText(formattedDate);

        long timestampl = c.getTime()/1000;
        timestamp1 = String.valueOf(timestampl);
        timestamp2 = String.valueOf(timestampl);

        timestamp11 = Integer.valueOf(timestamp1);
        timestamp22 = Integer.valueOf(timestamp2);


        getLaporan(timestamp11,timestamp22);



//        rc_list_request = v.findViewById(R.id.rc_list_request);
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        rc_list_request.setLayoutManager(mLayoutManager);
//        rc_list_request.setItemAnimator(new DefaultItemAnimator());








        v.findViewById(R.id.btn_pdf).setOnClickListener((view) -> {

            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }


        });



        v.findViewById(R.id.tv_tanggal1).setOnClickListener((view) -> {

            date1();

        });

        v.findViewById(R.id.tv_tanggal2).setOnClickListener((view) -> {

            date2();

        });


        return v;
    }


    private void getLaporan(Integer timestamp11, Integer timestamp22){

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.NotaPembayaran).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    if(noteDataSnapshot.child("tanggalTransaksi").exists()){
                        if(Integer.valueOf(noteDataSnapshot.child("tanggalTransaksi").getValue(String.class)) >= timestamp11 & Integer.valueOf(noteDataSnapshot.child("tanggalTransaksi").getValue(String.class)) <= timestamp22){

                            Laba requests = noteDataSnapshot.getValue(Laba.class);
                            requests.setKey(noteDataSnapshot.getKey());

                            daftarReq.add(requests);
//                            break;
                        }
                    }


                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
//                memintalaba = new MemintaLaba(daftarReq, getActivity());
//                rc_list_request.setAdapter(memintalaba);
                loading.dismiss();

                int totalPrice = 0,totalPrice1 = 0;
                for (int i = 0; i<daftarReq.size(); i++)
                {
                    totalPrice +=  daftarReq.get(i).getTotalTransaksi();
                    totalPrice1 +=  daftarReq.get(i).getTotalLaba();
                }
                DecimalFormat decim = new DecimalFormat("#,###.##");
                TotalTransaksi = "Rp. "+decim.format(totalPrice);
                TotalLaba = "Rp. "+decim.format(totalPrice1);

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

    Laba kodeTransaksi;
    Laba namaKaryawan;
    Laba namaKasir;
    Laba totalTransaksi;
    Laba totalLaba;
    Laba tanggal;


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

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c);

        String pdfname = formattedDate+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{1, 2, 2, 2, 2, 2});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(20);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Kode");
        table.addCell("Karyawan");
        table.addCell("Kasir");
        table.addCell("Transaksi");
        table.addCell("Laba");
        table.addCell("Tanggal");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        for (int i = 0; i < daftarReq.size(); i++) {
            kodeTransaksi = daftarReq.get(i);
            namaKaryawan = daftarReq.get(i);
            namaKasir = daftarReq.get(i);
            totalTransaksi = daftarReq.get(i);
            totalLaba = daftarReq.get(i);
            tanggal = daftarReq.get(i);
            DecimalFormat decim = new DecimalFormat("#,###.##");
            String kodeTransaksin = kodeTransaksi.getKey();
            String namaKaryawann = namaKaryawan.getNamaKaryawan();
            String namaKasirn = namaKasir.getNamaKasir();
            String totalTransaksin = "Rp. "+decim.format(totalTransaksi.getTotalTransaksi());
            String totalLaban = "Rp. "+decim.format(totalLaba.getTotalLaba());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(tanggal.getTanggalTransaksi())*1000));
            String tanggaln = dateString;


            table.addCell(String.valueOf(kodeTransaksin));
            table.addCell(String.valueOf(namaKaryawann));
            table.addCell(String.valueOf(namaKasirn));
            table.addCell(String.valueOf(totalTransaksin));
            table.addCell(String.valueOf(totalLaban));
            table.addCell(String.valueOf(tanggaln));

        }
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell(TotalTransaksi);
        table.addCell(TotalLaba);
        table.addCell(" ");
//        System.out.println("Done");


        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL, BaseColor.GRAY);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.RED);

        PdfPTable tableHeader = new PdfPTable(new float[] { 1 });
        PdfPCell cellOne = new PdfPCell(new Phrase(new Paragraph("Laporan Transaksi \n\n", f)));
        cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOne.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellOne);

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


    private void date1(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        tv_date1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));

                        combinedCal.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(combinedCal.getTime());
                        tv_date1.setText(formattedDate);

                        combinedCal.set(Calendar.HOUR_OF_DAY, 23);
                        combinedCal.set(Calendar.MINUTE, 59);

                        long timestampl = combinedCal.getTimeInMillis()/1000;
                        timestamp1 = String.valueOf(timestampl);

                        timestamp11 = Integer.valueOf(timestamp1);
                        getLaporan(timestamp11,timestamp22);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void date2(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        tv_date2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));

                        combinedCal.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(combinedCal.getTime());
                        tv_date2.setText(formattedDate);

                        combinedCal.set(Calendar.HOUR_OF_DAY, 23);
                        combinedCal.set(Calendar.MINUTE, 59);

                        long timestampl = combinedCal.getTimeInMillis()/1000;
                        timestamp2 = String.valueOf(timestampl);

                        timestamp22 = Integer.valueOf(timestamp2);
                        getLaporan(timestamp11,timestamp22);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
