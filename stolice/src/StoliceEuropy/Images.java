package StoliceEuropy;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Klasa wczytująca grafiki wykorzystywane w grze
 *
 */
public class Images {

    /**
     * Tło menu
     */
    public static Image menu;

    /**
     * Tło gry
     */
    public static Image map;

    /**
     * Tło końcowe gry
     */
    public static Image endScreen;

    /**
     * Czarna pinezka
     */
    public static Image blackPin;

    /**
     * Czerwona pinezka
     */
    public static Image redPin;

    /**
     * Wczytuje wszystkie grafiki wykorzystywane w grze
     */
    public static void getImages() {
        try {
            menu = ImageIO.read(Images.class.getResource("/res/menu.png"));
            map = ImageIO.read(Images.class.getResource("/res/map.png"));
            endScreen = ImageIO.read(Images.class.getResource("/res/blurredmap.png"));
            blackPin = ImageIO.read(Images.class.getResource("/res/pinblack.png"));
            redPin = ImageIO.read(Images.class.getResource("/res/pinred.png"));
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
