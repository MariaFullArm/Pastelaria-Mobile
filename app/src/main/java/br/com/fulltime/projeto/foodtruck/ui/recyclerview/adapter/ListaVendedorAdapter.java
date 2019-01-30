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
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.listener.OnItemClickListenerVendedor;

public class ListaVendedorAdapter extends RecyclerView.Adapter<ListaVendedorAdapter.VendedorViewHolder> {


    private final List<Vendedor> vendedores;
    private Context context;
    private OnItemClickListenerVendedor onItemClickListenerVendedor;

    public ListaVendedorAdapter(Context context, List<Vendedor> vendedores) {
        this.context = context;
        this.vendedores = vendedores;
    }

    public void setOnItemClickListenerVendedor(OnItemClickListenerVendedor onItemClickListenerVendedor){
        this.onItemClickListenerVendedor = onItemClickListenerVendedor;
    }

    @NonNull
    @Override
    public ListaVendedorAdapter.VendedorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_vendedor, viewGroup, false);
        return new VendedorViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVendedorAdapter.VendedorViewHolder holder, int posicao) {
        Vendedor vendedor = vendedores.get(posicao);
        holder.vincula(vendedor);
    }

    @Override
    public int getItemCount() {
        return vendedores.size();
    }

    public void adiciona(Vendedor vendedor) {
        vendedores.add(vendedor);
        notifyDataSetChanged();
    }

    public void altera(Vendedor vendedor, int posicao) {
        vendedores.set(posicao, vendedor);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        vendedores.remove(posicao);
        notifyItemRemoved(posicao);
    }

    class VendedorViewHolder extends RecyclerView.ViewHolder {

        private final TextView nome;
        private final TextView cpf;
        private Vendedor vendedor;

        public VendedorViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.item_produto_tipo);
            cpf = itemView.findViewById(R.id.item_vendedor_cpf);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenerVendedor.onItemClick(vendedor, getAdapterPosition());
                }
            });
        }

        public void vincula(Vendedor vendedor) {
            this.vendedor = vendedor;
            preencheCampo(vendedor);
        }

        private void preencheCampo(Vendedor vendedor) {
            nome.setText(vendedor.getNome());
            cpf.setText(vendedor.getCpf());
        }
    }
}
