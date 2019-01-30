package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;

public class HistoricoVendaAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Venda> vendas;

    public HistoricoVendaAdapter(Context context, List<Venda> vendas) {
        this.context = context;
        this.vendas = vendas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_historico, viewGroup, false);
        return new HistoricoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return vendas.size();
    }

    public class HistoricoViewHolder extends RecyclerView.ViewHolder{

        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
