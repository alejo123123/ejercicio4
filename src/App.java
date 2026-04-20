import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {

    // Lista ligada
    private ListaMelodia lista = new ListaMelodia();

    // Componentes de edición
    private JComboBox<Nota> cbNota;
    private JComboBox<Figura> cbFigura;
    private JSpinner spOctava;

    // Tabla
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public App() {
        setTitle("Editor de Melodías");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── BARRA DE HERRAMIENTAS ──
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton btnCargar   = makeBoton("📂", "Cargar melodía");
        JButton btnGuardar  = makeBoton("💾", "Guardar melodía");
        JButton btnAgregar  = makeBoton("➕", "Agregar nota");
        JButton btnModificar = makeBoton("✏️", "Modificar nota");
        JButton btnEliminar = makeBoton("➖", "Eliminar nota");
        JButton btnReproducir = makeBoton("🎹", "Reproducir melodía");

        // Controles de nota
        cbNota   = new JComboBox<>(Nota.values());
        cbFigura = new JComboBox<>(Figura.values());
        spOctava = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        spOctava.setPreferredSize(new Dimension(50, 30));
        spOctava.setMaximumSize(new Dimension(50, 30));

        toolbar.add(btnCargar);
        toolbar.add(btnGuardar);
        toolbar.addSeparator();
        toolbar.add(new JLabel(" Nota: "));
        toolbar.add(cbNota);
        toolbar.add(new JLabel(" Figura: "));
        toolbar.add(cbFigura);
        toolbar.add(new JLabel(" Octava: "));
        toolbar.add(spOctava);
        toolbar.addSeparator();
        toolbar.add(btnAgregar);
        toolbar.add(btnModificar);
        toolbar.add(btnEliminar);
        toolbar.addSeparator();
        toolbar.add(btnReproducir);

        add(toolbar, BorderLayout.NORTH);

        // ── TABLA ──
        modeloTabla = new DefaultTableModel(new String[]{"Nota", "Figura", "Octava"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ── LISTENERS ──
        btnCargar.addActionListener(e -> cargarMelodia());
        btnGuardar.addActionListener(e -> guardarMelodia());
        btnAgregar.addActionListener(e -> agregarNota());
        btnModificar.addActionListener(e -> modificarNota());
        btnEliminar.addActionListener(e -> eliminarNota());
        btnReproducir.addActionListener(e -> reproducirMelodia());

        // Al seleccionar fila → cargar en controles
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEnControles();
        });

        setVisible(true);
    }

    // ── Helpers ──

    private JButton makeBoton(String texto, String tooltip) {
        JButton b = new JButton(texto);
        b.setToolTipText(tooltip);
        return b;
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (NotaMusical nm : lista.aLista()) {
            modeloTabla.addRow(new Object[]{
                nm.getNota().name(),
                nm.getFigura().name(),
                nm.getOctava()
            });
        }
    }

    private void cargarEnControles() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        NotaMusical nm = lista.obtener(fila);
        if (nm == null) return;
        cbNota.setSelectedItem(nm.getNota());
        cbFigura.setSelectedItem(nm.getFigura());
        spOctava.setValue(nm.getOctava());
    }

    // ── Operaciones ──

    private void agregarNota() {
        NotaMusical nm = new NotaMusical(
            (Nota)   cbNota.getSelectedItem(),
            (Figura) cbFigura.getSelectedItem(),
            (int)    spOctava.getValue()
        );
        lista.agregar(nm);
        refrescarTabla();
        // Seleccionar última fila
        int ultima = modeloTabla.getRowCount() - 1;
        tabla.setRowSelectionInterval(ultima, ultima);
    }

    private void modificarNota() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una nota primero.");
            return;
        }
        NotaMusical nm = new NotaMusical(
            (Nota)   cbNota.getSelectedItem(),
            (Figura) cbFigura.getSelectedItem(),
            (int)    spOctava.getValue()
        );
        lista.modificar(fila, nm);
        refrescarTabla();
        tabla.setRowSelectionInterval(fila, fila);
    }

    private void eliminarNota() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una nota primero.");
            return;
        }
        lista.eliminar(fila);
        refrescarTabla();
    }

    private void guardarMelodia() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar melodía");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = fc.getSelectedFile();
        if (!archivo.getName().endsWith(".json")) {
            archivo = new File(archivo.getAbsolutePath() + ".json");
        }

        try {
            // Convertir lista ligada a List para Jackson
            List<NotaMusical> notas = lista.aLista();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(archivo, notas);
            JOptionPane.showMessageDialog(this, "Melodía guardada: " + archivo.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarMelodia() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Cargar melodía");
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = fc.getSelectedFile();
        try {
            ObjectMapper mapper = new ObjectMapper();
            NotaMusical[] arr = mapper.readValue(archivo, NotaMusical[].class);

            lista.limpiar();
            for (NotaMusical nm : arr) {
                lista.agregar(nm);
            }
            refrescarTabla();
            JOptionPane.showMessageDialog(this, "Melodía cargada: " + archivo.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage());
        }
    }

    private void reproducirMelodia() {
        if (lista.getTamanio() == 0) {
            JOptionPane.showMessageDialog(this, "No hay notas en la melodía.");
            return;
        }
        // Reproducir en hilo aparte para no bloquear la UI
        new Thread(() -> {
            ReproductorAudioMIDI.iniciarMelodia();
            for (NotaMusical nm : lista.aLista()) {
                ReproductorAudioMIDI.reproducirNota(nm);
            }
            ReproductorAudioMIDI.finalizarMelodia();
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}