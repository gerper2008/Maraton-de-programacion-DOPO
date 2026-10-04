/**
 * Rueda espejo (mirror): gira en sentido contrario al indicado.
 * Si se pide girar +1, gira -1 y viceversa.
 *
 * @author DOPO
 * @version 4.0
 */
public class MirrorWheel extends Wheel
{
    /**
     * Construye una rueda espejo.
     */
    public MirrorWheel()
    {
        super();
    }

    /**
     * @return "mirror".
     */
    @Override
    public String getType()
    {
        return "mirror";
    }

    /**
     * Gira en sentido contrario: invierte los pasos.
     *
     * @param steps número de pasos (se invierte el signo).
     */
    @Override
    public void spin(int steps)
    {
        super.spin(-steps);
    }
}
