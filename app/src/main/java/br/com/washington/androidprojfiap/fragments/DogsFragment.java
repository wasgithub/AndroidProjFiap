package br.com.washington.androidprojfiap.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.com.washington.androidprojfiap.DogsApplication;
import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.activity.DogActivity;
import br.com.washington.androidprojfiap.adapter.DogAdapter;
import br.com.washington.androidprojfiap.domain.Dog;
import br.com.washington.androidprojfiap.domain.DogDB;
import br.com.washington.androidprojfiap.domain.DogService;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.AndroidUtils;

import static br.com.washington.androidprojfiap.R.string.share;

public class DogsFragment extends BaseFragment {
    protected RecyclerView recyclerView;
    private int tipo;
    private List<Dog> dogs;
    private SwipeRefreshLayout swipeLayout;
    private ActionMode actionMode;

    // Método para instanciar esse fragment pelo tipo.
    public static DogsFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        DogsFragment f = new DogsFragment();
        f.setArguments(args);
        return f;
    }

    @Subscribe
     public void onBusAtualizarListaDogs(String refresh) {
        // Recebeu o evento, atualiza a lista.
         taskDogs(false);
      }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Lê o tipo dos argumentos.
            this.tipo = getArguments().getInt("tipo");
        }

        // Registra a classe para receber eventos.
        DogsApplication.getInstance().getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancela o recebimento de eventos.
        DogsApplication.getInstance().getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dogs, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
// Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    // Atualiza ao fazer o gesto Pull to Refresh
                    taskDogs(true);
                } else {
                    swipeLayout.setRefreshing(false);
                    snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskDogs(false);
    }

    private void taskDogs(boolean pullToRefresh) {
        // Busca os dogs: Dispara a Task
        startTask("dogs", new GetDogsTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }

    private DogAdapter.DogOnClickListener onClickDog() {
        return new DogAdapter.DogOnClickListener() {
            @Override
            public void onClickDog(View view, int idx) {
                Dog c = dogs.get(idx);
                if (actionMode == null) {
                    Intent intent = new Intent(getContext(), DogActivity.class);
                    intent.putExtra("dog", c);
                    startActivity(intent);
                } else { // Se a CAB está ativada
                    // Seleciona o dog
                    c.selected = !c.selected;
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onLongClickDog(View view, int idx) {
                if (actionMode != null) {
                    return;
                }
                // Liga a action bar de contexto (CAB)
                actionMode = getAppCompatActivity().
                        startSupportActionMode(getActionModeCallback());
                Dog c = dogs.get(idx);
                c.selected = true; // Seleciona o dog
                // Solicita ao Android para desenhar a lista novamente
                recyclerView.getAdapter().notifyDataSetChanged();
                // Atualiza o título para mostrar a quantidade de dogs selecionados
                updateActionModeTitle();
            }

        };
    }

    // Atualiza o título da action bar (CAB)
    private void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle(R.string.select_dog);
            actionMode.setSubtitle(null);
            List<Dog> selectedDogs = getSelectedDogs();
            if (selectedDogs.size() == 1) {
                actionMode.setSubtitle("1 " + R.string.select_dog);
            } else if (selectedDogs.size() > 1) {
                actionMode.setSubtitle(selectedDogs.size() + R.string.select_dog);
            }
        }
    }

    // Retorna a lista de dogs selecionados
    private List<Dog> getSelectedDogs() {
        List<Dog> list = new ArrayList<Dog>();
        for (Dog c : dogs) {
            if (c.selected) {
                list.add(c);
            }
        }
        return list;
    }

    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Infla o menu específico da action bar de contexto (CAB)
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_dogs_cab, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Dog> selectedDogs = getSelectedDogs();
                if (item.getItemId() == R.id.action_remove) {
                    DogDB db = new DogDB(getContext());
                    try {
                        for (Dog c : selectedDogs) {
                            db.delete(c); // Deleta o dog do banco
                            dogs.remove(c); // Remove da lista
                        }
                    } finally {
                        db.close();
                    }
                    snack(recyclerView, "Dogs excluídos com sucesso.");

                } else if (item.getItemId() == R.id.action_share) {
                    // Dispara a tarefa para fazer download das fotos
                    startTask(String.valueOf(share), new CompartilharTask(selectedDogs));
                }
                // Encerra o action mode
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Limpa o estado
                actionMode = null;
                // Configura todos os dogs para não selecionados
                for (Dog c : dogs) {
                    c.selected = false;
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        };
    }

    // Task para buscar os dogs
    private class GetDogsTask implements TaskListener<List<Dog>> {
        private boolean refresh;

        public GetDogsTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        public List<Dog> execute() throws Exception {
            // Busca os dogs em background (Thread)
            return DogService.getDogs(getContext(), tipo, refresh);
        }

        @Override
        public void updateView(List<Dog> dogs) {
            if (dogs != null) {
                // Salva a lista de dogs no atributo da classe
                DogsFragment.this.dogs = dogs;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new DogAdapter(getContext(), dogs, onClickDog()));
            }
        }

        @Override
        public void onError(Exception e) {
            // Qualquer exceção lançada no método execute vai cair aqui.
            alert("Ocorreu algum erro ao buscar os dados.");
        }

        @Override
        public void onCancelled(String s) {
        }
    }

    // Task para fazer o download
    // Faça import da classe android.net.Uri;
    private class CompartilharTask implements TaskListener {
        private final List<Dog> selectedDogs;
        // Lista de arquivos para compartilhar
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        private String urlImg;

        public CompartilharTask(List<Dog> selectedDogs) {
            this.selectedDogs = selectedDogs;
        }

        @Override
        public Object execute() throws Exception {
            if (selectedDogs != null) {
                for (Dog c : selectedDogs) {
                    // Faz o download da foto do dog para arquivo
                    String url = c.urlFoto;
                    urlImg = url;
                }
            }
            return null;
        }

        @Override
        public void updateView(Object o) {
            // Cria a intent com a foto dos dogs
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, "Link .");
            share.putExtra(Intent.EXTRA_TEXT, urlImg);

            startActivity(Intent.createChooser(share, "link share!"));
        }

        @Override
        public void onError(Exception e) {
            alert("share error");
        }

        @Override
        public void onCancelled(String s) {
        }
    }

}
