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

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.modelo.Produto;
import br.com.fulltime.projeto.foodtruck.util.MoedaUtil;

import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_POSICAO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.ProdutoConstantes.CHAVE_PRODUTO;
import static br.com.fulltime.projeto.foodtruck.ui.activity.constantes.VendedorConstantes.CODIGO_POSICAO_INVALIDA;

public class FormularioProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TITLE_TOOLBAR_PRODUTO = "Formulário do Produto";
    private int posicaoRecibida = CODIGO_POSICAO_INVALIDA;
    private Spinner spinnerTipo;
    private EditText campoNome;
    private EditText campoValor;
    private EditText campoDescricao;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produto);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_centralizada);
        TextView tituloToolbar = findViewById(R.id.title_toolbar);
        tituloToolbar.setText(TITLE_TOOLBAR_PRODUTO);

        inicializaCampos();

        Intent intentRecebida = getIntent();
        if (intentRecebida.hasExtra(CHAVE_PRODUTO)) {
            Produto produtoRecebido = (Produto) intentRecebida.getSerializableExtra(CHAVE_PRODUTO);
            posicaoRecibida = intentRecebida.getIntExtra(CHAVE_POSICAO, CODIGO_POSICAO_INVALIDA);
            preencheCampos(produtoRecebido);
        }

        Button botaoSalvar = findViewById(R.id.formulario_produto_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoNome.length() < 1 || campoValor.length() < 1) {
                    Toast.makeText(FormularioProdutoActivity.this,
                            "O formulario não foi prenchido corretamente", Toast.LENGTH_SHORT).show();
                } else {
                    Produto produtoCriado = criaProduto();
                    enviaProduto(produtoCriado);
                    finish();
                }
            }

        });
    }

    private void enviaProduto(Produto produto) {
        Intent intent = new Intent();
        intent.putExtra(CHAVE_PRODUTO, produto);
        intent.putExtra(CHAVE_POSICAO, posicaoRecibida);
        setResult(Activity.RESULT_OK, intent);
    }

    private Produto criaProduto() {
        String tipo = string;
        String nome = campoNome.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String valorString = campoValor.getText().toString();
        BigDecimal valorReal = MoedaUtil.validaMoeda(valorString);
        Produto produto = new Produto();
        produto.criaProdutoSemId(tipo, nome, valorReal, descricao);
        return produto;
    }

    public void inicializaCampos() {
        spinnerTipo = findViewById(R.id.formulario_produto_tipo);
        spinnerTipo.setOnItemSelectedListener(this);
        campoNome = findViewById(R.id.formulario_produto_nome);
        campoValor = findViewById(R.id.formulario_produto_preco);
        campoDescricao = findViewById(R.id.formulario_produto_descricao);

        configuraSpinner();
    }

    private void configuraSpinner() {
        List<String> tipos = new ArrayList<String>();
        tipos.add("Pastel");
        tipos.add("Bebida");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
