package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.fulltime.projeto.foodtruck.MoneyTextWatcher;
import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.request.RequestComunicador;
import br.com.fulltime.projeto.foodtruck.request.RequestProduto;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_PRODUTO;

public class FormularioProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TITLE_TOOLBAR_PRODUTO = "Formulário do Produto";
    private Spinner spinnerTipo;
    private EditText campoNome;
    private EditText campoValor;
    private EditText campoDescricao;
    private String string;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produto);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_centralizada);
        TextView tituloToolbar = findViewById(R.id.title_toolbar);
        tituloToolbar.setText(TITLE_TOOLBAR_PRODUTO);

        inicializaCampos();
        recebeProduto();
        configuraBotaoSalvar();
    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(R.id.formulario_produto_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ehFormularioDeProdutoValido()) {
                    Produto produto = criaProduto();

                    RequestProduto request = new RequestProduto(FormularioProdutoActivity.this);
                    if (id >= 0){
                        request.alteraProduto(produto, new RequestComunicador() {
                            @Override
                            public void comunica() {
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });
                    }
                    else{
                        request.insereProduto(produto, new RequestComunicador() {
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

    private void recebeProduto() {
        Intent intentRecebida = getIntent();
        if (intentRecebida.hasExtra(CHAVE_PRODUTO)) {
            Produto produtoRecebido = (Produto) intentRecebida.getSerializableExtra(CHAVE_PRODUTO);
            preencheCampos(produtoRecebido);
            this.id = produtoRecebido.getId();
        }
    }

    private boolean ehFormularioDeProdutoValido() {
        if (campoValor.length() < 1) {
            Toast.makeText(FormularioProdutoActivity.this, "O campo Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (campoNome.length() < 1) {
            Toast.makeText(FormularioProdutoActivity.this, "O campo Nome não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }
        String valorString = campoValor.getText().toString();
        BigDecimal valorReal = MoedaUtil.validaMoeda(valorString);
        if(valorReal== BigDecimal.ZERO){
            Toast.makeText(FormularioProdutoActivity.this, "Valor não pode ser zero", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Produto criaProduto() {
        Produto produto = new Produto();
        produto.setTipo(string);
        produto.setNome(campoNome.getText().toString());
        produto.setDescricao(campoDescricao.getText().toString());
        String valor = campoValor.getText().toString();
        produto.setValor(MoedaUtil.validaMoeda(valor));
        produto.setId(id);

        return produto;
    }

    public void inicializaCampos() {
        spinnerTipo = findViewById(R.id.formulario_produto_tipo);
        spinnerTipo.setOnItemSelectedListener(this);
        campoNome = findViewById(R.id.formulario_produto_nome);
        campoValor = findViewById(R.id.formulario_produto_preco);
        Locale br = new Locale("pt", "BR");
        campoValor.addTextChangedListener(new MoneyTextWatcher(campoValor, br));
        campoDescricao = findViewById(R.id.formulario_produto_descricao);

        configuraSpinner();
    }

    private void configuraSpinner() {
        List<String> tipos = new ArrayList<>();
        tipos.add("Pastel");
        tipos.add("Bebida");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
    }


    public void preencheCampos(Produto produto) {
        campoDescricao.setText(produto.getDescricao());
        campoNome.setText(produto.getNome());
        campoValor.setText(produto.getValor().toString());
        campoDescricao.setText(produto.getDescricao());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        string = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}