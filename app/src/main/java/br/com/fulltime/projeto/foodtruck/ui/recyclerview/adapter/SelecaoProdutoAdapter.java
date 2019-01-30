package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class SelecaoProdutoAdapter extends RecyclerView.Adapter<SelecaoProdutoAdapter.SelecaoProdutoViewHolder> {

    private final Context context;
    private final List<Produto> produtos;

    public SelecaoProdutoAdapter(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
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
        Produto produto = produtos.get(i);
        selecaoProdutoViewHolder.vincula(produto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class SelecaoProdutoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final Spinner spinner;
        private final TextView preco;
        private Produto produto;

        public SelecaoProdutoViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.item_venda_nome_produto);
            spinner = itemView.findViewById(R.id.item_venda_spinner);
            preco = itemView.findViewById(R.id.item_venda_preco);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void vincula(Produto produto) {
            this.produto = produto;
            preencheCampo(produto);
        }

        private void preencheCampo(Produto produto) {
            nome.setText(produto.getNome());
            preco.setText(MoedaUtil.formataParaBrasileiro(produto.getValor()));
            List<String> tipos = new ArrayList<String>();
            tipos.add("0");tipos.add("1");tipos.add("2");tipos.add("3");tipos.add("4");tipos.add("5");tipos.add("6");tipos.add("7");tipos.add("8");tipos.add("9");tipos.add("10");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, tipos);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}
