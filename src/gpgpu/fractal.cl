#ifdef AMD
	#pragma OPENCL EXTENSION cl_amd_fp64 : enable
	#define var double
#else
	#ifdef KHR
		#pragma OPENCL EXTENSION cl_khr_fp64 : enable
		#define var double
	#else
		#define var float	
	#endif
#endif

kernel void mandlebrot(global int* pxlBuffer, int const width, int const height, int const iter_max, var const x_min,
					var const x_max,var const y_min,var const y_max,global const int* colorBuffer) {
	   	const int xid = get_global_id(0); 
		int iter=0;
		var zoom_x = width/(x_max-x_min);
		var zoom_y = height/(y_max-y_min);

		int pxl_coordX = xid % width;
		int pxl_coordY = xid / width;

		var c_re = pxl_coordX/zoom_x+x_min;
		var c_im = pxl_coordY/zoom_y+y_min;
		var x = 0;
		var y = 0;
		var tmpX;
		var tmpY;
		while(((x*x + y*y) < 2*2)  &&  iter < iter_max )
		{
			tmpX = x*x - y*y + c_re;
			tmpY = 2*x*y + c_im;
			if ((x == tmpX)  &&  (y == tmpY))
			{
				iter = iter_max;
				break;
			}
			x = tmpX;
			y = tmpY;
			iter++;
		}
		
		if(iter==iter_max){
			pxlBuffer[xid*3]=0;
			pxlBuffer[xid*3+1]=0;
			pxlBuffer[xid*3+2]=0;
		}else{
			pxlBuffer[xid*3]=colorBuffer[(iter%256)*3];
			pxlBuffer[xid*3+1]=colorBuffer[(iter%256)*3+1];
			pxlBuffer[xid*3+2]=colorBuffer[(iter%256)*3+2];
		}
}

kernel void julia(global int* pxlBuffer, int const width, int const height, int const iter_max, var const x_min,
					var const x_max,var const y_min,var const y_max,
					var const c_re, var const c_im, global const int* colorBuffer) {
	   	const int xid = get_global_id(0); 
		int iter=0;
		var zoom_x = width/(x_max-x_min);
		var zoom_y = height/(y_max-y_min);
		int pxl_coordX = xid % width;
		int pxl_coordY = xid / width;
	
		var x = pxl_coordX/zoom_x+x_min;
		var y = pxl_coordY/zoom_y+y_min;
				
		var tmpX,tmpY;
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
		
		if(iter==iter_max){
			pxlBuffer[xid*3]=0;
			pxlBuffer[xid*3+1]=0;
			pxlBuffer[xid*3+2]=0;
		}else{
			pxlBuffer[xid*3]=colorBuffer[(iter%256)*3];
			pxlBuffer[xid*3+1]=colorBuffer[(iter%256)*3+1];
			pxlBuffer[xid*3+2]=colorBuffer[(iter%256)*3+2];
		}		
}




