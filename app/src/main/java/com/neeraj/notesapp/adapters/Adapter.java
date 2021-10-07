package com.neeraj.notesapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neeraj.notesapp.R;
import com.neeraj.notesapp.models.Model;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;

    List<Model> notesList;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public Adapter(List<Model> notesList, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.notesList = notesList;
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
        context = holder.itemView.getContext();
        final Model model = notesList.get(position);
        try {
            if (!TextUtils.isEmpty(model.getTitle()) && !TextUtils.isEmpty(model.getDescription())) {

                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
            }

            holder.layout.setTag(notesList);
        } catch (Exception e) {
            Toast.makeText(context, "Some error occured..", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public List<Model> getList() {
        return notesList;
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Model item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
