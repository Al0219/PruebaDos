package com.tuapp.pruebados;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText editMensaje;
    private Button btnEnviar;

    private String idUsuarioActual;
    private String idReceptor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        // Recibir IDs desde el intent
        idUsuarioActual = getIntent().getStringExtra("id_usuario");
        idReceptor = getIntent().getStringExtra("id_receptor");

        btnEnviar.setOnClickListener(v -> {
            String texto = editMensaje.getText().toString().trim();
            if (!texto.isEmpty()) {
                enviarMensaje(idUsuarioActual, idReceptor, texto);
                editMensaje.setText("");
            }
        });
    }

    private void enviarMensaje(String emisorId, String receptorId, String mensajeTexto) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String chatId = (Integer.parseInt(emisorId) < Integer.parseInt(receptorId)) ?
                emisorId + "_" + receptorId : receptorId + "_" + emisorId;

        DatabaseReference mensajesRef = database.getReference("mensajes").child(chatId);
        String mensajeId = mensajesRef.push().getKey();

        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("emisor_id", emisorId);
        mensaje.put("receptor_id", receptorId);
        mensaje.put("mensaje", mensajeTexto);
        mensaje.put("timestamp", ServerValue.TIMESTAMP);

        mensajesRef.child(mensajeId).setValue(mensaje);
    }
}
