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

    public int getSteps() {
        return steps;
    }

    public long getTimeNano() {
        return timeNano;
    }

    public String toCSVLine() {
        return algorithmName + "," + steps + "," + timeNano;
    }

    public static AlgorithmResult fromCSVLine(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int steps = Integer.parseInt(parts[1]);
        long time = Long.parseLong(parts[2]);
        return new AlgorithmResult(name, steps, time);
    }
}