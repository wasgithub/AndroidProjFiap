package br.com.washington.androidprojfiap.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.adapter.DogAdapter;
import br.com.washington.androidprojfiap.domain.Dog;
import br.com.washington.androidprojfiap.domain.DogDB;

public class DogsIntentActivity extends BaseActivity {
    private List<Dog> dogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogs_intent);
        // (*1*) Lê as informações da intent
        Intent intent = getIntent();
        Uri uri = intent.getData();
        Log.d("livroandroid", "Action: " + intent.getAction());
        Log.d("livroandroid", "Scheme: " + uri.getScheme());
        Log.d("livroandroid", "Host: " + uri.getHost());
        Log.d("livroandroid", "Path: " + uri.getPath());
        Log.d("livroandroid", "PathSegments: " + uri.getPathSegments());
        // RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        // Configura a Toolbar como a action bar
        setUpToolbar();
        DogDB db = new DogDB(this);
        try {
            if ("/dogs".equals(uri.getPath())) {
                // (*2*)Lista todos os dogs
                this.dogs = db.findAll();
                recyclerView.setAdapter(new DogAdapter(this, dogs, onClickdog()));
            } else {
                // (*3*) Busca o dog pelo nome: /dogs/Ferrari FF
                List<String> paths = uri.getPathSegments();
                String nome = paths.get(1);
                Dog dog = db.findByNome(nome);
                if (dog != null) {
                    // Se encontrou o dog, abre a activity para mostrá-lo
                    Intent dogIntent = new Intent(this, DogActivity.class);
                    dogIntent.putExtra("dog", dog);
                    startActivity(dogIntent);
                    finish();
                }
            }
        } finally {
            db.close();
        }
    }

    private DogAdapter.DogOnClickListener onClickdog() {
        return new DogAdapter.DogOnClickListener() {
            @Override
            public void onClickDog(View view, int idx) {
                Dog c = dogs.get(idx);
                // (*4*) Retorna o dog selecionado para quem chamou
                Intent intent = new Intent();
                intent.putExtra("nome", c.nome);
                intent.putExtra("url_foto", c.urlFoto);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLongClickDog(View view, int idx) {
                // nada aqui
            }
        };
    }
}
