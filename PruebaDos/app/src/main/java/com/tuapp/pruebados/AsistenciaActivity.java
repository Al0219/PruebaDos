package com.tuapp.pruebados;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AsistenciaActivity extends AppCompatActivity {

    Spinner spinnerMaterias;
    ListView listaAlumnos;
    ListView listaAsistencia;
    TextView tvFecha;
    ArrayList<String> listaMaterias = new ArrayList<>();
    ArrayList<Alumno> alumnos = new ArrayList<>();
    int idMaestro;
    String fechaHoy;
    int idMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

        spinnerMaterias = findViewById(R.id.spinnerMaterias);
        listaAsistencia = findViewById(R.id.listaAsistencia);
        tvFecha = findViewById(R.id.tvFecha);

        idMaestro = getIntent().getIntExtra("id_maestro", -1);
        fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvFecha.setText(fechaHoy);

        cargarMaterias();
    }

    private void cargarMaterias() {
        String URL = "http://10.0.2.2/centros/get_materias_por_maestro.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        listaMaterias.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String materia = obj.getString("nombre") + " - " + obj.getString("grado") + " " + obj.getString("seccion");
                            listaMaterias.add(materia);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaMaterias);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMaterias.setAdapter(adapter);

                        spinnerMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                // Aquí puedes cargar los alumnos según grado y sección
                                // usando un nuevo request
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al cargar materias", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_maestro", String.valueOf(idMaestro));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

        // Al final, asignar el adapter de AsistenciaAdapter
    }
    protected void onItemSelected(){
        String grado="";  // obtén desde el JSON de la materia seleccionada
        String seccion = "";

        String URL_ALUMNOS = "http://10.0.2.2/centros/get_alumnos_por_grado.php";
        StringRequest req = new StringRequest(Request.Method.POST, URL_ALUMNOS,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        alumnos.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            int id = obj.getInt("id");
                            String nombre = obj.getString("nombre");
                            alumnos.add(new Alumno(id, nombre));
                        }

                        AsistenciaAdapter adapter = new AsistenciaAdapter(this, alumnos, fechaHoy, idMateria);
                        listaAlumnos.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al cargar alumnos", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("grado", grado);
                params.put("seccion", seccion);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(req);

    }
}

