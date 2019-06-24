package dev.app.mobile.notebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        email = findViewById(R.id.textViewX);
        SharedPreferences prefs = getSharedPreferences("PREF", MODE_PRIVATE);
        String str = prefs.getString("email", "");
        email.setText(str);
    }

    public void backButtonHandler(View view) {
        finish();
    }

    public void logoutButtonHandler(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Confirm sign out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Logout();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    public void Logout() {
        SharedPreferences.Editor editor = getSharedPreferences("PREF", MODE_PRIVATE).edit();
        editor.putString("api_link", null);
        editor.putString("email", null);
        editor.apply();
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        finish();
    }
}
