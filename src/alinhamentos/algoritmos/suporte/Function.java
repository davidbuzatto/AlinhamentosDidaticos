package alinhamentos.algoritmos.suporte;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
@FunctionalInterface
public interface Function< CharP1, CharP2, Return > {
    public Return apply( CharP1 i, CharP2 j );
}
