package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import modelo.Cliente;
import modelo.Producto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PLANTILLA DEL ALUMNADO — ACCESO A DATOS, UD1.
 *
 * Objetivo: completar progresivamente la persistencia en ficheros:
 *  1. TXT  -> BufferedReader / BufferedWriter, separador ';'.
 *  2. CSV  -> cabecera, comas y comillas escapadas.
 *  3. XML  -> XmlMapper y clases contenedoras.
 *  4. JSON -> ObjectMapper y TypeReference<List<...>>.
 *
 * IMPORTANTE: cada TODO lanza IOException a propósito.
 * Así la interfaz Swing mostrará un error comprensible y NO vaciará
 * la lista de datos al intentar importar un formato sin implementar.
 * Reemplaza el throw por tu implementación cuando completes el método.
 *
 * PRUEBAS: exporta, abre el fichero y modifica campos; prueba tildes,
 * delimitadores, comillas, campos ausentes y tipos incorrectos.
 */
public class GestorFicheros {

    // Se usarán en las prácticas de XML y JSON.
    private static final XmlMapper XML_MAPPER =
            (XmlMapper) new XmlMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final ObjectMapper MAPPER =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    // =============== TXT ===============
    /**
     * TODO TXT — Exportación de clientes.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarClientesTxt(Path ruta, List<Cliente> datos) throws IOException {

        try (BufferedWriter bw = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {


            for (Cliente c : datos) {
                bw.write(
                        c.getId()
                        + ";"
                        + c.getNombre()
                        + ";"
                        + c.getEmail()
                        + ";"
                        + c.getTelefono()
                );
                bw.newLine();
            }

        }

    }

    /**
     * TODO TXT — Importación de clientes.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Cliente> importarClientesTxt(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Cliente.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        List<Cliente> resultado = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)){
            String linea;
            while (( linea = br.readLine()) != null ){
                //linea = "1;Ana García;;600123123"
                String[] c = linea.split(";", -1 );

                //c= [1, Ana García, ,600123123]

                if (c.length != 4) continue;

                try {
                    resultado.add(new Cliente(Integer.parseInt(c[0]), c[1], c[2], c[3]));
                } catch (NumberFormatException ex){
                    System.out.println("Cliente TXT incorrecto en el id" + linea);
                }

            }
        }

        return resultado;

    }

    /**
     * TODO TXT — Exportación de productos.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarProductosTxt(Path ruta, List<Producto> datos) throws IOException {
        // TODO: escribir los objetos en el formato TXT.
        throw new IOException("TODO: exportar productos a TXT todavía no implementado");
    }

    /**
     * TODO TXT — Importación de productos.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Producto> importarProductosTxt(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Producto.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar productos desde TXT todavía no implementado");
    }


    //ID, NOMBRE, EMAIL, TELE
    // "Rodríguez, Pepe"
    // =============== CSV ===============
    // TODO CSV: escapar comas, comillas y saltos de línea.
    private static String csv(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")){
            return "\"" + valor.replace( "\""   , "\"\""  ) + "\"";
        }
        return valor;
    }

    /**
     * ================================================================
     * TODO: MÉTODO parseCsv()
     * ================================================================
     *
     * OBJETIVO:
     *
     * Recibir una línea CSV y dividirla en sus diferentes campos,
     * respetando las comas que aparezcan dentro de comillas.
     *
     *
     * EJEMPLO 1:
     *
     * Entrada:
     *
     *      1,Ana,ana@email.com,600123456
     *
     * Resultado esperado:
     *
     *      campos[0] = "1"
     *      campos[1] = "Ana"
     *      campos[2] = "ana@email.com"
     *      campos[3] = "600123456"
     *
     *
     * EJEMPLO 2:
     *
     * Entrada:
     *
     *      2,"Pérez, Juan",juan@email.com,611222333
     *
     * Resultado esperado:
     *
     *      campos[0] = "2"
     *      campos[1] = "Pérez, Juan"
     *      campos[2] = "juan@email.com"
     *      campos[3] = "611222333"
     *
     *
     * ATENCIÓN:
     *
     * No podemos utilizar:
     *
     *      linea.split(",")
     *
     * porque dividiría incorrectamente el nombre:
     *
     *      "Pérez, Juan"
     *
     *
     * EJEMPLO 3: COMILLAS DENTRO DEL CONTENIDO
     *
     * Entrada:
     *
     *      3,"Juan ""Pepe""",juan@email.com,622333444
     *
     * Resultado esperado:
     *
     *      campos[0] = "3"
     *      campos[1] = "Juan \"Pepe\""
     *      campos[2] = "juan@email.com"
     *      campos[3] = "622333444"
     *
     *
     * En CSV, dos comillas consecutivas dentro de un campo
     * entrecomillado representan UNA comilla real.
     *
     *
     * IMPORTANTE:
     *
     * Este ejercicio trabaja con una línea CSV cada vez.
     * No resolveremos todavía los campos entrecomillados
     * que contienen saltos de línea reales.
     */
    private static List<String> parseCsv(String linea) {

        /*
         * ============================================================
         * PASO 1: PREPARAR LA LISTA DE RESULTADOS
         * ============================================================
         *
         * Aquí guardaremos los campos que vayamos identificando.
         *
         * Por ejemplo:
         *
         *      1,Ana,Madrid
         *
         * debería producir:
         *
         *      ["1", "Ana", "Madrid"]
         */
        List<String> campos = new ArrayList<>();


        /*
         * ============================================================
         * PASO 2: PREPARAR EL CAMPO ACTUAL
         * ============================================================
         *
         * Vamos a recorrer la línea carácter a carácter.
         *
         * Necesitamos una variable en la que ir acumulando
         * los caracteres que pertenecen al campo actual.
         *
         * StringBuilder nos permite añadir caracteres mediante:
         *
         *      actual.append(caracter);
         *
         * Y vaciar su contenido mediante:
         *
         *      actual.setLength(0);
         */
        StringBuilder actual = new StringBuilder();

        boolean entreComillas = false;


        for (int i = 0 ; i < linea.length(); i++){
            char c = linea.charAt(i);

            if (c == '"') {
                if (entreComillas && linea.charAt(i + 1) == '"' && i + 1 < linea.length()) {
                    actual.append('"');

                    i++;
                } else {
                    entreComillas = !entreComillas;

                }
            } else if ( c == ',' && !entreComillas) {
                campos.add(actual.toString());
                actual.setLength(0);
                } else {
                    actual.append(c);

                }

        }
        campos.add(actual.toString());
        return campos;

    }

    /**
     * TODO CSV — Exportación de clientes.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarClientesCsv(Path ruta, List<Cliente> clientes) throws IOException {

        try (BufferedWriter bw = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            bw.write("id,nombre,email,telefono");
            bw.newLine();

            for (Cliente c: clientes){
                bw.write(c.getId() + "," + csv(c.getNombre()) + "," + csv(c.getEmail()) + "," + csv(c.getTelefono()));
                bw.newLine();
            }

        }


    }

    /**
     * TODO CSV — Importación de clientes.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Cliente> importarClientesCsv(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Cliente.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar clientes desde CSV todavía no implementado");
    }

    /**
     * TODO CSV — Exportación de productos.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarProductosCsv(Path ruta, List<Producto> datos) throws IOException {
        // TODO: escribir los objetos en el formato CSV.
        throw new IOException("TODO: exportar productos a CSV todavía no implementado");
    }

    /**
     * TODO CSV — Importación de productos.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Producto> importarProductosCsv(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Producto.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar productos desde CSV todavía no implementado");
    }

    // =============== XML ===============
    /**
     * TODO XML — Exportación de clientes.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarClientesXml(Path ruta, List<Cliente> datos) throws IOException {
        // TODO: escribir los objetos en el formato XML.
        throw new IOException("TODO: exportar clientes a XML todavía no implementado");
    }

    /**
     * TODO XML — Importación de clientes.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Cliente> importarClientesXml(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Cliente.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar clientes desde XML todavía no implementado");
    }

    /**
     * TODO XML — Exportación de productos.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarProductosXml(Path ruta, List<Producto> datos) throws IOException {
        // TODO: escribir los objetos en el formato XML.
        throw new IOException("TODO: exportar productos a XML todavía no implementado");
    }

    /**
     * TODO XML — Importación de productos.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Producto> importarProductosXml(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Producto.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar productos desde XML todavía no implementado");
    }

    // =============== JSON ===============
    /**
     * TODO JSON — Exportación de clientes.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarClientesJson(Path ruta, List<Cliente> datos) throws IOException {
        // TODO: escribir los objetos en el formato JSON.
        throw new IOException("TODO: exportar clientes a JSON todavía no implementado");
    }

    /**
     * TODO JSON — Importación de clientes.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Cliente> importarClientesJson(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Cliente.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar clientes desde JSON todavía no implementado");
    }

    /**
     * TODO JSON — Exportación de productos.
     * @param ruta fichero destino (Path)
     * @param datos objetos en memoria que hay que guardar
     */
    public static void exportarProductosJson(Path ruta, List<Producto> datos) throws IOException {
        // TODO: escribir los objetos en el formato JSON.
        throw new IOException("TODO: exportar productos a JSON todavía no implementado");
    }

    /**
     * TODO JSON — Importación de productos.
     * @param ruta fichero origen (Path)
     * @return objetos reconstruidos; devolver lista vacía si el fichero válido no contiene registros
     */
    public static List<Producto> importarProductosJson(Path ruta) throws IOException {
        // TODO: leer el fichero y reconstruir objetos Producto.
        // No devolver new ArrayList<>() como sustituto provisional:
        // la interfaz reemplaza su lista actual por el resultado.
        throw new IOException("TODO: importar productos desde JSON todavía no implementado");
    }

    // Clases contenedoras listas para la práctica XML.
    // Estudia por qué el constructor vacío, los getters y los setters
    // permiten a Jackson reconstruir objetos durante la importación.
    public static class ClientesXml {
        private List<Cliente> clientes = new ArrayList<>();

        public ClientesXml() {}
        public ClientesXml(List<Cliente> clientes) { this.clientes = clientes; }

        public List<Cliente> getClientes() { return clientes; }
        public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    }

    public static class ProductosXml {
        private List<Producto> productos = new ArrayList<>();

        public ProductosXml() {}
        public ProductosXml(List<Producto> productos) { this.productos = productos; }

        public List<Producto> getProductos() { return productos; }
        public void setProductos(List<Producto> productos) { this.productos = productos; }
    }

}
