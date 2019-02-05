package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemVendaSelectedListener;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoAdapter extends RecyclerView.Adapter<SelecaoProdutoAdapter.SelecaoProdutoViewHolder> {

    private final Context context;
    private final List<ItemVenda> itemVendas;
    private OnItemVendaSelectedListener onItemVendaSelectedListener;

    public SelecaoProdutoAdapter(Context context, List<ItemVenda> itemVendas) {
        this.context = context;
        this.itemVendas = itemVendas;
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

    public void ordenaTipo(String tipo) {
        Comparador comparador = new Comparador();
        Collections.sort(itemVendas, comparador);
        int contadorDePosicoes = 0;
        int posicaoParaTrocar = 0;
        for (ItemVenda item : itemVendas) {
            if (item.getProduto().getTipo() == tipo) {
                Collections.swap(itemVendas, contadorDePosicoes, posicaoParaTrocar);
                posicaoParaTrocar++;
            }
            contadorDePosicoes++;
        }
        notifyDataSetChanged();
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
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int quantidade, long id) {
                    onItemVendaSelectedListener.onItemVendaSelected(itemVenda, getAdapterPosition(), quantidade);
                    itemVenda.setQuantidade(quantidade);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public void vincula(ItemVenda itemVenda) {
            this.itemVenda = itemVenda;
            preencheCampo(this.itemVenda);
        }

        private void preencheCampo(ItemVenda itemVenda) {
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
        }
    }

    public class Comparador implements Comparator<ItemVenda> {

        @Override
        public int compare(ItemVenda item1, ItemVenda item2) {
            return item1.getProduto().getNome().compareTo(item2.getProduto().getNome());
        }
    }
}
