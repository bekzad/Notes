package edu.harvard.cs50.notesdemo.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import edu.harvard.cs50.notesdemo.databinding.ActivityNoteBinding;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String contents = getIntent().getStringExtra("contents");
        id = getIntent().getIntExtra("id", 0);

        binding.noteEditText.setText(contents);
    }

    public void deleteNote(View view) {
        MainActivity.database.noteDao().deleteNote(id);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.database.noteDao().save(binding.noteEditText.getText().toString(), id);
    }
}