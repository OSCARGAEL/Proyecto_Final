// Clase que representa un jugador y su comportamiento
public class Jugador extends ElementoJuego implements Puntuaje, JugadorAcciones {
    private Dado[] dados = new Dado[5]; // Dados del jugador
    private int puntos; // Puntos del jugador
    private int tiradasDisponibles; // Tiradas disponibles por turno

    public Jugador(String nombre) {
        super(nombre);
    }

    // Metodo para inicializar los dados, puntos y tiradas disponibles
    public void inicializarJugador(String[] caras) {
        for (int i = 0; i < 5; i++) {
            dados[i] = new Dado("Dado " + (i + 1), caras);
        }
        puntos = 0; // Inicializa los puntos del jugador a 0
        tiradasDisponibles = 3; // Cada jugador puede tirar hasta tres veces por turno
    }

    // Metodo para lanzar todos los dados
    public void lanzarTodosDados() {
        for (Dado dado : dados) {
            dado.lanzar();
        }
        tiradasDisponibles--;
    }

    // Metodo para lanzar dados seleccionados
    public void lanzarDadosSeleccionados(boolean[] dadosSeleccionados) {
        for (int i = 0; i < dados.length; i++) {
            if (dadosSeleccionados[i]) {
                dados[i].lanzar();
            }
        }
        tiradasDisponibles--;
    }

    // Obtener los dados del jugador
    public Dado[] getDados() {
        return dados;
    }

    // Obtener los puntos del jugador
    public int getPuntos() {
        return puntos;
    }

    // Obtener las tiradas disponibles del jugador
    public int getTiradasDisponibles() {
        return tiradasDisponibles;
    }

    // Reiniciar las tiradas disponibles del jugador
    public void reiniciarTiradas() {
        tiradasDisponibles = 3;
    }

    // Agregar un punto al jugador
    public void agregarPunto() {
        puntos++;
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder(nombre + " tiene los dados: ");
        for (Dado dado : dados) {
            resultado.append(dado).append(" ");
        }
        return resultado.toString();
    }

    // Metodo para lanzar un solo dado al comienzo del juego
    public int lanzarUnDado() {
        Dado dado = new Dado("Dado", dados[0].getCaras());
        dado.lanzar();
        System.out.println(nombre + " tira el dado y obtiene: " + dado);
        return dado.getValor();
    }
}



