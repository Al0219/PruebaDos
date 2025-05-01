package com.tuapp.pruebados;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerarReporteActivity extends AppCompatActivity {

    Button btnGenerar;
    String nombreAlumno = "Juan Pérez";
    String[][] notasEjemplo = {
            {"Matemática", "Examen", "Unidad 1", "85"},
            {"Lenguaje", "Tarea", "Unidad 1", "90"},
            {"Ciencias", "Participación", "Unidad 1", "100"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_reporte);

        btnGenerar = findViewById(R.id.btnGenerar);

        btnGenerar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                crearPDF();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void crearPDF() {
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        paint.setTextSize(16);
        canvas.drawText("Reporte de Notas", 220, 50, paint);

        paint.setTextSize(12);
        canvas.drawText("Alumno: " + nombreAlumno, 50, 100, paint);

        int y = 140;
        for (String[] fila : notasEjemplo) {
            String linea = "Materia: " + fila[0] + " | Tipo: " + fila[1] + " | " + fila[2] + " | Nota: " + fila[3];
            canvas.drawText(linea, 50, y, paint);
            y += 25;
        }

        pdf.finishPage(page);

        String directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File archivo = new File(directorio, "reporte_notas.pdf");

        try {
            pdf.writeTo(new FileOutputStream(archivo));
            Toast.makeText(this, "PDF generado en: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF", Toast.LENGTH_SHORT).show();
        }

        pdf.close();
    }
}

