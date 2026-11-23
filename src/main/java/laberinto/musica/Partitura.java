package laberinto.musica;

import laberinto.matematicas.Fracciones;

import java.util.ArrayList;

public class Partitura {
    private String titulo;
    private String compositor;
    private int numeradorCompas;
    private int denominadorCompas;
    private ArrayList<Nota> notas;

    public Partitura(String titulo, String compositor, int numeradorCompas, int denominadorCompas) {
        if (numeradorCompas <= 0 || denominadorCompas <= 0) {throw new IllegalArgumentException("Compas no valido.");}
        this.titulo = titulo;
        this.compositor = compositor;
        this.numeradorCompas = numeradorCompas;
        this.denominadorCompas = denominadorCompas;
        notas = new ArrayList<>();
    }

    public void agregarNota(Nota nota) {
        Fracciones duracionActual = calcularDuracionTotal();
        Fracciones nuevaDuracion = duracionActual.sumar(nota.obtenerDuracion());
        int capacidadNumerador = numeradorCompas;
        int capacidadDenominador = denominadorCompas;
        int comparacion = nuevaDuracion.getNumerador() * capacidadDenominador - capacidadNumerador * nuevaDuracion.getDenominador();
        if (comparacion > 0) {throw new IllegalArgumentException("La nota supera la capacidad del compas.");}
        notas.add(nota);
    }

    public Fracciones calcularDuracionTotal() {
        Fracciones total = new Fracciones(0, 1);
        for (Nota nota : notas) {total = total.sumar(nota.obtenerDuracion());}
        return total;
    }

    public boolean estaCompasCompleto() {
        Fracciones total = calcularDuracionTotal();
        return total.getNumerador() * denominadorCompas == numeradorCompas * total.getDenominador();
    }

    public String obtenerInformacion() {
        String informacion = "Partitura: " + titulo
                + "\nCompositor: " + compositor
                + "\nCompas: " + numeradorCompas + "/" + denominadorCompas
                + "\nNotas:\n";
        for (Nota nota : notas) {informacion += nota + "\n";}
        informacion += "Duracion total: " + calcularDuracionTotal();
        return informacion;
    }

    public String getTitulo() {return titulo;}

    public String getCompositor() {return compositor;}

    public int getNumeradorCompas() {return numeradorCompas;}

    public int getDenominadorCompas() {return denominadorCompas;}

    public ArrayList<Nota> getNotas() {return new ArrayList<>(notas);}
}
