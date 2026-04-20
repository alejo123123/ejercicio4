import java.util.ArrayList;
import java.util.List;

public class ListaMelodia {
    private NodoMelodia cabeza;
    private int tamanio;

    public ListaMelodia() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Agregar al final
    public void agregar(NotaMusical nota) {
        NodoMelodia nuevo = new NodoMelodia(nota);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoMelodia actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamanio++;
    }

    // Obtener nota por índice
    public NotaMusical obtener(int indice) {
        NodoMelodia actual = cabeza;
        int i = 0;
        while (actual != null) {
            if (i == indice) return actual.getNotaMusical();
            actual = actual.getSiguiente();
            i++;
        }
        return null;
    }

    // Modificar nota en índice
    public void modificar(int indice, NotaMusical nueva) {
        NodoMelodia actual = cabeza;
        int i = 0;
        while (actual != null) {
            if (i == indice) {
                actual.setNotaMusical(nueva);
                return;
            }
            actual = actual.getSiguiente();
            i++;
        }
    }

    // Eliminar por índice
    public void eliminar(int indice) {
        if (cabeza == null) return;
        if (indice == 0) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return;
        }
        NodoMelodia actual = cabeza;
        int i = 0;
        while (actual.getSiguiente() != null) {
            if (i + 1 == indice) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return;
            }
            actual = actual.getSiguiente();
            i++;
        }
    }

    // Convertir a lista para la tabla
    public List<NotaMusical> aLista() {
        List<NotaMusical> lista = new ArrayList<>();
        NodoMelodia actual = cabeza;
        while (actual != null) {
            lista.add(actual.getNotaMusical());
            actual = actual.getSiguiente();
        }
        return lista;
    }

    // Limpiar lista
    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }

    public int getTamanio() {
        return tamanio;
    }
}