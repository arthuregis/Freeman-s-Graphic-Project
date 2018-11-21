import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class PainelBotoes extends JPanel {
	private JButton cadeiaFreeman, desfazer, refazer, limpar;
	private JButton quadrado,livre,triangulo, cor;
	private JPanel figuras, cores, acoes;
	private PainelDesenho painel_ativo;
		
	public PainelBotoes(PainelDesenho painel) {
		setLayout(new GridLayout(1,3));
		painel_ativo = painel;
		
		figuras = new JPanel();
		figuras.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		cores = new JPanel();
		cores.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		acoes = new JPanel();
		acoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		livre = new JButton("Livre");
		livre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.LIVRE);				
			}
		});
		figuras.add(livre);
		
		cadeiaFreeman = new JButton("Freeman");
		cadeiaFreeman.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.FREEMAN);
			}
		});
		figuras.add(cadeiaFreeman);
		
		quadrado = new JButton("Quadrado");
		quadrado.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.QUADRADO);
			}
		});
		figuras.add(quadrado);
		
		triangulo = new JButton("Triangulo");
		triangulo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.setTipoDesenho(Desenho.TRIANGULO);
			}
		});
		figuras.add(triangulo);
		
		cor = new JButton ();
		cor.setMargin(new Insets(11, 11, 11, 11));
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
		cores.add(cor);
		
		desfazer = new JButton("DESFAZER");
		desfazer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.desfazer();
			}
		});
		acoes.add(desfazer);
		
		refazer = new JButton("REFAZER");
		refazer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painel_ativo.refazer();				
			}
		});
		acoes.add(refazer);
		
		limpar = new JButton("LIMPAR");
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
