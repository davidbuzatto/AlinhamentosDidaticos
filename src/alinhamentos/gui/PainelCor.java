package alinhamentos.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class PainelCor extends JPanel {

    private Color cor = Color.BLACK;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor( cor );
        g.fillRect( 0, 0, getWidth(), getHeight() );
        g.setColor( Color.BLACK );
        g.drawRect( 0, 0, getWidth()-1, getHeight()-1 );
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
    
}
