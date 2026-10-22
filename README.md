# Actividad 2.4: Elementos Multimedia (Audio, Video, Animaciones y Permisos)

**Institución:** Instituto Profesional Santo Tomás  
**Carrera:** Ingeniería / Técnico en Informática  
**Asignatura:** Programación Android (IEI-093)  
**Profesor:** Oscar Monárdez Saguas  
**Estudiante:** Francisco Urquieta  
**Lenguaje:** Java  
**SDK Mínimo:** API 24 (Android 7.0 Nougat)  
**SDK Compilación / Destino:** API 34 (Android 14) / Destino API 30 (Android 11)  
**Herramienta de Construcción:** Gradle 8.6 + Android Gradle Plugin 8.3+  

---

## 1. Resumen Ejecutivo del Proyecto

El presente proyecto corresponde a la entrega formal exclusiva de la **Actividad 2.4: Elementos Multimedia**. La aplicación ha sido reestructurada para centrarse únicamente en los requerimientos de esta actividad, eliminando tareas y actividades anteriores acumuladas para ofrecer una arquitectura limpia, modular, de alto rendimiento y con una interfaz de usuario (*UI/UX*) moderna basada en **Material Design 3**.

La solución integra de manera armónica los cuatro pilares técnicos fundamentales estipulados en la **Pauta Oficial de Actividad** y las diapositivas de cátedra:
1. **Pilar 1: Incorporación y manipulación dinámica de Imágenes** (`ImageView` con recursos en `res/drawable`).
2. **Pilar 2: Reproducción y control de ciclo de vida de Audio** (`MediaPlayer` con archivo en `res/raw/tu_audio.mp3` y liberación estricta en `onDestroy()`).
3. **Pilar 3: Reproducción de Video con MediaController** (`VideoView` con URI canónica a `res/raw/tu_video.mp4`).
4. **Pilar 4: Implementación de Animaciones XML** (`fade_in.xml`, `bounce.xml` y `rotate_scale.xml` en `res/anim/`).
5. **Pilar 0: Gestión de Permisos en Tiempo de Ejecución** (`READ_EXTERNAL_STORAGE` y medios para Android 11+ / API 30).

---

## 2. Estructura y Arquitectura del Sistema

El proyecto cuenta con una estructura limpia y optimizada:

```
MiAplicacion/
├── app/
│   ├── build.gradle                                    # Configuración SDK, dependencias Material Components y compilación
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml                     # Declaración única de MainActivity y permisos de almacenamiento/red
│           ├── java/com/example/miaplicacion/
│           │   └── MainActivity.java                   # Orquestador principal de Multimedia (Imágenes, Audio, Video, Animaciones y Permisos)
│           └── res/
│               ├── anim/
│               │   ├── fade_in.xml                     # Animación de transparencia gradual (Alpha 0.0 -> 1.0)
│               │   ├── bounce.xml                      # Animación de desplazamiento con interpolador de rebote
│               │   └── rotate_scale.xml                # Animación combinada de rotación (360°) y escalado (0.7 -> 1.0)
│               ├── drawable/
│               │   ├── multimedia_hero.png             # Imagen principal de demostración multimedia
│               │   ├── paisaje_ejemplo.png             # Imagen secundaria de paisaje
│               │   ├── fotografo_ejemplo.png           # Imagen terciaria fotográfica
│               │   ├── bg_header_gradient.xml          # Gradiente curvo institucional Santo Tomás (#006847)
│               │   ├── bg_audio_disc.xml               # Estilo disco circular para reproductor de audio
│               │   ├── bg_badge_rounded.xml            # Insignias de estado dinámicas
│               │   ├── bg_image_frame.xml              # Marco visual para tarjetas multimedia
│               │   ├── ic_animation_sparkle.xml        # Ícono vectorial de destello para animaciones
│               │   ├── ic_audio_track.xml              # Ícono vectorial de nota musical para audio
│               │   ├── ic_video_play.xml               # Ícono vectorial de claqueta para video
│               │   ├── ic_permission_shield.xml        # Ícono vectorial de escudo para permisos
│               │   ├── ic_play_arrow.xml               # Ícono play
│               │   ├── ic_pause.xml                    # Ícono pausa
│               │   ├── ic_stop.xml                     # Ícono detener
│               │   └── ic_check_circle.xml             # Ícono de verificación
│               ├── layout/
│               │   └── activity_main.xml               # Interfaz moderna con tarjetas Material Design, controles interactivos y diseño responsivo
│               ├── raw/
│               │   ├── tu_audio.mp3                    # Pista de audio para MediaPlayer (MPEG-3)
│               │   └── tu_video.mp4                    # Clip de video local para VideoView (H.264)
│               └── values/
│                   ├── colors.xml                      # Paleta cromática corporativa Santo Tomás (#006847) y estados semánticos
│                   ├── strings.xml                     # Recursos de texto internacionalizables para la Actividad 2.4
│                   └── themes.xml                      # Tema base Material Components DayNight sin ActionBar redundante
```

---

## 3. Implementación Detallada de Requerimientos

### A. Pilar 1: Incorporación de Imágenes (`ImageView` y `res/drawable`)
- Enlazado de vista mediante identificador oficial `R.id.myImageView`.
- Visualización dentro de un marco Material con bordes curvos y fondo neutro.
- Alternancia interactiva entre tres imágenes locales (`multimedia_hero`, `paisaje_ejemplo`, `fotografo_ejemplo`) con botón de restauración (*Reset*).

### B. Pilar 2: Reproducción de Audio (`MediaPlayer` y `res/raw`)
- Control mediante la clase nativa `android.media.MediaPlayer`.
- Botón oficial de la pauta: `playAudioButton` (`@+id/playAudioButton`).
- Controles complementarios de **Pausa** (`btnPauseAudio`) y **Detener/Liberar** (`btnStopAudio`).
- **Mejora de interfaz:** Integración de `SeekBar` interactivo con cronómetros en tiempo real (`00:00 / 00:23`) y desplazamiento arrastrable (*scrubbing*).
- **Manejo de Ciclo de Vida:** Sobrescritura de `onDestroy()` con `mediaPlayer.release(); mediaPlayer = null;` para evitar fugas de memoria nativa (*memory leaks*).

### C. Pilar 3: Reproducción de Video (`VideoView` y `MediaController`)
- Componente `VideoView` con identificador oficial `myVideoView` (`@+id/myVideoView`).
- Carga de URI canónica local: `Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video)`.
- Anclaje de `MediaController` nativo de Android para controles flotantes en pantalla (barra de reproducción, botón play/pause).
- Botón oficial de la pauta: `playVideoButton` (`@+id/playVideoButton`), botón de pausa y botón de reinicio.
- Overlay informativo translúcido que guía al usuario y se oculta automáticamente al iniciar la reproducción.

### D. Pilar 4: Animaciones XML (`res/anim`)
- Botón oficial de la pauta: `animateButton` (`@+id/animateButton`) que carga y ejecuta `fade_in.xml` con `AnimationUtils.loadAnimation()`.
- Animación `bounce.xml` con `bounce_interpolator` para efecto de rebote elástico.
- Animación combinada `rotate_scale.xml` con rotación en 360° y escalado de 70% a 100%.
- Etiqueta dinámica en pantalla que muestra los atributos técnicos de la animación disparada en tiempo real.

### E. Pilar 0: Permisos en Tiempo de Ejecución (API 30 / Android 11+)
- Declaración en `AndroidManifest.xml` de `READ_EXTERNAL_STORAGE` e `INTERNET`.
- Evaluación dinámica en tiempo de ejecución con `ContextCompat.checkSelfPermission()`.
- Solicitud interactiva con `ActivityCompat.requestPermissions()` y captura en `onRequestPermissionsResult()`.
- Indicador visual con código de colores (Verde = Concedido, Ámbar = Pendiente, Rojo = Denegado).

---

## 4. Respuestas a las Preguntas de Cierre de la Pauta Oficial

### Pregunta 1: ¿Qué nuevos conocimientos y habilidades has adquirido en la incorporación de imágenes, audio, video y animaciones?
* **Gestión de Recursos en `res/raw/` frente a `assets/`:** Comprendí que colocar archivos en `res/raw/` genera identificadores estáticos de compilación (`R.raw.tu_audio`, `R.raw.tu_video`), facilitando la validación de tipos por parte de `AAPT2` sin necesidad de instanciar un `AssetManager`.
* **Ciclo de Vida y Responsabilidad de Memoria Nativa con MediaPlayer:** Aprendí que los decodificadores de audio en Android consumen buffers en código nativo en C/C++; si la actividad es destruida o rota sin invocar `mediaPlayer.release()`, el recolector de basura de Java no libera esos buffers nativos, provocando fugas de memoria severas (*memory leaks*).
* **Desacoplamiento de Animaciones en XML (`res/anim/`):** Adquirí la destreza de declarar animaciones de propiedades en XML, lo que permite reutilizarlas en múltiples componentes, optimiza el rendimiento mediante aceleración por hardware e interpoladores (`bounce_interpolator`), y mantiene el código Java limpio y legible.
* **Construcción Canónica de URIs Multimedia:** Comprendí el esquema canónico `"android.resource://" + getPackageName() + "/" + resourceId` para alimentar `VideoView`, garantizando compatibilidad multiplataforma sin depender de rutas absolutas del sistema de archivos.

---

### Pregunta 2: ¿Cómo podrías utilizar los elementos multimedia para mejorar la experiencia del usuario en otras aplicaciones móviles?
* **Retroalimentación Auditiva Inmediata (Micro-interacciones):** En aplicaciones financieras o de comercio electrónico, sonidos sutiles de confirmación reducen la incertidumbre cognitiva del usuario tras realizar pagos o transacciones importantes.
* **Tutoriales y Onboarding Dinámico con Video:** El uso de clips cortos en `VideoView` permite explicar flujos complejos de configuración mucho mejor que extensos bloques de texto estático.
* **Jerarquía Visual y Guía Espacial con Animaciones:** Transiciones suaves (`fade_in`, `scale`) guían la atención del usuario hacia los cambios de estado en pantalla, evitando parpadeos bruscos al actualizar contenidos asincrónicos.
* **Accesibilidad:** La combinación armónica de audio explicativo, imágenes con descripciones claras (`contentDescription`) y video subtitulado permite crear aplicaciones inclusivas aptas para personas con capacidades reducidas.

---

### Pregunta 3: ¿Lograste integrar imágenes, audio, video y animaciones sin problemas? ¿Qué desafíos encontraste en el proceso y cómo los superaste?
* **Desafío 1 - Conflicto de Nombres Duplicados en `res/raw/`:** Al compilar inicialmente, existían extensiones `.mp3` y `.wav` con el mismo nombre base. Se resolvió estandarizando el archivo en un único formato MP3 optimizado, permitiendo que `AAPT2` genere el ID `R.raw.tu_audio` sin ambigüedades.
* **Desafío 2 - Modelo de Permisos en Android 11 (API 30):** En Android 11+ rige el almacenamiento aislado (*Scoped Storage*); no basta con la declaración en el manifiesto. Se implementó la verificación dinámica en tiempo de ejecución con `ActivityCompat.requestPermissions()` para garantizar cumplimiento de seguridad.
* **Desafío 3 - Persistencia de Estado de Audio y Video:** Se gestionó el ciclo de vida en `onPause()` y `onDestroy()` pausando las reproducciones y deteniendo las tareas en segundo plano del `Handler`, garantizando que la aplicación no consuma batería de manera oculta cuando pasa a segundo plano.

---

## 5. Matriz de Cumplimiento de Criterios de Evaluación

| Criterio de Evaluación (Pauta Oficial IEI-093) | Estado | Evidencia Concreta en el Código Fuente |
| :--- | :---: | :--- |
| **Delimita el problema, considerando los tipos de variables involucradas y requerimientos** | **Cumplido al 100%** | Tipado estricto de variables (`MediaPlayer`, `VideoView`, `Animation`, `Uri`, `SeekBar`, `Handler`) y permisos en `MainActivity.java`. |
| **Testea App en Java, para Android, según especificaciones acordadas** | **Cumplido al 100%** | Compilación exitosa con Gradle (`gradlew assembleDebug`), generando el APK de depuración sin advertencias ni errores de enlazado de recursos. |
| **Corrige App en Java, para Android, según especificaciones** | **Cumplido al 100%** | Limpieza total de actividades anteriores acumuladas, centralización en Actividad 2.4 y liberación de memoria nativa con `mediaPlayer.release()` en `onDestroy()`. |
| **Analiza la naturaleza de las variables y alternativas de solución afines** | **Cumplido al 100%** | Alternancia de imágenes locales, uso de interpoladores en animaciones XML y compatibilidad con Scoped Storage en Android 11 (API 30). |

---

## 6. Instrucciones para Ejecución en Android Studio

1. Iniciar **Android Studio**.
2. Abrir la carpeta del proyecto: `c:\Users\pconp\OneDrive\Desktop\tarea android jueves` (o la copia sincronizada en `01_Universidad_y_Estudios`).
3. Sincronizar Gradle con JDK 17 o superior (usar el JDK embebido de Android Studio `jbr`).
4. Seleccionar un emulador configurado con **API 30 (Android 11)** o conectar un dispositivo físico.
5. Presionar **Run 'app'** (`Shift + F10`).
6. La aplicación abrirá de forma directa e inmediata la **Actividad 2.4: Elementos Multimedia**, permitiendo interactuar con la galería de imágenes, disparar las animaciones, controlar el audio con el SeekBar y reproducir el video con su MediaController nativo.
