package br.com.washington.androidprojfiap.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.domain.Dog;
import livroandroid.lib.fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapaFragment extends BaseFragment implements OnMapReadyCallback {
    // Objeto que controla o Google Maps
    private GoogleMap map;
    private Dog dog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        // Recupera o fragment que está no layout
        // Utiliza o getChildFragmentManager() pois é um fragment dentro do outro
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);
        // Inicia o Google Maps dentro do fragment
        mapFragment.getMapAsync(this);
        this.dog = getArguments().getParcelable("dog");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // O método onMapReady(map) é chamado quando a inicialização do mapa estiver Ok.
        this.map = map;
        if (dog != null) {
            // Ativa o botão para mostrar minha localização
            map.setMyLocationEnabled(true);
            // Cria o objeto LatLng com a coordenada da fábrica
            LatLng location = new LatLng(Double.parseDouble(dog.latitude),
                    Double.parseDouble(dog.longitude));
            // Posiciona o mapa na coordenada da fábrica (zoom = 13)
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
            map.moveCamera(update);
            // Marcador no local da fábrica
            map.addMarker(new MarkerOptions()
                    .title(dog.nome)
                    .snippet(dog.desc)
                    .position(location));
            // Tipo do mapa:
            // (normal, satélite, terreno ou híbrido)
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_mapa, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(map != null && dog != null) {
            if (item.getItemId() == R.id.action_location_dog) {
                // Posiciona mapa na localização da fábrica
                LatLng location = new LatLng(Double.parseDouble(dog.latitude),
                        Double.parseDouble(dog.longitude));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            } else if (item.getItemId() == R.id.action_location_directions) {
                // Posiciona mapa no usuário
                toast("Mostrar rota/direções até a fábrica.");
            } else if (item.getItemId() == R.id.action_zoom_in) {
                toast("zoom +");
                map.animateCamera(CameraUpdateFactory.zoomIn());
            } else if (item.getItemId() == R.id.action_zoom_out) {
                toast("zoom -");
                map.animateCamera(CameraUpdateFactory.zoomOut());
            } else if (item.getItemId() == R.id.action_mapa_normal) {
                // Modo Normal
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else if (item.getItemId() == R.id.action_mapa_satelite) {
                // Modo Satélite
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (item.getItemId() == R.id.action_mapa_terreno) {
                // Modo Terreno
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            } else if (item.getItemId() == R.id.action_mapa_hibrido) {
                // Modo Híbrido
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
