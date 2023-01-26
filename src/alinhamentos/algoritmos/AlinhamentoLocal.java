package alinhamentos.algoritmos;

import alinhamentos.algoritmos.suporte.Caminho;
import alinhamentos.algoritmos.suporte.Tipo;
import alinhamentos.algoritmos.suporte.Alinhamento;
import alinhamentos.algoritmos.suporte.Function;
import alinhamentos.util.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class AlinhamentoLocal {
    
    private List<Caminho> caminhos = new ArrayList<>();
    private String[] caminhosGerados = new String[100];
    private String caminhoAtual = "";
    
    private int level;
    private List<String>[][] matrizPrecedencia;
    private List<Alinhamento> alinhamentos;
    private int[][] matrizPG;
    
    private String s1;
    private String s2;
    
    private class Inicio {
        int i;
        int j;
        int valor;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.i;
            hash = 97 * hash + this.j;
            hash = 97 * hash + this.valor;
            return hash;
        }

        @Override
        public boolean equals( Object obj ) {
            if ( this == obj ) {
                return true;
            }
            if ( obj == null ) {
                return false;
            }
            if ( getClass() != obj.getClass() ) {
                return false;
            }
            final Inicio other = (Inicio) obj;
            if ( this.i != other.i ) {
                return false;
            }
            if ( this.j != other.j ) {
                return false;
            }
            if ( this.valor != other.valor ) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format( "%d (%d, %d)" , valor, i, j );
        }
    }
    
    public AlinhamentoLocal( String s1, String s2, Tipo tipo ) {
        this.s1 = s1;
        this.s2 = s2;
        switch ( tipo ) {
            case DNA:
                alinharDNAs( s1, s2 );
                break;
            case PROTEINA:
                alinharProteinas( s1, s2 );
                break;
        }
    }
    
    public static void main( String[] args ) {
        //AlinhamentoLocal ag = new AlinhamentoLocal( "MDNNPNINECIPYNCLSNPEVEVL", "MNNVLNSGRTTICDAYNVVAHDPF", Tipo.PROTEINA );
        AlinhamentoLocal ag = new AlinhamentoLocal( "AGC", "AAG", Tipo.DNA );
    }
    
    private void alinharProteinas( String s1, String s2 ) {
        alinhar( s1, s2, Utils::scoreProteina );
    }
    
    private void alinharDNAs( String s1, String s2 ) {
        alinhar( s1, s2, Utils::scoreDNA );
    }
    
    private void alinhar( String s1, String s2, Function<Character, Character, Integer> scoreFunction ) {
        
        calcularMatrizes( s1, s2, scoreFunction );
        alinhamentos = new ArrayList<>();
        
        List<Inicio> inicios = obterInicios();
        
        while ( !inicios.isEmpty() ) {
            inicios.sort( new Comparator<Inicio>() {
                @Override
                public int compare( Inicio o1, Inicio o2 ) {
                    return o2.valor - o1.valor;
                }
            });
            //System.out.println( inicios );
            obterCaminhos( matrizPrecedencia, inicios.get( 0 ).i, inicios.get( 0 ).j, inicios );
            caminhosGerados = new String[100];
            caminhoAtual = "";
        }
        
        /*for ( Caminho c : caminhos ) {
            System.out.println( c );
        }*/
        
        StringBuilder sbS1 = new StringBuilder();
        StringBuilder sbS2 = new StringBuilder();
        
        int contS1 = 0;
        int contS2 = 0;
        
        for ( Caminho c : caminhos ) {
            contS1 = c.i;
            contS2 = c.j;
            sbS1.setLength( 0 );
            sbS2.setLength( 0 );
            for ( char m : c.caminho.toCharArray() ) {
                switch ( m ) {
                    case '\\':
                        sbS1.append( s1.charAt( contS1++ ) );
                        sbS2.append( s2.charAt( contS2++ ) );
                        break;
                    case '|':
                        sbS1.append( s1.charAt( contS1++ ) );
                        sbS2.append( "_" );
                        break;
                    case '-':
                        sbS1.append( "_" );
                        sbS2.append( s2.charAt( contS2++ ) );
                        break;
                }
            }
            alinhamentos.add( new Alinhamento(
                    sbS1.toString(),
                    sbS2.toString(), 
                    c
            ));
        }
        
        /*for ( Alinhamento a : alinhamentos ) {
            System.out.println( a );
        }*/
        
    }
    
    private List<Inicio> obterInicios() {
        
        List<Inicio> valores = new ArrayList<>();
        
        for ( int i = 1; i < matrizPG.length; i++ ) {
            for ( int j = 1; j < matrizPG[i].length; j++ ) {
                
                int valor = matrizPG[i][j];
                if ( valor != 0 ) {
                    Inicio ini = new Inicio();
                    ini.i = i;
                    ini.j = j;
                    ini.valor = valor;
                    valores.add( ini );
                }
                
            }
        }
        
        return valores;
        
    }
    
    @SuppressWarnings( "unchecked" )
    private void calcularMatrizes( String s1, String s2, Function<Character, Character, Integer> scoreFunction ) {
        
        // inicialização da matriz de programação dinâmica
        matrizPG = new int[s1.length()+1][s2.length()+1];
        matrizPrecedencia = (List<String>[][]) new List[s1.length()+1][s2.length()+1];
        int d = -5;
        
        // cálculo
        for ( int i = 1; i < matrizPG.length; i++ ) {
            
            for ( int j = 1; j < matrizPG[i].length; j++ ) {
                
                //int r1 = matrizPG[i-1][j-1] + Utils.scoreProteina( s1.charAt(i-1), s2.charAt(j-1) );
                int r1 = matrizPG[i-1][j-1] + scoreFunction.apply( s1.charAt(i-1), s2.charAt(j-1) );
                int r2 = matrizPG[i-1][j] + d;
                int r3 = matrizPG[i][j-1] + d;
                List<String> caminho = new ArrayList<>();
                
                int maior = 0;
                if ( maior < r1 ) {
                    maior = r1;
                }
                if ( maior < r2 ) {
                    maior = r2;
                }
                if ( maior < r3 ) {
                    maior = r3;
                }
                
                if ( r1 == maior ) {
                    caminho.add( "\\" );
                }
                if ( r2 == maior ) {
                    caminho.add( "|" );
                }
                if ( r3 == maior ) {
                    caminho.add( "-" );
                }
                
                matrizPG[i][j] = maior;
                matrizPrecedencia[i][j] = caminho;
                
            }
            
        }
        
        // exibição
        /*for ( int i = 0; i < matrizPG.length; i++ ) {
            for ( int j = 0; j < matrizPG[i].length; j++ ) {
                System.out.print( matrizPG[i][j] + " " );
            }
            System.out.println( "" );
        }
        
        System.out.println( "" );
        
        for ( int i = 0; i < matrizPrecedencia.length; i++ ) {
            for ( int j = 0; j < matrizPrecedencia.length; j++ ) {
                if ( matrizPrecedencia[i][j] != null ) {
                    switch ( matrizPrecedencia[i][j].size() ) {
                        case 0:
                            System.out.print( String.format( "[](%2d,%2d)", i, j ) + "     " );
                            break;
                        case 1:
                            System.out.print( matrizPrecedencia[i][j] + String.format( "(%2d,%2d)", i, j ) + "        " );
                            break;
                        case 2:
                            System.out.print( matrizPrecedencia[i][j] + String.format( "(%2d,%2d)", i, j ) + "     " );
                            break;
                        case 3:
                            System.out.print( matrizPrecedencia[i][j] + String.format( "(%2d,%2d)", i, j ) + "  " );
                            break;
                    }
                } else {
                    System.out.print( String.format( "   (%2d,%2d)", i, j ) + "        " );
                }
                
            }
            System.out.println( "" );
        }*/
        
    }
    
    private void obterCaminhos( List<String>[][] matrizPrecedencia, int i, int j, List<Inicio> inicios ) {
        
        List<String> p = matrizPrecedencia[i][j];
        //System.out.println( p );
        if ( p != null && !p.isEmpty() ) {
            
            Inicio r = new Inicio();
            r.i = i;
            r.j = j;
            r.valor = matrizPG[i][j];
            inicios.remove( r );
            //System.out.println( "rem " + r );
            
            if ( p.size() > 1 ) {
                caminhosGerados[level] = caminhoAtual;
                level++;
            }
            
            for ( String s : p ) {
                
                caminhoAtual += s;
                //System.out.println( level + " " + s + " " + String.format( "(%d, %d)" , i, j ) );
                
                switch ( s ) {
                    case "\\":
                        obterCaminhos( matrizPrecedencia, i-1, j-1, inicios );
                        break;
                    case "|":
                        obterCaminhos( matrizPrecedencia, i-1, j, inicios );
                        break;
                    case "-":
                        obterCaminhos( matrizPrecedencia, i, j-1, inicios );
                        break;

                }

            }
            
            if ( p.size() > 1 ) {
                level--;
                if ( level > 0 ) {
                    caminhoAtual = caminhosGerados[level-1];
                } else {
                    caminhoAtual = "";
                }
            }
            
        } else {
            
            /*while ( i != 0 || j != 0 ) {
                if ( i != 0 && j != 0 ) {
                    caminhoAtual += "\\";
                    i--;
                    j--;
                } else if ( i != 0 ) {
                    caminhoAtual += "|";
                    i--;
                } else {
                    caminhoAtual += "-";
                    j--;
                }
            }*/
            
            caminhos.add( new Caminho( i, j, new StringBuilder( caminhoAtual ).reverse().toString() ) );
            if ( level > 0 ) {
                caminhoAtual = caminhosGerados[level-1];
            } else {
                caminhoAtual = "";
            }
        }
        
    }

    public List<String>[][] getMatrizPrecedencia() {
        return matrizPrecedencia;
    }

    public List<Alinhamento> getAlinhamentos() {
        return alinhamentos;
    }

    public int[][] getMatrizPG() {
        return matrizPG;
    }
    
    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }
    
}
