package com.tuapp.pruebados;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlumnoNotaAdapter extends ArrayAdapter<Alumno> {

    Context context;
    List<Alumno> alumnos;
    int idMateria;

    public AlumnoNotaAdapter(Context context, List<Alumno> alumnos, int idMateria) {
        super(context, 0, alumnos);
        this.context = context;
        this.alumnos = alumnos;
        this.idMateria = idMateria;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alumno alumno = alumnos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nota_alumno, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombreAlumno);
        Spinner spinnerTipo = convertView.findViewById(R.id.spinnerTipoNota);
        EditText etUnidad = convertView.findViewById(R.id.etUnidad);
        EditText etValor = convertView.findViewById(R.id.etValorNota);
        Button btnGuardar = convertView.findViewById(R.id.btnGuardarNota);

        tvNombre.setText(alumno.nombre);

        String[] tipos = {"tarea", "examen", "participación"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tipos);
        spinnerTipo.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            String tipo = spinnerTipo.getSelectedItem().toString();
            String unidad = etUnidad.getText().toString();
            String valor = etValor.getText().toString();

            if (unidad.isEmpty() || valor.isEmpty()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            guardarNota(alumno.id, tipo, Integer.parseInt(unidad), Double.parseDouble(valor));
        });

        return convertView;
    }

    private void guardarNota(int idAlumno, String tipo, int unidad, double valor) {
        String URL = "http://10.0.2.2/centros/insert_nota.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(context, "Nota guardada", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_alumno", String.valueOf(idAlumno));
                params.put("id_materia", String.valueOf(idMateria));
                params.put("tipo", tipo);
                params.put("unidad", String.valueOf(unidad));
                params.put("valor", String.valueOf(valor));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}

