package mehmetali.com.hizkoridoru.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class HizKoridoruContract {

    static final String CONTENT_AUTHOTRITY = "mehmetali.com.hizkoridoru.data.hizkoridoruprovider";
    static final String PATH_ARAC = "arac";
    static final String PATH_OTOYOL = "otoyol";
    static final String PATH_GISE = "gise";
    static final String PATH_GISE_MESAFE_UCRET = "mesafe_ucret_sinif";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHOTRITY);


    public static final class AracEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARAC);

        public static final String TABLE_NAME = "Arac";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ARAC_TIPI = "Arac_Tipi";
        public static final String COLUMN_HIZ_SINIRI = "Hiz_Siniri";
        public static final String COLUMN_ARAC_SINIFI = "Arac_Sinifi";

    }

    public static final class OtolyolEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_OTOYOL);

        public static final String TABLE_NAME = "Otoyol";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_OTOYOL_ADI = "Otoyol_Adi";

    }

    public static final class GiseEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GISE);

        public static final String TABLE_NAME = "Gise";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GISE_ADI = "Gise_Adi";
        public static final String COLUMN_OTOYOL_ID = "Otoyol_Id";

    }

    public static final class GiseMesafeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GISE_MESAFE_UCRET);

        public static final String TABLE_NAME = "MesafeUcretSinif";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GISE_ID_1 = "GiseID1";
        public static final String COLUMN_GISE_ID_2 = "GiseID2";
        public static final String COLUMN_MESAFE = "Mesafe";
        public static final String COLUMN_SINIF_UCRET_1 = "Sinif_Ucret1";
        public static final String COLUMN_SINIF_UCRET_2 = "Sinif_Ucret2";
        public static final String COLUMN_SINIF_UCRET_3 = "Sinif_Ucret3";
        public static final String COLUMN_SINIF_UCRET_4 = "Sinif_Ucret4";
        public static final String COLUMN_SINIF_UCRET_5 = "Sinif_Ucret5";
        public static final String COLUMN_SINIF_UCRET_6 = "Sinif_Ucret6";


    }
}
