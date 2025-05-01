package com.tuapp.pruebados;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AvisosActivity extends AppCompatActivity {

    EditText etTitulo, etContenido, etGrado, etSeccion;
    Button btnPublicar;
    int idMaestro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

        etTitulo = findViewById(R.id.etTituloAviso);
        etContenido = findViewById(R.id.etContenidoAviso);
        etGrado = findViewById(R.id.etGrado);
        etSeccion = findViewById(R.id.etSeccion);
        btnPublicar = findViewById(R.id.btnPublicarAviso);

        idMaestro = getIntent().getIntExtra("id_maestro", -1);

        btnPublicar.setOnClickListener(v -> publicarAviso());
    }

    private void publicarAviso() {
        String titulo = etTitulo.getText().toString().trim();
        String contenido = etContenido.getText().toString().trim();
        String grado = etGrado.getText().toString().trim();
        String seccion = etSeccion.getText().toString().trim();

        if (titulo.isEmpty() || contenido.isEmpty() || grado.isEmpty() || seccion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String URL = "http://10.0.2.2/centros/insert_aviso.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(this, "Aviso publicado", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Error al publicar", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_maestro", String.valueOf(idMaestro));
                params.put("titulo", titulo);
                params.put("contenido", contenido);
                params.put("grado", grado);
                params.put("seccion", seccion);
                params.put("fecha_publicacion", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}

