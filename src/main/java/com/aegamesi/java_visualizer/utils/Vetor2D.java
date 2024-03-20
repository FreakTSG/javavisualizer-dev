package utils;

public class Vetor2D {
    private double x, y;

    public Vetor2D() {
        this(0, 0);
    }

    public Vetor2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void normalize() {
        double var1 = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y);
        this.x *= var1;
        this.y *= var1;
    }

    public void scale(double fator) {
        this.x *= fator;
        this.y *= fator;
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public void add(Vetor2D v) {
        this.x += v.x;
        this.y += v.y;
    }

}
