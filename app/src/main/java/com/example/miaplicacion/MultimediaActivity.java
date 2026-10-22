package com.example.miaplicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Actividad 2.4: Elementos Multimedia (Audio, Video, Animaciones y Permisos)
 * 
 * Curso: Programación Android (IEI-093)
 * Profesor: Oscar Monárdez Saguas
 * Estudiante: Francisco Urquieta
 * Institución: Instituto Profesional Santo Tomás
 * 
 * Esta clase orquesta los cuatro pilares técnicos solicitados en la pauta y la infografía:
 * 1. Incorporación y manipulación dinámica de imágenes (ImageView en res/drawable).
 * 2. Reproducción de audio con control de ciclo de vida (MediaPlayer en res/raw y liberación en onDestroy).
 * 3. Reproducción de video local mediante URI con MediaController (VideoView en res/raw).
 * 4. Animaciones visuales declaradas en XML (res/anim: fade_in, bounce, rotate_scale).
 * 5. Gestión estricta de permisos en tiempo de ejecución para Android 11+ (API 30).
 */
public class MultimediaActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2001;

    // =========================================================================
    // Vistas y Componentes Multimedia
    // =========================================================================

    // Pilar 1: Imágenes
    private ImageView myImageView;
    private TextView tvImageInfo;
    private Button btnToggleImage;
    private Button btnResetImage;
    private boolean isDefaultImage = true;

    // Pilar 2: Audio (MediaPlayer)
    private MediaPlayer mediaPlayer;
    private Button playAudioButton;
    private Button btnPauseAudio;
    private Button btnStopAudio;
    private TextView tvAudioStatus;

    // Pilar 3: Video (VideoView)
    private VideoView myVideoView;
    private Button playVideoButton;
    private Button btnPauseVideo;
    private Button btnRestartVideo;
    private TextView tvVideoOverlay;
    private MediaController mediaController;
    private Uri localVideoUri;

    // Pilar 4: Animaciones XML
    private Button animateButton;
    private Button btnAnimBounce;
    private Button btnAnimRotateScale;

    // Pilar 5: Permisos
    private TextView tvPermissionStatus;
    private Button btnRequestPermissions;

    // Botón de navegación
    private ImageButton btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        initViews();
        setupImageSection();
        setupAudioSection();
        setupVideoSection();
        setupAnimationSection();
        setupPermissionSection();
        setupNavigation();
    }

    /**
     * Enlaza las referencias de vistas desde el layout XML.
     */
    private void initViews() {
        // Toolbar / Navegación
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // Permisos
        tvPermissionStatus = findViewById(R.id.tvPermissionStatus);
        btnRequestPermissions = findViewById(R.id.btnRequestPermissions);

        // Imágenes
        myImageView = findViewById(R.id.myImageView);
        tvImageInfo = findViewById(R.id.tvImageInfo);
        btnToggleImage = findViewById(R.id.btnToggleImage);
        btnResetImage = findViewById(R.id.btnResetImage);

        // Animaciones
        animateButton = findViewById(R.id.animateButton);
        btnAnimBounce = findViewById(R.id.btnAnimBounce);
        btnAnimRotateScale = findViewById(R.id.btnAnimRotateScale);

        // Audio
        playAudioButton = findViewById(R.id.playAudioButton);
        btnPauseAudio = findViewById(R.id.btnPauseAudio);
        btnStopAudio = findViewById(R.id.btnStopAudio);
        tvAudioStatus = findViewById(R.id.tvAudioStatus);

        // Video
        myVideoView = findViewById(R.id.myVideoView);
        playVideoButton = findViewById(R.id.playVideoButton);
        btnPauseVideo = findViewById(R.id.btnPauseVideo);
        btnRestartVideo = findViewById(R.id.btnRestartVideo);
        tvVideoOverlay = findViewById(R.id.tvVideoOverlay);
    }

    // =========================================================================
    // PILAR 1: INCORPORACIÓN DE IMÁGENES (res/drawable)
    // =========================================================================
    private void setupImageSection() {
        // Carga inicial explícita mediante setImageResource conforme a la diapositiva 6
        myImageView.setImageResource(R.drawable.multimedia_hero);

        // Alternar dinámicamente entre imágenes almacenadas en res/drawable
        btnToggleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDefaultImage) {
                    myImageView.setImageResource(R.drawable.paisaje_ejemplo);
                    tvImageInfo.setText("Recurso activo: @drawable/paisaje_ejemplo");
                    isDefaultImage = false;
                } else {
                    myImageView.setImageResource(R.drawable.multimedia_hero);
                    tvImageInfo.setText("Recurso activo: @drawable/multimedia_hero");
                    isDefaultImage = true;
                }
            }
        });

        btnResetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myImageView.setImageResource(R.drawable.multimedia_hero);
                tvImageInfo.setText("Recurso activo: @drawable/multimedia_hero");
                isDefaultImage = true;
            }
        });
    }

    // =========================================================================
    // PILAR 2: REPRODUCCIÓN DE AUDIO (MediaPlayer en res/raw)
    // =========================================================================
    private void setupAudioSection() {
        // Botón oficial de la pauta: playAudioButton
        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        btnPauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseAudio();
            }
        });

        btnStopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });
    }

    /**
     * Inicia o reanuda la reproducción del archivo de audio res/raw/tu_audio.
     */
    private void playAudio() {
        try {
            if (mediaPlayer == null) {
                // Código oficial de la Pauta / Diapositiva 8:
                // MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tu_audio);
                mediaPlayer = MediaPlayer.create(MultimediaActivity.this, R.raw.tu_audio);
                if (mediaPlayer == null) {
                    Toast.makeText(this, "No se pudo inicializar el recurso de audio", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Listener para detectar cuándo termina la canción
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        tvAudioStatus.setText("Reproducción finalizada (Listo)");
                        tvAudioStatus.setTextColor(ContextCompat.getColor(MultimediaActivity.this, R.color.text_secondary));
                    }
                });
            }

            mediaPlayer.start();
            tvAudioStatus.setText("Reproduciendo audio... (MP3 Activo)");
            tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.primary));
            Toast.makeText(this, "Reproduciendo audio desde res/raw", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            tvAudioStatus.setText("Error en reproducción: " + e.getMessage());
            Toast.makeText(this, "Error de audio: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Pausa la reproducción de audio si está activa.
     */
    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            tvAudioStatus.setText("Audio en pausa");
            tvAudioStatus.setTextColor(0xFFD97706);
            Toast.makeText(this, "Audio pausado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Detiene y resetea el reproductor de audio.
     */
    private void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            tvAudioStatus.setText("Listo para reproducir (Detenido)");
            tvAudioStatus.setTextColor(0xFF2563EB);
            Toast.makeText(this, "Audio detenido y recurso liberado", Toast.LENGTH_SHORT).show();
        }
    }

    // =========================================================================
    // PILAR 3: REPRODUCCIÓN DE VIDEO (VideoView en res/raw)
    // =========================================================================
    private void setupVideoSection() {
        // Construcción oficial de URI según Diapositiva 9 de la pauta:
        // Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video)
        localVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video);

        // Configuración de MediaController para controles estándar en pantalla (SeekBar, Play, Pause)
        mediaController = new MediaController(this);
        mediaController.setAnchorView(myVideoView);
        myVideoView.setMediaController(mediaController);

        // Botón oficial de la pauta: playVideoButton
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });

        btnPauseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myVideoView.isPlaying()) {
                    myVideoView.pause();
                    tvVideoOverlay.setVisibility(View.VISIBLE);
                    tvVideoOverlay.setText("Video en Pausa");
                    Toast.makeText(MultimediaActivity.this, "Video pausado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRestartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVideoView.seekTo(0);
                myVideoView.start();
                tvVideoOverlay.setVisibility(View.GONE);
                Toast.makeText(MultimediaActivity.this, "Video reiniciado", Toast.LENGTH_SHORT).show();
            }
        });

        // Ocultar overlay al iniciar y restaurar al terminar
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                tvVideoOverlay.setVisibility(View.VISIBLE);
                tvVideoOverlay.setText("Video Finalizado - Presiona Play Video");
            }
        });
    }

    /**
     * Carga la URI y comienza la reproducción en el VideoView.
     */
    private void playVideo() {
        try {
            tvVideoOverlay.setVisibility(View.GONE);
            myVideoView.setVideoURI(localVideoUri);
            myVideoView.start();
            Toast.makeText(this, "Reproduciendo video desde res/raw", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            tvVideoOverlay.setVisibility(View.VISIBLE);
            tvVideoOverlay.setText("Error al cargar video: " + e.getMessage());
            Toast.makeText(this, "Error de video: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // =========================================================================
    // PILAR 4: ANIMACIONES XML (res/anim)
    // =========================================================================
    private void setupAnimationSection() {
        // Código oficial de la Pauta para fade_in:
        // Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        // imageView.startAnimation(fadeInAnimation);
        animateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(MultimediaActivity.this, R.anim.fade_in);
                myImageView.startAnimation(fadeInAnimation);
                Toast.makeText(MultimediaActivity.this, "Ejecutando fade_in.xml (Alpha 0.0 -> 1.0)", Toast.LENGTH_SHORT).show();
            }
        });

        // Animación de rebote según Diapositiva 11 (bounce.xml con bounce_interpolator)
        btnAnimBounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bounceAnimation = AnimationUtils.loadAnimation(MultimediaActivity.this, R.anim.bounce);
                myImageView.startAnimation(bounceAnimation);
                Toast.makeText(MultimediaActivity.this, "Ejecutando bounce.xml (Translate + Bounce Interpolator)", Toast.LENGTH_SHORT).show();
            }
        });

        // Animación combinada de Rotación y Escala (rotate_scale.xml)
        btnAnimRotateScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotateScaleAnimation = AnimationUtils.loadAnimation(MultimediaActivity.this, R.anim.rotate_scale);
                myImageView.startAnimation(rotateScaleAnimation);
                Toast.makeText(MultimediaActivity.this, "Ejecutando rotate_scale.xml (Rotate 360° + Scale)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =========================================================================
    // PILAR 5: GESTIÓN DE PERMISOS EN TIEMPO DE EJECUCIÓN (API 30)
    // =========================================================================
    private void setupPermissionSection() {
        checkCurrentPermissions();

        btnRequestPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRequiredPermissions();
            }
        });
    }

    /**
     * Evalúa el estado actual de los permisos en el sistema Android.
     */
    private void checkCurrentPermissions() {
        // Para Android 11 (API 30), evaluamos READ_EXTERNAL_STORAGE
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
            tvPermissionStatus.setText("Concedido (Acceso a almacenamiento y medios activo)");
            tvPermissionStatus.setTextColor(0xFF16A34A); // Verde
        } else {
            tvPermissionStatus.setText("No Concedido (Requiere aprobación del usuario)");
            tvPermissionStatus.setTextColor(0xFFDC2626); // Rojo
        }
    }

    /**
     * Solicita permisos en tiempo de ejecución utilizando ActivityCompat.requestPermissions
     * tal como se exige en las instrucciones de la Pauta Oficial.
     */
    private void requestRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Los permisos ya fueron otorgados previamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Solicitud en tiempo de ejecución
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_STORAGE_PERMISSION
        );
    }

    /**
     * Maneja la respuesta del usuario ante el diálogo de solicitud de permisos.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tvPermissionStatus.setText("Concedido (Permiso otorgado por el usuario)");
                tvPermissionStatus.setTextColor(0xFF16A34A);
                Toast.makeText(this, "Permiso de almacenamiento concedido correctamente", Toast.LENGTH_SHORT).show();
            } else {
                tvPermissionStatus.setText("Denegado (El usuario no otorgó el permiso)");
                tvPermissionStatus.setTextColor(0xFFDC2626);
                Toast.makeText(this, "Permiso denegado. Algunas funciones de medios externos podrían verse limitadas.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // =========================================================================
    // NAVEGACIÓN
    // =========================================================================
    private void setupNavigation() {
        if (btnBackToMain != null) {
            btnBackToMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    // =========================================================================
    // GESTIÓN DEL CICLO DE VIDA Y LIBERACIÓN DE RECURSOS (CRÍTICO)
    // =========================================================================
    @Override
    protected void onPause() {
        super.onPause();
        // Pausar audio y video cuando la actividad pasa a segundo plano
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (myVideoView != null && myVideoView.isPlaying()) {
            myVideoView.pause();
        }
    }

    /**
     * Regla crítica documentada en la Diapositiva 8 y la Infografía:
     * Liberar los recursos nativos del MediaPlayer en onDestroy() para evitar fugas de memoria (Memory Leaks).
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 1. Liberar MediaPlayer
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception ignored) {
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // 2. Detener VideoView
        if (myVideoView != null) {
            myVideoView.stopPlayback();
        }
    }
}
