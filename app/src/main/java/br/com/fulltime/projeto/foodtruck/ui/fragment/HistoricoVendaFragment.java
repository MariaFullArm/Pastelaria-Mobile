package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Venda;
import br.com.fulltime.projeto.foodtruck.ui.activity.MainActivity;
import br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.HistoricoVendaAdapter;

public class HistoricoVendaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico_venda, container, false);

        ((MainActivity)getActivity()).setToolbarTiltle("Historico de Venda");

        List<Venda> vendas = Arrays.asList(
                new Venda(),
                new Venda(),
                new Venda());

        RecyclerView listaHistorico = view.findViewById(R.id.historico_venda_recycleview);
        listaHistorico.setAdapter(new HistoricoVendaAdapter(getContext(), vendas));

        return view;
    }
}
