package com.renan.seconds33;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.renan.seconds33.DAO.PontuacaoDao;
import com.renan.seconds33.model.Pontuacao;

public class MainActivity extends AppCompatActivity {
    ImageButton b1;
    ImageButton b2;
    ImageButton titulo;
    TextView txtScore;
    Chronometer chtempo;
    boolean inicio, ch = false;
    int click = 0;
    public int teste = 0;
    AlertDialog builder;
    PontuacaoDao pontuacaoDao;
    Pontuacao model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============================= cria act.
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //===============================variaveis.
        b1 = (ImageButton) findViewById(R.id.bt_start_bt1);
        b2 = (ImageButton) findViewById(R.id.bt_bt2);
        titulo = (ImageButton) findViewById(R.id.bt_titulo);
        txtScore = (TextView) findViewById(R.id.txt_score);
        chtempo = (Chronometer) findViewById(R.id.ch_tempo);
        inicio = true;

        //==============================Declara DB Pontuacao
        pontuacaoDao = new PontuacaoDao(this);
        model = pontuacaoDao.buscarPontuacaoPorID(1);
        txtScore.setText("Best: " + String.valueOf(model.getScore()));

    }

    //================================jogar.
    public void play(View v) {
        titulo.setBackground(null);
        txtScore.setText(String.valueOf(click));
        if (ch == false) chrono();
        ch = true;
        if (inicio == false) {
            b1.setVisibility(View.VISIBLE);
            b1.setBackgroundResource(R.drawable.blackbutton);
            b2.setVisibility(View.INVISIBLE);
            inicio = true;
        } else {
            b2.setVisibility(View.VISIBLE);
            b2.setBackgroundResource(R.drawable.blackbutton);
            b1.setVisibility(View.INVISIBLE);
            inicio = false;
        }
        click++;
        txtScore.setText("Score: " + String.valueOf(click));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void chrono() {

        chtempo.setBase(SystemClock.elapsedRealtime());
        chtempo.start();

        chtempo.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                teste++;
                if (chtempo.getText().equals("00:33")) {

                    // Toast.makeText(getBaseContext(), "SCORE :" + String.valueOf(click), Toast.LENGTH_LONG).show();

                    //================================ if score > score salvo no banco, atualizar aqui
                    if (model.getScore() < click) {

                        model.set_id(1);
                        model.setScore(click);
                        long resultado = pontuacaoDao.AtualizarPontuacao(model);
                        if (resultado != -1) {
                            //  Toast.makeText(getBaseContext(), "Salvo",
                            //          Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Erro",
                                    Toast.LENGTH_LONG).show();
                        }

                        msg();
                    } else {
                        msgSad();
                    }

                }

            }
        });
    }

    public void msg() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("RECORD!!");
        alertDialog.setMessage("Score: " + String.valueOf(click));
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.show();
    }


    public void msgSad() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Maybe tomorrow");
        alertDialog.setMessage("Score: " + String.valueOf(click));
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.show();
    }
}
