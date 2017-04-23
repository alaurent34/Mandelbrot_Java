package controller;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import textureManager.TextureManager;
import utils.State;
import view.DisplayManager;
import view.Render;
import fractal.Coloration;
import fractal.Fractal;
import gpgpu.OpenclProgram;

/**
 * 
 * @author gtrauchessec
 * @author alaurent
 * @author abrunel
 * 
 * Controller gerant les actions a effectuer sur l'affichage
 *
 */
public class Controller {

	private static boolean isLoad=false;
	private static boolean loading = false;
	private Render renderer;
	private static State state;
	private static boolean takeScreenshot = false;
	private OpenclProgram clProgram= null;
	private static boolean isZoomBox = false;
	private  boolean stopZoomBox = false;
	private double zoomBoxStart_x = 0,zoomBoxStart_y = 0;

	public int videoCompteur = 0;
	/**
	 * Constructeur
	 */
	public Controller() {
		super();
		//shader = new StaticShader();
		this.renderer = new Render();
		Controller.state = State.Mandelbrot;
		Fractal.setDefaultCoord(state);
		try {
			clProgram = new OpenclProgram();
		} catch (Exception e) {
			Controller_GUI.setGPUOn(false);
			Controller_GUI.setGPUEnable(false);

			Controller_GUI.updateSwing();

			System.out.println("Unable to use GPGPU.");			
		}

	}
	/**
	 * Prendre une capture d ecran
	 */
	public static void TakeScreenshot(){
		if(!loading)
			takeScreenshot = true;
	}
	/**
	 * Reinitialiser le zoom sur la fractale actuelle
	 */
	public static void resetZoom(){
		Fractal.setDefaultCoord(state);
		isLoad = false;
	}
	/**
	 * Zoom sur une position
	 * @param x Position sur x
	 * @param y Position sur y
	 * @param precision Largeur du zoom
	 */
	public static void zoomAt(double x, double y, double precision){
		if(Controller.state != State.Boudhabrot){
			Fractal.zoomAt(x, y, precision);
			isLoad = false;
		}
	}
	/**
	 * Mise a jour la fenetre de reglage pour la julia en cours (pour la sauvegarde)
	 * @param c_re Valeur Reelle 
	 * @param c_im Valeur imaginaire
	 */
	public static void juliaUpdateGUI(double c_re,double c_im){
		Controller_GUI.setC_im(c_im);
		Controller_GUI.setC_re(c_re);
		Controller_GUI.setState(state);
		Controller_GUI.updateSwing();
	}

	/**
	 * Mise a jour la fenetre de reglage pour le zoom en cours (pour la sauvegarde)
	 * @param x Zoom actuel sur x
	 * @param y Zoom actuel sur y
	 * @param precision Precision actuelle
	 */
	public static void zoomUpdateGUI(double x, double y, double precision){

		Controller_GUI.setZoom_coord_x(x);
		Controller_GUI.setZoom_coord_y(y);
		Controller_GUI.setPrecision(precision);
		Controller_GUI.updateSwing();
	}
	/**
	 * Mise a jour de l'affichage
	 * @param state Style de fractale
	 * @param zoom Zoom
	 * @param iter_max Iteration maximale
	 * @param couleur Index du mode de coloration
	 * @param resolution Index de resolution
	 * @param c_re Valeur Reelle de Julia
	 * @param c_im Valeur Imaginaire de Julia
	 * @param nbthreads Nombre de Thread
	 */
	public static void updateGraphic(State state, double zoom, double iter_max, int couleur, int resolution, double c_re, double c_im,int nbthreads){
		isLoad = false;
		if(Controller.state != state){
			Fractal.setDefaultCoord(state);
		}
		Controller.state = state;
		Fractal.setAll(zoom, iter_max, c_re, c_im);
		Coloration.setMode(couleur);
		Fractal.setNbThreads(nbthreads);
	}
	/**
	 * Gestion des evenements
	 */
	public void events(){
		InputListener.eventsListener();

		if(InputListener.isMouseLeftButtonPressed()){
			if(!isZoomBox && !stopZoomBox){
				isZoomBox = true;
				zoomBoxStart_x = InputListener.getMouseX();
				zoomBoxStart_y = InputListener.getMouseY();
			}
		}
		if(InputListener.isMouseLeftButtonClicked()){
			stopZoomBox = false;
			if(isZoomBox){
				double x1 = Fractal.getCoord_X(zoomBoxStart_x);
				double y1 = Fractal.getCoord_Y(zoomBoxStart_y);

				double x2 = Fractal.getCoord_X(InputListener.getMouseX());
				double y2 = Fractal.getCoord_Y(InputListener.getMouseY());

				double x_mid = (x1/2.0 + x2/2.0);
				double y_mid = (y1/2.0 + y2/2.0);

				double preci = Math.max(Math.abs(x1-x2), Math.abs(y1-y2)* Display.getWidth()/Display.getHeight());

				if(preci > Fractal.getZoom()/100.){
					zoomAt(x_mid, y_mid, preci);
					isZoomBox = false;

					zoomUpdateGUI((Fractal.getX_min()/2.0 + Fractal.getX_max()/2.0), (Fractal.getY_min()/2.0 + Fractal.getY_max()/2.0), Math.abs(Fractal.getX_max()-Fractal.getX_min()));
				}
			}
		}
		if(InputListener.isMouseRightButtonClicked()){
			isZoomBox = false;
			stopZoomBox = true;
		}
		if(InputListener.isMouseWeelDown() && Controller.state!=State.Boudhabrot){
			Fractal.zoomOut(InputListener.getMouseX(), InputListener.getMouseY());
			zoomUpdateGUI((Fractal.getX_min()/2.0 + Fractal.getX_max()/2.0), (Fractal.getY_min()/2.0 + Fractal.getY_max()/2.0), Math.abs(Fractal.getX_max()-Fractal.getX_min()));
			isLoad=false;
		}
		if(InputListener.isMouseWeelUp() && Controller.state!=State.Boudhabrot){
			Fractal.zoomIn(InputListener.getMouseX(), InputListener.getMouseY());
			isLoad=false;
			zoomUpdateGUI((Fractal.getX_min()/2.0 + Fractal.getX_max()/2.0), (Fractal.getY_min()/2.0 + Fractal.getY_max()/2.0), Math.abs(Fractal.getX_max()-Fractal.getX_min()));
		}

		double d_x = 10. /100.*Math.abs(Fractal.getX_min()-Fractal.getX_max());	
		double d_y = 10. /100.*Math.abs(Fractal.getY_min()-Fractal.getY_max());

		if(InputListener.isKeyClicked(Keyboard.KEY_M)){
			if(state != State.Mandelbrot){
				isLoad = false;
				state = State.Mandelbrot;
				Controller_GUI.setState(state);
				Controller_GUI.updateSwing();
				Fractal.setDefaultCoord(state);
			}
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_J)){
			if(state != State.Julia){
				isLoad = false;
				state = State.Julia;
				Fractal.setJulia(InputListener.getMouseX(), InputListener.getMouseY());
				Fractal.setDefaultCoord(state);
			}
		}
		if(InputListener.isKeyPressed(Keyboard.KEY_L)){
			isLoad = false;
			state = State.Julia;
			Fractal.setJulia(InputListener.getMouseX(), InputListener.getMouseY());
			Fractal.setDefaultCoord(state);
		}
		if(InputListener.isKeyPressed(Keyboard.KEY_B)){
			if(state != State.Boudhabrot){
				isLoad = false;
				state = State.Boudhabrot;
				Controller_GUI.updateSwing();
				Fractal.setDefaultCoord(state);

			}
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_A)){
			TakeScreenshot();
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_R)){
			Controller_GUI.setVisible(true);
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_UP) && Controller.state!=State.Boudhabrot){
			Fractal.zoomAt((Fractal.getX_min()+Fractal.getX_max())/2., (Fractal.getY_min()+Fractal.getY_max())/2.+d_y, Fractal.getZoom()*Fractal.getZoom_step());
			isLoad = false;
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_DOWN) && Controller.state!=State.Boudhabrot){
			Fractal.zoomAt((Fractal.getX_min()+Fractal.getX_max())/2., (Fractal.getY_min()+Fractal.getY_max())/2.-d_y, Fractal.getZoom()*Fractal.getZoom_step());
			isLoad = false;
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_LEFT) && Controller.state!=State.Boudhabrot){
			Fractal.zoomAt((Fractal.getX_min()+Fractal.getX_max())/2.-d_x, (Fractal.getY_min()+Fractal.getY_max())/2., Fractal.getZoom()*Fractal.getZoom_step());
			isLoad = false;
		}
		if(InputListener.isKeyClicked(Keyboard.KEY_RIGHT) && Controller.state!=State.Boudhabrot){
			Fractal.zoomAt((Fractal.getX_min()+Fractal.getX_max())/2.+d_x, (Fractal.getY_min()+Fractal.getY_max())/2., Fractal.getZoom()*Fractal.getZoom_step());
			isLoad = false;
		}
	}
	/**
	 * Chargement de la fractale
	 * @param s Fractae a charger
	 */
	public void loadFractal(State s){
		switch(s){
		case Mandelbrot:
			Fractal.mandleBrot();
			isLoad=true;
			break;
		case Julia :
			Fractal.julia();
			isLoad=true;
			break;
		case Boudhabrot :
			Fractal.buddhabrot();
			isLoad=true;
			break;
		default:
			break;
		}
	}

	/**
	 * Chargement de la fractale en GPGPU
	 * @param s Fractae a charger
	 */
	public void loadFractalGPGPU(State s){
		switch(s){
		case Mandelbrot:
			clProgram.executeMandelbrot((int)DisplayManager.getWidth(), (int)DisplayManager.getHeight(), 3, (int)Fractal.getIter_max()
					, Fractal.getX_min(),Fractal.getX_max(), Fractal.getY_min(), Fractal.getY_max(), Coloration.getColorBuffer());
			isLoad=true;
			break;
		case Julia :
			clProgram.executeJulia((int)DisplayManager.getWidth(), (int)DisplayManager.getHeight(), 3, (int)Fractal.getIter_max()
					, Fractal.getX_min(), Fractal.getX_max(), Fractal.getY_min(),Fractal.getY_max(),
					Fractal.getC_re(),Fractal.getC_im(), Coloration.getColorBuffer());
			isLoad=true;
			break;
		default:
			break;
		}
	}

	/**
	 * Mise a jour de l'affichage
	 */
	public void update(){

		//zoomVideo();

		renderer.updateRender();

		events();

		if(!Controller_GUI.isGPUOn() || state==State.Boudhabrot)
		{
			if(loading){
				loadFractal(state);

				loading = false;
				isLoad=true;
			}
		}
		else{
			if(loading){
				loadFractalGPGPU(state);
				loading = false;
			}
		}

		if(takeScreenshot){
			takeScreenshot = false;
			TextureManager.exportCurrentTexture();
		}

		renderer.drawFractale();

		if(!isLoad){
			renderer.drawLoading(16,16,1.);
			loading = true;
		}

		if(isZoomBox){
			renderer.drawEmptyRectangle(zoomBoxStart_x, zoomBoxStart_y, InputListener.getMouseX(), InputListener.getMouseY(), 0.7, 0.7, 0.7, 0.5,2.0);
		}


	}

	//(10÷(7×10^−14))^(1÷(25×secondes)) = zoom_step

	@SuppressWarnings("unused")
	/**
	 * Fonction de creation de ~700 screenshots pour la creation ulterieur de la video de demonstration
	 */
	private void zoomVideo() {

		int iter_max = 2048;

		if(Fractal.getIter_max()<iter_max)
		{
			//TakeScreenshot();
			Fractal.setIter_max((Fractal.getIter_max()*Math.sqrt(Math.sqrt(Math.sqrt(2)))));
			isLoad = false;
		}
		else
		{
			double X = -1.2546858599168187;
			double Y = 0.3818846997335791;
			double max = 7.0*Math.pow(10, -14);
			double x = X,y = Y, nb = 32;

			if(videoCompteur < nb){
				x = videoCompteur*(1./nb) * (X+0.75) -0.75;
				y = videoCompteur*(1./nb) * Y ;

				x =  (Math.log(videoCompteur+1)/3.5)*(X+0.75) -0.75;
				y =  (Math.log(videoCompteur+1)/3.5)*Y;
			}

			Fractal.setIter_max(iter_max);
			if(Fractal.getZoom() >= max){
				//TakeScreenshot();
				Fractal.zoomInVideo(x, y, 1.04);
				isLoad = false;
				videoCompteur++;
			}
		}
	}
	/**
	 * Supprime le programme opencl
	 */
	public void cleanUp() {
		if(clProgram!=null)
			clProgram.destroyCL();
	}

}
