package br.com.washington.androidprojfiap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by washington on 10/09/2017.
 */

public class PrefsUtils {
    // Verifica se o usuário marcou o checkbox de Push ON nas configurações
    public static boolean isCheckPushOn(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("PREF_CHECK_PUSH", false);
    }
}
