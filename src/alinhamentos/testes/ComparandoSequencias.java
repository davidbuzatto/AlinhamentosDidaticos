package alinhamentos.testes;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class ComparandoSequencias {
    
    public static void main( String[] args ) {
        System.out.println( indice( "AAB", "AAAABCDD" ) );
        System.out.println( indice( "AAUCD", "AAAABCDD", 1 ) );
    }
    
    public static int indice( String s1, String s2 ) {
        
        for ( int i = 0; i < s2.length() - s1.length() + 1; i++ ) {
            
            boolean encontrou = true;
            
            for ( int j = 0; j < s1.length(); j++ ) {
                if ( s1.charAt( j ) != s2.charAt( i+j ) ) {
                    encontrou = false;
                    break;
                }
            }
            
            if ( encontrou ) {
                return i;
            }
            
        }
        
        return -1;
        
    }
    
    public static int indice( String s1, String s2, int maximoErros ) {
        
        for ( int i = 0; i < s2.length() - s1.length() + 1; i++ ) {
            
            boolean encontrou = true;
            int erros = 0;
            
            for ( int j = 0; j < s1.length(); j++ ) {
                if ( s1.charAt( j ) != s2.charAt( i+j ) ) {
                    erros++;
                }
            }
            
            if ( erros <= maximoErros ) {
                return i;
            }
            
        }
        
        return -1;
        
    }
    
}
