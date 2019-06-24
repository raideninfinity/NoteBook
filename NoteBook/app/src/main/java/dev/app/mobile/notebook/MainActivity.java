package dev.app.mobile.notebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView list1;
    ArrayList<NotesDBModel> list;
    NotesDB db;
    String api_link;
    CustomAdapterNoteList adapter;
    String url = "http://192.168.43.189/notebook/";
    ProgressBarDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadPreferences();
        setContentView(R.layout.activity_main);
        list1 = findViewById(R.id.list1);
        db = new NotesDB(getApplicationContext());
        adapter = new CustomAdapterNoteList(db.GetAllItems());
        list1.setLayoutManager(new LinearLayoutManager(this));
        list1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        final RecyclerView recyclerView = findViewById(R.id.list1);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 0) {
                            NewNote();
                        }
                        else {
                            EditNote(position);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (position == 0) {
                        }
                        else {
                            DeleteItem(position);
                        }
                    }

                    public void NewNote()
                    {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("id", 0);
                        startActivityForResult(intent, 1);
                    }

                    public void EditNote(int position)
                    {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("id", adapter.getItem(position).getId());
                        startActivityForResult(intent, 1);
                    }

                    public void DeleteItem(int position) {
                        final int pos = position;
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Confirm")
                                .setMessage("Confirm delete note?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        NotesDBModel model = adapter.getItem(pos);
                                        int id = model.getId();
                                        db.DeleteItem(id);
                                        ((CustomAdapterNoteList)recyclerView.getAdapter()).deleteItem(pos);
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                        recyclerView.getAdapter().notifyItemRemoved(pos);
                                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                })
        );
        dialog = new ProgressBarDialog(this);
        if (api_link != null) {
            DoSync();
        }
    }

    public void LoadPreferences() {
        SharedPreferences prefs = getSharedPreferences("PREF", MODE_PRIVATE);
        api_link = prefs.getString("api_link", null);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void DoSync() {
        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Retrieve Hash List
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "hash_list.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        Log.e("Response", response);
                        List<NotesDBModel> models = db.GetAllItems();
                        List<String> r_models = new ArrayList<String>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            if (result.equals("NOT_EXIST")) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Invalid account! Please sign in again!", Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("SUCCESS")) {
                                SyncList sync_list = new SyncList();
                                JSONArray temp = jsonObject.getJSONArray("hash_list");
                                int length = temp.length();
                                if (length > 0) {
                                    for (int i = 0; i < length; i++) {
                                        JSONObject temp2 = temp.getJSONObject(i);
                                        String hash_id = temp2.getString("hash_id");
                                        String edit_date = temp2.getString("edit_date");
                                        Date date = null;
                                        Date fixedDate = null;
                                        try {
                                            date = dateFormat.parse(edit_date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (db.CheckItemDel("hash_id = '" + hash_id + "'"))
                                        {
                                            sync_list.DeleteList().add(hash_id);
                                        }
                                        else {
                                            NotesDBModel model = db.FindItem("hash_id = '" + hash_id + "'");
                                            if (model == null) {
                                                sync_list.RetrieveList().add(hash_id);
                                            }
                                            else {
                                                r_models.add(model.getHashId());
                                                try {
                                                    fixedDate = dateFormat.parse(dateFormat.format(model.getEditDate()));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                if (date != null && fixedDate != null) {
                                                    long time1 = fixedDate.getTime();
                                                    long time2 = date.getTime();
                                                    if (time1 > time2)
                                                        sync_list.UpdateList().add(hash_id);
                                                    else if (time1 < time2)
                                                        sync_list.RetrieveList().add(hash_id);
                                                }
                                            }
                                        }
                                    }
                                }
                                for(NotesDBModel model : models) {
                                    if (!r_models.contains(model.getHashId()))
                                        sync_list.UpdateList().add(model.getHashId());
                                }
                                PerformSync(sync_list);
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "An error has occurred in the web service!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "An unknown error has occurred!", Toast.LENGTH_SHORT).show();
                            Log.e("error", e.getMessage());
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
                params.put("api_link", api_link);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void PerformSync(SyncList syncList) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //create json
        String json = "";
        JSONArray arr1 = new JSONArray();
        for(int i = 0; i < syncList.DeleteList().size(); i++)
            arr1.put(syncList.DeleteList().get(i));
        JSONArray arr2 = new JSONArray();
        for(int i = 0; i < syncList.RetrieveList().size(); i++)
            arr2.put(syncList.RetrieveList().get(i));
        JSONArray arr3 = new JSONArray();
        for(int i = 0; i < syncList.UpdateList().size(); i++) {
            String hash_id = syncList.UpdateList().get(i);
            NotesDBModel model = db.FindItem("hash_id = '" + hash_id + "'");
            JSONObject obj = new JSONObject();
            try {
                obj.put("hash_id", hash_id);
                obj.put("content", Base64.encodeToString(model.getContent().getBytes("UTF-8"), Base64.DEFAULT));
                obj.put("create_date", dateFormat.format(model.getCreateDate()));
                obj.put("edit_date", dateFormat.format(model.getEditDate()));
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            arr3.put(obj);
        }
        JSONObject object = new JSONObject();
        try {
            object.put("delete", arr1);
            object.put("retrieve", arr2);
            object.put("update", arr3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = object.toString();
        Log.e("> json", json);
        //syncList.Debug();
        final String request_str = json;
        //send json
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "sync_action.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
                        Log.e("Response", response);
                        List<NotesDBModel> models = db.GetAllItems();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            if (result.equals("NOT_EXIST")) {
                                Toast.makeText(getApplicationContext(), "Invalid account! Please sign in again!", Toast.LENGTH_SHORT).show();
                            }
                            else if (result.equals("SUCCESS")) {
                                ProcessResponse(jsonObject.getJSONArray("retrieve"));
                                ProcessDelete(jsonObject.getJSONArray("delete"));
                                Toast.makeText(getApplicationContext(), "Sync completed!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "An error has occurred in the web service!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "An unknown error has occurred!", Toast.LENGTH_SHORT).show();
                            Log.e("error", e.getMessage());
                        }
                        dialog.dismiss();
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
                params.put("api_link", api_link);
                params.put("request_str", request_str);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ProcessResponse(JSONArray jsonArray) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int length = jsonArray.length();
        if (length <= 0){ return; }
        for(int i = 0; i < length; i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String hash_id = obj.getString("hash_id");
                String content = obj.getString("content");
                content = new String(Base64.decode(content, Base64.DEFAULT), "UTF-8");
                Date create_date = dateFormat.parse(obj.getString("create_date"));
                Date edit_date = dateFormat.parse(obj.getString("edit_date"));

                NotesDBModel model = db.FindItem("hash_id = '" + hash_id + "'");
                if (model != null) {
                    model.setContent(content);
                    model.setEditDate(edit_date);
                    db.UpdateItem(model);
                }
                else {
                    model = new NotesDBModel();
                    model.setHashId(hash_id);
                    model.setContent(content);
                    model.setCreateDate(create_date);
                    model.setEditDate(edit_date);
                    db.InsertItem(model);
                }
            } catch (JSONException | ParseException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        adapter.ReloadList(db.GetAllItems());
        adapter.notifyDataSetChanged();
    }

    public void ProcessDelete(JSONArray jsonArray) {
        int length = jsonArray.length();
        if (length <= 0){ return; }
        for(int i = 0; i < length; i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String hash = obj.getString("hash_id");
                db.DeleteItemFromHash(hash);
            } catch (JSONException e) {
                Log.e("err_del", e.getMessage());
                e.printStackTrace();
            }
        }
        adapter.ReloadList(db.GetAllItems());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            adapter.ReloadList(db.GetAllItems());
            adapter.notifyDataSetChanged();
        }
        else if (requestCode == 5) {
            LoadPreferences();
            if (api_link != null)
                DoSync();
        }
    }

    public void accountButtonHandler(View view) {
        LoadPreferences();
        if (api_link == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("id", 0);
            startActivityForResult(intent, 5);
        }
        else {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("id", 0);
            startActivityForResult(intent, 5);
        }
    }

    public void syncButtonHandler(View view) {
        LoadPreferences();
        if (api_link == null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            String str = "Please sign in first.";
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
        else {
            DoSync();
        }
    }

}
