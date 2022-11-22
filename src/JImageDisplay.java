import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {
    // приватное поле, которое управляет изображением
    private BufferedImage img;
    public BufferedImage getImage() {
        return img;
    }
    public JImageDisplay(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // компоненты (пиксели) имеют по 8 битов red green blue
        Dimension dim = new Dimension(width, height); // размер
        // метод родительского класса
        super.setPreferredSize(dim);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    public void clearImage() {
        // Заменяем каждый пиксель на черный
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                // Делает [i][j] пиксель черным
                img.setRGB(i, j, new Color(0, 0, 0).getRGB());
            }
        }
    }

    public void drawPixel(int x, int y, int rgbColor) {

        img.setRGB(x, y, rgbColor);
    }
}
