import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuBar barra_menu;
	private JMenu arquivo;
	private JMenuItem novo,abrir,salvar;
	private PainelDesenho pintando;
	private PainelBotoes botoes;
	private String ultimoDiretorioAcessado = ".";
	
	
	public TelaPrincipal() {
		super("πntando");
		
		barra_menu = new JMenuBar();
		setJMenuBar(barra_menu);
		
		arquivo = new JMenu("Arquivo");
		barra_menu.add(arquivo);
		
		novo = new JMenuItem("Novo");
		novo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!pintando.isSaved()) {
				String[] opcoes = {"Sim","Nao"};
				int i = JOptionPane.showOptionDialog(TelaPrincipal.this,
						"Tem certeza que deseja continuar? A figura atual sera perdida.",
						"Novo Desenho", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, opcoes, opcoes[0]);
				if (i == JOptionPane.YES_OPTION) 
					pintando.setFiguras(new ArrayList<Figura>());
				}
				else
					pintando.setFiguras(new ArrayList<Figura>());
			}
			
		});
		arquivo.add(novo);
		
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				int i=-1;
				if(!pintando.isSaved()) {
					String[] opcoes = {"Sim","Nao"};
					i = JOptionPane.showOptionDialog(TelaPrincipal.this,
							"Tem certeza que deseja continuar? A figura atual sera perdida.",
							"Abrir Desenho", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							null, opcoes, opcoes[0]);
				}
				if (pintando.isSaved() || i == JOptionPane.YES_OPTION) {
					JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
					escolhedorDeArquivos.setFileFilter(new FileFilter() {
						public boolean accept(File f) {
							return f.getName().toLowerCase().endsWith(".pint") || f.isDirectory();
						}
						public String getDescription() {
							return "*.pint";
						}
					});
					int situacao = escolhedorDeArquivos.showOpenDialog(TelaPrincipal.this);
					if (situacao == JFileChooser.APPROVE_OPTION) {
						ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
						String nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
						try {
							FileInputStream fis = new FileInputStream(nomeDoArquivo);
							ObjectInputStream ois = new ObjectInputStream(fis);
							Object obj = ois.readObject();
							fis.close();
							ois.close();
							pintando.setFiguras((ArrayList<Figura>) obj);
						} 
						catch (FileNotFoundException exc) { exc.printStackTrace(); } 
						catch (IOException exc) { exc.printStackTrace(); }
						catch (ClassNotFoundException exc) { exc.printStackTrace(); }
					}
				}
			}
			
		});
		arquivo.add(abrir);
		
		salvar = new JMenuItem("Salvar");
		salvar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String nomeDoArquivo;
				JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
				escolhedorDeArquivos.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return f.getName().toLowerCase().endsWith(".pint") || f.isDirectory();
					}
					public String getDescription() {
						return "*.pint";
					}
				});
				int situacao = escolhedorDeArquivos.showSaveDialog(TelaPrincipal.this);
				if (situacao == JFileChooser.APPROVE_OPTION) {
					ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
					nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
					if(! nomeDoArquivo.toLowerCase().endsWith(".pint"))
						nomeDoArquivo += ".pint";
					try {
						FileOutputStream fos = new FileOutputStream(nomeDoArquivo);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(pintando.getFiguras());
						fos.close();
						oos.close();
						pintando.setSaved();
					} 
					catch (FileNotFoundException exc) { exc.printStackTrace(); } 
					catch (IOException exc) { exc.printStackTrace(); }
				}
				if (situacao == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(TelaPrincipal.this, "Arquivo nao salvo",
							"Salvar", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
		});
		arquivo.add(salvar);
		
		pintando = new PainelDesenho();
		add(pintando);
		
		botoes = new PainelBotoes(pintando);
		add(botoes,BorderLayout.NORTH);
		
	}

}
