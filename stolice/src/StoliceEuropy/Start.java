package StoliceEuropy;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Uruchamia aplikację
 *
 */
public class Start {

    /**
     * Metoda uruchamiająca aplikację
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Stolice Europy");    //tworzy ekran gry
        frame.setSize(new Dimension(1024, 768));    //o rozmiarze 1024x768
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel map = new GamePanel();
        frame.add(map);
        frame.pack();
        frame.setVisible(true);

    }
}
