package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by Ryan PC on 2017-01-22.
 */

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {

        /**
         * Recommended constructor.
         *
         * @param c The cursor from which to get the data.
         * @param context The context
         * @param flags Flags used to determine the behavior of the adapter; may
         * be any combination of {@link #FLAG_AUTO_REQUERY} and
         * {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
         */
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        /**
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        layoutInflater.inflate(R.layout.list_item, parent, false);

         same as saying the expression below
        */

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        TextView breedTextView = (TextView) view.findViewById(R.id.breed_text_view);

        // Find columns of pet attributes that we are interested in
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);

        //Read the pet attributes from the cursor for the current pet
        String petName = cursor.getString(nameColumnIndex);
        String breedName = cursor.getString(breedColumnIndex);

        // Populate fields with extracted properties
        nameTextView.setText(petName);
        breedTextView.setText(breedName);

    }


}
