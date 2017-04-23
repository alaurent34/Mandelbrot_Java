package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import reglages.Main_Window;
import utils.Quadruple;
import utils.State;
import utils.Tuple;
/**
 * 
 * @author gtrauchessec
 * 
 * Controller gerant les actions a effectues sur la fenetre de reglages
 *
 */
public class Controller_GUI{
	private static double zoom_step		= 1.0;
	private static double iter_max		= 1024;
	private static double c_re			= 0.0;
	private static double c_im			= 0.0;
	private static State state 			= State.Mandelbrot;
	private static int couleur 			= 0;
	private static int resolution		= 0;
	public static double zoom_coord_x	= 0.0;
	public static double zoom_coord_y	= 0.0;
	public static double precision		= 1.0;
	public static int nbThreads			= 4;
	public static boolean gpuOn			= false;
	public static boolean antibb		= false;
	public static boolean approx			= false;

	public static String gradient_path  = "gradient.png";

	private static Vector<Tuple<String,Double,Double>> csaved = new Vector<Tuple<String,Double,Double>>();
	private static Vector<Quadruple<String,Double,Double,Double>> zoomsaved = new Vector<Quadruple<String,Double,Double,Double>>();
	private static boolean verbose = false;
	private static Main_Window window;
	/**
	 * Constructeur
	 */
	public Controller_GUI(){
	}
	/**
	 * Definit la fenetre de reglages associee
	 * @param w Fentre de reglage
	 */
	public static void setWindow(Main_Window w){
		window = w;
		window.updateValues();
	}
	/**
	 * Attribution manuel des attributs
	 * @param zoom Zoom Step
	 * @param iter_max Iteration Maximales
	 * @param c_re Valeur Reelle Julia
	 * @param c_im Valeur Imaginaire Julia
	 * @param stat Type de Fractale
	 * @param couleur Index de couleur
	 * @param resolution Index de resolution
	 * @param zoom_coord_x Posiotn du zoom sur x
	 * @param zoom_coord_y position du zoom sur y
	 * @param precision precision du zoom
	 */
	public static void init(double zoom,double iter_max,double c_re,double c_im,State stat,int couleur,int resolution,double zoom_coord_x,double zoom_coord_y,double precision){
		Controller_GUI.zoom_step = zoom;
		Controller_GUI.iter_max = iter_max;
		Controller_GUI.c_re = c_re;
		Controller_GUI.c_im = c_im;
		Controller_GUI.state = stat;
		Controller_GUI.couleur = couleur;
		Controller_GUI.resolution = resolution;
		Controller_GUI.zoom_coord_x = zoom_coord_x;
		Controller_GUI.zoom_coord_y = zoom_coord_y;
		Controller_GUI.precision = precision;
	}


	/**
	 * Donne l'index du nombre de threads
	 * @return L'index du nombre de threads
	 */
	public static int getNbThreadsIndex(){

		switch (nbThreads) {
		case 4:
			return 1;
		case 8:
			return 2;
		default:
			return 0;
		}
	}
	/**
	 * Definit le nombre de threads en fonction de l'index 
	 * @param nb Index du nombre de thread
	 */
	public static void setNbThreadsIndex(int nb){
		switch (nb) {
		case 1:
			nbThreads = 4;
			break;
		case 2:
			nbThreads = 8;
			break;
		default:
			nbThreads = 1;
			break;
		}
	}
	/**
	 * Force une capture d'ecran
	 */
	public static void screenshot(){
		if(verbose)
			System.out.println("Take a screenshot");
		Controller.TakeScreenshot();
	}
	/**
	 * Force un zoom
	 */
	public static void makeZoom(){
		if(verbose)
			System.out.println("Zoom ["+zoom_coord_x+"+i"+zoom_coord_y+"] with precision="+precision);
		Controller.zoomAt(Controller_GUI.zoom_coord_x, Controller_GUI.zoom_coord_y, Controller_GUI.precision);
	}
	/**
	 * Mise a jour de l'affichage
	 */
	public static void updateGraphic(){
		if(verbose)
			System.out.println("Graphic update");
		Controller.updateGraphic(state,zoom_step,iter_max,couleur,resolution,c_re,c_im,nbThreads);
	}
	/**
	 * Mise a jour de la fenetre de reglage
	 */
	public static void updateSwing(){
		if(verbose)
			System.out.println("Swing update");
		window.updateValues();
	}
	/**
	 * Reinitialise le zoom de l'affichage
	 */
	public static void resetZoom(){
		if(verbose)
			System.out.println("Reset Zoom");
		Controller.resetZoom();
	}
	/**
	 * Quit la fenetre de reglages
	 * @param quit Vrai pour quitter
	 */
	public static void setQuit(boolean quit) {
		window.quit();

		if(verbose)
			System.out.println("Set Quit to "+quit);
	}

	/**
	 * Ajoute une fractale de Julia a sauvegarder
	 * @param name Nom de la fractale
	 * @param re Valeur Reelle
	 * @param im Valeur imaginaire
	 */
	public static void addCSaved(String name,double re,double im){
		csaved.add(new Tuple<String,Double,Double>(name,re,im));
	}
	/**
	 * Supprime une fractale de Julia enregistree
	 * @param index Index de la fractale a supprimer
	 */
	public static void rmCSaved(int index){
		csaved.remove(index);
	}
	/**
	 * Ajoute un zoom a sauvegarder
	 * @param name Nom du zoom
	 * @param x Position sur x
	 * @param y Position sur y
	 * @param p Largeur du zoom
	 */
	public static void addZoomSaved(String name,double x,double y,double p){
		zoomsaved.add(new Quadruple<String,Double,Double,Double>(name,x,y,p));
	}
	/**
	 * Supprime un zoom sauvegarde
	 * @param index Index du zoom
	 */
	public static void rmZoomSaved(int index){
		zoomsaved.remove(index);
	}
	/**
	 * Force la visibilite de la fenetre de reglages
	 * @param v Vrai pour visible
	 */
	public static void setVisible(Boolean v)
	{
		window.setVisible(v);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                         Fichier de sauvegarde                                             //
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Lecture du fichier de sauvegarde
	 */
	public static void loadFile() {

		BufferedReader br;
		FileReader fr = null;

		try {
			fr = new FileReader("data.save");
		} catch (FileNotFoundException exception) {
			System.out.println("Fichier Erreur ouverture:"  + exception.getMessage());
			Controller_GUI.init(2.0, 1024, 0.0, 0.0, State.Mandelbrot, 1, 2,0.0,0.0,1.0);

			csaved.add(new Tuple<String,Double,Double>("Lapin de Douady",0.123,0.745));
			csaved.add(new Tuple<String,Double,Double>("Zède",0.285,0.013));
			csaved.add(new Tuple<String,Double,Double>("Pulsation",-1.417022285618,0.0));
			csaved.add(new Tuple<String,Double,Double>("Disque de Siegel",0.3905408702184,-0.58678790734696873));
			csaved.add(new Tuple<String,Double,Double>("Wave",-0.772691322542185,0.124281466072787));

			zoomsaved.add(new Quadruple<String, Double, Double, Double>("Myrberg-Feigenbaum", -1.401155, 0.0, 0.1));
			zoomsaved.add(new Quadruple<String, Double, Double, Double>("Vallée hypocampes", -0.75, 0.0, 0.3));
			zoomsaved.add(new Quadruple<String, Double, Double, Double>("Vallée élephants", 0.25, 0.0, 0.2));
			zoomsaved.add(new Quadruple<String, Double, Double, Double>("Misiurewicz", -0.77568377, 0.13646737, 0.05));
			zoomsaved.add(new Quadruple<String, Double, Double, Double>("Beau", -1.2546858591796248, 0.38188469951025267, 3.586137120592525E-8));

			return;
		}
		br = new BufferedReader(fr);

		String line;
		try {

			Controller_GUI.zoom_step 		= Double.parseDouble(br.readLine());
			Controller_GUI.iter_max 	= Double.parseDouble(br.readLine());
			Controller_GUI.c_re 		= Double.parseDouble(br.readLine());
			Controller_GUI.c_im 		= Double.parseDouble(br.readLine());

			line = br.readLine();
			if(line.equals("Mandelbrot"))
				state = State.Mandelbrot;
			else if(line.equals("Julia"))
				state = State.Julia;
			else
				state = State.Boudhabrot;

			Controller_GUI.couleur 		= Integer.parseInt(br.readLine());
			Controller_GUI.resolution 	= Integer.parseInt(br.readLine());
			Controller_GUI.gradient_path= br.readLine();
			Controller_GUI.nbThreads 	= Integer.parseInt(br.readLine());
			Controller_GUI.gpuOn 		= Boolean.parseBoolean(br.readLine());
			Controller_GUI.antibb 		= Boolean.parseBoolean(br.readLine());
			Controller_GUI.approx 		= Boolean.parseBoolean(br.readLine());

			int size;
			size = Integer.parseInt(br.readLine());
			for(int i=0;i<size ;i++){
				csaved.add(StringToTuple(br.readLine()));
			}

			size = Integer.parseInt(br.readLine());
			for(int i=0;i<size ;i++){
				zoomsaved.add(StringToQuadruple(br.readLine()));
			}

			br.close();

		} catch (IOException exception) {
			System.out.println("Fichier Erreur lecture:"  + exception.getMessage());
			return;
		}
	}

	/**
	 * Transformation d'un string en tuple
	 * @param readLine String a transformer
	 * @return Tuple genere 
	 */
	private static Tuple<String, Double, Double> StringToTuple(String readLine) {

		StringTokenizer strtok = new StringTokenizer(readLine,"~");
		Tuple<String, Double, Double> t = new Tuple<String, Double, Double>(strtok.nextToken().substring(1),
				Double.parseDouble(strtok.nextToken()),
				Double.parseDouble(strtok.nextToken()));
		return t;
	}
	/**
	 * Transformation d'un string en quadruple
	 * @param readLine String a transformer
	 * @return Quadruple genere 
	 */
	private static Quadruple<String, Double, Double, Double> StringToQuadruple(String readLine) {

		StringTokenizer strtok = new StringTokenizer(readLine,"~");
		Quadruple<String, Double, Double, Double> q = new Quadruple<String, Double, Double, Double>(strtok.nextToken().substring(1),
				Double.parseDouble(strtok.nextToken()),
				Double.parseDouble(strtok.nextToken()),
				Double.parseDouble(strtok.nextToken()));

		return q;
	}
	/**
	 * Sauvegarde de l'état actuel
	 */
	public static void saveFile(){
		File file = new File ("data.save");
		String c = "\n";

		String buffer = "";

		buffer += zoom_step + c + iter_max + c + c_re + c + c_im + c + state + c + couleur + c + resolution + c + gradient_path + c + nbThreads + c + gpuOn + c + antibb + c + approx;

		buffer += c + csaved.size() + c;
		for(int i =0;i<csaved.size();i++){
			buffer += " " + csaved.get(i).getFirst() + "~" + csaved.get(i).getSecond() + "~" + csaved.get(i).getThird() + c;
		}

		buffer += zoomsaved.size() + c;
		for(int i =0;i<zoomsaved.size();i++){
			buffer += " " + zoomsaved.get(i).getFirst() + "~" + zoomsaved.get(i).getSecond() + "~" + zoomsaved.get(i).getThird() + "~" + zoomsaved.get(i).getFourth() + c;
		}

		try
		{
			FileWriter fw = new FileWriter (file);

			fw.write (buffer);

			fw.close();
		}
		catch (IOException exception)
		{
			System.out.println ("Fichier Erreur Ecriture : " + exception.getMessage());
			return;
		}
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                           Getters and Setters                                             //
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * @return the zoom_step
	 */
	public static double getZoom() {
		return zoom_step;
	}

	/**
	 * @param zoom_step the zoom_step to set
	 */
	public static void setZoom(double zoom_step) {
		Controller_GUI.zoom_step = zoom_step;
	}

	/**
	 * @return the iter_max
	 */
	public static double getIter_max() {
		return iter_max;
	}

	/**
	 * @param iter_max the iter_max to set
	 */
	public static void setIter_max(double iter_max) {
		Controller_GUI.iter_max = iter_max;
	}

	/**
	 * @return the c_re
	 */
	public static double getC_re() {
		return c_re;
	}

	/**
	 * @param c_re the c_re to set
	 */
	public static void setC_re(double c_re) {
		Controller_GUI.c_re = c_re;
	}

	/**
	 * @return the c_im
	 */
	public static double getC_im() {
		return c_im;
	}

	/**
	 * @param c_im the c_im to set
	 */
	public static void setC_im(double c_im) {
		Controller_GUI.c_im = c_im;
	}

	/**
	 * @return the state
	 */
	public static State getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public static void setState(State state) {
		Controller_GUI.state = state;
	}

	/**
	 * @return the couleur
	 */
	public static int getCouleur() {
		return couleur;
	}

	/**
	 * @param couleur the couleur to set
	 */
	public static void setCouleur(int couleur) {
		Controller_GUI.couleur = couleur;
	}

	/**
	 * @return the resolution
	 */
	public static int getResolution() {
		return resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public static void setResolution(int resolution) {
		Controller_GUI.resolution = resolution;
	}

	/**
	 * @return the zoom_coord_x
	 */
	public static double getZoom_coord_x() {
		return zoom_coord_x;
	}

	/**
	 * @param zoom_coord_x the zoom_coord_x to set
	 */
	public static void setZoom_coord_x(double zoom_coord_x) {
		Controller_GUI.zoom_coord_x = zoom_coord_x;
	}

	/**
	 * @return the zoom_coord_y
	 */
	public static double getZoom_coord_y() {
		return zoom_coord_y;
	}

	/**
	 * @param zoom_coord_y the zoom_coord_y to set
	 */
	public static void setZoom_coord_y(double zoom_coord_y) {
		Controller_GUI.zoom_coord_y = zoom_coord_y;
	}

	/**
	 * @return the precision
	 */
	public static double getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public static void setPrecision(double precision) {
		Controller_GUI.precision = precision;
	}

	/**
	 * @return the shadersOn
	 */
	public static boolean isGPUOn() {
		return gpuOn;
	}

	/**
	 * @param shadersOn the shadersOn to set
	 */
	public static void setGPUOn(boolean shadersOn) {
		Controller_GUI.gpuOn = shadersOn;
	}

	/**
	 * @return the gradient_path
	 */
	public static String getGradient_path() {
		return gradient_path;
	}

	/**
	 * @param gradient_path the gradient_path to set
	 */
	public static void setGradient_path(String gradient_path) {
		Controller_GUI.gradient_path = gradient_path;
	}

	/**
	 * @return the csaved
	 */
	public static Vector<Tuple<String, Double, Double>> getCSaved() {
		return csaved;
	}

	/**
	 * @return the zoomsaved
	 */
	public static Vector<Quadruple<String, Double, Double, Double>> getZoomsaved() {
		return zoomsaved;
	}

	/**
	 * @return the nbThreads
	 */
	public static int getNbThreads(){
		return nbThreads;
	}

	/**
	 * @param nbThreads the nbThreads to set
	 */
	public static void setNbThreads(int nbThreads){
		Controller_GUI.nbThreads = nbThreads;
	}
	/**
	 * @return the antibb
	 */
	public static boolean isAntibb() {
		return antibb;
	}
	/**
	 * @param antibb the antibb to set
	 */
	public static void setAntibb(boolean antibb) {
		Controller_GUI.antibb = antibb;
	}


	public static void setGPUEnable(boolean b){
		window.setGPUEnable(b);
	}
	/**
	 * @return the approx
	 */
	public static boolean isApprox() {
		return approx;
	}
	/**
	 * @param approx the approx to set
	 */
	public static void setApprox(boolean approx) {
		Controller_GUI.approx = approx;
	}



}