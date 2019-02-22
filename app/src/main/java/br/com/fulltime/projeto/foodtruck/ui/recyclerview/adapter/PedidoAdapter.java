package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private final Context context;
    private List<ItemVenda> lista;

    public PedidoAdapter(Context context) {
        this.context = context;
        lista = new ArrayList<>();
    }

    public void carregaLista(List<ItemVenda> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.list_item_pedido, viewGroup, false);
        return new PedidoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder pedidoViewHolder, int i) {
        ItemVenda itemVenda = lista.get(i);
        pedidoViewHolder.vincula(itemVenda);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nomeProduto;
        private final TextView quantidade;
        private final TextView descricao;
        private ItemVenda itemVenda;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeProduto = itemView.findViewById(R.id.list_item_pedido_nome_produto);
            descricao = itemView.findViewById(R.id.list_item_pedido_descricao);
            quantidade = itemView.findViewById(R.id.list_item_pedido_quantidade);
        }

        public void vincula(ItemVenda itemVenda) {
            this.itemVenda = itemVenda;
            preencheCampo(itemVenda);
        }

        private void preencheCampo(ItemVenda itemVenda) {
            Integer quantidadeEmTexo = itemVenda.getQuantidade();

            quantidade.setText(quantidadeEmTexo.toString());
            nomeProduto.setText(itemVenda.getProduto().getNome());
            descricao.setText(itemVenda.getProduto().getDescricao());
        }
    }
}
