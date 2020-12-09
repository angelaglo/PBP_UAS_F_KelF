package com.tgsbesar.myapplication.registerLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tgsbesar.myapplication.API.ProfileAPI;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.database.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class popUpProfile extends AppCompatActivity {

    private String emails,jeniskelamin;
    private TextInputEditText nama_input, alamat_input, noTelp_input, umur_input;
    private RadioGroup radioGroup;
    private RadioButton radio_female, radio_male;
    private FirebaseAuth firebaseAuth;
    private int checkedIndex;
    private Button btn_save, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_profile);

        Preferences preferences = new Preferences(popUpProfile.this.getApplicationContext());
        emails = preferences.getEmailNorm();

        nama_input=findViewById(R.id.input_nama);
        alamat_input=findViewById(R.id.input_alamat);
        noTelp_input=findViewById(R.id.input_telp);
        umur_input=findViewById(R.id.input_umur);
        btn_save=findViewById(R.id.btn_save);

        radioGroup = findViewById(R.id.radGroup);
        radio_female = findViewById(R.id.rd_perempuan);
        radio_male = findViewById(R.id.rd_laki2);
        radioGroup.setOnCheckedChangeListener(radioGrouoOnCheckedListener);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaLengkap = nama_input.getText().toString();
                String alamat = alamat_input.getText().toString();
                String notelp = noTelp_input.getText().toString();
                String umur = umur_input.getText().toString();
                String jenis_kelamin = jeniskelamin;
                String email = emails;

                if(!validateForm())
                {
                    return;
                }else{
                    tambahProfil(namaLengkap,alamat,notelp,umur,jenis_kelamin,email);
                    Intent i = new Intent(popUpProfile.this, Login.class);
                    startActivity(i);
                }
            }
        });
    }

    RadioGroup.OnCheckedChangeListener radioGrouoOnCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
            checkedIndex =  radioGroup.indexOfChild(checkedRadioButton);
            if(checkedIndex == 0){
                jeniskelamin = "Perempuan";
            }else{
                jeniskelamin = "Laki-laki";
            }
        }
    };

    //validate profile field
    private boolean validateForm() {
        boolean result = true;

        if(TextUtils.isEmpty(nama_input.getText().toString())){
            Toast.makeText(this,"Please fill name",
                    Toast.LENGTH_SHORT).show();
            result = false;
            nama_input.setError("Name required");

        }else{
            nama_input.setError(null);
        }

        if(radio_female.isChecked() || radio_male.isChecked()){

        }else{
            Toast.makeText(this,"Please fill gender",
                    Toast.LENGTH_SHORT).show();
            result = false;
        }

        if(TextUtils.isEmpty(alamat_input.getText().toString())){
            Toast.makeText(this,"Please fill address",
                    Toast.LENGTH_SHORT).show();
            result = false;
            alamat_input.setError("Address required");

        }else{
            alamat_input.setError(null);
        }

        if(TextUtils.isEmpty(umur_input.getText().toString())){
            Toast.makeText(this,"Please fill age",
                    Toast.LENGTH_SHORT).show();
            result = false;
            umur_input.setError("Age required");
        }else{
            umur_input.setError(null);
        }

        if(TextUtils.isEmpty(noTelp_input.getText().toString())){
            Toast.makeText(this,"Please fill telp number",
                    Toast.LENGTH_SHORT).show();
            noTelp_input.setError("Phone number Required");
            result = false;
        }else if (noTelp_input.getText().toString().length()<10||noTelp_input.getText().toString().length()>13){
            Toast.makeText(this,"Number invalid",
                    Toast.LENGTH_SHORT).show();
            result = false;
            noTelp_input.setError("Number invalid");
        }else{
            noTelp_input.setError(null);
        }

        return result;
    }

    public void tambahProfil(final String namaLengkap, final String alamat, final String noTelp, final String umur, final String jeniskelamin, final String email){
        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data profil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, ProfileAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    Toast.makeText(popUpProfile.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(popUpProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama", namaLengkap);
                params.put("email", email);
                params.put("umur", umur);
                params.put("jenis_kelamin",jeniskelamin);
                params.put("notelp",noTelp);
                params.put("alamat",alamat);

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}