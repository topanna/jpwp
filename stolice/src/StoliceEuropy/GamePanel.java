package StoliceEuropy;

import static StoliceEuropy.Pin.capitals;
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
import java.util.Collections;
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
    private double getDistance; //różnica między wskazaniem gracza a faktycznym położeniem
    private JLabel capitalNameLabel, distanceLabel, scoreLabel;
    private int timePassed = 0;
    private boolean running = false;
    private boolean nextRound = false;
    private boolean activeDisplay = false;
    private boolean cursorSet = false;
    private String distanceInKm, score;
    private double sumScore = 0;    //suma punktów za wskazanie gracza
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
     * Przycisk końca gry
     */
    public Rectangle quitButton = new Rectangle(gameWidth - 215, gameHeight - 65, 200, 50);

    /**
     * Przycisk "Graj ponownie"
     */
    public Rectangle playAgainButton = new Rectangle(312, 544, 198, 50);

    /**
     * Przycisk wyjścia z gry
     */
    public Rectangle exitButton = new Rectangle(514, 544, 198, 50);

    /**
     * Określa możliwe stany gry
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
     * Określa wbudowane przyciski w grze
     */
    public enum BUTTON {

        /**
         * Przycisk kończący grę
         */
        QUIT,
        /**
         * Przycisk "Graj ponownie"
         */
        PLAYAGAIN,
        /**
         * Przycisk wyjścia z gry
         */
        EXIT
    };

    /**
     * Określa możliwe stany myszki
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
     * Wskazuje obecny stan gry
     */
    public static STATE State = STATE.MENU;

    /**
     * Wskazuje obecny stan myszki
     */
    public static MOUSE Mouse;

    /**
     * Wskazuje używany przez gracza przycisk (w grze)
     */
    public BUTTON Button;

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

        //Tworzy układ 3 obszarów na ekranie gry
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
        Graphics2D g2d = (Graphics2D) g;

        //Antyaliasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g);

        if (State == STATE.GAME) {
            //Ustawia kursor gry
            if (!cursorSet) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                cursorSet = true;
            }

            //Rysuje tło gry
            g.drawImage(Images.map, 0, 0, null);

            //Tworzy przycisk "Koniec gry"
            g.setColor(new Color(217, 179, 140, 255));
            g2d.fill(quitButton);

            //Obsługa przycisku "Koniec gry"
            if (Button == BUTTON.QUIT) {
                if (Mouse == MOUSE.HOVER) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    g.setColor(new Color(204, 153, 102, 255));
                    g2d.fill(quitButton);
                }
                if (Mouse == MOUSE.CLICKED) {
                    State = STATE.END;
                }
                if (Mouse == MOUSE.EXITED) {
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                }
            }

            //Wyświetlenie napisu na przycisku
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Koniec gry", quitButton.x + quitButton.width / 2 - 55, quitButton.y + quitButton.height / 2 + 10);

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
            //Przywraca domyślny kursor
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            //Rysuje tło ekranu końcowego gry
            g.drawImage(Images.endScreen, 0, 0, null);

            capitalNameLabel.setVisible(false);
            distanceLabel.setVisible(false);
            scoreLabel.setVisible(false);

            g.setColor(new Color(217, 179, 140, 255));
            g2d.fill(playAgainButton);   //przycisk "Graj ponownie"
            g2d.fill(exitButton);   //przycisk "Wyjście"
            g2d.fill(totalScoreLabel);  //obszar wyświetlania wyniku końcowego

            //Obsługa przycisku "Graj ponownie"
            if (Button == BUTTON.PLAYAGAIN) {
                if (Mouse == MOUSE.HOVER) {
                    g.setColor(new Color(204, 153, 102, 255));
                    g2d.fill(playAgainButton);
                }
                if (Mouse == MOUSE.CLICKED) {
                    capitalNameLabel.setVisible(true);
                    nextRound = true;
                    Collections.shuffle(capitals);  //losuje sekwencję dla nowej rozgrywki
                    totalScore = 0;
                    roundNumber = 0;
                    timePassed = 0;
                    cursorSet = false;
                    State = STATE.GAME; //przywraca stan gry
                }
            }

            //Obsługa przycisku "Wyjście"
            if (Button == BUTTON.EXIT) {
                if (Mouse == MOUSE.HOVER) {
                    g.setColor(new Color(204, 153, 102, 255));
                    g2d.fill(exitButton);
                }
                if (Mouse == MOUSE.CLICKED) {
                    System.exit(0);
                }
            }

            //Wyświetlenie wyniku końcowego i tekstu na elementach
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 55));
            g.drawString("Koniec gry", totalScoreLabel.x + totalScoreLabel.width / 2 - 125, totalScoreLabel.y + totalScoreLabel.height / 2 - 40);
            g.drawString(String.valueOf(totalScore), totalScoreLabel.x + totalScoreLabel.width / 2 - g.getFontMetrics().stringWidth(String.valueOf(totalScore)) / 2, totalScoreLabel.y + totalScoreLabel.height / 2 + 80);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Wynik końcowy: ", totalScoreLabel.x + totalScoreLabel.width / 2 - 90, totalScoreLabel.y + totalScoreLabel.height / 2 + 20);
            g.drawString("Graj ponownie", playAgainButton.x + 20, playAgainButton.y + playAgainButton.height / 2 + 10);
            g.drawString("Wyjście", exitButton.x + exitButton.width / 2 - 40, exitButton.y + exitButton.height / 2 + 10);

        } else if (State == STATE.MENU) {
            menu.render(g);
        }
    }

    /**
     * Obsługa zdarzeń - poruszanie myszką Zależne od stanu gry
     *
     *
     * @param e Zdarzenie uruchamiane w momencie interakcji gracza z myszką
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        //Obsługa myszki w grze
        if (State == STATE.GAME) {
            if (mx > quitButton.x && mx < quitButton.x + quitButton.width && my > quitButton.y
                    && my < quitButton.y + quitButton.height) {
                Mouse = MOUSE.HOVER;
                Button = BUTTON.QUIT;
            } else {
                Mouse = MOUSE.EXITED;
            }
        }

        //Obsługa myszki w menu
        if (State == STATE.MENU) {
            if (mx > menu.playButton.x && mx < menu.playButton.x + menu.playButton.width && my > menu.playButton.y
                    && my < menu.playButton.y + menu.playButton.height) {
                Mouse = MOUSE.HOVER;
                menu.Button = Menu.BUTTON.PLAY;
            } else if (mx > menu.exitButton.x && mx < menu.exitButton.x + menu.exitButton.width && my > menu.exitButton.y
                    && my < menu.exitButton.y + menu.exitButton.height) {
                Mouse = MOUSE.HOVER;
                menu.Button = Menu.BUTTON.EXIT;
            } else {
                Mouse = MOUSE.EXITED;
            }
        }

        //Obsługa myszki na ekranie końcowym
        if (State == STATE.END) {
            if (mx > exitButton.x && mx < exitButton.x + exitButton.width && my > exitButton.y
                    && my < exitButton.y + exitButton.height) {
                Mouse = MOUSE.HOVER;
                Button = BUTTON.EXIT;
            } else if (mx > playAgainButton.x && mx < playAgainButton.x + playAgainButton.width && my > playAgainButton.y
                    && my < playAgainButton.y + playAgainButton.height) {
                Mouse = MOUSE.HOVER;
                Button = BUTTON.PLAYAGAIN;
            } else {
                Mouse = MOUSE.EXITED;
            }
        }
    }

    /**
     * Obsługa zdarzeń - wciśnięcie przycisku myszki Zależne od stanu gry
     * Zapisuje współrzędne (w px) wskazania gracza, obsługuje zdarzenia w menu
     * i w grze
     *
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
            if (mx > quitButton.x && mx < quitButton.x + quitButton.width && my > quitButton.y
                    && my < quitButton.y + quitButton.height) {
                Mouse = MOUSE.CLICKED;
                Button = BUTTON.QUIT;
            } else if (activeDisplay) {
                mouseClicked = true;    //kliknięcie myszką powoduje przejście do następnej rundy
                roundNumber = roundNumber + 1;
                Pin.loadPlayerXY(mx, my);   //pobranie wskazania gracza
                sumScore(); //obliczenie punktów za wskazanie i sumowanie całkowitego wyniku
            } else {
                Mouse = MOUSE.EXITED;
            }
        }

        //Obsługa myszki w menu
        if (State == STATE.MENU) {
            if (mx > menu.playButton.x && mx < menu.playButton.x + menu.playButton.width && my > menu.playButton.y
                    && my < menu.playButton.y + menu.playButton.height) {
                Mouse = MOUSE.CLICKED;
                menu.Button = Menu.BUTTON.PLAY;
            } else if (mx > menu.exitButton.x && mx < menu.exitButton.x + menu.exitButton.width && my > menu.exitButton.y
                    && my < menu.exitButton.y + menu.exitButton.height) {
                Mouse = MOUSE.CLICKED;
                menu.Button = Menu.BUTTON.EXIT;
            } else {
                Mouse = MOUSE.EXITED;
            }
        }

        //Obsługa myszki na ekranie końcowym
        if (State == STATE.END) {
            if (mx > exitButton.x && mx < exitButton.x + exitButton.width && my > exitButton.y
                    && my < exitButton.y + exitButton.height) {
                Mouse = MOUSE.CLICKED;
                Button = BUTTON.EXIT;
            } else if (mx > playAgainButton.x && mx < playAgainButton.x + playAgainButton.width && my > playAgainButton.y
                    && my < playAgainButton.y + playAgainButton.height) {
                Mouse = MOUSE.CLICKED;
                Button = BUTTON.PLAYAGAIN;
            } else {
                Mouse = MOUSE.EXITED;
            }

        }
    }

    /**
     * Oblicza dystans w kilometrach oraz otrzymane punkty po każdej rundzie
     * Sumuje całkowity wynik
     *
     */
    public void sumScore() {
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
     * Zamiana zmiennej typu Double na String Usunięcie zbędnych zer na końcu
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
     * Wykonuje pojedynczą pętle gry (pojedynczą rundę) Zlicza czas odpowiedzi
     * gracza
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
                if (timePassed == 40) { //50ms x 40 = 2s
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
