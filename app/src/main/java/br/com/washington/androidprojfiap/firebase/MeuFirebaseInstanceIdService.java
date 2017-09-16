package br.com.washington.androidprojfiap.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by washington on 14/09/2017.
 */

public class MeuFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public  void onTokenRefresh(){
        super.onTokenRefresh();
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(firebaseToken);
    }


    private void sendRegistrationToServer(String token){

    }
}
