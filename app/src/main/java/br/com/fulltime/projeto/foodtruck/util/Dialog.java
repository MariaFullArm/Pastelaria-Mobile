package br.com.fulltime.projeto.foodtruck.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioProdutoActivity;
import br.com.fulltime.projeto.foodtruck.ui.activity.FormularioVendedorActivity;

public class Dialog {

    private AlertDialog alerta;
    private Context context;

    public Dialog(Context context) {
        this.context = context;
    }

    public void alertaVendedor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alerta");
        builder.setMessage("Não existe nenhum Vendedor cadastrado no momento");

        builder.setPositiveButton("Cadastrar vendendor", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(context, FormularioVendedorActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public void alertaProduto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alerta");
        builder.setMessage("Não existe nenhum Produto cadastrado no momento");

        builder.setPositiveButton("Cadastrar Produto", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(context, FormularioProdutoActivity.class);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public void alertaHistorico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alerta");
        builder.setMessage("Não existe nenhuma Venda nessa lista");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }
}
