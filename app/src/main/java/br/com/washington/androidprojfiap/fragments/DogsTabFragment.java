package br.com.washington.androidprojfiap.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.activity.DogActivity;
import br.com.washington.androidprojfiap.adapter.DogAdapter;
import br.com.washington.androidprojfiap.adapter.TabsAdapter;
import br.com.washington.androidprojfiap.domain.Dog;
import br.com.washington.androidprojfiap.domain.DogService;
import livroandroid.lib.fragment.BaseFragment;

/**
 * Created by washington on 09/09/2017.
 */

public class DogsTabFragment  extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dogs_tab, container, false);
        // ViewPager
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new TabsAdapter(getContext(), getChildFragmentManager()));
        // Tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        // Cria as tabs com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);
        int cor = ContextCompat.getColor(getContext(), R.color.white);
        // Cor branca no texto (o fundo azul foi definido no layout)
        tabLayout.setTabTextColors(cor, cor);
        return view;
    }
}
