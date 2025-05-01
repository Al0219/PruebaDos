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

public class NotasAlumnoActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> notasList = new ArrayList<>();
    int idAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_alumno);

        listView = findViewById(R.id.listViewNotas);
        idAlumno = getIntent().getIntExtra("id_alumno", -1);

        cargarNotas();
    }

    private void cargarNotas() {
        String URL = "http://10.0.2.2/centros/get_notas_alumno.php?id_alumno=" + idAlumno;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject nota = response.getJSONObject(i);
                            String linea = "Materia: " + nota.getString("materia") +
                                    "\nTipo: " + nota.getString("tipo") +
                                    "\nUnidad: " + nota.getInt("unidad") +
                                    "\nNota: " + nota.getDouble("valor") +
                                    "\nFecha: " + nota.getString("fecha");
                            notasList.add(linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notasList));
                },
                error -> Toast.makeText(this, "Error al cargar notas", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

