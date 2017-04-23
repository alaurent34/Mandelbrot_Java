package thread;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Thread pour Buddhabrot
 * @author abrunel
 *
 */
public class Buddhabrot extends ThreadFractal implements Runnable{
	/**
	 * Constructeur
	 * @param link Liaision entre le thread et la classe mere
	 * @param width Largeur du thread
	 * @param height Hauteur du thread
	 * @param iter_max Iteration maximale
	 * @param x_min X minimum
	 * @param x_max X maximun
	 * @param y_min Y minimum
	 * @param y_max Y maximun
	 * @param nbThreads Nombre de thread
	 * @param idThread Id du thread
	 */
	public Buddhabrot(LinkedBlockingQueue<ByteBuffer> link,
			int width, int height, double iter_max, double x_min, double x_max,
			double y_min, double y_max, int nbThreads,int idThread) {

		super(link, width, height, iter_max, x_min, x_max, y_min, y_max,nbThreads,idThread);

	}
	/**
	 * Lancement du thread
	 */
	public void run() {
		super.buddhabrotCalcul();

	}

}
