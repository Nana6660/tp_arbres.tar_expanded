package fr.istic.prg1.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.istic.prg1.tree_util.ImageWindow;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 4.0
 * @since 2015-06-15
 * 
 *        Classe pour tester visuellement la classe Image
 */
public class TpArbre {

	/**
	 * nombre des images manipulées
	 */
	private static final int NUMBER_OF_TREES = 5;
	/**
	 * ensemble des images manipulées
	 */
	private static ArrayList<Image> imageSet = new ArrayList<>(NUMBER_OF_TREES);
	/**
	 * entrée standard
	 */
	private static final Scanner standardInput = new Scanner(System.in);
	/**
	 * Chaînes décrivant les commandes du menus
	 */
	private static final String[] TEXT_COMMANDS = new String[19];
	/**
	 * noms des actions pour l'affichage des items de choix
	 */
	private static final String[] ITEM_NAMES = { "constructTreeFromFile", "saveTree", "height & numberOfNodes",
			"isPixelOn", "affect", "videoInverse", "rotate180", "rotation90", "mirrorV", "mirrorH", "zoomIn", "zoomOut",
			"intersection", "union", "testDiagonal", "sameLeaf", "isIncludedIn", "plotTree", "close" };

	private static JFrame ourWindow = new JFrame();
	private static JMenuBar menuBar = new JMenuBar();
	private static JMenu menuChoice = new JMenu("MENU");

	/**
	 * tailles de la fenêtre affichant les images
	 */
	private static final int SIZE_X = 1408;
	private static final int SIZE_Y = 340;
	/**
	 * décalage horizontal de fenêtre de dessin
	 */
	private static final int SHIFT_X = 10;
	/**
	 * décalage vertical de fenêtre de dessin
	 */
	private static final int SHIFT_Y = -50;
	/**
	 * fenêtre pour l'affichage des images
	 */
	private static final ImageWindow window = new ImageWindow(SIZE_X - SHIFT_X, SIZE_Y);

	/**
	 * numéros des arbres (i, j, k)
	 */
	private static int firstTreeNumber;
	private static int secondTreeNumber;
	private static int thirdTreeNumber;

	private static final String NON_EMPTY = " d'arbre non vide (-1 pour retour au menu) : ";

	/**
	 * Classe interne décrivant l'écouteur de la réaction au choix effectué avec la
	 * souris.
	 */
	private static class Action implements ActionListener {
		private int actionNumber;

		public Action(int numAction) {
			this.actionNumber = numAction;
		}

		@Override
		public synchronized void actionPerformed(ActionEvent event) {
			menuBar.remove(menuChoice);
			menuExecution(actionNumber);
			menuBar.add(menuChoice);
		}

		/**
		 * Exécuter l'action choisie dans le menu.
		 * 
		 * @param choiceNumber numéro de l'action choisie
		 */
		private static void menuExecution(int choiceNumber) {
			System.out.println("* " + TEXT_COMMANDS[choiceNumber]);
			acquisition(choiceNumber);
			if ((firstTreeNumber != -1) && (secondTreeNumber != -1) && (thirdTreeNumber != -1)) {
				switch (choiceNumber) {
				case 0:
					imageSet.get(firstTreeNumber).constructTreeFromFile();
					break;
				case 1:
					imageSet.get(firstTreeNumber).saveImage();
					break;
				case 2:
					System.out.println("hauteur = " + imageSet.get(firstTreeNumber).height() + " et nombre de noeuds = "
							+ imageSet.get(firstTreeNumber).numberOfNodes());
					break;
				case 3:
					int x = readCoords("x");
					int y = readCoords("y");
					if (imageSet.get(firstTreeNumber).isPixelOn(x, y)) {
						System.out.println("point (" + x + ", " + y + ") allumé");
					} else {
						System.out.println("point (" + x + ", " + y + ") éteint");
					}
					break;
				case 4:
					imageSet.get(firstTreeNumber).affect(imageSet.get(secondTreeNumber));
					break;
				case 5:
					imageSet.get(firstTreeNumber).videoInverse();
					break;
				case 6:
					imageSet.get(firstTreeNumber).rotate180(imageSet.get(secondTreeNumber));
					break;
				case 7:
					System.out.println("Rotation 90 n'est pas implémentée");
					break;
				case 8:
					imageSet.get(firstTreeNumber).mirrorV(imageSet.get(secondTreeNumber));
					break;
				case 9:
					imageSet.get(firstTreeNumber).mirrorH(imageSet.get(secondTreeNumber));
					break;
				case 10:
					imageSet.get(firstTreeNumber).zoomIn(imageSet.get(secondTreeNumber));
					break;
				case 11:
					imageSet.get(firstTreeNumber).zoomOut(imageSet.get(secondTreeNumber));
					break;
				case 12:
					imageSet.get(firstTreeNumber).intersection(imageSet.get(secondTreeNumber),
							imageSet.get(thirdTreeNumber));
					break;
				case 13:
					imageSet.get(firstTreeNumber).union(imageSet.get(secondTreeNumber), imageSet.get(thirdTreeNumber));
					break;
				case 14:
					if (imageSet.get(firstTreeNumber).testDiagonal()) {
						System.out.println("diagonale entierement allumee");
					} else {
						System.out.println("diagonale non entierement allumee");
					}
					break;
				case 15:
					int x1 = readCoords("x1");
					int y1 = readCoords("y1");
					int x2 = readCoords("x2");
					int y2 = readCoords("y2");
					if (imageSet.get(firstTreeNumber).sameLeaf(x1, y1, x2, y2)) {
						System.out.println("points dans la même feuille");
					} else {
						System.out.println("points dans feuilles différentes");
					}
					break;
				case 16:
					if (imageSet.get(firstTreeNumber).isIncludedIn(imageSet.get(secondTreeNumber)))
						System.out.println("E[" + firstTreeNumber + "] inclus dans E[" + secondTreeNumber + "]");
					else
						System.out.println("E[" + firstTreeNumber + "] non inclus dans E[" + secondTreeNumber + "]");
					break;
				case 17:
					imageSet.get(firstTreeNumber).plotTree();
					break;
				case 18:
				default:
					for (int i = NUMBER_OF_TREES; i > 0; --i) {
						imageSet.remove(i - 1);
					}
					standardInput.close();
					Image.closeAll();
					System.exit(0);
				}
			}
			plotAllTrees();
		}

		/**
		 * Afficher tous les arbres.
		 */
		private static void plotAllTrees() {
			for (int i = 0; i < NUMBER_OF_TREES; ++i) {
				imageSet.get(i).plotImage(i + 1, window);
			}
		}

		/**
		 * Acquérir les données pour les actions,
		 * 
		 * @param choiceNumber numéro de l'action choisie
		 */
		private static void acquisition(int choiceNumber) {
			int i = 0;
			int j = 0;
			int k = 0; // valeurs par default
			switch (choiceNumber) {
			case 0:
				i = readFirstNumber();
				break; // Construire : -1<= i<MAXARB
			case 1:
			case 2:
			case 3:
			case 5:
			case 14:
			case 15:
			case 17:
				/*
				 * Sauver, Dessiner, Hauteur, PointAllume, InverseVideo, TesterDiagonale,
				 * MemeFeuille, AfficherArbre : i<>-1 == > E.get(i) non vide
				 */
				do {
					i = readFirstNumberNotEmpty();
					if (i != -1) {
						imageSet.get(i);
					}
				} while ((i != -1) && imageSet.get(i).isEmpty());
				break;
			case 4:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
				// Affecter, Rotation180, Rotation90, mirrorV, mirrorH, zoomIn,
				// zoomOut : i<>-1, j<>-1 = > j<>i et E.get(j) non
				// vide
				i = readFirstNumber();
				if (i != -1) {
					do {
						j = readSecondNumber(i);
						if (j != -1) {
							imageSet.get(j);
						}
					} while ((j != -1) && imageSet.get(j).isEmpty());
				}
				break;
			case 16:
				/*
				 * Inclus : i<>-1 et j<>-1 == > E.get(i) non vide, j<>i et E.get(j) non vide
				 */
				do {
					i = readFirstNumberNotEmpty();
					if (i != -1) {
						imageSet.get(i);
					}
				} while ((i != -1) && imageSet.get(i).isEmpty());
				if (i != -1) {
					do {
						j = readSecondNumber(i);
						if (j != -1) {
							imageSet.get(j);
						}
					} while ((j != -1) && imageSet.get(j).isEmpty());
				}
				break;
			case 12:
			case 13:
				/*
				 * Inter, Union : i<>-1, j<>-1, k<>-1 == > j<>i, k<>i, E.get(j) et E.get(k) non
				 * vides
				 */
				i = readFirstNumber();
				if (i != -1) {
					do {
						j = readSecondNumber(i);
						if (j != -1) {
							imageSet.get(j);
						}
					} while ((j != -1) && imageSet.get(j).isEmpty());
					if (j != -1) {
						do {
							k = readThirdNumber(i, j);
							if (k != -1) {
								imageSet.get(k);
							}
						} while ((k != -1) && imageSet.get(k).isEmpty());
					}
				}
				break;
			case 18:
				break; // Quitter
			default:
				System.out.println("acquisition : choix non prevu");
			}
			firstTreeNumber = i;
			secondTreeNumber = j;
			thirdTreeNumber = k;
		}
		
		/**
		 * 
		 * @param xOrY nom de la coordonnée
		 * @return un entier entre 0 et 255
		 */
		private static int readCoords(String xOrY) {
			String stringForPrint = "coordonnée " + xOrY + " (entre 0 et 255) : ";
			return readInteger(0, 255, stringForPrint);
		}

		/**
		 * @return premier numéro d'arbre dans le menu
		 */
		private static int readFirstNumber() {
			String stringForPrint = "numéro i<" + NUMBER_OF_TREES + " d'arbre (-1 pour retour au menu) : ";
			return readInteger(-1, NUMBER_OF_TREES - 1, stringForPrint);
		}

		/**
		 * @return premier numéro d'arbre non vide dans le menu
		 */
		private static int readFirstNumberNotEmpty() {
			String stringForPrint = "numéro i<" + NUMBER_OF_TREES + NON_EMPTY;
			return readInteger(-1, NUMBER_OF_TREES - 1, stringForPrint);
		}

		/**
		 * @param i numéro du premier arbre
		 * @return deuxième numéro d'arbre (différent de i) dans le menu
		 */
		private static int readSecondNumber(int i) {
			int j;
			String stringForPrint = "numéro j<" + NUMBER_OF_TREES + " différent de " + i + NON_EMPTY;
			do {
				j = readInteger(-1, NUMBER_OF_TREES - 1, stringForPrint);
			} while (j == i);
			return j;
		}

		/**
		 * @param i numéro du premier arbre
		 * @param j numéro du deuxième arbre
		 * @return troisième numéro d'arbre (différent de i et de j) dans le menu
		 */
		private static int readThirdNumber(int i, int j) {
			int k;
			String stringForPrint = "numéro k<" + NUMBER_OF_TREES + " différent de " + i + " et de " + j + NON_EMPTY;
			do {
				k = readInteger(-1, NUMBER_OF_TREES - 1, stringForPrint);
			} while (k == i || k == j);
			return k;
		}

		/**
		 * @param min           valeur minimale du nombre saisi
		 * @param max           valeur maximale du nombre saisi
		 * @param commentString chaîne à afficher avant la saisie
		 * @return entier entre min et max
		 */
		private static int readInteger(int min, int max, String commentString) {
			int value = -1;
			boolean end;
			do {
				value = -1;
				System.out.print(commentString);
				try {
					value = standardInput.nextInt();
					end = (value >= min) && (value <= max);
				} catch (NumberFormatException e) {
					end = false;
				}
			} while (!end);
			return value;
		}
	}

	public static void main(String[] args) {
		try {
			TEXT_COMMANDS[0] = "E[i] <-- dessin contenu dans un fichier";
			TEXT_COMMANDS[1] = "sauvegarde du dessin E[i] dans un fichier";
			TEXT_COMMANDS[2] = "hauteur et nombre de noeuds de l'arbre E[i]";
			TEXT_COMMANDS[3] = "teste, par exploration de E[i], si un point est allumé";
			TEXT_COMMANDS[4] = "affectation E[i] <-- E[j]";
			TEXT_COMMANDS[5] = "E[i] <-- complémentaire de E[i]";
			TEXT_COMMANDS[6] = "E[i] <-- rotation à 180 degres de E[j]";
			TEXT_COMMANDS[7] = "E[i] <-- rotation à 90 degres de E[j]";
			TEXT_COMMANDS[8] = "E[i] <-- image miroir vertical de E[j]";
			TEXT_COMMANDS[9] = "E[i] <-- image miroir horizontal de E[j]";
			TEXT_COMMANDS[10] = "E[i] <-- zoom sur le quart supérieur gauche de E[j]";
			TEXT_COMMANDS[11] = "E[i] <-- E[j] devient le quart supérieur gauche";
			TEXT_COMMANDS[12] = "E[i] <-- E[j] intersection E[k]";
			TEXT_COMMANDS[13] = "E[i] <-- E[j] union E[k]";
			TEXT_COMMANDS[14] = "teste si la diagonale de E[i] est entièrement allumée";
			TEXT_COMMANDS[15] = "teste, pour E[i], si deux points sont dans la même feuille";
			TEXT_COMMANDS[16] = "teste si E[i] est inclus dans E[j]";
			TEXT_COMMANDS[17] = "affiche E[i] sous forme d'arbre";
			TEXT_COMMANDS[18] = "arrêt de l'exécution";

			for (int i = 0; i < NUMBER_OF_TREES; ++i) {
				imageSet.add(new Image());
			}
			/*
			 * préparation de la fenêtre d'affichage
			 */
			ourWindow.setTitle("TP arbres binaires");
			ourWindow.setSize(SIZE_X, SIZE_Y);
			ourWindow.setLocation(0, 0);
			ourWindow.setBackground(Color.white);
			ourWindow.setLayout(null);
			/*
			 * le placement n'utilise pas de gestionnaire
			 */

			/*
			 * ajout de la barre menu, du menu choix et des items de choix
			 */
			ourWindow.setJMenuBar(menuBar);
			menuChoice.setFont(new Font("Serif", Font.BOLD, 14));
			menuBar.add(menuChoice);
			for (int i = 0; i < ITEM_NAMES.length; ++i) {
				JMenuItem choice = new JMenuItem(ITEM_NAMES[i]);
				choice.setFont(new Font("Serif", Font.BOLD, 12));
				menuChoice.add(choice);
				menuChoice.addSeparator();
				choice.addActionListener(new Action(i));
			}

			/*
			 * ajout de la fenêtre de dessin
			 */
			window.setLocation(SHIFT_X, SHIFT_Y);
			window.setSize(SIZE_X - SHIFT_X, SIZE_Y);
			ourWindow.add(window);
			ourWindow.setVisible(true);
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}