package com.stimednp.dtsmywisata.mydb;

import android.provider.BaseColumns;

/**
 * Created by rivaldy on 7/29/2019.
 */

class DatabaseContract {
    static String TABLE_WISATA = "wisata";

    static final class WisataColumns implements BaseColumns {
        static String TITLE = "title";
        static String DESC = "desc";
        static String DATE = "date";
        static String URLIMAGE = "url_image";
        static String COORLAT = "coor_latitude";
        static String COORLNG = "coor_longitude";
    }
}
