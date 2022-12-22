package fr.istic.prg1.tree;

import java.util.Scanner;

import fr.istic.prg1.tree_util.AbstractImage;
import fr.istic.prg1.tree_util.Iterator;
import fr.istic.prg1.tree_util.Node;
import fr.istic.prg1.tree_util.NodeType;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2023-09-23
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public class Image extends AbstractImage {
	private static final Scanner standardInput = new Scanner(System.in);

	public Image() {
		super();
	}

	public static void closeAll() {
		standardInput.close();
	}

	/**
	 * this devient identique à image2.
	 *
	 * @param image2 image à copier
	 *
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image2) {
		  Iterator<Node> it = this.iterator();
	      Iterator<Node> it2 = image2.iterator();
	            it.clear();
	            affectAux(it,it2);
	    }
	    public void affectAux(Iterator<Node> it, Iterator<Node> it2) {
	        int val=it2.getValue().state;
	        it.addValue(Node.valueOf(val));
	        if (val==2) {
	            it.goLeft();
	            it2.goLeft();
	            affectAux(it,it2);
	            it.goUp();
	            it2.goUp();
	            it.goRight();
	            it2.goRight();
	            affectAux(it,it2);
	            it.goUp();
	            it2.goUp();
	        }
	    }

	/**
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image2 image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image2) {
		Iterator<Node> it2 = image2.iterator();
		Iterator<Node> it = this.iterator();
		it.clear();
		rotate180Aux(it,it2);
	}
	public void rotate180Aux(Iterator<Node>it,Iterator<Node>it2) {
		if (it2.nodeType() != NodeType.SENTINEL){
			it.addValue(Node.valueOf(it2.getValue().state));
			it2.goLeft();
			it.goRight();
			rotate180Aux(it, it2);
			it2.goUp();
			it.goUp();
			it2.goRight();
			it.goLeft();
			rotate180Aux(it, it2);
			it2.goUp();
			it.goUp();
		}
	}

	/**
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		Iterator<Node> it = this.iterator();
		videoInverseAux(it);
	}
	public void videoInverseAux(Iterator<Node>it){
		if (it.nodeType() != NodeType.SENTINEL){
	        switch (it.getValue().state){
                case 0:
                    it.setValue(Node.valueOf(1));
                    break;
                case 1:
                    it.setValue(Node.valueOf(0));
                    break;
                case 2:
                    it.setValue(Node.valueOf(2));
                    break;
                default : 
                	break ; 
            }
            it.goLeft();
            videoInverseAux(it);
	        it.goUp();
	        it.goRight();
	        videoInverseAux(it);
            it.goUp();
        }

	}

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2 image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image2) {
		Iterator<Node> it1 = this.iterator();
        Iterator<Node> it2 = image2.iterator();
        it1.clear();
        mirrorVAux(it1, it2, 1);
	}
	private void mirrorVAux(Iterator<Node> it1, Iterator<Node> it2, int parity){
        parity++;
        int val=it2.getValue().state;
        if (it2.nodeType() == NodeType.DOUBLE) {
            it1.addValue(Node.valueOf(val));

            if (parity % 2 == 1) {// lorsque c'est impair 
                it2.goLeft();
                it1.goLeft();
                mirrorVAux(it1, it2, parity);
                it2.goUp();
                it1.goUp();
                it2.goRight();
                it1.goRight();
                mirrorVAux(it1, it2, parity);
                it2.goUp();
                it1.goUp();
            }
            if (parity % 2 == 0) {// lorsque c'est pair 
                it2.goLeft();
                it1.goRight();
                mirrorVAux(it1, it2, parity);
                it2.goUp();
                it1.goUp();
                it2.goRight();
                it1.goLeft();
                mirrorVAux(it1, it2, parity);
                it2.goUp();
                it1.goUp();
            }
        }
        else {
            it1.addValue(Node.valueOf(val));
        }
    }

	
	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2 image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
		Iterator<Node> it2 =image2.iterator();
        it.clear();
        mirrorHAux(it, it2 , 0);
	}
	public void mirrorHAux(Iterator<Node> it, Iterator<Node> it2, int parity) {
		parity++; 
		int val=it2.getValue().state;
        if (it2.nodeType() == NodeType.DOUBLE){
            it.addValue(Node.valueOf(val));
            if (parity % 2 == 1){//pas pair
                it2.goLeft();
                it.goLeft();
                mirrorHAux(it, it2, parity);
                it2.goUp();
                it.goUp();

                it2.goRight();
                it.goRight();
                mirrorHAux(it, it2, parity);
                it2.goUp();
                it.goUp();
            }

            if (parity % 2 == 0){//pair
                it2.goLeft();
                it.goRight();
                mirrorHAux(it, it2, parity);
                it2.goUp();
                it.goUp();

                it2.goRight();
                it.goLeft();
                mirrorHAux(it, it2, parity);
                it2.goUp();
                it.goUp();
            }
        }
        else it.addValue(Node.valueOf(val));
    }


	/**
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2 image à agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		Iterator<Node> it= this.iterator();
        Iterator<Node> it2 = image2.iterator();
        it.clear();
        if (it2.nodeType() == NodeType.DOUBLE){
            it2.goLeft();

            if (it2.nodeType() == NodeType.DOUBLE){
                it2.goLeft();
            }
            affectAux(it, it2);
        }
	}
	

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this devient
	 * éteint.
	 * 
	 * @param image2 image à réduire
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
        Iterator<Node> it2 = image2.iterator();
        int taille = image2.height();
        it.clear();
        it.addValue(Node.valueOf(2));
        it.goRight();
        it.addValue(Node.valueOf(0));
        it.goUp();
        it.goLeft();
        it.addValue(Node.valueOf(2));
        it.goRight();
        it.addValue(Node.valueOf(0));
        it.goUp();
        it.goLeft();
        zoomOutAux(it, it2, 0, taille);
        if(this.height()==2 && it.getValue().state==0){
            it.goRoot();
            it.clear();
            it.addValue(Node.valueOf(0));
        }
	}
	private void zoomOutAux(Iterator<Node> it,Iterator<Node> it2,int niveau,int taille){
        niveau++;
        if(niveau<taille-1) {
            if (it2.nodeType() == NodeType.DOUBLE) {
                it.addValue(it2.getValue());
                it.goLeft();
                it2.goLeft();
                zoomOutAux(it,it2,niveau,taille);
                it.goUp();
                it2.goUp();
                it.goRight();
                it2.goRight();
                zoomOutAux(it,it2,niveau,taille);
                it.goUp();
                it2.goUp();

                it.goLeft();
                int state1= it.getValue().state;
                it.goUp();
                it.goRight();
                int state2= it.getValue().state;
                it.goUp();
                if(state1==state2 && state1!=2){
                    it.clear();
                    it.getValue();
                    it.addValue(Node.valueOf(state1));
                }
            } else if (it2.nodeType() == NodeType.LEAF){
                it.addValue(it2.getValue());
            }
        }
        else {
        	zoomOutA(it,it2);
        }
	}
	private void zoomOutA(Iterator<Node> it,Iterator<Node> it2){
		if (it2.nodeType() == NodeType.DOUBLE){
            it2.goLeft();
            int left= it2.getValue().state;
            it2.goUp();
            it2.goRight();
            int right= it2.getValue().state;
            it2.goUp();
            if(left==2 && right==2){
                it.addValue(Node.valueOf(1));
            } else if(left==1 || right==1){
                it.addValue(Node.valueOf(1));
            } else if(left==0 && right==2){
                it.addValue(Node.valueOf(0));
            } else if(left==2 && right==0){
                it.addValue(Node.valueOf(0));
            } else if(left==0 && right==0){
                it.addValue(Node.valueOf(0));
            }
		}
		else if(it2.nodeType() == NodeType.LEAF){
    	it.addValue(it2.getValue());
    }
	}
	
	

	/**
	 * this devient l'intersection de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {
		Iterator<Node> it1 = image1.iterator() ;
		Iterator<Node> it2 = image2.iterator() ;
		Iterator<Node> it = this.iterator() ;
		it.clear(); 
		intersectionAux(it1,it2,it);
	}
	private void intersectionAux(Iterator<Node> it1, Iterator<Node> it2, Iterator<Node> it) {
		int state1 = it1.getValue().state;
        int state2 = it2.getValue().state;
        
        if (state1 == 0 || state2 == 0) {
            it.addValue(Node.valueOf(0));
}
        else  if (state1 == 1 && state2 == 1) {
            it.addValue(Node.valueOf(1));
            
        }
        else if (state1 == 2 && state2 == 2) {
            it.addValue(Node.valueOf(2));
            it1.goLeft();
            it2.goLeft();
            it.goLeft();
            intersectionAux(it1, it2, it);
            it.goUp();
            it.goRight();
            it1.goUp();
            it1.goRight();
            it2.goUp();
            it2.goRight();
            intersectionAux(it1, it2, it);
            it.goUp();
            it1.goUp();
            it2.goUp();
            avoirPosition(it);
        }
        else if (state1 == 1) {
            affectAux(it, it2);
        }
        else {
            affectAux(it, it1);
        }
	}
	private void avoirPosition(Iterator<Node> it) {
		it.goLeft();
		Node node = it.getValue();
		it.goUp();
		it.goRight();
		Node node2 = it.getValue();
		Node node3 = Node.valueOf(2);
		if (node.equals(node2) && !node.equals(node3)) {
			it.clear();
			it.goUp();
			it.goLeft();
			it.clear();
			node3 = node2;
		}
		it.goUp();
		it.setValue(node3);
	}


	/**
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty()
	 *  && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		 Iterator<Node> it = this.iterator();
	     Iterator<Node> it1 = image1.iterator();
	     Iterator<Node> it2 = image2.iterator();
	     it.clear();
	     unionAux(it, it1, it2);
	}
	
	private void unionAux(Iterator<Node>it,Iterator<Node>it1,Iterator<Node>it2) {
		int state1 = it1.getValue().state;
        int state2 = it2.getValue().state;

        if (state1 == 0 && state2 == 0) {
            it.addValue(Node.valueOf(0));
        }
        else if (state1 == 1 || state2 == 1) {
            it.addValue(Node.valueOf(1));
        }
        else if (state1 == state2 && state1 == 2) {
            it.addValue(Node.valueOf(2));
            it1.goLeft();
            it2.goLeft();
            it.goLeft();
            unionAux(it, it1, it2);
            it1.goUp();
            it2.goUp();
            it.goUp();
            it1.goRight();
            it2.goRight();
            it.goRight();
            unionAux(it, it1, it2);
            it1.goUp();
            it2.goUp();
            it.goUp();
            avoirPosition(it);
        }
        else if (state1 == 0) {
            affectAux(it, it2);
        }
        else {
            affectAux(it, it1);
        }
	}
	
	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	@Override
	public boolean testDiagonal() {
		Iterator<Node> it = this.iterator();
        return it.getValue().state == 1 || testDiagonalAux(it);
	}
	private boolean testDiagonalAux(Iterator<Node> it){
        if (it.getValue().state == 1) {
            return true;
        }
        else if (it.getValue().state == 2) {
            it.goLeft();
            boolean left = diagonalAux(it, true);
            it.goUp();
            it.goRight();
            boolean right = diagonalAux(it, false);
            it.goUp();
            return left && right;
        }
        else {
            return false;
        }
    }
	private boolean diagonalAux(Iterator<Node> it, boolean isLeft) {
        if (it.getValue().state == 1) {
            return true;
        }
        else if (it.getValue().state == 0) {
            return false;
        }
        else {
            if (isLeft) {
                it.goLeft();
            }
            else {
                it.goRight();
            }
            boolean diag = testDiagonalAux(it);
            it.goUp();
            return diag;
        }
    }



	/**
	 * @param x abscisse du point
	 * @param y ordonnée du point
	 * @pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y){
		Iterator<Node> it = this.iterator(); 
		return isPixelOnAux(it,0,x,y);
	}
	private boolean isPixelOnAux(Iterator<Node> it,int depth , int x , int y ) {
		 int minX = 0; 
	     int minY = 0; 
	     int max = 256; 
	     int temp;
		while(it.nodeType()!= NodeType.LEAF) {
		temp = max/2 ;
		if(depth % 2 == 0) {
			if(y < temp + minY) {
				it.goLeft();
			}else {
				minY = minY + temp;
				it.goRight();
			}
		}else {
			max = temp ; 
			if (x < minX + temp) {
				it.goLeft();
			}else {
				minX = minX + temp; 
				it.goRight();
			}
		}
		++depth;
		}
		return it.getValue().state == 1;
	}
	/**
	 * @param x1 abscisse du premier point
	 * @param y1 ordonnée du premier point
	 * @param x2 abscisse du deuxième point
	 * @param y2 ordonnée du deuxième point
	 * @pre !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par la
	 *         même feuille de this, false sinon
	 */
	@Override
	public boolean sameLeaf(int x1, int y1, int x2, int y2) {
		Iterator<Node> it = this.iterator();
		return sameLeafAux(it,x1,y1,x2,y2) ; 
	}
	
	private boolean sameLeafAux(Iterator<Node> it,int x1, int y1, int x2, int y2) {
        int compte = 0;
        int minX = 0; 
        int minY = 0;
        int max = 256; //taille maximale
        int temp;
        while (it.nodeType() != NodeType.LEAF) {
            temp = max / 2;
            if (compte % 2 == 0) {
                if ((y1 < minY + temp) && (y2 < minY + temp)) {
                    it.goLeft();
                } else if ((y1 >= minY + temp) && (y2 >= minY + temp)) {
                	minY = minY + temp;
                    it.goRight();
                } else {
                    return false;
                }
            } else {
                max = temp;
                if ((x1 < minX + max) && (x2 < minX + max)) {
                    it.goLeft();
                } else if ((x1 >= minX + max) && (x2 >= minX + max)) {
                	minX = minX + max;
                    it.goRight();
                } else {
                    return false;
                }
            }
            compte++;
        }
        return true;
	}


	/**
	 * @param image2 autre image
	 * @pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés false
	 *         sinon
	 */
	@Override
	public boolean isIncludedIn(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
		Iterator<Node> it2 = image2.iterator();
        return xIncludedIn(it, it2);
		}
	private boolean xIncludedIn(Iterator<Node> it, Iterator<Node> it2) {
		if (((it.getValue().state == 1 || it.getValue().state == 2) && it2.getValue().state == 0) 
				|| (it.getValue().state == 1 && it2.getValue().state == 2)) {
            return false;
        }
        else if (it.getValue().state == 2 && it2.getValue().state == 2) {
            it.goLeft();
            it2.goLeft();
            boolean left = xIncludedIn(it, it2);
            it.goUp();
            it.goRight();
            it2.goUp();
            it2.goRight();
            boolean right = xIncludedIn(it, it2);
            it.goUp();
            it2.goUp();
            return left && right;
        } else {
            return true;
        }
    }
}