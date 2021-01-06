package StoliceEuropy;

/**
 * Klasa grupująca informacje o wszystkich stolicach wykorzystanych w grze
 *
 */
public class Capital {

    /**
     * ID stolicy
     */
    private int id;

    /**
     * Nazwa stolicy
     */
    public String name;

    /**
     * Współrzędna X stolicy
     */
    public int x;

    /**
     * Współrzędna Y stolicy
     */
    public int y;

    /**
     * Konstruktor klasy grupującej dane o stolicach
     *
     * @param id Numer przyporządkowany stolicy
     * @param name Nazwa stolicy
     * @param x Współrzędna X stolicy w pikselach
     * @param y Współrzędna Y stolicy w pikselach
     */
    public Capital(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * Zwraca nazwę stolicy
     *
     * @return Nazwa stolicy
     */
    public String getName() {
        return this.name;
    }

    /**
     * Zwraca współrzędną X stolicy (w pikselach)
     *
     * @return Współrzędna X (w px)
     */
    public int getX() {
        return this.x;
    }

    /**
     * Zwraca współrzędną Y stolicy (w pikselach)
     *
     * @return Współrzędna Y (w px)
     */
    public int getY() {
        return this.y;
    }

}
