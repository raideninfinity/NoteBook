package dev.app.mobile.notebook;

import java.util.Date;

public class NotesDBModel {

    private String content;
    private Date create_date;
    private Date edit_date;
    private String hash_id;
    private int id;

    public NotesDBModel(int id, String c, Date cd, Date ed, String h) {
        this.id = id;
        content = c;
        create_date = cd;
        edit_date = ed;
        hash_id = h;
    }

    public NotesDBModel(){}

    public int getId(){return id;}
    public void setId(int n){id = n;}
    public String getContent(){return content;}
    public void setContent(String s){content = s;}
    public Date getCreateDate(){return create_date;}
    public void setCreateDate(Date d){create_date = d;}
    public Date getEditDate(){return edit_date;}
    public void setEditDate(Date d){edit_date = d;}
    public String getHashId(){return hash_id;}
    public void setHashId(String s){hash_id = s;}
}
