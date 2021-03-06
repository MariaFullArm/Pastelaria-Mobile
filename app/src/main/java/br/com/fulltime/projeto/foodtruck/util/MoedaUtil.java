package br.com.fulltime.projeto.foodtruck.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by alex on 18/08/17.
 */

public class MoedaUtil {

    public static String formataParaBrasileiro(BigDecimal valor) {
        NumberFormat moeda = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return moeda.format(valor).replace("R$", "R$ ").replace("-R$ ", "R$ -");
    }

    public static BigDecimal validaMoeda(String valorEmTexto) {
        if (!valorEmTexto.isEmpty()) {
            String replace = valorEmTexto.replace(",", ".");
            return new BigDecimal(replace.substring(3));
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal validaMoedaEditText(String valorEmTexto) {
        if (!valorEmTexto.isEmpty()) {
            String replace = valorEmTexto.replace(",", ".");
            return new BigDecimal(replace.substring(2));
        }
        return BigDecimal.ZERO;
    }
}
