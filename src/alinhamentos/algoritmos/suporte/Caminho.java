package alinhamentos.algoritmos.suporte;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class Caminho {
    
    public int i;
    public int j;
    public String caminho;

    public Caminho( int i, int j, String caminho ) {
        this.i = i;
        this.j = j;
        this.caminho = caminho;
    }

    @Override
    public String toString() {
        return "Caminho{" + "i=" + i + ", j=" + j + ", caminho=" + caminho + '}';
    }
    
}
