import java.text.DecimalFormat;
/*
 *  -Funcion utilizada para la minimización: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2
 */
class Metodos {
    DecimalFormat dec = new DecimalFormat("#.###");
    DataSet data = new DataSet();

    //Mostrar matriz pasada por parametro en la consola
    void Array(double []array){
        for(int i=0;i<array.length;i++){
            System.out.println(dec.format(array[i]));
        }
    }
    //Mostrar matriz pasada por parametro en consola
    void Matrix(double [][]matrix){
        for(int row=0;row<matrix.length;row++){
            for(int column=0;column<matrix[0].length;column++){
                System.out.print(dec.format(matrix[row][column])+"\t");
            }
            System.out.println("");
        }
    }
    /*
        PASO 1 - INICIALIZACION
        * INICIALIZAR PARAMETROS
            -Número de variables (x1, x2, x3 ... x^n-1)
            -Poblacion = Cantidad de particulas (xN_i, xN_i+1, xN_i+1 ... xN_i-1)
        ** INICIALIZAR POBLACION (x_i) aleatoriamente para cada particula
            -Inicializar velocidad (v_i) aleatoriamente para cada particula
            -Inicializar posicion
                -Establecer mejor marca personal actual = Inicializar posicion
     */
    // *INICIALIZAR PARAMETROS
    //-El numero de variables y la poblacion se declararon en la clase 'DataSet'.
    double c1=2, c2=2;          //-valores constantes (factor de aceleracion)
    double w = Math.random();   //-Valor de inercia entre[0,1]
    int itera = 50;       //-Iteraciones máximas
    // **INICIALIZAR POBLACIoN
    double [][] initPopulation(){
        double[][] x_values = new double[data.particulas][data.v];
        //Normalmente, la poblacion se declara aleatoriamente, pero en este caso se codifico con el siguiente conjunto de datos:
        double [][] dataSet = {{41.9,  43.4,  43.9,  44.5,  47.3,  47.5,  47.9,  50.2,  52.8,  53.2,  56.7,  57.0,  63.5,  65.3,  71.1,  77.0,  77.8 },  //x1
                {29.1,  29.3,  29.5,  29.7,  29.9,  30.3,  30.5,  30.7,  30.8,  30.9,  31.5,  31.7,  31.9,  32.0,  32.1,  32.5,  32.9 },  //x2
                {251.3, 251.3, 248.3, 267.5, 273.0, 276.5, 270.3, 274.9, 285.0, 290.0, 297.0, 302.5, 304.5, 309.3, 321.7, 330.7, 349.0}}; //x3
        //instruccion "for" para obtener la transposicion de la matriz de ${x valores} y guardarla en la matriz ${matrix aux}
        for(int r=0;r<dataSet.length;r++){
            for(int column=0;column<dataSet[0].length;column++){
                x_values[column][r] = dataSet[r][column];
            }
        }
        return x_values; //La poblacion ha sido inicializada
    }
    //-velocidad de inicializacion
    double [][] IniciVel(){
        double [][]velo = new double[data.particulas][data.v];
        for(int row=0;row<velo.length;row++){
            for(int col=0;col<velo[0].length;col++){
                //Valor aleatorio entre [0,1]
                double r = Math.random();
                velo[row][col]=r;
            }
        }
        return velo; //La velocidad ha sido inicializada
    }
    //-Inicializar posicion (x_i) <--- Posicion actual = Posicion anterior (poblacion en la primera iteracion) + velocidad
    double [][] initPosition(double [][]population, double [][]velocity){
        for(int row=0;row<population.length;row++){
            for(int col=0;col<population[0].length;col++){
                population[row][col]+=velocity[row][col];
            }
        }
        return population; //La posicion ha sido inicializada
    }
    //Inicialice "Personal Best" con valores de primera posicion
    double [][] iniPersoB(double [][]position){
        double [][]persoB = new double [data.particulas][data.v];
        for(int row=0;row<position.length;row++){
            for(int col=0;col<position[0].length;col++){
                persoB[row][col]=position[row][col];
            }
        }
        return persoB;
    }
    /*
     *FIN DEL PASO 1 - INICIALIZACION
     */
    /*
       PASO 2 - EVALUAR LA CONDICION FISICA
        * CALCULAR EL VALOR DE APTITUD PARA CADA PARTICULA
            -Funcion objetivo utilizada para la minimizacion: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2
        ELIJA LA PARTICULA CON EL MEJOR VALOR DE CONDICION FISICA COMO 'Global Best'
            -Establezca 'Mejor global' en x1, x2, x3 ... xN-1
    */
    //* CALCULE EL VALOR DE APTITUD PARA CADA PARTICULA
    //-Funcion objetivo utilizada para la minimizacion: f(x) = 10*(x1 - 1)^2 + 20*(x2-2)^2 + 30*(x3-3)^2
    double[] FitnessFuntion(double [][]x_values){
        double fitnessVal[]=new double[data.particulas];
        for(int r = 0; r<fitnessVal.length;r++){
            fitnessVal[r]= 10*(Math.pow(x_values[r][0] - 1, 2)) + (20*(Math.pow(x_values[r][1] - 2, 2))) + (30*(Math.pow(x_values[r][2]-3,2)));
        }
        return fitnessVal;
    }
    //** ELIJA LA PARTICULA CON MEJOR VALOR DE APTITUD COMO 'Global Best'
    double fitnessValue(double []fitnessValue){
        double bestFitnessValue=fitnessValue[0];
        for(int i = 1; i<fitnessValue.length;i++){
            if(fitnessValue[i]<bestFitnessValue){
                bestFitnessValue=fitnessValue[i];
            }
        }
        return bestFitnessValue;
    }
    //-Establezca 'Mejor global' en x1, x2, x3 ... xN-1
    double[] GlobalBest(double []fitnessFuntion, double fitnessValue, double [][]x_positions){
        int position=0;
        double []globalBest= new double[data.v];
        for(int i=0;i<fitnessFuntion.length;i++){
            if(fitnessValue==fitnessFuntion[i]){
                position=i;
            }
        }
        for(int r=0;r<globalBest.length;r++){
            globalBest[r]=x_positions[position][r];
        }
        return globalBest;
    }
    /*
     * FIN DEL PASO 2 - EVALUAR LA CONDICION FISICA
     */
    /*
        PASO 3 - PARA CADA PARTICULA CALCULE LA VELOCIDAD Y LA POSICION
        * CALCULAR LA VELOCIDAD POR: Vi^t+1 = W*V^t + C1*R1(pBest - Xi^t) + C2R2(gBest - Xi^t)
        ** CALCULAR LAS POSICIONES DE LAS PARTICULAS POR: X^t+1 = X^t + V^t
    */
    //* ACTUALIZAR VELOCIDAD POR: Vi^t+1 = W*V^t + C1*R1(pBest - Xi^t) + C2R2(gBest - Xi^t)
    double[][] updateVel(double [][]velocity, double [][]x_position, double [][]pBest, double []gBest){
        double ram=Math.random();
        for(int r = 0; r<velocity.length;r++){
            for(int col=0;col<velocity[0].length;col++){
                velocity[r][col] = (w*velocity[r][col]) +
                        ((c1*r)*((pBest[r][col]) - (x_position[r][col]))) +
                        ((c2*r)*((gBest[col])      - (x_position[r][col])));
            }
        }
        return velocity;
    }
    //** CALCULE LAS POSICIONES DE LAS PARTICULAS POR: X^t+1 = X^t + V^t
    double[][] updatePos(double[][]velocity, double[][]x_values){
        for(int r = 0; r<velocity.length;r++){
            for(int col=0;col<velocity[0].length;col++){
                x_values[r][col]+=velocity[r][col];
            }
        }
        return x_values;
    }
    /*
     *   FIN DEL PASO 3 - PARA CADA PARTICULA CALCULAR LA VELOCIDAD Y LA POSICION
     */
    /*
        PASO 4 - ACTUALIZAR 'GLOBAL BEST' Y 'PERSONAL BEST'
        COMPARE EL VALOR MIN DE APTITUD ACTUAL CON EL NUEVO VALOR DE APTITUD MIN Y ELIJA EL MEJOR
        ACTUALIZAR MEJOR GLOBAL
        ACTUALIZAR LO MEJOR PERSONAL
     */
    //*  COMPARAR EL VALOR MIN ACTUAL DE APTITUD VS NUEVO VALOR MIN DE APTITUD Y ELEGIR EL MEJOR
    double updateFitnessVal(double minFitnessValue, double[] fitnessFuntion){
        for(int i=0;i<fitnessFuntion.length;i++){
            if(fitnessFuntion[i] < minFitnessValue){
                minFitnessValue=fitnessFuntion[i];
            }
        }
        return minFitnessValue;
    }
    //*** ACTUALIZAR MEJOR PERSONAL
    double[][] updatePersoBest(double [][]x_position, double[][]persoBest, double []fitness, double []currentFitnessFuntion){
        for(int r=0;r<fitness.length;r++){
            if(currentFitnessFuntion[r]<fitness[r]){
                for(int col=0;col<persoBest[0].length;col++){
                    persoBest[r][col]=x_position[r][col];
                }
            }
        }
        return persoBest;
    }
    /*
     *   FIN DEL PASO 4 - ACTUALIZAR 'GLOBAL BEST' Y 'PERSONAL BEST'
     */
}
