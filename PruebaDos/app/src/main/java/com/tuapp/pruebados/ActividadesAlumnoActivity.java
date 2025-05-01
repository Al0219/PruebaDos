package com.tuapp.pruebados;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActividadesAlumnoActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> actividadesList = new ArrayList<>();
    int idAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_alumno);

        listView = findViewById(R.id.listViewActividades);
        idAlumno = getIntent().getIntExtra("id_alumno", -1);

        cargarActividades();
    }

    private void cargarActividades() {
        String URL = "http://10.0.2.2/centros/get_actividades_alumno.php?id_alumno=" + idAlumno;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject act = response.getJSONObject(i);
                            String linea = "Materia: " + act.getString("materia") +
                                    "\nTipo: " + act.getString("tipo") +
                                    "\nTítulo: " + act.getString("titulo") +
                                    "\nDescripción: " + act.getString("descripcion") +
                                    "\nEntrega: " + act.getString("fecha_entrega");
                            actividadesList.add(linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, actividadesList));
                },
                error -> Toast.makeText(this, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

