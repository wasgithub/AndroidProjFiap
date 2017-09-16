package br.com.washington.androidprojfiap.domain;

/**
 * Created by washington on 14/09/2017.
 */

public class Login {
    private String usuario;
    private String senha;
    private String fblogin;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFblogin() {
        return fblogin;
    }

    public void setFblogin(String fblogin) {
        this.fblogin = fblogin;
    }
}
