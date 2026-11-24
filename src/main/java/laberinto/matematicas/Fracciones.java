package laberinto.matematicas;

public class Fracciones {
    private int numerador;
    private int denominador;

    public Fracciones(int numerador, int denominador) {
        if (denominador == 0) {throw new IllegalArgumentException("El denominador no puede ser cero.");}
        this.numerador = numerador;
        this.denominador = denominador;
        simplificar();
    }

    public int getNumerador() {return numerador;}

    public int getDenominador() {return denominador;}

    public void simplificar() {
        int a = Math.abs(numerador);
        int b = Math.abs(denominador);
        while (b != 0) {
            int residuo = a % b;
            a = b;
            b = residuo;
        }
        if (a != 0) {
            numerador = numerador / a;
            denominador = denominador / a;
        }
        if (denominador < 0) {
            numerador = -numerador;
            denominador = -denominador;
        }
    }

    public Fracciones sumar(Fracciones otra) {
        int nuevoNumerador = numerador * otra.denominador + otra.numerador * denominador;
        int nuevoDenominador = denominador * otra.denominador;
        return new Fracciones(nuevoNumerador, nuevoDenominador);
    }

    public Fracciones restar(Fracciones otra) {
        int nuevoNumerador = numerador * otra.denominador - otra.numerador * denominador;
        int nuevoDenominador = denominador * otra.denominador;
        return new Fracciones(nuevoNumerador, nuevoDenominador);
    }

    public Fracciones multiplicar(Fracciones otra) {
        int nuevoNumerador = numerador * otra.numerador;
        int nuevoDenominador = denominador * otra.denominador;
        return new Fracciones(nuevoNumerador, nuevoDenominador);
    }

    public Fracciones dividir(Fracciones otra) {
        if (otra.numerador == 0) {throw new ArithmeticException("No se puede dividir entre cero.");}
        int nuevoNumerador = numerador * otra.denominador;
        int nuevoDenominador = denominador * otra.numerador;
        return new Fracciones(nuevoNumerador, nuevoDenominador);
    }

    @Override
    public String toString() {return numerador + "/" + denominador;}
}
