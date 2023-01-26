package alinhamentos.gui;

import alinhamentos.algoritmos.suporte.Alinhamento;
import alinhamentos.algoritmos.AlinhamentoGlobal;
import alinhamentos.algoritmos.AlinhamentoLocal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JViewport;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class PainelDesenho extends JPanel {

    private AlinhamentoGlobal alinhamentoGlobal;
    private AlinhamentoLocal alinhamentoLocal;
    private int alinhamentoAtual = 0;
    private int larguraCelula = 25;
    private boolean desenharMatrizPrecedencia;
    private boolean desenharCaminhoAlinhamento;
    private int corMenor = 160;
    private int corMaior = 240;
    private Color corPrecedencia = new Color( 0, 0, 0, 200 );
    private Color corCaminho = new Color( 255, 255, 255, 200 );
    private double zoom = 1;
    private BufferedImage img;
    
    @Override
    protected void paintComponent( Graphics g ) {
        
        super.paintComponent( g ); //To change body of generated methods, choose Tools | Templates.
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        
        g2d.setColor( Color.WHITE );
        g2d.fillRect( 0, 0, getWidth(), getHeight() );
        
        g2d.setColor( Color.BLACK );
        g2d.drawRect( 0, 0, getWidth()-1, getHeight()-1 );
        
        if ( alinhamentoGlobal != null || alinhamentoLocal != null ) {
            if ( alinhamentoGlobal != null ) {
                desenharAlinhamentoGlobal();
            } else {
                desenharAlinhamentoLocal();
            }
        }
        
        g2d.drawImage( img, 0, 0, null );
        g2d.dispose();
        
    }
    
    private void desenharAlinhamentoGlobal() {
        
        if ( alinhamentoGlobal != null ) {
            
            Alinhamento atual = alinhamentoGlobal.getAlinhamentos().get( alinhamentoAtual );
            int[][] matrizPG = alinhamentoGlobal.getMatrizPG();
            List<String>[][] matrizPrecedencia = alinhamentoGlobal.getMatrizPrecedencia();
            
            String s1 = alinhamentoGlobal.getS1();
            String s2 = alinhamentoGlobal.getS2();
            
            img = desenharEstrutura( atual, matrizPG, matrizPrecedencia, s1, s2 );
            
        }
        
    }
    
    private void desenharAlinhamentoLocal() {
        
        if ( alinhamentoLocal != null ) {
            
            Alinhamento atual = alinhamentoLocal.getAlinhamentos().get( alinhamentoAtual );
            int[][] matrizPG = alinhamentoLocal.getMatrizPG();
            List<String>[][] matrizPrecedencia = alinhamentoLocal.getMatrizPrecedencia();
            
            String s1 = alinhamentoLocal.getS1();
            String s2 = alinhamentoLocal.getS2();
            
            img = desenharEstrutura( atual, matrizPG, matrizPrecedencia, s1, s2 );
            
        }
        
    }
    
    private BufferedImage desenharEstrutura( Alinhamento atual, int[][] matrizPG, List<String>[][] matrizPrecedencia, String s1, String s2 ) {
        
        int largura = (int) ( ( (s2.length()+2) * larguraCelula + 100 ) * zoom );
        int altura = (int) ( ( (s1.length()+2) * larguraCelula + 100 ) * zoom );
        
        img = new BufferedImage( largura, altura, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2d = img.createGraphics();
        
        AffineTransform af = AffineTransform.getTranslateInstance( 0, 0 );
        af.scale( zoom, zoom );
        g2d.setTransform( af );
        
        int menor = Integer.MAX_VALUE;
        int maior = Integer.MIN_VALUE;
        
        for ( int[] li : matrizPG ) {
            for ( int v : li ) {
                if ( menor > v ) {
                    menor = v;
                }
                if ( maior < v ) {
                    maior = v;
                }
            }
        }

        /*for ( int i = 0; i < matrizPG.length; i++ ) {
            for ( int j = 0; j < matrizPG[i].length; j++ ) {

                g2d.setColor( escala( matrizPG[j][i] + Math.abs( menor ), Math.abs( menor ) + maior ) );
                g2d.fillRect( (i+1)*larguraCelula, (j+1)*larguraCelula, larguraCelula, larguraCelula );

                g2d.setColor( Color.BLACK );
                g2d.drawString( String.valueOf( matrizPG[j][i] ), (i+1)*larguraCelula+2, (j+2)*larguraCelula-2 );

            }
        }*/
        
        for ( int i = 0; i < matrizPG.length; i++ ) {
            for ( int j = 0; j < matrizPG[i].length; j++ ) {

                g2d.setColor( escala( matrizPG[i][j] + Math.abs( menor ), Math.abs( menor ) + maior ) );
                g2d.fillRect( (j+1)*larguraCelula, (i+1)*larguraCelula, larguraCelula, larguraCelula );

                g2d.setColor( Color.BLACK );
                g2d.drawString( String.valueOf( matrizPG[i][j] ), (j+1)*larguraCelula+2, (i+2)*larguraCelula-2 );

            }
        }

        g2d.setColor( Color.BLACK );

        for ( int i = 1; i < matrizPG.length+2; i++ ) {
            g2d.drawLine( larguraCelula, i*larguraCelula, (matrizPG[0].length+1)*larguraCelula, i*larguraCelula );
        }

        for ( int i = 1; i < matrizPG[0].length+2; i++ ) {
            g2d.drawLine( i*larguraCelula, larguraCelula, i*larguraCelula, (matrizPG.length+1)*larguraCelula );
        }

        g2d.setFont( new Font( "Courier New" , Font.BOLD, 20 ) );

        for ( int i = 0; i < s1.length(); i++ ) {
            g2d.drawString( String.valueOf( s1.charAt( i ) ), 6, i*larguraCelula + 2*larguraCelula + 20 );
        }

        for ( int i = 0; i < s2.length(); i++ ) {
            g2d.drawString( String.valueOf( s2.charAt( i ) ), i*larguraCelula + 2*larguraCelula + 6, 22 );
        }

        g2d.setStroke( new BasicStroke( 3 ) );
        g2d.setColor( corPrecedencia );

        if ( desenharMatrizPrecedencia ) {

            for ( int i = 0; i < matrizPrecedencia.length; i++ ) {
                for ( int j = 0; j < matrizPrecedencia[i].length; j++ ) {

                    if ( matrizPrecedencia[i][j] != null ) {
                        for ( String v : matrizPrecedencia[i][j] ) {
                            switch ( v ) {
                                case "\\":
                                    g2d.drawLine( 
                                            j*larguraCelula+larguraCelula/2+4, 
                                            i*larguraCelula+larguraCelula/2+4,
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4 );
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4,
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2-8);
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4,
                                            (j+1)*larguraCelula+larguraCelula/2-8, 
                                            (i+1)*larguraCelula+larguraCelula/2-4);
                                    break;
                                case "|":
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2+4, 
                                            i*larguraCelula+larguraCelula/2+4,
                                            (j+1)*larguraCelula+larguraCelula/2+4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4 );
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2+4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4,
                                            (j+1)*larguraCelula+larguraCelula/2+1, 
                                            (i+1)*larguraCelula+larguraCelula/2-7 );
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2+4, 
                                            (i+1)*larguraCelula+larguraCelula/2-4,
                                            (j+1)*larguraCelula+larguraCelula/2+7, 
                                            (i+1)*larguraCelula+larguraCelula/2-7 );
                                    break;
                                case "-":
                                    g2d.drawLine( 
                                            j*larguraCelula+larguraCelula/2+4, 
                                            (i+1)*larguraCelula+larguraCelula/2+4,
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2+4 );
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2+4,
                                            (j+1)*larguraCelula+larguraCelula/2-7, 
                                            (i+1)*larguraCelula+larguraCelula/2+1);
                                    g2d.drawLine( 
                                            (j+1)*larguraCelula+larguraCelula/2-4, 
                                            (i+1)*larguraCelula+larguraCelula/2+4,
                                            (j+1)*larguraCelula+larguraCelula/2-7, 
                                            (i+1)*larguraCelula+larguraCelula/2+7);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        g2d.setColor( corCaminho );

        if ( desenharCaminhoAlinhamento ) {
            int i = atual.c.i;
            int j = atual.c.j;
            for ( char v : atual.c.caminho.toCharArray() ) {
                switch ( v ) {
                    case '\\':
                        i++;
                        j++;
                        g2d.drawLine( 
                                j*larguraCelula+larguraCelula/2+4, 
                                i*larguraCelula+larguraCelula/2+4,
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2-4 );
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2-4,
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2-8);
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2-4,
                                (j+1)*larguraCelula+larguraCelula/2-8, 
                                (i+1)*larguraCelula+larguraCelula/2-4);
                        break;
                    case '|':
                        i++;
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2+4, 
                                i*larguraCelula+larguraCelula/2+4,
                                (j+1)*larguraCelula+larguraCelula/2+4, 
                                (i+1)*larguraCelula+larguraCelula/2-4 );
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2+4, 
                                (i+1)*larguraCelula+larguraCelula/2-4,
                                (j+1)*larguraCelula+larguraCelula/2+1, 
                                (i+1)*larguraCelula+larguraCelula/2-7 );
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2+4, 
                                (i+1)*larguraCelula+larguraCelula/2-4,
                                (j+1)*larguraCelula+larguraCelula/2+7, 
                                (i+1)*larguraCelula+larguraCelula/2-7 );
                        break;
                    case '-':
                        j++;
                        g2d.drawLine( 
                                j*larguraCelula+larguraCelula/2+4, 
                                (i+1)*larguraCelula+larguraCelula/2+4,
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2+4 );
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2+4,
                                (j+1)*larguraCelula+larguraCelula/2-7, 
                                (i+1)*larguraCelula+larguraCelula/2+1);
                        g2d.drawLine( 
                                (j+1)*larguraCelula+larguraCelula/2-4, 
                                (i+1)*larguraCelula+larguraCelula/2+4,
                                (j+1)*larguraCelula+larguraCelula/2-7, 
                                (i+1)*larguraCelula+larguraCelula/2+7);
                        break;
                }
            }
        }

        g2d.setColor( Color.BLACK );
        g2d.setFont( new Font( "Courier New" , Font.BOLD, 40 ) );
        g2d.drawString( atual.s1, larguraCelula + 5, (s1.length()+2) * larguraCelula + 30 );
        g2d.drawString( atual.s2, larguraCelula + 5, (s1.length()+2) * larguraCelula + 80 );
        
        mudarTamanho( largura, altura );
        
        return img;
        
    }
    
    public void mudarTamanho( int larg, int alt ) {
        
        int largP = getParent().getWidth();
        int altP = getParent().getHeight();
        
        if ( larg < largP ) {
            larg = largP;
        }
        
        if ( alt < altP ) {
            alt = altP;
        }
        
        Dimension dim = new Dimension( larg, alt );
        setMinimumSize( dim );
        setMaximumSize( dim );
        setPreferredSize( dim );
        
        ( ( JViewport ) getParent() ).updateUI();
        
    }
    
    /*private Color escala( double valor ) {
        valor = 0.66 - valor / 150;
        return Color.getHSBColor( (float) valor, 0.8f, 1f );
    }
    
    private Color escala( double valor, double maximo ) {
        valor = valor / maximo * 2 / 3; // 2/3 do espectro hue
        return Color.getHSBColor( (float) valor, .8f, 1f );
    }*/
    
    /*private Color escala( double valor, double maximo ) {
        
        // http://colorizer.org/
        // extremidade 1
        double v1 = 160; // azul água
        // extremidade 2 do hue
        double v2 = 240; // azul puro
        double dif = v2 - v1;
        
        // de um lado
        valor = ( v1 + ( valor / maximo * dif ) ) / 360;
        // do outro
        //valor = ( v2 - ( valor / maximo * dif ) ) / 360;
        
        return Color.getHSBColor( (float) valor, 1f, 1f );
        
    }*/
    
    private Color escala( double valor, double maximo ) {
        
        double dif = corMaior - corMenor;
        
        // de um lado
        valor = ( corMenor + ( valor / maximo * dif ) ) / 360;
        // do outro
        //valor = ( v2 - ( valor / maximo * dif ) ) / 360;
        
        return Color.getHSBColor( (float) valor, 1f, 1f );
        
    }
    
    public void desenharAG( AlinhamentoGlobal alinhamentoGlobal ) {
        this.alinhamentoGlobal = alinhamentoGlobal;
        this.alinhamentoLocal = null;
        alinhamentoAtual = 0;
        repaint();
    }
    
    public void desenharAL( AlinhamentoLocal alinhamentoLocal ) {
        this.alinhamentoLocal = alinhamentoLocal;
        this.alinhamentoGlobal = null;
        alinhamentoAtual = 0;
        repaint();
    }
    
    public void proximo() {
        if ( alinhamentoGlobal != null ) {
            if ( alinhamentoAtual < alinhamentoGlobal.getAlinhamentos().size() - 1 ) {
                alinhamentoAtual++;
                repaint();
            }
        }
        if ( alinhamentoLocal != null ) {
            if ( alinhamentoAtual < alinhamentoLocal.getAlinhamentos().size() - 1 ) {
                alinhamentoAtual++;
                repaint();
            }
        }
    }
    
    public void anterior() {
        if ( alinhamentoGlobal != null ) {
            if ( alinhamentoAtual > 0 ) {
                alinhamentoAtual--;
                repaint();
            }
        }
        if ( alinhamentoLocal != null ) {
            if ( alinhamentoAtual > 0 ) {
                alinhamentoAtual--;
                repaint();
            }
        }
    }
    
    public void zoom( int valor ) {
        
        double f = valor / 100.0;
        zoom = 1 + f;
        
    }

    public BufferedImage getImg() {
        return img;
    }
    
    public void setDesenharMatrizPrecedencia( boolean desenharMatrizPrecedencia ) {
        this.desenharMatrizPrecedencia = desenharMatrizPrecedencia;
    }

    public void setDesenharCaminhoAlinhamento( boolean desenharCaminhoAlinhamento ) {
        this.desenharCaminhoAlinhamento = desenharCaminhoAlinhamento;
    }

    public void setCorMenor(int corMenor) {
        this.corMenor = corMenor;
    }

    public void setCorMaior(int corMaior) {
        this.corMaior = corMaior;
    }

    public Color getCorPrecedencia() {
        return corPrecedencia;
    }

    public void setCorPrecedencia( Color corPrecedencia ) {
        this.corPrecedencia = corPrecedencia;
    }

    public Color getCorCaminho() {
        return corCaminho;
    }

    public void setCorCaminho( Color corCaminho ) {
        this.corCaminho = corCaminho;
    }
    
}
