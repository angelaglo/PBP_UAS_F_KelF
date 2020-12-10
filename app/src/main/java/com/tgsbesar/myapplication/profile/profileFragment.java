package com.tgsbesar.myapplication.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.tgsbesar.myapplication.API.LaboratoriumAPI;
import com.tgsbesar.myapplication.API.ProfileAPI;
import com.tgsbesar.myapplication.MainActivity;
import com.tgsbesar.myapplication.R;
import com.tgsbesar.myapplication.database.DatabaseClient;
import com.tgsbesar.myapplication.database.Preferences;
import com.tgsbesar.myapplication.database.User;
import com.tgsbesar.myapplication.menu_laboratorium.laboratoriumActivity;
import com.tgsbesar.myapplication.menu_laboratorium.laboratoriumNextActivity;
import com.tgsbesar.myapplication.model.Laboratorium;
import com.tgsbesar.myapplication.model.Profile;
import com.tgsbesar.myapplication.registerLogin.Login;
import com.tgsbesar.myapplication.registerLogin.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class profileFragment extends Fragment {
    private Button btn_openCam;
    private Integer id_get;
    private List<Profile> profileList;
    //shared preference activity
    public static final int mode = MODE_PRIVATE;
    private String emails,jeniskelamin;
    private TextInputEditText nama_input, alamat_input, noTelp_input, umur_input;
    private RadioGroup radioGroup;

    private FirebaseAuth firebaseAuth;
    final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    private int checkedIndex;
    private View view;
    private Profile profile;
    private Button btn_save, btn_logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Preferences preferences = new Preferences(profileFragment.this.getContext());
        emails = preferences.getEmailNorm();
        profileList = new ArrayList<Profile>();
        nama_input=view.findViewById(R.id.input_nama);
        alamat_input=view.findViewById(R.id.input_alamat);
        noTelp_input=view.findViewById(R.id.input_telp);
        umur_input=view.findViewById(R.id.input_umur);
        btn_save=view.findViewById(R.id.btn_saveProfile);
        radioGroup = view.findViewById(R.id.radGroup);
        radioGroup.setOnCheckedChangeListener(radioGrouoOnCheckedListener);
        btn_logout =view.findViewById(R.id.btn_logout);

        //open camera for profile pict
        btn_openCam=view.findViewById(R.id.btn_image);
        btn_openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),cameraActivity.class);
                startActivity(i);

            }
        });


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
                    updateProfil(id_get, namaLengkap,alamat,notelp,umur,jenis_kelamin,email);
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(getContext(),"User Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Register.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateProfil(final Integer id_get, final String namaLengkap, final String alamat, final String notelp, final String umur, final String jenis_kelamin, final String email) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data profil");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest  stringRequest = new StringRequest(PUT, ProfileAPI.URL_UPDATE+"email?email="+emails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("nama", namaLengkap);
                params.put("umur", umur);
                params.put("notelp", notelp);
                params.put("jenis_kelamin", jenis_kelamin);
                params.put("alamat", alamat);

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfil();
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
            Toast.makeText(getActivity(),"Please fill name",
                    Toast.LENGTH_SHORT).show();
            result = false;
            nama_input.setError("Name required");

        }else{
            nama_input.setError(null);
        }

        if(TextUtils.isEmpty(alamat_input.getText().toString())){
            Toast.makeText(getActivity(),"Please fill address",
                    Toast.LENGTH_SHORT).show();
            result = false;
            alamat_input.setError("Address required");

        }else{
            alamat_input.setError(null);
        }

        if(TextUtils.isEmpty(umur_input.getText().toString())){
            Toast.makeText(getActivity(),"Please fill age",
                    Toast.LENGTH_SHORT).show();
            result = false;
            umur_input.setError("Age required");
        }else{
            umur_input.setError(null);
        }

        if(TextUtils.isEmpty(noTelp_input.getText().toString())){
            Toast.makeText(getActivity(),"Please fill telp number",
                    Toast.LENGTH_SHORT).show();
            noTelp_input.setError("Number Required");
            result = false;
        }else if (noTelp_input.getText().toString().length()<10||noTelp_input.getText().toString().length()>13){
            Toast.makeText(getActivity(),"Number invalid",
                    Toast.LENGTH_SHORT).show();
            result = false;
            noTelp_input.setError("Number invalid");
        }else{
            noTelp_input.setError(null);
        }

        return result;
    }

    public void getProfil() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data profile");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //select data
        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, ProfileAPI.URL_SELECT+"email?email="+emails
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                System.out.println(response.toString());
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONObject jsonObject = response.getJSONObject("data");

                    id_get = jsonObject.optInt("id");
                    String nama_get = jsonObject.optString("nama");
                    String umur_get = jsonObject.optString("umur");
                    String gender_get = jsonObject.optString("jenis_kelamin");
                    String noTelp_get = jsonObject.optString("notelp");
                    String alamat_get = jsonObject.optString("alamat");

                    nama_input.setText(nama_get);
                    umur_input.setText(umur_get);
                    if(gender_get.equalsIgnoreCase("Perempuan")){
                        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
                    }else{
                        ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
                    }
                    noTelp_input.setText(noTelp_get);
                    alamat_input.setText(alamat_get);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }



}