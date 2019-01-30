package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.SelecaoProdutoActivity;
import br.com.fulltime.projeto.foodtruck.util.DataUtil;


public class FormularioVendaFragment extends Fragment  {

    private EditText data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formulario_venda, container, false);

        data = view.findViewById(R.id.formulario_venda_data);
        Calendar calendar = Calendar.getInstance();
        String dataFormatada = DataUtil.formataParaBrasileiro(calendar);
        data.setText(dataFormatada);
        adicionaCalendario();

        Button botaoSelecionarProdutos = view.findViewById(R.id.formulario_venda_botao_selecionar_produto);
        botaoSelecionarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelecaoProdutoActivity.class);
                startActivity(intent);
            }
        });

        return view;
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
}
