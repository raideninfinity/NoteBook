package dev.app.mobile.notebook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAdapterNoteList extends RecyclerView.Adapter<CustomAdapterNoteList.ViewHolder>{
    List<NotesDBModel> list;
    private static ClickListener clickListener;
    public CustomAdapterNoteList(List<NotesDBModel> models) {
        ReloadList(models);
    }

    public void ReloadList(List<NotesDBModel> models){
        this.list = models;
        Collections.sort(this.list, new Comparator<NotesDBModel>() {
            @Override
            public int compare(NotesDBModel t1, NotesDBModel t2) {
                return t2.getEditDate().compareTo(t1.getEditDate());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_recycler,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterNoteList.ViewHolder holder, int position) {
        if (position == 0) {
            holder.col1.setText("");
            holder.col2.setText("");
            holder.col3.setText("New Note");
        }
        else {
            NotesDBModel model = list.get(position - 1);
            String str = model.getContent();
            String[] lines = str.split(System.lineSeparator(), 3);
            holder.col1.setText(lines[0]);
            if (lines.length > 1) {
                holder.col2.setText(lines[1]);
            }
            else {
                holder.col2.setText("");
            }
            holder.col3.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView col1, col2, col3;


        public ViewHolder(View v) {
            super(v);
            col1 = v.findViewById(R.id.col1);
            col2 = v.findViewById(R.id.col2);
            col3 = v.findViewById(R.id.col3);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CustomAdapterNoteList.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public NotesDBModel getItem(int position) {
        return list.get(position - 1);
    }

    public void deleteItem(int pos) {
        list.remove(pos - 1);
    }
}
