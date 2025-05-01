package com.tuapp.pruebados;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsistenciaAdapter extends ArrayAdapter<Alumno> {

    Context context;
    List<Alumno> alumnos;
    String fecha;
    int idMateria;

    public AsistenciaAdapter(Context context, List<Alumno> alumnos, String fecha, int idMateria) {
        super(context, 0, alumnos);
        this.context = context;
        this.alumnos = alumnos;
        this.fecha = fecha;
        this.idMateria = idMateria;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alumno alumno = alumnos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_asistencia, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombreAlumno);
        CheckBox checkPresente = convertView.findViewById(R.id.checkPresente);

        tvNombre.setText(alumno.nombre);

        checkPresente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            guardarAsistencia(alumno.id, isChecked);
        });

        return convertView;
    }

    private void guardarAsistencia(int idAlumno, boolean presente) {
        String URL = "http://10.0.2.2/centros/insert_asistencia.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {},
                error -> Toast.makeText(context, "Error al guardar asistencia", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_alumno", String.valueOf(idAlumno));
                params.put("fecha", fecha);
                params.put("presente", String.valueOf(presente ? 1 : 0));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }
}
