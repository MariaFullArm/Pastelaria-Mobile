package br.com.fulltime.projeto.foodtruck.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;
import br.com.fulltime.projeto.foodtruck.util.CpfUtil;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_POSICAO_INVALIDA;

public class FormularioVendedorFragment extends MainFragment {

    private int posicaoRecibida = CODIGO_POSICAO_INVALIDA;
    private EditText campoNome;
    private EditText campoCPF;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_formulario_vendedor, container, false);

        inicializaCampos(view);

        Bundle dadosRecebidos = getArguments();
        if (dadosRecebidos != null) {
            Vendedor vendedorRecebido = (Vendedor) dadosRecebidos.getSerializable(CHAVE_VENDEDOR);
            posicaoRecibida = dadosRecebidos.getInt(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);
            preencheCampos(vendedorRecebido);
        }

        Button botaoSalvar = view.findViewById(R.id.formulario_vendedor_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoCPF.length() < 11 || campoNome.length() < 1) {
                    Toast.makeText(getContext(),
                            "O formulario não foi prenchido corretamente", Toast.LENGTH_SHORT).show();
                } else {
                    Vendedor vendedor = criaVendedor();
                    retornaVendedor(vendedor);
                }
            }
        });

        return view;
    }

    @NonNull
    private void retornaVendedor(Vendedor vendedor) {
//        ListaVendedoresFragment listaVendedoresFragment = new ListaVendedoresFragment();
//        Bundle parametros = new Bundle();
//        parametros.putSerializable(CHAVE_VENDEDOR, vendedor);
//        parametros.putInt(CHAVE_POSICAO, posicaoRecibida);
//        listaVendedoresFragment.setArguments(parametros);
//        getFragmentManager().popBackStack();

        ListaVendedoresFragment listaVendedoresFragment = new ListaVendedoresFragment();
        Bundle parametros = new Bundle();
        parametros.putSerializable(CHAVE_VENDEDOR, vendedor);
        parametros.putInt(CHAVE_POSICAO, posicaoRecibida);
        listaVendedoresFragment.setArguments(parametros);
        
        trocaFragment(listaVendedoresFragment);
    }

    @NonNull
    private Vendedor criaVendedor() {
        String cpf = campoCPF.getText().toString();
        String nome = campoNome.getText().toString();
        Vendedor vendedor = new Vendedor();
        String cpfFormatado = new CpfUtil().formataCPF(cpf);
        vendedor.setCpf(cpfFormatado);
        vendedor.setNome(nome);

        return vendedor;
    }

    private void preencheCampos(Vendedor vendedorRecebido) {
        campoCPF.setText(vendedorRecebido.getCpf());
        campoNome.setText(vendedorRecebido.getNome());
    }

    private void inicializaCampos(View view) {
        campoNome = view.findViewById(R.id.formulario_vendedor_nome);
        campoCPF = view.findViewById(R.id.formulario_vendedor_cpf);
    }
}
