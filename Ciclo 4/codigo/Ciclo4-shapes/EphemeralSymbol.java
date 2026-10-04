/**
 * Símbolo efímero: en cada giro su tamaño decrece en 3
 * (mínimo 3). Se representa con un Triángulo.
 *
 * @author DOPO
 * @version 4.0
 */
public class EphemeralSymbol extends Symbol
{
    private Triangle triangleShape;

    /**
     * Construye un símbolo efímero.
     *
     * @param name nombre del símbolo.
     * @param color color del símbolo.
     */
    public EphemeralSymbol(String name, String color)
    {
        super(name, color);
        triangleShape = new Triangle();
        triangleShape.changeSize(currentSize, currentSize);
        triangleShape.changeColor(color);
    }

    /**
     * @return "ephemeral".
     */
    @Override
    public String getType()
    {
        return "ephemeral";
    }

    /**
     * Al girar, el tamaño decrece en 3 (mínimo 3).
     */
    @Override
    public void onSpin()
    {
        if (currentSize > 3) {
            currentSize -= 3;
            if (currentSize < 3) {
                currentSize = 3;
            }
        }
    }

    /**
     * Hace visible el triángulo.
     */
    @Override
    public void makeVisible()
    {
        isCurrentlyVisible = true;
        triangleShape.makeVisible();
    }

    /**
     * Hace invisible el triángulo.
     */
    @Override
    public void makeInvisible()
    {
        isCurrentlyVisible = false;
        triangleShape.makeInvisible();
    }

    /**
     * Borra el triángulo del canvas.
     */
    @Override
    public void erase()
    {
        triangleShape.makeInvisible();
    }

    /**
     * Dibuja el triángulo centrado en las coordenadas dadas.
     * El triángulo tiene su vértice superior en (xPosition, yPosition)
     * y su base en yPosition + height. El centro visual está
     * aproximadamente a 2/3 de la altura desde arriba.
     *
     * @param centerX coordenada X del centro.
     * @param centerY coordenada Y del centro.
     */
    @Override
    public void drawAt(int centerX, int centerY)
    {
        this.xPos = centerX;
        this.yPos = centerY;
        triangleShape.makeInvisible();
        triangleShape = new Triangle();
        triangleShape.changeSize(currentSize, currentSize);
        triangleShape.changeColor(getColor());
        // Triangle defaults at (140, 15) with top vertex there.
        // Para centrar: el vértice superior va en
        // (centerX, centerY - currentSize/2)
        int topX = centerX;
        int topY = centerY - currentSize / 2;
        triangleShape.moveHorizontal(topX - 140);
        triangleShape.moveVertical(topY - 15);
        if (isCurrentlyVisible) {
            triangleShape.makeVisible();
        }
    }
}
