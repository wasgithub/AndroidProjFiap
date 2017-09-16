package br.com.washington.androidprojfiap.adapter;

import br.com.washington.androidprojfiap.domain.Login;

/**
 * Created by washington on 14/09/2017.
 */

public class LoginAdapter {

    private Login _login;

    public LoginAdapter(Login login) {
        this._login = login;
    }

    public void update(Login login) {
        this._login = login;
    }
}
