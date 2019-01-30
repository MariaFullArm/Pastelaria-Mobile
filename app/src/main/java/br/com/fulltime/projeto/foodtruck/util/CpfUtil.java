package br.com.fulltime.projeto.foodtruck.util;

public class CpfUtil {

    public String formataCPF(String cpf) {

            String bloco1 = cpf.substring(0, 3);
            String bloco2 = cpf.substring(3, 6);
            String bloco3 = cpf.substring(6, 9);
            String bloco4 = cpf.substring(9, 11);
            cpf = bloco1 + "." + bloco2 + "." + bloco3 + "-" + bloco4;
            return cpf;
    }
}
