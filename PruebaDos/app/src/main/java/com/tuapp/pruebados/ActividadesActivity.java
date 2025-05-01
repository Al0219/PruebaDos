package com.tuapp.pruebados;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActividadesActivity extends AppCompatActivity {

    Spinner spinnerMaterias, spinnerTipo;
    EditText etTitulo, etDescripcion, etFechaEntrega;
    Button btnGuardar;
    ArrayList<String> materiasList = new ArrayList<>();
    ArrayList<Integer> idsMaterias = new ArrayList<>();
    int idMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        spinnerMaterias = findViewById(R.id.spinnerMaterias);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        etTitulo = findViewById(R.id.etTituloActividad);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFechaEntrega = findViewById(R.id.etFechaEntrega);
        btnGuardar = findViewById(R.id.btnGuardarActividad);

        idMaestro = getIntent().getIntExtra("id_maestro", -1);

        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"tarea", "examen"});
        spinnerTipo.setAdapter(tipoAdapter);

        cargarMaterias();

        btnGuardar.setOnClickListener(v -> guardarActividad());
    }

    private void cargarMaterias() {
        String URL = "http://10.0.2.2/centros/get_materias_maestro.php?id_maestro=" + idMaestro;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    materiasList.clear();
                    idsMaterias.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject materia = response.getJSONObject(i);
                            materiasList.add(materia.getString("nombre") + " (" + materia.getString("grado") + " " + materia.getString("seccion") + ")");
                            idsMaterias.add(materia.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinnerMaterias.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, materiasList));
                },
                error -> Toast.makeText(this, "Error al cargar materias", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void guardarActividad() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String fecha = etFechaEntrega.getText().toString().trim();
        int idMateria = idsMaterias.get(spinnerMaterias.getSelectedItemPosition());

        if (titulo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "http://10.0.2.2/centros/insert_actividad.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(this, "Actividad registrada", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error al registrar actividad", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_materia", String.valueOf(idMateria));
                params.put("titulo", titulo);
                params.put("descripcion", descripcion);
                params.put("tipo", tipo);
                params.put("fecha_entrega", fecha);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}

