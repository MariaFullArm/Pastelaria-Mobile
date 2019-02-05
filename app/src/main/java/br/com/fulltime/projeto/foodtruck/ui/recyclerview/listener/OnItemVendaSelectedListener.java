package br.com.fulltime.projeto.foodtruck.ui.recyclerview.adapter.listener;

import android.view.View;
import android.widget.AdapterView;

public interface OnItemSelectedListener extends AdapterView.OnItemSelectedListener {

     void onItemSelected(AdapterView<?> parent, View view, int position, long id);


    void onNothingSelected(AdapterView<?> parent);
