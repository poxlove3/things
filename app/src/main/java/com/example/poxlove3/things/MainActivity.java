package com.example.poxlove3.things;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        insertNote("Hello World");

//        Cursor cursor = getContentResolver().query(NotesProvider.CONTENT_URI,
//                DBOpenHelper.ALL_COLUMNS,null,null,null,null);
//
//        String[] from = {DBOpenHelper.NOTE_TEXT};
//        int[] to = {R.id.tvNote};


        cursorAdapter = new NotesCursorAdapter(this,null,0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);



















        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



        getLoaderManager().initLoader(0,null,this);
    }

    private void insertNote(String noteText)
    {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT,noteText);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI,values);
        Log.d("MainActivitysdsss","Insert notes" +  noteUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes()
    {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int button)
                    {
                        if (button == DialogInterface.BUTTON_POSITIVE)
                        {
                            getContentResolver().delete(NotesProvider.CONTENT_URI,
                                    null,null);
                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertSampleData()
    {
        insertNote("Simple note");
        insertNote("very very\nadgjshdghgdjahsdgjhsagdja");
        insertNote("hakdhskadgahsdghsjgdajsdgjashdgahjsdgjahsgdjashdgjsahdgjashdgajshdggasjhd");
        restartLoader();
    }

    private void restartLoader()
    {
        getLoaderManager().restartLoader(0,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this,NotesProvider.CONTENT_URI,
                null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorForNewNote(View view)
    {
        Intent intent = new Intent(this,EditorActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        restartLoader();
        if (requestCode == EDITOR_REQUEST_CODE && requestCode == RESULT_OK)
        {
            restartLoader();
        }
    }
}
