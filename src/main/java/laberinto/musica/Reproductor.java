package laberinto.musica;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class Reproductor {
    public void reproducirPartitura(Partitura partitura, int bpm) {
        if (partitura == null) {throw new IllegalArgumentException("La partitura no puede ser nula.");}
        if (bpm <= 0) {throw new IllegalArgumentException("El tempo debe ser positivo.");}
        Synthesizer sintetizador = null;
        try {
            sintetizador = MidiSystem.getSynthesizer();
            sintetizador.open();
            MidiChannel canal = sintetizador.getChannels()[0];
            canal.programChange(0);
            System.out.println("Reproduciendo: " + partitura.getTitulo());
            for (Nota nota : partitura.getNotas()) {
                int numeroMidi = obtenerNumeroMidi(nota);
                long duracion = calcularDuracion(nota, bpm);
                canal.noteOn(numeroMidi, 90);
                try {
                    Thread.sleep(duracion);
                } catch (InterruptedException e) {
                    canal.noteOff(numeroMidi);
                    Thread.currentThread().interrupt();
                    break;
                }
                canal.noteOff(numeroMidi);
            }
            System.out.println("Reproduccion finalizada.");
        } catch (Exception e) {
            System.out.println("No fue posible reproducir la partitura.");
            System.out.println("Detalle: " + e.getMessage());
        } finally {
            if (sintetizador != null && sintetizador.isOpen()) {
                sintetizador.close();
            }
        }
    }

    private int obtenerNumeroMidi(Nota nota) {
        int semitono = 0;
        switch (nota.getNombre()) {
            case "Do":
                semitono = 0;
                break;
            case "Re":
                semitono = 2;
                break;
            case "Mi":
                semitono = 4;
                break;
            case "Fa":
                semitono = 5;
                break;
            case "Sol":
                semitono = 7;
                break;
            case "La":
                semitono = 9;
                break;
            case "Si":
                semitono = 11;
                break;
        }
        if (nota.getAlteracion().equals("sostenido")) {
            semitono++;
        } else if (nota.getAlteracion().equals("bemol")) {
            semitono--;
        }
        return (nota.getOctava() + 1) * 12 + semitono;
    }

    private long calcularDuracion(Nota nota, int bpm) {
        double numerador = nota.obtenerDuracion().getNumerador();
        double denominador = nota.obtenerDuracion().getDenominador();
        double duracionRedonda = 4.0 * (60000.0 / bpm);
        return Math.max(1, Math.round(duracionRedonda * numerador / denominador));
    }
}
