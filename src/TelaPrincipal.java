import java.awt.BorderLayout;
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
	private JMenu edicao,rot_horario, rot_anti, copiar, passo;
	private PainelDesenho pintando;
	private String ultimoDiretorioAcessado = ".";
	
	public TelaPrincipal() {
		super("πntando");
		
		JMenuBar barra_menu = new JMenuBar();
		setJMenuBar(barra_menu);
		
		JMenu arquivo = new JMenu("Arquivo");
		barra_menu.add(arquivo);
		
		JMenuItem novo = new JMenuItem("Novo");
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
		
		JMenuItem abrir = new JMenuItem("Abrir");
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
		
		JMenuItem salvar = new JMenuItem("Salvar");
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
		
		edicao = new JMenu ("Edicao");
		barra_menu.add(edicao);
		
		rot_horario = new JMenu("Rotacionar -90°");
		edicao.add(rot_horario);
		
		rot_anti = new JMenu("Rotacionar +90°");
		edicao.add(rot_anti);
		
		passo = new JMenu("Alterar Passo");
		edicao.add(passo);
		
		copiar = new JMenu("Copiar");
		edicao.add(copiar);

		pintando = new PainelDesenho(this);
		add(pintando);
		
		PainelBotoes botoes = new PainelBotoes(pintando);
		add(botoes,BorderLayout.NORTH);
		
	}
	
	private void addMenuRotacaoH(Figura figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		rot_horario.add(item);
	}
	
	private void addMenuRotacaoAH(Figura figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		rot_anti.add(item);
	}
	
	private void addMenuPasso(Figura figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		passo.add(item);
	}
	
	private void addMenuCopiar(Figura figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		copiar.add(item);
	}
	
	public void addFiguraMenu(Figura figura, int indice) {
		addMenuRotacaoH(figura,indice);
		addMenuRotacaoAH(figura,indice);
		addMenuPasso(figura,indice);
		addMenuCopiar(figura,indice);
	}
	
	public void removeFiguraMenu(int indice) {
		rot_horario.remove(indice-1);
		rot_anti.remove(indice-1);
		passo.remove(indice-1);
		copiar.remove(indice-1);
	}
	
	public void removeAllFiguraMenu() {
		rot_horario.removeAll();
		rot_anti.removeAll();
		passo.removeAll();
		copiar.removeAll();
	}
}
