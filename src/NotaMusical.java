
public class NotaMusical {
    private Nota nota;
    private Figura figura;
    private int octava;

    public NotaMusical(Nota nota, Figura figura, int octava) {
        this.nota = nota;
        this.figura = figura;
        this.octava = octava;
    }

    public NotaMusical() {
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public int getOctava() {
        return octava;
    }

    public void setOctava(int octava) {
        this.octava = octava;
    }

}
