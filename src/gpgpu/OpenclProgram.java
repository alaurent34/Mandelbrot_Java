package gpgpu;



import static org.lwjgl.opencl.CL10.clCreateBuffer;
import static org.lwjgl.opencl.CL10.clEnqueueWriteBuffer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLDeviceCapabilities;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.Util;
import static org.lwjgl.opencl.CL10.*;
import textureManager.TextureManager;

/**
 * 
 * @author abrunel
 *
 * Execution des calculs avec la technologie gpgpu
 */
public class OpenclProgram {

	private static CLContext context;
	private static CLPlatform platform;
	private static List<CLDevice> devices;
	private static CLCommandQueue queue;
	private static CLProgram program;
	private static String KERNEL_FILE = "src/gpgpu/fractal.cl";
	private static int INDEX_PLATFORM = 1;
	private static int INDEX_DEVICE = 0;
	private static CLDeviceCapabilities caps;
	private static boolean doubleEnable = true;
	/**
	 * Constructeur
	 * @throws LWJGLException erreur a la creation du programme
	 */
	public OpenclProgram() throws LWJGLException{
		IntBuffer errorBuf = BufferUtils.createIntBuffer(1);

		CL.create();
		try{
			if(CLPlatform.getPlatforms().size() == 1){
				INDEX_PLATFORM = 0;
			}
			platform = CLPlatform.getPlatforms().get(INDEX_PLATFORM); 
			printDetailOpenclGPU();
			devices = platform.getDevices(CL10.CL_DEVICE_TYPE_GPU);
			caps = CLCapabilities.getDeviceCapabilities(devices.get(INDEX_DEVICE));
			context = CLContext.create(platform, devices, errorBuf);	
			queue = CL10.clCreateCommandQueue(context, devices.get(INDEX_DEVICE), CL10.CL_QUEUE_PROFILING_ENABLE, errorBuf);
			Util.checkCLError(errorBuf.get(0));
			createPrograms();
		}catch(java.lang.NullPointerException e)
		{
			throw new LWJGLException("Platform not found");
			
		}catch(Exception e){
			e.printStackTrace();
			throw new LWJGLException("Unexpected error");
		}
		System.out.println("Program done");
	}

	/**
	 * Initialise le programme
	 */
	public void createPrograms(){
		int error = 0;
		program = CL10.clCreateProgramWithSource(context, loadText(KERNEL_FILE), null);

		if ((caps.CL_AMD_fp64 && !caps.CL_KHR_fp64) && doubleEnable){
			error = CL10.clBuildProgram(program, devices.get(INDEX_DEVICE), "-D AMD", null);
		}
		else if (caps.CL_KHR_fp64 && doubleEnable){
			error = CL10.clBuildProgram(program, devices.get(INDEX_DEVICE), "-D KHR", null);
		}
		else{
			error = CL10.clBuildProgram(program, devices.get(INDEX_DEVICE), "", null);
		}

		Util.checkCLError(error);

	}	

	/**
	 * Calcul de mandlebrot et chargement de la texture dans TextureManager
	 * @param width largeur de la fenetre
	 * @param height hauteur de la fenetre
	 * @param format format rgb / rgba
	 * @param iter_max iteration maximale
	 * @param x_min borne inferieur abs
	 * @param x_max borne supperieur abs
	 * @param y_min borne inferieur ord
	 * @param y_max borne supperieur ord
	 * @param colorBuffer gradient
	 */
	public void executeMandelbrot(int width, int height,int format, int iter_max
			,double x_min, double x_max, double y_min, double y_max,IntBuffer colorBuffer){
		CLKernel mandlebrotKernel = CL10.clCreateKernel(program, "mandlebrot", null);

		final int size = width*height;
		IntBuffer errorBuff = BufferUtils.createIntBuffer(1);

		CLMem colorMemory = clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY,colorBuffer.capacity()*4, null);
		clEnqueueWriteBuffer(queue, colorMemory, 1, 0, colorBuffer, null, null);

		CLMem pxlMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, size*format*4, errorBuff);
		Util.checkCLError(errorBuff.get(0));

		mandlebrotKernel.setArg(0, pxlMemory);
		mandlebrotKernel.setArg(1, width);
		mandlebrotKernel.setArg(2, height);
		mandlebrotKernel.setArg(3, iter_max);
		if((caps.CL_KHR_fp64 || caps.CL_AMD_fp64) && doubleEnable){
			mandlebrotKernel.setArg(4, x_min);
			mandlebrotKernel.setArg(5, x_max);
			mandlebrotKernel.setArg(6, y_min);
			mandlebrotKernel.setArg(7, y_max);
		}else{
			mandlebrotKernel.setArg(4,(float) x_min);
			mandlebrotKernel.setArg(5,(float) x_max);
			mandlebrotKernel.setArg(6,(float) y_min);
			mandlebrotKernel.setArg(7,(float) y_max);
		}
		mandlebrotKernel.setArg(8, colorMemory);
		long tempsDebut = System.currentTimeMillis();

		final int dimensions = 1; 
		PointerBuffer globalWorkSize = BufferUtils.createPointerBuffer(dimensions);
		globalWorkSize.put(0, size);
		CL10.clEnqueueNDRangeKernel(queue, mandlebrotKernel, dimensions, null, globalWorkSize, null, null, null);
		CL10.clFinish(queue);

		IntBuffer pxlBuff = BufferUtils.createIntBuffer(size*format);
		CL10.clEnqueueReadBuffer(queue, pxlMemory, CL10.CL_TRUE, 0, pxlBuff, null, null);

		CL10.clReleaseKernel(mandlebrotKernel);

		CL10.clReleaseMemObject(colorMemory);
		CL10.clReleaseMemObject(pxlMemory);

		long tempsFin = System.currentTimeMillis();
		System.out.println("Fractale Mandelbrot calculée en: "+ Float.toString((tempsFin - tempsDebut) / 1000F) + " secondes.");

		TextureManager.loadTexture(pxlBuff);

	}
	/**
	 * Calcul de julia et chargement de la texture dans TextureManager
	 * @param width largeur de la fenetre
	 * @param height hauteur de la fenetre
	 * @param format format rgb / rgba
	 * @param iter_max iteration maximale
	 * @param x_min borne inferieur abs
	 * @param x_max borne supperieur abs
	 * @param y_min borne inferieur ord
	 * @param y_max borne supperieur ord
	 * @param colorBuffer gradient
	 */
	public void executeJulia(int width, int height,int format, int iter_max
			,double x_min, double x_max, double y_min, double y_max,double c_re,double  c_im, IntBuffer colorBuffer){
		long tempsDebut = System.currentTimeMillis();

		CLKernel juliaKernel = CL10.clCreateKernel(program, "julia", null);

		final int size = width*height;
		IntBuffer errorBuff = BufferUtils.createIntBuffer(1);

		CLMem colorMemory = clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY,colorBuffer.capacity()*4, null);
		clEnqueueWriteBuffer(queue, colorMemory, 1, 0, colorBuffer, null, null);

		CLMem pxlMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, size*format*4, errorBuff);
		Util.checkCLError(errorBuff.get(0));

		juliaKernel.setArg(0, pxlMemory);
		juliaKernel.setArg(1, width);
		juliaKernel.setArg(2, height);
		juliaKernel.setArg(3, iter_max);
		if((caps.CL_KHR_fp64 || caps.CL_AMD_fp64) && doubleEnable){
			juliaKernel.setArg(4, x_min);
			juliaKernel.setArg(5, x_max);
			juliaKernel.setArg(6, y_min);
			juliaKernel.setArg(7, y_max);
			juliaKernel.setArg(8, c_re);
			juliaKernel.setArg(9, c_im);
		}else{
			juliaKernel.setArg(4, (float)x_min);
			juliaKernel.setArg(5, (float)x_max);
			juliaKernel.setArg(6, (float)y_min);
			juliaKernel.setArg(7, (float)y_max);
			juliaKernel.setArg(8, (float)c_re);
			juliaKernel.setArg(9, (float)c_im);
		}
		juliaKernel.setArg(10, colorMemory);

		final int dimensions = 1; 
		PointerBuffer globalWorkSize = BufferUtils.createPointerBuffer(dimensions);
		globalWorkSize.put(0, size);

		CL10.clEnqueueNDRangeKernel(queue, juliaKernel, dimensions, null, globalWorkSize, null, null, null);
		CL10.clFinish(queue);

		IntBuffer pxlBuff = BufferUtils.createIntBuffer(size*format);
		CL10.clEnqueueReadBuffer(queue, pxlMemory, CL10.CL_TRUE, 0, pxlBuff, null, null);

		CL10.clReleaseKernel(juliaKernel);

		CL10.clReleaseMemObject(colorMemory);
		CL10.clReleaseMemObject(pxlMemory);
		long tempsFin = System.currentTimeMillis();
		System.out.println("Fractale Mandelbrot calculÃ©e en: "+ Float.toString((tempsFin - tempsDebut) / 1000F) + " secondes.");

		TextureManager.loadTexture(pxlBuff);

	}
	/**
	 * Charge le fichier sous forme de chaine de caractere
	 * @param file Fichier contenant le programme
	 * @return fichier charge en memoire
	 */
	public String loadText(String file){
		BufferedReader br = null;
		String resultString = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder result = new StringBuilder();
			while((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}
			resultString = result.toString();
		} catch(NullPointerException npe) {
			System.err.println("Error retrieving OpenCL source file: ");
			npe.printStackTrace();
		} catch(IOException ioe) {
			System.err.println("Error reading OpenCL source file: ");
			ioe.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ex) {
				System.err.println("Error closing OpenCL source file");
				ex.printStackTrace();
			}
		}

		return resultString;
	}

	/**
	 * Fonction d'affichage platform et device associï¿½
	 */
	public void printDetailOpenclGPU(){

		for(CLPlatform platform : CLPlatform.getPlatforms()){
			System.out.println("\nPlatform:"+CLCapabilities.getPlatformCapabilities(platform));
			System.out.println("	Name = " + platform.getInfoString(CL_PLATFORM_NAME));
			System.out.println("	Version = " + platform.getInfoString(CL_PLATFORM_VERSION));
			System.out.println("	Extension = " + platform.getInfoString(CL_PLATFORM_EXTENSIONS));

			List<CLDevice> devices = platform.getDevices(CL10.CL_DEVICE_TYPE_GPU);
			for(CLDevice device : devices){
				final CLDeviceCapabilities caps = CLCapabilities.getDeviceCapabilities(device);
				System.out.println("\nDevice: " + caps);
				System.out.println("	Max Dim = " + device.getInfoInt(CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS));
				System.out.println("	Max Work size = " + device.getInfoSize(CL_DEVICE_MAX_WORK_GROUP_SIZE));
				System.out.println("	Max clock = " + device.getInfoInt(CL_DEVICE_MAX_CLOCK_FREQUENCY));				
				System.out.println("	Name = " + device.getInfoString(CL_DRIVER_VERSION));
				System.out.println("	Driver Version = " + device.getInfoString(CL_DRIVER_VERSION));
				System.out.println("	Device Version = " + device.getInfoString(CL_DEVICE_VERSION));
				System.out.println("	Extension = " + device.getInfoString(CL_DEVICE_EXTENSIONS));
			}
		}
	}
	/**
	 * Delete opencl
	 */
	public void destroyCL() {
		CL10.clReleaseProgram(program);

		CL10.clReleaseCommandQueue(queue);
		CL10.clReleaseContext(context);
		CL.destroy();
	}
}
