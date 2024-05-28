import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;

public class Juego implements JuegoAcciones {
    private ArrayList<Jugador> jugadores; // Lista de jugadores
    private int rondas; // Numero de rondas
    private String[] carasDado; // Caras del dado
    private Jugador jugadorActual; // Jugador actual

    public Juego(ArrayList<Jugador> jugadores, int rondas, String[] carasDado) {
        this.jugadores = jugadores;
        this.rondas = rondas;
        this.carasDado = carasDado;
    }

    // Metodo para determinar el primer jugador
    public void determinarPrimerJugador() {
        boolean empate = true;
        ArrayList<Jugador> empatados = new ArrayList<>(jugadores);

        while (empate) {
            int maxValor = -1;
            empate = false;
            ArrayList<Jugador> nuevosEmpatados = new ArrayList<>();

            // Cada jugador lanza un dado
            for (Jugador jugador : empatados) {
                int valor = jugador.lanzarUnDado();
                if (valor > maxValor) {
                    maxValor = valor;
                    nuevosEmpatados.clear();
                    nuevosEmpatados.add(jugador);
                } else if (valor == maxValor) {
                    nuevosEmpatados.add(jugador);
                }
            }

            // Si solo un jugador tiene el valor mas alto, es el primer jugador
            if (nuevosEmpatados.size() == 1) {
                jugadorActual = nuevosEmpatados.get(0);
                empate = false;
            } else {
                empatados = nuevosEmpatados;
                empate = true;
            }
        }

        System.out.println("El primer jugador sera: " + jugadorActual.getNombre());
        Collections.rotate(jugadores, -jugadores.indexOf(jugadorActual));
    }

    // Metodo para jugar una ronda
    public void jugarRonda() {
        for (Jugador jugador : jugadores) {
            System.out.println(jugador.getNombre() + " esta lanzando");
            jugador.lanzarTodosDados();
            System.out.println(jugador);
            Scanner scanner = new Scanner(System.in);

            // Cada jugador puede decidir si volver a lanzar algunos dados
            while (jugador.getTiradasDisponibles() > 0) {
                System.out.println("¿Desea volver a lanzar algunos dados? (s/n):");
                String respuesta = scanner.nextLine();

                if (respuesta.equalsIgnoreCase("s")) {
                    boolean[] dadosSeleccionados = new boolean[5];
                    for (int i = 0; i < 5; i++) {
                        System.out.println("¿Lanzar de nuevo el dado " + (i + 1) + " (" + jugador.getDados()[i] + ")? (s/n):");
                        respuesta = scanner.nextLine();
                        dadosSeleccionados[i] = respuesta.equalsIgnoreCase("s");
                    }
                    jugador.lanzarDadosSeleccionados(dadosSeleccionados);
                    System.out.println(jugador);
                } else {
                    break;
                }
            }
        }

        // Determinar el ganador de la ronda y asignar puntos
        Jugador ganadorRonda = determinarGanadorRonda();
        if (ganadorRonda != null) {
            System.out.println("Ganador de la ronda: " + ganadorRonda.getNombre());
        }

        // Reiniciar tiradas disponibles para cada jugador
        for (Jugador jugador : jugadores) {
            jugador.reiniciarTiradas();
        }
    }

    // Metodo para determinar el ganador de una ronda
    public Jugador determinarGanadorRonda() {
        Jugador ganador = null;
        int maxPuntos = -1;
        ArrayList<Jugador> posiblesGanadores = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            int puntos = evaluarPuntuacion(jugador.getDados());
            System.out.println(jugador.getNombre() + " obtiene " + puntos + " puntos.");

            if (puntos > maxPuntos) {
                maxPuntos = puntos;
                posiblesGanadores.clear();
                posiblesGanadores.add(jugador);
            } else if (puntos == maxPuntos) {
                posiblesGanadores.add(jugador);
            }
        }

        // Si hay empate, desempatar entre los posibles ganadores
        if (posiblesGanadores.size() > 1) {
            ganador = desempatar(posiblesGanadores);
        } else if (posiblesGanadores.size() == 1) {
            ganador = posiblesGanadores.get(0);
        }

        if (ganador != null) {
            ganador.agregarPunto();
        }
        return ganador;
    }

    // Metodo para evaluar la puntuacion de los dados de un jugador
    public int evaluarPuntuacion(Dado[] dados) {
        Map<String, Integer> conteo = new HashMap<>();
        for (Dado dado : dados) {
            conteo.put(dado.getCara(), conteo.getOrDefault(dado.getCara(), 0) + 1);
        }

        int max = Collections.max(conteo.values());
        if (max == 5) return 15; // Quintilla
        if (max == 4) return 11; // Poker
        if (max == 3) {
            if (conteo.containsValue(2)) return 9; // Full
            return 7; // Tercia
        }
        if (max == 2) {
            int pares = 0;
            for (int count : conteo.values()) {
                if (count == 2) pares++;
            }
            if (pares == 2) return 5; // Dos pares
            return 3; // Un par
        }

        // Si no tiene combinacion, evaluar el dado con mayor valor
        int maxValorDado = -1;
        for (Dado dado : dados) {
            int valor = encontrarIndice(carasDado, dado.getCara());
            if (valor > maxValorDado) {
                maxValorDado = valor;
            }
        }

        return maxValorDado; // Retorna el valor del dado mas alto si no hay combinacion
    }

    // Metodo para desempatar entre multiples jugadores
    public Jugador desempatar(List<Jugador> jugadoresEmpatados) {
        Jugador ganador = null;
        int maxValor = -1;

        for (Jugador jugador : jugadoresEmpatados) {
            for (Dado dado : jugador.getDados()) {
                int valor = encontrarIndice(carasDado, dado.getCara());
                if (valor > maxValor) {
                    maxValor = valor;
                    ganador = jugador;
                } else if (valor == maxValor && ganador != null) {
                    // Si los valores son iguales, seguir revisando hasta encontrar el más alto
                    ganador = desempatarEmpate(jugadoresEmpatados, maxValor);
                }
            }
        }
        return ganador;
    }

    // Metodo auxiliar para desempatar en caso de valores iguales en la primera verificación
    public Jugador desempatarEmpate(List<Jugador> jugadoresEmpatados, int maxValor) {
        Jugador ganador = null;
        int nuevoMaxValor = -1;

        for (Jugador jugador : jugadoresEmpatados) {
            for (Dado dado : jugador.getDados()) {
                int valor = encontrarIndice(carasDado, dado.getCara());
                if (valor > maxValor && valor > nuevoMaxValor) {
                    nuevoMaxValor = valor;
                    ganador = jugador;
                }
            }
        }
        return ganador;
    }

    // Metodo auxiliar para encontrar el indice de una cadena en un arreglo
    public int encontrarIndice(String[] arreglo, String valor) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i].equals(valor)) {
                return i;
            }
        }
        return -1;
    }

    // Metodo para iniciar el juego
    public void iniciarJuego() {
        System.out.println("Determinando el primer jugador...");
        determinarPrimerJugador();

        for (int i = 0; i < rondas; i++) {
            System.out.println("Ronda " + (i + 1));
            jugarRonda();
        }

        Jugador ganadorJuego = determinarGanadorJuego();
        System.out.println("Ganador del juego: " + ganadorJuego.getNombre() + " con " + ganadorJuego.getPuntos() + " rondas ganadas");
    }

    // Metodo para determinar el ganador del juego
    public Jugador determinarGanadorJuego() {
        return Collections.max(jugadores, Comparator.comparingInt(Jugador::getPuntos));
    }

    // Metodo para mostrar instrucciones segun el modo de juego
    public static void mostrarInstrucciones(int modo) {
        switch (modo) {
            case 1:
                System.out.println("Modo Clasico seleccionado. Instrucciones:");
                System.out.println("Quintilla: 5 dados con el mismo valor. (15 puntos)");
                System.out.println("Poker: 4 dados con el mismo valor. (11 puntos)");
                System.out.println("Full: 3 dados de un valor y 2 de otro. (9 puntos)");
                System.out.println("Tercia: 3 dados con el mismo valor. (7 puntos)");
                System.out.println("Dos pares: 2 dados de un valor y 2 de otro. (5 puntos)");
                System.out.println("Un par: 2 dados con el mismo valor. (3 puntos)");
                System.out.println("Si no tiene ninguna combinacion, el dado con mayor valor gana 1 punto.");
                break;
            case 2:
                System.out.println("Modo Colores seleccionado. Instrucciones:");
                System.out.println("Quintilla: 5 dados del mismo color. (15 puntos)");
                System.out.println("Poker: 4 dados del mismo color. (11 puntos)");
                System.out.println("Full: 3 dados de un color y 2 de otro. (9 puntos)");
                System.out.println("Tercia: 3 dados del mismo color. (7 puntos)");
                System.out.println("Dos pares: 2 dados de un color y 2 de otro. (5 puntos)");
                System.out.println("Un par: 2 dados del mismo color. (3 puntos)");
                System.out.println("Si no tiene ninguna combinacion, el dado con mayor valor gana 1 punto.");
                break;
            case 3:
                System.out.println("Modo Letras seleccionado. Instrucciones:");
                System.out.println("Quintilla: 5 dados con la misma letra. (15 puntos)");
                System.out.println("Poker: 4 dados con la misma letra. (11 puntos)");
                System.out.println("Full: 3 dados de una letra y 2 de otra. (9 puntos)");
                System.out.println("Tercia: 3 dados con la misma letra. (7 puntos)");
                System.out.println("Dos pares: 2 dados de una letra y 2 de otra. (5 puntos)");
                System.out.println("Un par: 2 dados con la misma letra. (3 puntos)");
                System.out.println("Si no tiene ninguna combinacion, el dado con mayor valor gana 1 punto.");
                break;
            case 4:
                System.out.println("Modo Palabras seleccionado. Instrucciones:");
                System.out.println("Quintilla: 5 dados con la misma palabra. (15 puntos)");
                System.out.println("Poker: 4 dados con la misma palabra. (11 puntos)");
                System.out.println("Full: 3 dados de una palabra y 2 de otra. (9 puntos)");
                System.out.println("Tercia: 3 dados con la misma palabra. (7 puntos)");
                System.out.println("Dos pares: 2 dados de una palabra y 2 de otra. (5 puntos)");
                System.out.println("Un par: 2 dados con la misma palabra. (3 puntos)");
                System.out.println("Si no tiene ninguna combinacion, el dado con mayor valor gana 1 punto.");
                break;
            default:
                System.out.println("Modo no valido.");
                break;
        }
    }
}

