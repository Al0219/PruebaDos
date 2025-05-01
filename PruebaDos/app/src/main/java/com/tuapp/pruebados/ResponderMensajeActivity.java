package com.tuapp.pruebados;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ResponderMensajeActivity extends AppCompatActivity {

    EditText etMensaje;
    Button btnEnviar;

    String emisorId = "2"; // ID del padre
    String receptorId;     // ID del maestro (lo recibimos por Intent)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_mensaje);

        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        receptorId = getIntent().getStringExtra("receptorId");

        btnEnviar.setOnClickListener(v -> enviarMensaje());
    }

    private void enviarMensaje() {
        String mensaje = etMensaje.getText().toString();
        if (mensaje.isEmpty()) {
            Toast.makeText(this, "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/centros/enviar_mensaje.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("emisor_id", emisorId);
                params.put("receptor_id", receptorId);
                params.put("mensaje", mensaje);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
