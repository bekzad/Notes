package edu.harvard.cs50.notesdemo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "contents")
    public String contents;

    public int getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }
}
