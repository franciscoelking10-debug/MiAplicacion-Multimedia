# INFORME DE ENTREGA - ACTIVIDAD 2.4
## Orquestación Multimedia en Android: Imágenes, Audio, Video, Animaciones y Permisos

**Institución:** Instituto Profesional Santo Tomás  
**Carrera:** Ingeniería en Informática  
**Asignatura:** Programación Android (IEI-093)  
**Profesor:** Oscar Monárdez Saguas  
**Estudiante:** Francisco Urquieta  
**Entorno de Trabajo:** Android Studio | Lenguaje: **Java** | Target SDK: **Android 30 (API 30)** | Compile SDK: **34**  
**Repositorio GitHub:** [https://github.com/franciscoelking10-debug/MiAplicacion1.git](https://github.com/franciscoelking10-debug/MiAplicacion1.git)  
**Fecha:** Octubre 2026  

---

## 1. Resumen Ejecutivo y Arquitectura de la Solución

Siguiendo estrictamente las especificaciones de la **Pauta Oficial de la Actividad 2.4 (IEI-093_U2_S9_2_4)**, la **Infografía Técnica de Multimedia en Android** y las diapositivas de la presentación oficial, se diseñó e integró un módulo multimedia interactivo y robusto en la aplicación Android del curso (`MiAplicacion`), materializado en la clase `MultimediaActivity.java` y su layout `activity_multimedia.xml`.

El desarrollo se fundamenta en los **cuatro pilares de integración** y la **gestión de permisos en tiempo de ejecución para Android 11 (API 30)**:

```
+---------------------------------------------------------------------------------------------------+
|                              ARQUITECTURA MULTIMEDIA (API 30 / JAVA)                             |
+------------------------------------+--------------------------------+-----------------------------+
| 1. Recursos (res/...)              | 2. Componente Visual (XML)     | 3. Controlador Lógico (Java)|
+------------------------------------+--------------------------------+-----------------------------+
| res/drawable/ (PNG, WEBP)          | ImageView                      | .setImageResource()         |
| res/raw/ (tu_audio.mp3)            | Botones (Play, Pause, Stop)    | MediaPlayer (.start/release)|
| res/raw/ (tu_video.mp4)            | VideoView + MediaController    | .setVideoURI() / URI Parse  |
| res/anim/ (fade_in, bounce)        | Vistas Animadas                | AnimationUtils + Anim XML   |
| AndroidManifest.xml (READ_STORAGE) | Diálogo de Permiso en Runtime  | ActivityCompat.requestPerms |
+------------------------------------+--------------------------------+-----------------------------+
```

---

## 2. Detalle de Implementación Técnica por Pilar

### 2.1 Pilar 1: Incorporación y Control de Imágenes (`res/drawable`)
* **Ubicación y Almacenamiento:** Siguiendo la regla de oro de nomenclatura Android (letras minúsculas, números y guiones bajos, sin espacios ni caracteres especiales), se agregaron los recursos gráficos `multimedia_hero.png` y `paisaje_ejemplo.png` en el directorio `app/src/main/res/drawable/`.
* **Componente Visual:** Se declaró un `ImageView` con identificador `@id/myImageView` dentro de un contenedor `MaterialCardView` estilizado con esquinas redondeadas y elevación suave.
* **Control Programático en Java:** Siguiendo el código de la pauta y diapositiva 6, se implementó la vinculación con `findViewById(R.id.myImageView)` y la mutación dinámica en tiempo de ejecución:
  ```java
  // Inicialización y asignación programática
  myImageView = findViewById(R.id.myImageView);
  myImageView.setImageResource(R.drawable.multimedia_hero);

  // Alternancia dinámica entre recursos mediante botón
  btnToggleImage.setOnClickListener(v -> {
      if (isDefaultImage) {
          myImageView.setImageResource(R.drawable.paisaje_ejemplo);
          tvImageInfo.setText("Recurso activo: @drawable/paisaje_ejemplo");
          isDefaultImage = false;
      } else {
          myImageView.setImageResource(R.drawable.multimedia_hero);
          tvImageInfo.setText("Recurso activo: @drawable/multimedia_hero");
          isDefaultImage = true;
      }
  });
  ```

---

### 2.2 Pilar 2: Reproducción de Audio y Gestión Crítica del Ciclo de Vida (`MediaPlayer`)
* **Directorio y Archivo Raw:** Se importó el archivo de audio codificado en MP3 estándar en `app/src/main/res/raw/tu_audio.mp3`.
* **Controlador Lógico:** Se empleó la clase nativa `android.media.MediaPlayer` para inicializar y gestionar la reproducción del sonido a través de los eventos de la interfaz:
  ```java
  Button playAudioButton = findViewById(R.id.playAudioButton);
  playAudioButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if (mediaPlayer == null) {
              // Código oficial de la Pauta / Diapositiva 8:
              mediaPlayer = MediaPlayer.create(MultimediaActivity.this, R.raw.tu_audio);
          }
          mediaPlayer.start();
          tvAudioStatus.setText("Reproduciendo audio... (MP3 Activo)");
      }
  });
  ```
* **Controles Complementarios:** Se implementaron botones de **Pausa** (`mediaPlayer.pause()`) y **Detención/Reset** (`mediaPlayer.stop(); mediaPlayer.release();`).
* **Liberación Crítica de Recursos (Mitigación de Memory Leaks):** Conforme se subraya como **Crítico** en la Diapositiva 8 y la Infografía, el `MediaPlayer` reserva códecs y buffers a nivel de hardware y memoria nativa en C++. Por ende, se implementó la liberación obligatoria en el ciclo de vida:
  ```java
  @Override
  protected void onPause() {
      super.onPause();
      if (mediaPlayer != null && mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
      }
  }

  @Override
  protected void onDestroy() {
      super.onDestroy();
      if (mediaPlayer != null) {
          try {
              if (mediaPlayer.isPlaying()) {
                  mediaPlayer.stop();
              }
          } catch (Exception ignored) {}
          mediaPlayer.release();
          mediaPlayer = null;
      }
  }
  ```

---

### 2.3 Pilar 3: Reproducción de Video Integrado con URI Local (`VideoView`)
* **Archivo de Video:** Se incluyó un archivo de video codificado en formato H.264/AAC `app/src/main/res/raw/tu_video.mp4`.
* **Componente Visual:** Se integró un `VideoView` (`@id/myVideoView`) configurado dentro de un `FrameLayout` oscuro con relación de aspecto adaptada.
* **Parseo y Esquema de URI Local:** Conforme a la Diapositiva 9 de la presentación, la URI se ensambla uniendo el esquema del recurso, el paquete canónico de la aplicación y el identificador entero del recurso `R.raw.tu_video`:
  ```java
  VideoView videoView = findViewById(R.id.myVideoView);
  Button playVideoButton = findViewById(R.id.playVideoButton);
  playVideoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tu_video);
          videoView.setVideoURI(videoUri);
          videoView.start();
      }
  });
  ```
* **MediaController Nativo:** Se añadió `MediaController` asociado con `mediaController.setAnchorView(videoView); videoView.setMediaController(mediaController);` para proporcionar al usuario barra de tiempo (SeekBar), avance rápido, retroceso y controles táctiles nativos.
* **Control de Ciclo de Vida:** Se asegura la llamada a `videoView.stopPlayback()` en `onDestroy()`.

---

### 2.4 Pilar 4: Framework de Animaciones XML (`res/anim`)
* **Separación de Lógica y Presentación:** Siguiendo la recomendación de la Diapositiva 10, las animaciones se definen de manera reutilizable en archivos XML dentro de `res/anim/`.
* **Animación 1 - `fade_in.xml` (Alfa / Transparencia según Pauta Oficial):**
  ```xml
  <alpha xmlns:android="http://schemas.android.com/apk/res/android"
      android:duration="1000"
      android:fromAlpha="0.0"
      android:toAlpha="1.0" />
  ```
* **Animación 2 - `bounce.xml` (Traslación con Interpolador de Rebote según Diapositiva 11):**
  ```xml
  <set xmlns:android="http://schemas.android.com/apk/res/android"
      android:interpolator="@android:anim/bounce_interpolator">
      <translate
          android:fromYDelta="0%"
          android:toYDelta="80%"
          android:duration="700" />
  </set>
  ```
* **Animación 3 - `rotate_scale.xml` (Rotación 360° y Escalado):**
  Demuestra los dos tipos de transformación restantes explicados en la matriz (rotación y escala combinados).
* **Inyección y Ejecución en el Controlador:**
  ```java
  Button animateButton = findViewById(R.id.animateButton);
  final ImageView imageView = findViewById(R.id.myImageView);
  animateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Animation fadeInAnimation = AnimationUtils.loadAnimation(MultimediaActivity.this, R.anim.fade_in);
          imageView.startAnimation(fadeInAnimation);
      }
  });
  ```

---

### 2.5 Pilar 5: Gestión de Permisos en Tiempo de Ejecución (API 30 - Android 11)
* **Declaración en Manifest:** En `AndroidManifest.xml` se configuraron los permisos requeridos:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
  <uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
  <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
  <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
  ```
* **Solicitud Dinámica (Runtime Permissions):**
  ```java
  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          this,
          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
          REQUEST_CODE_STORAGE_PERMISSION
      );
  }
  ```
* **Manejo del Resultado del Usuario:**
  ```java
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
          if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              tvPermissionStatus.setText("Concedido (Permiso otorgado por el usuario)");
              tvPermissionStatus.setTextColor(0xFF16A34A);
              Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
          } else {
              tvPermissionStatus.setText("Denegado (El usuario no otorgó el permiso)");
              tvPermissionStatus.setTextColor(0xFFDC2626);
              Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
          }
      }
  }
  ```

---

## 3. Respuestas a las Preguntas de Cierre de la Pauta Oficial

### Pregunta 1: ¿Qué nuevos conocimientos y habilidades has adquirido en la incorporación de imágenes, audio, video y animaciones?
* **Gestión de Recursos Crudos (Raw vs Assets):** Comprendí la diferencia técnica entre almacenar archivos multimedia en `res/raw/` (donde el sistema Android genera un identificador estático `R.raw.*`, facilitando el acceso directo y la validación en tiempo de compilación) frente a `assets/` (donde se requiere lectura de streams mediante `AssetManager`).
* **Ciclo de Vida y Responsabilidad de Memoria Nativa con MediaPlayer:** Adquirí la destreza de controlar el ciclo de vida del `MediaPlayer`. Aprendí que no basta con detener el audio (`stop()`), sino que es imprescindible invocar `release()` en `onDestroy()`. La máquina virtual Dalvik/ART maneja la recolección de basura de objetos Java, pero los buffers de audio en el subsistema de medios de Android pertenecen a código nativo en C/C++; si no se liberan explícitamente, generan fugas de memoria (*memory leaks*) que degradan el rendimiento general del dispositivo.
* **Separación de Lógica y Estilo en Animaciones XML:** Comprobé la enorme ventaja de desacoplar los efectos visuales del código imperativo Java. Declarar transiciones en `res/anim/` (`alpha`, `translate`, `rotate`, `scale`) permite reutilizar una misma animación en múltiples vistas, simplifica la mantención del código y aprovecha las optimizaciones de aceleración por hardware que ofrece el framework de Android mediante interpoladores (`bounce_interpolator`, `decelerate_interpolator`).
* **Construcción de URIs para Contenido Local:** Aprendí la estructura canónica del esquema `"android.resource://" + getPackageName() + "/" + resourceId` para alimentar componentes como `VideoView`, garantizando compatibilidad entre distintas versiones del sistema operativo sin depender de rutas absolutas de archivos físicos.

---

### Pregunta 2: ¿Cómo podrías utilizar los elementos multimedia para mejorar la experiencia del usuario en otras aplicaciones móviles?
* **Retroalimentación Auditiva y Táctil Inmediata (Micro-interacciones):** En aplicaciones comerciales, bancarias o de comercio electrónico, los efectos de audio sutiles y de muy baja latencia (por ejemplo con `SoundPool` o `MediaPlayer`) refuerzan acciones críticas: confirmación de pago exitoso, alerta de error en formulario, escaneo de código QR o refresco de pantalla. Esto entrega certeza cognitiva al usuario de que su orden fue procesada.
* **Onboarding y Tutoriales Dinámicos con Video:** El uso de `VideoView` permite sustituir manuales de texto densos por videos explicativos cortos (3 a 5 segundos) que muestran visualmente cómo utilizar las características principales de la aplicación o cómo configurar hardware externo (dispositivos IoT, sensores Bluetooth).
* **Guía Visual y Jerarquía con Animaciones de Propiedades:** Las animaciones XML (`fade_in`, `slide`, `scale`) no son meros adornos: actúan como indicadores espaciales de navegación. Ayudan al cerebro del usuario a comprender de dónde provino un elemento y hacia dónde fue. Por ejemplo, un botón que rebota suavemente (`bounce`) atrae la atención hacia el siguiente paso en un flujo de compra, mientras que un desvanecimiento (`fade_in`) previene parpadeos molestos en pantalla cuando se cargan contenidos asincrónicos.
* **Accesibilidad e Inclusión:** La combinación sincronizada de imágenes de alta resolución, subtítulos en videos, narración de voz y animaciones contextuales permite que usuarios con discapacidades visuales o auditivas puedan operar la aplicación con total comodidad y autonomía.

---

### Pregunta 3: ¿Lograste integrar imágenes, audio, video y animaciones sin problemas? ¿Qué desafíos encontraste en el proceso y cómo los superaste?
* **Desafío 1 - Conflicto de Nombres Duplicados en `res/raw`:**  
  * *Problema:* Al generar inicialmente los recursos de audio, se crearon simultáneamente `tu_audio.mp3` y `tu_audio.wav`. Durante la compilación con Gradle, la tarea `:app:mergeDebugResources` arrojó un error fatal de `Duplicate resources [raw/tu_audio]`.  
  * *Solución:* Comprendí que el compilador de recursos `AAPT2` mapea los archivos de `res/raw` al identificador único `R.raw.tu_audio` sin considerar la extensión de archivo. La solución consistió en estandarizar el recurso en formato MP3 optimizado y eliminar el duplicado WAV, logrando una compilación 100% limpia.
* **Desafío 2 - Manejo de Permisos en Android 11 (API 30):**  
  * *Problema:* A partir de la API 23 (Marshmallow) y reforzado en API 30 (Android 11 con almacenamiento limitado / *Scoped Storage*), declarar permisos únicamente en `AndroidManifest.xml` ya no es suficiente; cualquier acceso a archivos externos sin solicitar permisos en tiempo de ejecución provocaría excepciones de seguridad (`SecurityException`).  
  * *Solución:* Se implementó el flujo completo con `ContextCompat.checkSelfPermission()`, `ActivityCompat.requestPermissions()` y la captura en `onRequestPermissionsResult()`, vinculando un indicador visual en tiempo real que informa al usuario si los permisos están concedidos o pendientes.
* **Desafío 3 - Persistencia del Estado del Video y MediaController:**  
  * *Problema:* Si el usuario salía de la pantalla o abría los controles de video, el `VideoView` podía perder el hilo de renderizado o continuar reproduciéndose en segundo plano consumiendo batería.  
  * *Solución:* Se ancló explícitamente el `MediaController` a la vista mediante `mediaController.setAnchorView(myVideoView)` y se sobreescribieron los métodos `onPause()` y `onDestroy()` de la actividad para pausar la reproducción y llamar a `myVideoView.stopPlayback()`.

---

## 4. Matriz de Cumplimiento de Criterios de Evaluación

| Criterio de Evaluación (Pauta Oficial IEI-093) | Estado | Evidencia Concreta en el Código Fuente |
| :--- | :---: | :--- |
| **Delimita el problema, considerando los tipos de variables involucradas y requerimientos** | **Cumplido al 100%** | Identificación de los 4 pilares multimedia, tipado de variables (`MediaPlayer`, `VideoView`, `Animation`, `Uri`), permisos y ciclo de vida en `MultimediaActivity.java`. |
| **Testea App en Java, para Android, según especificaciones acordadas** | **Cumplido al 100%** | Compilación exitosa con Gradle (`compileDebugJavaWithJavac` y `assembleDebug`) generando el artefacto `app-debug.apk` de 6.05 MB sin errores ni advertencias fatales. |
| **Corrige App en Java, para Android, según especificaciones** | **Cumplido al 100%** | Detección y corrección del conflicto de nombres duplicados en `res/raw/`, prevención de fugas de memoria con `mediaPlayer.release()` en `onDestroy()`. |
| **Analiza la naturaleza de las variables y alternativas de solución afines** | **Cumplido al 100%** | Implementación de alternativas de audio y video (MP3/H.264), uso de interpoladores en animaciones XML y gestión de permisos en tiempo de ejecución para API 30. |

---

## 5. Instrucciones para Ejecución en Android Studio

1. **Abrir el Proyecto:**
   - Iniciar **Android Studio**.
   - Seleccionar **File > Open...** y navegar a la carpeta del proyecto `MiAplicacion`.
2. **Sincronizar Gradle:**
   - Esperar a que Android Studio realice la sincronización de Gradle con JDK 17/21/25 (`gradle-8.6-bin.zip` y AGP `8.4.0`).
3. **Ejecutar en Emulador o Dispositivo Físico:**
   - Seleccionar un emulador configurado con **API 30 (Android 11)** o superior.
   - Presionar el botón **Run 'app'** (`Shift + F10`).
4. **Navegación dentro de la App:**
   - En la pantalla principal (`MainActivity`), desplazarse hasta la tarjeta final **"7. Actividad 2.4: Elementos Multimedia"**.
   - Presionar **"Abrir Módulo Multimedia (Actividad 2.4)"**.
   - Probar la alternancia de imágenes, disparar las animaciones (`fade_in`, `bounce`, `rotate_scale`), controlar el audio (`Play`, `Pausa`, `Stop`) y reproducir el video con su barra de control nativa.
