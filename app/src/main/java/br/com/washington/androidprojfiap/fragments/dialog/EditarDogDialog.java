package br.com.washington.androidprojfiap.fragments.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.domain.Dog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditarDogDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditarDogDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarDogDialog extends DialogFragment {
    private Callback callback;
    private Dog dog;
    private TextView tNome;

    // Método utilitário para criar o dialog
    public static void show(FragmentManager fm, Dog dog, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_dog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarDogDialog frag = new EditarDogDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        // Passa o objeto dog por parâmetro
        args.putParcelable("dog", dog);
        frag.setArguments(args);
        frag.show(ft, "editar_dog");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        // Atualiza o tamanho do dialog
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_editar_dog, container, false);
        view.findViewById(R.id.btAtualizar).setOnClickListener(onClickAtualizar());
        tNome = (TextView) view.findViewById(R.id.tNome);
        this.dog = getArguments().getParcelable("dog");
        if (dog != null) {
            tNome.setText(dog.nome);
        }
        return view;
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novoNome = tNome.getText().toString();
                if (novoNome == null || novoNome.trim().length() == 0) {
                    tNome.setError("Informe o nome");
                    return;
                }
                Context context = view.getContext();
                // Atualiza o banco de dados
                dog.nome = novoNome;
                if (callback != null) {
                    callback.onDogUpdated(dog);
                }
                // Fecha o DialogFragment
                dismiss();
            }
        };
    }

    // Interface para retornar o resultado
    public interface Callback {
        void onDogUpdated(Dog dog);
    }
}
