package thread;


import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.BufferUtils;
import utils.Pair;
import view.DisplayManager;
import controller.Controller_GUI;
import fractal.Coloration;
import fractal.Fractal;
/**
 * Gestion des threads
 * @author abrunel
 * @author gtrauchessec
 *
 */
public class ThreadFractal{

	private int width;
	private int height;
	private double iter_max;
	private double x_min;
	private double x_max;
	private double y_min;
	private double y_max;
	private int nbThreads;
	private int idThread;

	private LinkedBlockingQueue<ByteBuffer> link;
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
	public ThreadFractal(LinkedBlockingQueue<ByteBuffer> link, int width, int height, double iter_max
			,double x_min, double x_max, double y_min, double y_max,int nbThreads,int idThread) {
		super();
		this.width = width;
		this.height = height;
		this.iter_max = iter_max;
		this.x_min = x_min;
		this.x_max = x_max;
		this.y_min = y_min;
		this.y_max = y_max;
		this.nbThreads = nbThreads;
		this.idThread = idThread;

		//setAxes();
		this.link = link;
	}

	/**
	 * Calculs de Mandelbrot
	 */
	public void mandlebrotCalcul(){
		ByteBuffer buffer;
		buffer = BufferUtils.createByteBuffer(width*height*3/nbThreads+1);
		buffer.put((byte)(idThread-1));
		double zoom_x = (double) (width/(x_max-x_min));
		double zoom_y = (double) (height/(y_max-y_min));

		for(int i=0;i<height;++i){
			for(int j=idThread-1;j<width;j+=nbThreads){
				double iter=0;

				double c_re = (double) j/zoom_x+x_min;
				double c_im = (double) i/zoom_y+y_min;

				double x = 0;
				double y = 0;

				double tmpX,tmpY;

				while ( x*x + y*y < 2*2  &&  iter < iter_max )
				{
					tmpX = x*x - y*y + c_re;
					//tmpX = x*x*x - 3*x*y*y+c_re;
					tmpY = 2*x*y + c_im;
					//tmpY = 3*x*x*y - y*y*y + c_im;
					if (x == tmpX  &&  y == tmpY)
					{
						iter = iter_max;
						break;
					}
					x = tmpX;
					y = tmpY;
					iter++;
				}

				int r,g,b;

				r = Coloration.getRed(iter,iter_max,x,y,c_re,c_im);
				g = Coloration.getGreen(iter,iter_max,x,y,c_re,c_im);
				b = Coloration.getBlue(iter,iter_max,x,y,c_re,c_im);
				buffer.put((byte)r);
				buffer.put((byte)g);
				buffer.put((byte)b);

			}
		}
		buffer.flip();

		link.add(buffer);
	}

	/**
	 * Calculs de Julia
	 * @param c_re Valeur reelle Julia
	 * @param c_im Valeur imaginaire Julia
	 */
	public void juliaCalcul(double c_re,double c_im){
		ByteBuffer buffer;
		buffer = BufferUtils.createByteBuffer(width*height*3/nbThreads+1);
		buffer.put((byte)(idThread-1));
		double zoom_x = (double) (width/(x_max-x_min));
		double zoom_y = (double) (height/(y_max-y_min));

		for(int i=0;i<height;++i){
			for(int j=idThread-1;j<width;j+=nbThreads){
				double iter=0;

				double x = (double) j/zoom_x+x_min;
				double y = (double) i/zoom_y+y_min;

				double tmpX,tmpY;

				while ( x*x + y*y < 2*2  &&  iter < iter_max )
				{
					tmpX = x*x - y*y + c_re;
					tmpY = 2*x*y + c_im;

					if (x == tmpX  &&  y == tmpY)
					{
						iter = iter_max;
						break;
					}
					x = tmpX;
					y = tmpY;
					iter++;
				}
				int r,g,b;

				r = Coloration.getRed(iter,iter_max,x,y,c_re,c_im);
				g = Coloration.getGreen(iter,iter_max,x,y,c_re,c_im);
				b = Coloration.getBlue(iter,iter_max,x,y,c_re,c_im);
				buffer.put((byte)r);
				buffer.put((byte)g);
				buffer.put((byte)b);
			}
		}
		buffer.flip();
		link.add(buffer);
	}
	/**
	 * Calculs de Buddhabrot
	 */
	public void buddhabrotCalcul(){
		ByteBuffer buffer;
		buffer = BufferUtils.createByteBuffer(width*height*3+1);
		buffer.put((byte)(idThread-1));
		double zoom_x = (double) (width/(x_max-x_min));
		double zoom_y = (double) (height/(y_max-y_min));

		int[][] table = new int[DisplayManager.getWidth()][DisplayManager.getHeight()];

		Vector<Pair<Double,Double>> tmp_list = new Vector<Pair<Double,Double>>();
		double iter_min = iter_max*0.1;
		for(int i=0;i<height;++i){
			for(int j=idThread-1;j<width;j+=nbThreads){
				double iter=0;

				double c_re = (double) j/zoom_x+x_min;
				double c_im = (double) i/zoom_y+y_min;

				double x = 0;
				double y = 0;

				double tmpX,tmpY;

				tmp_list.clear();

				while ( x*x + y*y < 4  &&  iter < iter_max )
				{
					tmpX = x*x - y*y + c_re;
					tmpY = 2*x*y + c_im;

					x = tmpX;
					y = tmpY;
					iter++;
					tmp_list.add(new Pair<Double,Double>(x,y));
				}

				if( (!Controller_GUI.isAntibb() && iter<iter_max || Controller_GUI.isAntibb() && iter==iter_max) && iter>= iter_min){

					for(Pair<Double, Double> p : tmp_list){

						double x_coord = (double) p.getFirst();
						double y_coord = (double) p.getSecond();

						double x_min_abs = Fractal.getX_min();
						double y_min_abs = Fractal.getY_min();

						int x_window = (int) ((x_coord - x_min_abs)*zoom_x);
						int y_window = (int) ((y_coord - y_min_abs)*zoom_y);
						if(x_window < DisplayManager.getWidth() && y_window < DisplayManager.getHeight() && x_window >= 0 && y_window >= 0){
							table[x_window][y_window]++;
						}
					}
				}
			}
		}

		for(int i=0;i<DisplayManager.getHeight();i++){
			for(int j=0;j<DisplayManager.getWidth();j++){
				buffer.put((byte)(table[j][i]/(255*255)));
				buffer.put((byte)((table[j][i]%(255*255))/255));
				buffer.put((byte)(table[j][i]%255));
			}
		}
		buffer.flip();
		link.add(buffer);

	}
}
