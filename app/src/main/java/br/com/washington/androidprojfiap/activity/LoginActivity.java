package br.com.washington.androidprojfiap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.washington.androidprojfiap.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //recuperar os componentes da tela
        etLogin=(EditText)findViewById(R.id.etUser);
        etSenha=(EditText)findViewById(R.id.etPass);

    }


    //clique no botao
    public void signIn(View view){
        //recuperar os valores logados
        String login=etLogin.getText().toString();
        String senha=etSenha.getText().toString();

        //valida o usuario e senha
        if(login.equals("android") && senha.equals("mobile")){
            //mudar de tela   tela de destino
            Intent intent = new Intent(this, MainActivity.class);
            //passar valor para outra tela
            intent.putExtra("usuario",login);
            //iniciar a outra tela
            startActivity(intent);
            finish();
        }else{
            //login ou senha invalido
            Toast.makeText(this, "Usuario ou senha inválido", Toast.LENGTH_SHORT).show();
        }
    }
}
