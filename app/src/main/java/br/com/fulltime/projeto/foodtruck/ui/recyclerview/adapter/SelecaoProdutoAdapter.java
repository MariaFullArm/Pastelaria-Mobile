package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemVendaSelectedListener;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoAdapter extends RecyclerView.Adapter<SelecaoProdutoAdapter.SelecaoProdutoViewHolder> {

    private final Context context;
    private List<ItemVenda> itemVendas;
    private List<ItemVenda> listaAuxiliar;
    private OnItemVendaSelectedListener onItemVendaSelectedListener;

    public SelecaoProdutoAdapter(Context context) {
        this.context = context;
        this.itemVendas = new ArrayList<>();
    }

    public void setOnItemVendaSelectedListener(OnItemVendaSelectedListener onItemVendaSelectedListener) {
        this.onItemVendaSelectedListener = onItemVendaSelectedListener;
    }

    @NonNull
    @Override
    public SelecaoProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_selecao_produto, viewGroup, false);
        return new SelecaoProdutoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecaoProdutoViewHolder selecaoProdutoViewHolder, int i) {
        ItemVenda itemVenda = itemVendas.get(i);
        selecaoProdutoViewHolder.vincula(itemVenda);
    }

    @Override
    public int getItemCount() {
        return itemVendas.size();
    }

    public void substituiLista(List<Produto> produtos) {
        if (produtos != null) {
            itemVendas.clear();
            notifyDataSetChanged();
            for (int i = 0; i < produtos.size(); i++) {
                itemVendas.add(new ItemVenda(produtos.get(i)));
                notifyItemInserted(i);
            }
            listaAuxiliar = new ArrayList<>(itemVendas);
        }
    }

    public void filtraListaDeExibicao(String tipo) {
        if (tipo != null) {
            itemVendas.clear();
            if (listaAuxiliar != null) {
                for (ItemVenda item : listaAuxiliar) {
                    String tipoItem = item.getProduto().getTipo();
                    if (tipoItem.equalsIgnoreCase(tipo)) {
                        this.itemVendas.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        } else if (listaAuxiliar != null)
            itemVendas = new ArrayList<>(listaAuxiliar);
        notifyDataSetChanged();
    }

    public void trocaItens(List<ItemVenda> lista) {
        for (ItemVenda aux : lista) {
            for (int i = 0; i < listaAuxiliar.size(); i++) {
                if(aux.getProduto().getId() == listaAuxiliar.get(i).getProduto().getId()){
                    listaAuxiliar.set(i, aux);
                    itemVendas.set(i, aux);
                }
            }
        }
    }

    public class SelecaoProdutoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final Spinner spinner;
        private final TextView preco;
        private ItemVenda itemVenda;

        public SelecaoProdutoViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.item_venda_nome_produto);
            spinner = itemView.findViewById(R.id.item_venda_spinner);
            preco = itemView.findViewById(R.id.item_venda_preco);

        }

        public void vincula(ItemVenda itemVenda) {
            this.itemVenda = itemVenda;
            preencheCampo();
        }

        private void preencheCampo() {
            nome.setText(itemVenda.getProduto().getNome());
            if (itemVenda.getProduto().getValor() != null) {
                preco.setText(MoedaUtil.formataParaBrasileiro(itemVenda.getProduto().getValor()));
            }

            List<Integer> quantidadeProduto = new ArrayList<>();
            quantidadeProduto.add(0);
            quantidadeProduto.add(1);
            quantidadeProduto.add(2);
            quantidadeProduto.add(3);
            quantidadeProduto.add(4);
            quantidadeProduto.add(5);
            quantidadeProduto.add(6);
            quantidadeProduto.add(7);
            quantidadeProduto.add(8);
            quantidadeProduto.add(9);
            quantidadeProduto.add(10);

            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, quantidadeProduto);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(itemVenda.getQuantidade());
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int quantidade, long id) {
                    itemVenda.setQuantidade(quantidade);
                    onItemVendaSelectedListener.onItemVendaSelected(itemVenda, quantidade);

                    for (int i = 0; i < listaAuxiliar.size(); i++) {
                        if (listaAuxiliar.get(i).getProduto().getId() == itemVenda.getProduto().getId())
                            listaAuxiliar.set(i, itemVenda);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
