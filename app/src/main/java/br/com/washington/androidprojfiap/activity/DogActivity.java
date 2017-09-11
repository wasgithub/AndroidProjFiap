package br.com.washington.androidprojfiap.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.domain.Dog;
import br.com.washington.androidprojfiap.fragments.DogFragment;

public class DogActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);
        // Configura a Toolbar como a action bar
        setUpToolbar();
        // Título da toolbar e botão up navigation
        Dog c = getIntent().getExtras().getParcelable("dog");
        getSupportActionBar().setTitle(c.nome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Imagem de header na action bar
        ImageView appBarImg = (ImageView) findViewById(R.id.appBarImg);
        Picasso.with(getContext()).load(c.urlFoto).into(appBarImg);
        // Adiciona o fragment no layout
        if (savedInstanceState == null) {
            // Cria o fragment com o mesmo Bundle (args) da intent
            DogFragment frag = new DogFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment no layout
            getSupportFragmentManager().beginTransaction().add(R.id.DogFragment, frag).commit();
        }
    }

    public void setTitle(String s) {
        // O título deve ser setado na CollapsingToolbarLayout
        CollapsingToolbarLayout c = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        c.setTitle(s);
    }
}
