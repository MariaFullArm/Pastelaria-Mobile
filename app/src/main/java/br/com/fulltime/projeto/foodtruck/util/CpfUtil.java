package br.com.fulltime.projeto.foodtruck.util;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class CpfUtil {

    public String formataCPF(String cpf) {

        String bloco1 = cpf.substring(0, 3);
        String bloco2 = cpf.substring(3, 6);
        String bloco3 = cpf.substring(6, 9);
        String bloco4 = cpf.substring(9, 11);
        cpf = bloco1 + "." + bloco2 + "." + bloco3 + "-" + bloco4;
        return cpf;
    }

    public Boolean validaCPF(String s) {
        Integer[] validos = new Integer[]{10, 11, 12, 21, 22, 23, 32, 33, 34, 43, 44, 45, 54, 55, 56, 65, 66, 67, 76, 77, 78, 87, 88};

        ArrayList<Integer> n = new ArrayList<>();
        n.add(Integer.parseInt(s.substring(0, 1)));
        n.add(Integer.parseInt(s.substring(1, 2)));
        n.add(Integer.parseInt(s.substring(2, 3)));
        n.add(Integer.parseInt(s.substring(4, 5)));
        n.add(Integer.parseInt(s.substring(5, 6)));
        n.add(Integer.parseInt(s.substring(6, 7)));
        n.add(Integer.parseInt(s.substring(8, 9)));
        n.add(Integer.parseInt(s.substring(9, 10)));
        n.add(Integer.parseInt(s.substring(10, 11)));
        n.add(Integer.parseInt(s.substring(12, 13)));
        n.add(Integer.parseInt(s.substring(13)));

        int total = 0;
        for (Integer i : n) {
            total += i;
        }
        for (int i = 0; i < 23; i++){
            if(total == validos[i])
                return true;
        }
        return false;
    }
}
