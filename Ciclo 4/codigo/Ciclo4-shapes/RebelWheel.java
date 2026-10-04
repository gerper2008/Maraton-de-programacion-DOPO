/**
 * Rueda rebelde (rebel): no se deja bloquear, ni eliminar,
 * ni intercambiar. Siempre gira normalmente.
 *
 * @author DOPO
 * @version 4.0
 */
public class RebelWheel extends Wheel
{
    /**
     * Construye una rueda rebelde.
     */
    public RebelWheel()
    {
        super();
    }

    /**
     * @return "rebel".
     */
    @Override
    public String getType()
    {
        return "rebel";
    }

    /**
     * No hace nada: la rebel ignora los bloqueos.
     */
    @Override
    public void lock()
    {
        // Ignorar
    }

    /**
     * Siempre retorna false: la rebel nunca está bloqueada.
     *
     * @return false.
     */
    @Override
    public boolean isLocked()
    {
        return false;
    }

    /**
     * No se puede eliminar.
     *
     * @return false.
     */
    @Override
    public boolean canBeDeleted()
    {
        return false;
    }

    /**
     * No se puede intercambiar.
     *
     * @return false.
     */
    @Override
    public boolean canBeSwapped()
    {
        return false;
    }
}
