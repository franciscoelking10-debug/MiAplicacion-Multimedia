package com.example.miaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRut, etNombre, etApellidoPaterno, etApellidoMaterno;
    private EditText etEdad, etTelefono, etDireccion, etCorreo;
    private RadioGroup rgGenero;
    private ProgressBar progressBarRegister;
    private TextView tvValidatingStatus;
    private Button btnValidar;
    private ImageButton btnBackRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        btnBackRegister = findViewById(R.id.btnBackRegister);
        etRut = findViewById(R.id.etRut);
        etNombre = findViewById(R.id.etNombre);
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno);
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno);
        etEdad = findViewById(R.id.etEdad);
        rgGenero = findViewById(R.id.rgGenero);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etCorreo = findViewById(R.id.etCorreo);

        progressBarRegister = findViewById(R.id.progressBarRegister);
        tvValidatingStatus = findViewById(R.id.tvValidatingStatus);
        btnValidar = findViewById(R.id.btnValidar);

        btnBackRegister.setOnClickListener(v -> finish());

        btnValidar.setOnClickListener(v -> validarYRegistrar());
    }

    private void validarYRegistrar() {
        String rut = etRut.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apPaterno = etApellidoPaterno.getText().toString().trim();
        String apMaterno = etApellidoMaterno.getText().toString().trim();
        String edad = etEdad.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();

        // Obtener género seleccionado
        String genero = "Masculino";
        int selectedId = rgGenero.getCheckedRadioButtonId();
        if (selectedId == R.id.rbFemenino) {
            genero = "Femenino";
        } else if (selectedId == R.id.rbOtro) {
            genero = "Otro";
        }

        // Validaciones de campos obligatorios
        if (TextUtils.isEmpty(rut)) {
            etRut.setError("El RUT es obligatorio");
            etRut.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(apPaterno)) {
            etApellidoPaterno.setError("El apellido paterno es obligatorio");
            etApellidoPaterno.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(apMaterno)) {
            etApellidoMaterno.setError("El apellido materno es obligatorio");
            etApellidoMaterno.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(edad)) {
            etEdad.setError("La edad es obligatoria");
            etEdad.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(telefono)) {
            etTelefono.setError("El teléfono es obligatorio");
            etTelefono.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(direccion)) {
            etDireccion.setError("La dirección es obligatoria");
            etDireccion.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(correo)) {
            etCorreo.setError("El correo es obligatorio");
            etCorreo.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Formato de correo no válido");
            etCorreo.requestFocus();
            return;
        }

        // Mostrar ProgressBar (pasa de GONE a VISIBLE según la pauta)
        progressBarRegister.setVisibility(View.VISIBLE);
        tvValidatingStatus.setVisibility(View.VISIBLE);
        btnValidar.setEnabled(false);

        String finalGenero = genero;

        // Simulación de procesamiento de validación
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressBarRegister.setVisibility(View.GONE);
            tvValidatingStatus.setVisibility(View.GONE);
            btnValidar.setEnabled(true);

            Toast.makeText(RegisterActivity.this, "¡Validación exitosa! Bienvenido al sistema.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            intent.putExtra("EXTRA_RUT", rut);
            intent.putExtra("EXTRA_NOMBRE", nombre);
            intent.putExtra("EXTRA_PATERNO", apPaterno);
            intent.putExtra("EXTRA_MATERNO", apMaterno);
            intent.putExtra("EXTRA_EDAD", edad);
            intent.putExtra("EXTRA_GENERO", finalGenero);
            intent.putExtra("EXTRA_TELEFONO", telefono);
            intent.putExtra("EXTRA_DIRECCION", direccion);
            intent.putExtra("EXTRA_CORREO", correo);
            startActivity(intent);
        }, 1200);
    }
}
