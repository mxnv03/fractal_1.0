import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double rectangle) {
        // Значения для фрактала Мандельброта
        rectangle.x = -2;
        rectangle.y = -1.5;
        rectangle.height = 3;
        rectangle.width = 3;
    }

    /*
     * returns the number of iterations for the corresponding coordinate.
     */
    public int numIterations(double x, double y) {
        // Функция для фрактала Мандельброта
        // Zn= (Zn-1)^2 + c
        // Ограничения для функции
        // |Z| > 2
        // или пока меньше MAX_ITERATIONS
        int iteration = 0; // кол-во итераций
        double zReal = 0.0; // действительная часть
        double zComplex = 0.0; // комплексная часть
        while (iteration < MAX_ITERATIONS && zReal * zReal + zComplex * zComplex < 4) {
            double zRealNew = zReal * zReal - zComplex * zComplex + x;
            double zComplexNew = 2 * zReal * zComplex + y;
            zReal = zRealNew;
            zComplex = zComplexNew;
            iteration++;
        }

        if (iteration == MAX_ITERATIONS) {
            return -1;
        }
        return iteration;
    }
    public String toString() {
        return "Mandelbrot";
    }
}
