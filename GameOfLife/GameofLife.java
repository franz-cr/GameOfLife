
/**
 * GameofLife: Open 'Readme.txt' for details.
 *
 * @author Franz J. Ulrich
 * @version 1.0
 */
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameofLife
{
    // Instance variables - replace the example below with your own
    private int x, y;
    private int underPopulation = 2;
    private int liveLimit = 3;
    private int colonia = 0;
    private char[][] world;
    Random generator;
    // Define el factor de llenado de la matriz
    double densidad = 0.300;    

    /**
     * Constructor for objects of class GameofLife
     */
    public GameofLife(int wide, int height)
    {
        // initialise instance variables
        this.x = wide;
        this.y = height;
        this.generator = new Random();
        //generator.setSeed(42);
        world = new char[wide][height];
    }

    public static void main(String[] args) throws InterruptedException {
        //Instantiate class
        
        // Variables locales
        // xMax = 15, yMax = 21 - Cuadrado
        int value = 0, gens = 10, xMax = 15, yMax = 25;
        boolean validNum = false, isManualRun = false;
        String opciones = "";
        
        Scanner in = new Scanner(System.in);
        
        // Capturar parámetros de la simulación
        do {
            System.out.println("Indique el tamaño del mundo: ");
            System.out.println("1) 15 x 21");
            System.out.println("2) 21 x 21");
            System.out.println("3) 15 x 25");
            System.out.print("> ");
            //while (in.hasNextInt()) {
                value = in.nextInt();
                if (value >= 1 && value <= 3)
                    validNum = true;
            //}
            System.out.print('\f');
            System.out.println();
        } while (!validNum);
        validNum = false;
        if (value == 1) {
            xMax = 15;
            yMax = 21;
        }
        else if (value == 2) {
            xMax = 21;
            yMax = 21;
        }
        GameofLife game = new GameofLife(xMax, yMax);

        do {
            System.out.println("Indique la cantidad de generaciones (entre 5 y 100): ");
            System.out.print("> ");
            gens = in.nextInt();
            if (gens >= 5 && gens <= 100)
                validNum = true;
            System.out.print('\f');
            System.out.println();
        } while (!validNum);
        validNum = false;

        do {
            System.out.println("Seleccione el tipo de corrida: ");
            System.out.println("1) Manual.");
            System.out.println("2) Automática.");
            System.out.print("> ");
            value = in.nextInt();
            if (value >= 1 && value <= 2)
                validNum = true;
            System.out.print('\f');
            System.out.println();
        } while (!validNum);
        validNum = false;
        if (value == 1)
            isManualRun = true;
        else
            isManualRun = false;
        
        // Definir la colonización inicial
        game.coloniaInicial(1);
        
        for (int g = 0; g <= gens; g++) {
            System.out.println("Generación < " + g + " >:");
            game.mostrarMatriz();
            System.out.printf("Colonia: %3d individuos.\n", game.contarCelulas());
            
            // Muestra vecindades ejemplo si el usuario indica '1'.
            if (opciones == "1") {
                game.qtyVecinos(0, 0);
                game.qtyVecinos(xMax - 1, 0);
                game.qtyVecinos(2, 3);
                game.qtyVecinos(6, 10);
                game.qtyVecinos(0, yMax - 1);
                game.qtyVecinos(xMax - 1, yMax - 1);
            }

            if (g < gens) {
                System.out.println();
                if (isManualRun) {
                    System.out.println("Enter any key to continue next generation ...");
                    System.out.println("1) Evaluar coordenadas");
                    System.out.print("> "); 
                    opciones = in.next();
                }
                else
                    TimeUnit.SECONDS.sleep(1);
                System.out.print("\f");
                System.out.println();
            }
            game.crearGeneracion();
        }
    }
    
    /**
     * coloniaInicial - Coloca la colonia inicial en el mundo.
     *
     * @param   patron: el tipo de patrón a utilizar para la primera colonia de 5 células
     * @return  ninguno
     */
    public void coloniaInicial(int patron) {
        
        //return x;
        for (int j = 0; j < this.y; j++) {
            for (int i = 0; i < this.x; i++) {
                if (generator.nextDouble() >= (1 - densidad)) 
                    this.world[i][j] = 'H';
                else
                    this.world[i][j] = ' ';
            }
        }
    }
    
    /**
     * crearGeneracion - Recorre las celdas de mundo y cuenta los vecinos de cada una. 
     * Marca con 'D' los individuos que no van a sobrevivir a la nueva generación y
     * con 'C' aquellos que nacen en la nueva generación.
     *
     * @param   Ninguno.
     * @return  Ninguno.  
     */    
    public void crearGeneracion() {
        // Variable local
        int vecinos = 0;
        
        for (int j = 0; j < this.y; j++) {
            for (int i = 0; i < this.x; i++) {
                vecinos = qtyVecinos(i, j);
                if (this.world[i][j] == ' ') {
                    if (vecinos == liveLimit)
                        this.world[i][j] = 'C';
                }
                else if (this.world[i][j] == 'H') {
                    if (vecinos < underPopulation || vecinos > liveLimit)
                        this.world[i][j] = 'D';
                }
            }
        }
        limpiarGeneracion();
    }
    
    /**
     * limpiarGeneracion - Limpia los individuos muertos y agrega los recién nacidos.
     *
     * @param   Ninguno.
     * @return  Ninguno.  
     */        
    public void limpiarGeneracion() {
        for (int j = 0; j < this.y; j++) {
            for (int i = 0; i < this.x; i++) {
                if (this.world[i][j] == 'D') {
                    this.world[i][j] = ' ';
                }
                else if (this.world[i][j] == 'C') {
                    this.world[i][j] = 'H';
                }
            }
        }
    }
    
    public int contarCelulas() {
        
        int colonia = 0;
        
        for (int j = 0; j < this.y; j++) {
            for (int i = 0; i < this.x; i++) {
                if (this.world[i][j] == 'H') {
                    colonia++;
                }
            }
        }
        
        return colonia;
    }
    
    /**
     * qtyVecinos - Evalúa la cantidad de vecinos que rodean la celda dada.
     *
     * @param   xCoord - La coordena en X de la celda a evaluar.
     * @param   yCoord - La coordena en y de la celda a evaluar.
     * @return  Retorna la cantidad de vecinos encontrados.  
     */
    public int qtyVecinos(int xCoord, int yCoord) {

        int vecinos = 0;
        int xStr = xCoord - 1;
        int yStr = yCoord - 1;
        int xEnd = xCoord + 1;
        int yEnd = yCoord + 1;
        
        // Define los límites en X de la submatriz a evaluar
        if (xStr < 0)
            xStr = 0;
        
        if (xEnd > (this.x - 1))
            xEnd = this.x - 1;
        
        // Define los límites en X de la submatriz a evaluar
        if (yStr < 0)
            yStr = 0;
        
        if (yEnd > (this.y - 1))
            yEnd = this.y - 1;
            
        // Recorre la vecindad de la célula
        for (int j = yStr; j <= yEnd; j++)
            for (int i = xStr; i <= xEnd; i++)
                if (!(i == xCoord && j == yCoord))
                    if (this.world[i][j] == 'H' || this.world[i][j] == 'D')
                        vecinos++;

        // Pruebas de establecimiento de límites
        //System.out.printf("Celda: %2d,%2d; Ini(%2d,%2d); Fin(%2d,%2d); V: %d\n", 
        //    xCoord, yCoord, xStr, yStr, xEnd, yEnd, vecinos);
        return vecinos;
    }
    
    /**
     * mostrarMatriz - Despliega la matriz en pantalla en un formato dado.
     *
     * @param   Ninguno.
     * @return  Ninguno.  
     */
    public void mostrarMatriz() {
        
        // Construye numeración horizontal
        System.out.print("    |");
        for (int i = 0; i < this.x; i++) 
            System.out.printf("%3d", i); 
        System.out.println("  |");
        
        // Construye el borde horizontal
        String strBorde = "----+";
        for (int l = 0; l < (this.x * 3 + 2); l++)
            strBorde += "-";
        strBorde += "+";
        
        // Despliega el borde superior
        System.out.println(strBorde);
        
        // Despliega el contentido de la matriz
        for (int j = 0; j < this.y; j++) {
            System.out.printf("%3d |", j);
            for (int i = 0; i < this.x; i++) 
                System.out.printf("%3s", this.world[i][j]);
            System.out.println("  |");
        }
        
        // Despliega el borde inferior
        System.out.println(strBorde);

    }
}
