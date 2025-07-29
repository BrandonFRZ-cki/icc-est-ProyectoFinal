package dao.impl;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz AlgorithmResultDAO.
 * Esta clase se encarga de guardar, listar y limpiar los resultados
 * de algoritmos utilizando un archivo CSV como almacenamiento persistente.
 */
public class AlgorithmResultDAOFile implements AlgorithmResultDAO {

    // Nombre del archivo CSV donde se almacenan los resultados
    private static final String FILE_NAME = "results.csv";

    /**
     * Guarda un nuevo resultado o actualiza uno existente en el archivo CSV.
     * Si ya existe un algoritmo con el mismo nombre, se actualiza; si no, se agrega.
     *
     * @param nuevo Objeto AlgorithmResult con los datos del algoritmo a guardar.
     */
    @Override
    public void guardar(AlgorithmResult nuevo) {
        // Lista actual de resultados leída desde el archivo
        List<AlgorithmResult> resultados = listar();
        boolean actualizado = false;

        // Busca si ya existe un algoritmo con ese nombre
        for (AlgorithmResult r : resultados) {
            if (r.getAlgorithmName().equals(nuevo.getAlgorithmName())) {
                // Si lo encuentra, actualiza pasos y tiempo
                r.setSteps(nuevo.getSteps());
                r.setTimeNano(nuevo.getTimeNano());
                actualizado = true;
                break;
            }
        }

        // Si no existía, se agrega como nuevo
        if (!actualizado) {
            resultados.add(nuevo);
        }

        // Escribe la lista de resultados actualizada al archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (AlgorithmResult r : resultados) {
                writer.write(r.toCSVLine()); // convierte cada resultado a una línea CSV
                writer.newLine(); // salta de línea
            }
        } catch (IOException e) {
            // Muestra error si ocurre una excepción de escritura
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }

    /**
     * Lee todos los resultados almacenados en el archivo CSV.
     *
     * @return Lista de AlgorithmResult leída desde el archivo.
     */
    @Override
    public List<AlgorithmResult> listar() {
        List<AlgorithmResult> resultados = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Si el archivo no existe, retorna lista vacía
        if (!file.exists()) return resultados;

        // Lee línea por línea y convierte cada una a un AlgorithmResult
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AlgorithmResult r = AlgorithmResult.fromCSVLine(line);
                if (r != null) {
                    resultados.add(r);
                }
            }
        } catch (IOException e) {
            // Muestra error si ocurre una excepción de lectura
            System.err.println("Error al leer resultados: " + e.getMessage());
        }

        return resultados;
    }

    /**
     * Limpia todos los resultados sobrescribiendo el archivo CSV con contenido vacío.
     */
    @Override
    public void limpiar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // No escribe nada, simplemente vacía el archivo
        } catch (IOException e) {
            // Muestra error si ocurre una excepción al limpiar
            System.err.println("Error al limpiar resultados: " + e.getMessage());
        }
    }
}
