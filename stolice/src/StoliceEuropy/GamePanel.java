package StoliceEuropy;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Klasa odpowiadająca za główny obszar graficzny gry
 *
 */
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    private boolean mouseClicked = false;
    private int roundNumber = 0;
    private Timer timer;
    private double getDistance;
    private JLabel capitalNameLabel, distanceLabel, scoreLabel;
    private int timePassed = 0;
    private boolean running = false;
    private boolean nextRound = false;
    private boolean activeDisplay = false;
    private String distanceInKm, score;
    private double sumScore = 0;
    private int timeBonus = 0;
    private final int gameWidth = 1024;
    private final int gameHeight = 768;
    private int timebarWidth = 1024;
    private int totalScore = 0;
    private Menu menu;

    /**
     * Obszar wyświetlający wynik końcowy
     */
    public Rectangle totalScoreLabel = new Rectangle((gameWidth - 400) / 2, (gameHeight - 300) / 2, 400, 300);

    /**
     * Przycisk wyjścia z gry
     */
    public Rectangle quitButton = new Rectangle((gameWidth - 200) / 2, 544, 200, 50);

    /**
     * Stan gry
     */
    public enum STATE {
        /**
         * Stan gry w trakcie wyświetlania menu
         */
        MENU,
        /**
         * Stan gry w trakcie rozgrywki
         */
        GAME,
        /**
         * Stan gry po zakończeniu rozgrywki
         */
        END
    };

    /**
     * Wskazuje obecny stan gry
     */
    public static STATE State = STATE.MENU;

    /**
     * Konstruktor klasy pola graficznego gry Wczytuje dane i grafikę Wczytuje
     * menu
     */
    public GamePanel() {
        setPreferredSize(new Dimension(gameWidth, gameHeight));
        addMouseListener(this);
        addMouseMotionListener(this);
        Pin.initCapitals();
        Images.getImages();
        initLayout();
        menu = new Menu();
        timer = new Timer(50, new GameLoop(this));  //pętla gry wykonuje się co 50ms
        timer.start();
        running = true;
        activeDisplay = true;
    }

    /**
     * Inicjalizuje układ graficzny ekranu gry
     */
    private void initLayout() {

        //Tworzy 3 obszary: do wyświetlania nazwy stolicy, dystansu oraz punktów
        capitalNameLabel = new JLabel("", JLabel.CENTER);
        distanceLabel = new JLabel("", JLabel.CENTER);
        scoreLabel = new JLabel("", JLabel.CENTER);
        distanceLabel.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        distanceLabel.setBackground(new Color(242, 242, 242, 255));
        distanceLabel.setOpaque(true);
        capitalNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
        capitalNameLabel.setBackground(new Color(242, 242, 242, 255));
        capitalNameLabel.setOpaque(true);
        scoreLabel.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        scoreLabel.setBackground(new Color(242, 242, 242, 255));
        scoreLabel.setOpaque(true);
        scoreLabel.setVisible(false);   //punkty są widoczne dopiero po wskazaniu gracza
        distanceLabel.setVisible(false);    //dystans jest widoczny dopiero po wskazaniu gracza

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(capitalNameLabel, gbc);
        gbc.gridx = 1;
        add(distanceLabel, gbc);
        gbc.gridx = 2;
        add(scoreLabel, gbc);
    }

    /**
     * Wyświetla pinezkę w miejscu wskazania gracza
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    public void showPlayerPin(Graphics g) {

        if (mouseClicked) {
            g.drawImage(Images.blackPin, Pin.getPlayerX() - 12, Pin.getPlayerY() - 38, null);
        }
    }

    /**
     * Wyświetla pinezkę wskazującą faktyczne położenie miasta
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    public void showActualPin(Graphics g) {

        if (mouseClicked) {
            g.drawImage(Images.redPin, Pin.getActualX(roundNumber) - 12, Pin.getActualY(roundNumber) - 38, null);
        }

    }

    /**
     * Rozpoczyna kolejną rundę (wyświetla nowe miasto do wskazania)
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    public void startRound(Graphics g) {
        if (running && activeDisplay) {
            capitalNameLabel.setText(Pin.capitals.get(roundNumber).getName());
            capitalNameLabel.repaint();
        }
    }

    /**
     * Wyświetla dystans w kilometrach i otrzymane punkty
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    public void displayScore(Graphics g) {

        if (mouseClicked) {
            if (!nextRound) {
                scoreLabel.setText("Punkty: " + score);
                distanceLabel.setText("Dystans: " + distanceInKm + " km");
                scoreLabel.setVisible(true);
                distanceLabel.setVisible(true);
            }
        }

        if (nextRound) {
            scoreLabel.setVisible(false);
            distanceLabel.setVisible(false);
            nextRound = false;
            timebarWidth = 1024;    //dla nowej rundy pasek czasu pełny
        }
    }

    /**
     * Nadpisuje metodę odpowiedzialną za rysowanie w panelu - wypełnienie
     * zależne od stanu gry
     *
     * @param g Aktualny kontekst graficzny wyświetlany na ekranie
     */
    @Override
    public void paintComponent(Graphics g) {

        //Antyaliasing
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g);

        if (State == STATE.GAME) { 
            //Ustawia kursor
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            
            //Rysuje tło gry
            g.drawImage(Images.map, 0, 0, null);
            
            startRound(g);

            //Pasek czasu
            g.setColor(Color.GRAY);
            g.fillRect(0, 30, 1024, 8);
            g.setColor(Color.GREEN);
            g.fillRect(0, 30, timebarWidth, 8);

            showPlayerPin(g);
            showActualPin(g);
            displayScore(g);
        }

        if (State == STATE.END) {
            Graphics2D g2d = (Graphics2D) g;

            //Rysuje tło ekranu końcowego gry
            g.drawImage(Images.endScreen, 0, 0, null);

            capitalNameLabel.setVisible(false);
            distanceLabel.setVisible(false);
            scoreLabel.setVisible(false);

            g.setColor(new Color(217, 179, 140, 255));
            g2d.fill(quitButton);
            g2d.fill(totalScoreLabel);

            //Obsługa przycisku "Wyjście"
            if (menu.Button == Menu.BUTTON.ENDQUIT) {
                if (menu.Mouse == Menu.MOUSE.HOVER) {
                    g.setColor(new Color(204, 153, 102, 255));
                    g2d.fill(quitButton);
                }
                if (menu.Mouse == Menu.MOUSE.CLICKED) {
                    System.exit(0);
                }
            }

            //Wyświetlenie wyniku końcowego
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 55));
            g.drawString("Koniec gry", totalScoreLabel.x + totalScoreLabel.width / 2 - 125, totalScoreLabel.y + totalScoreLabel.height / 2 - 40);
            g.drawString(String.valueOf(totalScore), totalScoreLabel.x + totalScoreLabel.width / 2 - g.getFontMetrics().stringWidth(String.valueOf(totalScore)) / 2, totalScoreLabel.y + totalScoreLabel.height / 2 + 80);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Wynik końcowy: ", totalScoreLabel.x + totalScoreLabel.width / 2 - 90, totalScoreLabel.y + totalScoreLabel.height / 2 + 20);
            g.drawString("Wyjście", quitButton.x + quitButton.width / 2 - 40, quitButton.y + quitButton.height / 2 + 10);

        } else if (State == STATE.MENU) {
            menu.render(g);
        }
    }

    /**
     * Obsługa zdarzeń - poruszanie myszką
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        //Obsługa myszki w menu
        if (State == STATE.MENU) {
            if (mx > menu.playButton.x && mx < menu.playButton.x + menu.playButton.width && my > menu.playButton.y
                    && my < menu.playButton.y + menu.playButton.height) {
                menu.Mouse = Menu.MOUSE.HOVER;
                menu.Button = Menu.BUTTON.PLAY;
            } else if (mx > menu.quitButton.x && mx < menu.quitButton.x + menu.quitButton.width && my > menu.quitButton.y
                    && my < menu.quitButton.y + menu.quitButton.height) {
                menu.Mouse = Menu.MOUSE.HOVER;
                menu.Button = Menu.BUTTON.QUIT;
            } else {
                menu.Mouse = Menu.MOUSE.EXITED;
            }
        }

        //Obsługa myszki na ekranie końcowym
        if (State == STATE.END) {
            if (mx > quitButton.x && mx < quitButton.x + quitButton.width && my > quitButton.y
                    && my < quitButton.y + quitButton.height) {
                menu.Mouse = Menu.MOUSE.HOVER;
                menu.Button = Menu.BUTTON.ENDQUIT;
            } else {
                menu.Mouse = Menu.MOUSE.EXITED;
            }
        }
    }

    /**
     * Obsługa zdarzeń - wciśnięcie przycisku myszki
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mousePressed(MouseEvent e) {

        //System.out.println(Pin.getPlayerX() + ", " + Pin.getPlayerY());
        int mx = e.getX();
        int my = e.getY();

        //Obsługa myszki w grze
        if (State == STATE.GAME) {
            if (activeDisplay) {
                mouseClicked = true;
                roundNumber = roundNumber + 1;
                Pin.loadPlayerXY(mx, my);
                sumScore();
            }
        }

        //Obsługa myszki w menu
        if (State == STATE.MENU) {
            if (mx > menu.playButton.x && mx < menu.playButton.x + menu.playButton.width && my > menu.playButton.y
                    && my < menu.playButton.y + menu.playButton.height) {
                menu.Mouse = Menu.MOUSE.CLICKED;
                menu.Button = Menu.BUTTON.PLAY;
            } else if (mx > menu.quitButton.x && mx < menu.quitButton.x + menu.quitButton.width && my > menu.quitButton.y
                    && my < menu.quitButton.y + menu.quitButton.height) {
                menu.Mouse = Menu.MOUSE.CLICKED;
                menu.Button = Menu.BUTTON.QUIT;
            } else {
                menu.Mouse = Menu.MOUSE.EXITED;
            }
        }

        //Obsługa myszki na ekranie końcowym
        if (State == STATE.END) {
            if (mx > quitButton.x && mx < quitButton.x + quitButton.width && my > quitButton.y
                    && my < quitButton.y + quitButton.height) {
                menu.Mouse = Menu.MOUSE.CLICKED;
                menu.Button = Menu.BUTTON.ENDQUIT;
            } else {
                menu.Mouse = Menu.MOUSE.EXITED;
            }

        }
    }

    /**
     * Oblicza dystans w kilometrach oraz otrzymane punkty po każdej rundzie.
     * Sumuje całkowity wynik
     *
     */
    private void sumScore() {
        //Obliczanie różnicy między wskazaniem gracza a faktycznym położeniem
        getDistance = Math.round(Pin.getDifferenceInKm(Pin.getPlayerX(), Pin.getPlayerY(), Pin.getActualX(roundNumber), Pin.getActualY(roundNumber)));

        //Obliczanie punktów za wskazanie gracza
        sumScore = Math.floor(Math.exp(-getDistance / 1000) * timeBonus);

        //Sumowanie punktów
        totalScore += sumScore;

        distanceInKm = toString(getDistance);
        score = toString(sumScore);
        //System.out.println(totalScore);
    }

    /**
     * Zamiana zmiennej typu Double na String. Usunięcie zbędnych zer na końcu
     * liczby
     *
     * @param d Wejściowy obiekt typu Double
     * @return Obiekt po konwersji na typ String
     */
    private String toString(double d) {
        BigDecimal diffString = new BigDecimal(Double.valueOf(d).toString());
        return diffString.stripTrailingZeros().toPlainString();
    }

    /**
     * Wykonuje pojedynczą pętle gry (pojedynczą rundę)
     *
     */
    public void doOneLoop() {
        //Wykonuje się co 50ms
        if (State == STATE.GAME) {
            //Runda aktywna - czeka na wskazanie gracza
            if (running == true) {
                if (activeDisplay == true) {
                    timePassed++;   //zwiększa upłynięty czas (wielokrotność 50ms)
                    timeBonus = 140 - timePassed;   //bonus punktowy maleje wraz z upłyniętym czasem
                    timebarWidth -= 8;  //zmniejsza pasek czasu o 8 px z każdą pętlą

                    //System.out.println(timebarWidth);
                    //Jeśli gracz nie wskaże w ciągu 7s gra przechodzi do następnej rundy
                    if (timePassed == 140) {    //50ms x 140 = 7s
                        nextRound = true;
                        mouseClicked = false;
                        roundNumber = roundNumber + 1;
                        timePassed++;
                        if (roundNumber == 43) {    //jeśli gra wyświetliła wszystkie stolice do wskazania następuje koniec gry
                            State = STATE.END;
                        }
                    }
                    if (timePassed > 140) { //zerowanie licznika czasu rundy, gdy upłynął czas na wskazanie
                        timePassed = 0;
                        activeDisplay = true;
                    }
                    if (mouseClicked) { //zerowanie licznika czasu rundy, gdy wskazano odpowiedź
                        timePassed = 0;
                    }
                }
            }

            /*
            Jeśli gracz wskaże w przeciągu 7s gra przechodzi do następnej rundy,
            ale najpierw ekran zostaje zamrożony na 2s, aby wyświetlić wyniki
             */
            if (mouseClicked == true) {
                activeDisplay = false;
                timePassed++;
                //System.out.println(timePassed);
                if (timePassed == 40) {            //50ms x 40 = 2s
                    mouseClicked = false;
                    nextRound = true;   //po 2s przechodzi do następnej rundy
                    timePassed++;
                    if (roundNumber == 43) {    //jeśli gra wyświetliła wszystkie stolice do wskazania następuje koniec gry
                        State = STATE.END;
                    }
                }
                if (timePassed > 40) {  //zerowanie licznika czasu
                    timePassed = 0;
                    activeDisplay = true;
                }
            }
        }

        repaint();
    }

    /**
     * Obsługa zdarzeń - pojedyncze kliknięcie myszką (nieużywane)
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Obsługa zdarzeń - przeciągnięcie myszką (nieużywane)
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * Obsługa zdarzeń - zwolnienie przycisku myszki (nieużywane)
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Obsługa zdarzeń - kursor myszki w granicach komponentu (nieużywane)
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Obsługa zdarzeń - kursor myszki poza granicami komponentu (nieużywane)
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

}
