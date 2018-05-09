package com.example.poxlove3.things;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity
{

    private String action;
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editor = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null)
        {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_notes));
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == android.R.id.home)
        {
            finishEditing();
            return true;
        }

        return super.onOptionsItemSelected(item);


//        int id = item.getItemId();
//
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                finish();
//                finishEditing();
//                break;
//                return true;
//        }
//        return super.onOptionsItemSelected(item);


    }

    private void finishEditing()
    {
        String newText = editor.getText().toString().trim();

        switch (action)
        {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0)
                {
                    setResult(RESULT_CANCELED);
                }
                else
                {
                    insertNote(newText);
                }
        }
        finish();
    }

    private void insertNote(String noteText)
    {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,noteText);
        getContentResolver().insert(NotesProvider.CONTENT_URI,values);
        setResult(RESULT_OK);
    }


    @Override
    public void onBackPressed()
    {
        finishEditing();
    }
}
