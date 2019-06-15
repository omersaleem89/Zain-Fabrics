package com.zainfabrics.pk;

/**
 * Created by M Umer Saleem on 10-Dec-17.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;


    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String COUNTRY_NAME="Pakistan";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setCountryName(String countryName) {
        editor.putString(COUNTRY_NAME, countryName);
        editor.commit();
    }
    public String getCountryName() {
        return pref.getString(COUNTRY_NAME, "Pakistan");
    }
}
