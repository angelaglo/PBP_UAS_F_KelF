package com.tgsbesar.myapplication.menu_laboratorium;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tgsbesar.myapplication.API.transaksiLaboratoriumAPI;
import com.tgsbesar.myapplication.API.transaksiRInapAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.menu_rawatInap.editRawatInap;
import com.tgsbesar.myapplication.model.Laboratorium;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class editTransaksiLab extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String jam, no_booking ,strDate;
    Integer id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaksi_lab);

        id = Integer.valueOf((String) getIntent().getSerializableExtra("id"));


        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int Month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);

        Button btnDate = findViewById(R.id.buttonPickDateCheckUp);
        TextView tanggal = findViewById(R.id.tv_dateCheckUp);
        Button btnSend = findViewById(R.id.buttonSend);
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

        AutoCompleteTextView drop = findViewById(R.id.dropdown_fill_CheckUp);
        List<String> categories = new ArrayList<String>();
        categories.add("09:00 - 11:00");
        categories.add("13:00 - 15:00");
        categories.add("17:00 - 19:00");
        categories.add("21:00 - 22:00");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, categories);
        drop.setAdapter(adapter);
        drop.setOnItemClickListener(this);



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTransaksi(strDate,jam);
            }
        });

        Button btn_delete = findViewById(R.id.buttonDelete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batalkanTransaksi(String.valueOf(id));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        jam = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Jam " +jam, Toast.LENGTH_SHORT).show();
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


    public void editTransaksi(final String tgl_checkUp, final String jam_checkUp){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data transaksi");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest  stringRequest = new StringRequest(PUT, transaksiLaboratoriumAPI.URL_UPDATE + String.valueOf(id) ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(editTransaksiLab.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    System.out.println(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(editTransaksiLab.this, tampilLaboratorium.class);
                i.putExtra("id",id);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(editTransaksiLab.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("tgl_checkUp", tgl_checkUp);
                params.put("jam_checkUp", jam_checkUp);
                System.out.println(tgl_checkUp);
                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    //Fungsi menghapus data transaksi
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
        StringRequest stringRequest = new StringRequest(DELETE, transaksiLaboratoriumAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(editTransaksiLab.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(editTransaksiLab.this, MainActivity.class);

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
                Toast.makeText(editTransaksiLab.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}