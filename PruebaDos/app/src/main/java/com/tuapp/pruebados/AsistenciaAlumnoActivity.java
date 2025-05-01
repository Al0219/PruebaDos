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

public class AsistenciaAlumnoActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> asistenciaList = new ArrayList<>();
    int idAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_alumno);

        listView = findViewById(R.id.listViewAsistencia);
        idAlumno = getIntent().getIntExtra("id_alumno", -1);

        cargarAsistencia();
    }

    private void cargarAsistencia() {
        String URL = "http://10.0.2.2/centros/get_asistencia_alumno.php?id_alumno=" + idAlumno;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject registro = response.getJSONObject(i);
                            String fecha = registro.getString("fecha");
                            boolean presente = registro.getInt("presente") == 1;

                            String linea = fecha + " - " + (presente ? "Presente ✅" : "Ausente ❌");
                            asistenciaList.add(linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, asistenciaList));
                },
                error -> Toast.makeText(this, "Error al cargar asistencia", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

