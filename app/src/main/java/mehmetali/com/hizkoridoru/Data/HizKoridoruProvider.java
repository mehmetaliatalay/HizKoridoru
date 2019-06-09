package mehmetali.com.hizkoridoru.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class HizKoridoruProvider extends ContentProvider {

    private static final UriMatcher matcher;
    public static final int URICDODE_ARAC = 1;
    public static final int URICODE_OTOYOL = 2;
    public static final int URICODE_GISE = 3;
    public static final int URICODE_MESAFE_UCRET = 4;

    private SQLiteDatabase database;
    private SQLiteAssetHelper helper;


    static {

        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(HizKoridoruContract.CONTENT_AUTHOTRITY, HizKoridoruContract.PATH_ARAC, URICDODE_ARAC);
        matcher.addURI(HizKoridoruContract.CONTENT_AUTHOTRITY, HizKoridoruContract.PATH_OTOYOL, URICODE_OTOYOL);
        matcher.addURI(HizKoridoruContract.CONTENT_AUTHOTRITY, HizKoridoruContract.PATH_GISE, URICODE_GISE);
        matcher.addURI(HizKoridoruContract.CONTENT_AUTHOTRITY, HizKoridoruContract.PATH_GISE_MESAFE_UCRET, URICODE_MESAFE_UCRET);

    }


    @Override
    public boolean onCreate() {

        helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (matcher.match(uri)) {

            case 1:
                cursor = database.query(HizKoridoruContract.AracEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            case 2:
                cursor = database.query(HizKoridoruContract.OtolyolEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            case 3:
                cursor = database.query(HizKoridoruContract.GiseEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            case 4:
                cursor = database.query(HizKoridoruContract.GiseMesafeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
