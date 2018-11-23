import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PainelDesenho extends JPanel 
	implements MouseListener,MouseMotionListener,ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	private ArrayList<Figura> principal = new ArrayList<>();
	private ArrayList<Figura> lixeira = new ArrayList<>();
	private Color aux_cor = Color.BLACK;
	private Figura aux_figura = new Figura(aux_cor);
	private Point aux_ponto = new Point();
	private Point aux_ponto2 = new Point();
	private int aux_cod;
	private int tipo_desenho = Desenho.DEFAULT;
	private JPanel aux_painel = new JPanel();
	private JLabel aux_label = new JLabel();
	private JTextField aux_texto = new JTextField(30);
	private boolean mousemoved = false;
	private boolean limpar = false;
	private boolean saved = true;
	
	public PainelDesenho() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		aux_painel.setLayout(new FlowLayout());
		aux_painel.add(aux_label);
		aux_painel.add(aux_texto);
		aux_texto.addActionListener(this);
		
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gg = (Graphics2D) g;
		gg.setStroke(new BasicStroke(2f));
		
		
		for(Figura figura:principal) 
			doDesenho(figura,gg);
		
		if(aux_figura.getPonto()!=null) 
			doDesenho(aux_figura,gg);
		
	}
	
	public void doDesenho(Figura figura, Graphics2D gg) {
		
		gg.setColor(figura.getCor());
		int passo = figura.getPasso();
		Point ponto = figura.getPonto().getLocation();
		int codigos[] = figura.getCodigo();
		Point[] coordenadas;
		if(codigos!=null) {
			coordenadas = new Point[codigos.length+1];
			coordenadas[0] = ponto.getLocation();
			
			for(int i = 0; i<codigos.length;i++) {
				switch(codigos[i]) {
				case 0:
					ponto.setLocation(ponto.x+passo,ponto.y);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 1:
					ponto.setLocation(ponto.x+passo,
							ponto.y-passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 2:
					ponto.setLocation(ponto.x, ponto.y-passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 3:
					ponto.setLocation(ponto.x-passo,
							ponto.y-passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 4: 
					ponto.setLocation(ponto.x- passo, ponto.y);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 5:
					ponto.setLocation(ponto.x-passo, 
							ponto.y+passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 6:
					ponto.setLocation(ponto.x, ponto.y+passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				case 7:
					ponto.setLocation(ponto.x+passo, 
							ponto.y+passo);
					coordenadas[i+1] = ponto.getLocation();
					break;
				}
				
				
				gg.drawLine(coordenadas[i].x,coordenadas[i].y,
						coordenadas[i+1].x,coordenadas[i+1].y);
				
			}
		}
		else {
			coordenadas = new Point[1];
			coordenadas[0] = figura.getPonto();
		}
			
		if(figura.equals(aux_figura))
			aux_ponto =  coordenadas[coordenadas.length-1].getLocation();
		
		if(mousemoved){
			gg.setColor(aux_cor);
			gg.drawLine(aux_ponto.x,
					aux_ponto.y,
					aux_ponto2.x,aux_ponto2.y);
			mousemoved = false;
		}
		
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved() {
		saved = true;
	}
	
	public ArrayList<Figura> getFiguras() {
		return principal;
	}
	
	public void setFiguras(ArrayList<Figura> figuras) {
		saved = true;
		principal = figuras;
		lixeira.clear();
		repaint();
	}
	
	public void setCor(Color cor) {
		aux_cor = cor;
		aux_figura.setCor(cor);
	}
	
	public void removePainelAuxiliar() {
		remove(aux_painel);
		aux_texto.setText("");
		repaint();
		validate();
	}
	
	public void addPainelAuxiliar() {
		add(aux_painel,BorderLayout.SOUTH);
		aux_texto.requestFocus();
		validate();
		repaint();
	}
	
	public void newFigura(){
		saved = false;
		removePainelAuxiliar();
		if(aux_figura.getPonto()!=null)
			lixeira.clear();
		if(aux_figura.getCodigo()!=null && aux_figura.getPonto()!=null && aux_figura.getPasso()!=0)
			principal.add(aux_figura);
		this.aux_figura = new Figura(aux_cor);				
	}
	
	public void setTipoDesenho(int tipo) {
		aux_figura = new Figura(aux_cor);
		this.tipo_desenho = tipo;
		removePainelAuxiliar();
		repaint();
	}
	
	public void resetarFigura() {
		if(aux_figura.getPonto()!=null) {
			aux_figura = new Figura(aux_cor);
			lixeira.clear();
			removePainelAuxiliar();
		}
	}
	
	public void desfazer() {
		resetarFigura();
		if(principal.size()>0) {
				lixeira.add(principal.get(principal.size()-1));
				principal.remove(principal.size()-1);
				limpar=false;
			}
		else if(lixeira.size()>0 && limpar) {
				for(int i = 0; i<lixeira.size(); i++)
					principal.add(lixeira.get(i));
				lixeira.clear();
				limpar = false;
		}
		repaint();
	}
	
	public void refazer() {
		resetarFigura();
		if(lixeira.size()>0 && !limpar) {
			principal.add(lixeira.get(lixeira.size()-1));
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
		limpar = true;
		repaint();
	}
		
	@Override
	public void mouseDragged(MouseEvent e) {
		if(tipo_desenho == Desenho.LIVRE && e.getModifiersEx()!=4096) {
			int y = e.getY() - aux_ponto.y;
			int x = e.getX() - aux_ponto.x ;

			int codigos[];
			int cont,resto,distrib;
			int yd = Math.abs(y);
			int xd = Math.abs(x);
			int flag = 1;	
			int flag2 = 0;
			
			if(xd>yd) {
				codigos = new int[xd];
				if(yd!=0) {
					cont = xd/yd;
					resto = xd%yd;
				}
				else {
					cont = xd+1;
					resto=0;
				}
				if(resto>0)
					distrib=yd/resto;
				else
					distrib=0;
			}else {
				codigos = new int[yd];
				if(xd!=0) {
					cont = yd/xd;
					resto = yd%xd;
				}
				else {
					cont = yd+1;
					resto=0;
				}
				if(resto>0)
					distrib=xd/resto;
				else
					distrib=0;
			}
						
			if(x>=0) {
				if(y<0) {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 1;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=0;
									resto--;
								}
							}else {
								codigos[i] = 0;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 1;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=2;
									resto--;
								}
							}else {
								codigos[i] = 2;
								flag++;
							}
						}
					}
				}else {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 7;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=0;
									resto--;
								}
							}else {
								codigos[i] = 0;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 7;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=6;
									resto--;
								}
							}else {
								codigos[i] = 6;
								flag++;
							}
						}
					}
				}
			}else {
				if(y<0) {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 3;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=4;
									resto--;
								}
							}else {
								codigos[i] = 4;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 3;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=2;
									resto--;
								}
							}else {
								codigos[i] = 2;
								flag++;
							}
						}
					}
				}else {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 5;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=4;
									resto--;
								}
							}else {
								codigos[i] = 4;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 5;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=6;
									resto--;
								}
							}else {
								codigos[i] = 6;
								flag++;
							}
						}
					}
				}
			}
			aux_figura.addCodigo(codigos);
			repaint();
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(tipo_desenho == Desenho.QUADRADO && aux_figura.getPonto()!=null) {
			int y1 = aux_figura.getPonto().y;
			int y2 = e.getY();
			int x1 = aux_figura.getPonto().x;
			int x2 = e.getX();
			int p = Math.abs(y1-y2);
			int p2 = Math.abs(x1-x2);
			if(p>p2) {
				aux_texto.setText(p+"");
				aux_figura.setPasso(p);
			}else {
				aux_texto.setText(p2+"");
				aux_figura.setPasso(p2);
			}
			if(y1<y2) {
				if(x1<x2) {
					int codigos[] = {0,6,4,2};
					aux_figura.setCodigo(codigos);
				}
				else {
					int codigos[] = {4,6,0,2};
					aux_figura.setCodigo(codigos);
				}
			}
			else {
				if(x1<x2) {
					int codigos[] = {0,2,4,6};
					aux_figura.setCodigo(codigos);
				}
				else {
					int codigos[] = {2,4,6,0};
					aux_figura.setCodigo(codigos);
				}
			}
			
			repaint();
		}
		else if(tipo_desenho == Desenho.FREEMAN) {
			int p = aux_figura.getPasso();
			int y = e.getY() - aux_ponto.y;
			int x = e.getX() - aux_ponto.x ;
					
			if(x<(-p/2)) {
				if(y>p/2) {
					aux_ponto2.setLocation(aux_ponto.x-p, aux_ponto.y+p);
					aux_cod=5;
				}
				else if(y<(-p/2)) {
					aux_ponto2.setLocation(aux_ponto.x-p, aux_ponto.y-p);
					aux_cod=3;
				}
				else {
					aux_ponto2.setLocation(aux_ponto.x-p, aux_ponto.y);
					aux_cod=4;
				}
			}
			else if(x>p/2) {
				if(y>p/2) {
					aux_ponto2.setLocation(aux_ponto.x+p, aux_ponto.y+p);
					aux_cod=7;
				}
				else if(y<(-p/2)) {
					aux_ponto2.setLocation(aux_ponto.x+p, aux_ponto.y-p);
					aux_cod=1;
				}
				else {
					aux_ponto2.setLocation(aux_ponto.x+p, aux_ponto.y);
					aux_cod=0;
				}
			}
			else {
				if(y>p/2) {
					aux_ponto2.setLocation(aux_ponto.x, aux_ponto.y+p);
					aux_cod=6;
				}
				else if(y<(-p/2)) {
					aux_ponto2.setLocation(aux_ponto.x, aux_ponto.y-p);
					aux_cod=2;
				}
			}
			mousemoved = true;
			repaint();
		}
		else if(tipo_desenho == Desenho.TRIANGULO  && aux_figura.getPonto()!=null) {
			int x1 = aux_figura.getPonto().x;
			int y1 = aux_figura.getPonto().y;
			int x2 = e.getX();
			int y2 = e.getY();
			int x = x2-x1;
			int y = y2-y1;
			int xd = Math.abs(x);
			int yd = Math.abs(y);
			
			if(xd>yd && yd<(xd/2)) {
				aux_figura.setPasso(xd/2);
				if(y2>y1) {
					if(x2>x1) {
						int codigos[] = {0,0,5,3};
						aux_figura.setCodigo(codigos);
					}else {
						int codigos[] = {4,4,7,1};
						aux_figura.setCodigo(codigos);
					}
				}else {
					if(x2>x1) {
						int codigos[] = {0,0,3,5};
						aux_figura.setCodigo(codigos);
					}else {
						int codigos[] = {4,4,1,7};
						aux_figura.setCodigo(codigos);
					}
				}
				
			}
			else if(yd>xd && xd<(yd/2)) {
				aux_figura.setPasso(yd/2);
				if(y2>y1) {
					if(x2>x1) {
						int codigos[] = {6,6,1,3};
						aux_figura.setCodigo(codigos);
					}else {
						int codigos[] = {6,6,3,1};
						aux_figura.setCodigo(codigos);
					}
				}else {
					if(x2>x1) {
						int codigos[] = {2,2,7,5};
						aux_figura.setCodigo(codigos);
					}else {
						int codigos[] = {2,2,5,7};
						aux_figura.setCodigo(codigos);
					}
				}
			}else {
				if(xd>yd)
					aux_figura.setPasso(xd);
				else
					aux_figura.setPasso(yd);
				
				if((x*y)>0) {
					if(x>0) {
						if(y<x) {
							int codigos[] = {0,6,3};
							aux_figura.setCodigo(codigos);
						}else {
							int codigos[] = {6,0,3};
							aux_figura.setCodigo(codigos);
						}
					}else {
						if(x<y) {
							int codigos[] = {4,2,7};
							aux_figura.setCodigo(codigos);
						}else {
							int codigos[] = {2,4,7};
							aux_figura.setCodigo(codigos);
						}
					}
				}else {
					if(x>0) {
						if(x>(Math.abs(y))) {
							int codigos[] = {0,2,5};
							aux_figura.setCodigo(codigos);
						}else {
							int codigos[] = {2,0,5};
							aux_figura.setCodigo(codigos);
						}
					}else {
						if(y>(Math.abs(x))) {
							int codigos[] = {6,4,1};
							aux_figura.setCodigo(codigos);
						}else {
							int codigos[] = {4,6,1};
							aux_figura.setCodigo(codigos);
						}
					}
				}
			}
			repaint();
		}
		else if(tipo_desenho == Desenho.RETA && aux_figura.getPonto()!=null) {
			int y = e.getY() - aux_figura.getPonto().y;
			int x = e.getX() - aux_figura.getPonto().x ;
			
			int codigos[];
			int cont,resto,distrib;
			int yd = Math.abs(y);
			int xd = Math.abs(x);
			int flag = 1;	
			int flag2 = 0;
			
			if(xd>yd) {
				codigos = new int[xd];
				if(yd!=0) {
					cont = xd/yd;
					resto = xd%yd;
				}
				else {
					cont = xd+1;
					resto=0;
				}
				if(resto>0)
					distrib=yd/resto;
				else
					distrib=0;
			}else {
				codigos = new int[yd];
				if(xd!=0) {
					cont = yd/xd;
					resto = yd%xd;
				}
				else {
					cont = yd+1;
					resto=0;
				}
				if(resto>0)
					distrib=xd/resto;
				else
					distrib=0;
			}
			
			
			if(x>=0) {
				if(y<0) {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 1;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=0;
									resto--;
								}
							}else {
								codigos[i] = 0;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 1;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=2;
									resto--;
								}
							}else {
								codigos[i] = 2;
								flag++;
							}
						}
					}
				}else {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 7;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=0;
									resto--;
								}
							}else {
								codigos[i] = 0;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 7;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=6;
									resto--;
								}
							}else {
								codigos[i] = 6;
								flag++;
							}
						}
					}
				}
			}else {
				if(y<0) {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 3;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=4;
									resto--;
								}
							}else {
								codigos[i] = 4;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 3;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=2;
									resto--;
								}
							}else {
								codigos[i] = 2;
								flag++;
							}
						}
					}
				}else {
					if(xd>yd) {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 5;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=4;
									resto--;
								}
							}else {
								codigos[i] = 4;
								flag++;
							}
						}
					}else {
						for(int i = 0; i<codigos.length; i++) {
							if(flag==cont) {
								codigos[i] = 5;
								flag = 1;
								flag2++;
								if(resto>0 && flag2==distrib) {
									flag2=0;
									codigos[++i]=6;
									resto--;
								}
							}else {
								codigos[i] = 6;
								flag++;
							}
						}
					}
				}
			}
			aux_figura.setCodigo(codigos);
			repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		requestFocus();
		if(e.isMetaDown()) {
			if(tipo_desenho!=Desenho.FREEMAN)
				aux_figura.setCodigo(null);
			newFigura();
		}
		else if(tipo_desenho == Desenho.FREEMAN) {
			if(aux_figura.getPonto()==null) {
				aux_figura.setPonto(e.getPoint());
				aux_ponto2.setLocation(e.getPoint());
				aux_label.setText("Passo");
				addPainelAuxiliar();
			}
			else if(aux_figura.getPasso()>0) {
				int codigo[] = {aux_cod};
				aux_figura.addCodigo(codigo);
				repaint();
			}	
		}
		else if(tipo_desenho == Desenho.QUADRADO) {
			if(aux_figura.getPonto()==null) {
				aux_figura.setPonto(e.getPoint());
				lixeira.clear();
				aux_label.setText("Aresta");
				addPainelAuxiliar();
			}
			else
				newFigura();			}
		else if(tipo_desenho == Desenho.TRIANGULO) {
			if(aux_figura.getPonto()==null) {
				aux_figura.setPonto(e.getPoint());
				lixeira.clear();
			}
			else 
				newFigura();
		}
		else if(tipo_desenho == Desenho.RETA) {
			if(aux_figura.getPonto()==null) {
				aux_figura.setPonto(e.getPoint());
				aux_figura.setPasso(1);
				lixeira.clear();
			}else {
				newFigura();
				aux_figura.setPonto(e.getPoint());
				aux_figura.setPasso(1);
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(tipo_desenho == Desenho.LIVRE && aux_figura.getPonto()==null) {
			aux_figura.setPonto(e.getPoint());
			aux_figura.setPasso(1);
			repaint();
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(tipo_desenho == Desenho.LIVRE) {
			if(e.isMetaDown()) {
				aux_figura = new Figura(aux_cor);
				repaint();
			}
			else
				newFigura();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(tipo_desenho == Desenho.FREEMAN) {
			if(aux_figura.getPasso()==0) {
				try {
					int i = Integer.parseInt(aux_texto.getText());
					if(i<1)
						throw new NumberFormatException();
					else {
						aux_figura.setPasso(i);
						aux_label.setText("Coordenadas: ");
						requestFocus();
						aux_texto.setText("");
					}
				}catch(NumberFormatException e1) {
					aux_texto.setText("Apenas numero inteiros positivos");
				}
			}
			else {
				String s = aux_texto.getText();
				s = s.replace(" " , "");
				s = s.replace(",","");
				
				int codigos[] = new int[s.length()];
				
				try {
					for(int i = 0 ; i<s.length(); i++) {
						int x = Integer.parseInt(s.substring(i, i+1));
						if(x<0 || x>7)
							throw new NumberFormatException();
						else
							codigos[i] = x;
					}			
					
					requestFocus();
					aux_texto.setText("");	
					
					aux_figura.addCodigo(codigos);
					repaint();
					
				}catch(NumberFormatException e2) {
					aux_texto.setText("Coordenada(s) Invalidas");
				}
				
				
			}
		}
		
		else if(tipo_desenho == Desenho.QUADRADO) {
			try {
				int i = Integer.parseInt(aux_texto.getText());
				if(i<1)
					throw new NumberFormatException();
				else {
					aux_figura.setPasso(i);
					newFigura();
				}
			}catch(NumberFormatException e3) {
				aux_texto.setText("Apenas numero inteiros positivos");
			}
			
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(tipo_desenho==Desenho.FREEMAN && aux_figura.getPonto()!=null && aux_figura.getPasso()>0) {
			try {
				int aux = Integer.parseInt(""+e.getKeyChar());
				if(aux<0 || aux>7)
					throw new NumberFormatException();
				int codigo[] = {aux};
				aux_figura.addCodigo(codigo);
				mousemoved = false;
				repaint();
			}catch(NumberFormatException e1) {}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (tipo_desenho==Desenho.FREEMAN && e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(aux_figura.getPonto()!=null)
				newFigura();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}
		
}
