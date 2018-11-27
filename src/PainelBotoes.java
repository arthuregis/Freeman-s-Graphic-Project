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

public class PainelBotoes extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton cadeiaFreeman, desfazer, refazer, limpar;
	private JButton quadrado, livre, triangulo, reta, cor, cor2;
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
		
		livre = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\livre.png"));
		livre.setMargin(new Insets(1,1,1,1));
		livre.setToolTipText("Livre");
		livre.addActionListener(this);
		figuras.add(livre);
		
		cadeiaFreeman = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\freeman.png"));
		cadeiaFreeman.setMargin(new Insets(1,1,1,1));
		cadeiaFreeman.setToolTipText("Freeman");
		cadeiaFreeman.addActionListener(this);
		figuras.add(cadeiaFreeman);
		
		quadrado = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\quadrado.png"));
		quadrado.setMargin(new Insets(1,1,1,1));
		quadrado.setToolTipText("Quadrado");
		quadrado.addActionListener(this);
		figuras.add(quadrado);
		
		triangulo = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\triangulo.png"));
		triangulo.setMargin(new Insets(1,1,1,1));
		triangulo.setToolTipText("Triangulo");
		triangulo.addActionListener(this);
		figuras.add(triangulo);
		
		reta = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\reta.png"));
		reta.setMargin(new Insets(1,1,1,1));
		reta.setToolTipText("Reta");
		reta.addActionListener(this);
		figuras.add(reta);
		
		cor = new JButton ();
		cor2 = new JButton();
		cor2.setMargin(new Insets(1,1,1,1));
		cor2.setEnabled(false);
		cor.setMargin(new Insets(7,7,7,7));
		cor.setToolTipText("Selecione a Cor");
		cor.setBackground(Color.BLACK);
		cor.addActionListener(this);
		cor2.add(cor);
		cores.add(cor2);
		
		desfazer = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\undo.png"));
		desfazer.setMargin(new Insets(1,1,1,1));
		desfazer.setToolTipText("Desfazer");
		desfazer.addActionListener(this);
		acoes.add(desfazer);
		
		refazer = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\redo.png"));
		refazer.setMargin(new Insets(1,1,1,1));
		refazer.setToolTipText("Refazer");
		refazer.addActionListener(this);
		acoes.add(refazer);
		
		limpar = new JButton(new ImageIcon("C:\\Users\\arthu\\eclipse-workspace\\FREEMAN\\imagens\\lixeira.png"));
		limpar.setMargin(new Insets(1,1,1,1));
		limpar.setToolTipText("Limpar");
		limpar.addActionListener(this);
		acoes.add(limpar);
	
		add(figuras);
		add(cores);
		add(acoes);
	}
	
	public void setPainelAtivo(PainelDesenho painel) {
		painel_ativo = painel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == livre)
			painel_ativo.setTipoDesenho(Desenho.LIVRE);
		else if(e.getSource() == cadeiaFreeman)
			painel_ativo.setTipoDesenho(Desenho.FREEMAN);
		else if(e.getSource() == quadrado)
			painel_ativo.setTipoDesenho(Desenho.QUADRADO);
		else if(e.getSource() == triangulo)
			painel_ativo.setTipoDesenho(Desenho.TRIANGULO);
		else if(e.getSource() == reta)
			painel_ativo.setTipoDesenho(Desenho.RETA);
		else if(e.getSource() == cor) {
			Color color = JColorChooser.showDialog(painel_ativo,"painel_ativo de Cores", Color.BLACK);
			if(color!=null) {
				painel_ativo.setCor(color);
				cor.setBackground(color);
			}
		}
		else if(e.getSource() == desfazer)
			painel_ativo.desfazer();
		else if(e.getSource() == refazer)
			painel_ativo.refazer();
		else if(e.getSource() == limpar)
			painel_ativo.limpar();
	}

}
