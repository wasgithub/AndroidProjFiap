package br.com.washington.androidprojfiap.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.fragments.DogsFragment;

public class SiteFiapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_livro);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Título
        getSupportActionBar().setTitle(getString(getIntent().getIntExtra("tipo", 0)));
        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            DogsFragment frag = new DogsFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, frag).commit();
        }
    }
}
