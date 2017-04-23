package textureManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import view.DisplayManager;

/**
 * @author gtrauchessec
 * @author alaurent
 * @author abrunel
 * Gestion des textures
 */
public class TextureManager {
	private static int texture_id;

	/**
	 * Initialise la texture principal
	 */
	public static void setUpTextureManager(){
		int target = GL11.GL_TEXTURE_2D;

		texture_id = GL11.glGenTextures();
		GL11.glEnable(target);
		GL11.glBindTexture(target, texture_id) ;

		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		ByteBuffer buffer = BufferUtils.createByteBuffer(DisplayManager.getHeight()*DisplayManager.getWidth()*3);
		for(int i=0;i<DisplayManager.getHeight();++i){
			for(int j=0;j<DisplayManager.getWidth();++j){
				buffer.put((byte)0);
				buffer.put((byte)0);
				buffer.put((byte)0);
			}
		}
		buffer.flip();
		GL11.glTexImage2D(target, 0, GL11.GL_RGB, DisplayManager.getWidth(), DisplayManager.getHeight(), 0, GL11.GL_RGB,GL11.GL_UNSIGNED_BYTE, buffer);
	}

	/**
	 * Charge la texture sur la texture principale
	 * @param buffer buffer des pixels de la texture a charger
	 */
	public static void loadTexture(ByteBuffer buffer){

		int target = GL11.GL_TEXTURE_2D;
		GL11.glEnable(target);

		GL11.glBindTexture(target, texture_id);
		GL11.glTexSubImage2D(target, 0,0,0, DisplayManager.getWidth(), DisplayManager.getHeight(), GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,buffer);
	}
	
	/**
	 * Charge la texture sur la texture principale
	 * @param buffer buffer des pixels de la texture a charger
	 */
	public static void loadTexture(IntBuffer iBuffer){
		ByteBuffer buffer = BufferUtils.createByteBuffer(iBuffer.capacity());
		for(int i=0;i<iBuffer.capacity();++i){
			buffer.put((byte) iBuffer.get(i));
		}
		buffer.flip();
		int target = GL11.GL_TEXTURE_2D;
		GL11.glEnable(target);

		GL11.glBindTexture(target, texture_id);
		GL11.glTexSubImage2D(target, 0,0,0, DisplayManager.getWidth(), DisplayManager.getHeight(), GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,buffer);
	}

	
	/**
	 * ScreenShot
	 */
	public static void exportCurrentTexture(){

		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = DisplayManager.getWidth();
		int height= DisplayManager.getHeight();
		int bpp = 3;

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer );

		String folder = "./screenshots";
		String name = "screenshot_";
		File f = new File(folder);
		if(!f.exists()) {
			(new File(folder)).mkdirs();
		}
		GregorianCalendar calendar = new GregorianCalendar();
		int heure = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int seconde = calendar.get(Calendar.SECOND);
		int mseconde = calendar.get(Calendar.MILLISECOND);
		int jour = calendar.get(Calendar.DAY_OF_MONTH);
		int mois = calendar.get(Calendar.MONTH);
		int ans = calendar.get(Calendar.YEAR);
		name = name + jour+"_"+mois+"_"+ans+"_"+heure+"_"+minute+"_"+seconde+"_"+mseconde+".png";
		File file = new File(folder+"/"+name);
		String format = "PNG";
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int x = 0; x < width; x++) 
		{
			for(int y = 0; y < height; y++)
			{
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) { e.printStackTrace(); }
		System.out.println("Screenshot sauvegarde: "+name);
	}
	
	/**
	 * 
	 * @return id de la texture charge
	 */
	public static int getTexture_id() {
		return texture_id;
	}



	/**
	 * Supprime la texture principale
	 */
	public static void cleanUp(){
		GL11.glDeleteTextures(texture_id);
	}

}
