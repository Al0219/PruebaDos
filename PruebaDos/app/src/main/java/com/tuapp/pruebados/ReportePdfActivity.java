package com.tuapp.pruebados;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ReportePdfActivity extends AppCompatActivity {

    EditText etIdAlumno;
    Button btnGenerar;
    String URL_DATOS = "http://10.0.2.2/centros/get_datos_reporte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_pdf);

        etIdAlumno = findViewById(R.id.etIdAlumno);
        btnGenerar = findViewById(R.id.btnGenerar);

        btnGenerar.setOnClickListener(v -> {
            String id = etIdAlumno.getText().toString();
            if (!id.isEmpty()) {
                obtenerDatosYCrearPdf(id);
            } else {
                Toast.makeText(this, "Ingresa un ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDatosYCrearPdf(String idAlumno) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_DATOS,
                response -> {
                    try {
                        JSONObject data = new JSONObject(response);
                        crearPDF(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id_alumno", idAlumno);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void crearPDF(JSONObject datos) throws Exception {
        String correoPadre = datos.getString("correo_padre");
        File carpeta = new File(Environment.getExternalStorageDirectory(), "ReportesEscolares");
        if (!carpeta.exists()) carpeta.mkdirs();

        File archivo = new File(carpeta, "reporte_alumno.pdf");
        FileOutputStream fos = new FileOutputStream(archivo);

        Document doc = new Document();
        PdfWriter.getInstance(doc, fos);
        doc.open();

        // Encabezado
        doc.add(new Paragraph("REPORTE ESCOLAR", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
        doc.add(new Paragraph(" "));

        // Info del alumno
        JSONObject alumno = datos.getJSONObject("alumno");
        doc.add(new Paragraph("Nombre: " + alumno.getString("nombre")));
        doc.add(new Paragraph("Grado: " + alumno.getString("grado") + " - Sección: " + alumno.getString("seccion")));
        doc.add(new Paragraph(" "));

        // Notas
        doc.add(new Paragraph("📘 Notas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        JSONArray notas = datos.getJSONArray("notas");
        for (int i = 0; i < notas.length(); i++) {
            JSONObject n = notas.getJSONObject(i);
            doc.add(new Paragraph(n.getString("materia") + " - " + n.getString("tipo") +
                    " (Unidad " + n.getInt("unidad") + "): " + n.getDouble("valor")));
        }
        doc.add(new Paragraph(" "));

        // Asistencia
        doc.add(new Paragraph("📆 Asistencia:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        JSONObject asistencia = datos.getJSONObject("asistencia");
        doc.add(new Paragraph("Total días: " + asistencia.getInt("total")));
        doc.add(new Paragraph("Presentes: " + asistencia.getInt("presentes")));
        doc.add(new Paragraph("Ausentes: " + asistencia.getInt("ausentes")));
        doc.add(new Paragraph(" "));

        // Actividades
        doc.add(new Paragraph("📌 Actividades:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        JSONArray actividades = datos.getJSONArray("actividades");
        for (int i = 0; i < actividades.length(); i++) {
            JSONObject a = actividades.getJSONObject(i);
            doc.add(new Paragraph(a.getString("tipo").toUpperCase() + ": " +
                    a.getString("titulo") + " - " + a.getString("fecha_entrega")));
        }

        Toast.makeText(this, "PDF creado: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
        // Compartir por correo
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", archivo);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Escolar");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Adjunto el reporte escolar del alumno.");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{correoPadre}); // ← Aquí se usa el correo
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Compartir PDF usando..."));



        doc.close();
    }
}

