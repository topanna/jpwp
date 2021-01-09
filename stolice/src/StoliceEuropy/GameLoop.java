package StoliceEuropy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiadająca za pętle gry
 *
 */
public class GameLoop implements ActionListener {

    private GamePanel panel;

    /**
     * Konstruktor pętli odwołujący się do panelu gry
     *
     * @param panel Panel gry
     */
    public GameLoop(GamePanel panel) {
        this.panel = panel;
    }

    /**
     * Metoda odpowiedzialna za wywołanie pojedynczej pętli gry
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.panel.doOneLoop();

    }

}
