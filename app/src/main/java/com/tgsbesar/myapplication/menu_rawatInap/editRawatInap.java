package com.tgsbesar.myapplication.menu_rawatInap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.RawatInapAPI;
import com.tgsbesar.myapplication.API.transaksiLaboratoriumAPI;
import com.tgsbesar.myapplication.API.transaksiRInapAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.database.Preferences;
import com.tgsbesar.myapplication.home.homeFragment;
import com.tgsbesar.myapplication.menu_laboratorium.editTransaksiLab;
import com.tgsbesar.myapplication.menu_laboratorium.tampilLaboratorium;
import com.tgsbesar.myapplication.model.KelasKamar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class editRawatInap extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String jam, no_booking,email ,strDate;
    private String id="";
    private String kelasKamar="";
    private Double hargaKamar=0.0;
    private ArrayList<KelasKamar> listKamar = new ArrayList<>();
    private AutoCompleteTextView drop;

    private ArrayList<String>namaKelas= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rawat_inap);
        Preferences preferences = new Preferences(editRawatInap.this.getApplicationContext());
        email = preferences.getEmailNorm();
        id = (String) getIntent().getSerializableExtra("id");

        getKamar();
        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);

        Button btnDate = findViewById(R.id.buttonPickDateCheckUp);
        TextView tanggal = findViewById(R.id.tv_dateCheckUp);
        Button btnSend = findViewById(R.id.buttonSendERI);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        strDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                        tanggal.setText(strDate);
                    }
                },Year, Month, Day);

                dialog.show(getFragmentManager(),"DatePickerDialog");
            }
        });

        drop = findViewById(R.id.dropdown_fill_Kelas);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTransaksi(kelasKamar, hargaKamar, strDate);
            }
        });

        Button btn_batal = findViewById(R.id.buttonDelete);
        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batalkanTransaksi(id);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        kelasKamar = adapterView.getItemAtPosition(i).toString();
        hargaKamar = listKamar.get(i).harga_kamar;

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
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    public void editTransaksi(final String kelas_kamar, final Double harga_kamar, final String tgl_rinap){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data rawat inap");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, transaksiRInapAPI.URL_UPDATE + String.valueOf(id) ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(editRawatInap.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    System.out.println(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(editRawatInap.this, tampilRawatInap.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(editRawatInap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("kelas_kamar", kelas_kamar);
                params.put("tgl_rinap", tgl_rinap);
                params.put("harga_kamar", String.valueOf(harga_kamar));
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    public void getKamar() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data laboratorium");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, RawatInapAPI.URL_SELECT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);


                        String tipe_kamar             = jsonObject.optString("kelas_kamar");
                        String fasilitas            = jsonObject.optString("fasilitas_kamar");
                        Double harga_kamar           = jsonObject.optDouble("harga_kamar");



                        //Membuat objek user
                        KelasKamar kamar= new KelasKamar(tipe_kamar,fasilitas,harga_kamar);

                        //Menambahkan objek user tadi ke list user
                        listKamar.add(kamar);

                        namaKelas.add(tipe_kamar);


                    }
                    ArrayAdapter<String> adapter= new ArrayAdapter<String>(editRawatInap.this,R.layout.support_simple_spinner_dropdown_item, namaKelas);
                    drop.setAdapter(adapter);
                    drop.setOnItemClickListener(editRawatInap.this);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(editRawatInap.this, response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(editRawatInap.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    //Fungsi menghapus data mahasiswa
    public void batalkanTransaksi(String id){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data transaksi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, transaksiRInapAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(editRawatInap.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(editRawatInap.this, MainActivity.class);
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(editRawatInap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }


}