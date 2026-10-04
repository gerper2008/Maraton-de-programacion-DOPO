import java.util.ArrayList;

/**
 * Representa una rueda de la máquina tragamonedas.
 * Una rueda contiene una colección de símbolos y una posición actual.
 * Esta es la clase base (rueda "normal").
 *
 * @author DOPO
 * @version 4.0
 */
public class Wheel
{
    protected ArrayList<Symbol> symbols;
    protected int position;
    protected boolean locked;

    /**
     * Construye una rueda vacía.
     */
    public Wheel()
    {
        symbols = new ArrayList<Symbol>();
        position = 0;
        locked = false;
    }

    /**
     * Obtiene el tipo de la rueda.
     *
     * @return "normal".
     */
    public String getType()
    {
        return "normal";
    }

    /**
     * Agrega un símbolo en la posición indicada.
     *
     * @param symbol símbolo que se desea agregar.
     * @param pos posición donde se agregará (base 1).
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
     * @param pos posición del símbolo (base 1).
     */
    public void delSymbol(int pos)
    {
        if (pos >= 1 && pos <= symbols.size()) {
            symbols.remove(pos - 1);
            normalizePosition();
        }
    }

    /**
     * Coloca la posición actual de la rueda y llama onSelect()
     * sobre el símbolo que queda visible.
     *
     * @param pos posición que se desea seleccionar (base 1).
     */
    public void placeSymbol(int pos)
    {
        if (symbols.isEmpty()) {
            position = 0;
        } else {
            position = normalize(pos);
            Symbol current = currentSymbol();
            if (current != null) {
                current.onSelect();
            }
        }
    }

    /**
     * Gira la rueda una posición hacia adelante.
     */
    public void spin()
    {
        spin(1);
    }

    /**
     * Gira la rueda un número arbitrario de pasos.
     * Llama onSpin() en todos los símbolos, mueve la posición,
     * y luego llama onSelect() en el símbolo que queda visible.
     * No tiene efecto si la rueda está bloqueada o vacía.
     *
     * @param steps número de pasos a girar (puede ser negativo).
     */
    public void spin(int steps)
    {
        if (!locked && !symbols.isEmpty()) {
            // Llamar onSpin en todos los símbolos
            for (Symbol s : symbols) {
                s.onSpin();
            }
            // Mover posición
            int size = symbols.size();
            int normalized = ((steps % size) + size) % size;
            position = (position + normalized) % size;
            // Llamar onSelect en el símbolo que queda visible
            Symbol current = currentSymbol();
            if (current != null) {
                current.onSelect();
            }
        }
    }

    /**
     * Bloquea la rueda.
     */
    public void lock()
    {
        locked = true;
    }

    /**
     * Desbloquea la rueda.
     */
    public void unlock()
    {
        locked = false;
    }

    /**
     * Indica si la rueda está bloqueada.
     *
     * @return true si está bloqueada.
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Indica si la rueda puede ser eliminada.
     *
     * @return true para rueda normal.
     */
    public boolean canBeDeleted()
    {
        return true;
    }

    /**
     * Indica si la rueda puede ser intercambiada.
     *
     * @return true para rueda normal.
     */
    public boolean canBeSwapped()
    {
        return true;
    }

    /**
     * Obtiene la posición actual (base 1).
     *
     * @return posición actual de la rueda.
     */
    public int currentPosition()
    {
        return position + 1;
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
     * Obtiene la cantidad de símbolos.
     *
     * @return cantidad de símbolos.
     */
    public int size()
    {
        return symbols.size();
    }

    /**
     * Hace visibles todos los símbolos.
     */
    public void makeVisible()
    {
        for (Symbol symbol : symbols) {
            symbol.makeVisible();
        }
    }

    /**
     * Hace invisibles todos los símbolos.
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
     * @return posición del símbolo (base 1) o -1 si no existe.
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
     * Copia el estado (posición visible) de otra rueda.
     * Busca en esta rueda el símbolo cuyo nombre coincida
     * con el que la otra rueda muestra, y se posiciona ahí.
     *
     * @param other rueda de la cual copiar el estado.
     */
    public void copyStateFrom(Wheel other)
    {
        if (other == null) {
            return;
        }
        String otherName = other.configuration();
        if (otherName.isEmpty()) {
            return;
        }
        int found = findSymbol(otherName);
        if (found != -1) {
            position = found - 1;
        }
    }

    /**
     * Normaliza una posición (base 1) al índice interno.
     *
     * @param pos posición recibida (base 1).
     * @return índice normalizado (base 0).
     */
    protected int normalize(int pos)
    {
        int result = (pos - 1) % symbols.size();
        if (result < 0) {
            result += symbols.size();
        }
        return result;
    }

    /**
     * Corrige la posición actual si queda fuera de rango.
     */
    protected void normalizePosition()
    {
        if (symbols.isEmpty()) {
            position = 0;
        } else if (position >= symbols.size()) {
            position = 0;
        }
    }
}
