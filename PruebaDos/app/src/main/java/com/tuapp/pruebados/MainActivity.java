package com.tuapp.pruebados;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int TIEMPO_ESPERA = 1500; // 1.5 segundos

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Crear canal de notificaciones (para Firebase)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "canal_notificaciones", "Notificaciones Centro",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TOKEN", "Fallo al obtener token", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    Log.d("TOKEN", "Token actual: " + token);

                    // Recuperar id_usuario desde SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
                    String idUsuario = prefs.getString("id_usuario", null);

                    if (idUsuario != null) {
                        guardarTokenEnServidor(token, idUsuario);
                    } else {
                        Log.w("TOKEN", "ID de usuario no disponible");
                    }
                });




        // Configurar diseño edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Redireccionar a LoginActivity después de 1.5 segundos
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, TIEMPO_ESPERA);
    }
    private void guardarTokenEnServidor(String token, String idUsuario) {
        String URL = "http://10.0.2.2/centros/guardar_token.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> Log.d("TOKEN", "Token guardado"),
                error -> Log.e("TOKEN", "Error al guardar token", error)
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", idUsuario);
                params.put("token", token);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

}

