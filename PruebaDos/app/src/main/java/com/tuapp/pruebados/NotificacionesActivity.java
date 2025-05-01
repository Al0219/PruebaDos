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

public class NotificacionesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> notificacionesList = new ArrayList<>();
    String idPadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        listView = findViewById(R.id.listViewNotificaciones);
        idPadre = getIntent().getStringExtra("id_padre");

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        String URL = "http://10.0.2.2/centros/get_notificaciones.php?idDestino=" + idPadre;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject noti = response.getJSONObject(i);
                            String linea = "📌 " + noti.getString("titulo") +
                                    "\n" + noti.getString("mensaje") +
                                    "\n📅 " + noti.getString("fechaEnvio");
                            notificacionesList.add(linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificacionesList));
                },
                error -> Toast.makeText(this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

