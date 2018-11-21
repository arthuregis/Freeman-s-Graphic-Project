import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		TelaPrincipal app = new TelaPrincipal();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//app.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		app.setSize(1100,600);
		app.setVisible(true);
		
	}

}
