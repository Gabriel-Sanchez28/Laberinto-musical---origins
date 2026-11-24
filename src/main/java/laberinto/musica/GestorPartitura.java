package laberinto.musica;

import java.util.ArrayList;

public class GestorPartitura {
    private Partitura partitura;
    private ArrayList<FragmentoMusical> fragmentos;

    public GestorPartitura(Partitura partitura) {
        if (partitura == null) {throw new IllegalArgumentException("La partitura no puede ser nula.");}
        this.partitura = partitura;
        fragmentos = new ArrayList<>();
    }

    public void agregarFragmento(FragmentoMusical fragmento) {
        if (fragmento == null) {throw new IllegalArgumentException("El fragmento no puede ser nulo.");}
        for (FragmentoMusical actual : fragmentos) {
            if (actual.getId() == fragmento.getId()) {throw new IllegalArgumentException("El fragmento ya existe.");}
        }
        fragmentos.add(fragmento);
    }

    public boolean recuperarFragmento(int id) {
        for (FragmentoMusical fragmento : fragmentos) {
            if (fragmento.getId() == id) {
                if (fragmento.estaRecuperado()) {return false;}
                partitura.agregarNota(fragmento.getNota());
                fragmento.marcarRecuperado();
                return true;
            }
        }
        return false;
    }

    public int obtenerCantidadRecuperada() {
        int cantidad = 0;
        for (FragmentoMusical fragmento : fragmentos) {
            if (fragmento.estaRecuperado()) {cantidad++;}
        }
        return cantidad;
    }

    public boolean estaObraCompleta() {
        if (fragmentos.isEmpty()) {return false;}
        for (FragmentoMusical fragmento : fragmentos) {
            if (!fragmento.estaRecuperado()) {return false;}
        }
        return partitura.estaCompasCompleto();
    }

    public Partitura getPartitura() {return partitura;}

    public ArrayList<FragmentoMusical> getFragmentos() {return new ArrayList<>(fragmentos);}
}
