import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class FractalExplorer {
    private int displaySize; // размер экрана
    private JImageDisplay display; // обновление отображения в разных методах в процессе вычисления
    private FractalGenerator fractal;
    private Rectangle2D.Double rectangle; // диапазон комплексной плоскости

    public FractalExplorer(int size) {
        displaySize = size;
        fractal = new Mandelbrot();
        rectangle = new Rectangle2D.Double();
        fractal.getInitialRange(rectangle);
        display = new JImageDisplay(displaySize, displaySize); // квадрат
    }

    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer"); // заголовок
        frame.add(display, BorderLayout.CENTER);
        // создание кнопки
        JButton resetButton = new JButton("Reset scale");

        // обработчик кнопки сброса
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);
        frame.add(resetButton, BorderLayout.SOUTH);

        // обработчик клика мыши по фракталу
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        // окно закрывается только при нажатие крестика
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox ComboBox = new JComboBox();

        FractalGenerator mandelbrotFractal = new Mandelbrot();
        ComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        ComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        ComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        ComboBox.addActionListener(fractalChooser);

        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(ComboBox);
        frame.add(myPanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        frame.add(myBottomPanel, BorderLayout.SOUTH);

        /** Instance of ButtonHandler on the save button. **/
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /*
     * метод для отрисовки фрактала
     * цвет пикселей в зависимости от кол-ва итераций
     */
    private void drawFractal() {
        System.out.println("drawFractal() start");
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {

                double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x +
                        rectangle.width, displaySize, x);
                double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y +
                        rectangle.height, displaySize, y);

                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1) { // точка не выходит за границы, ставим черный
                    display.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        System.out.println("drawFractal() done");
        display.repaint();
    }

    private class ResetHandler implements ActionListener {

        // метод созданный по умолчанию
        @Override
        public void actionPerformed(ActionEvent e) {
            // возвращение фрактала к начальному положению
            fractal.getInitialRange(rectangle);

            drawFractal();
        }
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            // Координаты клика
            int x = e.getX();
            int y = e.getY();
            // Новые координаты центра
            double xCoord = FractalGenerator.getCoord(rectangle.x,
                    rectangle.x + rectangle.width, displaySize, x);
            double yCoord = FractalGenerator.getCoord(rectangle.y,
                    rectangle.y + rectangle.height, displaySize, y);

            // Устанавливаем центр в точку по которой был клик и приближаем
            fractal.recenterAndZoomRange(rectangle, xCoord, yCoord, 0.5);
            display.clearImage(); // очищаем экран
            // перерисовываем
            drawFractal();
        }
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            /** Get the source of the action. **/
            String command = e.getActionCommand();

            /**
             * If the source is the combo box, get the fractal the user
             * selected and display it.
             */
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(rectangle);
                drawFractal();

            }
            /**
             * If the source is the reset button, reset the display and draw
             * the fractal.
             */
            else if (command.equals("Reset")) {
                fractal.getInitialRange(rectangle);
                drawFractal();
            }
            /**
             * If the source is the save button, save the current fractal
             * image.
             */
            else if (command.equals("Save")) {

                /** Allow the user to choose a file to save the image to. **/
                JFileChooser myFileChooser = new JFileChooser();

                /** Save only PNG images. **/
                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                /**
                 * Ensures that the filechooser won't allow non-".png"
                 * filenames.
                 */
                myFileChooser.setAcceptAllFileFilterUsed(false);

                /**
                 * Pops up a "Save file" window which lets the user select a
                 * directory and file to save to.
                 */
                int userSelection = myFileChooser.showSaveDialog(display);

                /**
                 * If the outcome of the file-selection operation is
                 * APPROVE_OPTION, continue with the file-save operation.
                 */
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    /** Get the file and file name. **/
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                /**
                 * If the file-save operation is not APPROVE_OPTION, return.
                 */
                else return;
            }
        }
    }


    public static void main(String[] args) {
        // размер окна
        FractalExplorer fractal = new FractalExplorer(700);
        // рисование gui
        fractal.createAndShowGUI();
        // оторисовка
        fractal.drawFractal();
    }
}
