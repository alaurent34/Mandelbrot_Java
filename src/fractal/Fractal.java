package fractal;

import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import textureManager.TextureManager;
import thread.Buddhabrot;
import thread.Julia;
import thread.Mandelbrot;
import utils.State;
import view.DisplayManager;
import controller.Controller;
/**
 * Gestion des calculs des fractales
 * @author gtrauchessec
 * @author abrunel
 *
 */
public class Fractal {
	private static double x_max=0.6;
	private static double x_min=-2.1;
	private static double y_max=1.2;
	private static double y_min=-1.2;	
	private static double c_re;
	private static double c_im;
	private static double iter_max=1024;
	private static double zoom=0.0;
	private static double zoom_step=2.0;
	private static int nbThreads = 1;

	private static boolean verbose = true;

	/**
	 * Calcul de la fractale de Mandelbrot
	 */
	public static void mandleBrot(){
		long tempsDebut = System.currentTimeMillis();
		int width = DisplayManager.getWidth();
		int height = DisplayManager.getHeight();
		LinkedBlockingQueue<ByteBuffer> link = new LinkedBlockingQueue<ByteBuffer>();
		try {
			Vector<Thread> threads = new Vector<Thread>();
			int idThread=0;
			for(int i=0;i<nbThreads;++i){
				idThread++;
				threads.add(new Thread(new Mandelbrot(link, width, height,iter_max,x_min,x_max,y_min,y_max,nbThreads,idThread)));
			}
			for(Thread t: threads){
				t.start();
			}
			ByteBuffer tmp = BufferUtils.createByteBuffer(((width*height*3)/nbThreads)+1);
			ByteBuffer [] bufferListe = new ByteBuffer[nbThreads];
			for(int i=0;i<nbThreads;++i){
				bufferListe[i] = BufferUtils.createByteBuffer(((width*height*3)/nbThreads)+1);
			}
			for(int i=0;i<nbThreads;++i){
				tmp = link.take();
				int index = tmp.get();
				bufferListe[index].put(tmp);
				bufferListe[index].rewind();
			}
			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*3);
			for(int i=0;i<height;++i){
				for(int j=0;j<width/nbThreads;++j){
					for(int k=0;k<nbThreads;++k){
						buffer.put(bufferListe[k].get());
						buffer.put(bufferListe[k].get());
						buffer.put(bufferListe[k].get());
					}
				}
			}
			buffer.flip();
			TextureManager.loadTexture(buffer);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		long tempsFin = System.currentTimeMillis();
		if(verbose)
			System.out.println("Fractale Mandelbrot calculée en: "+ Float.toString((tempsFin - tempsDebut) / 1000F) + " secondes.");
	}

	/**
	 * Calcul de la fractale de Julia
	 */
	public static void julia(){
		long tempsDebut = System.currentTimeMillis();
		int width = DisplayManager.getWidth();
		int height = DisplayManager.getHeight();
		LinkedBlockingQueue<ByteBuffer> link = new LinkedBlockingQueue<ByteBuffer>();
		try {
			Vector<Thread> threads = new Vector<Thread>();
			int idThread=0;
			for(int i=0;i<nbThreads;++i){
				idThread++;
				threads.add(new Thread(new Julia(link, width, height,iter_max,x_min,x_max,y_min,y_max,nbThreads,idThread,c_re,c_im)));
			}
			for(Thread t: threads){
				t.start();
			}
			ByteBuffer tmp = BufferUtils.createByteBuffer(((width*height*3)/nbThreads)+1);
			ByteBuffer [] bufferListe = new ByteBuffer[nbThreads];
			for(int i=0;i<nbThreads;++i){
				bufferListe[i] = BufferUtils.createByteBuffer(((width*height*3)/nbThreads)+1);
			}
			for(int i=0;i<nbThreads;++i){
				tmp = link.take();
				int index = tmp.get();
				bufferListe[index].put(tmp);
				bufferListe[index].rewind();
			}
			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*3);
			for(int i=0;i<height;++i){
				for(int j=0;j<width/nbThreads;++j){
					for(int k=0;k<nbThreads;++k){
						buffer.put(bufferListe[k].get());
						buffer.put(bufferListe[k].get());
						buffer.put(bufferListe[k].get());
					}
				}
			}
			buffer.flip();
			TextureManager.loadTexture(buffer);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		long tempsFin = System.currentTimeMillis();
		if(verbose)
			System.out.println("Fractale Julia calculée en: "+ Float.toString((tempsFin - tempsDebut) / 1000F) + " secondes.");
	}

	/**
	 * Calcul de la fractale Buddhabrot
	 */
	public static void buddhabrot(){
		long tempsDebut = System.currentTimeMillis();
		ByteBuffer buffer = BufferUtils.createByteBuffer(DisplayManager.getWidth()*DisplayManager.getHeight()*3);
		int[][] iterTable_3 = boudhabrotIter_max((int) iter_max);
		int max_iter_3 	= 0;

		for(int j=0;j<Display.getHeight();j++){
			for(int i=0;i<Display.getWidth();i++){
				max_iter_3 	= Math.max(max_iter_3, iterTable_3[i][j]);
			}
		}

		for(int j=0;j<Display.getHeight();j++){
			for(int i=0;i<Display.getWidth();i++){
				
				int r = 0;
				int g = 0;
				int b = 0;

				r=g=b=iterTable_3[i][j]*256/max_iter_3;
				
				buffer.put((byte) ( r*2));
				buffer.put((byte) ( g*4));
				buffer.put((byte) ( b*5));
			}
		}
		buffer.flip();
		TextureManager.loadTexture(buffer);
		long tempsFin = System.currentTimeMillis();
		if(verbose)
			System.out.println("Fractale Buddhabrot calculée en: "+ Float.toString((tempsFin - tempsDebut) / 1000F) + " secondes.");

	}

	/**
	 * Calcul de la fractale Buddhabrot pour un iter max donne
	 */
	public static int[][] boudhabrotIter_max(int iter_m){
		int width = DisplayManager.getWidth();
		int height = DisplayManager.getHeight();
		LinkedBlockingQueue<ByteBuffer> link = new LinkedBlockingQueue<ByteBuffer>();

		int[][] table = new int [DisplayManager.getWidth()][DisplayManager.getHeight()];

		try {
			Vector<Thread> threads = new Vector<Thread>();
			int idThread=0;
			for(int i=0;i<nbThreads;++i){
				idThread++;
				threads.add(new Thread(new Buddhabrot(link, width, height,iter_m,x_min,x_max,y_min,y_max,nbThreads,idThread)));
			}
			for(Thread t: threads){
				t.start();
			}

			ByteBuffer tmp = BufferUtils.createByteBuffer(((width*height*3))+1);
			ByteBuffer [] bufferListe = new ByteBuffer[nbThreads];
			for(int i=0;i<nbThreads;++i){
				bufferListe[i] = BufferUtils.createByteBuffer(((width*height*3))+1);
			}

			for(int i=0;i<nbThreads;++i){

				tmp = link.take();
				int index = tmp.get();

				bufferListe[index].put(tmp);

				bufferListe[index].rewind();

			}



			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					int iter = 0;
					for(int k=0;k<nbThreads;k++){
						//bufferListe[k].get(i*3+j*Display.getWidth()*3+0+idThread)
						iter += bufferListe[k].get(j*3+i*Display.getWidth()*3+0+1) + bufferListe[k].get(j*3+i*Display.getWidth()*3+1+1)*255 
								+ bufferListe[k].get(j*3+i*Display.getWidth()*3+2+1)*255*255;
					}
					table[j][i] = iter;
				}
			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}

		return table;
	}


	/**
	 * Zoom en fonction de la position de la souris
	 * @param mouseX Position de la souris sur X
	 * @param mouseY Position de la souris sur Y
	 */
	public static void zoomIn(double mouseX, double mouseY){
		double _x = getCoord_X(mouseX);
		double _y = getCoord_Y(mouseY);

		zoom /= zoom_step;
		x_max=(_x+zoom);
		x_min=(_x-zoom);
		y_max=(_y+zoom*DisplayManager.getHeight()/DisplayManager.getWidth());
		y_min=(_y-zoom*DisplayManager.getHeight()/DisplayManager.getWidth());

	}
	/**
	 * Zoom en fonction de coordonnes (pour la video)
	 * @param _x Position sur X
	 * @param _y Position sur Y
	 * @param step Pas du zoom
	 */
	public static void zoomInVideo(double _x, double _y,double step){
		zoom /= step;
		x_max=(_x+zoom);
		x_min=(_x-zoom);
		y_max=(_y+zoom*DisplayManager.getHeight()/DisplayManager.getWidth());
		y_min=(_y-zoom*DisplayManager.getHeight()/DisplayManager.getWidth());

	}
	/**
	 * De-zoom en fonction de la position de la souris
	 * @param mouseX Position de la souris sur X
	 * @param mouseY Position de la souris sur Y
	 */
	public static void zoomOut(double mouseX, double mouseY){
		double _x = getCoord_X(mouseX);
		double _y = getCoord_Y(mouseY);

		zoom *= zoom_step;
		x_max=(_x+zoom);
		x_min=(_x-zoom);
		y_max=(_y+zoom*DisplayManager.getHeight()/DisplayManager.getWidth());
		y_min=(_y-zoom*DisplayManager.getHeight()/DisplayManager.getWidth());

	}
	/**
	 * Zoom en fonction de coordonnes
	 * @param x Position sur X
	 * @param y Position sur Y
	 * @param ecart Precision du zoom
	 */
	public static void zoomAt(double x, double y, double ecart){
		double ecart_y = ecart *DisplayManager.getHeight()/DisplayManager.getWidth();

		x_max=x+ecart/2.;
		x_min=x-ecart/2.;
		y_max=y+ecart_y/2.;
		y_min=y-ecart_y/2.;

		zoom = ecart/zoom_step;
	}
	/**
	 * @return the zoom step
	 */
	public static double getZoom_step() {
		return zoom_step;
	}
	/**
	 * Mise a jour des valeurs pour les calculs
	 * @param zoom_step Pas du zoom
	 * @param iter_max Iteration Maximale
	 * @param c_re Valeur Reelle de Julia
	 * @param c_im Valeur Imaginaire de Julia
	 */
	public static void setAll(double zoom_step,double iter_max,double c_re,double c_im){
		Fractal.zoom_step = zoom_step;
		Fractal.iter_max = iter_max;
		Fractal.c_re = c_re;
		Fractal.c_im = c_im;
	}
	/**
	 * Transforme les coordonnes de la souris en position
	 * @param mouseX Coordonne sur X
	 * @return Position sur X
	 */
	public static double getCoord_X(double mouseX){
		double x = x_min+((x_max-x_min)/DisplayManager.getWidth())*mouseX;
		return x;
	}
	/**
	 * Transforme les coordonnes de la souris en position
	 * @param mouseY Coordonne sur Y
	 * @return Position sur Y
	 */
	public static double getCoord_Y(double mouseY){
		double y = y_min+((y_max-y_min)/DisplayManager.getHeight())*mouseY;
		return y;
	}
	/**
	 * @return x_max de la fenetre
	 */
	public static double getX_max() {
		return x_max;
	}
	/**
	 * @return x_min de la fenetre
	 */
	public static double getX_min() {
		return x_min;
	}
	/**
	 * @return y_max de la fenetre
	 */
	public static double getY_max() {
		return y_max;
	}
	/**
	 * @return y_min de la fenetre
	 */
	public static double getY_min() {
		return y_min;
	}
	/**
	 * @return Iteration maximale
	 */
	public static double getIter_max() {
		return iter_max;
	}
	/**
	 * @return Zoom actuel
	 */
	public static double getZoom() {
		return zoom;
	}
	/**
	 * @return la coordonn�e r�el pour julia
	 */
	public static double getC_re() {
		return c_re;
	}
	/**
	 * @return la coordonn�e imaginaire pour julia
	 */
	public static double getC_im() {
		return c_im;
	}

	/**
	 * @param d Iteration maxiale to set
	 */
	public static void setIter_max(double d) {
		Fractal.iter_max = d;
	}
	/**
	 * Affiche la Julia correspondant au position de la souris
	 * @param mouseX Position sur X
	 * @param mouseY Position sur Y
	 */
	public static void setJulia(int mouseX, int mouseY) {
		c_re = getCoord_X(mouseX);
		c_im = getCoord_Y(mouseY);

		Controller.juliaUpdateGUI(c_re, c_im);
	}
	/**
	 * Parametre la fenetre en fonction de la fractale
	 * @param state Type de fractale
	 */
	public static void setDefaultCoord(State state){
		switch(state){
		case Mandelbrot:
			zoomAt(-0.75, 0, 2.7);//*4);
			break;
		case Julia:
			zoomAt(0, 0, 3);
			break;
		case Boudhabrot:
			zoomAt(-0.75, 0, 2.7);
			break;
		default:
			break;
		}
	}
	/**
	 * @param nbthreads Nombre de threads to set
	 */
	public static void setNbThreads(int nbthreads) {
		Fractal.nbThreads = nbthreads;
	}
}
