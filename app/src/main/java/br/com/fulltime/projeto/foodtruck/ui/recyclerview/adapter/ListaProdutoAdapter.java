package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerProduto;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ProdutoViewHolder> {

    private final Context context;
    private List<Produto> produtos;
    private OnItemClickListenerProduto onItemClickListenerProduto;

    public ListaProdutoAdapter(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    public void setOnItemClickListenerProduto(OnItemClickListenerProduto onItemClickListenerProduto) {
        this.onItemClickListenerProduto = onItemClickListenerProduto;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_produto, viewGroup, false);
        return new ProdutoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder produtoViewHolder, int posicao) {
        Produto produto = produtos.get(posicao);
        produtoViewHolder.vincula(produto);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public void adiciona(Produto produto) {
        produtos.add(produto);
        notifyDataSetChanged();
    }

    public void altera(Produto produto, int posicao) {
        produtos.set(posicao, produto);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        produtos.remove(posicao);
        notifyItemRemoved(posicao);
    }

    public void substituiLista(List<Produto> produtos) {
        this.produtos = produtos;
        notifyDataSetChanged();
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final TextView tipo;
        private final TextView preco;
        private final ImageView mais;
        private Produto produto;

        public ProdutoViewHolder(@NonNull final View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.item_produto_nome);
            tipo = itemView.findViewById(R.id.item_vendedor_tipo);
            preco = itemView.findViewById(R.id.item_produto_preco);
            mais = itemView.findViewById(R.id.item_produto_mais);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenerProduto.onItemClick(produto, getAdapterPosition());
                }
            });
            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, mais);

                    popup.inflate(R.menu.menu_opcao_produtos);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.produto_deletar:
                                    onItemClickListenerProduto.onItemClickDeletar(produto, getAdapterPosition());
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }

        public void vincula(Produto produto) {
            this.produto = produto;
            preencheCampo(produto);
        }

        private void preencheCampo(Produto produto) {
            nome.setText(produto.getNome());
            tipo.setText(produto.getTipo());
            preco.setText(MoedaUtil.formataParaBrasileiro(produto.getValor()));
        }
    }
}
