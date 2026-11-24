package laberinto.musica;

public class FragmentoMusical {
    private int id;
    private Nota nota;
    private boolean recuperado;

    public FragmentoMusical(int id, Nota nota) {
        if (id <= 0) {throw new IllegalArgumentException("El ID debe ser positivo.");}
        if (nota == null) {throw new IllegalArgumentException("La nota no puede ser nula.");}
        this.id = id;
        this.nota = nota;
        this.recuperado = false;
    }

    public void marcarRecuperado() {recuperado = true;}

    public int getId() {return id;}

    public Nota getNota() {return nota;}

    public boolean estaRecuperado() {return recuperado;}

    @Override
    public String toString() {return "Fragmento " + id + ": " + nota + " | Recuperado: " + recuperado;}
}
