import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    // Максимальное количество итераций
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double rectangle) {
        // Значения для фрактала Tricorn
        rectangle.x = -2;
        rectangle.y = -2.5;
        rectangle.height = 4;
        rectangle.width = 4;
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
        /** Start with iterations at 0. */
        int iteration = 0;
        /** Initialize zreal and zimaginary. */
        double zreal = 0.0;
        double zimaginary = 0.0;
        while (iteration < MAX_ITERATIONS && zreal * zreal + zimaginary * zimaginary < 4)
            while (iteration < MAX_ITERATIONS &&
                    zreal * zreal + zimaginary * zimaginary < 4)
            {
                double zrealNew = zreal * zreal - zimaginary * zimaginary + x;
                double zimaginaryNew = 2 * Math.abs(zreal)
                        * Math.abs(zimaginary) + y;

                zreal = zrealNew;
                zimaginary = zimaginaryNew;

                iteration += 1;
            }

        if (iteration == MAX_ITERATIONS) {
            return -1;
        }
        return iteration;
    }
    public String toString() {
        return "Burning Ship";
    }
}

