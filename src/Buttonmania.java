// Tejedor Pérez, Diego

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import java.util.Random;
import java.util.Scanner;

public class Buttonmania {

    Scanner sc = new Scanner(System.in);

    // Comandos de teclas
    public static final int VK_UP = 0x26;
    public static final int VK_DOWN = 0x27;
    public static final int VK_LEFT = 0x28;
    public static final int VK_RIGHT = 0x29;
    public static final int VK_ENTER = 0x0D;

    public static final int LETRA_N = 0x4E;
    public static final int LETRA_R = 0x52;
    public static final int LETRA_S = 0x53;
    public static final int LETRA_U = 0x55;
    public static final int LETRA_L = 0x4C;

    public static final int NUMERO_2 = 0x32;
    public static final int NUMERO_4 = 0x34;
    public static final int NUMERO_6 = 0x36;
    public static final int NUMERO_8 = 0x38;


    // Booleans de si están presionadas las teclas o no


    private static boolean arribaPressed = false;
    private static boolean abajoPressed = false;
    private static boolean izquierdaPressed = false;
    private static boolean derechaPressed = false;
    private static boolean introPressed = false;

    private static boolean nPressed = false;
    private static boolean lPressed = false;
    private static boolean sPressed = false;
    private static boolean rPressed = false;
    private static boolean uPressed = false;

    private static boolean n2Pressed = false;
    private static boolean n4Pressed = false;
    private static boolean n6Pressed = false;
    private static boolean n8Pressed = false;


    // Definimos las constantes de tamaño, así como el nivel y los golpes.
    final int TAM = 8; // El tamaño es de 8 huecos {0,1,2,3,4,5,6,7} pero solo mostraremos 6 {1,2,3,4,5,6}
    int RES = 0; // Tamaño del array dinamico para guardar las posiciones al resolver.
    int nivel = 5; // Nivel en el que empieza el juego 5 o Normal (Ni muy fácil, ni muy difícil).
    int nuevoNivel;
    int golpes = nivel * 3; // 3 golpes por cada nivel usado.
    int golpesUsados = 0; // Contador de golpes para el resultado final.


    // Creamos el array del tablero y el de la copia, en caso de querer reinciar.
    int[][] tablero = new int[TAM][TAM];
    int[][] copia = new int[TAM][TAM];
    int fila = 3;
    int columna = 3;
    int[][] filaG = new int[RES][fila];
    int[][] columnaG = new int[RES][columna];
    int[][] filaGAux = new int[RES][fila];
    int[][] columnaGAux = new int[RES][columna];


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

        for (int i = 0; i < filaG.length; i++) {
            for (int j = 0; j < filaG[i].length; j++) {
                filaGAux[i][j] = filaG[i][j];
                RES++;
            }
        }
        for (int i = 0; i < columnaG.length; i++) {
            for (int j = 0; j < columnaG[i].length; j++) {
                columnaGAux[i][j] = columnaG[i][j];
                RES++;
            }
        }

        decrementarCasilla(fila, columna);
        if (fila > 0) decrementarCasilla(fila - 1, columna);  // Arriba
        if (fila < TAM - 1) decrementarCasilla(fila + 1, columna);  // Abajo
        if (columna > 0) decrementarCasilla(fila, columna - 1);  // Izquierda
        if (columna < TAM - 1) decrementarCasilla(fila, columna + 1);  // Derecha
        golpesUsados++;
    }


    public void deshacer() {
        for (int i = 0; i < filaG.length; i++) {
            for (int j = 0; j < filaG[i].length; j++) {
                filaGAux[i][j] = filaG[i][j];
                RES--;
            }
        }
        for (int i = 0; i < columnaG.length; i++) {
            for (int j = 0; j < columnaG[i].length; j++) {
                columnaGAux[i][j] = columnaG[i][j];
                RES--;
            }
        }
        incrementarCasilla(fila, columna);
        if (fila > 0) incrementarCasilla(fila - 1, columna);  // Arriba
        if (fila < TAM - 1) incrementarCasilla(fila + 1, columna);  // Abajo
        if (columna > 0) incrementarCasilla(fila, columna - 1);  // Izquierda
        if (columna < TAM - 1) incrementarCasilla(fila, columna + 1);  // Derecha
        golpesUsados--;
    }

    // Creamos la función reiniciar tablero usando la copia que habíamos hecho antes
    public void reiniciarTablero() {

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = copia[i][j];
            }
        }
        System.out.println("Tablero reiniciado");
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
            if (nivel != nuevoNivel) {
                // Inciamos la cantidad a cero para que el bucle se incialice.
                System.out.println();
                System.out.println("Elige la dificultad del nuevo juego (1-9): ");
                nuevoNivel = sc.nextInt();

                // Hasta que el jugador no metá un número dentro del rango se lo seguirá pidiendo
                while (nuevoNivel < 1 || nuevoNivel > 9) {
                    System.out.println("Has de elegir un nivel entre 1 y 9");
                    nuevoNivel = sc.nextInt();
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

                // Al igual que en reinciar aprovechamos y guardamos la copia en caso de reinciar
                for (int i = 0; i < TAM; i++) {
                    for (int j = 0; j < TAM; j++) {
                        copia[i][j] = tablero[i][j];
                    }
                }
                System.out.println("El nuevo juego tiene dificultad " + nuevoNivel);
            }
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
                System.out.println("\u001B[91m Enhorabuena! Has completado el juego en menos golpes de los esperados: " + golpesUsados + " / " + golpes);
            } else if (golpesUsados == golpes) {
                System.out.println("\u001B[91m Bien hecho! Has completado el juego en los golpes esperados." + golpesUsados + " / " + golpes);
            } else {
                System.out.println("\u001B[91m Juego completado. Usaste: " + golpesUsados + " golpes");
            }
        }
    }


    public void moverCorchete() {


        boolean teclaPresionada = false;
        while (true) {
            // Detecta las teclas de movimiento y las acciones correspondientes
            if ((User32.INSTANCE.GetAsyncKeyState(0x60 + 2) & 0x8000) != 0 || (User32.INSTANCE.GetAsyncKeyState(VK_DOWN) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    // Mover hacia abajo
                    if (fila < 6) {  // Limita el movimiento dentro del tablero
                        fila++;
                    }
                    teclaPresionada = true;
                }
            } else if ((User32.INSTANCE.GetAsyncKeyState(0x60 + 4) & 0x8000) != 0 || (User32.INSTANCE.GetAsyncKeyState(VK_LEFT) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    // Mover hacia la izquierda
                    if (columna > 1) {  // Limita el movimiento dentro del tablero
                        columna--;
                    }
                    teclaPresionada = true;
                }
            } else if ((User32.INSTANCE.GetAsyncKeyState(0x60 + 6) & 0x8000) != 0 || (User32.INSTANCE.GetAsyncKeyState(VK_RIGHT) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    // Mover hacia la derecha
                    if (columna < 6) {  // Limita el movimiento dentro del tablero
                        columna++;
                    }
                    teclaPresionada = true;
                }
            } else if ((User32.INSTANCE.GetAsyncKeyState(0x60 + 8) & 0x8000) != 0 || (User32.INSTANCE.GetAsyncKeyState(VK_UP) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    // Mover hacia arriba
                    if (fila > 1) {  // Limita el movimiento dentro del tablero
                        fila--;
                    }
                    teclaPresionada = true;
                }
            }
        }
    }

    public void opciones() {
        boolean teclaPresionada = false;
        while (true) {

            // Detecta la tecla 'ENTER' (Confirmar acción o decrementar)
            if ((User32.INSTANCE.GetAsyncKeyState(0x0D) & 0x8000) != 0) {  // VK_ENTER = 0x0D
                if (!teclaPresionada) {
                    // Llama a la función que maneja la acción cuando se presiona Enter
                    decrementarCasilla(fila, columna);  // Asegúrate de que fila y columna estén bien definidos
                    teclaPresionada = true;  // Marca que la tecla ha sido presionada
                }
            }
            // Detecta la tecla 'N' (Nuevo Juego)
            else if ((User32.INSTANCE.GetAsyncKeyState(0x4E) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    nuevoJuego(); // Llama a la función para iniciar un nuevo juego
                    teclaPresionada = true;
                }
            }
            // Detecta la tecla 'L' (Cambiar nivel)
            else if ((User32.INSTANCE.GetAsyncKeyState(0x4C) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    cambiarNivel(); // Llama a la función para preguntar por el nivel
                    teclaPresionada = true;
                }
            }
            // Detecta la tecla 'S' (Salir)
            else if ((User32.INSTANCE.GetAsyncKeyState(0x53) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    salir(); // Llama a la función para salir del juego
                    teclaPresionada = true;
                }
            }
            // Detecta la tecla 'U' (Deshacer acción)
            else if ((User32.INSTANCE.GetAsyncKeyState(0x55) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    deshacer(); // Llama a la función para deshacer
                    teclaPresionada = true;
                }
            }
            // Detecta la tecla 'R' (Reiniciar)
            else if ((User32.INSTANCE.GetAsyncKeyState(0x52) & 0x8000) != 0) {
                if (!teclaPresionada) {
                    reiniciarTablero(); // Llama a la función para reiniciar el juego
                    teclaPresionada = true;
                }
            }
            // Si no se presiona ninguna tecla, reseteamos la variable para detectar la siguiente tecla
            else {
                teclaPresionada = false;
            }

        }
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


            if (juego.tableroCero()) {
                juego.verificarTablero();
                break;


            } else {
                // Leer la tecla
                String tecla = scanner.nextLine().toUpperCase();

                // Realizar la acción correspondiente según la tecla
                switch (tecla) {

                    case "8": // Mover hacia arriba
                        if (juego.fila > 1) {
                            juego.fila--;
                            System.out.println("Corchete movido hacia arriba.");
                        }
                        break;
                    case "2": // Mover hacia abajo
                        if (juego.fila < 6) {
                            juego.fila++;
                            System.out.println("Corchete movido hacia abajo.");
                        }
                        break;
                    case "4": // Mover hacia la izquierda
                        if (juego.columna > 1) {
                            juego.columna--;
                            System.out.println("Corchete movido hacia la izquierda.");
                        }
                        break;
                    case "6": // Mover hacia la derecha
                        if (juego.columna < 6) {
                            juego.columna++;
                            System.out.println("Corchete movido hacia la derecha.");
                        }
                        break;
                    case "5": // Decrementar casilla
                        juego.resolver();
                        System.out.println("Casilla decrementada.");
                        break;
                    case "N": // Nuevo juego
                        juego.nuevoJuego();
                        System.out.println("Nuevo juego iniciado.");
                        break;
                    case "L": // Cambiar nivel
                        juego.cambiarNivel();
                        System.out.println("Nivel cambiado.");
                        break;
                    case "R": // Reiniciar juego
                        juego.reiniciarTablero();
                        System.out.println("Juego reiniciado.");
                        break;
                    case "U": // Deshacer acción
                        juego.deshacer();
                        System.out.println("Acción deshecha.");
                        break;
                    case "S": // Salir
                        System.out.println("Gracias por jugar.");
                        scanner.close();
                        return; // Termina el programa
                    default:
                        System.out.println("Tecla no válida, intenta de nuevo.");
                        break;
                }
            }
        }

    }
}






