package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.listener.OnItemClickListenerHistorico;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.util.MoedaUtil.formataParaBrasileiro;

public class HistoricoVendaAdapter extends RecyclerView.Adapter<HistoricoVendaAdapter.HistoricoViewHolder> {

    private final Context context;
    private List<Venda> vendas;
    private List<Vendedor> vendedores;
    private OnItemClickListenerHistorico onItemClickListenerHistorico;

    public HistoricoVendaAdapter(Context context) {
        this.context = context;
        vendas = new ArrayList<>();

        Call<List<Vendedor>> call = new RetrofitConfig().getVendedorService().lista();
        call.enqueue(new Callback<List<Vendedor>>() {
            @Override
            public void onResponse(Call<List<Vendedor>> call, Response<List<Vendedor>> response) {
                if (response.isSuccessful()) {
                    vendedores = new ArrayList<>(response.body());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Vendedor>> call, Throwable t) {

            }
        });
    }

    public void setOnClickListener(OnItemClickListenerHistorico onClickListener) {
        onItemClickListenerHistorico = onClickListener;
    }

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_historico, viewGroup, false);
        return new HistoricoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int i) {
        Venda venda = vendas.get(i);
        holder.vincula(venda);
    }

    @Override
    public int getItemCount() {
        return vendas.size();
    }

    public void subtituiLista(List<Venda> vendas) {
        this.vendas = new ArrayList<>(vendas);
        notifyDataSetChanged();
    }

    public class HistoricoViewHolder extends RecyclerView.ViewHolder {

        private Venda venda;
        private final TextView nomeVendedor;
        private final TextView dataVenda;
        private final TextView precoVenda;
        private final ImageView opcoes;

        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeVendedor = itemView.findViewById(R.id.item_historico_vendedor);
            dataVenda = itemView.findViewById(R.id.item_historico_data);
            precoVenda = itemView.findViewById(R.id.item_historico_valor);
            opcoes = itemView.findViewById(R.id.item_historico_opcoes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenerHistorico.onItemClick(venda);
                }
            });

            opcoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, opcoes);

                    popup.inflate(R.menu.menu_opcao_historico);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.historico_deletar:
                                    onItemClickListenerHistorico.onItemClickDeletar(venda);
                                    break;
                                case R.id.historico_editar:
                                    onItemClickListenerHistorico.onItemClickEditar(venda);
                                    break;
                                case R.id.historico_finalizar:
                                    onItemClickListenerHistorico.onItemClickFinalizar(venda);
                                    break;
                            }
                            return false;
                        }
                    });
                    Menu menu = popup.getMenu();
                    if(venda.isStatus()){
                        menu.findItem(R.id.historico_finalizar).setTitle("Desfazer");
                        menu.findItem(R.id.historico_editar).setEnabled(false);
                    }
                    popup.show();
                }
            });
        }

        public void vincula(Venda venda) {
            this.venda = venda;
            preencheCampos(venda);

        }

        private void preencheCampos(Venda venda) {
            if (vendedores != null) {
                if (!vendedores.isEmpty())
                    for (Vendedor vendedor : vendedores) {
                        if (venda.getId_vendedor() == vendedor.getId())
                            nomeVendedor.setText(vendedor.getNome());
                    }
            }
            dataVenda.setText(venda.getData_venda().toString().replace("-", "/"));
            precoVenda.setText(formataParaBrasileiro(venda.getTotal()));
            ConstraintLayout contraint = itemView.findViewById(R.id.item_historico_constraint);
            if (venda.isStatus() == false) {
                contraint.setBackgroundResource(R.drawable.borda_pendente);
            }
            else{
                contraint.setBackgroundResource(R.drawable.borda_finalizada);
            }
        }
    }
}
