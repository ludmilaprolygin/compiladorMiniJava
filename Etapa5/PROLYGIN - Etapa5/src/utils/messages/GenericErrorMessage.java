package utils.messages;

public class GenericErrorMessage {
    public static final String MISUSE_ERROR =
            "Error en la invocacion.\n" +
            "Ejecutar con alguna de las siguientes alternativas:\n    " +
                    "java -cp out main.Main <fuente.java>\n    " +
                    "java -jar Compilador.java <fuente.java>";
    public static final String FILE_NOT_FOUND_ERROR = "Error: Archivo no encontrado. Revisar la ruta e intentar nuevamente.";
    public static final String FILE_READ_ERROR = "Error al leer el archivo.";
}