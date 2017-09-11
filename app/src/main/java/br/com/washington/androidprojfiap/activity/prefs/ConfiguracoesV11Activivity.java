package br.com.washington.androidprojfiap.activity.prefs;

import android.app.FragmentTransaction;
import android.os.Bundle;

import br.com.washington.androidprojfiap.R;

/**
 * Created by washington on 10/09/2017.
 */

public class ConfiguracoesV11Activivity extends android.app.Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Adiciona o fragment de configurações
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, new PrefsFragment());
        ft.commit();
    }

    public static class PrefsFragment extends android.preference.PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Carrega as configurações
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
