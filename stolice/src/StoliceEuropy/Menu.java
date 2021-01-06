package StoliceEuropy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * Klasa odpowiadająca za menu gry
 *
 * @author Anna Topa
 */
public class Menu {

    /**
     * Stan myszki
     */
    public enum MOUSE {

        /**
         * Kursor myszki poza aktywnymi elementami
         */
        EXITED,
        /**
         * Najechanie kursorem myszki
         */
        HOVER,
        /**
         * Kliknięcie myszką
         */
        CLICKED
    };

    /**
     * Wybrany przycisk
     */
    public enum BUTTON {

        /**
         * Przycisk rozpoczynający grę
         */
        PLAY,
        /**
         * Przycisk wyjścia z gry (w menu)
         */
        QUIT,
        /**
         * Przycisk wyjścia z gry po zakończeniu gry
         */
        ENDQUIT
    };

    /**
     * Wskazuje stan myszki
     */
    public MOUSE Mouse;

    /**
     * Wskazuje wciśnięty przez gracza przycisk
     */
    public BUTTON Button;
    private int buttonWidth = 400;
    private int buttonHeight = 100;

    /**
     * Przycisk rozpoczęcia gry
     */
    public Rectangle playButton = new Rectangle((1024 - buttonWidth) / 2, (768 - buttonHeight) / 2 - 60, buttonWidth, buttonHeight);

    /**
     * Przycisk wyjścia
     */
    public Rectangle quitButton = new Rectangle((1024 - buttonWidth) / 2, (768 - buttonHeight) / 2 + 60, buttonWidth, buttonHeight);

    /**
     * Tworzy wygląd menu
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //Antyaliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        //Rysuje tło ekranu gry
        g.drawImage(Images.menu, 0, 0, null);

        //Tworzy przyciski 'Gra' i 'Wyjście'
        g.setColor(new Color(217, 179, 140, 255));
        g2d.fill(playButton);
        g2d.fill(quitButton);

        //Obsługa najechania myszką - podświetlenie przycisku
        if (Mouse == MOUSE.HOVER) {
            g.setColor(new Color(204, 153, 102, 255));

            if (Button == BUTTON.PLAY) {
                g2d.fill(playButton);
            }
            if (Button == BUTTON.QUIT) {
                g2d.fill(quitButton);
            }
        }

        //Obsługa kliknięcia myszką
        if (Mouse == MOUSE.CLICKED) {
            if (Button == BUTTON.PLAY) {
                GamePanel.State = GamePanel.STATE.GAME; //start gry
            }
            if (Button == BUTTON.QUIT) {
                System.exit(0); //wyjście z gry
            }
        }

        //Wyświetlenie napisów na przyciskach
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString("Gra", playButton.x + playButton.width / 2 - 40, playButton.y + playButton.height / 2 + 15);
        g.drawString("Wyjście", quitButton.x + quitButton.width / 2 - 85, quitButton.y + quitButton.height / 2 + 15);

    }
}
