package br.com.washington.androidprojfiap.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.api.IRetrofitApi;
import br.com.washington.androidprojfiap.domain.Login;

public class SplashActivity extends BaseActivity {

    public static final int SPLASH_DISPLAY_LENGTH = 3500;
    private boolean loadedData = false;
    private IRetrofitApi retrofitApi;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        loadSplashLogo();

    }
    private void loadSplashLogo() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacao_splash);
        animation.reset();

        ImageView imageView = (ImageView) findViewById(R.id.splash);
        imageView.clearAnimation();
        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Após o tempo definido irá executar a próxima tela
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //TODO verify
                startActivity(intent);

                //Retira a Activity da pilha quando clicar no botao voltar
                SplashActivity.this.finish();

            }

        }, SPLASH_DISPLAY_LENGTH);

    }
}