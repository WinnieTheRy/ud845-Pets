package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

//private PetDbHelper mDbHelper;

    // A unique identifier for this cursor loader. Can be whatever you want. Identifiers are scoped to a particular LoaderManager instance.
    private static final int PET_LOADER = 0;

    // This is the Adapter being used to display the list's data.
    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /**
         * List view for setting the empty view when no pets are added to the databse
         */
        ListView listView = (ListView) findViewById(R.id.list_View);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);

        // Make instance of cursor adapter class
        mCursorAdapter = new PetCursorAdapter(this, null);

        // Attach cursor adapter to the list view
        listView.setAdapter(mCursorAdapter);

        // Starting loader
        getLoaderManager().initLoader(PET_LOADER, null, this);

        //displayDatabaseInfo();

        //PetDbHelper mDbHelper = new PetDbHelper(CatalogActivity.this);

        /**Creates or opens an already existing sqlite database  */
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
    }

    //When new pet is added in the editor activity, the onStart() method will update catalog
    //actvity with the new pets
    @Override
    protected void onStart() {
        super.onStart();

        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);

        //The columns we want to recieve from the query
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        /**
         * READING DATA:
         * Cursor: This interface provides random read-write access to the result set returned by a database query.
         */
        //This is the query we send to get the columns we want from the projection and all the rows from that pets table since we have null
        //Cursor cursor = db.query(PetEntry.TABLE_NAME, projection, null, null, null, null, null);

        //Now using a content resolver and content provider to send query to database instead of directly to the database
        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI, projection, null, null, null);

        // Get reference to the list view in the activity catoalog layout
        ListView listView = (ListView) findViewById(R.id.list_View);

        // Make instance of cursor adapter class
        PetCursorAdapter cursorAdapter = new PetCursorAdapter(this, cursor);

        // Attach cursor adapter to the list view
        listView.setAdapter(cursorAdapter);

        // Display the number of rows in the Cursor (which reflects the number of rows in the
        // pets table in the database).
        //TextView displayView = (TextView) findViewById(R.id.text_view_pet);

//        try {

/*

            displayView.setText("The pets table contains " + cursor.getCount() + " pets" + "\n");

            displayView.append("\n" + PetEntry._ID + " - "
                    + PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT + "\n");

            //columns index value
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);//gets the value of the columns number which is 0
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME); //gets the calue of the column number which is 1
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightCoumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            //row index values which loop from 0 to the total amount of rows, It starts at -1 since that represents the names of the columns
            while (cursor.moveToNext()) {

                //Getting the string or int value (for _id) based on the current row we are in
                //ex: row: 0 we well get all the int and string values based on the colums set in above and below
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightCoumnIndex);

                //displaying all the values to the textview
                displayView.append("\n" + currentId + " - " + currentName + " - " + currentBreed + " - " + currentGender + " - " + currentWeight);

                }
*/

        /*} finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }*/
    }

    private void insertPet() {

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        /**
         * Created a global PetDbHelper variable
         */
        //PetDbHelper mDbHelper = new PetDbHelper(this);

        //Gets the data repsository in write mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert the new row, returning the primary key value of the new row
        //long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

        //getting row id of the newll added dummy pet
        long idValid = ContentUris.parseId(newUri);

        //displaying toast to verify pet was added
        //we could have also said if newUri == null then display dummy pet failed
        if (idValid == -1) {
            Toast.makeText(this, R.string.dummy_pet_not_added, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.dummy_pet_added, Toast.LENGTH_SHORT).show();
        }

    }

    //Loader:


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //Define the prjection that specfifies the columns form the table we care about
        // We include _ID because the CursorAdapter assumes that the Cursor contains a column called _id.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };

        // This loader will execute the content providers query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                PetEntry.CONTENT_URI,   // Provide content uri for query
                projection,             // Columns to include in the resulting query
                null,                   // No selection clause (no specific rows)
                null,                   // No selection args (The specific row we are looking for)
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)

        // Update {@link PetCursorAdapter} with the new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.

        //Callback called when the data needs to be deleted (udacity)
        mCursorAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

