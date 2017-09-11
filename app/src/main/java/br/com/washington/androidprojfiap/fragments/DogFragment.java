package br.com.washington.androidprojfiap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.washington.androidprojfiap.DogsApplication;
import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.activity.DogActivity;
import br.com.washington.androidprojfiap.activity.MapaActivity;
import br.com.washington.androidprojfiap.activity.VideoActivity;
import br.com.washington.androidprojfiap.domain.Dog;
import br.com.washington.androidprojfiap.domain.DogDB;
import br.com.washington.androidprojfiap.fragments.dialog.DeletarDogDialog;
import br.com.washington.androidprojfiap.fragments.dialog.EditarDogDialog;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.IntentUtils;

/**
 * Created by washington on 09/09/2017.
 */

public class DogFragment extends BaseFragment{
    private Dog dog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog, container, false);
        dog = getArguments().getParcelable("dog");
        setHasOptionsMenu(true);
        view.findViewById(R.id.imgPlayVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo(dog.urlVideo, v);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Atualiza a view do fragment com os dados do dog
        setTextString(R.id.tDesc, dog.desc);
        final ImageView imgView = (ImageView) getView().findViewById(R.id.img);
        Picasso.with(getContext()).load(dog.urlFoto).fit().into(imgView);
        // Seta a Lat/Lng
        setTextString(R.id.tLatLng, String.format("%s/%s", dog.latitude, dog.longitude));
        // Adiciona o fragment do Mapa
        MapaFragment mapaFragment = new MapaFragment();
        mapaFragment.setArguments(getArguments());
        getChildFragmentManager().beginTransaction().replace(R.id.mapFragment, mapaFragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_dog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            //toast("Editar: " + dog.nome);
            EditarDogDialog.show(getFragmentManager(), dog, new EditarDogDialog.Callback() {
                @Override
                public void onDogUpdated(Dog dog) {
                    toast("Dog [" + dog.nome + "] atualizado");
                    // Salva o dog
                    DogDB db = new DogDB(getContext());
                    db.save(dog);
                    // Atualiza o título com o novo nome
                    DogActivity a = (DogActivity) getActivity();
                    a.setTitle(dog.nome);
                    // Envia o evento para o bus
                    DogsApplication.getInstance().getBus().post("refresh");
                }
            });

            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            DeletarDogDialog.show(getFragmentManager(), new DeletarDogDialog.Callback() {
                @Override
                public void onClickYes() {
                    toast("Dog [" + dog.nome + "] deletado.");
                    // Deleta o dog
                    DogDB db = new DogDB(getActivity());
                    db.delete(dog);
                    // Fecha a activity
                    getActivity().finish();
                    // Envia o evento para o bus
                    DogsApplication.getInstance().getBus().post("refresh");
                }
            });


            return true;
        } else if (item.getItemId() == R.id.action_share) {
            toast("Compartilhar");
        } else if (item.getItemId() == R.id.action_maps) {
            // Abre outra activity para mostrar o mapa
            Intent intent = new Intent(getContext(), MapaActivity.class);
            intent.putExtra("dog", dog);
            startActivity(intent);

        } else if (item.getItemId() == R.id.action_video) {
            // URL do vídeo
            final String url = dog.urlVideo;
            // Lê a view que é a âncora do popup (é a view do botão da action bar)
            View menuItemView = getActivity().findViewById(item.getItemId());
            // Mostra o alerta com as opções do vídeo
            showVideo(url, menuItemView);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showVideo(final String url, View ancoraView) {
        if(url != null && ancoraView != null) {
            // Cria o PopupMenu posicionado na âncora
            PopupMenu popupMenu = new PopupMenu(getActionBar().getThemedContext(), ancoraView);
            popupMenu.inflate(R.menu.menu_popup_video);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_video_browser) {
                        // Abre o vídeo no browser
                        IntentUtils.openBrowser(getContext(), url);
                    } else if (item.getItemId() == R.id.action_video_player) {
                        // Abre o vídeo no Player de Vídeo Nativo
                        IntentUtils.showVideo(getContext(), url);
                    } else if (item.getItemId() == R.id.action_video_videoview) {
                        // Abre outra activity com VideoView
                        Intent intent = new Intent(getContext(), VideoActivity.class);
                        intent.putExtra("dog", dog);
                        startActivity(intent);

                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }
}

