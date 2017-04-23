package fractal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import controller.Controller_GUI;

/**
 * 
 * Coloration de la fractale en fonction du mode choisit
 * 
 * @author gtrauchessec
 *
 */
public class Coloration {

	private static int colorMode = 0;

	private static int[][] mapping;
	private static int[] color1;
	private static int[] color2;
	private static File file;
	private static BufferedImage image;
	private static int gradient_widht=0; 

	/**
	 * Semi-Constructeur pour le static
	 */
	public static void InitColorSystem(){

		colorMode = 0;
		mapping = new int[16][3];
		int iColor=0;

		mapping[iColor][0] = 66;	mapping[iColor][1] = 30;	mapping[iColor][2] = 15;	iColor++;
		mapping[iColor][0] = 25;	mapping[iColor][1] = 7;		mapping[iColor][2] = 26;	iColor++;
		mapping[iColor][0] = 9;		mapping[iColor][1] = 1;		mapping[iColor][2] = 47;	iColor++;
		mapping[iColor][0] = 4;		mapping[iColor][1] = 4;		mapping[iColor][2] = 73;	iColor++;
		mapping[iColor][0] = 0;		mapping[iColor][1] = 7;		mapping[iColor][2] = 100;	iColor++;
		mapping[iColor][0] = 12;	mapping[iColor][1] = 44;	mapping[iColor][2] = 138;	iColor++;
		mapping[iColor][0] = 24;	mapping[iColor][1] = 82;	mapping[iColor][2] = 177;	iColor++;
		mapping[iColor][0] = 57;	mapping[iColor][1] = 125;	mapping[iColor][2] = 209;	iColor++;
		mapping[iColor][0] = 134;	mapping[iColor][1] = 181;	mapping[iColor][2] = 229;	iColor++;
		mapping[iColor][0] = 211;	mapping[iColor][1] = 236;	mapping[iColor][2] = 248;	iColor++;
		mapping[iColor][0] = 241;	mapping[iColor][1] = 233;	mapping[iColor][2] = 191;	iColor++;
		mapping[iColor][0] = 248;	mapping[iColor][1] = 201;	mapping[iColor][2] = 95;	iColor++;
		mapping[iColor][0] = 255;	mapping[iColor][1] = 107;	mapping[iColor][2] = 0;		iColor++;
		mapping[iColor][0] = 204;	mapping[iColor][1] = 128;	mapping[iColor][2] = 0;		iColor++;
		mapping[iColor][0] = 153;	mapping[iColor][1] = 87;	mapping[iColor][2] = 0;		iColor++;
		mapping[iColor][0] = 106;	mapping[iColor][1] = 52;	mapping[iColor][2] = 3;		iColor++;

		color1 = new int[3];
		color1[0] = 0; color1[1] = 0; color1[2] = 0;
		color2 = new int[3];
		color2[0] = 255; color2[1] = 255; color2[2] = 255;
	}

	/**
	 * Setter pour le chemin d'acces du gradient
	 * @param path chemin d'acces du gradient
	 */
	public static void setGradientFile(String path){
		file= new File(path);
		try {
			image = ImageIO.read(file);
			gradient_widht = image.getWidth();
		} catch (IOException e) {
			System.out.println("Error: File not found ("+path+")");
			image = null;
		}
	}

	/**
	 * Setter du mode d'affichage
	 * @param mode Mode d'affichage
	 */
	public static void setMode(int mode){
		Coloration.colorMode = mode;

		Coloration.setGradientFile(Controller_GUI.getGradient_path());



	}
	/**
	 * Donne la valeur du rouge pour le degradé
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de rouge
	 */
	public static int getMappingRed(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return mapping[(int)iter%16][0];
	}
	/**
	 * Donne la valeur du vert pour le degradé
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de vert
	 */
	public static int getMappingGreen(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return mapping[(int)iter%16][1];
	}
	/**
	 * Donne la valeur du bleu pour le degradé
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de bleu
	 */
	public static int getMappingBlue(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return mapping[(int)iter%16][2];
	}



	/**
	 * Donne la valeur du rouge pour le mode bicolor
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de rouge
	 */
	public static int getColorRed(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return color2[0];
		else
			return color1[0];
	}
	/**
	 * Donne la valeur du vert pour le mode bicolor
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de vert
	 */
	public static int getColorGreen(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return color2[1];
		else
			return color1[1];
	}
	/**
	 * Donne la valeur du bleu pour le mode bicolor
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de bleu
	 */
	public static int getColorBlue(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return color2[2];
		else
			return color1[2];
	}


	/**
	 * Donne la valeur du rouge pour le mode gradient
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de rouge
	 */
	public static int getFunctionRed(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return Coloration.getPixelColor((int) (iter%gradient_widht), 0).getRed();
	}
	/**
	 * Donne la valeur du vert pour le mode gradient
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de vert
	 */
	public static int getFunctionGreen(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return Coloration.getPixelColor((int) (iter%gradient_widht), 0).getGreen();
	}
	/**
	 * Donne la valeur du bleu pour le mode gradient
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de bleu
	 */
	public static int getFunctionBlue(double iter, double iter_max,double x,double y,double c_re,double c_im){
		if(iter >= iter_max)
			return 0;
		else
			return Coloration.getPixelColor((int) (iter%gradient_widht), 0).getBlue();
	}




	/**
	 * Donne la valeur du rouge pour le mode d'affichage actuel
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de rouge
	 */
	public static int getRed(double iter, double iter_max,double x,double y,double c_re,double c_im){
		switch(colorMode){
		case 0:
			return getColorRed(iter, iter_max,x,y,c_re,c_im);
		case 2:
			return getFunctionRed(iter, iter_max,x,y,c_re,c_im);
		default:
			return getMappingRed(iter, iter_max,x,y,c_re,c_im);
		}
	}
	/**
	 * Donne la valeur du vert pour le mode d'affichage actuel
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de vert
	 */
	public static int getGreen(double iter, double iter_max,double x,double y,double c_re,double c_im){
		switch(colorMode){
		case 0:
			return getColorGreen(iter, iter_max,x,y,c_re,c_im);
		case 2:
			return getFunctionGreen(iter, iter_max,x,y,c_re,c_im);
		default:
			return getMappingGreen(iter, iter_max,x,y,c_re,c_im);
		}
	}
	/**
	 * Donne la valeur du bleu pour le mode d'affichage actuel
	 * @param iter Iteration du pixel a colorier
	 * @param iter_max Iteration Maximale
	 * @param x Position du pixel sur X
	 * @param y Position du pixel sur Y
	 * @param c_re Valeur reelle de Julia
	 * @param c_im Valeur imaginaire de Julia
	 * @return Une valeur de bleu
	 */
	public static int getBlue(double iter, double iter_max,double x,double y,double c_re,double c_im){
		switch(colorMode){
		case 0:
			return getColorBlue(iter, iter_max,x,y,c_re,c_im);
		case 2:
			return getFunctionBlue(iter, iter_max,x,y,c_re,c_im);
		default:
			return getMappingBlue(iter, iter_max,x,y,c_re,c_im);
		}
	}
	/**
	 * Recupere la valeur RGB d'un pixel
	 * @param x Position sur X
	 * @param y Position sur Y
	 * @return couleur RGC
	 */
	public static Color getPixelColor(int x,int y){

		if(image==null)
			return new Color(255,0,0);

		x = Math.abs(x);

		int clr=  image.getRGB(x,y); 
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;

		return new Color(red, green, blue);
	}

	public static IntBuffer getColorBuffer(){
		int BYTES_PER_PIXEL = 3;
		IntBuffer buffer;
		if(image != null){
			buffer = BufferUtils.createIntBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); 
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

			for(int y = 0; y < image.getHeight(); y++){
				for(int x = 0; x < image.getWidth(); x++){
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put( ((pixel >> 16) & 0xFF)); 
					buffer.put( ((pixel >> 8) & 0xFF));    
					buffer.put( (pixel & 0xFF));      
				}
			}
			buffer.flip();
		}else{
			buffer = BufferUtils.createIntBuffer(256 * BYTES_PER_PIXEL); 
			int[] defaultBuffer = {1, 10, 103, 1, 11, 104, 1, 11, 104, 2, 
					12, 105, 2, 12, 105, 3, 14, 107, 3, 13, 
					109, 3, 15, 111, 4, 14, 116, 4, 16, 116, 5, 
					16, 122, 5, 18, 121, 6, 18, 127, 6, 20, 
					125, 7, 21, 129, 7, 25, 130, 9, 26, 132, 
					9, 30, 135, 10, 34, 136, 12, 38, 139, 13, 
					42, 142, 15, 48, 145, 16, 52, 148, 17, 57, 
					152, 18, 61, 156, 19, 65, 160, 20, 69, 163, 
					21, 72, 167, 22, 75, 170, 23, 78, 173, 24, 80, 
					175, 24, 83, 178, 25, 85, 180, 26, 87, 183, 27,
					90, 185, 27, 92, 188, 28, 95, 190, 30, 100, 195,
					30, 102, 198, 31, 104, 200, 32, 107, 203, 34, 109,
					204, 37, 111, 205, 41, 113, 205, 41, 113, 205, 41,
					113, 205, 50, 120, 208, 53, 122, 208, 56, 124, 209,
					59, 127, 210, 62, 129, 211, 65, 131, 211, 68, 133, 212,
					71, 135, 213, 74, 138, 214, 77, 140, 215, 80, 142, 215,
					83, 144, 216, 86, 146, 217, 89, 149, 218, 92, 151, 218,
					95, 153, 219, 98, 155, 220, 101, 157, 221, 104, 160, 221,
					107, 162, 222, 110, 164, 223, 113, 166, 224, 113, 166, 224,
					120, 170, 225, 123, 173, 226, 126, 175, 227, 129, 177, 228, 
					132, 179, 228, 135, 181, 229, 138, 184, 230, 141, 186, 231,
					144, 188, 232, 147, 190, 232, 150, 192, 233, 153, 195, 234, 
					156, 197, 235, 159, 199, 235, 162, 201, 236, 165, 203, 237,
					168, 206, 238, 171, 208, 238, 174, 210, 239, 177, 212, 240,
					180, 214, 241, 183, 217, 242, 186, 219, 242, 189, 221, 243,
					192, 223, 244, 195, 225, 245, 199, 228, 245, 202, 230, 246,
					205, 232, 247, 208, 234, 248, 211, 236, 248, 214, 238, 249,
					217, 241, 250, 220, 243, 251, 223, 245, 252, 226, 247, 252,
					229, 249, 253, 232, 252, 254, 235, 254, 255, 237, 255, 254, 
					237, 254, 250, 238, 251, 241, 238, 249, 236, 239, 248, 232,
					239, 246, 227, 239, 245, 223, 240, 243, 219, 240, 242, 214, 
					240, 240, 210, 241, 239, 205, 241, 237, 201, 241, 236, 197,
					242, 234, 192, 242, 233, 188, 242, 231, 183, 242, 230, 179,
					243, 228, 174, 243, 227, 170, 243, 226, 166, 244, 224, 161,
					244, 223, 157, 244, 221, 152, 245, 220, 148, 245, 218, 144,
					245, 217, 139, 246, 215, 135, 246, 214, 130, 246, 212, 126, 
					247, 211, 121, 247, 211, 121, 247, 208, 113, 247, 206, 108,
					248, 205, 104, 248, 203, 99, 248, 202, 95, 249, 201, 91, 249,
					199, 86, 249, 198, 82, 250, 196, 77, 250, 195, 73, 250, 193,
					68, 251, 192, 64, 251, 190, 60, 251, 189, 55, 251, 187, 51, 
					252, 186, 46, 252, 184, 42, 252, 183, 38, 253, 181, 33, 253,
					180, 29, 253, 178, 24, 254, 177, 20, 254, 175, 15, 254, 174, 
					11, 255, 173, 7, 255, 171, 2, 253, 169, 0, 248, 166, 0, 244,
					163, 0, 239, 160, 0, 234, 157, 0, 230, 154, 0, 225, 151, 0, 
					221, 148, 0, 216, 145, 0, 212, 142, 0, 207, 139, 0, 202, 136, 
					0, 198, 133, 0, 193, 130, 0, 189, 127, 0, 184, 124, 0, 180, 120,
					0, 166, 111, 0, 166, 111, 0, 161, 108, 0, 157, 105, 0, 152, 102,
					0, 148, 99, 0, 143, 96, 0, 138, 93, 0, 134, 90, 0, 129, 87, 0, 125,
					84, 0, 120, 81, 0, 120, 81, 0, 111, 75, 0, 107, 72, 0, 102, 69, 0,
					97, 66, 0, 93, 63, 0, 88, 60, 0, 84, 57, 0, 79, 54, 0, 75, 51, 0,
					70, 48, 0, 65, 45, 0, 61, 42, 0, 56, 39, 0, 52, 36, 0, 47, 33, 0, 
					43, 30, 0, 38, 27, 0, 33, 24, 0, 29, 21, 0, 24, 18, 0, 20, 15, 0,
					15, 12, 0, 11, 9, 0, 6, 6, 0, 1, 3, 0, 0, 2, 2, 0, 2, 5, 0, 2, 8, 
					0, 3, 10, 0, 3, 13, 0, 3, 16, 0, 3, 19, 0, 3, 21, 0, 3, 24, 0, 3,
					27, 0, 3, 29, 0, 4, 32, 0, 4, 35, 0, 4, 37, 0, 4, 40, 0, 4, 43, 0,
					4, 46, 0, 4, 48, 0, 5, 51, 0, 5, 54, 0, 5, 56, 0, 5, 59, 0, 5, 62, 
					0, 5, 64, 0, 5, 67, 0, 6, 70, 0, 6, 73, 0, 6, 75, 0, 6, 78, 0, 6, 81,
					0, 6, 83, 0, 6, 86, 0, 6, 89, 0, 7, 92, 0, 7, 94, 0, 7, 97};
			buffer.put(defaultBuffer);
			buffer.flip();
		}
			return buffer;
		}
	
}
