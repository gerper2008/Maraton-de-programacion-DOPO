/**
 * Rueda izquierdista (lefty): al girar, copia el estado de
 * la rueda que esté a su izquierda. Si no tiene vecina
 * izquierda, gira normalmente.
 *
 * @author DOPO
 * @version 4.0
 */
public class LeftyWheel extends Wheel
{
    private Wheel leftNeighbor;

    /**
     * Construye una rueda lefty sin vecina izquierda.
     */
    public LeftyWheel()
    {
        super();
        this.leftNeighbor = null;
    }

    /**
     * @return "lefty".
     */
    @Override
    public String getType()
    {
        return "lefty";
    }

    /**
     * Asigna la rueda vecina izquierda.
     *
     * @param neighbor rueda a la izquierda, o null si no hay.
     */
    public void setLeftNeighbor(Wheel neighbor)
    {
        this.leftNeighbor = neighbor;
    }

    /**
     * Gira la rueda. Si tiene vecina izquierda, copia su estado.
     * Si no, gira normalmente. En ambos casos llama onSpin()
     * en todos los símbolos y onSelect() en el que queda visible.
     *
     * @param steps número de pasos (ignorado si copia de vecina).
     */
    @Override
    public void spin(int steps)
    {
        if (!locked && !symbols.isEmpty()) {
            // Llamar onSpin en todos los símbolos
            for (Symbol s : symbols) {
                s.onSpin();
            }
            if (leftNeighbor != null) {
                // Copiar el estado de la vecina izquierda
                copyStateFrom(leftNeighbor);
            } else {
                // Sin vecina: girar normalmente
                int size = symbols.size();
                int normalized = ((steps % size) + size) % size;
                position = (position + normalized) % size;
            }
            // Llamar onSelect en el símbolo que queda visible
            Symbol current = currentSymbol();
            if (current != null) {
                current.onSelect();
            }
        }
    }
}
