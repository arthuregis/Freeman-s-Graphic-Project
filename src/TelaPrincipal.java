import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TelaPrincipal extends JFrame {
	
	private JMenuBar barra_menu;
	private JMenu arquivo;
	private JMenuItem novo,abrir,salvar;
	private PainelDesenho pintando;
	private PainelBotoes botoes;
	
	
	public TelaPrincipal() {
		super("πntando");
		
		barra_menu = new JMenuBar();
		setJMenuBar(barra_menu);
		
		arquivo = new JMenu("Arquivo");
		barra_menu.add(arquivo);
		
		novo = new JMenuItem("Novo");
		arquivo.add(novo);
		
		abrir = new JMenuItem("Abrir");
		arquivo.add(abrir);
		
		salvar = new JMenuItem("Salvar");
		arquivo.add(salvar);
		
		pintando = new PainelDesenho();
		add(pintando);
		
		botoes = new PainelBotoes(pintando);
		add(botoes,BorderLayout.NORTH);
		
	}

}
