/**
 * Representa un símbolo de la máquina tragamonedas.
 * Cada símbolo tiene un nombre, un color y una representación gráfica
 * usando un Circle. Esta es la clase base (símbolo "normal").
 *
 * @author DOPO
 * @version 4.0
 */
public class Symbol
{
    private String name;
    private String color;
    protected Circle shape;
    protected int currentSize;
    protected int xPos;
    protected int yPos;
    protected boolean isCurrentlyVisible;

    /**
     * Construye un símbolo con un nombre y un color.
     *
     * @param name nombre del símbolo.
     * @param color color del símbolo.
     */
    public Symbol(String name, String color)
    {
        this.name = name;
        this.color = color;
        this.currentSize = 30;
        this.xPos = 0;
        this.yPos = 0;
        this.isCurrentlyVisible = false;
        shape = new Circle();
        shape.changeSize(currentSize);
        shape.changeColor(color);
    }

    /**
     * Obtiene el nombre del símbolo.
     *
     * @return nombre del símbolo.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Obtiene el color del símbolo.
     *
     * @return color del símbolo.
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Obtiene el tipo del símbolo.
     *
     * @return "normal".
     */
    public String getType()
    {
        return "normal";
    }

    /**
     * Obtiene el tamaño actual del símbolo.
     *
     * @return tamaño actual.
     */
    public int getCurrentSize()
    {
        return currentSize;
    }

    /**
     * Indica si este símbolo es wild (comodín).
     *
     * @return false para símbolo normal.
     */
    public boolean isWild()
    {
        return false;
    }

    /**
     * Hace visible el símbolo.
     */
    public void makeVisible()
    {
        isCurrentlyVisible = true;
        shape.makeVisible();
    }

    /**
     * Hace invisible el símbolo.
     */
    public void makeInvisible()
    {
        isCurrentlyVisible = false;
        shape.makeInvisible();
    }

    /**
     * Borra el símbolo del canvas.
     */
    public void erase()
    {
        shape.makeInvisible();
    }

    /**
     * Dibuja el símbolo centrado en las coordenadas dadas.
     * Crea un nuevo Circle y lo posiciona para que su centro
     * quede en (centerX, centerY).
     *
     * @param centerX coordenada X del centro.
     * @param centerY coordenada Y del centro.
     */
    public void drawAt(int centerX, int centerY)
    {
        this.xPos = centerX;
        this.yPos = centerY;
        shape.makeInvisible();
        shape = new Circle();
        shape.changeSize(currentSize);
        shape.changeColor(color);
        // Circle tiene posición por defecto (20, 15).
        // Queremos la esquina superior izquierda en
        // (centerX - currentSize/2, centerY - currentSize/2).
        int topLeftX = centerX - currentSize / 2;
        int topLeftY = centerY - currentSize / 2;
        shape.moveHorizontal(topLeftX - 20);
        shape.moveVertical(topLeftY - 15);
        if (isCurrentlyVisible) {
            shape.makeVisible();
        }
    }

    /**
     * Cambia la posición gráfica del símbolo.
     *
     * @param x posición horizontal del centro.
     * @param y posición vertical del centro.
     */
    public void changePosition(int x, int y)
    {
        drawAt(x, y);
    }

    /**
     * Cambia la visibilidad del símbolo.
     *
     * @param visible indica si debe ser visible.
     */
    public void setVisible(boolean visible)
    {
        if (visible) {
            makeVisible();
        } else {
            makeInvisible();
        }
    }

    /**
     * Hook: se llama cuando la rueda gira.
     */
    public void onSpin()
    {
    }

    /**
     * Hook: se llama cuando este símbolo queda seleccionado.
     */
    public void onSelect()
    {
    }

    /**
     * Indica si el símbolo debe mostrarse gráficamente.
     *
     * @return true si debe mostrarse.
     */
    public boolean shouldDisplay()
    {
        return true;
    }
}
