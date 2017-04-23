package thread;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Thread pour Julia
 * @author abrunel
 *
 */
public class Julia extends ThreadFractal implements Runnable{

	private double c_re;
	private double c_im;

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
	 * @param c_re Valeur reelle Julia
	 * @param c_im Valeur imaginaire Julia
	 */
	public Julia(LinkedBlockingQueue<ByteBuffer> link, int width,
			int height, double iter_max, double x_min, double x_max,
			double y_min, double y_max, int nbThreads,int idThread, double c_re, double c_im) {

		super(link, width, height, iter_max, x_min, x_max, y_min, y_max,nbThreads,idThread);
		this.c_re = c_re;
		this.c_im = c_im;
	}

	/**
	 * Lancement du thread
	 */
	public void run() {
		super.juliaCalcul(c_re, c_im);
	}

}
