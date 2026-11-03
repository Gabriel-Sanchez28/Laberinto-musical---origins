

import juego.Jugador;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        System.out.println("--------------------------------");
        System.out.println("     LABERINTO Y LA PARTITURA");
        System.out.println("--------------------------------");
        System.out.println("  El silencio reclama sus almas...");
        System.out.println();
        do {
            System.out.println("--------- MENU PRINCIPAL ---------");
            System.out.println("1. Nueva partida");
            System.out.println("2. Guia de controles");
            System.out.println("3. Salir del juego");
            System.out.println("-------------------------------");
            System.out.print("Selecciona una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();
            switch (opcion) {
                case 1:
                    System.out.print("\nIngresa tu nombre: ");
                    String nombre = scanner.nextLine();
                    Jugador jugador = new Jugador(nombre);
                    System.out.println("\nSelecciona tu obra musical:");
                    System.out.println("1. Toccata and Fugue in D minor, BWV 565");
                    System.out.println("2. Peer Gynt, Op. 23, No. 8");
                    System.out.println("3. Nocturne in E-flat major, Op. 9, No. 2");
                    System.out.print("Opcion: ");
                    int obra = scanner.nextInt();
                    scanner.nextLine();
                    switch (obra) {
                        case 1:
                            jugador.setObraSeleccionada(
                                    "Toccata and Fugue in D minor, BWV 565");
                            break;

                        case 2:
                            jugador.setObraSeleccionada(
                                    "Peer Gynt, Op. 23, No. 8");
                            break;

                        case 3:
                            jugador.setObraSeleccionada(
                                    "Nocturne in E-flat major, Op. 9, No. 2");
                            break;

                        default:
                            System.out.println("Opcion no valida.");
                            jugador.setObraSeleccionada(
                                    "Toccata y fuga en re menor - Bach");
                    }
                    System.out.println("\n¡Bienvenido, " + jugador.getNombre() + "!");
                    System.out.println("Obra seleccionada: " + jugador.getObraSeleccionada());
                    mostrarGuia();
                    System.out.println("\nPreparando el laberinto...");
                    System.out.println("La partida se iniciara proximamente.");
                    break;
                case 2:
                    mostrarGuia();
                    break;
                case 3:
                    System.out.println("\nAbandonas la catedral...");
                    System.out.println("¡Hasta la proxima partida!");
                    break;
                default:
                    System.out.println("\nOpcion no valida. Intenta nuevamente.");
            }
            System.out.println();
        } while (opcion != 3);
        scanner.close();
    }
    public static void mostrarGuia() {
        System.out.println("\n---------- GUIA DE CONTROLES ----------");
        System.out.println("W  - Moverse hacia arriba");
        System.out.println("A  - Moverse hacia la izquierda");
        System.out.println("S  - Moverse hacia abajo");
        System.out.println("D  - Moverse hacia la derecha");
        System.out.println("E  - Interactuar con puertas y objetos");
        System.out.println("H  - Consultar ayuda durante la partida");
        System.out.println("ESC - Pausar o volver al menu");
        System.out.println("------------------------------------");
    }
}
