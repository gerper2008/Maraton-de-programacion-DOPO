/**
 * Símbolo comodín (wild): cuenta como cualquier otro símbolo
 * para efectos del jackpot. Se representa con un círculo
 * exterior (del color) y uno interior blanco más pequeño.
 *
 * @author DOPO
 * @version 4.0
 */
public class WildSymbol extends Symbol
{
    private Circle innerCircle;

    /**
     * Construye un símbolo wild.
     *
     * @param name nombre del símbolo.
     * @param color color del símbolo.
     */
    public WildSymbol(String name, String color)
    {
        super(name, color);
        innerCircle = new Circle();
        innerCircle.changeSize(currentSize / 2);
        innerCircle.changeColor("white");
    }

    /**
     * @return "wild".
     */
    @Override
    public String getType()
    {
        return "wild";
    }

    /**
     * @return true, es un símbolo comodín.
     */
    @Override
    public boolean isWild()
    {
        return true;
    }

    /**
     * Hace visibles ambos círculos.
     */
    @Override
    public void makeVisible()
    {
        isCurrentlyVisible = true;
        shape.makeVisible();
        innerCircle.makeVisible();
    }

    /**
     * Hace invisibles ambos círculos.
     */
    @Override
    public void makeInvisible()
    {
        isCurrentlyVisible = false;
        shape.makeInvisible();
        innerCircle.makeInvisible();
    }

    /**
     * Borra ambos círculos del canvas.
     */
    @Override
    public void erase()
    {
        shape.makeInvisible();
        innerCircle.makeInvisible();
    }

    /**
     * Dibuja ambos círculos centrados en las coordenadas dadas.
     * Círculo exterior del color del símbolo, interior blanco.
     *
     * @param centerX coordenada X del centro.
     * @param centerY coordenada Y del centro.
     */
    @Override
    public void drawAt(int centerX, int centerY)
    {
        this.xPos = centerX;
        this.yPos = centerY;

        // Círculo exterior
        shape.makeInvisible();
        shape = new Circle();
        shape.changeSize(currentSize);
        shape.changeColor(getColor());
        int outerTopX = centerX - currentSize / 2;
        int outerTopY = centerY - currentSize / 2;
        shape.moveHorizontal(outerTopX - 20);
        shape.moveVertical(outerTopY - 15);

        // Círculo interior (blanco, mitad de tamaño)
        innerCircle.makeInvisible();
        int innerSize = currentSize / 2;
        innerCircle = new Circle();
        innerCircle.changeSize(innerSize);
        innerCircle.changeColor("white");
        int innerTopX = centerX - innerSize / 2;
        int innerTopY = centerY - innerSize / 2;
        innerCircle.moveHorizontal(innerTopX - 20);
        innerCircle.moveVertical(innerTopY - 15);

        if (isCurrentlyVisible) {
            shape.makeVisible();
            innerCircle.makeVisible();
        }
    }
}
