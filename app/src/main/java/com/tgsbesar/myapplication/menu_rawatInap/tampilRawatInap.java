package com.tgsbesar.myapplication.menu_rawatInap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.tgsbesar.myapplication.API.transaksiLaboratoriumAPI;
import com.tgsbesar.myapplication.API.transaksiRInapAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.database.Preferences;
import com.tgsbesar.myapplication.menu_laboratorium.PdfViewModelLab;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;
import com.tgsbesar.myapplication.menu_rawatJalan.Dokter;
import com.tgsbesar.myapplication.model.KelasKamar;
import com.tgsbesar.myapplication.model.transaksiLaboratorium;
import com.tgsbesar.myapplication.model.transaksiRawatInap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.android.volley.Request.Method.GET;

public class tampilRawatInap extends AppCompatActivity {

    //pdf
    private PdfViewModelLab pdfViewModel;
    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private File pdfFile;
    private PdfWriter writer;
    private AlertDialog.Builder builder;

    String  email;
    List<transaksiRawatInap> listTransaksi;
    transaksiRawatInap transaksiRawatInap;
    private TextView txt_TipeKamar, txt_Harga, txt_Tanggal, txt_noBooking, txt_tanggal;
    private String id_transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rawat_inap);
        pdfViewModel = new ViewModelProvider(this).get(PdfViewModelLab.class);
        listTransaksi = new ArrayList<transaksiRawatInap>();
        Preferences preferences = new Preferences(tampilRawatInap.this.getApplicationContext());
        email = preferences.getEmailNorm();

        KelasKamar kmr = (KelasKamar) getIntent().getSerializableExtra("KelasKamar");
        String tanggal = getIntent().getStringExtra("Tanggal");
        // String no_book = getIntent().getStringExtra("no_book");

        txt_TipeKamar = (TextView) findViewById(R.id.txtKelasKamar);
        // txt_Fasilitas = (TextView) findViewById(R.id.txtFasilitas);
        txt_Harga = (TextView)findViewById(R.id.txtHarga) ;
        txt_tanggal = (TextView) findViewById(R.id.txtTanggal);
        txt_noBooking = (TextView) findViewById(R.id.txtNoBookingRI);


        getRawatInap();

        //button untuk cetak pdf setelah selesai cetak masuk ke main activity
        Button btn_cetak = (Button)findViewById(R.id.buttonSendRI);
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Toast.makeText(tampilRawatInap.this,"Cetak bukti pendaftaran dapat dilihat pada file Download", Toast.LENGTH_LONG).show();
                Intent i = new Intent(tampilRawatInap.this, MainActivity.class);
                createNotificationChannel();
                addNotification();
                startActivity(i);
            }
        });

        Button btn_edit = (Button)findViewById(R.id.buttonEdit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tampilRawatInap.this,editRawatInap.class);
                i.putExtra("id",id_transaksi);
                startActivity(i);
            }
        });

    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {
        //isikan code createPdfWrapper()
        int hasWriteStoragePermission = 0;
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Izinkan aplikasi untuk akses penyimpanan?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new
                                                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

    private void createPdf() throws FileNotFoundException, DocumentException{
        //isikan code createPdf()
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Download/");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Direktori baru untuk file pdf berhasil dibuat");
        }

        //nama file pdf
        String pdfname = "TransaksiRawatInap"+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
        writer = PdfWriter.getInstance(document, output);
        document.open();

        //judul dokumen
        Paragraph judul = new Paragraph(" Data Transaksi Rawat Inap \n\n", new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16,
                com.itextpdf.text.Font.BOLD, BaseColor.BLACK));
        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);

        //tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);

        //paragraf kepada
        Paragraph Kepada= new Paragraph(
                "Kepada Yth : \n" + email +"\n",
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK)
        );
        cellSupplier.addElement(Kepada);
        tables.addCell(cellSupplier);
        PdfPCell cellPO = new PdfPCell();

        //paragraf nomor & tanggal
        Paragraph NomorTanggal = new Paragraph(
                "No : " + "73489" + "\n\n" +
                        "Tanggal : " + "10/12/2020" + "\n",
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                        com.itextpdf.text.Font.NORMAL, BaseColor.BLACK)
        );
        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);
        document.add(tables);
        com.itextpdf.text.Font f = new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut merupakan data rawat inap yang dipesan : \n\n",f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);

        //table
        PdfPTable tableHeader = new PdfPTable(new float[]{3,3,3,3});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);

        //bikin header buat data
        PdfPCell h1 = new PdfPCell(new Phrase("Kelas Kamar"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Harga Kamar"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Tanggal"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        PdfPCell h5 = new PdfPCell(new Phrase("No Booking"));
        h5.setHorizontalAlignment(Element.ALIGN_CENTER);
        h5.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h4);
        tableHeader.addCell(h5);
        PdfPCell[] cells = tableHeader.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        document.add(tableHeader);

        //isi tabel
        PdfPTable tableData = new PdfPTable(new float[]{3,3,3,3});
        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        int arrLength = listTransaksi.size();
        for(int x=0;x<arrLength;x++){
            for(int i=0;i<cells.length;i++){
                if(i==0){
                    tableData.addCell(listTransaksi.get(x).getKelas());
                }else if(i==1){
                    tableData.addCell("Rp " + String.valueOf(listTransaksi.get(x).getHarga_kamar()));
                }else if(i==2){
                    tableData.addCell(listTransaksi.get(x).getTanggal_masuk());
                }else if(i==3) {
                    tableData.addCell(id_transaksi);
                }
            }
        }
        document.add(tableData);

        //terakhir
        com.itextpdf.text.Font h = new
                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String tglDicetak = sdf.format(currentTime);
        Paragraph P = new Paragraph("\nDicetak tanggal " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        previewPdf();

    }

    private void previewPdf() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".provider",
                        pdfFile);
            } else {
                uri = Uri.fromFile(pdfFile);
            }
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //TODO 2.6 - Sesuaikan package dengan package yang anda buat
            getApplicationContext().grantUriPermission("com.tgsbesar.myapplication.menu_laboratorium", uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        } else {
            FancyToast.makeText(getApplicationContext(),"Unduh pembuka PDF untuk menampilkan file ini",
                    FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private String CHANNEL_ID = "Channel 1";

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Channel 1";

            String description = "This is Channel 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void addNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo_background)
                .setContentTitle("Pendaftaran Rawat Inap Sukses!")
                .setContentText("No Booking anda "+id_transaksi)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent= new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


    //mengambil data rawatInap
    public void getRawatInap() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data laboratorium");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, transaksiRInapAPI.URL_SELECT +"email?email="+email
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONObject jsonArray = response.getJSONObject("data");



                    String kelas_kamar         = jsonArray.getString("kelas_kamar");
                    Double harga_kamar         = Double.valueOf(jsonArray.getString("harga_kamar"));
                    String tgl_rinap           = jsonArray.getString("tgl_rinap");
                    String id                  = jsonArray.getString("id");

                    transaksiRawatInap transaksiRawatInap = new transaksiRawatInap(kelas_kamar,harga_kamar,tgl_rinap,Integer.valueOf(id));


                    //     System.out.println(transaksiLaboratorium.getPaket_checkUp());

                    txt_TipeKamar.setText(transaksiRawatInap.getKelas());
                    txt_noBooking.setText(String.valueOf(id));
                    txt_Harga.setText(String.valueOf(transaksiRawatInap.getHarga_kamar()));
                    txt_tanggal.setText(transaksiRawatInap.getTanggal_masuk());
                    id_transaksi=id;
                    listTransaksi.clear();
                    listTransaksi.add(transaksiRawatInap);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(tampilRawatInap.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(tampilRawatInap.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}