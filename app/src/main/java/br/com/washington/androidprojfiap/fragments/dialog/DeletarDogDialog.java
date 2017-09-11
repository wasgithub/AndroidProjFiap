package br.com.washington.androidprojfiap.fragments.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

public class DeletarDogDialog extends Fragment  {
    private Callback callback;

    public static void show(FragmentManager fm, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("deletar_dog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DeletarDogDialog frag = new DeletarDogDialog();
        frag.callback = callback;
        //frag.show(ft, "deletar_dog");
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        DialogInterface.OnClickListener dialogClickListener = new
//                DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                if (callback != null) {
//                                    // Confirmou que vai deletar o dog.
//                                    callback.onClickYes();
//                                }
//                                break;
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                break;
//                        }
//                    }
//                };
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Deletar esse dog?");
//        builder.setPositiveButton("Sim", dialogClickListener);
//        builder.setNegativeButton("Não", dialogClickListener);
//        return builder.create();
//    }

    public interface Callback {
        void onClickYes();
    }
}
