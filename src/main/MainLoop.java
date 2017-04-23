package main;

import org.lwjgl.opengl.Display;

import reglages.Main_Window;
import textureManager.TextureManager;
import view.DisplayManager;
import controller.Controller;
import controller.Controller_GUI;
import fractal.Coloration;

/**
 * Lancement du programme
 * @author abrunel
 * @author gtrauchessec
 * @author alaurent
 *
 */
public class MainLoop {
	/**
	 * Main Principal
	 * @param args Arguments du main
	 */
	public static void main(String[] args) {
		
		System.out.println("Start");
		
		Main_Window fenetre = new Main_Window();
		Controller_GUI.loadFile();
		Controller_GUI.setWindow(fenetre);
		
		Coloration.InitColorSystem();
		Coloration.setMode(Controller_GUI.getCouleur());
		
		DisplayManager.createDisplay2D(Controller_GUI.getResolution());
		Controller controler = new Controller();
		
		Controller_GUI.updateGraphic();
		
		while(!Display.isCloseRequested()){
			controler.update();
			DisplayManager.updateDisplay();
		}
		
		System.out.println("Close");
		Controller_GUI.saveFile();
		
		Controller_GUI.setQuit(true);
		controler.cleanUp();
		TextureManager.cleanUp();
		DisplayManager.closeDisplay();
	}
}
