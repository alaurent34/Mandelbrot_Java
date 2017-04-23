package view;

import java.awt.Font;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import textureManager.TextureManager;

/**
 * 
 * @author gtrauchessec
 * @author alaurent
 * @author abrunel
 * Classe de rendu graphique
 */
public class Render {
	/**
	 * Constructeur
	 */
	public Render() {
		super();
		TextureManager.setUpTextureManager();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
	}

	/**
	 * Actualise le rendu
	 */
	public void updateRender(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Dessine la fractale à l'écran
	 */
	public void drawFractale(){

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureManager.getTexture_id());
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0,0);
		
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(DisplayManager.getWidth(),0);
		
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(DisplayManager.getWidth(),DisplayManager.getHeight());
		
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(0,DisplayManager.getHeight());
		
		GL11.glEnd();
	}

	/**
	 * Dessine le rectangle du chargement et la zoombox
	 * @param x1 x1
	 * @param y1 y1
	 * @param x2 x2
	 * @param y2 y2
	 */
	public void drawRectangle(double x1,double y1,double x2, double y2){

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x1,y1);
		GL11.glVertex2d(x2,y1);
		GL11.glVertex2d(x2,y2);
		GL11.glVertex2d(x1,y2);
		GL11.glEnd();
	}

	/**
	 * 
	 * @param x1 x1
	 * @param y1 y1
	 * @param x2 x2
	 * @param y2 y2
	 * @param r rouge
	 * @param g vert
	 * @param b bleu
	 * @param a alpha
	 * @param w epaisseur
	 */
	public void drawEmptyRectangle(double x1,double y1,double x2, double y2, double r,double g,double b,double a,double w){

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor4d(r,g,b,a);

		y1 = Display.getHeight() - y1;
		y2 = Display.getHeight() - y2;

		drawRectangle(x1, y1-w/2, x2, y1+w/2);
		drawRectangle(x1-w/2, y1, x1+w/2, y2);
		drawRectangle(x2+w/2, y1, x2-w/2, y2);
		drawRectangle(x1, y2+w/2, x2, y2-w/2);

		GL11.glColor4d(1.,1., 1.,1.);
	}

	/**
	 * 
	 * @param x x
	 * @param y y
	 * @param a alpha
	 */
	public void drawLoading(double x,double y,double a){

		double r = 1.;
		double g = 1.;
		double b = 1.;

		int size_x = 128;
		int size_y = 32;


		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor4d(0.3, 0.3, 0.3,0.5);
		drawRectangle(x, y, x+size_x, y+size_y);
		
		drawEmptyRectangle(x,Display.getHeight() - y,x+size_x,Display.getHeight() - y-size_y,r,g,b,a,2.0);

		TrueTypeFont font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24),true);
		Color c = new Color((float)r, (float)g, (float)b, (float)a);
		font.drawString((float)x+7, (float)y+2, "LOADING",c);
		GL11.glColor4d(1.,1., 1.,1.);
	}
	
}
