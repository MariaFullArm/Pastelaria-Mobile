package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class ItemVendaAdapter extends RecyclerView.Adapter<ItemVendaAdapter.Holder> {

    private final Context context;
    private final List<ItemVenda> lista;

    public ItemVendaAdapter(Context context, List<ItemVenda> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.list_item_venda, viewGroup, false);
        return new Holder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        ItemVenda itemVenda = lista.get(i);
        holder.vincula(itemVenda);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final TextView quantidade;
        private final TextView preco;
        private ItemVenda itemVenda;

        public Holder(@NonNull View itemView) {
            super(itemView);

            quantidade = itemView.findViewById(R.id.list_item_venda_quantidade);
            nome = itemView.findViewById(R.id.list_item_venda_nome_produto);
            preco = itemView.findViewById(R.id.list_item_venda_preco);
        }

        public void vincula(ItemVenda itemVenda) {
            this.itemVenda = itemVenda;
            preencheCampo(itemVenda);
        }

        private void preencheCampo(ItemVenda itemVenda) {
            Integer quantidadeEmTexo = itemVenda.getQuantidade();

            quantidade.setText(quantidadeEmTexo.toString());
            nome.setText(itemVenda.getProduto().getNome());
            preco.setText(new MoedaUtil().formataParaBrasileiro(itemVenda.getProduto().getValor()));
        }
    }
}

