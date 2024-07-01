import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TransformadaHoughCirculos {
    private BufferedImage imagen;
    private int[][][] acumulador;
    private int ancho;
    private int alto;
    private int radioMax;
    private int radioMin;
    private int pasoRadio;
    
    public TransformadaHoughCirculos(String rutaImagen, int radioMin, int radioMax, int pasoRadio) throws IOException {
        System.out.println("Cargando imagen desde: " + rutaImagen);
        imagen = ImageIO.read(new File(rutaImagen));
        ancho = imagen.getWidth();
        alto = imagen.getHeight();
        this.radioMin = radioMin;
        this.radioMax = radioMax;
        this.pasoRadio = pasoRadio;
        acumulador = new int[ancho][alto][(radioMax - radioMin) / pasoRadio];
        System.out.println("Imagen cargada exitosamente.");
    }
    
    public void aplicarTransformada() {
        System.out.println("Aplicando transformada de Hough para círculos...");
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int color = imagen.getRGB(x, y) & 0xFF;
                if (color < 128) { // Punto blanco en la imagen binaria
                    for (int r = radioMin; r < radioMax; r += pasoRadio) {
                        for (int theta = 0; theta < 360; theta++) {
                            int a = (int)(x - r * Math.cos(Math.toRadians(theta)));
                            int b = (int)(y - r * Math.sin(Math.toRadians(theta)));
                            if (a >= 0 && a < ancho && b >= 0 && b < alto) {
                                acumulador[a][b][(r - radioMin) / pasoRadio]++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Transformada aplicada.");
    }
    
    public void detectarCirculos(int umbral) {
        System.out.println("Detectando círculos con umbral: " + umbral);
        for (int a = 0; a < ancho; a++) {
            for (int b = 0; b < alto; b++) {
                for (int rIdx = 0; rIdx < (radioMax - radioMin) / pasoRadio; rIdx++) {
                    if (acumulador[a][b][rIdx] > umbral) {
                        int r = radioMin + rIdx * pasoRadio;
                        System.out.println("Círculo detectado: centro = (" + a + ", " + b + "), radio = " + r);
                    }
                }
            }
        }
        System.out.println("Detección de círculos completa.");
    }
    
    public static void main(String[] args) {
        try {
            TransformadaHoughCirculos thc = new TransformadaHoughCirculos("C:/Users/Caro/OneDrive/Escritorio/motor.jpg", 20, 50, 1);
            thc.aplicarTransformada();
            thc.detectarCirculos(150); // Umbral arbitrario
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }
}
