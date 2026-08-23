import java.util.ArrayList;

/**
 * Representa una rueda de la máquina tragamonedas.
 * Una rueda contiene una colección de símbolos y una posición actual.
 *
 * @author
 * @version 1.0
 */
public class Wheel
{
    private ArrayList<Symbol> symbols;
    private int position;

    /**
     * Construye una rueda vacía.
     */
    public Wheel()
    {
        symbols = new ArrayList<Symbol>();
        position = 0;
    }

    /**
     * Agrega un símbolo en la posición indicada.
     *
     * @param symbol símbolo que se desea agregar.
     * @param pos posición donde se agregará.
     */
    public void addSymbol(Symbol symbol, int pos)
    {
        if (pos < 1) {
            pos = 1;
        }

        if (pos > symbols.size() + 1) {
            pos = symbols.size() + 1;
        }

        symbols.add(pos - 1, symbol);
    }

    /**
     * Elimina el símbolo ubicado en una posición.
     *
     * @param pos posición del símbolo.
     */
    public void delSymbol(int pos)
    {
        if (pos >= 1 && pos <= symbols.size()) {
            symbols.remove(pos - 1);
            normalizePosition();
        }
    }

    /**
     * Coloca la posición actual de la rueda.
     *
     * @param pos posición que se desea seleccionar.
     */
    public void placeSymbol(int pos)
    {
        if (symbols.isEmpty()) {
            position = 0;
        } else {
            position = normalize(pos);
        }
    }

    /**
     * Gira la rueda una posición.
     */
    public void spin()
    {
        if (!symbols.isEmpty()) {
            position = (position + 1) % symbols.size();
        }
    }

    /**
     * Obtiene los símbolos de la rueda.
     *
     * @return arreglo con los símbolos.
     */
    public Symbol[] getSymbols()
    {
        return symbols.toArray(new Symbol[0]);
    }

    /**
     * Obtiene el símbolo actualmente visible.
     *
     * @return símbolo actual o null si la rueda está vacía.
     */
    public Symbol currentSymbol()
    {
        if (symbols.isEmpty()) {
            return null;
        }

        return symbols.get(position);
    }

    /**
     * Obtiene el nombre del símbolo actualmente visible.
     *
     * @return nombre del símbolo o cadena vacía.
     */
    public String configuration()
    {
        Symbol symbol = currentSymbol();

        if (symbol == null) {
            return "";
        }

        return symbol.getName();
    }

    /**
     * Obtiene la cantidad de símbolos de la rueda.
     *
     * @return cantidad de símbolos.
     */
    public int size()
    {
        return symbols.size();
    }

    /**
     * Hace visibles todos los símbolos de la rueda.
     */
    public void makeVisible()
    {
        for (Symbol symbol : symbols) {
            symbol.makeVisible();
        }
    }

    /**
     * Hace invisibles todos los símbolos de la rueda.
     */
    public void makeInvisible()
    {
        for (Symbol symbol : symbols) {
            symbol.makeInvisible();
        }
    }

    /**
     * Busca un símbolo por su nombre.
     *
     * @param name nombre que se desea buscar.
     * @return posición del símbolo o -1 si no existe.
     */
    public int findSymbol(String name)
    {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getName().equals(name)) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * Normaliza una posición para mantenerla dentro de la rueda.
     *
     * @param pos posición recibida.
     * @return posición normalizada.
     */
    private int normalize(int pos)
    {
        int result = (pos - 1) % symbols.size();

        if (result < 0) {
            result += symbols.size();
        }

        return result;
    }

    /**
     * Corrige la posición actual después de eliminar símbolos.
     */
    private void normalizePosition()
    {
        if (symbols.isEmpty()) {
            position = 0;
        } else if (position >= symbols.size()) {
            position = 0;
        }
    }
}
