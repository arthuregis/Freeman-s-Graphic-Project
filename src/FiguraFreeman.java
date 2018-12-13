import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class FiguraFreeman implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT = 0;
	public static final int FREEMAN = 1;
	public static final int QUADRADO = 2;
	public static final int LIVRE = 3;
	public static final int TRIANGULO = 4;
	public static final int RETA = 5;
	public static final int COPIAR = 6;
	public static final int MOVER = 7;
	
	
	private ArrayList<Integer> codigoFreeman;
    private int passo;
    private Point pontoInicial;
    private Color cor;
    
    private int[] pontosX = new int[]{1, 1, 0, -1, -1, -1, 0, 1};
    private int[] pontosY = new int[]{0, -1, -1, -1, 0, 1, 1, 1};
    
    public FiguraFreeman(Color cor) {
    	setCor(cor);
    	passo=0;
    }    
    
    public ArrayList<Point> gerarPontos(){
    	ArrayList<Point> pontos = new ArrayList<>();
        
        pontos.add(new Point(pontoInicial.x, pontoInicial.y));
        
        
        int xAnterior = pontoInicial.x;
        int yAnterior = pontoInicial.y;
        
        int xAtual;
        int yAtual;
        if(codigoFreeman!=null)
	        for(int i: codigoFreeman){
	            
	            xAtual = xAnterior + (passo*pontosX[i]);
	            yAtual = yAnterior + (passo*pontosY[i]);
	            
	            pontos.add(new Point(xAtual,yAtual));
	            
	            xAnterior = xAtual;
	            yAnterior = yAtual;
	        }
        
        return pontos;
    }
    
    public Point lastPoint() {
    	if(codigoFreeman==null)
    		return pontoInicial;
    	ArrayList<Point> pontos = gerarPontos();
    	return pontos.get(pontos.size()-1);
    }
    
    public void removeLast() {
    	if(codigoFreeman.size()>0)
    		codigoFreeman.remove(codigoFreeman.size()-1);
    }
    
    public void reset() {
    	codigoFreeman = null;
    }
 
    public ArrayList<Integer> getCodigoFreeman() {
        return codigoFreeman;
    }
    
    public void addCodigoFreeman(int codigo) {
    	if(codigoFreeman==null)
    		codigoFreeman = new ArrayList<>();
    	codigoFreeman.add(codigo);
    }
    
    public void addCodigoFreeman(ArrayList<Integer> codigoFreeman) {
    	if(this.codigoFreeman==null)
    		this.codigoFreeman = new ArrayList<>();
    	this.codigoFreeman.addAll(codigoFreeman);
    }

    public int getPasso() {
        return passo;
    }

    public void setPasso(int passo) {
        this.passo = passo;
    }

    public void setPontoInicial(Point ponto) {
    	pontoInicial = ponto;
    }
    
    public Point getPontoInicial() {
    	return pontoInicial;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }
    
    public void quadrado(MouseEvent e) {
    	int y1 = pontoInicial.y;
        int y2 = e.getY();
        int x1 = pontoInicial.x;
        int x2 = e.getX();
        int passoy = Math.abs(y1 - y2);
        int passox = Math.abs(x1 - x2);
        if (passoy > passox)
        	passo = passoy;
        else 
        	passo = passox;
        
        ArrayList<Integer> codigos = new ArrayList<Integer>();
       
        if (y1 < y2) {
            if (x1 < x2) {
            	codigos.add(0);
            	codigos.add(6);
            	codigos.add(4);
            	codigos.add(2);
            } else {
            	codigos.add(4);
            	codigos.add(6);
            	codigos.add(0);
            	codigos.add(2);
            }
        } else {
            if (x1 < x2) {
            	codigos.add(0);
            	codigos.add(2);
            	codigos.add(4);
            	codigos.add(6);
            } else {
            	codigos.add(2);
            	codigos.add(4);
            	codigos.add(6);
            	codigos.add(0);
            }
        }
        
        codigoFreeman = codigos;
    }
    
    public void triangulo(MouseEvent e) {
    	int x1 = pontoInicial.x;
        int y1 = pontoInicial.y;
        int x2 = e.getX();
        int y2 = e.getY();
        int x = x2 - x1;
        int y = y2 - y1;
        int passox = Math.abs(x);
        int passoy = Math.abs(y);
        ArrayList<Integer> codigos = new ArrayList<Integer>();

        if (passox > passoy && passoy < (passox / 2)) {
        	passo = passox / 2;
            if (y2 > y1) {
                if (x2 > x1) {
                    codigos.add(0);
                    codigos.add(0);
                    codigos.add(5);
                    codigos.add(3);
                } else {
                    codigos.add(4);
                    codigos.add(4);
                    codigos.add(7);
                    codigos.add(1);
                }
            } else {
                if (x2 > x1) {
                    codigos.add(0);
                    codigos.add(0);
                    codigos.add(3);
                    codigos.add(5);
                } else {
                    codigos.add(4);
                    codigos.add(4);
                    codigos.add(1);
                    codigos.add(7);
                }
            }

        } else if (passoy > passox && passox < (passoy / 2)) {
            passo = passoy / 2;
            if (y2 > y1) {
                if (x2 > x1) {
                    codigos.add(6);
                    codigos.add(6);
                    codigos.add(1);
                    codigos.add(3);
                } else {
                    codigos.add(6);
                    codigos.add(6);
                    codigos.add(3);
                    codigos.add(1);
                }
            } else {
                if (x2 > x1) {
                    codigos.add(2);
                    codigos.add(2);
                    codigos.add(7);
                    codigos.add(5);
                } else {
                    codigos.add(2);
                    codigos.add(2);
                    codigos.add(5);
                    codigos.add(7);
                }
            }
        } else {
            if (passox > passoy) {
                passo = passox;
            } else {
                passo = passoy;
            }

            if ((x * y) > 0) {
                if (x > 0) {
                    if (y < x) {
                        codigos.add(0);
                        codigos.add(6);
                        codigos.add(3);
                    } else {
                        codigos.add(6);
                        codigos.add(0);
                        codigos.add(3);
                    }
                } else {
                    if (x < y) {
                        codigos.add(4);
                        codigos.add(2);
                        codigos.add(7);
                    } else {
                        codigos.add(2);
                        codigos.add(4);
                        codigos.add(7);
                    }
                }
            } else {
                if (x > 0) {
                    if (x > (Math.abs(y))) {
                        codigos.add(0);
                        codigos.add(2);
                        codigos.add(5);
                    } else {
                        codigos.add(2);
                        codigos.add(0);
                        codigos.add(5);
                    }
                } else {
                    if (y > (Math.abs(x))) {
                        codigos.add(6);
                        codigos.add(4);
                        codigos.add(1);
                    } else {
                        codigos.add(4);
                        codigos.add(6);
                        codigos.add(1);
                    }
                }
            }
        }
        codigoFreeman = codigos;
    }
    
    private ArrayList<Integer> conectLivre(int tamanho,int cont, int resto,int distrib, int diagonal, int reta) {
		ArrayList<Integer> codigos = new ArrayList<>();
		int flag = 1, flag2 =0;
		for(int i = 0; i<tamanho; i++) {
			if(flag==cont) {
				codigos.add(diagonal);
				flag = 1;
				flag2++;
				if(resto>0 && flag2==distrib) {
					flag2=0;
					codigos.add(reta);
					i++;
					resto--;
				}
			}else {
				codigos.add(reta);
				flag++;
			}
		}
		return codigos;
	}
    
    public void livre(MouseEvent e) {
    	int y = e.getY() - lastPoint().y;
		int x = e.getX() - lastPoint().x ;

		ArrayList<Integer> codigos = new ArrayList<>();
		int cont,resto,tamanho,distrib;
		int yd = Math.abs(y);
		int xd = Math.abs(x);
		
		if(xd>yd) {
			tamanho = xd;
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
			tamanho = yd;
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
					codigos = conectLivre(tamanho,cont,resto,distrib,1,0);
				}else {
					codigos = conectLivre(tamanho,cont,resto,distrib,1,2);
				}
			}else {
				if(xd>yd) {
					codigos = conectLivre(tamanho,cont,resto,distrib,7,0);
				}else {
					codigos = conectLivre(tamanho,cont,resto,distrib,7,6);
				}
			}
		}else {
			if(y<0) {
				if(xd>yd) {
					codigos = conectLivre(tamanho,cont,resto,distrib,3,4);
				}else {
					codigos = conectLivre(tamanho,cont,resto,distrib,3,2);
				}
			}else {
				if(xd>yd) {
					codigos = conectLivre(tamanho,cont,resto,distrib,5,4);
				}else {
					codigos = conectLivre(tamanho,cont,resto,distrib,5,6);
				}
			}
		}
		addCodigoFreeman(codigos);
    }
    
    private ArrayList<Integer> conectReta(int tamanho,int prop, int aux, int cod_excesso, int cod_minoria){
		ArrayList<Integer> codigos = new ArrayList<>();
		int flag = 0;
		for(int i = 0; i <tamanho ;i++){
			if((i+1)%aux == 0)
				codigos.add(cod_excesso);
			else if(flag==0){
					codigos.add(cod_minoria);
					flag=prop;
				}else{
					codigos.add(cod_excesso);
					flag--;
				}
			
		}
		return codigos;
	}
    
    public void reta (MouseEvent e) {
	    int y = e.getY() - pontoInicial.y;
		int x = e.getX() - pontoInicial.x ;

		int xd = Math.abs(x);
		int yd = Math.abs(y);

		int diagonais, retas, prop, resto, aux, tamanho;
		ArrayList<Integer> codigos = new ArrayList<>();

		if (xd>yd){
			tamanho = xd;
			diagonais = yd;
			retas = xd - yd;
		}else{
			tamanho = yd;
			diagonais = xd;
			retas = yd-xd;
		}

		if(diagonais>retas){
			if(retas == 0){
				prop = diagonais+1;
				resto=0;
			}else{
				prop = diagonais/retas;
				resto=diagonais%retas;
			}
		}else{
			if(diagonais==0){
				prop = retas+1;
				resto = 0;
			}else{
				prop = retas/diagonais;
				resto=retas%diagonais;
			}
		}
		 if(resto==0)
			 aux = tamanho+1;
		 else
			aux = tamanho/(resto+1);


		if(x>0){
			if(y<0){
				if(yd>xd){
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,2,1);
					}else{
						codigos = conectReta(tamanho,prop,aux,1,2);
					}
				}else{
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,0,1);
					}else{
						codigos = conectReta(tamanho,prop,aux,1,0);
					}
				}
			}else{
				if(xd>yd){
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,0,7);
					}else{
						codigos = conectReta(tamanho,prop,aux,7,0);
					}
				}else{
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,6,7);
					}else{
						codigos = conectReta(tamanho,prop,aux,7,6);
					}
				}
			}
		}else{
			if(y>0){
				if(yd>xd){
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,6,5);
					}else{
						codigos = conectReta(tamanho,prop,aux,5,6);
					}
				}else{
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,4,5);
					}else{
						codigos = conectReta(tamanho,prop,aux,5,4);
					}
				}
			}else{
				if(xd>yd){
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,4,3);
					}else{
						codigos = conectReta(tamanho,prop,aux,3,4);
					}
				}else{
					if(retas>diagonais){
						codigos = conectReta(tamanho,prop,aux,2,3);
					}else{
						codigos = conectReta(tamanho,prop,aux,3,2);
					}
				}
			}
		}
		codigoFreeman = codigos;
    }
    
    public void rotacionarH() {
    	
		for(int i = 0; i<codigoFreeman.size(); i++) {
			switch(codigoFreeman.get(i)) {
			case 0:
				codigoFreeman.set(i,6);
				break;
			case 1:
				codigoFreeman.set(i,7);
				break;
			case 2:
				codigoFreeman.set(i,0);
				break;
			case 3:
				codigoFreeman.set(i,1);
				break;
			case 4:
				codigoFreeman.set(i,2);
				break;
			case 5:
				codigoFreeman.set(i,3);
				break;
			case 6:
				codigoFreeman.set(i,4);
				break;
			case 7:
				codigoFreeman.set(i,5);
				break;
			}
		}
	}
    
    public void rotacionarAH() {
    	
		for(int i = 0; i<codigoFreeman.size(); i++) {
			switch(codigoFreeman.get(i)) {
			case 0:
				codigoFreeman.set(i,2);
				break;
			case 1:
				codigoFreeman.set(i,3);
				break;
			case 2:
				codigoFreeman.set(i,4);
				break;
			case 3:
				codigoFreeman.set(i,5);
				break;
			case 4:
				codigoFreeman.set(i,6);
				break;
			case 5:
				codigoFreeman.set(i,7);
				break;
			case 6:
				codigoFreeman.set(i,0);
				break;
			case 7:
				codigoFreeman.set(i,1);
				break;
			}
		}
	}
    
    @SuppressWarnings("unchecked")
	public FiguraFreeman clone() {
    	FiguraFreeman clone = new FiguraFreeman(cor);
    	clone.codigoFreeman = (ArrayList<Integer>) this.codigoFreeman.clone();
    	clone.passo = this.passo;
    	clone.pontoInicial = this.pontoInicial.getLocation();
		return clone;
    	
    }
}
