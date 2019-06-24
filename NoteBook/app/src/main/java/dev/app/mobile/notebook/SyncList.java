package dev.app.mobile.notebook;

import android.util.Log;

import java.util.ArrayList;

public class SyncList {

    public SyncList() {
    }

    private ArrayList<String> delete_list = new ArrayList<String>();
    private ArrayList<String> retrieve_list = new ArrayList<String>();
    private ArrayList<String> update_list = new ArrayList<String>();

    public ArrayList<String> DeleteList() {
        return delete_list;
    }

    public ArrayList<String> RetrieveList() {
        return retrieve_list;
    }

    public ArrayList<String> UpdateList() {
        return update_list;
    }

    public void Debug() {
        Log.e("> del", delete_list.size() + "");
        Log.e("> ret", retrieve_list.size() + "");
        Log.e("> upd", update_list.size() + "");
    }
}
