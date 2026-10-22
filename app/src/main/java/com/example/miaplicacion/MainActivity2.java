package com.example.miaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    // Widgets declarados
    private CalendarView calendarView;
    private TextView tvSelectedDate;
    private ProgressBar progressBarHorizontal;
    private TextView tvProgressValue;
    private Button btnIncrementProgress, btnResetProgress;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private SeekBar seekBarContinuous, seekBarDiscrete;
    private TextView tvSeekBarContinuous, tvSeekBarDiscrete;
    private ImageButton btnBackMain2;
    private Button btnGoRegister, btnGoCourses;

    private int currentProgress = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Referencias de UI
        btnBackMain2 = findViewById(R.id.btnBackMain2);
        calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);

        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        tvProgressValue = findViewById(R.id.tvProgressValue);
        btnIncrementProgress = findViewById(R.id.btnIncrementProgress);
        btnResetProgress = findViewById(R.id.btnResetProgress);

        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tvRatingValue);

        seekBarContinuous = findViewById(R.id.seekBarContinuous);
        tvSeekBarContinuous = findViewById(R.id.tvSeekBarContinuous);
        seekBarDiscrete = findViewById(R.id.seekBarDiscrete);
        tvSeekBarDiscrete = findViewById(R.id.tvSeekBarDiscrete);

        btnGoRegister = findViewById(R.id.btnGoRegister);
        btnGoCourses = findViewById(R.id.btnGoCourses);

        btnBackMain2.setOnClickListener(v -> finish());

        // 1. Interacción con CalendarView (Capturar cambio de fecha)
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            tvSelectedDate.setText(String.format("Fecha seleccionada: %s", fecha));
            Toast.makeText(MainActivity2.this, "Fecha: " + fecha, Toast.LENGTH_SHORT).show();
        });

        // 2. Interacción con ProgressBar
        btnIncrementProgress.setOnClickListener(v -> {
            currentProgress = Math.min(100, currentProgress + 15);
            progressBarHorizontal.setProgress(currentProgress);
            tvProgressValue.setText(String.format(Locale.getDefault(), "Barra de Progreso: %d%%", currentProgress));
            if (currentProgress == 100) {
                Toast.makeText(MainActivity2.this, "¡Progreso completado al 100%!", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetProgress.setOnClickListener(v -> {
            currentProgress = 0;
            progressBarHorizontal.setProgress(0);
            tvProgressValue.setText("Barra de Progreso: 0%");
            Toast.makeText(MainActivity2.this, "Progreso reiniciado a 0%", Toast.LENGTH_SHORT).show();
        });

        // 3. Interacción con RatingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            tvRatingValue.setText(String.format(Locale.getDefault(), "RatingBar (Calificación: %.1f / 5.0):", rating));
            if (fromUser) {
                Toast.makeText(MainActivity2.this, "Calificación asignada: " + rating + " estrellas", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Interacción con SeekBar Continua
        seekBarContinuous.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarContinuous.setText(String.format(Locale.getDefault(), "Barra de segmento continua: %d%%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 5. Interacción con SeekBar Discreta
        seekBarDiscrete.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekBarDiscrete.setText(String.format(Locale.getDefault(), "Barra de segmento discreta: Nivel %d de 5", progress));
                if (fromUser) {
                    Toast.makeText(MainActivity2.this, "Nivel seleccionado: " + progress, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 6. Navegación a Desafíos
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, RegisterActivity.class)));
        btnGoCourses.setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, CourseListActivity.class)));
    }
}