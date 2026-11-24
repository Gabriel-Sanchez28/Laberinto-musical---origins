package laberinto.musica;

import laberinto.matematicas.Fracciones;

public class Nota {
    private String nombre;
    private int octava;
    private String alteracion;
    private Fracciones duracion;
    private int puntillos;

    public Nota(String nombre, int octava, String alteracion, Fracciones duracion, int puntillos) {
        if (!nombre.equals("Do") && !nombre.equals("Re")
                && !nombre.equals("Mi") && !nombre.equals("Fa")
                && !nombre.equals("Sol") && !nombre.equals("La")
                && !nombre.equals("Si")) {
            throw new IllegalArgumentException("Nombre de nota no valido.");
        }
        if (duracion.getNumerador() <= 0) {throw new IllegalArgumentException("La duracion debe ser positiva.");}
        if (puntillos < 0 || puntillos > 3) {throw new IllegalArgumentException("Cantidad de puntillos no valida.");}
        if (!alteracion.equals("natural")
                && !alteracion.equals("sostenido")
                && !alteracion.equals("bemol")) {
            throw new IllegalArgumentException("Alteracion no valida.");
        }
        this.nombre = nombre;
        this.octava = octava;
        this.alteracion = alteracion;
        this.duracion = duracion;
        this.puntillos = puntillos;
    }

    public Fracciones obtenerDuracion() {
        Fracciones total = duracion;
        Fracciones incremento = duracion.dividir(new Fracciones(2, 1));
        for (int i = 0; i < puntillos; i++) {
            total = total.sumar(incremento);
            incremento = incremento.dividir(new Fracciones(2, 1));
        }
        return total;
    }

    public String getNombre() {return nombre;}

    public int getOctava() {return octava;}

    public String getAlteracion() {return alteracion;}

    public int getPuntillos() {return puntillos;}

    @Override
    public String toString() {
        return nombre + " (" + alteracion + "), octava " + octava + ", duracion " + obtenerDuracion();
    }
}
