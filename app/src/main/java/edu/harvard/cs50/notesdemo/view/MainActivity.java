package edu.harvard.cs50.notesdemo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;

import edu.harvard.cs50.notesdemo.controller.NotesAdapter;
import edu.harvard.cs50.notesdemo.controller.SwipeToDeleteCallback;
import edu.harvard.cs50.notesdemo.databinding.ActivityMainBinding;
import edu.harvard.cs50.notesdemo.model.NotesDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static NotesDatabase database;
    private NotesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        adapter = new NotesAdapter();
        layoutManager = new LinearLayoutManager(this);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);
        // For swipe deleting
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        // Build a database with a name notes
        database = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "notes")
                .allowMainThreadQueries().build();

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.noteDao().create();
                adapter.reload();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.reload();
    }
}