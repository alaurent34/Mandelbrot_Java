package controller;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * 
 * @author gtrauchessec
 * @author alaurent
 * @author abrunel
 * 
 * Lecture et stockage des evenements
 *
 */
public class InputListener {

	private static Map<Integer,Boolean> keyMapPressed = new HashMap<Integer, Boolean>();
	private static Map<Integer,Boolean> keyMapClicked = new HashMap<Integer, Boolean>();
	private static Map<Integer,Boolean> buttonMapClicked = new HashMap<Integer, Boolean>();
	/**
	 * Test sur l'activation d'une touche
	 * @param key Touche du clavier
	 * @return Vrai si la touche est enfonce
	 */
	public static boolean isKeyPressed(int key){
		if(!keyMapPressed.containsKey(key))
			return false;
		return keyMapPressed.get(key);
	}
	/**
	 * Test sur le clic d'une touche
	 * @param key Touche du clavier
	 * @return Vrai si la touche a ete cliquee
	 */
	public static boolean isKeyClicked(int key){
		if(!keyMapClicked.containsKey(key))
			return false;
		boolean a = keyMapClicked.get(key);
		keyMapClicked.put(key, false);
		return a;
	}
	/**
	 * Test sur le clic du bouton gauche de la souris
	 * @return Vrai si le bouton a ete clique
	 */
	public static boolean isMouseLeftButtonClicked(){
		if(!buttonMapClicked.containsKey(0))
			return false;
		boolean a = buttonMapClicked.get(0);
		buttonMapClicked.put(0, false);
		return a;
	}
	/**
	 * Test sur l'activation du bouton gauche de la souris
	 * @return Vrai si le bouton est acive
	 */
	public static boolean isMouseLeftButtonPressed(){
		return Mouse.isButtonDown(0);
	}
	/**
	 * Test sur le clic du bouton droit de la souris
	 * @return Vrai si le bouton a ete clique
	 */
	public static boolean isMouseRightButtonClicked(){
		if(!buttonMapClicked.containsKey(1))
			return false;
		boolean a = buttonMapClicked.get(1);
		buttonMapClicked.put(1, false);
		return a;
	}
	/**
	 * Test sur l'activation du bouton droit de la souris
	 * @return Vrai si le bouton est acive
	 */
	public static boolean isMouseRightButtonPressed(){
		return Mouse.isButtonDown(1);
	}
	/**
	 * Test sur l'activation de la molette de la souris vers le haut
	 * @return Vrai si la molette a bouge vers le haut
	 */
	public static boolean isMouseWeelUp(){
		if(!buttonMapClicked.containsKey(-1))
			return false;
		boolean a = buttonMapClicked.get(-1);
		buttonMapClicked.put(-1, false);
		return a;
	}
	/**
	 * Test sur l'activation de la molette de la souris vers le bas
	 * @return Vrai si la molette a bouge vers le bas
	 */
	public static boolean isMouseWeelDown(){
		if(!buttonMapClicked.containsKey(-2))
			return false;
		boolean a = buttonMapClicked.get(-2);
		buttonMapClicked.put(-2, false);
		return a;
	}
	/**
	 * Stockage des evenements
	 */
	public static void eventsListener(){
		while(Keyboard.next()) {
			if (!Keyboard.getEventKeyState()){
				keyMapClicked.put(Keyboard.getEventKey(), true);
				keyMapPressed.put(Keyboard.getEventKey(), false);
			}
			else
				keyMapPressed.put(Keyboard.getEventKey(), true);
		}

		while(Mouse.next()) {
			if (!Mouse.getEventButtonState() && Mouse.getEventButton()!=-1){
				buttonMapClicked.put(Mouse.getEventButton(), true);
			}
			int dweel = Mouse.getDWheel();
			if(dweel>0){
				buttonMapClicked.put(-1, true);
			}
			if(dweel<0){
				buttonMapClicked.put(-2, true);
			}
		}
	}
	/**
	 * Recupere la position de la souris sur X
	 * @return position de la souis sur X
	 */
	public static int getMouseX(){
		return Mouse.getX();
	}
	/**
	 * Recupere la position de la souris sur Y
	 * @return position de la souis sur Y
	 */
	public static int getMouseY(){
		return Mouse.getY();
	}
}
