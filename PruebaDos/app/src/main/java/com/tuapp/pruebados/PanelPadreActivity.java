package com.tuapp.pruebados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PanelPadreActivity extends AppCompatActivity {

    Spinner spinnerHijos;
    Button btnNotas, btnAsistencia, btnActividades, btnAvisos;
    ArrayList<String> hijosList = new ArrayList<>();
    ArrayList<Integer> idsHijos = new ArrayList<>();
    int idPadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_padre);

        spinnerHijos = findViewById(R.id.spinnerHijos);
        btnNotas = findViewById(R.id.btnVerNotas);
        btnAsistencia = findViewById(R.id.btnVerAsistencia);
        btnActividades = findViewById(R.id.btnVerActividades);
        btnAvisos = findViewById(R.id.btnVerAvisos);

        idPadre = getIntent().getIntExtra("id_padre", -1);

        cargarHijos();

        btnNotas.setOnClickListener(v -> abrirPantalla("notas"));
        btnAsistencia.setOnClickListener(v -> abrirPantalla("asistencia"));
        btnActividades.setOnClickListener(v -> abrirPantalla("actividades"));
        btnAvisos.setOnClickListener(v -> abrirPantalla("avisos"));
    }

    private void cargarHijos() {
        String URL = "http://10.0.2.2/centros/get_hijos.php?id_padre=" + idPadre;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject hijo = response.getJSONObject(i);
                            String nombre = hijo.getString("nombre") + " (" + hijo.getString("grado") + " " + hijo.getString("seccion") + ")";
                            hijosList.add(nombre);
                            idsHijos.add(hijo.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinnerHijos.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hijosList));
                },
                error -> Toast.makeText(this, "Error al cargar hijos", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void abrirPantalla(String destino) {
        if (idsHijos.isEmpty()) return;

        int idAlumno = idsHijos.get(spinnerHijos.getSelectedItemPosition());

        Intent intent = null;
        switch (destino) {
            case "notas":
                intent = new Intent(this, NotasAlumnoActivity.class);
                break;
            case "asistencia":
                intent = new Intent(this, AsistenciaAlumnoActivity.class);
                break;
            case "actividades":
                intent = new Intent(this, ActividadesAlumnoActivity.class);
                break;
            case "avisos":
                intent = new Intent(this, AvisosAlumnoActivity.class);
                break;
        }

        if (intent != null) {
            intent.putExtra("id_alumno", idAlumno);
            startActivity(intent);
        }
    }
}
