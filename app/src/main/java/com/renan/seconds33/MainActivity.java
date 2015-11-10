package com.renan.seconds33;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.*;
import com.google.android.gms.games.Game;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.renan.seconds33.DAO.PontuacaoDao;
import com.renan.seconds33.model.Pontuacao;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public ImageButton b1;
    public ImageButton b2;
    public ImageButton titulo;
    public TextView txtScore;
    public Chronometer chtempo;
    boolean inicio, ch = false;
    public int click = 0;
    public int teste = 0;
    public PontuacaoDao pontuacaoDao;
    public Pontuacao model;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============================= cria act.
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //================================ GOOGLE API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        //mGoogleApiClient.connect();

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

        //==============================Verifica se a pontuação já esta gravada na PlayGames
        int best = model.getScore();
        try {
            checkar(best);
        } catch (Exception e) {
            //Do nothing
        }

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
                    chtempo.stop();
                    //================================ if score > score salvo no banco, atualizar aqui
                    if (model.getScore() < click) {

                        model.set_id(1);
                        model.setScore(click);
                        long resultado = pontuacaoDao.AtualizarPontuacao(model);
                        try {
                            //================================== PLACAR dos jogadores
                            Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_best_score), Long.valueOf(click));
                        } catch (Exception e) {
                            //do notthing
                        }
                        try {
                            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getString(R.string.leaderboard_best_score)), 100);
                        } catch (Exception e) {
                            //do notthing
                        }

                        if (resultado != -1) {
                            //Do nothing
                        } else {
                            Toast.makeText(getBaseContext(), "Erro",
                                    Toast.LENGTH_LONG).show();
                        }
                        // alertdialogs + verificacao de desbloq. de conquista
                        msg();
                    } else {
                        msgSad();
                    }

                }

            }
        });
    }

    public void msg() {
        try {
            if (click >= 100) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_100_clicks));
            }
            if (click >= 250) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_250_clicks));
            }
            if (click >= 350) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_350_clicks));
            }
            if (click == 1) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_desito));
            }
            if (click == 33) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_33));
            }
            if (click >= 400) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_400_clicks));
            }
            if (click == 42) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_resposta_de_tudo));
            }
            if (click == 69) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement____));
            }
        } catch (Exception e) {
            //do notthing
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("RECORD!!");
        alertDialog.setCanceledOnTouchOutside(false);
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
        try {
            if (click >= 100) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_100_clicks));
            }
            if (click >= 250) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_250_clicks));
            }
            if (click >= 350) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_350_clicks));
            }
            if (click == 1) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_desito));
            }
            if (click == 33) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_33));
            }
            if (click >= 400) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_400_clicks));
            }
            if (click == 42) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_resposta_de_tudo));
            }
            if (click == 69) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement____));
            }
        } catch (Exception e) {
            //do notthing
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Maybe tomorrow");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage("Score: " + String.valueOf(click));
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.show();
    }

    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, "outro erro")) {
                mResolvingConnectionFailure = false;
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.sign_in_failed);
            }
        }
    }

    final String TAG = "TanC";

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): connecting");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStart(): not disconnect");
        // mGoogleApiClient.disconnect();
    }

    public void checkar(int click) {
        try {
            if (click >= 100) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_100_clicks));
            }
            if (click >= 250) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_250_clicks));
            }
            if (click >= 350) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_350_clicks));
            }
            if (click == 1) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_desito));
            }
            if (click == 33) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_33));
            }
            if (click >= 400) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_400_clicks));
            }
            if (click == 42) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_resposta_de_tudo));
            }
            if (click == 69) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement____));
            }
        } catch (Exception e) {
            //do notthing
        }
    }
    public void verplacares(View v){
        try {
            //================================== PLACAR dos jogadores
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getString(R.string.leaderboard_best_score)), 100);
        } catch (Exception e) {
            //do notthing
        }
    }
}
