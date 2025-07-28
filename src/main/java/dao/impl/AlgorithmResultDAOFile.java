package dao.impl;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmResultDAOFile implements AlgorithmResultDAO {
    private static final String ARCHIVO = "resultados.csv";

    @Override
    public void guardar(AlgorithmResult nuevo) {
        List<AlgorithmResult> actuales = listar();
        boolean existe = false;

        for (int i = 0; i < actuales.size(); i++) {
            AlgorithmResult r = actuales.get(i);
            if (r.getAlgorithmName().equalsIgnoreCase(nuevo.getAlgorithmName())) {
                actuales.set(i, nuevo);
                existe = true;
                break;
            }
        }

        if (!existe) {
            actuales.add(nuevo);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (AlgorithmResult r : actuales) {
                writer.println(r.toCSVLine());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }

    @Override
    public List<AlgorithmResult> listar() {
        List<AlgorithmResult> lista = new ArrayList<>();

        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(AlgorithmResult.fromCSVLine(linea));
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void limpiar() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO))) {

        } catch (IOException e) {
            System.err.println("Error al limpiar archivo: " + e.getMessage());
        }
    }
}