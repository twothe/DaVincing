package hx.minepainter.sculpture;

import hx.utils.Debug;
import net.minecraftforge.common.util.ForgeDirection;

public class Rotation {
	
	byte[] r = new byte[9];
	
	int x,y,z;
	public void apply(int x,int y,int z){
		this.x = x*r[0] + y*r[3] + z*r[6];
		this.y = x*r[1] + y*r[4] + z*r[7];
		this.z = x*r[2] + y*r[5] + z*r[8];
		
		if(r[0] + r[3] + r[6] < 0)this.x = 7 + this.x;
		if(r[1] + r[4] + r[7] < 0)this.y = 7 + this.y;
		if(r[2] + r[5] + r[8] < 0)this.z = 7 + this.z;
	}
	
	public void applyUnbounded(int x,int y,int z){
		this.x = x*r[0] + y*r[3] + z*r[6];
		this.y = x*r[1] + y*r[4] + z*r[7];
		this.z = x*r[2] + y*r[5] + z*r[8];
	}
	
	public void multiply(Rotation r){
		byte[] result = new byte[9];
		for(int i = 0 ; i < 9; i ++){
			int x = i / 3;
			int y = i % 3;
			for(int j = 0; j < 3; j ++)
				result[i] += r.r[x*3 + j] * this.r[j*3 + y];  
		}
		this.r = result;
	}
	
	public void rotate(int face){
		if(face == 0)multiply(new Rotation(2,0));
		else if(face == 1)multiply(new Rotation(0,2));
		else if(face == 2)multiply(new Rotation(0,1));
		else if(face == 3)multiply(new Rotation(1,0));
		else if(face == 4)multiply(new Rotation(1,2));
		else if(face == 5)multiply(new Rotation(2,1));
		
//		Debug.log(r[0],r[1],r[2]);
//		Debug.log(r[3],r[4],r[5]);
//		Debug.log(r[6],r[7],r[8]);
	}
	
	Rotation(int axis1, int axis2){
		int to1 = axis1 * 3 + axis2;
		int to2 = axis2 * 3 + axis1;
		
		r[to1] = 1;
		r[to2] = -1;
		for(int i = 0; i < 3; i ++){
			if(i != axis1 && i != axis2)
				r[i *3 + i] = 1;
		}
	}
	
	public Rotation(){
		r[0] = 1;
		r[4] = 1;
		r[8] = 1;
	}
	
	private int normalize(int x){
		if(x<0)x+= (x/8)*8+8;
		x%=8;
		return x;
	}
}
