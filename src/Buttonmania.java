// Tejedor Pérez, Diego

import com.sun.jna.platform.win32.User32;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Buttonmania {

    Scanner sc = new Scanner(System.in);

    // Definimos las constantes de tamaño, así como el nivel y los golpes.
    final int TAM = 8; // El tamaño es de 8 huecos {0,1,2,3,4,5,6,7} pero solo mostraremos 6 {1,2,3,4,5,6}
    int guardado = 0;
    int nivel = 5; // Nivel en el que empieza el juego 5 o Normal (Ni muy fácil, ni muy difícil).
    int nuevoNivel;
    int golpes = nivel * 3; // 3 golpes por cada nivel usado.
    int golpesUsados = 0; // Contador de golpes para el resultado final.


    // Creamos el array del tablero y el de la copia, en caso de querer reinciar.
    int[][] tablero = new int[TAM][TAM];
    int[][] copia = new int[TAM][TAM];
    int fila=3;
    int columna=4;
    int[][] filaG = new int[1][1];
    int[][] columnaG = new int[1][1];



    // Creamos la función de incrementar Casilla.
    // Si la casilla tiene un valor de 3, el siguiente golpe la pondrá a cero.
    // En caso contrario sumará 1.
    // Como es un array recibira 2 parametros, fila y columna.
    public void incrementarCasilla(int fila, int columna) {
        if (tablero[fila][columna] == 3) {
            tablero[fila][columna] = 0;
        } else {
            tablero[fila][columna]++;
        }
    }

    // Creamos la función decrementarCasilla que usaremos cuando empecemos a resolver el tablero.
    public void decrementarCasilla(int fila, int columna) {
        if (tablero[fila][columna] == 0) {
            tablero[fila][columna] = 3; // Si es 0, vuelve a 3
        } else {
            tablero[fila][columna]--;
        }
    }


    // Creamos la función para aplicar golpes.
    // Usaremos la clase Random para que aplique los golpes de forma aleatoria.
    // La variable golpes es el resultado de "int nivel = 5" multiplicado por 3 (cantidad de golpes por nivel).
    // Por eso mismo comenzamos con 15 golpes.
    // La función "aplicarGolpes" elige 15 casillas a las cuales golpear.
    // Usará la función incrementar casilla para que ninguna de ellas exceda los límites.
    public void aplicarGolpes(int golpes) {
        Random r = new Random();
        for (int i = 0; i < golpes; i++) {
            fila = r.nextInt(1, TAM - 1);
            columna = r.nextInt(1, TAM - 1);
            incrementarCasilla(fila, columna);

            // Eso ha incrementado la casilla que ha recibido el golpe, ahora habrá que modificar las de alrededor.
            if (fila > 0) incrementarCasilla(fila - 1, columna);// Si la fila es 1 o mayor modificará la fila de arriba.
            if (fila < TAM - 1)
                incrementarCasilla(fila + 1, columna); // Si la fila 6 o menor modificará la fila de abajo.
            if (columna > 0)
                incrementarCasilla(fila, columna - 1); // Si la columna es 1 o mayor modificará la columna de arriba.
            if (columna < TAM - 1)
                incrementarCasilla(fila, columna + 1); // Si la columna es 7 o menor modificará la columna de abajo.


        }
        // Ya que estamos haremos una copia del tablero en caso de que queramos reinciar.
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                copia[i][j] = tablero[i][j];
            }
        }
    }

    // Creamos la función resolver que almacenará el historial de golpes que da el usuario.
    // usando la función decrementar casilla que hace lo contrario que cuando se genera.
    public void resolver() {

        if (guardado == filaG[0].length) {

            int[][] auxFilaG = new int[filaG.length][filaG[0].length + 1];
            for (int i = 0; i < filaG.length; i++) {
                for (int j = 0; j < filaG[i].length; j++) {
                    auxFilaG[i][j] = filaG[i][j];
                }
            }
            filaG = auxFilaG;
        }


        if (guardado == columnaG[0].length) {

            int[][] auxColumnaG = new int[columnaG.length][columnaG[0].length + 1];
            for (int i = 0; i < columnaG.length; i++) {
                for (int j = 0; j < columnaG[i].length; j++) {
                    auxColumnaG[i][j] = columnaG[i][j];
                }
            }
            columnaG = auxColumnaG;
        }

        filaG[0][guardado] = fila;
        columnaG[0][guardado] = columna;

        guardado++;


        decrementarCasilla(fila, columna);
        if (fila > 0) decrementarCasilla(fila - 1, columna);  // Arriba
        if (fila < TAM - 1) decrementarCasilla(fila + 1, columna);  // Abajo
        if (columna > 0) decrementarCasilla(fila, columna - 1);  // Izquierda
        if (columna < TAM - 1) decrementarCasilla(fila, columna + 1);  // Derecha
        golpesUsados++;
    }


    public void deshacer() {
        if (guardado > 0) {
            guardado--;  // Retrocedemos al paso anterior
            fila = filaG[0][guardado];
            columna = columnaG[0][guardado];

            // Restauramos la casilla y sus adyacentes (hacemos lo contrario que en 'resolver')
            incrementarCasilla(fila, columna);
            if (fila > 0) incrementarCasilla(fila - 1, columna);  // Arriba
            if (fila < TAM - 1) incrementarCasilla(fila + 1, columna);  // Abajo
            if (columna > 0) incrementarCasilla(fila, columna - 1);  // Izquierda
            if (columna < TAM - 1) incrementarCasilla(fila, columna + 1);  // Derecha

            // Decrementamos la cantidad de golpes usados al deshacer
            golpesUsados--;
        }
    }

    // Creamos la función reiniciar tablero usando la copia que habíamos hecho antes
    public void reiniciarTablero() {

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = copia[i][j];
            }
        }
        System.out.println("Tablero reiniciado");
        golpesUsados =0;
    }

    // Creamos la función nuevo juego.
    public void nuevoJuego() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = 0;
            }
        }
        aplicarGolpes(golpes);
    }

    public void salir() {
        System.out.println("Gracias por jugar");
        System.exit(0);
    }


    // Creamos la función cambiar de nivel.
    public void cambiarNivel() {

        System.out.println();
        System.out.println("Elige la dificultad del nuevo juego (1-9): ");
        boolean nivelValido = false;


        while (!nivelValido) {
            try {
                nuevoNivel = sc.nextInt();
                // Inciamos la cantidad a cero para que el bucle se incialice.

                if (nuevoNivel >= 1 && nuevoNivel <= 9) {
                    nivelValido = true;
                } else {
                    System.out.println("\u001B[91mNúmero fuera de rango. Tiene que ser un número entero entre 1 y 9");
                }

            } catch (InputMismatchException e) {
                System.out.println("\u001B[91mEso no es un número. Tiene que ser un número entero entre 1 y 9");
                sc.nextLine();
            }
        }
            // Reiniciamos el nivel y los golpes necesarios, hemos dejado como predefinido el nivel 5 o Normal;
            nivel = nuevoNivel;
            golpes = nivel * 3;

            for (int i = 0; i < TAM; i++) {
                for (int j = 0; j < TAM; j++) {
                    tablero[i][j] = 0;
                }
            }
            aplicarGolpes(golpes);
            golpesUsados = 0;

            // Al igual que en reinciar aprovechamos y guardamos la copia en caso de reinciar
            for (int i = 0; i < TAM; i++) {
                for (int j = 0; j < TAM; j++) {
                    copia[i][j] = tablero[i][j];
                }
            }
            System.out.println("El nuevo juego tiene dificultad " + nuevoNivel);
        }




    public void imprimirTablero() {
        // Definir los colores
        String colorAmarillo = "\u001B[93m";
        String colorAzulClaro = "\u001B[94m"; // Color azul claro
        String quitarColor = "\u001B[0m"; // Para resetear el color

        // Imprimir el borde superior
        for (int i = 0; i < (TAM - 1) * 4 + 1; i++) {  // Ajuste para incluir los bordes
            System.out.print(colorAmarillo + "-" + quitarColor);  // Imprime los guiones para el borde
        }
        System.out.println();  // Salto de línea después del borde superior

        // Imprimir las filas del tablero
        for (int i = 1; i < TAM - 1; i++) {  // Evita los bordes
            // Imprimir el borde izquierdo con color amarillo
            System.out.print(colorAmarillo + "|" + quitarColor);  // Imprime el borde izquierdo

            // Imprimir los valores de la fila
            for (int j = 1; j < TAM - 1; j++) {  // Evita los bordes
                if (i == fila && j == columna) {
                    // Casilla seleccionada con corchetes y color amarillo
                    System.out.print(colorAmarillo + "[" + tablero[i][j] + "]" + quitarColor);  // Casilla seleccionada
                } else {
                    // Valor normal con color azul claro
                    System.out.print(colorAzulClaro + " " + tablero[i][j] + " " + quitarColor);  // Valor de la casilla
                }

                // Imprimir el separador solo si no es la última columna
                if (j < TAM - 2) {
                    System.out.print(colorAmarillo + "|" + quitarColor);  // Separadores entre columnas
                }
            }

            // Fin de la fila
            System.out.println();  // Salto de línea después de cada fila

            // Imprimir el borde inferior después de cada fila
            if (i < TAM - 2) {
                for (int j = 0; j < (TAM - 1) * 4 + 1; j++) {  // Mismo número de guiones que el borde superior
                    System.out.print(colorAmarillo + "-" + quitarColor);  // Imprime los guiones para separar las filas
                }
                System.out.println();  // Salto de línea después del borde inferior
            }
        }

        // Imprimir el borde inferior final
        for (int i = 0; i < (TAM - 1) * 4 + 1; i++) {  // Mismo número de guiones que el borde superior
            System.out.print(colorAmarillo + "-" + quitarColor);  // Imprime los guiones para el borde
        }
        System.out.println();  // Salto de línea después del borde inferior final
    }


    // Creamos una función para saber si el tablero está a cero.
    public boolean tableroCero() {
        for (int i = 1; i < TAM - 1; i++) {
            for (int j = 1; j < TAM - 1; j++) {
                if (tablero[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }


    // Creamos una función para saber cuantos golpes uso el jugador
    public void verificarTablero() {
        if (tableroCero()) {
            if (golpesUsados < golpes) {
                System.out.println();
                System.out.println("\u001B[91m Enhorabuena! Has completado el juego en menos golpes de los esperados: " + golpesUsados + " / " + golpes);
            } else if (golpesUsados == golpes) {
                System.out.println();
                System.out.println("\u001B[91m Bien hecho! Has completado el juego en los golpes esperados: " + golpesUsados + " / " + golpes);
            } else {
                System.out.println();
                System.out.println("\u001B[91m Juego completado. Usaste: " + golpesUsados + " golpes");

            }
        }
        System.out.println();
        System.out.println("\033[1;36m Pues de regalo otro tablero del mismo nivel");
        nuevoJuego();
        golpesUsados = 0;
    }


    public static void main(String[] args) {

        Buttonmania juego = new Buttonmania();
        Scanner scanner = new Scanner(System.in);
        juego.nuevoJuego();

        while (true) {
            // Mostrar el tablero en cada iteración
            // Limpiar la pantalla antes de mostrar el tablero
            juego.imprimirTablero();
            System.out.println("\u001B[92m Opciones: Nuevo (N) - Reiniciar (R) - Deshacer (U) - Salir (S) - Cambiar Nivel (L)");
            System.out.println();
            System.out.println("\u001B[93m Movimientos: Arriba (8) - Derecha (6) - Izquierda (4) - Abajo (2) - Pulsar (5)");
            System.out.println();
            System.out.println("\u001B[95m Nivel de juego: " + juego.nivel);
            System.out.println("\u001B[95m Golpes restantes: " + juego.golpesUsados + " / " + juego.golpes);
            System.out.println();
            System.out.println("\u001B[97m Objetivo: Dejar todos los botones en '0'.");
            System.out.println("\u001B[97m Instrucciones: Al pulsar se reducirá la casilla seleccionada y sus cuatro cardinales (Arriba, Izquierda, Derecha y Abajo).");

            /*for (int i = 0; i <juego.filaG.length;i++){
                System.out.println();
                for (int j = 0; j < juego.filaG[i].length; j++){
                    System.out.print(juego.filaG[i][j]+" , ");
                }
            }
            for (int i = 0; i <juego.columnaG.length;i++){
                System.out.println();
                for (int j = 0; j < juego.columnaG[i].length; j++){
                    System.out.print(juego.columnaG[i][j]+" , ");
                }
            }*/


            if (juego.tableroCero()) {
                juego.verificarTablero();



            } else {
                // Leer la tecla
                String tecla = scanner.nextLine().toUpperCase();

                // Realizar la acción correspondiente según la tecla
                switch (tecla) {

                    case "8": // Mover hacia arriba
                        if (juego.fila > 1) {
                            juego.fila--;
                            System.out.println("\033[1;36m Corchete movido hacia arriba.");
                        }
                        break;
                    case "2": // Mover hacia abajo
                        if (juego.fila < 6) {
                            juego.fila++;
                            System.out.println("\033[1;36m Corchete movido hacia abajo.");
                        }
                        break;
                    case "4": // Mover hacia la izquierda
                        if (juego.columna > 1) {
                            juego.columna--;
                            System.out.println("\033[1;36m Corchete movido hacia la izquierda.");
                        }
                        break;
                    case "6": // Mover hacia la derecha
                        if (juego.columna < 6) {
                            juego.columna++;
                            System.out.println("\033[1;36m Corchete movido hacia la derecha.");
                        }
                        break;
                    case "5": // Decrementar casilla
                        juego.resolver();
                        System.out.println("\033[1;36m Casilla decrementada.");
                        break;
                    case "N": // Nuevo juego
                        juego.nuevoJuego();
                        System.out.println("\033[1;36m Nuevo juego iniciado.");
                        break;
                    case "L": // Cambiar nivel
                        juego.cambiarNivel();
                        System.out.println("\033[1;36m Nivel cambiado.");
                        break;
                    case "R": // Reiniciar juego
                        juego.reiniciarTablero();
                        System.out.println("\033[1;36m Juego reiniciado.");
                        break;
                    case "U": // Deshacer acción
                        juego.deshacer();
                        if (juego.golpesUsados > 0) {
                            System.out.println("\033[1;36m Acción deshecha.");
                        }else {
                            System.out.println(" \033[1;36m No hay movimientos que deshacer");
                        }
                        break;
                    case "S": // Salir
                        System.out.println("\033[1;36m Gracias por jugar.");
                        scanner.close();
                        return; // Termina el programa
                    default:
                        System.out.println("\033[1;36m Tecla no válida, intenta de nuevo.");
                        break;
                }
            }
        }

    }
}
