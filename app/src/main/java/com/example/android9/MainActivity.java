package com.example.android9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText filenameEditText;
    private EditText contentEditText;
    private TextView fileContentTextView;

    private static final String FILENAME_KEY = "filename";
    private static final String CONTENT_KEY = "content";
    private static final String FILE_CONTENT_KEY = "file_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filenameEditText = findViewById(R.id.filename_editText);
        contentEditText = findViewById(R.id.content_editText);
        fileContentTextView = findViewById(R.id.file_content_textView);

        if (savedInstanceState != null) {
            String filename = savedInstanceState.getString(FILENAME_KEY);
            String content = savedInstanceState.getString(CONTENT_KEY);
            String fileContent = savedInstanceState.getString(FILE_CONTENT_KEY);

            filenameEditText.setText(filename);
            contentEditText.setText(content);
            fileContentTextView.setText(fileContent);
        }

        filenameEditText = findViewById(R.id.filename_editText);
        contentEditText = findViewById(R.id.content_editText);
        fileContentTextView = findViewById(R.id.file_content_textView);

        Button createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(v -> createFile());

        Button appendButton = findViewById(R.id.append_button);
        appendButton.setOnClickListener(v -> appendToFile());

        Button readButton = findViewById(R.id.read_button);
        readButton.setOnClickListener(v -> readFile());

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String filename = filenameEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String fileContent = fileContentTextView.getText().toString();

        outState.putString(FILENAME_KEY, filename);
        outState.putString(CONTENT_KEY, content);
        outState.putString(FILE_CONTENT_KEY, fileContent);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String filename = savedInstanceState.getString(FILENAME_KEY);
        String content = savedInstanceState.getString(CONTENT_KEY);
        String fileContent = savedInstanceState.getString(FILE_CONTENT_KEY);

        filenameEditText.setText(filename);
        contentEditText.setText(content);
        fileContentTextView.setText(fileContent);
    }

    private void createFile() {
        String filename = filenameEditText.getText().toString();
        String content = contentEditText.getText().toString();

        File file = new File(getFilesDir(), filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.getCause();
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
        }
    }

    private void appendToFile() {
        String filename = filenameEditText.getText().toString();
        String content = contentEditText.getText().toString();

        File file = new File(getFilesDir(), filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(content);
            Toast.makeText(this, "Content appended", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.getCause();
            Toast.makeText(this, "Error appending content", Toast.LENGTH_SHORT).show();
        }
    }

    private void readFile() {
        String filename = filenameEditText.getText().toString();
        File file = new File(getFilesDir(), filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            fileContentTextView.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.getCause();
            Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile() {
        String filename = filenameEditText.getText().toString();
        File file = new File(getFilesDir(), filename);

        if (file.exists() && file.delete()) {
            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting file", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete file")
                .setMessage("Are you sure you want to delete the selected file?")
                .setPositiveButton("Yes", (dialog, which) -> deleteFile())
                .setNegativeButton("No", null)
                .show();
    }
}
