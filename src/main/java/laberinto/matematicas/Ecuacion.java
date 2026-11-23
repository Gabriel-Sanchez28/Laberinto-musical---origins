package laberinto.matematicas;

public class Ecuacion {
    private double coeficiente;
    private double terminoIndependiente;
    private double resultado;

    public Ecuacion(double coeficiente, double terminoIndependiente, double resultado) {
        if (coeficiente == 0) {throw new IllegalArgumentException("El coeficiente no puede ser cero.");}
        this.coeficiente = coeficiente;
        this.terminoIndependiente = terminoIndependiente;
        this.resultado = resultado;
    }

    public double resolver() {return (resultado - terminoIndependiente) / coeficiente;}

    public String obtenerEnunciado() {
        return coeficiente + "x + " + terminoIndependiente + " = " + resultado;
    }

    public boolean verificarRespuesta(double respuesta) {return Math.abs(resolver() - respuesta) < 0.000001;}

    public double obtenerCoeficiente() {return coeficiente;}

    public double obtenerTerminoIndependiente() {return terminoIndependiente;}

    public double obtenerResultado() {return resultado;}
}
