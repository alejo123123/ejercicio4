public class NodoMelodia {
    private NotaMusical notaMusical;
    private NodoMelodia siguiente;

    public NodoMelodia(NotaMusical notaMusical) {
        this.notaMusical = notaMusical;
        this.siguiente = null;
    }

    public NotaMusical getNotaMusical() {
        return notaMusical;
    }

    public void setNotaMusical(NotaMusical notaMusical) {
        this.notaMusical = notaMusical;
    }

    public NodoMelodia getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoMelodia siguiente) {
        this.siguiente = siguiente;
    }
}