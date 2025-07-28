package models;

public class AlgorithmResult {
    private String algorithmName;
    private int steps;
    private long timeNano;

    public AlgorithmResult(String algorithmName, int steps, long timeNano) {
        this.algorithmName = algorithmName;
        this.steps = steps;
        this.timeNano = timeNano;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getTimeNano() {
        return timeNano;
    }

    public void setTimeNano(long timeNano) {
        this.timeNano = timeNano;
    }

    /**
     * Convierte este resultado a una línea de texto CSV para guardar en archivo.
     */
    public String toCSVLine() {
        return algorithmName + "," + steps + "," + timeNano;
    }

    /**
     * Crea un objeto AlgorithmResult a partir de una línea CSV.
     */
    public static AlgorithmResult fromCSVLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) return null;

        try {
            String name = parts[0];
            int steps = Integer.parseInt(parts[1]);
            long time = Long.parseLong(parts[2]);
            return new AlgorithmResult(name, steps, time);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "algorithmName='" + algorithmName + '\'' +
                ", steps=" + steps +
                ", timeNano=" + timeNano +
                '}';
    }
}
