import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;


class Background extends JComponent {
	
	private Image backgroundImage;
	public Background(Image inImage) {
		this.backgroundImage = inImage;
	}
	@Override 
	protected void paintComponent(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
	}
}