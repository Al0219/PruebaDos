package com.tuapp.pruebados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MaestroActivity extends AppCompatActivity {

    Button btnNotas, btnAsistencia, btnActividades, btnAvisos, btnChat;
    int idMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maestro);

        // Recibe el ID del maestro desde el Login
        idMaestro = getIntent().getIntExtra("id_usuario", -1);

        btnNotas = findViewById(R.id.btnNotas);
        btnAsistencia = findViewById(R.id.btnAsistencia);
        btnActividades = findViewById(R.id.btnActividades);
        btnAvisos = findViewById(R.id.btnAvisos);
        btnChat = findViewById(R.id.btnChat);

        btnNotas.setOnClickListener(v -> {
            Intent i = new Intent(this, NotasActivity.class);
            i.putExtra("id_maestro", idMaestro);
            startActivity(i);
        });

        btnAsistencia.setOnClickListener(v -> {
            Intent i = new Intent(this, AsistenciaActivity.class);
            i.putExtra("id_maestro", idMaestro);
            startActivity(i);
        });

        btnActividades.setOnClickListener(v -> {
            Intent i = new Intent(this, ActividadesActivity.class);
            i.putExtra("id_maestro", idMaestro);
            startActivity(i);
        });

        btnAvisos.setOnClickListener(v -> {
            Intent i = new Intent(this, AvisosActivity.class);
            i.putExtra("id_maestro", idMaestro);
            startActivity(i);
        });

        /*btnChat.setOnClickListener(v -> {
            Intent i = new Intent(this, ChatMaestroActivity.class);
            i.putExtra("id_maestro", idMaestro);
            startActivity(i);
        });*/
    }
}

