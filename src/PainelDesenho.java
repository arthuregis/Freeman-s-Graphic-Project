import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PainelDesenho extends JPanel 
	implements MouseListener,MouseMotionListener,ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	private ArrayList<FiguraFreeman> principal,lixeira;
	private ArrayList<Point> borda,pontos;
	private Color corBorda;
	private FiguraFreeman figuraAtual;
	private Point pontoAtual;
	private int codigoAtual , tipoFiguraFreeman;
	private JPanel painelAuxiliar;
	private JLabel labelAuxiliar;
	private JTextField textoAuxiliar;
	private boolean mousemoved,limpar,saved;
	private TelaPrincipal tela;
	private BufferedImage imagemFundo;
	
	public PainelDesenho(TelaPrincipal tela) {
		this.tela = tela;
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		principal = new ArrayList<>();
		lixeira = new ArrayList<>();
		borda = new ArrayList<>();
		pontos = new ArrayList<>();
		figuraAtual = new FiguraFreeman(corBorda);
		pontoAtual = new Point();
		tipoFiguraFreeman = FiguraFreeman.DEFAULT;
		painelAuxiliar = new JPanel();
		labelAuxiliar = new JLabel();
		textoAuxiliar = new JTextField(30);
		mousemoved = false;
		limpar = false;
		saved = true;
		corBorda = Color.BLACK;
		
		painelAuxiliar.setLayout(new FlowLayout());
		painelAuxiliar.add(labelAuxiliar);
		painelAuxiliar.add(textoAuxiliar);
		textoAuxiliar.addActionListener(this);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gg = (Graphics2D) g;
		gg.setStroke(new BasicStroke(2f));
		
		if(imagemFundo!=null) {
			gg.drawImage(imagemFundo, null, 
					((this.getWidth()/2)-(imagemFundo.getWidth()/2)) ,
					((this.getHeight()/2)-(imagemFundo.getHeight()/2))
					);
		}
		if(borda.size()>0) {
			gg.setColor(corBorda);
			for(Point ponto:borda)
				gg.fillRect(ponto.x, ponto.y, 1, 1);
		}
		if(pontos.size()>0) {
			for(Point ponto:pontos) {
				gg.setColor(Color.RED);
				gg.fillOval(ponto.x-2, ponto.y-2, 4, 4);
			}
		}
		
		for(FiguraFreeman figura:principal) {
        	gg.setColor(figura.getCor());
        	ArrayList<Point> pontos = figura.gerarPontos();
        	for(int i = 1 ; i < pontos.size() ; i++)
        		gg.drawLine(pontos.get(i-1).x, pontos.get(i-1).y, pontos.get(i).x, pontos.get(i).y);
        }
        
        if(figuraAtual.getCodigoFreeman()!=null) {
        	gg.setColor(figuraAtual.getCor());
        	ArrayList<Point> pontos = figuraAtual.gerarPontos();
        	for(int i = 1 ; i < pontos.size() ; i++)
        		gg.drawLine(pontos.get(i-1).x, pontos.get(i-1).y, pontos.get(i).x, pontos.get(i).y);
        	
        }
        if(mousemoved) {
        	gg.drawLine(figuraAtual.lastPoint().x,
        			figuraAtual.lastPoint().y,
        			pontoAtual.x, pontoAtual.y);
        	mousemoved = false;
        }
		
	}
	
	public void pegarPontos(int passo) {
		ArrayList<Point> pontos = new ArrayList<>();
		BufferedImage plano = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = plano.createGraphics();
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, this.getWidth(),this.getHeight());
		
		if(borda.size()>0) {
			g2d.setColor(Color.BLACK);
			for(Point ponto:borda)
				g2d.fillRect(ponto.x, ponto.y, 1, 1);
			g2d.dispose();
			
			for(int i = passo/2+1; i < plano.getWidth()-(passo/2) ; i+=passo) {
				for(int j = passo/2+1 ; j < plano.getHeight()-(passo/2) ; j+=passo) {
					boolean flag=false;
			
						for(int x = i-(passo/2)-1; x<i+(passo/2)-1; x++) {
							if( plano.getRGB(x,j)!=-1) {
								pontos.add(new Point(i,j));
								flag =true;
								break;
								}
						}
						if(!flag)
							for(int y = j-(passo/2)-1; y < j+(passo/2)-1; y++) {
								if(plano.getRGB(i,y)!=-1) {
									pontos.add(new Point(i,j));
									break;
								}
							}
				}
			}
			this.pontos=pontos;
			repaint();
		}
		else {
			JOptionPane.showMessageDialog(this, "Desenhe a borda primeiro");
		}
		
		
	}
	
	public void removerPontos() {
		this.pontos.clear();
		repaint();
	}
	
	public void removerBorda() {
		this.borda.clear();
		repaint();
	}
	
	public void addBorda() {
		ArrayList<Point> borda = new ArrayList<>();
		BufferedImage img = exportar();
		for(int i = 0 ; i < img.getWidth(); i++) {
			for(int j = 1; j < img.getHeight(); j++) {
				if(img.getRGB(i,j-1)==-1 && img.getRGB(i,j)!=-1)
					borda.add(new Point(i,j));
				else if(img.getRGB(i,j-1)!=-1 && img.getRGB(i,j)==-1)
					borda.add(new Point(i,j-1));
			}
		}
		
		for(int i = 0 ; i < img.getHeight(); i++) {
			for(int j = 1; j < img.getWidth(); j++) {
				if(img.getRGB(j-1,i)==-1 && img.getRGB(j,i)!=-1)
					borda.add(new Point(j,i));
				else if(img.getRGB(j-1,i)!=-1 && img.getRGB(j,i)==-1)
					borda.add(new Point(j-1,i));
			}
		}
		
		this.borda = borda;
		repaint();
	}
	
	public void setImagemFundo(BufferedImage imagem) {
		imagemFundo = imagem;
		repaint();
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved() {
		saved = true;
	}
	
	public ArrayList<FiguraFreeman> getFiguras() {
		return principal;
	}
	
	public void setFiguras(ArrayList<FiguraFreeman> figuras) {
		saved = true;
		principal = figuras;
		if (principal!=null) {
			for(int i = 0; i<principal.size() ;i++) {
				tela.addFiguraMenu(principal.get(i), i+1);
			}
		}
		lixeira.clear();
		repaint();
	}
	
	public void setCor(Color cor) {
		corBorda = cor;
		figuraAtual.setCor(cor);
		repaint();
	}
	
	private void removePainelAuxiliar() {
		remove(painelAuxiliar);
		textoAuxiliar.setText("");
		repaint();
		validate();
	}
	
	private void addPainelAuxiliar() {
		add(painelAuxiliar,BorderLayout.SOUTH);
		textoAuxiliar.requestFocus();
		validate();
		repaint();
	}
	
	public void setFiguraAuxiliar(FiguraFreeman figura) {
		figuraAtual=figura;
	}
	
	private void salvarFigura(FiguraFreeman figura) {
		saved = false;
		principal.add(figura);
		tela.addFiguraMenu(figura, principal.size());
	}
	
	private void newFigura(){
		removePainelAuxiliar();
		if(figuraAtual.getPontoInicial()!=null)
			lixeira.clear();
		if(figuraAtual.getCodigoFreeman()!=null && figuraAtual.getPontoInicial()!=null && figuraAtual.getPasso()!=0)
			salvarFigura(figuraAtual);
		this.figuraAtual = new FiguraFreeman(corBorda);				
	}
	
	public void setTipoDesenho(int tipo) {
		figuraAtual = new FiguraFreeman(corBorda);
		this.tipoFiguraFreeman = tipo;
		removePainelAuxiliar();
		repaint();
	}
	
	private void resetarFigura() {
		if(figuraAtual.getPontoInicial()!=null) {
			figuraAtual = new FiguraFreeman(corBorda);
			lixeira.clear();
			removePainelAuxiliar();
		}
	}
	
	public BufferedImage exportar() {
		BufferedImage imagem = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = imagem.createGraphics();
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, this.getWidth(),this.getHeight());
		
		if(imagemFundo!=null)
			g2d.drawImage(imagemFundo, null, 
					((this.getWidth()/2)-(imagemFundo.getWidth()/2)) ,
					((this.getHeight()/2)-(imagemFundo.getHeight()/2))
					);
		if(borda.size()>0) {
			g2d.setColor(corBorda);
			for(Point ponto:borda)
				g2d.fillRect(ponto.x, ponto.y, 1, 1);
		}
		if(pontos.size()>0) {
			for(Point ponto:pontos) {
				g2d.setColor(Color.RED);
				g2d.fillOval(ponto.x-2, ponto.y-2, 4, 4);
			}
		}
		
		g2d.setStroke(new BasicStroke(2f));
		
		for(FiguraFreeman figura:principal) {
			g2d.setColor(figura.getCor());
        	ArrayList<Point> pontos = figura.gerarPontos();
        	for(int i = 1 ; i < pontos.size() ; i++)
        		g2d.drawLine(pontos.get(i-1).x, pontos.get(i-1).y, pontos.get(i).x, pontos.get(i).y);
        }
		
		g2d.dispose();
		
		return imagem;
	}
	
	public void desfazer() {
		resetarFigura();
		if(principal.size()>0) {
				lixeira.add(principal.get(principal.size()-1));
				tela.removeFiguraMenu(principal.size());
				principal.remove(principal.size()-1);
				limpar=false;
			}
		else if(lixeira.size()>0 && limpar) {
				for(int i = 0; i<lixeira.size(); i++)
					salvarFigura(lixeira.get(i));
				lixeira.clear();
				limpar = false;
		}
		repaint();
	}
	
	public void refazer() {
		resetarFigura();
		if(lixeira.size()>0 && !limpar) {
			salvarFigura(lixeira.get(lixeira.size()-1));
			lixeira.remove(lixeira.size()-1);
			limpar = false;
		}
		repaint();
	}
	
	public void limpar() {
		resetarFigura();
		if(!limpar)
			lixeira.clear();
		for(int i = 0 ; i< principal.size() ; i++)
			lixeira.add(principal.get(i));
		principal.clear();
		tela.removeAllFiguraMenu();
		limpar = true;
		repaint();
	}
	
	
		
	@Override
	public void mouseDragged(MouseEvent e) {
		if(tipoFiguraFreeman == FiguraFreeman.LIVRE && e.getModifiersEx()!=4096) {
			figuraAtual.livre(e);
			repaint();
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(tipoFiguraFreeman == FiguraFreeman.QUADRADO && figuraAtual.getPontoInicial()!=null) {
			figuraAtual.quadrado(e);
			repaint();
		}
		else if(tipoFiguraFreeman == FiguraFreeman.FREEMAN && figuraAtual.getPontoInicial()!=null) {
			
			Point ultimoPonto = figuraAtual.lastPoint();
			int p = figuraAtual.getPasso();
			int y = e.getY() - ultimoPonto.y;
			int x = e.getX() - ultimoPonto.x;
			
			if(x<(-p/2)) {
				if(y>p/2) {
					pontoAtual.setLocation(ultimoPonto.x-p, ultimoPonto.y+p);
					codigoAtual=5;
				}
				else if(y<(-p/2)) {
					pontoAtual.setLocation(ultimoPonto.x-p, ultimoPonto.y-p);
					codigoAtual=3;
				}
				else {
					pontoAtual.setLocation(ultimoPonto.x-p, ultimoPonto.y);
					codigoAtual=4;
				}
			}
			else if(x>p/2) {
				if(y>p/2) {
					pontoAtual.setLocation(ultimoPonto.x+p, ultimoPonto.y+p);
					codigoAtual=7;
				}
				else if(y<(-p/2)) {
					pontoAtual.setLocation(ultimoPonto.x+p, ultimoPonto.y-p);
					codigoAtual=1;
				}
				else {
					pontoAtual.setLocation(ultimoPonto.x+p, ultimoPonto.y);
					codigoAtual=0;
				}
			}
			else {
				if(y>p/2) {
					pontoAtual.setLocation(ultimoPonto.x, ultimoPonto.y+p);
					codigoAtual=6;
				}
				else if(y<(-p/2)) {
					pontoAtual.setLocation(ultimoPonto.x, ultimoPonto.y-p);
					codigoAtual=2;
				}
			}
			mousemoved = true;
			repaint();
		}
		else if(tipoFiguraFreeman == FiguraFreeman.TRIANGULO  && figuraAtual.getPontoInicial()!=null) {
			figuraAtual.triangulo(e);
			repaint();
		}
		else if(tipoFiguraFreeman == FiguraFreeman.RETA && figuraAtual.getPontoInicial()!=null) {
			figuraAtual.reta(e);
			repaint();
		}
		else if (tipoFiguraFreeman == FiguraFreeman.COPIAR || tipoFiguraFreeman == FiguraFreeman.MOVER) {
			figuraAtual.setPontoInicial(e.getPoint());
			repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		requestFocus();
		if(e.isMetaDown()) {
			if(tipoFiguraFreeman!=FiguraFreeman.FREEMAN)
				figuraAtual.reset();
			newFigura();
		}
		else if(tipoFiguraFreeman == FiguraFreeman.FREEMAN) {
			if(figuraAtual.getPontoInicial()==null) {
				figuraAtual.setPontoInicial(e.getPoint());
				pontoAtual.setLocation(e.getPoint());
				labelAuxiliar.setText("Passo");
				addPainelAuxiliar();
			}
			else if(figuraAtual.getPasso()>0) {
				figuraAtual.addCodigoFreeman(codigoAtual);
				repaint();
			}	
		}
		else if(tipoFiguraFreeman == FiguraFreeman.QUADRADO) {
			if(figuraAtual.getPontoInicial()==null) {
				figuraAtual.setPontoInicial(e.getPoint());
				labelAuxiliar.setText("Aresta");
				addPainelAuxiliar();
			}
			else
				newFigura();			}
		else if(tipoFiguraFreeman == FiguraFreeman.TRIANGULO) {
			if(figuraAtual.getPontoInicial()==null) {
				figuraAtual.setPontoInicial(e.getPoint());
			}
			else 
				newFigura();
		}
		else if(tipoFiguraFreeman == FiguraFreeman.RETA) {
			if(figuraAtual.getPontoInicial()==null) {
				figuraAtual.setPontoInicial(e.getPoint());
				figuraAtual.setPasso(1);
			}else {
				newFigura();
				figuraAtual.setPontoInicial(e.getPoint());
				figuraAtual.setPasso(1);
			}
		}
		else if(tipoFiguraFreeman == FiguraFreeman.COPIAR) {
			newFigura();
			tipoFiguraFreeman = FiguraFreeman.DEFAULT;
		}
		else if(tipoFiguraFreeman == FiguraFreeman.MOVER) {
			tipoFiguraFreeman = FiguraFreeman.DEFAULT;
			figuraAtual = new FiguraFreeman(corBorda);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(tipoFiguraFreeman == FiguraFreeman.LIVRE && figuraAtual.getPontoInicial()==null) {
			figuraAtual.setPontoInicial(e.getPoint());
			figuraAtual.setPasso(1);
			repaint();
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(tipoFiguraFreeman == FiguraFreeman.LIVRE) {
			if(e.isMetaDown()) {
				figuraAtual = new FiguraFreeman(corBorda);
				repaint();
			}
			else
				newFigura();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(tipoFiguraFreeman == FiguraFreeman.FREEMAN) {
			if(figuraAtual.getPasso()==0) {
				try {
					int i = Integer.parseInt(textoAuxiliar.getText());
					if(i<1)
						throw new NumberFormatException();
					else {
						figuraAtual.setPasso(i);
						labelAuxiliar.setText("Coordenadas: ");
						requestFocus();
						textoAuxiliar.setText("");
					}
				}catch(NumberFormatException e1) {
					textoAuxiliar.setText("Apenas numero inteiros positivos");
				}
			}
			else {
				String s = textoAuxiliar.getText();
				s = s.replace(" " , "");
				s = s.replace(",","");
				
				ArrayList<Integer> codigos = new ArrayList<>();
				
				try {
					for(int i = 0 ; i<s.length(); i++) {
						int x = Integer.parseInt(s.substring(i, i+1));
						if(x<0 || x>7)
							throw new NumberFormatException();
						else
							codigos.add(x);
					}			
					
					requestFocus();
					textoAuxiliar.setText("");	
					
					figuraAtual.addCodigoFreeman(codigos);
					repaint();
					
				}catch(NumberFormatException e2) {
					textoAuxiliar.setText("Coordenada(s) Invalidas");
				}
				
				
			}
		}
		
		else if(tipoFiguraFreeman == FiguraFreeman.QUADRADO) {
			try {
				int i = Integer.parseInt(textoAuxiliar.getText());
				if(i<1)
					throw new NumberFormatException();
				else {
					figuraAtual.setPasso(i);
					newFigura();
				}
			}catch(NumberFormatException e3) {
				textoAuxiliar.setText("Apenas numero inteiros positivos");
			}
			
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(tipoFiguraFreeman==FiguraFreeman.FREEMAN && figuraAtual.getPontoInicial()!=null && figuraAtual.getPasso()>0) {
			try {
				int aux = Integer.parseInt(""+e.getKeyChar());
				switch(aux) {
					case 6:
						aux = 0;
						break;
					case 9:
						aux = 1;
						break;
					case 8:
						aux = 2;
						break;
					case 7:
						aux = 3;
						break;
					case 4:
						break;
					case 1:
						aux = 5;
						break;
					case 2:
						aux = 6;
						break;
					case 3:
						aux = 7;
						break;
					default:
						throw new NumberFormatException();
							
				}
				figuraAtual.addCodigoFreeman(aux);
				mousemoved = false;
				repaint();
				
			}catch(NumberFormatException e1) {
				char aux = e.getKeyChar();
				int codigo = 10;
				
				if(aux=='d' || aux=='D')
					codigo=0;
				else if(aux=='e' || aux=='E')
					codigo=1;
				else if(aux=='w' || aux=='W')
					codigo=2;
				else if(aux=='q' || aux=='Q')
					codigo=3;
				else if(aux=='a' || aux=='A')
					codigo=4;
				else if(aux=='z' || aux=='Z')
					codigo=5;
				else if(aux=='x' || aux=='X')
					codigo=6;
				else if(aux=='c' || aux=='C')
					codigo=7;
				
				if(codigo!=10) {
					figuraAtual.addCodigoFreeman(codigo);
					mousemoved = false;
					repaint();
				}
					
				
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (tipoFiguraFreeman==FiguraFreeman.FREEMAN ) {
			if(figuraAtual.getPontoInicial()!=null && e.getKeyCode() == KeyEvent.VK_ENTER)
				newFigura();
			else if(figuraAtual.getCodigoFreeman()!=null && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				figuraAtual.removeLast();
				repaint();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}
		
}
