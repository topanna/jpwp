/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stolice;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import static java.awt.SystemColor.text;
import javax.swing.JFrame;

/**
 *
 * @author Ania
 */
public class Menu {

    public enum MOUSE {
        EXITED,
        HOVER,
        CLICKED
    };

    public enum BUTTON {
        PLAY,
        QUIT
    };

    public MOUSE Mouse;
    public BUTTON Button;
    int buttonWidth = 400;
    int buttonHeight = 100;
    public Rectangle playButton = new Rectangle((1024 - buttonWidth) / 2, (768 - buttonHeight) / 2 - 60, buttonWidth, buttonHeight);
    public Rectangle quitButton = new Rectangle((1024 - buttonWidth) / 2, (768 - buttonHeight) / 2 + 60, buttonWidth, buttonHeight);
    boolean startGame = false;
    
    public void init(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(Images.menu, 0, 0, null);
        g.setColor(new Color(217, 179, 140, 255));

        g2d.fill(playButton);
        g2d.fill(quitButton);

        if (Mouse == MOUSE.HOVER) {
            g.setColor(new Color(204, 153, 102, 255));

            if (Button == BUTTON.PLAY) {
                g2d.fill(playButton);
            }
            if (Button == BUTTON.QUIT) {
                g2d.fill(quitButton);
            }
        }
        
        if (Mouse == MOUSE.CLICKED) {
            if (Button == BUTTON.PLAY) {
                GamePanel.State = GamePanel.STATE.GAME;
            }
            if (Button == BUTTON.QUIT) {
                System.exit(0);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString("Gra", playButton.x + playButton.width / 2 - 40, playButton.y + playButton.height / 2 + 15);
        g.drawString("Wyjście", quitButton.x + quitButton.width / 2 - 85, quitButton.y + quitButton.height / 2 + 15);

    }

    public void onMouseHover(Graphics g) {

    }
}
