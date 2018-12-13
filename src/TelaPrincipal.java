import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class TelaPrincipal extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenu edicao,rot_horario, rot_anti, copiar, passo, mover;
	private PainelDesenho pintando;
	private String ultimoDiretorioAcessado = "C:\\Users\\arthu\\Downloads";
	private JMenuItem novo,abrir,salvar,exportar,importar;
	private JMenuItem addBorda, removerBorda, addPontos, removerFundo, removerPontos;
	private PainelBotoes botoes;
	
	public TelaPrincipal() {
		super("πntando");
		
		JMenuBar barra_menu = new JMenuBar();
		setJMenuBar(barra_menu);
		
		JMenu arquivo = new JMenu("Arquivo");
		barra_menu.add(arquivo);
		
		novo = new JMenuItem("Novo");
		novo.addActionListener(this);
		arquivo.add(novo);
		
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(this);
		arquivo.add(abrir);
		
		salvar = new JMenuItem("Salvar");
		salvar.addActionListener(this);
		arquivo.add(salvar);
		arquivo.addSeparator();
		
		exportar = new JMenuItem("Exportar");
		exportar.addActionListener(this);
		arquivo.add(exportar);
		
		importar = new JMenuItem("Importar");
		importar.addActionListener(this);
		arquivo.add(importar);
		
		edicao = new JMenu ("Edicao");
		barra_menu.add(edicao);
		
		rot_horario = new JMenu("Rotacionar -90°");
		edicao.add(rot_horario);
		
		rot_anti = new JMenu("Rotacionar +90°");
		edicao.add(rot_anti);
		
		passo = new JMenu("Alterar Passo");
		edicao.add(passo);
		
		mover = new JMenu("Mover");
		edicao.add(mover);
		
		copiar = new JMenu("Copiar");
		edicao.add(copiar);
		
		edicao.addSeparator();
		
		JMenu borda = new JMenu("Borda");
		addBorda = new JMenuItem("Desenhar");
		addBorda.addActionListener(this);
		borda.add(addBorda);
		removerBorda = new JMenuItem("Remover");
		removerBorda.addActionListener(this);
		borda.add(removerBorda);
		addPontos = new JMenuItem("Adicionar Pontos");
		addPontos.addActionListener(this);
		borda.add(addPontos);
		removerPontos = new JMenuItem("Remover Pontos");
		removerPontos.addActionListener(this);
		borda.add(removerPontos);
		edicao.add(borda);
		
		JMenu fundo = new JMenu("Imagem Importada");
		removerFundo = new JMenuItem("Remover");
		removerFundo.addActionListener(this);
		fundo.add(removerFundo);
		edicao.add(fundo);
		
		pintando = new PainelDesenho(this);
		add(pintando);
		
		botoes = new PainelBotoes(pintando);
		add(botoes,BorderLayout.NORTH);
		
	}
	
	private void addMenuRotacaoH(FiguraFreeman figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				figura.rotacionarH();
				pintando.repaint();
			}
		});
		rot_horario.add(item);
	}
	
	private void addMenuRotacaoAH(FiguraFreeman figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				figura.rotacionarAH();
				pintando.repaint();
			}
		});
		rot_anti.add(item);
	}
	
	private void addMenuPasso(FiguraFreeman figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int passo = Integer.parseInt(JOptionPane.showInputDialog(pintando, 
							"Digite o novo Passo:", "Passo", JOptionPane.QUESTION_MESSAGE));
					if(passo<1)
						throw new NumberFormatException();
					figura.setPasso(passo);
					pintando.repaint();					
				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(pintando, "Valor Invalido", "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		passo.add(item);
	}
	
	private void addMenuCopiar(FiguraFreeman figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FiguraFreeman aux = figura.clone();
				pintando.setTipoDesenho(FiguraFreeman.COPIAR);
				pintando.setFiguraAuxiliar(aux);
			}
		});
		copiar.add(item);
	}
	
	private void addMenuMover(FiguraFreeman figura, int indice) {
		JMenuItem item = new JMenuItem("Figura "+indice);
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pintando.setTipoDesenho(FiguraFreeman.MOVER);
				pintando.setFiguraAuxiliar(figura);
			}
		});
		mover.add(item);
	}
	
	public void addFiguraMenu(FiguraFreeman figura, int indice) {
		addMenuRotacaoH(figura,indice);
		addMenuRotacaoAH(figura,indice);
		addMenuPasso(figura,indice);
		addMenuCopiar(figura,indice);
		addMenuMover(figura,indice);
	}
	
	public void removeFiguraMenu(int indice) {
		rot_horario.remove(indice-1);
		rot_anti.remove(indice-1);
		passo.remove(indice-1);
		copiar.remove(indice-1);
		mover.remove(indice-1);
	}
	
	public void removeAllFiguraMenu() {
		rot_horario.removeAll();
		rot_anti.removeAll();
		passo.removeAll();
		copiar.removeAll();
		mover.removeAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == novo) {
			if(!pintando.isSaved()) {
				String[] opcoes = {"Sim","Nao"};
				int i = JOptionPane.showOptionDialog(TelaPrincipal.this,
						"Tem certeza que deseja continuar? A figura atual sera perdida.",
						"Novo Desenho", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, opcoes, opcoes[0]);
				if (i == JOptionPane.YES_OPTION) {
					pintando.setFiguras(new ArrayList<FiguraFreeman>());
					removeAllFiguraMenu();
					pintando.setImagemFundo(null);
				}
				}
				else {
					remove(pintando);
					pintando = new PainelDesenho(TelaPrincipal.this);
					add(pintando);
					botoes.setPainelAtivo(pintando);
					repaint();
					validate();
				}
		}
		else if(e.getSource() == abrir) {
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
						return f.getName().toLowerCase().endsWith(".pnt") || f.isDirectory();
					}
					public String getDescription() {
						return "*.pnt";
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
						pintando.setFiguras((ArrayList<FiguraFreeman>) obj);
					} 
					catch (FileNotFoundException exc) { exc.printStackTrace(); } 
					catch (IOException exc) { exc.printStackTrace(); }
					catch (ClassNotFoundException exc) { exc.printStackTrace(); }
				}
			}
		}
		else if(e.getSource() == salvar) {
			System.out.println("oi");
			String nomeDoArquivo;
			JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
			escolhedorDeArquivos.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".pnt") || f.isDirectory();
				}
				public String getDescription() {
					return "*.pnt";
				}
			});
			int situacao = escolhedorDeArquivos.showSaveDialog(TelaPrincipal.this);
			if (situacao == JFileChooser.APPROVE_OPTION) {
				ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
				nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
				if(! nomeDoArquivo.toLowerCase().endsWith(".pnt"))
					nomeDoArquivo += ".pnt";
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
		}else if(e.getSource() == exportar) {
			String nomeDoArquivo;
			JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
			escolhedorDeArquivos.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
				}
				public String getDescription() {
					return "*.png";
				}
			});
			int situacao = escolhedorDeArquivos.showSaveDialog(TelaPrincipal.this);
			if (situacao == JFileChooser.APPROVE_OPTION) {
				ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
				nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
				if(! nomeDoArquivo.toLowerCase().endsWith(".png"))
					nomeDoArquivo += ".png";
				try {
					FileOutputStream file = new FileOutputStream (nomeDoArquivo);
					ImageIO.write(pintando.exportar(),"png", file);
					file.close();
					if(pintando.getFiguras().size()>0) {
						FileOutputStream fos = new FileOutputStream(nomeDoArquivo.replace(".png", ".pnt"));
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(pintando.getFiguras());
						fos.close();
						oos.close();
						pintando.setSaved();
					}
				} 
				catch (FileNotFoundException exc) { exc.printStackTrace(); } 
				catch (IOException exc) { exc.printStackTrace(); }
			}
			if (situacao == JFileChooser.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(TelaPrincipal.this, "Arquivo nao salvo",
						"Salvar", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else if(e.getSource() == importar) {
			String nomeDoArquivo;
			JFileChooser escolhedorDeArquivos = new JFileChooser(ultimoDiretorioAcessado);
			escolhedorDeArquivos.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
				}
				public String getDescription() {
					return "*.png";
				}
			});
			int situacao = escolhedorDeArquivos.showSaveDialog(TelaPrincipal.this);
			if (situacao == JFileChooser.APPROVE_OPTION) {
				ultimoDiretorioAcessado = escolhedorDeArquivos.getCurrentDirectory().toString();
				nomeDoArquivo = escolhedorDeArquivos.getSelectedFile().getAbsolutePath();
				if(! nomeDoArquivo.toLowerCase().endsWith(".png"))
					nomeDoArquivo += ".png";
				try {
					FileInputStream file = new FileInputStream (nomeDoArquivo);
					BufferedImage imagem = ImageIO.read(file);
					pintando.setFiguras(new ArrayList<FiguraFreeman>());
					removeAllFiguraMenu();
					pintando.setImagemFundo(imagem);
					file.close();
				} 
				catch (FileNotFoundException exc) { exc.printStackTrace(); } 
				catch (IOException exc) { exc.printStackTrace(); }
			}
		}
		else if(e.getSource() == addBorda) {
			pintando.addBorda();
		}
		else if(e.getSource() == removerBorda) {
			pintando.removerBorda();
		}
		else if(e.getSource() == removerFundo) {
			pintando.setImagemFundo(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
		}
		else if(e.getSource() == addPontos) {
			try {
				int passo = Integer.parseInt(JOptionPane.showInputDialog(pintando, 
						"Insira o Passo:", "Passo", JOptionPane.PLAIN_MESSAGE));
				pintando.pegarPontos(passo);
				
			}catch(NumberFormatException e1) {
				JOptionPane.showMessageDialog(pintando, "Insira Apenas valores inteiros positivos",
						"Valor Invalido", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource() == removerPontos)
			pintando.removerPontos();
		
	}
	
}
