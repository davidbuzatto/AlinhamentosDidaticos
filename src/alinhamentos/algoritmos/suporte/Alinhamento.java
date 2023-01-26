package alinhamentos.algoritmos.suporte;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class Alinhamento {
    
    public String s1;
    public String s2;
    public Caminho c;

    public Alinhamento( String s1, String s2, Caminho c ) {
        this.s1 = s1;
        this.s2 = s2;
        this.c = c;
    }

    @Override
    public String toString() {
        return s1 + "\n" + s2;
    }
    
}
