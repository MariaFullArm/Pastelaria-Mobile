package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVendidoAPI;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.retrofit.RetrofitConfig;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.SelecaoProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.adapter.SpinnerVendedorAdapter;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ItemVendaAdapter;
import br.com.fulltime.projeto.foodtruck.util.DataUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.fulltime.projeto.foodtruck.util.MoedaUtil.formataParaBrasileiro;
import static br.com.fulltime.projeto.foodtruck.util.MoedaUtil.validaMoeda;


public class FormularioVendaFragment extends Fragment {

    private RecyclerView listaDeProdutos;
    private List<ItemVenda> itensVenda = new ArrayList<>();
    private List<Vendedor> vendedores;
    private ItemVendaAdapter adapter;
    private Button botaoSelecionarProdutos;
    private Button botaoSalvarVenda;
    private Spinner spinnerDeVendedor;
    private EditText observacoes;
    private TextView total;
    private EditText data;
    private String cpfVendedor;
    private Calendar calendar = Calendar.getInstance();
    private Venda venda = new Venda();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formulario_venda, container, false);

        ((MainActivity) getActivity()).setToolbarTiltle("Cadastrar Venda");
        inicializaCampos(view);
        Bundle arguments = getArguments();

        venda.setId(-1);
        if (arguments != null) {
            if (arguments.getSerializable("venda") != null) {
                venda = (Venda) arguments.getSerializable("venda");
                if (venda.getObservacoes() != null) {
                    observacoes.setText(venda.getObservacoes());
                }
                Call<List<ItemVendidoAPI>> call = new RetrofitConfig().getItemService().listaDeItem(venda.getId());
                call.enqueue(new Callback<List<ItemVendidoAPI>>() {
                    @Override
                    public void onResponse(Call<List<ItemVendidoAPI>> call, Response<List<ItemVendidoAPI>> response) {
                        if (response.isSuccessful()) {
                            List<ItemVendidoAPI> vendidoAPIS = response.body();
                            populaItemVenda(vendidoAPIS);

                        } else try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ItemVendidoAPI>> call, Throwable t) {

                    }
                });
            }
        }

        configuraData(view);

        configuraAdapter();
        configuraBotaoSelecionarProduto();
        configuraSpinnerVendedor();
        configuraValorTotal();
        configuraBotaoSalvar();
        adapter.substituiLista(itensVenda);

        return view;
    }

    private void populaItemVenda(final List<ItemVendidoAPI> lista) {
        Call<List<Produto>> call = new RetrofitConfig().getProdutoService().lista();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();
                    for (int i = 0; i < lista.size(); i++) {
                        for (int j = 0; j < produtos.size(); j++) {
                            if (lista.get(i).getId_produto() == produtos.get(j).getId()) {
                                ItemVenda itemVenda = new ItemVenda(produtos.get(j));
                                itemVenda.setQuantidade(lista.get(i).getQuantidade());
                                itensVenda.add(itemVenda);
                            }
                        }
                    }
                    adapter.substituiLista(itensVenda);
                    configuraValorTotal();

                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }

    private void configuraBotaoSalvar() {
        botaoSalvarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Venda vendaCriada = criaVenda();

                if (venda.getId() < 0) {
                    Call<Venda> call = new RetrofitConfig().getVendaService().insere(vendaCriada);
                    call.enqueue(new Callback<Venda>() {
                        @Override
                        public void onResponse(Call<Venda> call, Response<Venda> response) {
                            if (response.isSuccessful()) {
                                Venda venda = response.body();
                                unirItensVendidosNaVenda(venda);
                            } else try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Venda> call, Throwable t) {
                            Log.e("onFailure", t.toString());
                        }
                    });
                } else {
                    Call<ResponseBody> call = new RetrofitConfig().getVendaService().altera(venda.getId(), vendaCriada);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                excluirItemVendidoDaVenda(vendaCriada);
                            } else try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    private void excluirItemVendidoDaVenda(final Venda vendaCriada) {
        Call<ResponseBody> call = new RetrofitConfig().getItemService().deletaItensDaVenda(venda.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    unirItensVendidosNaVenda(vendaCriada);
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void unirItensVendidosNaVenda(Venda vendaRecebida) {
        for (ItemVenda itemVenda : itensVenda) {
            itemVenda.setVenda(vendaRecebida);
        }
        Call<ResponseBody> call = new RetrofitConfig().getItemService().insere(itensVenda);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (venda.getId() < 0) {
                        Toast.makeText(getContext(), "Venda realizada com sucesso", Toast.LENGTH_SHORT).show();
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction tx = manager.beginTransaction();
                        HistoricoVendaFragment fragment = new HistoricoVendaFragment();
                        tx.replace(R.id.frame_main, fragment);
                        tx.commit();
                    } else {
                        Toast.makeText(getContext(), "Venda Modificada com sucesso", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }

                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private Venda criaVenda() {
        venda.setTotal(validaMoeda(total.getText().toString()));
        venda.setObservacoes(observacoes.getText().toString());

        if(itensVenda.size() == 0)
            total.setBackgroundResource(R.drawable.underline_vermelho);

        if (vendedores.size() == 0) {
            spinnerDeVendedor.setBackgroundResource(R.drawable.underline_vermelho);
        } else {
            for (Vendedor vendedor : vendedores) {
                if (vendedor.getCpf().equals(cpfVendedor)) {
                    venda.setId_vendedor(vendedor.getId());
                }
            }
        }
        venda.setData_venda(new DataUtil().converteParaDataApi(calendar.getTime()));

        return venda;
    }

    private void configuraValorTotal() {
        BigDecimal valorDaCompra = new BigDecimal(0);
        for (ItemVenda item : itensVenda) {
            BigDecimal valorAntigo = new BigDecimal(valorDaCompra.doubleValue());
            BigDecimal valor = item.getProduto().getValor();
            BigDecimal i = new BigDecimal(item.getQuantidade());
            valorDaCompra = (valorAntigo.add(valor.multiply(i)));
        }
        total.setText(formataParaBrasileiro(valorDaCompra));
    }

    private void configuraSpinnerVendedor() {
        inicializaListaDeVendedores();
        spinnerDeVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cpfVendedor = ((TextView) view.findViewById(R.id.spinner_vendedor_cpf)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void inicializaListaDeVendedores() {
        Call<List<Vendedor>> call = new RetrofitConfig().getVendedorService().lista();
        call.enqueue(new Callback<List<Vendedor>>() {
            @Override
            public void onResponse(Call<List<Vendedor>> call, Response<List<Vendedor>> response) {
                if (response.isSuccessful()) {
                    vendedores = response.body();
                    SpinnerVendedorAdapter adapterSpinner = new SpinnerVendedorAdapter(getContext(), R.layout.item_spinner_vendedor, vendedores);
                    spinnerDeVendedor.setAdapter(adapterSpinner);

                    if (venda.getId_vendedor() >= -1) {
                        for (int i = 0; i < vendedores.size(); i++) {
                            if (vendedores.get(i).getId() == venda.getId_vendedor()) {
                                spinnerDeVendedor.setSelection(i);
                            }
                        }
                    }
                } else try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    Toast.makeText(getContext(), jObjError.getString("erro"), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vendedor>> call, Throwable t) {

            }
        });
    }

    private void configuraBotaoSelecionarProduto() {
        botaoSelecionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelecaoProdutoActivity.class);
                intent.putExtra("LISTA_ITEM_VENDA", (Serializable) itensVenda);
                startActivityForResult(intent, 6);
            }
        });
    }

    private void inicializaCampos(View view) {
        listaDeProdutos = view.findViewById(R.id.pedido_lista_produtos);
        botaoSelecionarProdutos = view.findViewById(R.id.formulario_venda_botao_selecionar_produto);
        spinnerDeVendedor = view.findViewById(R.id.formulario_venda_spinner_vendedor);
        observacoes = view.findViewById(R.id.formulario_venda_observacoes);
        total = view.findViewById(R.id.formulario_venda_total);
        botaoSalvarVenda = view.findViewById(R.id.pedido_botao_finalizar);
    }

    private void configuraAdapter() {
        adapter = new ItemVendaAdapter(getContext());
        listaDeProdutos.setAdapter(adapter);
    }

    private void configuraData(View view) {
        data = view.findViewById(R.id.formulario_venda_data);
        if (venda.getData_venda() != null) {
            calendar.setTime(venda.getData_venda());
        }
        String dataFormatada = DataUtil.formataCalendar(calendar);
        data.setText(dataFormatada);
        adicionaCalendario();
    }

    private void adicionaCalendario() {
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaDatePicker();
            }
        });
    }

    private void chamaDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), configuraDatePicker(), year, month, day).show();
    }

    @NonNull
    private DatePickerDialog.OnDateSetListener configuraDatePicker() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                calendar.set(ano, mes, dia);
                String dataFormatada = DataUtil.formataCalendar(calendar);
                data.setText(dataFormatada.replace("-", "/"));
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
            List<ItemVenda> itensRecebidos = (List<ItemVenda>) data.getSerializableExtra("LISTA_ITEM_VENDA");
            adapter.substituiLista(itensRecebidos);
            itensVenda = new ArrayList<>(itensRecebidos);
            configuraValorTotal();
        }
    }
}
