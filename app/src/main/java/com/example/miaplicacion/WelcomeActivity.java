package com.example.miaplicacion;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvWelcomeGreeting, tvWelcomeRut, tvWelcomeEmail;
    private TextView tvWelcomePhone, tvWelcomeAddress, tvWelcomeAge, tvWelcomeGender;
    private Button btnBackFromWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvWelcomeGreeting = findViewById(R.id.tvWelcomeGreeting);
        tvWelcomeRut = findViewById(R.id.tvWelcomeRut);
        tvWelcomeEmail = findViewById(R.id.tvWelcomeEmail);
        tvWelcomePhone = findViewById(R.id.tvWelcomePhone);
        tvWelcomeAddress = findViewById(R.id.tvWelcomeAddress);
        tvWelcomeAge = findViewById(R.id.tvWelcomeAge);
        tvWelcomeGender = findViewById(R.id.tvWelcomeGender);
        btnBackFromWelcome = findViewById(R.id.btnBackFromWelcome);

        // Obtener datos transferidos desde RegisterActivity
        String rut = getIntent().getStringExtra("EXTRA_RUT");
        String nombre = getIntent().getStringExtra("EXTRA_NOMBRE");
        String apPaterno = getIntent().getStringExtra("EXTRA_PATERNO");
        String apMaterno = getIntent().getStringExtra("EXTRA_MATERNO");
        String edad = getIntent().getStringExtra("EXTRA_EDAD");
        String genero = getIntent().getStringExtra("EXTRA_GENERO");
        String telefono = getIntent().getStringExtra("EXTRA_TELEFONO");
        String direccion = getIntent().getStringExtra("EXTRA_DIRECCION");
        String correo = getIntent().getStringExtra("EXTRA_CORREO");

        tvWelcomeGreeting.setText("¡Hola, " + (nombre != null ? nombre : "") + " " + (apPaterno != null ? apPaterno : "") + "!");
        tvWelcomeRut.setText("RUT: " + (rut != null ? rut : "N/A"));
        tvWelcomeEmail.setText("Correo: " + (correo != null ? correo : "N/A"));
        tvWelcomePhone.setText("Teléfono: " + (telefono != null ? telefono : "N/A"));
        tvWelcomeAddress.setText("Dirección: " + (direccion != null ? direccion : "N/A"));
        tvWelcomeAge.setText("Edad: " + (edad != null ? edad : "N/A") + " años");
        tvWelcomeGender.setText("Género: " + (genero != null ? genero : "N/A"));

        btnBackFromWelcome.setOnClickListener(v -> finish());
    }
}
