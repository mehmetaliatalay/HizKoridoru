package mehmetali.com.hizkoridoru;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mehmetali.com.hizkoridoru.Data.HizKoridoruContract;
import mehmetali.com.hizkoridoru.Model.Arac;
import mehmetali.com.hizkoridoru.Model.Gise;
import mehmetali.com.hizkoridoru.Model.MesafeUcret;
import mehmetali.com.hizkoridoru.Model.Otoyol;
import mehmetali.com.hizkoridoru.Model.SeyahatBilgileri;


public class MainActivity extends AppCompatActivity {

    private int giseID1 = 0;
    private int giseID2 = 0;
    private int sinif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.toolbarMain);
        mToolbar.setTitle("Hız Koridoru");
        setSupportActionBar(mToolbar);
        defAracSpinner();
        defOtoyolSpinner();
        defHesaplaButton();

    }
    //Arac spinnerı tanımlar ve veri ile doldurur.
    private void defAracSpinner() {

        Spinner aracSpinner = findViewById(R.id.aracSpinner);
        ArrayAdapter<Arac> aracAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, getAllArac());
        aracAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        aracSpinner.setAdapter(aracAdapter);

        SeyahatBilgileri.aracTipi = getAllArac().get(0).getAracTipi();
        SeyahatBilgileri.hizLimiti = getAllArac().get(0).getHizSiniri();
        sinif = getAllArac().get(0).getSinif();

        aracSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SeyahatBilgileri.aracTipi = getAllArac().get(position).getAracTipi();
                SeyahatBilgileri.hizLimiti = getAllArac().get(position).getHizSiniri();
                sinif = getAllArac().get(position).getSinif();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Otoyol spinnerı tanımlar ve veri ile doldurur.
    private void defOtoyolSpinner() {

        Spinner otoyolSpinner = findViewById(R.id.otoyolSpinner);
        ArrayAdapter<Otoyol> otoyolAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, getAllOtoyol());
        otoyolAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        otoyolSpinner.setAdapter(otoyolAdapter);
        SeyahatBilgileri.otoyol = getAllOtoyol().get(0).getOtoyolAdi();
        otoyolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SeyahatBilgileri.otoyol = getAllOtoyol().get(position).getOtoyolAdi();
                List<Gise> giseList = getAllGise(getAllOtoyol().get(position).getId());
                defGirisSpinner(giseList);
                defCikisSpinner(giseList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Giris spinnerı tanımlar
    private void defGirisSpinner(final List<Gise> list) {

        Spinner girisSpinner = findViewById(R.id.girisSpinner);
        ArrayAdapter<Gise> giseAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, list);
        giseAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        girisSpinner.setAdapter(giseAdapter);
        girisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                giseID1 = list.get(position).getId();
                SeyahatBilgileri.girisGise = list.get(position).getGiseAdi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Cikis spinnerı tanımlar
    private void defCikisSpinner(final List<Gise> list) {

        Spinner cikisSpinner = findViewById(R.id.cikisSpinner);
        ArrayAdapter<Gise> giseAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, list);
        giseAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        cikisSpinner.setAdapter(giseAdapter);
        cikisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                giseID2 = list.get(position).getId();
                SeyahatBilgileri.cikisGise = list.get(position).getGiseAdi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Buttona tıklandığında gerçekleşen işlemler.
    private void defHesaplaButton() {

        Button hesaplaButton = findViewById(R.id.hesaplaButton);
        hesaplaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkGise()) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Seçtiğiniz gişeler arası rota bulunmamaktadır!!")
                            .create().show();
                } else {
                    Intent intent = new Intent(MainActivity.this, DriveActivity.class);
                    startActivity(intent);
                    getMesafeUcret();
                    SeyahatBilgileri.sure = (long) (SeyahatBilgileri.mesafe * 3600000) / (long) (SeyahatBilgileri.hizLimiti);
                }
            }
        });
    }

    //Veritabanından araçları getirir.
    private List<Arac> getAllArac() {

        List<Arac> aracList = new ArrayList<>();

        Cursor cursor = getContentResolver().query(HizKoridoruContract.AracEntry.CONTENT_URI, new String[]{"*"}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                aracList.add(new Arac(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("Arac_Tipi")),
                        cursor.getInt(cursor.getColumnIndex("Hiz_Siniri")),
                        cursor.getInt(cursor.getColumnIndex("Arac_Sinifi"))));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return aracList;


    }

    //Veritabanından otoyolları getirir.
    private List<Otoyol> getAllOtoyol() {

        List<Otoyol> otoyolList = new ArrayList<>();

        Cursor cursor = getContentResolver().query(HizKoridoruContract.OtolyolEntry.CONTENT_URI, new String[]{"*"}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                otoyolList.add(new Otoyol(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("Otoyol_Adi"))));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return otoyolList;


    }

    //Otoyol seçildikten sonra giseler gelir.
    private List<Gise> getAllGise(int id) {

        List<Gise> giseList = new ArrayList<>();
        String selection = HizKoridoruContract.GiseEntry.COLUMN_OTOYOL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = getContentResolver().query(HizKoridoruContract.GiseEntry.CONTENT_URI, new String[]{"*"}, selection, selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                giseList.add(new Gise(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("Gise_Adi")),
                        cursor.getInt(cursor.getColumnIndex("Otoyol_Id"))));
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return giseList;
    }

    //Sınfa göre ücreti bulmak için aracın hangi sınıfa ait olduğunu belirler.
    private String getSinifUcret(int sinif) {

        switch (sinif) {

            case 1:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_1;
            case 2:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_2;
            case 3:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_3;
            case 4:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_4;
            case 5:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_5;
            case 6:
                return HizKoridoruContract.GiseMesafeEntry.COLUMN_SINIF_UCRET_6;

        }
        return null;
    }

    //Gişeler kontrol edildikten sonra rotadaki mesafe ve ücret bilgileri alınır.
    private void getMesafeUcret() {

        MesafeUcret mMesafeUcret = null;

        String selection = "( " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_1 + " = ?" + " and " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_2 + " = ?" + " )" +
                " or " + " ( " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_1 + " = ?" + " and " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_2 + " = ?" + " ) ";
        String[] selectionArgs = {String.valueOf(giseID1), String.valueOf(giseID2), String.valueOf(giseID2), String.valueOf(giseID1)};
        String aracSinif = getSinifUcret(sinif);

        Cursor cursor = getContentResolver().query(HizKoridoruContract.GiseMesafeEntry.CONTENT_URI, new String[]{"_id", "Mesafe", aracSinif}, selection, selectionArgs, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                mMesafeUcret = new MesafeUcret(cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getFloat(cursor.getColumnIndex("Mesafe")),
                        cursor.getString(cursor.getColumnIndex(aracSinif)));
            }
        }
        SeyahatBilgileri.mesafe = mMesafeUcret.getMesafe();
        SeyahatBilgileri.fiyat = mMesafeUcret.getUcret();


        if (cursor != null) {
            cursor.close();
        }


    }

    //Seçilen gişeler arası rota olup olmadığını kontrol eder.
    private boolean checkGise() {

        if (giseID1 != 0 && giseID2 != 0) {

            String selection = "( " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_1 + " = ?" + " and " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_2 + " = ?" + " )" +
                    " or " + " ( " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_1 + " = ?" + " and " + HizKoridoruContract.GiseMesafeEntry.COLUMN_GISE_ID_2 + " = ?" + " ) ";
            String[] selectionArgs = {String.valueOf(giseID1), String.valueOf(giseID2), String.valueOf(giseID2), String.valueOf(giseID1)};

            Cursor cursor = getContentResolver().query(HizKoridoruContract.GiseMesafeEntry.CONTENT_URI, new String[]{"*"}, selection, selectionArgs, null);

            if (cursor != null && cursor.getCount() == 0) {
                return false;
            }
            return true;

        }
        return false;

    }
}
