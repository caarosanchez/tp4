import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TransformadaHough {
    private BufferedImage imagen;
    private int[][] acumulador;
    private int mBins = 100; // Número de bins para m
    private int bBins = 100; // Número de bins para b
    private double pasoM;
    private double pasoB;
    
    public TransformadaHough(String rutaImagen) throws IOException {
        System.out.println("Cargando imagen desde: " + rutaImagen);
        imagen = ImageIO.read(new File(rutaImagen));
        pasoM = (double)20 / mBins; // Rango de m: [-10, 10]
        pasoB = (double)20 / bBins; // Rango de b: [-10, 10]
        acumulador = new int[mBins][bBins];
        System.out.println("Imagen cargada exitosamente.");
    }
    
    public void aplicarTransformada() {
        System.out.println("Aplicando transformada de Hough...");
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int color = imagen.getRGB(x, y) & 0xFF;
                if (color < 128) { // Punto blanco en la imagen binaria
                    for (int mIdx = 0; mIdx < mBins; mIdx++) {
                        double m = -10 + mIdx * pasoM;
                        double b = y - m * x;
                        int bIdx = (int)((b + 10) / pasoB);
                        if (bIdx >= 0 && bIdx < bBins) {
                            acumulador[mIdx][bIdx]++;
                        }
                    }
                }
            }
        }
        System.out.println("Transformada aplicada.");
    }
    
    public void detectarLineas(int umbral) {
        System.out.println("Detectando líneas con umbral: " + umbral);
        for (int mIdx = 0; mIdx < mBins; mIdx++) {
            for (int bIdx = 0; bIdx < bBins; bIdx++) {
                if (acumulador[mIdx][bIdx] > umbral) {
                    double m = -10 + mIdx * pasoM;
                    double b = -10 + bIdx * pasoB;
                    System.out.println("Línea detectada: y = " + m + "x + " + b);
                }
            }
        }
        System.out.println("Detección de líneas completa.");
    }
    
    public static void main(String[] args) {
        try {
            TransformadaHough th = new TransformadaHough("C:/Users/Caro/OneDrive/Escritorio/sudoku.jpg");
            th.aplicarTransformada();
            th.detectarLineas(50);  // Umbral arbitrario
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}

