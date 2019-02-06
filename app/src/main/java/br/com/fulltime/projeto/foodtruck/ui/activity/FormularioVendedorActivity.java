package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_POSICAO_INVALIDA;

public class FormularioVendedorActivity extends AppCompatActivity {


    public static final String TITLE_TOOLBAR_VENDEDOR = "Formulário do Vendedor";
    private int posicaoRecibida = CODIGO_POSICAO_INVALIDA;
    private EditText campoNome;
    private EditText campoCPF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_vendedor);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_centralizada);
        TextView tituloToolbar = findViewById(R.id.title_toolbar);
        tituloToolbar.setText(TITLE_TOOLBAR_VENDEDOR);

        inicializaCampos();

        Intent intentRecebida = getIntent();
        if (intentRecebida.hasExtra(CHAVE_VENDEDOR)) {
            Vendedor vendedorRecebido = (Vendedor) intentRecebida.getSerializableExtra(CHAVE_VENDEDOR);
            posicaoRecibida = intentRecebida.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);
            preencheCampos(vendedorRecebido);
        }

        Button botaoSalvar = findViewById(R.id.formulario_vendedor_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoCPF.length() < 11 || campoNome.length() < 1) {
                    Toast.makeText(FormularioVendedorActivity.this,
                            "O formulario não foi prenchido corretamente", Toast.LENGTH_SHORT).show();
                } else {
                    Vendedor vendedor = criaVendedor();
                    retornaVendedor(vendedor);
                    finish();
                }
            }
        });

    }

    @NonNull
    private void retornaVendedor(Vendedor vendedor) {
        Intent intent = new Intent();
        intent.putExtra(CHAVE_VENDEDOR, vendedor);
        intent.putExtra(CHAVE_POSICAO, posicaoRecibida);
        setResult(Activity.RESULT_OK, intent);
    }

    @NonNull
    private Vendedor criaVendedor() {
        String cpf = campoCPF.getText().toString();
        String nome = campoNome.getText().toString();
        Vendedor vendedor = new Vendedor();
        vendedor.setCpf(cpf);
        vendedor.setNome(nome);

        return vendedor;
    }

    private void preencheCampos(Vendedor vendedorRecebido) {
        campoCPF.setText(vendedorRecebido.getCpf());
        campoNome.setText(vendedorRecebido.getNome());
    }

    private void inicializaCampos() {
        campoNome = findViewById(R.id.formulario_vendedor_nome);
        campoCPF = findViewById(R.id.formulario_vendedor_cpf);
    }
}
