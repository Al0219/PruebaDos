package com.tuapp.pruebados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MensajesMaestroActivity extends AppCompatActivity {

    ListView listMensajes;
    ArrayList<String> listaMensajes = new ArrayList<>();
    ArrayList<JSONObject> jsonMensajes = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String idMaestro = "3"; // Obtener desde la sesión si se puede

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes_maestro);

        listMensajes = findViewById(R.id.listMensajesMaestro);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaMensajes);
        listMensajes.setAdapter(adapter);

        obtenerMensajes();

        listMensajes.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject mensaje = jsonMensajes.get(position);
                String idPadre = mensaje.getString("emisor_id");

                Intent intent = new Intent(this, ResponderMensajeActivity.class);
                intent.putExtra("receptorId", idPadre); // Receptor será el padre
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void obtenerMensajes() {
        String URL = "http://10.0.2.2/centros/get_mensajes_recibidos.php?idMaestro=" + idMaestro;

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mensaje = jsonArray.getJSONObject(i);
                            jsonMensajes.add(mensaje);
                            String texto = "De: " + mensaje.getString("nombre_emisor") + "\n" +
                                    "Mensaje: " + mensaje.getString("mensaje") + "\n" +
                                    "Fecha: " + mensaje.getString("fechaEnvio");
                            listaMensajes.add(texto);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al obtener mensajes", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

