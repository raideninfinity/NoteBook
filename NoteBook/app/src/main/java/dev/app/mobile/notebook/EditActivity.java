package dev.app.mobile.notebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EditActivity extends AppCompatActivity {

    EditText editText;
    int id = 0;
    NotesDB db;
    boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new NotesDB(getApplicationContext());
        id = getIntent().getIntExtra("id", 0);
        setContentView(R.layout.activity_edit);
        editText = findViewById(R.id.editText7);
        if(id != 0) {
            NotesDBModel item = db.GetItem(id);
            editText.setText(item.getContent());
            editText.setSelection(editText.getText().length());
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                edited = true;
            }
        });
    }

    public void SaveText(String str) {
        if (!edited){ return; }
        if (id == 0) {
            id = db.NewNote(str);
        }
        else {
            db.UpdateNote(str, id);
        }
    }

    @Override
    protected void onPause() {
        SaveText(editText.getText().toString());
        super.onPause();
    }

    public void backButtonHandler(View view) {
        finish();
    }

    public void infoButtonHandler(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        String str = "";
        String text = editText.getText().toString();
        int char_count = text.trim().length();
        str += "Length: " + char_count + "\n";
        if (id != 0) {
            NotesDBModel model = db.GetItem(id);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String c_date_str = dateFormat.format(model.getCreateDate());
            String e_date_str = dateFormat.format(model.getEditDate());
            str += "Created at: " + c_date_str + "\n";
            str += "Last Edited: " + e_date_str + "\n";
        }
        builder1.setMessage(str);
        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void deleteButtonHandler(View view) {
        if (id == 0){ return; }
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Confirm delete note?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        NotesDBModel model = db.GetItem(id);
                        int id = model.getId();
                        db.DeleteItem(id);
                        Toast.makeText(EditActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                        edited = false;
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
