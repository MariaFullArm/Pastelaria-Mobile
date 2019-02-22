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

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import br.com.fulltime.projeto.foodtruck.request.RequestComunicador;
import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.request.RequestVendedor;
import br.com.fulltime.projeto.foodtruck.modelo.Vendedor;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CHAVE_VENDEDOR;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR;

public class FormularioVendedorActivity extends AppCompatActivity {

    public static final String TITLE_TOOLBAR_VENDEDOR = "Formulário do Vendedor";
    private int requisicao;
    private EditText campoNome;
    private EditText campoCPF;
    private RequestVendedor request = new RequestVendedor(this);
    private int id = -1;

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
            requisicao = intentRecebida.getIntExtra("requisicao", CODIGO_DE_REQUISICAO_ALTERA_VENDEDOR);
            preencheCampos(vendedorRecebido);
            id = vendedorRecebido.getId();
        }

        Button botaoSalvar = findViewById(R.id.formulario_vendedor_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ehFormularioDeVendedorValido()) {
                    Vendedor vendedor = criaVendedor();

                    if (vendedor.getId() >= 0) {
                        request.alterarVendedor(vendedor, new RequestComunicador() {
                            @Override
                            public void comunica() {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                    } else {
                        request.inserirVendedor(vendedor, new RequestComunicador() {
                            @Override
                            public void comunica() {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                    }
                }
            }
        });

    }

    private boolean ehFormularioDeVendedorValido() {
        boolean bool = true;
        if (campoNome.length() < 1) {
            Toast.makeText(FormularioVendedorActivity.this, "Nome não foi prenchido", Toast.LENGTH_SHORT).show();
            campoNome.setBackgroundResource(R.drawable.underline_vermelho);
            bool = false;
        }else
            campoNome.setBackgroundResource(R.drawable.underline_preto);

        if (campoCPF.length() < 14) {
            Toast.makeText(FormularioVendedorActivity.this, "CPF não foi prenchido corretamente", Toast.LENGTH_SHORT).show();
            campoCPF.setBackgroundResource(R.drawable.underline_vermelho);
            bool = false;
        }else
            campoCPF.setBackgroundResource(R.drawable.underline_preto);

        return bool;
    }

    @NonNull
    private Vendedor criaVendedor() {
        String cpf = campoCPF.getText().toString();
        String nome = campoNome.getText().toString();
        Vendedor vendedor = new Vendedor();
        vendedor.setCpf(cpf);
        vendedor.setNome(nome);
        vendedor.setId(id);

        return vendedor;
    }

    private void preencheCampos(Vendedor vendedorRecebido) {
        campoCPF.setText(vendedorRecebido.getCpf());
        campoNome.setText(vendedorRecebido.getNome());
    }

    private void inicializaCampos() {
        campoNome = findViewById(R.id.formulario_vendedor_nome);
        campoCPF = findViewById(R.id.formulario_vendedor_cpf);
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(campoCPF, smf);
        campoCPF.addTextChangedListener(mtw);
    }
}
