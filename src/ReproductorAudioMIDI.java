import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class ReproductorAudioMIDI {

    private static final int VELOCIDAD = 100; // Volumen de la nota
    private static final int CANAL_PIANO = 0; // Canal MIDI para piano

    // Mapeo de notas a valores MIDI
    private static final int[] NOTAS_MIDI = {
            60, // DO
            61, // DO#
            62, // RE
            63, // RE#
            64, // MI
            65, // FA
            66, // FA#
            67, // SOL
            68, // SOL#
            69, // LA
            70, // LA#
            71 // SI
    };

    // Duraciones en milisegundos para cada figura musical
    private static final int DURACION_REDONDA = 2000; // 2 seg
    private static final int DURACION_BLANCA = 1000; // 1 seg
    private static final int DURACION_NEGRA = 500; // 0.5 seg
    private static final int DURACION_CORCHEA = 300; // 0.3 seg

    private static Synthesizer synth;
    private static MidiChannel piano;

    public static void iniciarMelodia() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            piano = synth.getChannels()[CANAL_PIANO];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reproduce una única nota
    public static void reproducirNota(NotaMusical notaMusical) {
        try {
            /*
             * Synthesizer synth = MidiSystem.getSynthesizer();
             * synth.open();
             * MidiChannel piano = synth.getChannels()[CANAL_PIANO];
             */

            int notaMidi = NOTAS_MIDI[notaMusical.getNota().ordinal()] + (notaMusical.getOctava() - 4) * 12;
            int duracion = obtenerDuracion(notaMusical.getFigura());

            piano.noteOn(notaMidi, VELOCIDAD);
            Thread.sleep(duracion);
            piano.noteOff(notaMidi);

            //Thread.sleep(80);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finalizarMelodia() {
        synth.close();
    }

    // Devuelve la duración de la nota según su figura
    private static int obtenerDuracion(Figura figura) {
        switch (figura) {
            case REDONDA:
                return DURACION_REDONDA;
            case BLANCA:
                return DURACION_BLANCA;
            case NEGRA:
                return DURACION_NEGRA;
            case CORCHEA:
                return DURACION_CORCHEA;
            default:
                return 400;
        }
    }

}
