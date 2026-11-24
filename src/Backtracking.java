
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Backtracking {

    /*
     * Creamos un metodo para intercambiar las posiciones
     * de los elementos de la matriz.
     */
    public static void intercambiar(String[][] matriz, int i, int j, int I, int J) {

        /*
         * Creamos una variable auxiliar para guardar
         * el valor anterior y no perderlo.
         */
        String matrizRetornable = matriz[i][j];

        matriz[i][j] = matriz[I][J];
        matriz[I][J] = matrizRetornable;
    }

    /*
     * Comprobar si la matriz esta ordenada.
     */
    public static boolean estaOrdenado(String[][] matriz) {

        int numero = 1;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {

                if (i == 2 && j == 2) {
                    return matriz[i][j].equals("0");
                }

                if (!matriz[i][j].equals(String.valueOf(numero))) {
                    return false;
                }

                numero++;
            }
        }

        return true;
    }

    /*
     * Mostrar los elementos de la matriz.
     */
    public static void mostrarMatriz(String[][] matriz) {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    /*
     * Comprobar que el archivo tenga un tablero valido.
     */
    public static boolean tableroValido(String[][] matriz) {

        boolean[] encontrados = new boolean[9];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {

                String valor = matriz[i][j];

                if (valor.length() != 1 ||
                    valor.charAt(0) < '0' ||
                    valor.charAt(0) > '8') {
                    return false;
                }

                int numero = Integer.parseInt(valor);

                if (encontrados[numero]) {
                    return false;
                }

                encontrados[numero] = true;
            }
        }

        return true;
    }

    /*
     * Comprobar si el tablero tiene solucion.
     */
    public static boolean tieneSolucion(String[][] matriz) {

        int inversiones = 0;

        for (int i = 0; i < 9; i++) {
            int a = Integer.parseInt(matriz[i / 3][i % 3]);

            if (a == 0) {
                continue;
            }

            for (int j = i + 1; j < 9; j++) {
                int b = Integer.parseInt(matriz[j / 3][j % 3]);

                if (b != 0 && a > b) {
                    inversiones++;
                }
            }
        }

        return inversiones % 2 == 0;
    }

    /*
     * Metodo recursivo para resolver el tablero
     * utilizando Backtracking.
     */
    public static boolean resolver(String[][] matriz, int profundidad, int limite, String anterior,ArrayList<String> movimientos) {

        //Caso base: comprobar si el tablero esta ordenado
        if (estaOrdenado(matriz)) {
            return true;
        }

        //Evitar que la busqueda supere el limite de movimientos
        if (profundidad == limite) {
            return false;
        }

        //Encontrar la posicion del cero
        int fila = 0;
        int columna = 0;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {

                if (matriz[i][j].equals("0")) {
                    fila = i;
                    columna = j;
                }
            }
        }

        //Movimientos: arriba, abajo, izquierda, derecha
        int[] movimientosI = {-1, 1, 0, 0};
        int[] movimientosJ = {0, 0, -1, 1};

        String[] nombres = { "ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA" };

        for (int k = 0; k < 4; k++) {

            //Evitar regresar inmediatamente al tablero anterior
            if ((anterior.equals("ARRIBA") && k == 1) ||
                (anterior.equals("ABAJO") && k == 0) ||
                (anterior.equals("IZQUIERDA") && k == 3) ||
                (anterior.equals("DERECHA") && k == 2)) {
                continue;
            }

            int nuevaI = fila + movimientosI[k];
            int nuevaJ = columna + movimientosJ[k];

            //Comprobar que el movimiento sea valido
            if (nuevaI >= 0 && nuevaI < matriz.length &&
                nuevaJ >= 0 && nuevaJ < matriz[nuevaI].length) {

                //Mover el cero
                intercambiar(matriz, fila, columna, nuevaI, nuevaJ);

                //Guardar el movimiento realizado
                movimientos.add(nombres[k]);

                //Probar recursivamente
                if (resolver(matriz, profundidad + 1, limite,
                             nombres[k], movimientos)) {
                    return true;
                }

                //Backtracking: deshacer el movimiento
                movimientos.remove(movimientos.size() - 1);
                intercambiar(matriz, nuevaI, nuevaJ, fila, columna);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        /*
         *
         * Prompt IA: Como puedo leer archivos multiples
         * (entre 3-4 archivos disponibles en la carpeta raiz)
         * y que uno sea seleccionado aleatoriamente?
         */

        //Lee los archivos (tablero1, tablero2 y tablero3)
        String[] archivos = {"src/Files/tablero1.txt","src/Files/tablero2.txt","src/Files/tablero3.txt"}; //!!Falta agregar tableroimposible

        Random random = new Random(); //Selecciona un archivo aleatoriamente
        int x = random.nextInt(archivos.length);

        try {
            File archivo = new File(archivos[x]);
            Scanner lector = new Scanner(archivo);

            System.out.println("Leyendo: " + archivos[x]);

            //Transformar el archivo en una matriz
            String[][] matriz = new String[3][3];

            try {
                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[i].length; j++) {
                        matriz[i][j] = lector.next();
                    }
                }

                //Comprobar que no sobren datos
                if (lector.hasNext()) {
                    System.out.println("Error: el archivo contiene datos adicionales");
                    return;
                }

            } catch (java.util.NoSuchElementException e) {
                System.out.println("Error: el archivo tiene datos incompletos");
                return;

            } finally {
                lector.close();
            }

            //Comprobar el formato del tablero
            if (!tableroValido(matriz)) {
                System.out.println("Error: el tablero debe contener los numeros del 0 al 8 sin repetir");
                return;
            }

            //Mostrar el tablero inicial
            System.out.println("Tablero inicial:");
            mostrarMatriz(matriz);

            //Recorrer la matriz hasta encontrar los indices[i][j] que contengan a (0)
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {

                    if (matriz[i][j].equals("0")) {
                        System.out.println("Cero encontrado en: [" + i + "][" + j + "]");
                    }
                }
            }

            //Comprobar si el tablero tiene solucion
            if (!tieneSolucion(matriz)) {
                System.out.println("El tablero no tiene solucion");
                return;
            }

            //Aplicar Backtracking
            ArrayList<String> movimientos = new ArrayList<>();
            boolean solucion = false;

            //Probar soluciones aumentando el limite de movimientos
            for (int limite = 0; limite <= 31 && !solucion; limite++) {
                movimientos.clear();
                solucion = resolver(matriz, 0, limite, "", movimientos);
            }

            if (solucion) {
                System.out.println("\nTablero resuelto");

                //Mostrar la secuencia de movimientos
                System.out.println("Movimientos realizados:");
                System.out.println(String.join(" -> ", movimientos));

                //Mostrar el numero total de pasos
                System.out.println("Total de pasos: " + movimientos.size());

                //Mostrar el tablero final
                System.out.println("Tablero final:");
                mostrarMatriz(matriz);

            } else {
                System.out.println("No se encontro solucion dentro del limite");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + archivos[x]);
        }
    }
}
