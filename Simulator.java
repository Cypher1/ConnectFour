package back_end;
//import java.util.Scanner;
//import java.io.*;
public class Simulator 
{
	public static final int ROWS=6;  
	public static final int COLS=7; 
	public static final int RED=2;
	public static final int YELLOW=1; 
	public static final int BLANK=0;
	public Player player1;
	public Player player2; 
	int [][] map = new int[6][7];
	
	void gamestart()
	{
		int y = player1.nextMove(map);
		int x = getrow(y); 
		int count = 0;
		map[x][y] = RED;
		while(!iswin(x,y))
		{
			if(count == 41)
			{
				break;
			}
			if(count%2 == 0)
			{
				y = player2.nextMove(map);
				x = getrow(y); 
				if(inregion(x,y))
				{
					map[x][y] = YELLOW;
				}
				else
				{
					System.out.println("It can not be done! Please retry");
					count--;
				}
			}
			else
			{
				y = player1.nextMove(map);
				x = getrow(y); 
				if(inregion(x,y))
				{
					map[x][y] = RED;
				}
				else
				{
					System.out.println("It can not be done! Please retry");
					count--;
				}
			}
			count++;
		}
		if(count%2 == 1 && count!=41)
		{
			System.out.println("YELLOW WIN");
		}
		else if(count%2 == 0 && count!=41)
		{
			System.out.println("RED WIN");
		}
		else if(count == 41 && iswin(x,y))
		{
			System.out.println("YELLOW WIN");
		}
		else
		{
			System.out.println("DRAW");
		}
	}
//	public static void main(String[] args) 
//	 {
//		Scanner input = new Scanner(System.in);
//		String temp = input.nextLine();
//		int y = Integer.parseInt(temp);
//		Simulator sti = new Simulator();
//		int x = sti.getrow(y);
//		sti.map[x][y] =  RED;
//		for(int i=0;i<6;i++)
//		{
//			int sum = 0;
//			for(int j = 0;j<7;j++)
//			{
//				sum+=sti.map[i][j];
//			}
//			System.out.println(sum);
//		}
//		while(!sti.iswin(x, y))
//		{
////			sti.map[x][y] =  RED;
//			temp = input.nextLine();
//			y = Integer.parseInt(temp);
//			x = sti.getrow(y);
//			if(sti.inregion(x,y))
//			{
//				sti.map[x][y] = RED;
//			}
//			else
//			{
//				System.out.println("It can not be done! Please retry");
//			}
//			for(int i=0;i<6;i++)
//			{
//				int sum = 0;
//				for(int j = 0;j<7;j++)
//				{
//					sum+=sti.map[i][j];
//				}
//				System.out.println(sum);
//			}
//		}
//	 }
//	boolean readred(int col)
//	{
////		boolean result = false;
//		if(inregion(getrow(col),col))
//			return false;
//		else
//		{
//			map[getrow(col)][col] = RED;
//			map[getrow(col)][col].x = getrow(col);
//			map[getrow(col)][col].y = col;				
//			return true;
//		}
//	}
//	boolean readyellow(int col)
//	{
//		if(inregion(getrow(col),col))
//			return false;
//		else
//		{
//			map[getrow(col)][col] = YELLOW;
//			map[getrow(col)][col].x = getrow(col);
//			map[getrow(col)][col].y = col;				
//			return true;
//		}
//	}
	int getrow(int col)
	{
		int i=0;
		for(;i<6;i++)
		{
			if(map[i][col] != YELLOW && map[i][col] != RED)
				break;
		}
		return i;
	}
	private boolean inregion(int x,int y)
	{
		boolean result = false;
		if(x>=0&&x<ROWS&&y>=0&&y<COLS)
			result = true;
		return result;
	}
	private boolean iswin(int x, int y)
	{
		boolean result = false;
		int [] check = new int[7];
		for(int i=x,j=y,c=0;c<7;c++,i++,j++)
		{
			if(inregion(i-3,j-3))
			{
				check[c] = map[i-3][j-3];
			}
			else
				check[c] = BLANK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
			
		for(int i=x-3,j=y+3,c=0;c<7;c++,i++,j--)
		{
			if(inregion(i,j))
			{
				check[c] = map[i][j];
			}
			else
				check[c] = BLANK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
			
		for(int i=x,j=y+3,c=0;c<7;c++,j--)
		{
			if(inregion(i,j))
			{
				check[c] = map[i][j];
			}
			else
				check[c] = BLANK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
		for(int i=x-3,j=y,c=0;c<7;c++,i++)
		{
			if(inregion(i,j))
			{
				check[c] = map[i][j];
			}
			else
				check[c] = BLANK;
		}
		result = linedcheck(check);
			if(result==true)
				return true;
			
			return result;
	}
	private boolean linedcheck(int check[])
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
