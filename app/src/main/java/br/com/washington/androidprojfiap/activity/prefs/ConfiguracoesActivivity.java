package br.com.washington.androidprojfiap.activity.prefs;

import android.os.Bundle;

import br.com.washington.androidprojfiap.R;

/**
 * Created by washington on 10/09/2017.
 */

public class ConfiguracoesActivivity extends android.preference.PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega as configurações
        addPreferencesFromResource(R.xml.preferences);
    }
}