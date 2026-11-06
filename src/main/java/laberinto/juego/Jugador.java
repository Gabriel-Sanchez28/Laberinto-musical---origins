package laberinto.juego;

public class Jugador {
    private String nombre;
    private int vida;
    private int puntuacion;
    private int rachaSinErrores;
    private int tareasCompletadas;
    private int fila;
    private int columna;
    private String obraSeleccionada;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.vida = 3;
        this.puntuacion = 0;
        this.rachaSinErrores = 0;
        this.tareasCompletadas = 0;
        this.fila = 1;
        this.columna = 1;
        this.obraSeleccionada = "";
    }

    public String getNombre() {return nombre;}
    public int getVida() {return vida;}
    public int getPuntuacion() {return puntuacion;}
    public int getRachaSinErrores() {return rachaSinErrores;}
    public int getTareasCompletadas() {return tareasCompletadas;}
    public int getFila() {return fila;}
    public int getColumna() {return columna;}
    public String getObraSeleccionada() {return obraSeleccionada;}

    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setObraSeleccionada(String obraSeleccionada) {this.obraSeleccionada = obraSeleccionada;}
    public void sumarPuntos(int puntos) {puntuacion += puntos;}
    public void perderVida() {if (vida > 0) {vida--;}}
    public void aumentarRacha() {rachaSinErrores++;}
    public void reiniciarRacha() {rachaSinErrores = 0;}
    public void completarTarea() {tareasCompletadas++;}
    public void cambiarPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
    public boolean estaVivo() {return vida > 0;}
}
