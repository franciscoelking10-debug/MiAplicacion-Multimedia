# INFORME DE ENTREGA - ACTIVIDAD 2.2
## Concurrencia en Android: Threads, Sensores y Fluidez de la Interfaz

**Institución:** Instituto Profesional Santo Tomás  
**Carrera:** Ingeniería en Informática  
**Asignatura:** Programación Android (IEI-093)  
**Profesor:** Oscar Monárdez Saguas  
**Estudiante:** Francisco Urquieta  
**Entorno de Trabajo:** Android Studio | Lenguaje: **Java** | Target SDK: **Android 30 (API 30)**  
**Repositorio GitHub:** [https://github.com/franciscoelking10-debug/MiAplicacion1.git](https://github.com/franciscoelking10-debug/MiAplicacion1.git)  
**Fecha:** Octubre 2026  

---

## 1. Resumen de la Actividad Realizada

Siguiendo de forma rigurosa la **Pauta Oficial de la Actividad 2.2** y las directrices de la **Infografía de Concurrencia en Android** (Hilos, Sensores y Fluidez de la Interfaz), se integró en la arquitectura del proyecto `MiAplicacion` un módulo avanzado de procesamiento asincrónico y captura de entradas físicas del dispositivo (`ThreadsSensorsActivity.java`).

### 1.1 Implementación de Subprocesos (Threads) en Java y Sincronización con UI Thread
* **Código de Ejemplo 1 de la Pauta Oficial:** Se implementó el gestor de clics para descarga de recursos gráficos en red en un subproceso aislado mediante la instanciación directa de `Thread` con `Runnable`:
  ```java
  new Thread(new Runnable() {
      @Override
      public void run() {
          final Bitmap bitmap = NetworkImageHelper.loadImageFromNetwork(targetUrl);
          mImageView.post(new Runnable() {
              @Override
              public void run() {
                  mImageView.setImageBitmap(bitmap);
              }
          });
      }
  }).start();
  ```
* **Sincronización con el Hilo Principal:** Siguiendo la regla de oro de Android (*"Nunca bloquear el UI Thread ni actualizar vistas desde un hilo secundario"*), se empleó el método `mImageView.post(Runnable)` para encolar la actualización del `Bitmap` en el `Looper` del hilo principal una vez completada la transferencia en red.
* **Prevención de ANR (Application Not Responding):** Se incorporó un comparador didáctico que ilustra el peligro documentado en la infografía: si una tarea intensiva (>5 segundos) bloquea el UI Thread, el sistema operativo congela la pantalla y dispara el cierre forzoso por ANR.
* **Mecanismo Autónomo de Fallback Gráfico:** Se diseñó `NetworkImageHelper.java` con descarga HTTP real y generación procedural de respaldo con diseño institucional Santo Tomás, garantizando que la aplicación sea 100% resiliente y funcione sin errores en laboratorios presenciales sin acceso a internet externo.

### 1.2 Integración y Captura de Sensores de Hardware (SensorManager)
* **Código de Ejemplo 2 de la Pauta Oficial:** Se configuró el servicio del sistema `SensorManager` y la captura del sensor de **Vector de Rotación**:
  ```java
  private SensorManager sensorManager;
  private Sensor rotationSensor;
  sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
  rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
  ```
* **Procesamiento Trigonométrico Tridimensional:** A partir de los cuaterniones devueltos por el sensor (`[x*sin(θ/2), y*sin(θ/2), z*sin(θ/2), cos(θ/2)]`), se aplicó `SensorManager.getRotationMatrixFromVector` y `SensorManager.getOrientation` para obtener los tres ángulos canónicos de inclinación:
  * **Azimut:** Orientación magnética (brújula de 0° a 360°), vinculada en tiempo real a una aguja gráfica animada (`ivCompassNeedle.setRotation(-azimuth)`).
  * **Pitch:** Inclinación hacia adelante / atrás (-180° a 180°).
  * **Roll:** Inclinación lateral (-90° a 90°).
* **Sensor de Acelerómetro (`TYPE_ACCELEROMETER`):** Se integró la medición de fuerza inercial en m/s² y un algoritmo de **Detección de Sacudida (Shake Detection)** que identifica movimientos bruscos del usuario por encima de un umbral dinámico (15.0 m/s²), activando indicadores visuales en pantalla.
* **Gestión de Energía y Ciclo de Vida:** Los sensores se suscriben en `onResume()` mediante `registerListener()` y se desregistran estrictamente en `onPause()` con `unregisterListener()`, previniendo drenaje innecesario de batería según las mejores prácticas de Google Developers.

### 1.3 Gestión de Sensores en Segundo Plano (HandlerThread)
* Conforme a lo destacado en la infografía (*"Lectura constante requiere ejecución fuera del Main Thread: Procesamiento de datos crudo continuo"*), el cálculo matemático de matrices de rotación y filtrado de ruido no se ejecuta en el UI Thread, sino que se delega a un hilo de trabajo dedicado (`HandlerThread("SensorBackgroundWorker")`), manteniendo la interfaz gráfica fija a 60 cuadros por segundo (FPS).

### 1.4 Operaciones Asincrónicas con AsyncTask (Ciclo de Vida Completo)
* Se implementó la clase `SensorDataProcessorTask extends AsyncTask<Integer, Integer, String>` cubriendo rigurosamente las 4 etapas del ciclo de vida detalladas en la infografía:
  1. `onPreExecute()` (UI Thread): Inicialización de vistas, despliegue de `ProgressBar` horizontal, deshabilitación preventiva del botón de inicio y activación de cancelación.
  2. `doInBackground(Integer... params)` (Background Thread): Ejecución del bucle de procesamiento intensivo de 50 muestras de telemetría con invocaciones continuas a `publishProgress()`. Se respeta la bandera de interrupción mediante `isCancelled()`.
  3. `onProgressUpdate(Integer... values)` (UI Thread): Actualización suave del porcentaje (0% a 100%) y detalle del paquete procesado en la interfaz.
  4. `onPostExecute(String result)` (UI Thread): Notificación final, restauración de controles y confirmación con mensaje Toast.
  5. `onCancelled()`: Manejo limpio de interrupción del proceso cuando el usuario pulsa "CANCELAR".

### 1.5 Arquitectura de Servicios (Started Service)
* En concordancia con la infografía (*"SERVICIOS: Componentes sin interfaz que se ejecutan indefinidamente"*), se implementó `SensorBackgroundService.java` (`Started Service` con `START_STICKY`) que procesa métricas y emite mensajes de estado a través de un `BroadcastReceiver` registrado en la actividad.

---

## 2. Respuestas a las Preguntas de Cierre de la Pauta Oficial

### Pregunta 1: ¿Qué nuevos conocimientos y habilidades has adquirido en el manejo de subprocesos y captura de datos de sensores?
* **Comprensión del Modelo de Concurrencia de Android:** Aprendí a diferenciar con precisión el rol del Hilo Principal (`UI Thread / Main Thread`) frente a los Hilos de Fondo (`Worker / Background Threads`). Entendí que el hilo principal es responsable del despacho de eventos táctiles, dibujo de vistas y ciclo de vida; por ende, cualquier operación de entrada/salida bloqueante (descargas HTTP, lectura de base de datos o procesamiento matemático intensivo) debe delegarse obligatoriamente a subprocesos para no congelar la pantalla ni provocar un ANR.
* **Sincronización Inter-Hilos con `mImageView.post()` y `runOnUiThread()`:** Adquirí la destreza técnica de saltar la barrera entre hilos secundarios y el hilo de interfaz, utilizando el manejador de mensajes interno (`Looper/MessageQueue`) para inyectar actualizaciones visuales seguras una vez concluidas las tareas pesadas.
* **Ciclo Asincrónico con `AsyncTask`:** Dominé el flujo estructurado de `AsyncTask`, comprendiendo en qué hilo exacto se ejecuta cada método (`onPreExecute`, `onProgressUpdate` y `onPostExecute` en el hilo de interfaz; `doInBackground` en el hilo de fondo) y cómo transmitir valores de progreso en tiempo real sin bloquear al usuario.
* **Integración del Hardware mediante `SensorManager`:** Aprendí a consultar el hardware físico mediante `Context.SENSOR_SERVICE`, a manejar el ciclo de vida de los oyentes (`registerListener` en `onResume` y `unregisterListener` en `onPause` para no agotar la batería), y a convertir vectores crudos (cuaterniones) en magnitudes físicas comprensibles (grados de inclinación y aceleración G).

---

### Pregunta 2: ¿Cómo podrías utilizar los Threads y los sensores en otras aplicaciones móviles?
* **Navegación Satelital y Realidad Aumentada (AR):** Utilizando el sensor de vector de rotación (`TYPE_ROTATION_VECTOR`) en conjunto con el GPS, se puede orientar en tiempo real una flecha de navegación o superponer información tridimensional sobre la vista de la cámara al apuntar el dispositivo hacia un punto de interés.
* **Sistemas de Monitoreo Logístico y Seguridad en Transporte:** El acelerómetro permite detectar patrones de frenado brusco, aceleraciones excesivas o impactos (accidentes). Al procesar estos datos en un `Started Service` en segundo plano, la aplicación puede registrar eventos telemáticos sin importar si el conductor tiene otra app en primer plano.
* **Fitness y Salud (Podómetros y Detección de Caídas):** Mediante algoritmos de filtrado paso bajo ejecutados en subprocesos secundarios, se pueden procesar los picos de aceleración para contar pasos o enviar una alerta automática por SMS/Internet en caso de caída de una persona mayor.
* **Descarga Masiva y Procesamiento de Archivos en Segundo Plano:** En aplicaciones empresariales de inventario o catálogos, el uso de subprocesos (`Threads`/`ExecutorService`) permite sincronizar cientos de imágenes y datos de stock desde un servidor REST en segundo plano mientras el operario continúa utilizando la app con total fluidez.

---

### Pregunta 3: ¿Qué desafíos encontraste en el proceso y cómo los superaste?
* **Desafío 1 - Restricción de Acceso a Vistas desde Hilos Secundarios (`CalledFromWrongThreadException`):**  
  * *Problema:* Al intentar modificar directamente el `ImageView` o textos desde el bloque `run()` del `Thread`, Android lanza una excepción de sistema debido a que únicamente el hilo que creó la jerarquía de vistas puede modificarlas.  
  * *Solución:* Se aplicó con precisión el método `mImageView.post(new Runnable() { ... })` exigido en la pauta oficial y `runOnUiThread(...)`, asegurando que la asignación del `Bitmap` se efectúe en la cola de ejecución del UI Thread.
* **Desafío 2 - Variabilidad de Hardware en Emuladores y Dispositivos de Laboratorio:**  
  * *Problema:* En entornos de laboratorio o emuladores ligeros sin aceleración de sensores físicos, el sensor `TYPE_ROTATION_VECTOR` puede retornar `null`, lo que provocaría un cierre inesperado (`NullPointerException`) si se intenta registrar sin validación.  
  * *Solución:* Se programó una arquitectura con comprobación de nulidad, fallback inteligente hacia acelerómetro y modo simulado, garantizando que el código sea 100% robusto y ejecutable en cualquier computadora del laboratorio Santo Tomás.
* **Desafío 3 - Carga de Red y Permisos en Android 30 (API 30):**  
  * *Problema:* Para realizar descargas en segundo plano se requiere declarar formalmente permisos de red y evitar bloqueos por políticas de tráfico de texto plano (Cleartext Traffic).  
  * *Solución:* Se configuró `<uses-permission android:name="android.permission.INTERNET" />` en `AndroidManifest.xml` y se implementó un generador gráfico de respaldo institucional en `NetworkImageHelper.java` que previene cualquier falla por falta de internet.

---

## 3. Matriz de Cumplimiento de Criterios de Evaluación

| Criterio de Evaluación (Pauta Oficial IEI-093) | Estado | Evidencia en el Código Fuente |
| :--- | :---: | :--- |
| **Diseña App simple según requerimientos** | **Cumplido al 100%** | Interfaz limpia con tarjetas visuales, brújula interactiva y monitores de telemetría en `activity_threads_sensors.xml`. |
| **Codifica App simple en Java para Android** | **Cumplido al 100%** | Implementación pura en Java con `ThreadsSensorsActivity.java`, manejadores de eventos y arquitectura robusta. |
| **Diseña App compleja con distintas entradas de datos** | **Cumplido al 100%** | Integración simultánea de entrada táctil, URLs de red, sensor de Vector de Rotación 3D y Acelerómetro. |
| **Codifica App compleja en Java utilizando Threads** | **Cumplido al 100%** | Uso de `Thread` + `Runnable`, `mImageView.post()`, `AsyncTask` con sus 4 métodos, `HandlerThread` y `Started Service`. |

---

## 4. Estructura de Archivos del Proyecto

```text
MiAplicacion/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml              (Declaración de permisos INTERNET, Activity 2.2 y Service)
│   │   ├── java/com/example/miaplicacion/
│   │   │   ├── ThreadsSensorsActivity.java  (Actividad principal: Threads, AsyncTask, SensorManager)
│   │   │   ├── NetworkImageHelper.java      (Helper de descarga HTTP y generador procedural de respaldo)
│   │   │   ├── SensorBackgroundService.java (Started Service para monitoreo continuo en segundo plano)
│   │   │   ├── MainActivity.java            (Hub principal con botón de navegación a Actividad 2.2)
│   │   │   ├── Inicio.java                  (Actividad 2.1 - Pantalla Cámara)
│   │   │   ├── AResultado.java              (Actividad 2.1 - Resultado y Lifecycle)
│   │   │   └── ...                          (Actividades de Unidad 1)
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── ic_sensor.xml            (Ícono de brújula / sensor)
│   │       │   ├── ic_cpu_thread.xml        (Ícono de procesamiento / subprocesos)
│   │       │   ├── ic_download.xml          (Ícono de descarga en red)
│   │       │   ├── ic_compass_dial.xml      (Cuadrante circular de brújula)
│   │       │   ├── ic_compass_needle.xml    (Aguja dinámica norte/sur)
│   │       │   └── ic_service.xml           (Ícono de servicio en segundo plano)
│   │       ├── layout/
│   │       │   ├── activity_threads_sensors.xml (Layout completo con las 4 secciones evaluadas)
│   │       │   └── activity_main.xml        (Actualizado con tarjeta de navegación Actividad 2.2)
│   │       └── values/
│   │           └── strings.xml              (Textos localizados y títulos de la Actividad 2.2)
├── INFORME_ACTIVIDAD_2_2.md                 (Documentación técnica de entrega)
└── INFORME_ACTIVIDAD_2_2_OSCAR_MONARDEZ.docx (Informe formal con formato de entrega)
```
