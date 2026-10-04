/**
 * Símbolo tímido: alterna entre visible e invisible cada vez
 * que es seleccionado. Se representa con un Rectángulo.
 *
 * @author DOPO
 * @version 4.0
 */
public class ShySymbol extends Symbol
{
    private Rectangle rectShape;
    private boolean showing;

    /**
     * Construye un símbolo tímido.
     *
     * @param name nombre del símbolo.
     * @param color color del símbolo.
     */
    public ShySymbol(String name, String color)
    {
        super(name, color);
        this.showing = true;
        rectShape = new Rectangle();
        rectShape.changeSize(currentSize, currentSize);
        rectShape.changeColor(color);
    }

    /**
     * @return "shy".
     */
    @Override
    public String getType()
    {
        return "shy";
    }

    /**
     * Al ser seleccionado, alterna su estado de visibilidad.
     */
    @Override
    public void onSelect()
    {
        showing = !showing;
    }

    /**
     * @return true si el símbolo está en estado visible.
     */
    @Override
    public boolean shouldDisplay()
    {
        return showing;
    }

    /**
     * Hace visible el rectángulo (si shouldDisplay lo permite).
     */
    @Override
    public void makeVisible()
    {
        isCurrentlyVisible = true;
        if (showing) {
            rectShape.makeVisible();
        }
    }

    /**
     * Hace invisible el rectángulo.
     */
    @Override
    public void makeInvisible()
    {
        isCurrentlyVisible = false;
        rectShape.makeInvisible();
    }

    /**
     * Borra el rectángulo del canvas.
     */
    @Override
    public void erase()
    {
        rectShape.makeInvisible();
    }

    /**
     * Dibuja el rectángulo centrado en las coordenadas dadas,
     * solo si shouldDisplay() es true.
     *
     * @param centerX coordenada X del centro.
     * @param centerY coordenada Y del centro.
     */
    @Override
    public void drawAt(int centerX, int centerY)
    {
        this.xPos = centerX;
        this.yPos = centerY;
        rectShape.makeInvisible();
        if (!showing) {
            return;
        }
        rectShape = new Rectangle();
        rectShape.changeSize(currentSize, currentSize);
        rectShape.changeColor(getColor());
        // Rectangle defaults at (70, 15).
        // Para centrar: esquina superior izquierda en
        // (centerX - currentSize/2, centerY - currentSize/2).
        int topLeftX = centerX - currentSize / 2;
        int topLeftY = centerY - currentSize / 2;
        rectShape.moveHorizontal(topLeftX - 70);
        rectShape.moveVertical(topLeftY - 15);
        if (isCurrentlyVisible) {
            rectShape.makeVisible();
        }
    }
}
