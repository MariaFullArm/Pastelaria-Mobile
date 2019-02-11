package br.com.fulltime.projeto.foodtruck.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.fulltime.projeto.foodtruck.R;
import br.com.fulltime.projeto.foodtruck.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    public static SwipeRefreshLayout swipeMain;
    public static ProgressBar progressMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_centralizada);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_main, new MainFragment()).commit();

        swipeMain = findViewById(R.id.swipe_main);
        progressMain = findViewById(R.id.progress_main);
    }

    public void setToolbarTiltle(String string) {
        TextView title = findViewById(R.id.title_toolbar);
        title.setText(string);
    }

    public static void setSwipeStatusMain(boolean bool) {
        swipeMain.setEnabled(bool);
    }

    public static void setSwipeRefreshingMain(boolean bool) {
        swipeMain.setRefreshing(bool);
    }

    public static void exibirProgressMain(boolean exibir) {
        progressMain.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}