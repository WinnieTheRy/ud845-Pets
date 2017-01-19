package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by Ryan PC on 2017-01-14.
 */

public class PetProvider extends ContentProvider {

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int PETS = 100;

    private static final int PETS_ID = 101;

    private static final String LOG_TAG = PetProvider.class.getSimpleName();


    static {

        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        // Adds a list of uris to the matcher array. If one of the cases matches one of these
        // uris then it will execute that block of code.

        // Given an int code of 100
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);

        // Given an int code of 101
        // /# reoresent that we are looking for a spcefic row in the table. Therefore if the uri being sent in has a int at teh end
        // it will chose this uri matcher with code 101
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PETS_ID);
    }

    //database helper object
    private PetDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {

        mDbHelper = new PetDbHelper(getContext());

        return false;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        final int match = sUriMatcher.match(uri);

        //switch case:
        switch (match) {

            case PETS:

                //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
                cursor = database.query(PetEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

                break;

            case PETS_ID:

                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.


                /**rows with selectionargs with certian _id */
                selection = PetEntry._ID + "=?"; //appended strings //_id=? the 5 will replace the question mark
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))}; //parseId is looking for a int
                // at the end of the uri for the _id ex: { "5" }
                //it then gets conterted back into a string so it can be
                //added to the selection string ex: _id=5

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);


        }
        return cursor;

    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                return insertPet(uri, contentValues); //no need to add break since we are returning the value right away
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {

        // get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Check that name is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // No need to check breed null is okay if we do no know

        // Check that gender is valid
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        // If the weight is null, that’s fine, and we can proceed with insertion (the database will insert default weight 0 automatically).
        // If the weight is not null AND it’s a negative weight, then we need to throw an exception with the message “Pet requires valid weight.”
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        //the insert() method returns the rowId of the newly added animal
        // Insert the new pet with the given values
        long newRowId = database.insert(PetEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for uri " + uri);
        }

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, newRowId);

    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     * Returns the number of rows to be updated
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PETS:
                return updatePet(uri, values, selection, selectionArgs);

            case PETS_ID:

                /**rows with selectionargs with certian _id */
                selection = PetEntry._ID + "=?"; //_id=? the 5 will replace the question mark
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))}; //parseId is looking for a int
                // at the end of the uri for the _id ex: { "5" }
                //it then gets conterted back into a string so it can be
                //added to the selection string ex: _id=5

                return updatePet(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update pet not supported for " + uri);
        }


    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /**
         * Convenience method for updating rows in the database.
         *
         * @param table the table to update in
         * @param values a map from column names to new column values. null is a
         *            valid value that will be translated to NULL.
         * @param whereClause the optional WHERE clause to apply when updating.
         *            Passing null will update all rows.
         * @param whereArgs You may include ?s in the where clause, which
         *            will be replaced by the values from whereArgs. The values
         *            will be bound as Strings.
         * @return the number of rows affected
         *
         * public int update(String table, ContentValues values, String whereClause, String[] whereArgs)
         */


        // Check to see if the content value has a value inside it that needs to be updated
        // If it doesnt, then we do not check to see if the string is valid
        boolean nameHasValue = values.containsKey(PetEntry.COLUMN_PET_NAME);
        if (nameHasValue) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }

        // No need to check breed, null is okay

        boolean genderHasValue = values.containsKey(PetEntry.COLUMN_PET_GENDER);
        if (genderHasValue) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException();
            }
        }

        boolean weightHasValue = values.containsKey(PetEntry.COLUMN_PET_WEIGHT);
        if (weightHasValue) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException();
            }
        }

        // If there is no value being updated then there is no need to waste resources and
        // don't try to update the database
        if (values.size() == 0) {

            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int numberOfRows = database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numberOfRows == 0) {
            Log.e(LOG_TAG, "No row was updated" + uri);
        }

        return numberOfRows;
    }


    /**
     * Returns the MIME type of data for the content URI.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:

                return PetEntry.CONTENT_LIST_TYPE;

            case PETS_ID:

            return PetEntry.CONTENT_ITEM_TYPE;

            default:

                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }

    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PETS:
                // Delete all rows that match the selection and selection args
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            case PETS_ID:

                // Delete a single row given by the ID in the URI
                selection = PetEntry._ID + "=?"; //_id=? the 5 will replace the question mark
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))}; //parseId is looking for a int
                // at the end of the uri for the _id ex: { "5" }
                //it then gets conterted back into a string so it can be
                //added to the selection string ex: _id=5

                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }
}
