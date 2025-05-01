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

public class AvisosAlumnoActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> avisosList = new ArrayList<>();
    String grado, seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos_alumno);

        listView = findViewById(R.id.listViewAvisos);
        grado = getIntent().getStringExtra("grado");
        seccion = getIntent().getStringExtra("seccion");

        cargarAvisos();
    }

    private void cargarAvisos() {
        String URL = "http://10.0.2.2/centros/get_avisos_alumno.php?grado=" + grado + "&seccion=" + seccion;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject aviso = response.getJSONObject(i);
                            String linea = "Título: " + aviso.getString("titulo") +
                                    "\nContenido: " + aviso.getString("contenido") +
                                    "\nFecha: " + aviso.getString("fecha_publicacion");
                            avisosList.add(linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, avisosList));
                },
                error -> Toast.makeText(this, "Error al cargar avisos", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
