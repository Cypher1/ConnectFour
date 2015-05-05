package back_end;
 
public class Simulator 
{
	public static final int ROWS=6;  
	public static final int COLS=7; 
	public static final int RED=0;
	public static final int YELLOW=1; 
	public static final int BACK=2;
	point [][] map = new point[6][7];
	
	void readred()
	{
		
	}
	void readyellow()
	{
		
	}
	
	boolean inregion(int x,int y)
	{
		boolean result = false;
		if(x>=0&&x<ROWS&&y>=0&&y<COLS)
			result = true;
		return result;
	}
	boolean iswin(point p,int color)
	{
		boolean result = false;
		int [] check = new int[6];
		for(int i=p.x,j=p.y,c=0;c<7;c++,i++,j++)
		{
			if(inregion(i-3,j-3))
			{
				check[i] = map[i-3][j-3].color;
			}
			else
				check[i] = BACK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
			
		for(int i=p.x-3,j=p.y+3,c=0;c<7;c++,i++,j--)
		{
			if(inregion(i,j))
			{
				check[i] = map[i][j].color;
			}
			else
				check[i] = BACK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
			
		for(int i=p.x,j=p.y+3;i<7;j--)
		{
			if(inregion(i,j))
			{
				check[i] = map[i][j].color;
			}
			else
				check[i] = BACK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
		for(int i=p.x-3,j=p.y;i<7;i++)
		{
			if(inregion(i,j))
			{
				check[i] = map[i][j].color;
			}
			else
				check[i] = BACK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
	return result;
	}
	boolean linedcheck(int check[])
	{
		boolean result = false;
		if(check[3] == check[4] && check[5] == check[6] && check[4]==check[5])
			result = true;
		else if(check[3] == check[4] && check[5] == check[2] && check[4]==check[5])
			result = true;
		else if(check[3] == check[4] && check[1] == check[2] && check[4]==check[1])
			result = true;
		else if(check[3] == check[0] && check[1] == check[2] && check[0]==check[1])
			result = true;
		return result;		
	}
}
