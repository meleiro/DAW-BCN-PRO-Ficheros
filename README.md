# TiendaFicheros — Plantilla de alumnado (TXT → CSV → XML → JSON)

Proyecto **Java 21 + Swing + Maven**. Abrir la carpeta con IntelliJ IDEA, cargar Maven y ejecutar `app.Main`.

## Qué funciona desde el inicio
- Interfaz Swing con pestañas de clientes y productos.
- CRUD en memoria de clientes y productos.
- Selector de formato, selector de fichero y datos de ejemplo en `datos/`.
- Modelos, servicio y estructura Maven.

## Qué debe programar el alumnado
El archivo `src/main/java/persistencia/GestorFicheros.java` contiene **16 métodos de importación/exportación** sin implementar: clientes y productos, en TXT, CSV, XML y JSON. También quedan pendientes los auxiliares `csv()` y `parseCsv()`.

**Orden recomendado:** TXT → CSV → XML → JSON. Cada método pendiente lanza una `IOException` con el texto `TODO`, para que la interfaz avise sin sustituir accidentalmente los datos actuales por una lista vacía.

### Pruebas sugeridas
1. TXT: separar campos con `;`, probar `ñ` y campos vacíos.
2. CSV: probar `Pérez, Ana` y `Tienda "Pepe"`; observar comillas duplicadas.
3. XML: añadir o quitar etiquetas y cambiar tipos de datos.
4. JSON: eliminar propiedades, añadir `ciudad`, alterar un número y comparar `[]` con `{}`.
5. Comparar los ficheros exportados con los ejemplos en `datos/`.

**Aviso:** los datos del CRUD están en memoria hasta implementar la exportación/importación; cerrar la aplicación no los guarda automáticamente.
