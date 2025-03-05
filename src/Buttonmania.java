/*
        Tejedor
        Pérez,
        Diego
*/

import com.sun.jna.platform.win32.User32;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Buttonmania {

    Scanner sc = new Scanner(System.in);

    final int TAM = 8;       // Contante TAM de 8 huecos {0,1,2,3,4,5,6,7} pero solo mostraremos 6 {1,2,3,4,5,6}
    int guardado = 0;        // Variable que gestiona los arrays filaG y columnaG para el guardado de los movimientos para poder deshacerlos después
    int nivel = 5;           // Nivel en el que empieza el juego 5 o Normal (Ni muy fácil, ni muy difícil).
    int nuevoNivel;          // Variable que preguntamos al usuario, para evitar problemas de que se sobreescriba la variable nivel.
    int golpes = nivel * 3;  // 3 golpes por cada nivel usado.
    int golpesUsados = 0;    // Contador de golpes para la que se mostrará al usuario mientras resuelve el tablero.

    int[][] tablero = new int[TAM][TAM]; // Creamos la Matriz Tablero que tiene un tamaño de 8x8, todas las casillas se inicializan a cero.
    int[][] copia = new int[TAM][TAM];   // Creamos una Matriz copia que usaremos en las funciones reiniciar, por ejemplo.
    int fila=3;                          // Creamos la variable fila, que almacenará la fila donde dimos un golpe, necesaria en la creación y la resolución del tablero.
    int columna=4;                       // Creamos la variable columna, que almacenará la fila donde dimos un golpe, necesaria en la creación y la resolución del tablero.
    int[][] filaG = new int[1][1];       // Creamos la Matriz filaG que usarán las funciones resolver y deshacer, guardará las posiciones del array y las casillas modificadas.
    int[][] columnaG = new int[1][1];    // Creamos la Matriz columnaG que usarán las funciones resolver y deshacer, guardará las posiciones del array y las casillas modificadas.


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void incrementarCasilla(int fila, int columna) {   // Creamos la función de incrementar Casilla.
        if (tablero[fila][columna] == 3) {                    // Si la casilla tiene un valor de 3, el siguiente golpe la pondrá a cero.
            tablero[fila][columna] = 0;
        } else {
            tablero[fila][columna]++;                         // En caso contrario sumará 1.
        }                                                     // Como es una Matriz recibira 2 parametros, fila y columna.
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void decrementarCasilla(int fila, int columna) {   // Creamos la función decrementar Casilla, hará lo contrario que la anterior
        if (tablero[fila][columna] == 0) {                    // Si la casilla tiene un valor de 0, el siguiente golpe la pondrá a 3.
            tablero[fila][columna] = 3;
        } else {
            tablero[fila][columna]--;                         // En caso contrario restará 1.
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void aplicarGolpes(int golpes) {                 // Creamos la función para aplicar golpes.
        Random r = new Random();                            // Usaremos la clase Random para que elija las casillas de forma aleatoria.
        for (int i = 0; i < golpes; i++) {                  // Creamos un bucle para que de los golpes según el nivel elegido.
            fila = r.nextInt(1, TAM - 1);       // Hacemos que escoja la fila Random, con mucho cuidado de que los valores sean entre 1 y 6 (TAM - 1)
            columna = r.nextInt(1, TAM - 1);    // Lo mismo con las columnas
            incrementarCasilla(fila, columna);              // Utilizamos la función incrementar Casilla que coincida con fila y columna, ahora habrá que modificar las de alrededor

            if (fila > 0) incrementarCasilla(fila - 1, columna);                // Si la fila es 1 o mayor modificará la fila de arriba.
            if (fila < TAM - 1) incrementarCasilla(fila + 1, columna);          // Si la fila 6 o menor modificará la fila de abajo.
            if (columna > 0) incrementarCasilla(fila, columna - 1);         // Si la columna es 1 o mayor modificará la columna de arriba.
            if (columna < TAM - 1) incrementarCasilla(fila, columna + 1);   // Si la columna es 7 o menor modificará la columna de abajo.

        }
        for (int i = 0; i < TAM; i++) {         // Ya que estamos haremos una copia del tablero en caso de que queramos reinciar.
            for (int j = 0; j < TAM; j++) {     // La matriz copia obtendrá los valores de tablero.
                copia[i][j] = tablero[i][j];
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void resolver() {                                                    // Creamos la función resolver, que decrementará las casillas y guardará los movimientos.
        if (guardado == filaG[0].length) {                                      // Primero comprobamos si no tenemos espacio en el array, guardado tiene valor 0.
            int[][] auxFilaG = new int[filaG.length][filaG[0].length + 1];      // Creamos la matriz auxiliar auxFilaG que guardará las posiciones y los casillas de las filas
            for (int i = 0; i < filaG.length; i++) {
                for (int j = 0; j < filaG[i].length; j++) {
                    auxFilaG[i][j] = filaG[i][j];
                }
            }
            filaG = auxFilaG;
        }


        if (guardado == columnaG[0].length) {                                           // Hacemos lo mismo con las columnas

            int[][] auxColumnaG = new int[columnaG.length][columnaG[0].length + 1];
            for (int i = 0; i < columnaG.length; i++) {
                for (int j = 0; j < columnaG[i].length; j++) {
                    auxColumnaG[i][j] = columnaG[i][j];
                }
            }
            columnaG = auxColumnaG;
        }

        filaG[0][guardado] = fila;              // Guardamos la posición de la fila
        columnaG[0][guardado] = columna;        // Guardamos la posición de la columna

        guardado++;                             // Sumamos una posición más para que los arrays no esten fuera de parametros y podamos usarla en la función deshacer


        decrementarCasilla(fila, columna);                                          // Finalmente usamos la función decrementar en la casilla seleccionada y las cardinales a esta
        if (fila > 0) decrementarCasilla(fila - 1, columna);                    // Arriba
    if (fila < TAM - 1) decrementarCasilla(fila + 1, columna);                  // Abajo
        if (columna > 0) decrementarCasilla(fila, columna - 1);             // Izquierda
        if (columna < TAM - 1) decrementarCasilla(fila, columna + 1);       // Derecha
        golpesUsados++;                                                             // Sumamos 1 golpe en la resolución del nivel
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void deshacer() {                                                         // Crearemos la función deshacer
        if (guardado > 0) {                                                          // Primero comprobamos si hemos hecho guardados
            guardado--;                                                              // En caso positivo entraremos al if y se nos descontará un guardado (nunca puede ser menor que 1)
            fila = filaG[0][guardado];                                               // Automaticamente se nos colocará el corchete en la fila hicimos el guardado, independientemente de donde estemos
            columna = columnaG[0][guardado];                                         // Igual con las columnas

        incrementarCasilla(fila, columna);                                           // Restauramos la casilla y sus adyacentes (hacemos lo contrario que en 'resolver')
        if (fila > 0) incrementarCasilla(fila - 1, columna);                     // Arriba
            if (fila < TAM - 1) incrementarCasilla(fila + 1, columna);           // Abajo
            if (columna > 0) incrementarCasilla(fila, columna - 1);          // Izquierda
            if (columna < TAM - 1) incrementarCasilla(fila, columna + 1);    // Derecha

            golpesUsados--;                                                          // Decrementamos la cantidad de golpes usados al deshacer
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void reiniciarTablero() {                // Creamos la función reiniciarTablero (Como su nombre indica reiniciara el tablero a como se creo
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = copia[i][j];        // Para ello usamos la Matriz copia que sobreescribirá los valores de Tablero
            }
        }
        System.out.println("Tablero reiniciado");   // Mensaje del sistema para que se sepa que el tablero se ha reiniciado
        golpesUsados =0;                            // Reseteamos el contador de golpes a cero
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void nuevoJuego() {                      // Creamos la función nuevoJuego
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = 0;                  // Ponemos todas las casillas a cero
            }
        }
        aplicarGolpes(golpes);                      // Y aplicamos los golpes, que son aleatorios, como es el mismo nivel, ya sabemos la cantidad necesaria.
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void salir() {                           // Creamos la función salir
        System.out.println("Gracias por jugar");    // Damos las gracias al jugador
        System.exit(0);                      // Se cierra el programa
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // Creamos la función cambiar de nivel.
    public void cambiarNivel() {                            // Creamos la función cambiarNivel.
        System.out.println();                               // Con el Print sabe que tiene que ser un entero entre 1 y 9.
        System.out.println("\u001B[94m Elige la dificultad del nuevo juego (1-9): ");

        if (nivel != nuevoNivel) {                          // Si el nivel del juego es igual al que tiene el jugador no se hará nada, si es distinto entrará en el if y le preguntará que nivel quiere

            boolean nivelValido = false;                        // Inicializamos la variable boolean en false.

            while (!nivelValido) {                              // Creamos un bucle while la variable sea falsa.
                try {
                    nuevoNivel = sc.nextInt();                  // Damos la opción al usuario para que introduzca el nivel que quiere jugar.

                    if (nuevoNivel >= 1 && nuevoNivel <= 9) {   // Si el nivel está entre 1 y 9, ambos inclusive, la variable boolean se convierte en true y salimos del bucle.
                        nivelValido = true;                     // Si introduce 0, -1 ó 10, por ejemplo, el programa seguirá pidiendo que introduzca un número valido.
                    } else {
                        System.out.println("\u001B[91m Número fuera de rango. Tiene que ser un número entero entre 1 y 9");
                    }

                } catch (
                        InputMismatchException e) {            // En cambio si no introduce un entero y es cualquier otro simbolo saltará la Excepcion con otro mensaje algo distinto.
                    System.out.println("\u001B[91m Eso no es un número. Tiene que ser un número entero entre 1 y 9");
                    sc.nextLine();
                }
            }

            nivel = nuevoNivel;                             // Sobreescribimos el nivel.
            golpes = nivel * 3;                             // Con el nivel cambiado recalculamos los golpes.

            for (int i = 0; i < TAM; i++) {
                for (int j = 0; j < TAM; j++) {
                    tablero[i][j] = 0;                      // Seteamos el tablero a cero.
                }
            }
            aplicarGolpes(golpes);                          // Aplicamos golpes
            golpesUsados = 0;                               // Reseteamos los golpes dados a 0 también.


            for (int i = 0; i < TAM; i++) {
                for (int j = 0; j < TAM; j++) {
                    copia[i][j] = tablero[i][j];            // Como hemos generado un tablero nuevo de otro nivel volvemos a hacer la copia, por si queremos reiniciar
                }
            }                                                   // Avisamos al usuario del nivel del nuevo tablero
            System.out.println("\u001B[94m El nuevo juego tiene dificultad " + nuevoNivel);
        }
    }



    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void imprimirTablero() {                             // Creamos la función imprimirTablero, la cual nos permitira ver lo que está sucediendo
        String colorAmarillo = "\u001B[93m";                    // Variable para el color amarillo
        String colorAzulClaro = "\u001B[94m";                   // Color azul claro
        String quitarColor = "\u001B[0m";                       // Para resetear el color

        for (int i = 0; i < (TAM - 1) * 4 + 1; i++) {
            System.out.print(colorAmarillo + "-" + quitarColor);// Imprimimos los guiones de la primera fila
        }
        System.out.println();

        for (int i = 1; i < TAM - 1; i++) {
            System.out.print(colorAmarillo + "|" + quitarColor); // Imprimimos los guiones que separaran las columnas

            for (int j = 1; j < TAM - 1; j++) {                  // Imprimimos las casillas del tablero
                if (i == fila && j == columna) {
                    System.out.print(colorAmarillo + "[" + tablero[i][j] + "]" + quitarColor);  // La casilla seleccionada tendrá color amarillo
                } else {
                    System.out.print(colorAzulClaro + " " + tablero[i][j] + " " + quitarColor); // Mientras que el resto de casillas tendrá color azul
                }

                if (j < TAM - 2) {
                    System.out.print(colorAmarillo + "|" + quitarColor);  // Imprimimos los separadores entre columnas
                }
            }

            System.out.println();

            if (i < TAM - 2) {                                             // Imprimimos los guiones despues de cada fila
                for (int j = 0; j < (TAM - 1) * 4 + 1; j++) {
                    System.out.print(colorAmarillo + "-" + quitarColor);
                }
                System.out.println();
            }
        }


        for (int i = 0; i < (TAM - 1) * 4 + 1; i++) {                       // Al igual que al principio imprimimos la fila de guiones de cierre
            System.out.print(colorAmarillo + "-" + quitarColor);
        }
        System.out.println();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public boolean tableroCero() {                  // Creamos la función booleana tableroCero que verificará si las casillas entre 1 y 6 (6x6) las que se ven han llegado a cero
        for (int i = 1; i < TAM - 1; i++) {
            for (int j = 1; j < TAM - 1; j++) {
                if (tablero[i][j] != 0) {
                    return false;                   // Mientras haya alguna distinta nos devolverá false
                }
            }
        }
        return true;
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void verificarTablero() {                // Creamos la función verificarTablero
        if (tableroCero()) {                        // Primero necesitamos confirmar que la función tableroCero es positiva
            if (golpesUsados < golpes) {            // Comprobaremos la cantidad de golpes en las 3 posibilidades Menor / Igual / Mayor
                System.out.println();               // En todos ellos el jugador recibirá un mensaje de lo bien o mal que lo hizo y los golpes que uso
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
        nuevoJuego();                               // Una vez recibida el mensaje se generará un tablero automatico del mismo nivel que resolvió
        golpesUsados = 0;                           // Y los golpes se resetearán a cero igualmente.
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {

        Buttonmania juego = new Buttonmania();      // Creamos un objeto nuevo de la clase Buttonmania
        Scanner scanner = new Scanner(System.in);   // Creamos un Scanner
        juego.nuevoJuego();                         // Iniciamos un juego nuevo, de nivel 5, puesto que inicializamos la variable en ese nivel

        while (true) {    // Creamos un bucle infinito
            juego.imprimirTablero(); // Imprimimos el tablero con los golpes ya aplicados

            // Mostramos por pantalla las opciones al jugador

            System.out.println("\u001B[92m Opciones: Nuevo (N) - Reiniciar (R) - Deshacer (U) - Salir (S) - Cambiar Nivel (L)");
            System.out.println();

            // Mostramos las teclas necesarias para que el jugador pueda finalizar el tablero.

            System.out.println("\u001B[93m Movimientos: Arriba (8) - Derecha (6) - Izquierda (4) - Abajo (2) - Pulsar (5) - Aceptar (ENTER)");
            System.out.println();

            // Mostramos el nivel del juego así como los golpes usados y los golpes necesarios para resolver el tablero.

            System.out.println("\u001B[95m Nivel de juego: " + juego.nivel);
            System.out.println("\u001B[95m Golpes restantes: " + juego.golpesUsados + " / " + juego.golpes);
            System.out.println();

            // Finalmente mostramos los Objetivos y las Instrucciones al usuario.

            System.out.println("\u001B[97m Objetivo: Dejar todos los botones en '0'.");
            System.out.println("\u001B[97m Instrucciones: Al pulsar se reducirá la casilla seleccionada y sus cuatro cardinales (Arriba, Izquierda, Derecha y Abajo).");


            if (juego.tableroCero()) {                  // Si todas las casillas entre 1 y 6 son cero entrará dentro del if.
                juego.verificarTablero();               // Entonces verificará que se haya completada y procederá a dar los resultados.

            } else {                                    // En caso de que no este resuelto el tablero
                // Leer la tecla
                String tecla = scanner.nextLine().toUpperCase(); // Leera las teclas necesarias ya sean los numeros o las letras que se pondrán automaticamente en mayusculas

                switch (tecla) {

                    case "8":                       // Mover hacia arriba
                        if (juego.fila > 1) {       // Si la fila es mayor que 1 se reducirá en 1, así evitamos que el corchete se pierda por la pantalla
                            juego.fila--;
                            System.out.println("\033[1;36m Corchete movido hacia arriba.");
                        }
                        break;
                    case "2":                       // Mover hacia abajo
                        if (juego.fila < 6) {       // Si la fila es menor que 6 se sumará en 1, así evitamos que el corchete se pierda por la pantalla
                            juego.fila++;
                            System.out.println("\033[1;36m Corchete movido hacia abajo.");
                        }
                        break;
                    case "4":                       // Mover hacia la izquierda
                        if (juego.columna > 1) {    // Si la columna es mayor que 1 se reducirá en 1, así evitamos que el corchete se pierda por la pantalla
                            juego.columna--;
                            System.out.println("\033[1;36m Corchete movido hacia la izquierda.");
                        }
                        break;
                    case "6":                       // Mover hacia la derecha
                        if (juego.columna < 6) {    // Si la columna es menor que 6 se sumará en 1, así evitamos que el corchete se pierda por la pantalla
                            juego.columna++;
                            System.out.println("\033[1;36m Corchete movido hacia la derecha.");
                        }
                        break;
                    case "5":                       // Decrementar casilla
                        juego.resolver();           // Para poder resolver el tablero
                        System.out.println("\033[1;36m Casilla decrementada.");
                        break;
                    case "N":                       // Nuevo juego
                        juego.nuevoJuego();
                        System.out.println("\033[1;36m Nuevo juego iniciado.");
                        break;
                    case "L":                       // Cambiar nivel
                        juego.cambiarNivel();
                        System.out.println("\033[1;36m Nivel cambiado.");
                        break;
                    case "R":                       // Reiniciar juego
                        juego.reiniciarTablero();
                        System.out.println("\033[1;36m Juego reiniciado.");
                        break;
                    case "U":                       // Deshacer acción
                        juego.deshacer();
                        if (juego.golpesUsados > 0) {// Comprobamos que haya la posibilidad de deshacer en cada caso aparecerá un mensaje
                            System.out.println("\033[1;36m Acción deshecha.");
                        }else {
                            System.out.println("\033[1;36m No hay movimientos que deshacer");
                        }
                        break;
                    case "S":                       // Salir
                        System.out.println("\033[1;36m Gracias por jugar.");
                        juego.salir();
                        scanner.close();
                        return;                     // Termina el programa
                    default:                        // En caso de que no se seleccione ninguna de las opciones del Switch aparecerá la opción como no valida
                        System.out.println("\033[1;36m Tecla no válida, intenta de nuevo.");
                        break;
                }
            }
        }

    }
}
