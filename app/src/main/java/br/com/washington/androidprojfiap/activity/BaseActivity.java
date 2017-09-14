package br.com.washington.androidprojfiap.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

//import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.activity.prefs.ConfiguracoesActivivity;
import br.com.washington.androidprojfiap.activity.prefs.ConfiguracoesV11Activivity;
import br.com.washington.androidprojfiap.fragments.DogsFragment;
import br.com.washington.androidprojfiap.fragments.DogsTabFragment;
import br.com.washington.androidprojfiap.fragments.SiteFiapFragment;
import livroandroid.lib.utils.AndroidUtils;
import livroandroid.lib.utils.NavDrawerUtil;

/**
 * Created by washington on 09/09/2017.
 */

public class BaseActivity extends livroandroid.lib.activity.BaseActivity {
    protected DrawerLayout drawerLayout;

    // Configura a Toolbar
    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    // Configura o Nav Drawer
    protected void setupNavDrawer() {
        // Drawer Layout
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Ícone do menu do nav drawer
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null && drawerLayout != null) {
                // Atualiza a imagem e textos do header
                NavDrawerUtil.setHeaderValues(navigationView, R.id.containerNavDrawerListViewHeader, R.drawable.nav_drawer_header, R.drawable.ic_logo_user, R.string.nav_drawer_username, R.string.nav_drawer_email);
                // Trata o evento de clique no menu.
                navigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                // Seleciona a linha
                                menuItem.setChecked(true);
                                // Fecha o menu
                                drawerLayout.closeDrawers();
                                // Trata o evento do menu
                                onNavDrawerItemSelected(menuItem);
                                return true;
                            }
                        });
            }
        }
    }

    // Trata o evento do menu lateral
    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_dogs_all:
                // Nada aqui pois somente a MainActivity possui menu lateral
                break;
            case R.id.nav_item_dogs_small:
                Intent intent = new Intent(getContext(), DogsActivity.class);
                intent.putExtra("tipo", R.string.small);
                startActivity(intent);
                break;
            case R.id.nav_item_dogs_medium:
                intent = new Intent(getContext(), DogsActivity.class);
                intent.putExtra("tipo", R.string.medium);
                startActivity(intent);
                break;
            case R.id.nav_item_dogs_large:
                intent = new Intent(getContext(), DogsActivity.class);
                intent.putExtra("tipo", R.string.large);
                startActivity(intent);
                break;
            case R.id.nav_item_ligar_fiap:
                //faz ligacao para FIAP
                Uri uri = Uri.parse("tel:01133858010");
                Intent intentTel = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intentTel);
                break;
            case R.id.nav_item_exit:
                this.finish();
                break;
        }
    }

    // Adiciona o fragment no centro da tela
    protected void replaceFragment(Fragment frag) {
        toast("OK!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Trata o clique no botão que abre o menu.
                if (drawerLayout != null) {
                    openDrawer();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Abre o menu lateral
    protected void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Fecha o menu lateral
    protected void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}