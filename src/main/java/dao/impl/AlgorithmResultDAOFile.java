package dao.impl;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz AlgorithmResultDAO que utiliza archivos CSV
 * para almacenar los resultados de los algoritmos de resolución de laberintos.
 */
public class AlgorithmResultDAOFile implements AlgorithmResultDAO {

    private static final String FILE_NAME = "results.csv";

    /**
     * Guarda un resultado nuevo o actualiza uno existente en el archivo CSV.
     *
     * @param nuevo Objeto AlgorithmResult con los datos del algoritmo.
     */
    @Override
    public void guardar(AlgorithmResult nuevo) {
        List<AlgorithmResult> resultados = listar();
        boolean actualizado = false;

        for (AlgorithmResult r : resultados) {
            if (r.getAlgorithmName().equals(nuevo.getAlgorithmName())) {
                r.setSteps(nuevo.getSteps());
                r.setTimeNano(nuevo.getTimeNano());
                actualizado = true;
                break;
            }
        }

        if (!actualizado) {
            resultados.add(nuevo);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (AlgorithmResult r : resultados) {
                writer.write(r.toCSVLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }

    /**
     * Lee todos los resultados del archivo CSV.
     *
     * @return Lista de resultados de algoritmos.
     */
    @Override
    public List<AlgorithmResult> listar() {
        List<AlgorithmResult> resultados = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return resultados;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AlgorithmResult r = AlgorithmResult.fromCSVLine(line);
                if (r != null) {
                    resultados.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer resultados: " + e.getMessage());
        }

        return resultados;
    }

    /**
     * Elimina todos los resultados guardados sobrescribiendo el archivo con contenido vacío.
     */
    @Override
    public void limpiar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Sobrescribe el archivo con nada
        } catch (IOException e) {
            System.err.println("Error al limpiar resultados: " + e.getMessage());
        }
    }
}
