# WakeUpNow
## 1. Índice
- 1. Índice
- 2. Inicio
- 3. Guia para usuarios
- 4. Documentación técnica
## 2. Inicio 
 WakeUpNow es una aplicación para móviles que permite la creación de alarmas puntuales y periódicas las cuales presentaran un pequeño desafío a elección del usuario, el cual deberá ser resuelto antes de poder apagar la alarma.

El objetivo de esta aplicación es proveer al usuario de alarmas robustas y dificiles de ignorar que aseguren que este se despierte y no sea posible ignorar las alarmas para seguir durmiendo.
## 3. Guia para usuarios
### Instalación
Para instalar la aplicación se tendra que descargar el archivo apk provisto en el apartado de "Releases" una vez adquirido se debera pulsar sobre el y seguir las indicaciones de instalación.
Es posible que se requiera permisos para la instalación de aplicaciones de fuentes desconocidas.
### Primeros pasos
Configuración inicial: 
- Concede los permisos necesarios para su correcto funcionamiento (acceso a notificaciones y almacenamiento).
- Accede al generador de QR pulsando el botón de la esquina inferior izquierda con un icono de QR. Allí podrás visualizar el QR necesario para apagar las alarmas con tipo de desafío QR. Envía el QR a tu correo personal o descárgalo en la librería, imprímelo y colócalo lejos de tu habitación.

Crear una alarma:
- Pulsa el botón "Crear Alarma" en la pantalla principal para añadir una nueva alarma.
- Define:
  - Hora: Selecciona la hora para que suene la alarma.
  - Periodicidad: Configura si es diaria, semanal o puntual.
  - Desafío: Selecciona el tipo de reto que deberás resolver al sonar la alarma:
      - Problema corto: problema lógico-matemático, problema de cálculo o adivinanzas.
      - Sudoku 4x4.
      - Escanear un código QR.
- Pulsa el botón de "Confirmar" para guardar la alarma.

### Otras funcionalidades
Gestión de alarmas:
- Editar alarmas:
Modifica una alarma existente pulsando sobre ella en la lista.
- Activar/desactivar alarmas:
Usa el interruptor junto a cada alarma para activarla o desactivarla rápidamente.
- Eliminar alarmas:
Dentro de la pantalla de edición al pulsar sobre una alarma en la lista, hay un botón de "Eliminar".

Estadísticas detalladas. Consulta métricas relacionadas con tus alarmas, como:
- Tiempo promedio, máximo y mínimo para apagar una alarma.
- Errores cometidos al resolver desafíos.
- Visualiza estadísticas mediante gráficos de barras por rangos de hora:
  - Tiempo medio.
  - Tiempo máximo.
  - Tiempo mínimo.
  - Máximo de errores.

## 4. Documentación técnica

### Arquitectura

La aplicación sigue el patrón arquitectónico **MVVM** (Model-View-ViewModel), separando la lógica de negocio, los datos y la interfaz de usuario. Esto facilita el mantenimiento, escalabilidad y modularidad del código.

Las capas principales incluyen:

1. **Model**: Contiene las clases de datos y lógica relacionada con ellos.
2. **Repository**: Intermediario entre los modelos y las fuentes de datos, como `SharedPreferences`.
3. **ViewModel**: Gestiona la lógica de negocio y los datos necesarios para la vista.
4. **View**: Representa la interfaz de usuario (UI), compuesta por actividades, vistas personalizadas y adaptadores.

---

### Componentes Principales

#### 1. Model

Define las estructuras de datos fundamentales que utiliza la aplicación:

##### **Alarm**

Clase que representa una alarma configurada por el usuario.

- **Propiedades:**
  - `id: Int`: Identificador único de la alarma.
  - `time: String`: Hora de la alarma en formato `HH:mm`.
  - `name: String`: Nombre de la alarma.
  - `periodicity: String`: Frecuencia de la alarma (e.g., "Diaria", "Lunes").
  - `problem: String`: Tipo de desafío asociado ("Problema", "Sudoku", "QR").
  - `isActive: Boolean`: Indica si la alarma está activa.
  - `ringTime: Calendar`: Objeto `Calendar` que almacena la hora programada.

##### **AlarmStatistic**

Registra métricas relacionadas con la interacción del usuario al apagar la alarma.

- **Propiedades:**
  - `id: String`: Identificador único de la estadística.
  - `alarmSetTime: String`: Hora programada de la alarma.
  - `timeToTurnOff: Long`: Tiempo en milisegundos que tomó apagarla.
  - `failures: Int`: Número de intentos fallidos para desactivar la alarma.

- **Métodos:**  
  Serialización y deserialización para almacenar y recuperar las estadísticas.

##### **Problema**

Clase que genera problemas matemáticos, de lógica o acertijos.

- **Propiedades:**
  - `enunciado: String`: Texto del problema.
  - `opciones: List<String>`: Lista de respuestas posibles.
  - `respuestaCorrecta: String`: Respuesta correcta.

- **Métodos:**
  - `crearProblemaAleatorio()`: Genera problemas dinámicos con diferentes niveles de dificultad.

##### **Sudoku**

Clase que gestiona la lógica de un tablero de Sudoku.

- **Propiedades:**
  - `sudokuBoard: Array<IntArray>`: Tablero completo.
  - `userBoard: Array<IntArray>`: Tablero actual del usuario.
  - `editableCells: Array<BooleanArray>`: Celdas editables.

- **Métodos:**
  - `initializeSudoku()`: Inicializa el tablero.
  - `placeNumber(row: Int, col: Int, num: Int)`: Valida y coloca un número.
  - `isSudokuCompleted()`: Verifica si el tablero está completo.

---

#### 2. Repository

Gestión de la persistencia de datos y conexión con la capa de almacenamiento.

##### **AlarmPreferences**

Gestiona el almacenamiento y edición de alarmas en `SharedPreferences`.

- **Métodos:**
  - `loadAlarms()`: Carga la lista de alarmas.
  - `saveAlarm(alarm: Alarm)`: Almacena una nueva alarma.
  - `editAlarm(alarm: Alarm)`: Modifica una alarma existente.
  - `deleteAlarm(alarmId: Int)`: Elimina una alarma.

##### **AlarmRepository**

Proporciona una capa de abstracción para las operaciones CRUD sobre las alarmas.

- **Métodos:**
  - `getAlarms()`: Devuelve todas las alarmas.
  - `getAlarmById(alarmId: Int)`: Busca una alarma específica.
  - `saveAlarm(alarm: Alarm)`: Guarda una nueva alarma.
  - `deleteAlarm(alarmId: Int)`: Elimina una alarma.

##### **AlarmStatsRepository**

Gestiona las estadísticas de las alarmas, calculando métricas de rendimiento.

- **Métodos:**
  - `getAllStatistics()`: Devuelve todas las estadísticas almacenadas.
  - `getAverageTimeInRange(startHour: Int, endHour: Int)`: Calcula el tiempo promedio de apagado para un rango horario.
  - `getMaxErrorsInRange(startHour: Int, endHour: Int)`: Encuentra el máximo de errores en un rango.

---

#### 3. ViewModel

Gestiona los datos y la lógica de negocio necesarios para la capa de vista.

##### **CreateAlarmViewModel**

Gestiona la creación de alarmas.

- **Métodos:**
  - `saveAlarm(name: String)`: Crea y guarda una nueva alarma.

##### **EditAlarmViewModel**

Gestiona la edición y eliminación de alarmas existentes.

- **Métodos:**
  - `loadAlarm(alarmId: Int)`: Carga una alarma.
  - `updateAlarm(name: String, time: String, periodicity: String, problem: String)`: Actualiza los detalles de una alarma.
  - `deleteAlarm()`: Elimina una alarma seleccionada.

##### **AlarmSoundingViewModel**

Lógica para alarmas sonando con desafíos matemáticos.

- **Propiedades:**
  - `problema: LiveData<Problema>`: Desafío actual.
  - `shouldFinish: LiveData<Boolean>`: Indica si la actividad debe cerrarse.

- **Métodos:**
  - `verificarRespuesta(respuestaSeleccionada: String)`: Valida si la respuesta del usuario es correcta.

##### **QrGeneratorViewModel**

Genera y administra códigos QR.

- **Métodos:**
  - `saveBitmapToGallery(bitmap: Bitmap)`: Guarda un QR generado.
  - `sendEmailWithSmtp(bitmap: Bitmap)`: Envía un QR como archivo adjunto en un correo.

##### **SudokuSoundingViewModel**

Gestiona la lógica para el desafío de Sudoku.

- **Métodos:**
  - `checkAndPlaceNumber(row: Int, col: Int, selectedNumber: Int)`: Valida y coloca un número.
  - `isEditable(row: Int, col: Int)`: Verifica si una celda es editable.

---

#### 4. View

Representa la capa de interfaz de usuario, que interactúa directamente con el usuario.

##### **Actividades Principales:**
1. **MainActivity**: Lista las alarmas configuradas.
2. **CreateAlarmActivity**: Permite configurar nuevas alarmas.
3. **EditAlarmActivity**: Facilita la edición y eliminación de alarmas existentes.
4. **AlarmSoundingActivity**: Muestra problemas matemáticos para desactivar alarmas.
5. **QRSoundingActivity**: Escanea códigos QR para apagar alarmas.
6. **StatisticsActivity**: Presenta estadísticas en gráficos de barras.
7. **SudokuSoundingActivity**: Presenta un desafío de Sudoku.

##### **Adaptadores:**
- **AlarmAdapter**: Muestra la lista de alarmas en un `RecyclerView`.

##### **Vistas Personalizadas:**
- **BarChartView**: Genera gráficos de barras para estadísticas.

---

### Detalles Técnicos

### Configuración de Alarmas:
- **Programación**: Utiliza `AlarmManager`.
- **Persistencia**: Almacena alarmas en `SharedPreferences`.
- **Notificaciones**: Utiliza `NotificationManager`.

#### Desafíos de Alarmas:
- **Problemas Matemáticos**: Generados dinámicamente.
- **Códigos QR**: Validación con `JourneyApps BarcodeScanner`.
- **Sudoku**: Tablero interactivo con lógica de validación.

#### Estadísticas:
- **Recolección**: Registro de tiempos y errores.
- **Visualización**: Gráficos en tiempo real agrupados por hora.
