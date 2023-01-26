package alinhamentos.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class Utils {
    
    private static HashMap<Character, HashMap<Character, Integer>> matrizSubstituicaoBlosum62;
    private static HashMap<Character, HashMap<Character, Integer>> matrizSubstituicaoDNA;
    private static char[] ncArray;
    private static char[] aaArray;
    
    static {
        try {
            carregarBlosum62();
            carregarMDNA();
        } catch ( IOException exc )  {
            exc.printStackTrace();
        }
    }
    
    private static void carregarBlosum62() throws IOException {
        
        Scanner s = new Scanner( new File( "BLOSUM62.txt" ) );
        matrizSubstituicaoBlosum62 = new LinkedHashMap<>();
        
        while ( s.hasNextLine() ) {
            
            String line = s.nextLine();
            
            if ( !line.startsWith( "#" ) ) {
                
                if ( line.startsWith( " " ) ) {
                    aaArray = line.replace( " ", "" ).toCharArray();
                } else {
                    
                    char currAa = line.charAt( 0 );
                    int aaCount = 0;
                    HashMap<Character, Integer> dataLine = new LinkedHashMap<>();
                    matrizSubstituicaoBlosum62.put( currAa, dataLine );
                    
                    for ( String v : line.substring( 1 ).trim().split( "\\s+" ) ) {
                        dataLine.put( aaArray[aaCount++], Integer.valueOf( v ) );
                    }
                    
                }
                
            }
            
        }
        
    }
    
    private static void carregarMDNA() throws IOException {
        
        Scanner s = new Scanner( new File( "DNA_TABLE.txt" ) );
        matrizSubstituicaoDNA = new LinkedHashMap<>();
        
        while ( s.hasNextLine() ) {
            
            String line = s.nextLine();
            
            if ( !line.startsWith( "#" ) ) {
                
                if ( line.startsWith( " " ) ) {
                    ncArray = line.replace( " ", "" ).toCharArray();
                } else {
                    
                    char currNc = line.charAt( 0 );
                    int ncCount = 0;
                    HashMap<Character, Integer> dataLine = new LinkedHashMap<>();
                    matrizSubstituicaoDNA.put( currNc, dataLine );
                    
                    for ( String v : line.substring( 1 ).trim().split( "\\s+" ) ) {
                        dataLine.put( ncArray[ncCount++], Integer.valueOf( v ) );
                    }
                    
                }
                
            }
            
        }
        
    }
    
    public static int scoreProteina( char i, char j ) {
        return matrizSubstituicaoBlosum62.get( i ).get( j );
    }
    
    public static int scoreDNA( char i, char j ) {
        return matrizSubstituicaoDNA.get( i ).get( j );
    }
    
}
