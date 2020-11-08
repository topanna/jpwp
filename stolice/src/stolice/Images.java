/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Ania
 */
public class Images {

    static Image map;
    static Image blackPin;
    static Image redPin;

    public static void getImages() {
        try {
            //img = ImageIO.read(new File("build\\classes\\res\\map.png"));
            map = ImageIO.read(Images.class.getResource("/res/map.png"));
            blackPin = ImageIO.read(Images.class.getResource("/res/pinblack.png"));
            redPin = ImageIO.read(Images.class.getResource("/res/pinred.png"));
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
