# MiAplicacion - Actividad 1.4: Controles Widgets y Desarrollo de Containers

**Institución:** Instituto Profesional Santo Tomás  
**Carrera:** Ingeniería / Técnico en Informática  
**Asignatura:** Programación Android (IEI-093)  
**Actividad:** 1.4 - Controles Widgets y desarrollo de Containers  
**Lenguaje:** Java  
**SDK Mínimo:** API 24 (Android 7.0 Nougat)  
**SDK Compilación / Destino:** API 34 (Android 14)  
**Herramienta de Construcción:** Gradle 8.6 + Android Gradle Plugin 8.3+  

---

## 1. Resumen Ejecutivo del Proyecto

El presente proyecto corresponde a la entrega formal de la **Actividad 1.4: Controles Widgets y desarrollo de Containers**, complementando y expandiendo la base arquitectónica desarrollada en la sesión previa (Actividad 1.3). 

La aplicación integra de manera armónica las especificaciones técnicas estipuladas en la **Pauta de Actividad oficial** y los **Desafíos Prácticos 1 y 2 de la Presentación de cátedra**, conformando una solución robusta, interactiva, modular y con estética Material Design para el sistema operativo Android.

---

## 2. Estructura y Arquitectura del Sistema

El proyecto se encuentra modularizado en diferentes actividades y componentes reutilizables:

```
MiAplicacion/
├── app/
│   ├── build.gradle                                    # Configuración SDK y dependencias (RecyclerView, CardView, Material)
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml                     # Declaración de componentes y actividades del sistema
│           ├── java/com/example/miaplicacion/
│           │   ├── MainActivity.java                   # Menú principal y demostración de Layouts base (Act. 1.3)
│           │   ├── MainActivity2.java                  # Implementación oficial Pauta 1.4 (Widgets y Containers)
│           │   ├── RegisterActivity.java               # Desafío 1: Formulario de registro con validaciones y ProgressBar
│           │   ├── WelcomeActivity.java                # Desafío 1: Pantalla de confirmación y bienvenida de usuario
│           │   ├── CourseModel.java                    # Desafío 2: POJO (Modelo de datos) para cursos
│           │   ├── CourseAdapter.java                  # Desafío 2: Adaptador RecyclerView con patrón ViewHolder
│           │   └── CourseListActivity.java             # Desafío 2: Pantalla con RecyclerView y lista de cursos
│           └── res/
│               ├── drawable/
│               │   ├── ic_avatar_student.xml           # Recurso vectorial para avatar de alumnos en tabla
│               │   ├── ic_course_icon.xml              # Recurso vectorial para tarjetas de cursos
│               │   ├── bg_table_border.xml             # Fondo y contornos estilizados para TableLayout
│               │   ├── bg_card.xml                     # Bordes redondeados y fondos de tarjetas
│               │   └── bg_header_gradient.xml          # Gradiente institucional Santo Tomás (#006847)
│               ├── layout/
│               │   ├── activity_main.xml               # Layout principal con navegación integrada
│               │   ├── activity_main2.xml              # Layout Pauta 1.4: CalendarView, Bars y TableLayout
│               │   ├── activity_register.xml           # Layout Desafío 1: Formulario con RadioGroup y ProgressBar
│               │   ├── activity_welcome.xml            # Layout Desafío 1: Resumen de datos registrados
│               │   ├── activity_course_list.xml        # Layout Desafío 2: Contenedor RecyclerView
│               │   └── card_layout.xml                 # Layout Desafío 2: Tarjeta CardView reutilizable
│               └── values/
│                   ├── colors.xml                      # Paleta cromática corporativa y semántica
│                   ├── strings.xml                     # Recursos de texto internacionalizables
│                   └── themes.xml                      # Estilos base Material Components
```

---

## 3. Implementación Detallada de Requerimientos

### A. Pauta de Actividad 1.4 (`MainActivity2.java` y `activity_main2.xml`)
1. **CalendarView**:
   - Incorporado con los atributos de espaciado y márgenes requeridos (`layout_marginTop="30dp"`, `layout_marginBottom="30dp"`, `layout_marginStart="31dp"`, `layout_marginEnd="31dp"`, `id="@+id/calendarView"`).
   - Conexión dinámica en Java mediante el evento `setOnDateChangeListener`, capturando día, mes y año para reflejar la fecha seleccionada en tiempo real.
2. **ProgressBar**:
   - Barra de progreso horizontal (`style="?android:attr/progressBarStyleHorizontal"`) con controles interactivos en Java (`+15% Progreso` y `Reiniciar 0%`).
3. **RatingBar**:
   - Implementada con `contentDescription="Calificación del producto"` según instrucción explícita del paso 57 de la pauta.
   - Manejo del evento `setOnRatingBarChangeListener` en Java para mostrar feedback inmediato y mensajes contextuales `Toast`.
4. **Barras de Segmento (SeekBar)**:
   - **SeekBar Continua**: Rango 0-100 con captura de eventos `OnSeekBarChangeListener` que actualiza el porcentaje en pantalla.
   - **SeekBar Discreta**: Implementada con estilo `style="@style/Widget.AppCompat.SeekBar.Discrete"` y pasos enteros (1 a 5).
5. **Containers: TableLayout con TableRow e ImageView**:
   - Contenedor padre `LinearLayout` con orientación vertical y título de encabezado.
   - `TableLayout` con propiedad `stretchColumns="*"` y fondo con bordes redondeados.
   - Filas `TableRow` que contienen:
     - Cabecera de columnas: `TextView` ("ID", "Nombre", "Apellido", "Foto") con estilo negrita.
     - Filas de registros con datos de alumnos.
     - Inclusión obligatoria de `ImageView` referenciando el recurso vectorial de la carpeta `drawable` (`@drawable/ic_avatar_student`), demostrando la inserción de imágenes dentro de celdas de una tabla en Android.

---

### B. Desafío Práctico 1: Formulario de Registro y Validación (`RegisterActivity.java`)
- **Campos obligatorios**: RUT, Nombre, Apellido Paterno, Apellido Materno, Edad, Teléfono, Dirección y Correo Electrónico.
- **Entrada de Decisión Excluyente**: `RadioGroup` con opciones de género (`RadioButton`: Masculino, Femenino, Otro).
- **ProgressBar condicional**: Inicia con `android:visibility="gone"` en el XML. Al presionar el botón `VALIDAR`, el código Java verifica que ningún campo esté vacío y que el formato de correo sea válido; tras la validación, cambia a `View.VISIBLE` durante el procesamiento.
- **Navegación**: Tras la validación exitosa, transfiere todos los datos a través de un `Intent` hacia `WelcomeActivity`, donde se visualiza el resumen del usuario registrado.

---

### C. Desafío Práctico 2: Lista Dinámica de Cursos (`RecyclerView` + `CardView`)
- **RecyclerView**: Diseñado para el reciclaje eficiente de vistas en memoria mediante `LinearLayoutManager`.
- **CardView (`card_layout.xml`)**: Tarjetas de diseño visual Material Design con elevación (`cardElevation="4dp"`), bordes curvos (`cardCornerRadius="12dp"`) e imagen de curso.
- **Modelo de Datos POJO (`CourseModel.java`)**: Estructura limpia de atributos (`courseName`, `courseRating`, `courseImage`, `courseDetail`) con constructor y métodos *getters/setters*.
- **Adaptador (`CourseAdapter.java`)**: Extiende de `RecyclerView.Adapter<CourseAdapter.ViewHolder>`, implementando el patrón de caché de vistas `ViewHolder` para evitar llamadas redundantes a `findViewById` e integrando respuesta táctil con `Toast`.

---

## 4. Respuestas a las Preguntas de Cierre (Pauta de Actividad)

### Pregunta 1: ¿Qué aprendiste sobre la integración de Widgets y Containers en una aplicación Android durante esta actividad?
> **Respuesta:**  
> Durante esta actividad se consolidó la comprensión de la jerarquía visual de Android: los **Containers (ViewGroup)** representan la infraestructura invisible que gobierna el flujo, alineación, distribución espacial y adaptabilidad de la interfaz (como `TableLayout` para matrices de datos y `RecyclerView` para listas de alto rendimiento), mientras que los **Widgets (View)** son los componentes atómicos interactivos (`CalendarView`, `ProgressBar`, `RatingBar`, `SeekBar`, `ImageView`, `EditText`, `Button`).  
> Se aprendió que una correcta integración no solo radica en la declaración en el XML, sino en la adecuada vinculación lógica en Java mediante listeners (`OnDateChangeListener`, `OnRatingBarChangeListener`, `OnSeekBarChangeListener`), la gestión del ciclo de visibilidad de componentes (como el `ProgressBar` que transiciona entre `GONE` y `VISIBLE`) y la optimización de memoria a través de patrones como el reciclaje de vistas en `RecyclerView`.

---

### Pregunta 2: ¿En qué ámbitos o proyectos podrías aplicar las habilidades desarrolladas en el uso de diferentes elementos de interfaz de usuario y la codificación de aplicaciones más complejas?
> **Respuesta:**  
> Las habilidades adquiridas son fundamentales en el desarrollo de software móvil empresarial y de consumo masivo, tales como:
> 1. **Sistemas de Gestión Académica y Portales Estudiantiles**: Módulos de toma de asignaturas, calificaciones en formato tabular (`TableLayout`), selección de fechas de exámenes (`CalendarView`) y encuestas de satisfacción docente con estrellas (`RatingBar`).
> 2. **Aplicaciones Bancarias, Fintech y Trámites Digitales**: Formularios de onboarding y validación de identidad con validación estricta de campos (RUT, correo, teléfono) e indicadores de carga y progreso (`ProgressBar`).
> 3. **Comercio Electrónico y Catálogos Móviles (E-commerce)**: Despliegue de catálogos extensos de productos mediante `RecyclerView` y `CardView`, filtros dinámicos por rango de precios utilizando `SeekBar`, y valoraciones de usuarios con `RatingBar`.
> 4. **Herramientas de Productividad y Reservas**: Agendamiento de citas médicas o reservación de salas donde la interactividad con calendarios y el feedback visual inmediato son críticos para la experiencia de usuario (UX).

---

### Pregunta 3: ¿Pudiste diseñar y codificar correctamente los Widgets y Containers en la aplicación Android según las especificaciones dadas?
> **Respuesta:**  
> **Sí, se diseñaron y codificaron exitosamente todos y cada uno de los componentes solicitados**, respetando los nombres de IDs, atributos específicos, márgenes y comportamientos definidos en la pauta:
> - El `CalendarView` cuenta con los márgenes especificados (30dp superior/inferior, 31dp laterales) y captura de fechas interactiva.
> - La `RatingBar` incluye la propiedad `contentDescription="Calificación del producto"` y respuesta a la interacción del usuario.
> - Se implementaron tanto la `SeekBar` continua como la `SeekBar` discreta con visualización de estado en tiempo real.
> - El `TableLayout` organiza filas (`TableRow`) con cabecera y registros, incorporando el `ImageView` desde la carpeta `drawable`.
> - Se completaron con excelencia los dos desafíos adicionales de la presentación: el Formulario de Registro con validaciones completas y activación de `ProgressBar`, y el `RecyclerView` dinámico con tarjetas `CardView` basadas en Material Design.
> - La solución compila de forma limpia en Gradle (`BUILD SUCCESSFUL`) y genera el APK instalable sin advertencias bloqueantes ni errores.

---

## 5. Instrucciones de Compilación y Ejecución

### Opción A: Compilación por Consola (Gradle Wrapper)
Desde la raíz del proyecto, ejecute:
```powershell
# Compilación del código fuente
.\gradlew.bat compileDebugSources

# Generación del paquete APK instalable
.\gradlew.bat assembleDebug
```
El archivo compilado se genera en:
`app/build/outputs/apk/debug/app-debug.apk`

### Opción B: Ejecución en Android Studio
1. Abrir **Android Studio**.
2. Seleccionar **Open** y abrir la carpeta del proyecto (`MiAplicacion`).
3. Sincronizar el proyecto con los archivos Gradle (**Sync Project with Gradle Files**).
4. Seleccionar un emulador virtual (AVD) o dispositivo físico conectado por USB con depuración activada.
5. Presionar **Run 'app'** (Shift + F10) para compilar y lanzar la aplicación.

---
*Desarrollado para la cátedra de Programación Android (IEI-093) - Instituto Profesional Santo Tomás.*


---

# Actividad 2.1: Programación de Actividades y Eventos (Intents & Ciclo de Vida)

**Asignatura:** Programación Android (IEI-093)  
**Docente:** Oscar Monárdez Saguas  
**Institución:** Instituto Profesional Santo Tomás  
**Lenguaje:** Java | **Target SDK:** 30 (Android 11) | **Compile SDK:** 34  

### Novedades implementadas en esta entrega:
1. **Activity Emisora (Inicio.java + activity_inicio.xml):**
   - Header temático "CAMARA".
   - Botón "HACER FOTO" (id/btnFoto): Invoca la cámara del sistema mediante un Intent Implícito (MediaStore.ACTION_IMAGE_CAPTURE).
   - Vista de imagen ImageView (id/imageView) con la fotografía de muestra de la pauta.
   - Campo de texto EditText (id/ETNombreFoto) para capturar el nombre de la foto (StNombre).
   - Botón "ACEPTAR" (id/BTAceptar): Valida y transmite StNombre a AResultado.class mediante un Intent Explícito con putExtra("STNombre", StNombre).
2. **Activity Receptora (AResultado.java + activity_aresultado.xml):**
   - Desempaqueta el dato extraído mediante getIntent().getStringExtra("STNombre").
   - Presenta el nombre de la foto recibido y la imagen correspondiente.
   - Botón para Compartir (Intent.ACTION_SEND con Intent.createChooser(), diapositiva 10).
   - Botón para abrir el Navegador Web (Intent.ACTION_VIEW, diapositiva 9).
   - Botón para volver a Inicio (finish()).
3. **Control del Ciclo de Vida (Lifecycle):**
   - Implementación y trazabilidad en consola de onCreate(), onStart(), onResume(), onPause(), onStop(), onDestroy().
4. **Informe Formal:**
   - Disponible en INFORME_ACTIVIDAD_2_1_OSCAR_MONARDEZ.docx y INFORME_ACTIVIDAD_2_1.md con las respuestas a las 3 preguntas de cierre.
