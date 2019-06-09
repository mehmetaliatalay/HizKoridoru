package mehmetali.com.hizkoridoru.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "MyDatabase.db";
    public static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }


}
