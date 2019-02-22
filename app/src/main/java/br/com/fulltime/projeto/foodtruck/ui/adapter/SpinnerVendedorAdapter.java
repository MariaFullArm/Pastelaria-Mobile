package br.com.fulltime.projeto.foodtruck.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;

public class SpinnerVendedorAdapter extends ArrayAdapter<String> {

    private final int resource;
    private final List<Vendedor> vendedores;
    private LayoutInflater inflater;

    public SpinnerVendedorAdapter(Context context, int resource, List vendedores) {
        super(context, resource, vendedores);
        this.resource = resource;
        this.vendedores = vendedores;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    private View createItemView(int position, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);

        TextView nomeText = view.findViewById(R.id.spinner_vendedor_nome);
        TextView cpfText = view.findViewById(R.id.spinner_vendedor_cpf);

        nomeText.setText(vendedores.get(position).getNome());
        cpfText.setText(vendedores.get(position).getCpf());

        return view;
    }
}
