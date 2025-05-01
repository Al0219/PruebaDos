package com.tuapp.pruebados;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnviarNotificacionActivity extends AppCompatActivity {

    EditText etTitulo, etMensaje;
    ListView listViewPadres;
    Button btnEnviar;
    List<String> nombres = new ArrayList<>();
    List<String> ids = new ArrayList<>();

    String URL_GET_PADRES = "http://10.0.2.2/centros/get_padres.php";
    String URL_ENVIAR = "http://10.0.2.2/centros/enviar_notificacion.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_notificacion);

        etTitulo = findViewById(R.id.etTitulo);
        etMensaje = findViewById(R.id.etMensaje);
        listViewPadres = findViewById(R.id.listViewPadres);
        btnEnviar = findViewById(R.id.btnEnviar);

        cargarPadres();

        btnEnviar.setOnClickListener(v -> enviarNotificacion());
    }

    private void cargarPadres() {
        StringRequest request = new StringRequest(Request.Method.GET, URL_GET_PADRES,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            nombres.add(obj.getString("nombre"));
                            ids.add(obj.getString("id"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_multiple_choice, nombres);
                        listViewPadres.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al cargar padres", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void enviarNotificacion() {
        String titulo = etTitulo.getText().toString();
        String mensaje = etMensaje.getText().toString();
        SparseBooleanArray seleccionados = listViewPadres.getCheckedItemPositions();
        List<String> destinatarios = new ArrayList<>();

        for (int i = 0; i < seleccionados.size(); i++) {
            if (seleccionados.valueAt(i)) {
                destinatarios.add(ids.get(seleccionados.keyAt(i)));
            }
        }

        if (titulo.isEmpty() || mensaje.isEmpty() || destinatarios.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, URL_ENVIAR,
                response -> Toast.makeText(this, "Notificación enviada", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("titulo", titulo);
                map.put("mensaje", mensaje);
                map.put("destinatarios", TextUtils.join(",", destinatarios));
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
