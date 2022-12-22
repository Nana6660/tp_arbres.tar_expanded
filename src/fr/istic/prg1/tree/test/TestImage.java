
package fr.istic.prg1.tree.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import fr.istic.prg1.tree.Image;
import fr.istic.prg1.tree_util.Iterator;
import fr.istic.prg1.tree_util.Node;

import static org.junit.Assert.assertTrue;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 2.0
 * @since 2015-06-15
 * 
 *        Classe pour les tests unitaires de la classe Image
 */
public class TestImage {

	private static final String ARB1 = "a1.arb";
	private static final String ARB2 = "a2.arb";
	private static final String ARB3 = "a3.arb";
	private static final String ARB5 = "a5.arb";
	private static final String ARB6 = "a6.arb";
	private static final String CARTOON = "cartoon.arb";

	/**
	 * @param image1 première image
	 * @param image2 deuxième image
	 * @return true si les deux images sont identiques comme arbres, false sinon
	 */
	public static boolean compareImages(Image image1, Image image2) {
		Iterator<Node> it1 = image1.iterator();
		Iterator<Node> it2 = image2.iterator();
		return compareImagesAux(it1, it2);
	}

	private static boolean compareImagesAux(Iterator<Node> it1, Iterator<Node> it2) {
		if (it1.isEmpty()) {
			return it2.isEmpty();
		} else if (it2.isEmpty()) {
			return false;
		} else {
			Node n1 = it1.getValue();
			Node n2 = it2.getValue();
			if (n1.state != n2.state) {
				return false;
			}
			if (n1.state == 1 || n1.state == 0) {
				return true;
			}
			it1.goLeft();
			it2.goLeft();
			boolean bool = compareImagesAux(it1, it2);
			it1.goUp();
			it2.goUp();
			if (bool) {
				it1.goRight();
				it2.goRight();
				bool = compareImagesAux(it1, it2);
				it1.goUp();
				it2.goUp();
			}
			return bool;
		}
	}

	/**
	 * @param image image à tester
	 * @return true si l'arbre satisfait les spécifications, false sinon
	 */
	public static boolean testCorrectness(Image image) {
		Iterator<Node> it = image.iterator();
		return testCorrectnessAux(it);
	}

	private static boolean testCorrectnessAux(Iterator<Node> it) {
		if (it.isEmpty()) {
			return true;
		}
		Node n = it.getValue();
		if (n.state == 2) {
			boolean tmp = true;
			boolean tmpL = true;
			it.goLeft();
			if (it.isEmpty()) {
				it.goUp();
				return false; // 2 a fils gauche vide
			}
			Node nLeft = it.getValue();
			tmpL = testCorrectnessAux(it);
			it.goUp();
			if (!tmpL) {
				return false;
			}
			it.goRight();
			if (it.isEmpty()) {
				it.goUp();
				return false; // 2 a fils droit vide
			}
			Node nRight = it.getValue();
			boolean tmpR = testCorrectnessAux(it);
			tmp = tmpR;
			it.goUp();
			if (nLeft.state != 2 && nLeft.state == nRight.state) {
				return false; // 2 a deux fils identiques
			} else {
				return tmp;
			}
		} else {
			it.goLeft();
			boolean tmp = it.isEmpty();
			it.goUp();
			it.goRight();
			tmp = tmp && it.isEmpty();
			it.goUp();
			return tmp; // 0 ou 1 a un fils non vide
		}
	}

	/**
	 * @param fileName nom du fichier à lire
	 * @return Image créée à partir du fichier
	 */
	public static Image readFile(String fileName) {
		Image image = new Image();
		try {
			image.xCreateTreeFromFile(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}

	@Test
	public void testArbre1() {
		Image image = new Image();
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre0() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(2));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre2() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(2));
		it.goLeft();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre3() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(2));
		it.goRight();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre4() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(2));
		it.goLeft();
		it.addValue(Node.valueOf(1));
		it.goRight();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre5() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(1));
		it.goLeft();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre6() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(1));
		it.goRight();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	// @Test
	public void arbre7() {
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.valueOf(1));
		it.goLeft();
		it.addValue(Node.valueOf(1));
		it.goRight();
		it.addValue(Node.valueOf(1));
		assertTrue(testCorrectness(image));
	}

	@Test
	public void testPixel1() {
		Image image1 = readFile(ARB1);
		boolean result = image1.isPixelOn(0, 128);
		assertTrue("pixel a1 0 128", result);

		result = image1.isPixelOn(128, 0);
		assertTrue("pixel a1 128 0", !result);

		result = image1.isPixelOn(192, 128);
		assertTrue("pixel a1 192 128", !result);

		result = image1.isPixelOn(128, 192);
		assertTrue("pixel a1 128 192", !result);

		result = image1.isPixelOn(255, 192);
		assertTrue("pixel a1 255 192", !result);

		result = image1.isPixelOn(192, 255);
		assertTrue("pixel a1 192 255", !result);
	}

	@Test
	public void testPixel2() {
		Image image1 = readFile(ARB2);
		boolean result = image1.isPixelOn(0, 128);

		assertTrue("pixel a2 0 128", !result);

		result = image1.isPixelOn(128, 0);
		assertTrue("pixel a2 128 0", !result);

		result = image1.isPixelOn(192, 128);
		assertTrue("pixel a2 192 128", !result);

		result = image1.isPixelOn(128, 192);
		assertTrue("pixel a2 128 192", result);

		result = image1.isPixelOn(255, 192);
		assertTrue("pixel a2 255 192", !result);

		result = image1.isPixelOn(192, 255);
		assertTrue("pixel a2 192 255", !result);

		result = image1.isPixelOn(32, 128);
		assertTrue("pixel a2 32 128", result);
	}

	@Test
	public void testPixel3() {
		Image image = readFile(ARB2);
		boolean bool1 = image.isPixelOn(213, 95);
		boolean bool2 = image.isPixelOn(97, 97);
		boolean bool3 = image.isPixelOn(5, 249);
		boolean bool4 = image.isPixelOn(249, 5);
		boolean bool5 = image.isPixelOn(5, 5);
		assertTrue("isPixelOn test 3", !bool1 && bool2 && !bool3 && !bool4 && !bool5);
	}

	@Test
	public void testUnion1() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile(ARB2);
		Image image3 = new Image();
		Image image4 = readFile("test-u12.arb");
		image3.union(image1, image2);
		assertTrue("union a1 et a2: arbre conforme ?", testCorrectness(image3));
		assertTrue("union a1 et a2", compareImages(image3, image4));
	}

	@Test
	public void testUnion2() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile(ARB5);
		Image image3 = new Image();
		Image image4 = readFile("test-u15.arb");
		image3.union(image1, image2);
		assertTrue("union a1 et a5: arbre conforme ?", testCorrectness(image3));
		assertTrue("union a1 et a5", compareImages(image3, image4));
	}

	@Test
	public void testIntersection1() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile(ARB2);
		Image image3 = new Image();
		Image image4 = readFile("test-i12.arb");
		image3.intersection(image1, image2);
		assertTrue("intersection a1 et a2: arbre conforme ?", testCorrectness(image3));
		assertTrue("intersection a1 et a2", compareImages(image3, image4));
	}

	@Test
	public void testIntersection2() {
		Image image1 = readFile(ARB2);
		Image image2 = readFile(ARB6);
		Image image3 = new Image();
		Image image4 = readFile("test-i26.arb");
		image3.intersection(image1, image2);
		assertTrue("intersection a2 et a6 : arbre conforme ?", testCorrectness(image3));
		assertTrue("intersection a2 et a6", compareImages(image3, image4));
	}

	@Test
	public void testAffect1() {
		Image image1 = readFile(ARB1);
		Image image2 = new Image();
		image2.affect(image1);
		assertTrue("affect a1: arbre conforme ?", testCorrectness(image2));
		assertTrue("affect a1", compareImages(image1, image2));
	}

	@Test
	public void testAffect2() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile(ARB2);
		image2.affect(image1);
		assertTrue("affect a1: arbre conforme ?", testCorrectness(image2));
		assertTrue("affect a1", compareImages(image1, image2));
	}

	@Test
	public void testRotation1() {
		Image image1 = readFile(ARB1);
		Image image2 = new Image();
		Image image3 = readFile("test-r1.arb");
		image2.rotate180(image1);
		assertTrue("rotation a1", compareImages(image2, image3));
	}

	@Test
	public void testRotation2() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile(ARB2);
		Image image3 = readFile("test-r1.arb");
		image2.rotate180(image1);
		assertTrue("rotation a1", compareImages(image2, image3));
	}

	@Test
	public void testVideoInverse() {
		Image image1 = readFile(ARB2);
		Image image2 = readFile("test-i2.arb");
		image1.videoInverse();
		assertTrue("videoInverse a2", compareImages(image1, image2));
	}

	@Test
	public void testMirrorH() {
		Image image1 = readFile(CARTOON);
		Image image2 = new Image();
		Image image3 = readFile("cartoonh.arb");
		image2.mirrorH(image1);
		assertTrue("mirrorH cartoon", compareImages(image2, image3));
	}

	@Test
	public void testMirrorV() {
		Image image1 = readFile(CARTOON);
		Image image2 = new Image();
		Image image3 = readFile("cartoonv.arb");
		image2.mirrorV(image1);
		assertTrue("mirrorV cartoon", compareImages(image2, image3));
	}

	@Test
	public void testZoomIn1() {
		Image image1 = readFile(CARTOON);
		Image image2 = new Image();
		Image image3 = readFile("cartoonin.arb");
		image2.zoomIn(image1);
		assertTrue("zoomIn cartoon", compareImages(image2, image3));
	}

	@Test
	public void testZoomIn2() {
		Image image1 = readFile(ARB5);
		Image image2 = new Image();
		Image image3 = readFile(ARB3);
		image2.zoomIn(image1);
		assertTrue("zoomIn cartoon", compareImages(image2, image3));
	}

	@Test
	public void testZoomOut1() {
		Image image1 = readFile(CARTOON);
		Image image2 = new Image();
		Image image3 = readFile("cartoonout.arb");
		Image image4 = readFile(CARTOON);
		image2.zoomOut(image1);
		assertTrue("zoomOut cartoon : arbre conforme ?", testCorrectness(image2));
		assertTrue("zoomOut cartoon test 1", compareImages(image2, image3));
		assertTrue("zoomOut cartoon test 2", compareImages(image1, image4));
	}

	@Test
	public void testZoomOut2() {
		Image image1 = readFile(ARB5);
		Image image2 = new Image();
		Image image3 = readFile(ARB3);
		image1.videoInverse();
		image2.zoomOut(image1);
		image3.videoInverse();
		assertTrue("zoomOut2 : arbre conforme ?", testCorrectness(image2));
		assertTrue("zoomOut2 test", compareImages(image2, image3));
	}

	@Test
	public void testDiagonal1() {
		Image image = readFile("d2.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d2", bool);
	}

	@Test
	public void testDiagonal2() {
		Image image = readFile("d3.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d3", !bool);
	}

	@Test
	public void testDiagonal3() {
		Image image = readFile(ARB5);
		boolean bool = image.testDiagonal();
		assertTrue("diagonal a5", !bool);
	}

	@Test
	public void testDiagonal4() {
		Image image = readFile("d4.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d4", bool);
	}

	@Test
	public void testDiagonal5() {
		Image image = readFile("d7.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d7", !bool);
	}

	@Test
	public void testDiagonal6() {
		Image image = readFile("d8.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d8", !bool);
	}

	@Test
	public void testDiagonal7() {
		Image image = readFile("d9.arb");
		boolean bool = image.testDiagonal();
		assertTrue("diagonal d9", !bool);
	}

	@Test
	public void testInclude1() {
		Image image1 = readFile(ARB2);
		Image image2 = readFile(ARB6);
		boolean bool = image1.isIncludedIn(image2);
		assertTrue("inclusion : a2 in a6", !bool);
	}

	@Test
	public void testInclude2() {
		Image image1 = readFile("a8.arb");
		Image image2 = readFile(ARB6);
		boolean bool = image1.isIncludedIn(image2);
		assertTrue("inclusion : a8 in a6", bool);
	}

	@Test
	public void testInclude3() {
		Image image1 = readFile("a8.arb");
		Image image2 = readFile(ARB6);
		boolean bool = image2.isIncludedIn(image1);
		assertTrue("inclusion : a6 in a8", !bool);
	}

	@Test
	public void testInclude4() {
		Image image1 = readFile(ARB1);
		Image image2 = readFile("a4.arb");
		boolean bool = image1.isIncludedIn(image2);
		assertTrue("inclusion : a1 in a4", !bool);
	}

	@Test
	public void testInclude5() {
		Image image1 = readFile(ARB5);
		Image image2 = readFile(ARB3);
		boolean bool = image1.isIncludedIn(image2);
		assertTrue("inclusion : a5 in a3", bool);
	}

	@Test
	public void testSameLeaf1() {
		Image image1 = readFile(ARB5);
		boolean result = image1.sameLeaf(0, 0, 1, 1);
		assertTrue("sameLeaf a5 0 0 1 1", result);

		result = image1.sameLeaf(255, 255, 254, 254);
		assertTrue("sameLeaf a5 255 255 254 254", !result);

		result = image1.sameLeaf(253, 253, 254, 254);
		assertTrue("sameLeaf a5 253 253 254 254", !result);

		result = image1.sameLeaf(252, 252, 253, 253);
		assertTrue("sameLeaf a5 252 252 253 253", result);

		result = image1.sameLeaf(250, 128, 128, 250);
		assertTrue("sameLeaf a5 250 128 128 250", !result);

		result = image1.sameLeaf(128, 192, 191, 255);
		assertTrue("sameLeaf a5 128 192 191 255", result);
	}

	@Test
	public void testSameLeaf2() {
		Image image1 = readFile(ARB2);
		boolean result = image1.sameLeaf(64, 64, 127, 127);
		assertTrue("sameLeaf a2 64 64 127 127", result);

		result = image1.sameLeaf(127, 191, 128, 192);
		assertTrue("sameLeaf a2 127 191 128 192", !result);

		result = image1.sameLeaf(128, 191, 128, 192);
		assertTrue("sameLeaf a2 128 191 128 192", !result);

		result = image1.sameLeaf(127, 192, 128, 192);
		assertTrue("sameLeaf a2 127 192 128 192", !result);

		result = image1.sameLeaf(129, 193, 128, 192);
		assertTrue("sameLeaf a2 129 193 128 192", result);
	}
}
