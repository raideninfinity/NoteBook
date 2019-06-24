package dev.app.mobile.notebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, password2;
    String url = "http://192.168.43.189/notebook/";
    ProgressBarDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.editText3);
        password = findViewById(R.id.editText4);
        password2 = findViewById(R.id.editText5);
        dialog = new ProgressBarDialog(this);
    }

    public void backButtonHandler(View view) {
        finish();
    }

    public void registerButtonHandler(View view) {
        String str = email.getText().toString();
        String pw = password.getText().toString();
        String pw2 = password2.getText().toString();
        if (str.equals("") || pw.equals("") || pw2.equals("")) {
            Toast.makeText(getApplicationContext(), "Invalid input! Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!pw.equals(pw2))
        {
            Toast.makeText(getApplicationContext(), "Passwords did not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hash = md.toString();
            md.update((str + pw).getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            pw = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        final String email_ = str;
        final String pw_ = pw;
        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "register_action.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        if (response == null) response = "";
                        Log.e("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            Log.e(">", result);
                            String api_link = jsonObject.getString("api_link");
                            Log.e(">", api_link);
                            if (result.equals("INPUT_EMPTY"))
                                Toast.makeText(getApplicationContext(), "Invalid input! Please try again!", Toast.LENGTH_SHORT).show();
                            else if (result.equals("USER_EXIST"))
                                Toast.makeText(getApplicationContext(), "Account already exists! Please use a new email!", Toast.LENGTH_SHORT).show();
                            else if (result.equals("REG_SUCCESS")) {
                                SharedPreferences.Editor editor = getSharedPreferences("PREF", MODE_PRIVATE).edit();
                                editor.putString("api_link", api_link);
                                editor.putString("email", email_);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Sign up success!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "An error has occurred in the web service!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "An unknown error has occurred!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("Volley Error", "" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to connect to web service!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_);
                params.put("password", pw_);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
