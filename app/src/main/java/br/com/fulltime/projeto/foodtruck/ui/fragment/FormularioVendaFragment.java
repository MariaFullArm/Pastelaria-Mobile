package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.ItemVenda;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.SelecaoProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.ItemVendaAdapter;
import br.com.fulltime.projeto.foodtruck.util.DataUtil;


public class FormularioVendaFragment extends Fragment {

    private EditText data;
    private RecyclerView listaDeProdutos;
    private List<ItemVenda> itensVenda;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formulario_venda, container, false);

        ((MainActivity)getActivity()).setToolbarTiltle("Cadastrar Venda");
        configuraData(view);
        listaDeProdutos = view.findViewById(R.id.formulario_venda_lista_produtos);
        itensVenda = new ArrayList<>();
        ItemVendaAdapter adapter = new ItemVendaAdapter(getContext(), itensVenda);
        listaDeProdutos.setAdapter(adapter);

        Button botaoSelecionarProdutos = view.findViewById(R.id.formulario_venda_botao_selecionar_produto);
        botaoSelecionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelecaoProdutoActivity.class);
                startActivityForResult(intent, 6);
            }
        });

        return view;
    }

    private void configuraData(View view) {
        data = view.findViewById(R.id.formulario_venda_data);
        Calendar calendar = Calendar.getInstance();
        String dataFormatada = DataUtil.formataParaBrasileiro(calendar);
        data.setText(dataFormatada);
        adicionaCalendario();
    }

    private void adicionaCalendario() {
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaDatePicker(Calendar.getInstance());
            }
        });
    }

    private void chamaDatePicker(Calendar calendar) {
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(ano, mes, dia);
                String formatoBrasileiro = DataUtil.formataParaBrasileiro(calendar);
                data.setText(formatoBrasileiro);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
                List<ItemVenda> itensRecebidos = (List<ItemVenda>) data.getSerializableExtra("LISTA_ITEM_VENDA");
            itensVenda.clear();
            itensVenda = itensRecebidos;
        }
    }
}
