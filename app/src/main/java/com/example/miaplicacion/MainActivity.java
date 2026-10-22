package com.example.miaplicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/**
 * Actividad Principal - MiAplicacion
 * 
 * Actividad 2.4: Elementos Multimedia (Audio, Video, Animaciones y Permisos)
 * Curso: Programación Android (IEI-093)
 * Institución: Instituto Profesional Santo Tomás
 * 
 * Esta clase orquesta los cuatro pilares técnicos fundamentales exigidos por la pauta oficial:
 * 1. Incorporación y alternancia de imágenes (ImageView en res/drawable).
 * 2. Reproducción y control de ciclo de vida de audio (MediaPlayer en res/raw, con liberación en onDestroy).
 * 3. Reproducción de video local mediante URI canónica con MediaController (VideoView en res/raw).
 * 4. Animaciones visuales declaradas en XML (res/anim: fade_in, bounce, rotate_scale).
 * 5. Gestión de permisos en tiempo de ejecución acorde a Android 11+ (API 30).
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 2001;

    // =========================================================================
    // Vistas y Componentes Multimedia
    // =========================================================================

    // Pilar 0: Permisos en tiempo de ejecución
    private TextView tvPermissionStatus;
    private Button btnRequestPermissions;

    // Pilar 1: Imágenes
    private ImageView myImageView;
    private TextView tvImageInfo;
    private Button btnToggleImage;
    private Button btnResetImage;
    private int currentImageIndex = 0;
    private final int[] drawableImages = {
        R.drawable.santo_tomas,
        R.drawable.paisaje_ejemplo,
        R.drawable.fotografo_ejemplo
    };
    private final int[] drawableNames = {
        R.string.active_image_default,
        R.string.active_image_landscape,
        R.string.active_image_photographer
    };

    // Pilar 2: Audio (MediaPlayer con progreso interactivo)
    private MediaPlayer mediaPlayer;
    private Button playAudioButton;
    private Button btnPauseAudio;
    private Button btnStopAudio;
    private SeekBar seekBarAudio;
    private TextView tvAudioStatus;
    private TextView tvAudioCurrentTime;
    private TextView tvAudioTotalTime;
    private final Handler audioHandler = new Handler(Looper.getMainLooper());
    private Runnable audioProgressRunnable;
    private boolean isUserSeekingAudio = false;

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
    private TextView tvAnimStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupPermissionSection();
        setupImageSection();
        setupAudioSection();
        setupVideoSection();
        setupAnimationSection();
    }

    /**
     * Enlaza las referencias de vistas desde el layout XML activity_main.xml.
     */
    private void initViews() {
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
        tvAnimStatus = findViewById(R.id.tvAnimStatus);

        // Audio
        playAudioButton = findViewById(R.id.playAudioButton);
        btnPauseAudio = findViewById(R.id.btnPauseAudio);
        btnStopAudio = findViewById(R.id.btnStopAudio);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        tvAudioStatus = findViewById(R.id.tvAudioStatus);
        tvAudioCurrentTime = findViewById(R.id.tvAudioCurrentTime);
        tvAudioTotalTime = findViewById(R.id.tvAudioTotalTime);

        // Video
        myVideoView = findViewById(R.id.myVideoView);
        playVideoButton = findViewById(R.id.playVideoButton);
        btnPauseVideo = findViewById(R.id.btnPauseVideo);
        btnRestartVideo = findViewById(R.id.btnRestartVideo);
        tvVideoOverlay = findViewById(R.id.tvVideoOverlay);
    }

    // =========================================================================
    // PILAR 0: GESTIÓN DE PERMISOS EN TIEMPO DE EJECUCIÓN (API 30)
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
     * Evalúa el estado actual de los permisos multimedia y almacenamiento.
     */
    private void checkCurrentPermissions() {
        boolean hasPermission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33+) utiliza permisos granulares de medios
            int audioPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO);
            int videoPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO);
            int imagePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);
            hasPermission = (audioPerm == PackageManager.PERMISSION_GRANTED)
                    && (videoPerm == PackageManager.PERMISSION_GRANTED)
                    && (imagePerm == PackageManager.PERMISSION_GRANTED);
        } else {
            // Android 11 (API 30) / Android 12
            int storagePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            hasPermission = (storagePerm == PackageManager.PERMISSION_GRANTED);
        }

        if (hasPermission) {
            tvPermissionStatus.setText(getString(R.string.permission_status_granted));
            tvPermissionStatus.setTextColor(ContextCompat.getColor(this, R.color.state_active));
        } else {
            tvPermissionStatus.setText(getString(R.string.permission_status_denied));
            tvPermissionStatus.setTextColor(ContextCompat.getColor(this, R.color.state_warning));
        }
    }

    /**
     * Solicita permisos en tiempo de ejecución utilizando ActivityCompat.requestPermissions
     * conforme a las directrices de la Pauta Oficial.
     */
    private void requestRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permissions = {
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
            };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            boolean allGranted = grantResults.length > 0;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                tvPermissionStatus.setText(getString(R.string.permission_status_granted));
                tvPermissionStatus.setTextColor(ContextCompat.getColor(this, R.color.state_active));
                Toast.makeText(this, getString(R.string.toast_permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                tvPermissionStatus.setText(getString(R.string.permission_status_denied));
                tvPermissionStatus.setTextColor(ContextCompat.getColor(this, R.color.state_danger));
                Toast.makeText(this, getString(R.string.toast_permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    // =========================================================================
    // PILAR 1: INCORPORACIÓN Y MANIPULACIÓN DE IMÁGENES (res/drawable)
    // =========================================================================
    private void setupImageSection() {
        // Carga inicial explícita mediante setImageResource según la pauta
        myImageView.setImageResource(drawableImages[0]);
        tvImageInfo.setText(getString(drawableNames[0]));

        btnToggleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex = (currentImageIndex + 1) % drawableImages.length;
                myImageView.setImageResource(drawableImages[currentImageIndex]);
                tvImageInfo.setText(getString(drawableNames[currentImageIndex]));
                Toast.makeText(MainActivity.this, getString(drawableNames[currentImageIndex]), Toast.LENGTH_SHORT).show();
            }
        });

        btnResetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex = 0;
                myImageView.setImageResource(drawableImages[0]);
                tvImageInfo.setText(getString(drawableNames[0]));
                Toast.makeText(MainActivity.this, "Imagen restaurada: @drawable/multimedia_hero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =========================================================================
    // PILAR 2: REPRODUCCIÓN DE AUDIO (MediaPlayer en res/raw)
    // =========================================================================
    private void setupAudioSection() {
        // Configuración de SeekBar para avance interactivo
        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    tvAudioCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeekingAudio = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                isUserSeekingAudio = false;
            }
        });

        // Botón oficial de la Pauta: playAudioButton
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
                // Código canónico oficial de la Pauta IEI-093:
                // MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tu_audio);
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tu_audio);

                if (mediaPlayer == null) {
                    Toast.makeText(this, "No se pudo inicializar el recurso de audio res/raw/tu_audio", Toast.LENGTH_SHORT).show();
                    return;
                }

                int totalDuration = mediaPlayer.getDuration();
                seekBarAudio.setMax(totalDuration);
                tvAudioTotalTime.setText(formatTime(totalDuration));

                // Callback al finalizar la pista
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        tvAudioStatus.setText(getString(R.string.audio_status_finished));
                        tvAudioStatus.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_secondary));
                        stopAudioProgressUpdater();
                        seekBarAudio.setProgress(0);
                        tvAudioCurrentTime.setText(getString(R.string.audio_time_zero));
                    }
                });
            }

            mediaPlayer.start();
            tvAudioStatus.setText(getString(R.string.audio_status_playing));
            tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.primary));
            startAudioProgressUpdater();
            Toast.makeText(this, "Reproduciendo audio desde res/raw/tu_audio.mp3", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            tvAudioStatus.setText("Error: " + e.getMessage());
            Toast.makeText(this, "Error de audio: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Pausa la reproducción de audio si se encuentra activa.
     */
    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            stopAudioProgressUpdater();
            tvAudioStatus.setText(getString(R.string.audio_status_paused));
            tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.state_warning));
            Toast.makeText(this, "Audio pausado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Detiene la reproducción y libera inmediatamente los recursos de MediaPlayer.
     */
    private void stopAudio() {
        stopAudioProgressUpdater();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        seekBarAudio.setProgress(0);
        tvAudioCurrentTime.setText(getString(R.string.audio_time_zero));
        tvAudioStatus.setText(getString(R.string.audio_status_idle));
        tvAudioStatus.setTextColor(ContextCompat.getColor(this, R.color.state_info));
        Toast.makeText(this, "Audio detenido y recurso liberado (release)", Toast.LENGTH_SHORT).show();
    }

    /**
     * Actualiza periódicamente el SeekBar y el cronómetro de tiempo transcurrido.
     */
    private void startAudioProgressUpdater() {
        stopAudioProgressUpdater();
        audioProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    if (!isUserSeekingAudio) {
                        seekBarAudio.setProgress(currentPos);
                        tvAudioCurrentTime.setText(formatTime(currentPos));
                    }
                    audioHandler.postDelayed(this, 250);
                }
            }
        };
        audioHandler.post(audioProgressRunnable);
    }

    private void stopAudioProgressUpdater() {
        if (audioProgressRunnable != null) {
            audioHandler.removeCallbacks(audioProgressRunnable);
            audioProgressRunnable = null;
        }
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    // =========================================================================
    // PILAR 3: REPRODUCCIÓN DE VIDEO (VideoView en res/raw)
    // =========================================================================
    private void setupVideoSection() {
        // Construcción oficial de URI conforme a la diapositiva 9 y la pauta:
        // Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video)
        localVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video);

        // Anclaje de MediaController para controles nativos (barra de tiempo, pausa y play)
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
                    tvVideoOverlay.setText(getString(R.string.video_overlay_paused));
                    Toast.makeText(MainActivity.this, "Video en pausa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRestartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvVideoOverlay.setVisibility(View.GONE);
                myVideoView.seekTo(0);
                myVideoView.start();
                Toast.makeText(MainActivity.this, "Video reiniciado desde el principio", Toast.LENGTH_SHORT).show();
            }
        });

        // Ocultar overlay durante la reproducción y mostrarlo al finalizar
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                tvVideoOverlay.setVisibility(View.VISIBLE);
                tvVideoOverlay.setText(getString(R.string.video_overlay_finished));
            }
        });
    }

    /**
     * Carga la URI local en el VideoView y comienza su reproducción.
     */
    private void playVideo() {
        try {
            tvVideoOverlay.setVisibility(View.GONE);
            myVideoView.setVideoURI(localVideoUri);
            myVideoView.start();
            Toast.makeText(this, "Reproduciendo video desde res/raw/tu_video.mp4", Toast.LENGTH_SHORT).show();
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
        // Código oficial de la Pauta para fade_in (id: animateButton):
        // Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        // imageView.startAnimation(fadeInAnimation);
        animateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                myImageView.startAnimation(fadeInAnimation);
                tvAnimStatus.setText(getString(R.string.anim_status_fade_in));
                Toast.makeText(MainActivity.this, "Ejecutando fade_in.xml (Alpha 0.0 -> 1.0)", Toast.LENGTH_SHORT).show();
            }
        });

        // Animación de rebote según Diapositiva 11 (bounce.xml con interpolador)
        btnAnimBounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bounceAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                myImageView.startAnimation(bounceAnimation);
                tvAnimStatus.setText(getString(R.string.anim_status_bounce));
                Toast.makeText(MainActivity.this, "Ejecutando bounce.xml (Translate + Bounce Interpolator)", Toast.LENGTH_SHORT).show();
            }
        });

        // Animación combinada de Rotación y Escala (rotate_scale.xml)
        btnAnimRotateScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotateScaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_scale);
                myImageView.startAnimation(rotateScaleAnimation);
                tvAnimStatus.setText(getString(R.string.anim_status_rotate_scale));
                Toast.makeText(MainActivity.this, "Ejecutando rotate_scale.xml (Rotate 360° + Scale)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =========================================================================
    // CICLO DE VIDA Y LIBERACIÓN DE MEMORIA NATIVA
    // =========================================================================
    @Override
    protected void onPause() {
        super.onPause();
        // Pausar reproducción para no drenar recursos ni batería en segundo plano
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            stopAudioProgressUpdater();
            tvAudioStatus.setText(getString(R.string.audio_status_paused));
        }
        if (myVideoView != null && myVideoView.isPlaying()) {
            myVideoView.pause();
            tvVideoOverlay.setVisibility(View.VISIBLE);
            tvVideoOverlay.setText(getString(R.string.video_overlay_paused));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Criterio de Evaluación: Liberar obligatoriamente la memoria nativa
        stopAudioProgressUpdater();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (myVideoView != null) {
            myVideoView.stopPlayback();
        }
    }
}
