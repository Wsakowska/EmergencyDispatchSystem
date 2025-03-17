package main.symulacja.model;

public class Lokalizacja {
    private double x;
    private double y;

    public Lokalizacja(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Obliczanie dystansu euklidesowego.
     */
    public double distanceTo(Lokalizacja other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f]", x, y);
    }
}
