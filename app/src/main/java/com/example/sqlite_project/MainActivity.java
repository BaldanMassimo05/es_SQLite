package com.example.sqlite_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlite_project.Helper;

public class MainActivity extends AppCompatActivity {
    private EditText nome;
    private TextView output;
    private Helper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.name_input);
        Button bottone_invio = findViewById(R.id.bottone_invio);
        Button bottone_show = findViewById(R.id.bottone_show);
        Button bottone_delete = findViewById(R.id.bottone_delete);
        output = findViewById(R.id.output_db);

        dbHelper = new Helper(this);

        bottone_invio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStringToDatabase();
            }
        });

        bottone_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayStrings();
            }
        });

        bottone_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllRecords();
            }
        });
    }

    private void saveStringToDatabase() {
        String inputString = nome.getText().toString();

        if (!inputString.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Helper.COLUMN_STRING, inputString);
            long newRowId = db.insert(Helper.TABLE_NAME, null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Stringa salvata con successo", Toast.LENGTH_SHORT).show();
                nome.setText("");
            } else {
                Toast.makeText(this, "Errore nel salvataggio della stringa", Toast.LENGTH_SHORT).show();
            }

            db.close();
        } else {
            Toast.makeText(this, "Inserisci una stringa prima di salvare", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayStrings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {Helper.COLUMN_STRING};
        Cursor cursor = db.query(
                Helper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder result = new StringBuilder("Stringhe salvate:\n");

        while (cursor.moveToNext()) {
            String savedString = cursor.getString(cursor.getColumnIndexOrThrow(Helper.COLUMN_STRING));
            result.append(savedString).append("\n");
        }

        cursor.close();
        db.close();

        output.setText(result.toString());
    }

    private void deleteAllRecords() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Helper.TABLE_NAME, null, null);
        Toast.makeText(this, "Tutti i record eliminati con successo", Toast.LENGTH_SHORT).show();
        db.close();
        output.setText("");
    }
}