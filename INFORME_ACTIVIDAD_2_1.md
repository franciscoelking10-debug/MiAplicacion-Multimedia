# INFORME DE ENTREGA - ACTIVIDAD 2.1
## Programación de Actividades y Eventos (Intents & Lifecycle)

**Institución:** Instituto Profesional Santo Tomás  
**Carrera:** Ingeniería en Informática  
**Asignatura:** Programación Android (IEI-093)  
**Profesor:** Oscar Monárdez Saguas  
**Estudiante:** Francisco Urquieta  
**Entorno de Trabajo:** Android Studio | Lenguaje: **Java** | Target SDK: **Android 30 (API 30)**  
**Repositorio GitHub:** [https://github.com/franciscoelking10-debug/MiAplicacion1.git](https://github.com/franciscoelking10-debug/MiAplicacion1.git)

---

## 1. Resumen de la Actividad Realizada

Siguiendo estrictamente la **Pauta de Actividad oficial**, el material de la **Presentación de Cátedra (Diapositivas 1 a 17)** y la **Infografía**:
1. Se avanzó sobre la aplicación base de la unidad anterior (`MiAplicacion`).
2. Se construyó la pantalla emisora `Inicio.java` con su layout `activity_inicio.xml`, incorporando:
   - Encabezado con título **"CAMARA"**.
   - Botón interactivo **"HACER FOTO"** (`id/btnFoto`) que dispara un **Intent Implícito** (`MediaStore.ACTION_IMAGE_CAPTURE`) para invocar la cámara del sistema.
   - Vista de imagen `ImageView` (`id/imageView`) con la fotografía de muestra oficial (`@drawable/fotografo_ejemplo`).
   - Campo de texto `EditText` (`id/ETNombreFoto`) con etiqueta **"Nombre Foto:"** y hint `"Nombre"`.
   - Botón **"ACEPTAR"** (`id/BTAceptar`) con el listener exacto exigido en la pauta:
     ```java
     final Button BTAceptar = (Button) findViewById(R.id.BTAceptar);
     BTAceptar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             EditText ETNombre = (EditText) findViewById(R.id.ETNombreFoto);
             String StNombre = ETNombre.getText().toString();
             
             Intent sIntent = new Intent(Inicio.this, AResultado.class);
             sIntent.putExtra("STNombre", StNombre);
             startActivity(sIntent);
         }
     });
     ```
3. Se construyó la pantalla receptora `AResultado.java` con su layout `activity_aresultado.xml`:
   - Desempaqueta el dato transferido mediante el **Intent Explícito**:
     ```java
     String stNombre = getIntent().getStringExtra("STNombre");
     ```
   - Muestra el nombre recibido en un `TextView` destacado (`tvResultadoNombre`).
   - Muestra la fotografía capturada o de muestra (`ivResultadoFoto`).
   - Muestra un detalle técnico didáctico de la comunicación inter-actividades.
4. Se incorporaron **Intents Implícitos del Sistema Operativo**:
   - Invocación de la Cámara (`MediaStore.ACTION_IMAGE_CAPTURE`).
   - Menú nativo del sistema para **Compartir** texto/foto (`Intent.ACTION_SEND` con `Intent.createChooser()`, conforme a la Diapositiva 10).
   - Acceso al navegador web (`Intent.ACTION_VIEW` con URL institucional / Android Developers, conforme a la Diapositiva 9).
5. Se implementó la trazabilidad del **Ciclo de Vida (Lifecycle)** en ambas Activities (`onCreate()`, `onStart()`, `onResume()`, `onPause()`, `onStop()`, `onDestroy()`) con registros en `Logcat` y monitoreo en pantalla.
6. Se ajustó el proyecto a **Target SDK 30 (Android 11)** cumpliendo con los requerimientos de visibilidad de paquetes (`<queries>`) y compatibilidad Java 8 / JBR 21.

---

## 2. Respuestas a las Preguntas de Cierre

### Pregunta 1: ¿Qué nuevos conocimientos y habilidades has adquirido?
* **Diferenciación y aplicación de Intents Explícitos e Implícitos:** Comprendí que los Intents Explícitos se emplean para la navegación determinista interna (especificando clase origen y destino exacta, como `new Intent(Inicio.this, AResultado.class)`), mientras que los Intents Implícitos delegan acciones al sistema operativo Android (como `MediaStore.ACTION_IMAGE_CAPTURE` o `Intent.ACTION_SEND`) permitiendo que el usuario elija su app favorita (WhatsApp, Gmail, etc.) mediante un selector nativo (`createChooser`).
* **Mecanismo de transporte Clave-Valor (`putExtra` y `getStringExtra`):** Aprendí a estructurar la transferencia de datos entre pantallas aisladas mediante el contenedor `Bundle` de los Intents, reconociendo la importancia crítica de mantener claves idénticas entre la Activity emisora y receptora.
* **Control y comprensión del Ciclo de Vida (Lifecycle):** Comprobé cómo Android gestiona la memoria y la visibilidad de cada pantalla mediante los métodos callback (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`), entendiendo cuándo guardar datos temporales (en `onPause`) y cuándo liberar recursos.
* **Compatibilidad Android 30 (API 30):** Configuración de `<queries>` en `AndroidManifest.xml` para cumplir con las políticas de visibilidad de paquetes introducidas en Android 11.

---

### Pregunta 2: ¿Cómo podrías utilizar la comunicación entre Activities y el manejo de eventos en otras aplicaciones?
* **Flujos de Autenticación y Perfil:** Un formulario de Login o Registro valida las credenciales y transfiere mediante un Intent Explícito los datos del usuario autenticado (nombre, rol, token) hacia el Dashboard principal de la aplicación sin sobrecargar peticiones de red repetitivas.
* **Comercio Electrónico (E-Commerce):** Al seleccionar un producto en un catálogo (`RecyclerView`), se lanza la pantalla de detalle enviando el ID, precio y datos del producto vía `putExtra`. Al concretar el pedido, un Intent Implícito puede conectar con aplicaciones bancarias o de pago.
* **Sistemas de Gestión de Terreno / Bodega:** Un operario puede tomar una fotografía de un producto o remito con la cámara nativa (Intent Implícito), registrar el código en un campo de texto y transferir el paquete de datos a la pantalla de recepción de mercadería (Intent Explícito).
* **Retorno de Resultados:** Emplear `startActivityForResult` o `ActivityResultLauncher` para invocar selectores de archivos, lectores de códigos QR o contactos, procesando la respuesta recibida en la Activity emisora.

---

### Pregunta 3: ¿Lograste subir tu proyecto al repositorio Git sin problemas? ¿Qué desafíos encontraste en el proceso y cómo los superaste?
* **Integración al repositorio remoto:** El proyecto se integró correctamente al repositorio oficial en GitHub (`origin/main`).
* **Desafío 1 - Compatibilidad de Entorno Gradle y JBR:** Se presentó una incompatibilidad entre la versión de ejecución de Gradle y Java (`class file major version 69`), la cual se solucionó configurando explícitamente `org.gradle.java.home` hacia el JDK 21 / JBR en `gradle.properties`, permitiendo compilar sin ningún error (`BUILD SUCCESSFUL`).
* **Desafío 2 - Manejo de Recursos Gráficos y .gitignore:** Al añadir la fotografía de muestra de la pauta (`fotografo_ejemplo.png`) y los íconos vectoriales XML, se verificó que el archivo `.gitignore` mantuviera fuera de control de versiones las carpetas temporales (`build/`, `.gradle/`, `.idea/`), asegurando un repositorio limpio y profesional.
* **Desafío 3 - Trazabilidad y Commits Semánticos:** Se utilizó la convención Conventional Commits (`feat(actividad-2.1): ...`) para documentar claramente cada funcionalidad desarrollada para la revisión del profesor.

---

## 3. Estructura de Archivos del Proyecto

```text
MiAplicacion/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml              (Declaración de Inicio, AResultado y queries API 30)
│   │   ├── java/com/example/miaplicacion/
│   │   │   ├── Inicio.java                  (Activity Emisora: botón HACER FOTO y ACEPTAR con Intent)
│   │   │   ├── AResultado.java              (Activity Receptora: desempaqueta STNombre y muestra resultado)
│   │   │   ├── MainActivity.java            (Hub principal con enlace directo a Actividad 2.1)
│   │   │   ├── MainActivity2.java           (Unidad 1)
│   │   │   ├── RegisterActivity.java        (Unidad 1)
│   │   │   ├── WelcomeActivity.java         (Unidad 1)
│   │   │   ├── CourseListActivity.java      (Unidad 1)
│   │   │   └── ...
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── fotografo_ejemplo.png    (Imagen oficial recortada de la pauta)
│   │       │   ├── ic_camera.xml            (Ícono vectorial de cámara)
│   │       │   ├── ic_share.xml             (Ícono vectorial para compartir)
│   │       │   ├── ic_web.xml               (Ícono vectorial para navegador)
│   │       │   ├── ic_arrow_back.xml        (Ícono vectorial para volver)
│   │       │   └── ic_check_circle.xml      (Ícono vectorial de estado)
│   │       ├── layout/
│   │       │   ├── activity_inicio.xml      (Layout idéntico al diseño de la pauta)
│   │       │   ├── activity_aresultado.xml  (Layout para visualizar el resultado recibido)
│   │       │   └── activity_main.xml        (Sección 5 con acceso directo a Actividad 2.1)
│   │       └── values/
│   │           ├── strings.xml              (Textos y recursos de la Actividad 2.1)
│   │           └── colors.xml               (Paleta institucional Santo Tomás)
│   └── build.gradle                         (targetSdk 30, compileSdk 34, Java 8)
├── INFORME_ACTIVIDAD_2_1_OSCAR_MONARDEZ.docx (Informe formal en Word listo para entrega)
└── README.md                                (Documentación técnica general del repositorio)
```
