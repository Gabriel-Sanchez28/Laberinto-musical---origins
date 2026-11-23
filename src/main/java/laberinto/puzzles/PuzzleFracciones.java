package laberinto.puzzles;

import laberinto.matematicas.Fracciones;

public class PuzzleFracciones {
    private Fracciones primera;
    private Fracciones segunda;
    private String operacion;
    private Fracciones resultado;

    public PuzzleFracciones(int numerador1, int denominador1, int numerador2,
                            int denominador2, String operacion, int dificultad, int puntos) {
        /*super(
                crearEnunciado(
                        numerador1,
                        denominador1,
                        numerador2,
                        denominador2,
                        operacion
                ),
                dificultad,
                puntos
        );*/

        if (!operacion.equals("+") && !operacion.equals("-")) {throw new IllegalArgumentException("Operacion no valida.");}
        primera = new Fracciones(numerador1, denominador1);
        segunda = new Fracciones(numerador2, denominador2);
        this.operacion = operacion;
        calcularResultado();
    }

    private static String crearEnunciado(
            int n1, int d1,
            int n2, int d2,
            String operacion) {
        return "Resuelve: " + n1 + "/" + d1
                + " " + operacion + " " + n2 + "/" + d2
                + ". Escribe tu respuesta como fraccion.";
    }

    private void calcularResultado() {
        if (operacion.equals("+")) {
            resultado = primera.sumar(segunda);
        } else {
            resultado = primera.restar(segunda);
        }
    }

   // @Override
    public boolean verificarRespuesta(String respuesta) {
        try {
            String[] partes = respuesta.trim().split("/");
            if (partes.length != 2) {return false;}
            int numerador = Integer.parseInt(partes[0].trim());
            int denominador = Integer.parseInt(partes[1].trim());
            if (denominador == 0) {return false;}
            Fracciones respuestaJugador = new Fracciones(numerador, denominador);
            return respuestaJugador.getNumerador()
                    == resultado.getNumerador()
                    && respuestaJugador.getDenominador()
                    == resultado.getDenominador();
        } catch (NumberFormatException e) {
            return false;
        }
    }
/*
    @Override
    public String obtenerRespuestaCorrecta() {return resultado.toString();}
    */
}
