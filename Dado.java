import java.util.Random;

// Clase que representa un dado y su comportamiento
public class Dado extends ElementoJuego implements Tirada {
    private int valor; // Valor actual del dado
    private String[] caras; // Caras del dado
    private Random aleatorio = new Random(); // Generador de numeros aleatorios

    public Dado(String nombre, String[] caras) {
        super(nombre);
        this.caras = caras;
        lanzar();
    }

    // Metodo para lanzar el dado
    public void lanzar() {
        valor = aleatorio.nextInt(caras.length);
    }

    // Obtener la cara actual del dado
    public String getCara() {
        return caras[valor];
    }

    // Obtener el valor actual del dado
    public int getValor() {
        return valor;
    }

    // Obtener las caras del dado
    public String[] getCaras() {
        return caras;
    }

    @Override
    public String toString() {
        return getCara();
    }
}

