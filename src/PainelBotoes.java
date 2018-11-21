import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class PainelBotoes extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton cadeiaFreeman, desfazer, refazer, limpar;
	private JButton quadrado,livre,triangulo, cor, cor2;
	private JPanel figuras, cores, acoes;
	private PainelDesenho painel_ativo;
		
	public PainelBotoes(PainelDesenho painel) {
		setLayout(new GridLayout(1,3));
		painel_ativo = painel;
		
		figuras = new JPanel();
		figuras.setBackground(Color.DARK_GRAY);
		figuras.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		cores = new JPanel();
		cores.setBackground(Color.DARK_GRAY);
		cores.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		acoes = new JPanel();
		acoes.setBackground(Color.DARK_GRAY);
		acoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		livre = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\livre.png"));
		livre.setMargin(new Insets(1,1,1,1));
		livre.setToolTipText("Livre");
		livre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.LIVRE);				
			}
		});
		figuras.add(livre);
		
		cadeiaFreeman = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\freeman.png"));
		cadeiaFreeman.setMargin(new Insets(1,1,1,1));
		cadeiaFreeman.setToolTipText("Freeman");
		cadeiaFreeman.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.FREEMAN);
			}
		});
		figuras.add(cadeiaFreeman);
		
		quadrado = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\quadrado.png"));
		quadrado.setMargin(new Insets(1,1,1,1));
		quadrado.setToolTipText("Quadrado");
		quadrado.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.QUADRADO);
			}
		});
		figuras.add(quadrado);
		
		triangulo = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\triangulo.png"));
		triangulo.setMargin(new Insets(1,1,1,1));
		triangulo.setToolTipText("Triangulo");
		triangulo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.TRIANGULO);
			}
		});
		figuras.add(triangulo);
		
		cor = new JButton ();
		cor2 = new JButton();
		cor2.setMargin(new Insets(1,1,1,1));
		cor2.setEnabled(false);
		cor.setMargin(new Insets(7,7,7,7));
		cor.setToolTipText("Selecione a Cor");
		cor.setBackground(Color.BLACK);
		cor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(painel_ativo,"painel_ativo de Cores", Color.BLACK);
				if(color!=null) {
					painel_ativo.setCor(color);
					cor.setBackground(color);
				}
			}
			
		});
		cor2.add(cor);
		cores.add(cor2);
		
		desfazer = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\undo.png"));
		desfazer.setMargin(new Insets(1,1,1,1));
		desfazer.setToolTipText("Desfazer");
		desfazer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.desfazer();
			}
		});
		acoes.add(desfazer);
		
		refazer = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\redo.png"));
		refazer.setMargin(new Insets(1,1,1,1));
		refazer.setToolTipText("Refazer");
		refazer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.refazer();				
			}
		});
		acoes.add(refazer);
		
		limpar = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\lixeira.png"));
		limpar.setMargin(new Insets(1,1,1,1));
		limpar.setToolTipText("Limpar");
		limpar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.limpar();
			}
		});
		acoes.add(limpar);
	
		add(figuras);
		add(cores);
		add(acoes);
	}
	
	public void setPainelAtivo(PainelDesenho painel) {
		painel_ativo = painel;
	}

}
