import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Seleccion de tipo de dados
        System.out.println("Seleccione el tipo de dados:");
        System.out.println("1. Clasico (9, 10, J, Q, K, As)");
        System.out.println("2. Colores");
        System.out.println("3. Letras");
        System.out.println("4. Palabras");

        int eleccion = -1;
        while (eleccion < 1 || eleccion > 4) {
            if (scanner.hasNextInt()) {
                eleccion = scanner.nextInt();
                if (eleccion < 1 || eleccion > 4) {
                    System.out.println("Opcion no valida. Por favor, seleccione una opcion entre 1 y 4.");
                }
            } else {
                System.out.println("Entrada no valida. Por favor, ingrese un numero.");
                scanner.next(); // Limpiar entrada no valida
            }
        }

        // Mostrar instrucciones segun el modo seleccionado
        Juego.mostrarInstrucciones(eleccion);

        String[] caras;
        switch (eleccion) {
            case 1:
                caras = new String[]{"9", "10", "J", "Q", "K", "A"};
                break;
            case 2:
                caras = new String[]{"Rojo", "Azul", "Verde", "Amarillo", "Negro", "Blanco"};
                break;
            case 3:
                caras = new String[]{"A", "B", "C", "D", "E", "F"};
                break;
            case 4:
                caras = new String[]{"Joahan,", "Omar el tilin,", "Navarro el temible,", "Arcangel CurlanGod,", "Olguin el sabio,", "Dictadora Maria luisa,"};
                break;
            default:
                caras = new String[]{"9", "10", "J", "Q", "K", "A"};
                break;
        }

        // Configuracion de jugadores
        ArrayList<Jugador> jugadores = new ArrayList<>();
        System.out.println("Ingrese el numero de jugadores (2-4):");

        int numJugadores = -1;
        while (numJugadores < 2 || numJugadores > 4) {
            if (scanner.hasNextInt()) {
                numJugadores = scanner.nextInt();
                if (numJugadores < 2 || numJugadores > 4) {
                    System.out.println("Numero no valido. Por favor, ingrese un numero entre 2 y 4.");
                }
            } else {
                System.out.println("Entrada no valida. Por favor, ingrese un numero.");
                scanner.next(); // Limpiar entrada no valida
            }
        }
        scanner.nextLine(); // Consumir nueva linea

        for (int i = 0; i < numJugadores; i++) {
            System.out.println("Ingrese el nombre del jugador " + (i + 1) + ":");
            String nombreJugador = scanner.nextLine();
            Jugador jugador = new Jugador(nombreJugador);
            jugador.inicializarJugador(caras); // Inicializar los dados, puntos y tiradas disponibles
            jugadores.add(jugador);
        }

        // Configuracion de rondas
        System.out.println("Ingrese el numero de rondas:");
        int rondas = -1;
        while (rondas < 1) {
            if (scanner.hasNextInt()) {
                rondas = scanner.nextInt();
                if (rondas < 1) {
                    System.out.println("Numero no valido. Por favor, ingrese un numero positivo.");
                }
            } else {
                System.out.println("Entrada no valida. Por favor, ingrese un numero.");
                scanner.next(); // Limpiar entrada no valida
            }
        }

        // Iniciar el juego
        Juego juego = new Juego(jugadores, rondas, caras);
        juego.iniciarJuego();
    }
}