package com.app.android.cronometro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.os.Handler;

public class CronometroActivity extends AppCompatActivity {
    private int segundos = 0;
    private boolean ejecutando;
    private boolean estabaEjecutando;

    Button empezar, pausar, reiniciar;
    Chronometer cronometro;
    long Time = 0;
    String tiempoActual = "";
    Boolean actividad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);


        if (savedInstanceState != null) {
            segundos = savedInstanceState.getInt("segundos");
            ejecutando = savedInstanceState.getBoolean("ejecutando");
            estabaEjecutando = savedInstanceState.getBoolean("estabaEjecutando");
        }

        cronometro = findViewById(R.id.etiquetaContador);
        empezar = findViewById(R.id.botonEmpezar);
        pausar = findViewById(R.id.botonPausar);
        reiniciar = findViewById(R.id.botonReiniciar);
        empezar.setEnabled(true);
        pausar.setEnabled(false);
        reiniciar.setEnabled(false);
        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empezar.setEnabled(false);
                pausar.setEnabled(true);
                reiniciar.setEnabled(true);
                if (!actividad) {
                    cronometro.setBase(SystemClock.elapsedRealtime());
                    cronometro.start();
                } else {
                    cronometro.start();
                }
            }
        });
        pausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empezar.setEnabled(true);
                pausar.setEnabled(false);
                reiniciar.setEnabled(true);
                cronometro.stop();
                cronometro.setText(tiempoActual);
                actividad = true;
            }
        });
        reiniciar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                empezar.setEnabled(true);
                pausar.setEnabled(false);
                reiniciar.setEnabled(false);
                cronometro.stop();
                cronometro.setText("00:00");
                actividad = false;
            }
        });
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (!actividad) {
                    long minutos = ((SystemClock.elapsedRealtime() - cronometro.getBase()) / 1000) / 60;
                    long segundos = ((SystemClock.elapsedRealtime() - cronometro.getBase()) / 1000) % 60;
                    tiempoActual = "0" + minutos + ":" + "0" + segundos;
                    cronometro.setText(tiempoActual);
                    Time = SystemClock.elapsedRealtime();
                } else {
                    long minutos = ((Time - cronometro.getBase()) / 1000) / 60;
                    long segundos = ((Time - cronometro.getBase()) / 1000) % 60;
                    tiempoActual = "0" + minutos + ":" + "0" + segundos;
                    cronometro.setText(tiempoActual);
                    Time = Time + 1000;
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void onClickIniciar(View view) {
        ejecutando = true;
        ejecutarTemporizador();
    }

    public void onClickPausar(View view) {
        ejecutando = false;
    }

    public void onClickReiniciar(View view) {
        ejecutando = false;
        segundos = 0;
    }

    private void ejecutarTemporizador() {
        final TextView tiempo = findViewById(R.id.etiquetaReloj);
        final Handler manipulador = new Handler();
        manipulador.post(new Runnable() {
            @Override
            public void run() {
                int horas = segundos / 3600;
                int minutos = (segundos % 3600) / 60;
                int seg = segundos & 60;
                @SuppressLint("DefaultLocale") String crono = String.format("%d:&02d:%02d", horas, minutos, seg);
                tiempo.setText(crono);
                if (ejecutando) {
                    segundos++;
                }
                manipulador.postDelayed(this, 1000);
            }
        });
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("segundos", segundos);
        savedInstanceState.putBoolean("ejecutando", ejecutando);
        savedInstanceState.putBoolean("estabaEjecutando", estabaEjecutando);
    }

    @Override
    protected void onStop() {
        super.onStop();
        estabaEjecutando = ejecutando;
        ejecutando = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (estabaEjecutando) {
            ejecutando = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        estabaEjecutando = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cronometro, menu);
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
}