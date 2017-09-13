package br.com.washington.androidprojfiap.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.washington.androidprojfiap.R;
import br.com.washington.androidprojfiap.domain.Dog;

/**
 * Created by washington on 09/09/2017.
 */

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogsViewHolder>  {
    protected static final String TAG = "fiap";
    private final List<Dog> dogs;
    private final Context context;
    private DogOnClickListener dogOnClickListener;

    public DogAdapter(Context context, List<Dog> dogs, DogOnClickListener
            dogOnClickListener) {
        this.context = context;
        this.dogs = dogs;
        this.dogOnClickListener = dogOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.dogs != null ? this.dogs.size() : 0;
    }

    @Override
    public DogsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_dog, viewGroup, false);
        DogsViewHolder holder = new DogsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DogsViewHolder holder, final int position) {
        // Atualiza a view
        Dog c = dogs.get(position);
        holder.tNome.setText(c.nome);
        holder.progress.setVisibility(View.VISIBLE);
        // Faz o download da foto e mostra o ProgressBar
        Picasso.with(context).load(c.urlFoto).fit().into(holder.img,
                new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progress.setVisibility(View.GONE); // download ok
                    }

                    @Override
                    public void onError() {
                        holder.progress.setVisibility(View.GONE);
                    }
                });
        // Click
        if (dogOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // A variável position é final
                    dogOnClickListener.onClickDog(holder.itemView, position);
                }
            });
        }
        // Click longo
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dogOnClickListener.onLongClickDog(holder.itemView, position);
                return true;
            }
        });

        // Pinta o fundo de azul se a linha estiver selecionada
        int corFundo = context.getResources().getColor(c.selected ? R.color.primary : R.color.white);
        holder.cardView.setCardBackgroundColor(corFundo);
        // A cor do texto é branca ou azul, depende da cor do fundo.
        int corFonte = context.getResources().getColor(c.selected ? R.color.white : R.color.primary);
        holder.tNome.setTextColor(corFonte);


    }

    public interface DogOnClickListener {
        void onClickDog(View view, int idx);

        void onLongClickDog(View view, int idx);
    }

    // ViewHolder com as views
    public static class DogsViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        ImageView img;
        ProgressBar progress;
        CardView cardView;

        public DogsViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}

