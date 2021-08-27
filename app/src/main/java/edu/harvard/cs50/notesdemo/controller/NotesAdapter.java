package edu.harvard.cs50.notesdemo.controller;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.harvard.cs50.notesdemo.R;
import edu.harvard.cs50.notesdemo.databinding.NoteRowBinding;
import edu.harvard.cs50.notesdemo.model.Note;
import edu.harvard.cs50.notesdemo.view.MainActivity;
import edu.harvard.cs50.notesdemo.view.NoteActivity;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private NoteRowBinding binding;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;

        public NoteViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.container_view_row);
            textView = view.findViewById(R.id.text_view_row);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note current = (Note) v.getTag();
                    Intent intent = new Intent(v.getContext(), NoteActivity.class);

                    intent.putExtra("contents", current.getContents());
                    intent.putExtra("id", current.getId());

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = NoteRowBinding
                .inflate(LayoutInflater
                .from(parent.getContext()), parent, false);

        return new NoteViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        Note current = notes.get(position);
        holder.textView.setText(current.getContents());
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void reload() {
        notes = MainActivity.database.noteDao().getAllNotes();
        notifyDataSetChanged();
    }

    private Note recentlyDeletedItem;
    int recentlyDeletedPosition;
    public void deleteNote(int position) {

        recentlyDeletedItem = notes.get(position);
        recentlyDeletedPosition = position;

        notes.remove(position);
        notifyItemRemoved(position);

        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = binding.getRoot();
        Snackbar snackbar = Snackbar.make(view, "Are you sure you want to delete?",
                Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", v -> undoDelete());
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    deleteFromDB();
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        notes.add(recentlyDeletedPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedPosition);
    }

    public void deleteFromDB() {
        MainActivity.database.noteDao().deleteNote(recentlyDeletedItem.id);
    }
}
