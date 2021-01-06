package StoliceEuropy;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 * Klasa odpowiadającą za pobranie informacji o wyświetlanych pinezkach
 *
 * @author Anna Topa
 */
public class Pin {

    /**
     * Współrzędna X pinezki gracza
     */
    public static int playerX;

    /**
     * Współrzędna Y pinezki gracza
     */
    public static int playerY;

    //Tablice przechowujące współrzędne X i Y (w pikselach) stolic
    private static final int[] actualX = new int[44];
    private static final int[] actualY = new int[44];

    /**
     * Lista stolic
     */
    public static ArrayList<Capital> capitals;

    /**
     * Inicjalizuje wszystkie stolice
     */
    public static void initCapitals() {
        capitals = new ArrayList<>();
        capitals.add(new Capital(1, "MADRYT, Hiszpania", 227, 640));
        capitals.add(new Capital(2, "BERLIN, Niemcy", 494, 409));
        capitals.add(new Capital(3, "MIŃSK, Białoruś", 660, 360));
        capitals.add(new Capital(4, "PRAGA, Czechy", 506, 461));
        capitals.add(new Capital(5, "WIEDEŃ, Austria", 539, 500));
        capitals.add(new Capital(6, "BRATYSŁAWA, Słowacja", 554, 495));
        capitals.add(new Capital(7, "REYKJAVIK, Islandia", 170, 112));
        capitals.add(new Capital(8, "DUBLIN, Irlandia", 253, 375));
        capitals.add(new Capital(9, "LONDYN, Wielka Brytania", 324, 424));
        capitals.add(new Capital(10, "WARSZAWA, Polska", 587, 408));
        capitals.add(new Capital(11, "LIZBONA, Portugalia", 144, 656));
        capitals.add(new Capital(12, "ANDORA, Andora", 324, 611));
        capitals.add(new Capital(13, "BERNO, Szwajcaria", 417, 528));
        capitals.add(new Capital(14, "MONAKO, Monako", 407, 594));
        capitals.add(new Capital(15, "RZYM, Włochy", 492, 631));
        capitals.add(new Capital(16, "VALETTA, Malta", 527, 751));
        capitals.add(new Capital(17, "SAN MARINO, San Marino", 490, 590));
        capitals.add(new Capital(18, "KIJÓW, Ukraina", 717, 420));
        capitals.add(new Capital(19, "OSLO, Norwegia", 460, 260));
        capitals.add(new Capital(20, "TIRANA, Albania", 607, 635));
        capitals.add(new Capital(21, "ATENY, Grecja", 679, 691));
        capitals.add(new Capital(22, "BELGRAD, Serbia", 604, 572));
        capitals.add(new Capital(23, "BUDAPESZT, Węgry", 576, 508));
        capitals.add(new Capital(24, "WILNO, Litwa", 633, 352));
        capitals.add(new Capital(25, "BRUKSELA, Belgia", 380, 446));
        capitals.add(new Capital(26, "SOFIA, Bułgaria", 657, 593));
        capitals.add(new Capital(27, "KOPENHAGA, Dania", 478, 351));
        capitals.add(new Capital(28, "VADUZ, Liechtenstein", 447, 524));
        capitals.add(new Capital(29, "KISZYNIÓW, Mołdawia", 715, 495));
        capitals.add(new Capital(30, "LUBLANA, Słowenia", 517, 542));
        capitals.add(new Capital(31, "MOSKWA, Rosja", 756, 288));
        capitals.add(new Capital(32, "SKOPJE, Macedonia Północna", 631, 617));
        capitals.add(new Capital(33, "LUKSEMBURG, Luksemburg", 401, 468));
        capitals.add(new Capital(34, "ZAGRZEB, Chorwacja", 539, 549));
        capitals.add(new Capital(35, "RYGA, Łotwa", 607, 309));
        capitals.add(new Capital(36, "SARAJEWO, Bośnia i Hercegowina", 576, 582));
        capitals.add(new Capital(37, "AMSTERDAM, Holandia", 388, 412));
        capitals.add(new Capital(38, "PODGORICA, Czarnogóra", 592, 609));
        capitals.add(new Capital(39, "BUKARESZT, Rumunia", 692, 548));
        capitals.add(new Capital(40, "TALLIN, Estonia", 609, 257));
        capitals.add(new Capital(41, "SZTOKHOLM, Szwecja", 536, 265));
        capitals.add(new Capital(42, "PARYŻ, Francja", 348, 486));
        capitals.add(new Capital(43, "HELSINKI, Finlandia", 604, 236));

    }

    /**
     * Pobiera współrzędną X danej stolicy z listy
     *
     * @param i Numer ID stolicy
     * @return Współrzędna X (w px)
     */
    public static int getActualX(int i) {
        Pin.actualX[i] = (int) capitals.get(i - 1).getX(); //i-1 bo najpierw klikniecie i=i+1, dopiero potem wyswietla
        return Pin.actualX[i];
    }

    /**
     * Pobiera współrzędną Y danej stolicy z listy
     *
     * @param i Numer ID stolicy
     * @return Współrzędna Y (w px)
     */
    public static int getActualY(int i) {
        Pin.actualY[i] = (int) capitals.get(i - 1).getY();
        return Pin.actualY[i];
    }

    /**
     * Zapisuje współrzędne X i Y wskazane przez gracza
     *
     * @param X Współrzędna X wskazania gracza
     * @param Y Współrzędna Y wskazania gracza
     */
    public static void loadPlayerXY(int X, int Y) {
        Pin.playerX = X;
        Pin.playerY = Y;
    }

    /**
     * Pobiera współrzędną X wskazaną przez gracza
     *
     * @return Współrzędna X wskazania gracza (w px)
     */
    public static int getPlayerX() {
        return Pin.playerX;
    }

    /**
     * Pobiera współrzędną Y wskazaną przez gracza
     *
     * @return Współrzędna Y wskazania gracza (w px)
     */
    public static int getPlayerY() {
        return Pin.playerY;
    }

    /**
     * Oblicza różnicę w kilometrach między wskazaniem gracza a faktycznym
     * położeniem stolicy na mapie
     *
     * @param x1 Współrzędna X wskazania gracza
     * @param y1 Współrzędna Y wskazania gracza
     * @param x2 Współrzędna X faktycznego położenia stolicy
     * @param y2 Współrzędna Y faktycznego położenia stolicy
     * @return Różnica między wskazaniem a faktycznym położeniem (w km)
     */
    public static double getDifferenceInKm(int x1, int y1, int x2, int y2) {
        int h = abs(x2 - x1);
        int v = abs(y2 - y1);
        double diffInPx = sqrt(h * h + v * v);
        return diffInPx / 18.25 * 100; //km
    }

}
