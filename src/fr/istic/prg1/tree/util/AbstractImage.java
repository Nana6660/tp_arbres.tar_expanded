package fr.istic.prg1.tree.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import fr.istic.prg1.tree_util.ImageTable;
import fr.istic.prg1.tree_util.ImageWindow;
import fr.istic.prg1.tree_util.Iterator;
import fr.istic.prg1.tree_util.Node;
import fr.istic.prg1.tree_util.NodeType;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2016-04-20
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public abstract class AbstractImage extends BinaryTree<Node> {

	protected static final Scanner standardInput = new Scanner(System.in);
	protected static final int WINDOW_SIZE = 256;
	private static final ImageTable table = new ImageTable();

	protected AbstractImage() {
		super();
	}

	/**
	 * Crée this à partir d’un fichier texte (cf a1.arb, ...) et l’affiche dans une
	 * fenêtre. Chaque ligne du fichier est de la forme (e x1 y1 x2 y2) et indique
	 * si on souhaite éteindre (e=0) ou allumer (e=1) la région rectangulaire de
	 * coordonnées x1, y1, x2, y2. Le fichier se termine par un e de valeur -1.
	 */
	public void constructTreeFromFile() {
		Iterator<Node> it = this.iterator();
		it.clear();
		System.out.print("nom du fichier d'entree : ");
		String fileName = standardInput.nextLine();
		try {
			InputStream inFile = new FileInputStream(fileName);
			System.out.println("Corrige : createTreeFromFile");
			System.out.println("---------------------");
			xCreateTreeFromFile(inFile);
			inFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("fichier " + fileName + " inexistant");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("impossible de fermer le fichier " + fileName);
			System.exit(0);
		}
	}

	public void xCreateTreeFromFile(InputStream is) {
		xReadImageFromFile(is);
		Iterator<Node> it = this.iterator();
		xConstructTree(it, 0, 0, WINDOW_SIZE, true);
	}

	private static void xReadImageFromFile(InputStream is) {
		Scanner scanner = (is == System.in) ? standardInput : new Scanner(is);
		int state;
		int x1;
		int x2;
		int y1;
		int y2;
		table.clearWindow();
		state = scanner.nextInt();
		while (state != -1) {
			x1 = scanner.nextInt();
			y1 = scanner.nextInt();
			x2 = scanner.nextInt();
			y2 = scanner.nextInt();
			if (state == 0) {
				table.switchOff(x1, y1, x2, y2);
			} else {
				table.switchOn(x1, y1, x2, y2);
			}
			state = scanner.nextInt();
		}
		if (is != System.in) {
			scanner.close();
		}
	}

	private static void xConstructTree(Iterator<Node> it, int x, int y, int squareWidth, boolean isSquare) {
		int rectangleWidth = squareWidth / 2;
		int state;
		if (isSquare) {
			state = table.state(x, y, x + squareWidth - 1, y + squareWidth - 1);
		} else {
			state = table.state(x, y, x + squareWidth - 1, y + rectangleWidth - 1);
		}
		it.addValue(Node.valueOf(state));
		if (state == 2) {
			it.goLeft();
			if (isSquare) {
				xConstructTree(it, x, y, squareWidth, false);
			} else {
				xConstructTree(it, x, y, rectangleWidth, true);
			}
			it.goUp();
			it.goRight();
			if (isSquare) {
				xConstructTree(it, x, y + rectangleWidth, squareWidth, false);
			} else {
				xConstructTree(it, x + rectangleWidth, y, rectangleWidth, true);
			}
			it.goUp();
		}
	}

	/**
	 * Sauvegarder, dans un fichier texte, les feuilles de this selon un format
	 * conforme aux fichiers manipulés par la commande constructTreeFromFile.
	 *
	 * @pre !this.isEmpty()
	 */
	public void saveImage() {
		System.out.print("nom du fichier de sortie : ");
		String fileName = standardInput.next();
		try (OutputStream outFile = new FileOutputStream(fileName);) {
			System.out.println("Corrige : Save");
			System.out.println("----------------");
			Iterator<Node> it = this.iterator();
			String ch = xSave(it, 0, 0, WINDOW_SIZE, true) + "-1\n";
			outFile.write(ch.getBytes());
			outFile.flush();
		} catch (FileNotFoundException e) {
			System.out.println("probleme d'ouverture de fichier pour " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("impossible de fermer le fichier " + fileName);
			System.exit(0);
		}
	}

	private static String xSave(Iterator<Node> it, int x, int y, int squareWidth, boolean isSquare) {
		int rectangleWidth = squareWidth / 2;
		Node node;
		String result = "";
		switch (it.nodeType()) {
		case LEAF:
			node = it.getValue();
			if (isSquare) {
				if (node.state == 1) {
					result = result + "1 " + x + " " + y + " " + (x + squareWidth - 1) + " " + (y + squareWidth - 1)
							+ "\n";
				}
			} else if (node.state == 1) {
				result = result + "1 " + x + " " + y + " " + (x + squareWidth - 1) + " " + (y + rectangleWidth - 1)
						+ "\n";
			}
			break;
		case DOUBLE:
			it.goLeft();
			if (isSquare) {
				result += xSave(it, x, y, squareWidth, false);
			} else {
				result += xSave(it, x, y, rectangleWidth, true);
			}
			it.goUp();
			it.goRight();
			if (isSquare) {
				result += xSave(it, x, y + rectangleWidth, squareWidth, false);
			} else {
				result += xSave(it, x + rectangleWidth, y, rectangleWidth, true);
			}
			it.goUp();
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * Afficher this sous forme d'image dans la fenêtre graphique.
	 * 
	 * @param windowNumber numéro de la fenêtre (de 0 à 4)
	 * @param window       fenêtre graphique pour l'affichage des images
	 */
	public void plotImage(int windowNumber, ImageWindow window) {
		System.out.println("Corrige : plotImage");
		System.out.println("------------------");
		window.clearWindow(windowNumber);
		Iterator<Node> it = this.iterator();
		xPlotTree(it, 0, 0, WINDOW_SIZE, true, windowNumber, window);
	}

	private static void xPlotTree(Iterator<Node> it, int x, int y, int squareWidth, boolean isSquare, int windowNumber,
			ImageWindow window) {
		int rectangleWidth = squareWidth / 2;
		Node node;
		switch (it.nodeType()) {
		case LEAF:
			node = it.getValue();
			if (node.state == 1) {
				if (isSquare) {
					window.switchOn(x, y, x + squareWidth - 1, y + squareWidth - 1, windowNumber);
				} else {
					window.switchOn(x, y, x + squareWidth - 1, y + rectangleWidth - 1, windowNumber);
				}
			}
			break;
		case DOUBLE:
			it.goLeft();
			if (isSquare) {
				xPlotTree(it, x, y, squareWidth, false, windowNumber, window);
			} else {
				xPlotTree(it, x, y, rectangleWidth, true, windowNumber, window);
			}
			it.goUp();
			it.goRight();
			if (isSquare) {
				xPlotTree(it, x, y + rectangleWidth, squareWidth, false, windowNumber, window);
			} else {
				xPlotTree(it, x + rectangleWidth, y, rectangleWidth, true, windowNumber, window);
			}
			it.goUp();
			break;
		default:
			break;
		}

	}

	/**
	 * Afficher this sous forme d’arbre dans une fenêtre externe.
	 *
	 * @pre !this.isEmpty()
	 */
	public void plotTree() {
		System.out.println("Corrige plotTree");
		System.out.println("------------------");
		TreeFrame frame = new TreeFrame(this);
		frame.setVisible(true);
	}

	/**
	 * @pre !this.isEmpty()
	 * @return hauteur de this
	 */
	public int height() {
		System.out.println("Corrige : Height");
		System.out.println("-----------------");
		return xHeight(this.iterator());
	}

	protected static int xHeight(Iterator<Node> it) {
		NodeType type = it.nodeType();
		assert type == NodeType.LEAF || type == NodeType.DOUBLE : "l'arbre comporte des noeuds simples";
		int leftHeight = 0;
		int rightHeight = 0;
		switch (type) {
		case LEAF:
			return 0;
		case DOUBLE:
			it.goLeft();
			leftHeight = xHeight(it);
			it.goUp();
			it.goRight();
			rightHeight = xHeight(it);
			it.goUp();
			break;
		default: /* impossible */
		}
		return (leftHeight > rightHeight) ? leftHeight + 1 : rightHeight + 1;
	}

	/**
	 * @pre !this.isEmpty()
	 * @return nombre de noeuds de this
	 */
	public int numberOfNodes() {
		System.out.println("Corrige : numberOfNodes");
		System.out.println("-----------------");
		return xNumberOfNodes(this.iterator());
	}

	protected static int xNumberOfNodes(Iterator<Node> it) {
		NodeType type = it.nodeType();
		assert type == NodeType.LEAF || type == NodeType.DOUBLE : "l'arbre comporte des noeuds simples";
		assert (type != NodeType.SENTINEL) : "l'iterateur est sur le butoir";
		int number = 0;
		switch (type) {
		case LEAF:
			return 1;
		case DOUBLE:
			it.goLeft();
			number = xNumberOfNodes(it);
			it.goUp();
			it.goRight();
			number += xNumberOfNodes(it);
			it.goUp();
			break;
		default: /* impossible */
		}
		return number + 1;
	}

	/**
	 * @param x abscisse du point
	 * @param y ordonnée du point
	 * @pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	public abstract boolean isPixelOn(int x, int y);

	/**
	 * this devient identique à image2.
	 *
	 * @param image2 image à copier
	 *
	 * @pre !image2.isEmpty()
	 */
	public abstract void affect(AbstractImage image);

	/**
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image2 image pour rotation
	 * @pre !image2.isEmpty()
	 */
	public abstract void rotate180(AbstractImage image2);

	/**
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	public abstract void videoInverse();

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2 image à agrandir
	 * @pre !image2.isEmpty()
	 */
	public abstract void mirrorV(AbstractImage image2);

	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2 image à agrandir
	 * @pre !image2.isEmpty()
	 */
	public abstract void mirrorH(AbstractImage image2);

	/**
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2 image à agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	public abstract void zoomIn(AbstractImage image2);

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this devient
	 * éteint.
	 * 
	 * @param image2 image à réduire
	 * @pre !image2.isEmpty()
	 */
	public abstract void zoomOut(AbstractImage image2);

	/**
	 * this devient l'intersection de image2 et image3 au sens des pixels allumés.
	 * 
	 * @pre !image2.isEmpty() && !image3.isEmpty()
	 * 
	 * @param image2
	 * @param image3
	 */
	public abstract void intersection(AbstractImage image2, AbstractImage image3);

	/**
	 * this devient l'union de image2 et image3 au sens des pixels allumés.
	 * 
	 * @pre !image2.isEmpty() && !image3.isEmpty()
	 * 
	 * @param image2
	 * @param image3
	 */
	public abstract void union(AbstractImage image2, AbstractImage image3);

	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	public abstract boolean testDiagonal();

	/**
	 * @param x1 abscisse du premier point
	 * @param y1 ordonnée du premier point
	 * @param x2 abscisse du deuxième point
	 * @param y2 ordonnée du deuxième point
	 * @pre !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par la
	 *         même feuille de this, false sinon
	 */
	public abstract boolean sameLeaf(int x1, int y1, int x2, int y2);

	/**
	 * @param image2 autre image
	 * @pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés false
	 *         sinon
	 */
	public abstract boolean isIncludedIn(AbstractImage image2);

}
