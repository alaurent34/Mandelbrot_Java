package view;



import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
/**
 * Gestion de l'affichage opengl
 * @author abrunel
 *
 */
public class DisplayManager{

	private static int width = 1024;
	private static int height = 768;
	private static final int fps_cap = 30;
	private static DisplayMode displayMode;

	/**
	 * Initialise l'affichage
	 * @param resolution option de résolution
	 */
	public static void createDisplay2D(int resolution){  
		
		switch(resolution){
		case 0:
			width = 640;
			height = 480;
			break;
		case 1:
			width = 800;
			height = 600;
			break;
		default:
			width = 1024;
			height = 768;
			break;
		}
		
		try {
			displayMode = new DisplayMode(width,height);
			Display.setDisplayMode(displayMode);
			Display.setFullscreen(true);
			Display.setTitle("2D");

			Display.setVSyncEnabled(false);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable( GL11.GL_BLEND );
	}


	/**
	 * Actualise l'affichage
	 */
	public static void updateDisplay(){

		Display.update();
		Display.sync(fps_cap);

	}

	/**
	 * ferme la fenetre
	 */
	public static void closeDisplay(){
		Display.destroy();
	}

	/**
	 * 
	 * @return largeur de l'affichage
	 */
	public static int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return hauteur de l'affichage
	 */
	public static int getHeight() {
		return height;
	}
}

