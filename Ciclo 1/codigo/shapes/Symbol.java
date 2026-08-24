/**
 * Representa un símbolo de la máquina tragamonedas.
 * Cada símbolo tiene un nombre, un color y una representación gráfica.
 *
 * @author
 * @version 1.0
 */
public class Symbol
{
    private String name;
    private String color;
    private Circle shape;

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
        shape = new Circle();
        shape.changeSize(30);
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
     * Hace visible el símbolo.
     */
    public void makeVisible()
    {
        shape.makeVisible();
    }

    /**
     * Hace invisible el símbolo.
     */
    public void makeInvisible()
    {
        shape.makeInvisible();
    }

    /**
     * Cambia la posición gráfica del símbolo.
     *
     * @param x posición horizontal.
     * @param y posición vertical.
     */
    public void changePosition(int x, int y)
    {
        shape.makeInvisible();
        shape = new Circle();
        shape.changeSize(30);
        shape.changeColor(color);
        shape.moveHorizontal(x - 20);
        shape.moveVertical(y - 15);
        shape.makeVisible();
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
}
