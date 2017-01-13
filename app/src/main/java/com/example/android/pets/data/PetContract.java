package com.example.android.pets.data;

//just a class for providing constants so owe make it final so it cannot be extended

import android.provider.BaseColumns;

public final class PetContract {


    public static abstract class PetEntry implements BaseColumns {

        /* pets table constants */
        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID; //the value is: _id
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_WEIGHT = "weight";

        /*pets gender values for male female and unknown*/
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;


    }
}
