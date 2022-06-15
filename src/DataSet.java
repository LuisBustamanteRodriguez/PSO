public class DataSet {
    int particulas = 17;  //Cantidad de particulas (poblacion) (en x_i, x_i+1, x_i+1 ... x_i-1)
    int v =3;  // Cantidad de variables en la matriz (x1, x2, x3 ... Xn-1)

    private
    double [][] x_val =new double [particulas][v];
    private double [][] vel =new double [particulas][v];

    private double [] fitness = new double[particulas];   //Array para guardar el valor resultado de la funcion utilizando partículas en x1, x2, x3... Xn-1
    private double [][] personalBest = new double[particulas][v]; //Matriz para guardar los valores de 'Mejores valores personales'
    private double [] globalBest = new double[v];
    private double minFitnessValue;

    public double getMinFitnessValue() {
        return minFitnessValue;
    }

    public void setMinFitnessValue(double minFitnessValue) {
        this.minFitnessValue = minFitnessValue;
    }

    public double[] getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(double[] globalBest) {
        this.globalBest = globalBest;
    }


    public double[][] getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(double[][] personalBest) {
        this.personalBest = personalBest;
    }

    public double[][] getX_val() {
        return x_val;
    }

    public void setX_val(double[][] x_val) {
        this.x_val = x_val;
    }

    public double[][] getVel() {
        return vel;
    }

    public void setVel(double[][] vel) {
        this.vel = vel;
    }

    public double[] getFitness() {
        return fitness;
    }

    public void setFitness(double[] fitness) {
        this.fitness = fitness;
    }
}

