import java.util.Random;


public class randomNum {
	public static void main(String[] args)
	{
		int sample = 85;
		int avg = 0;
		
		Random rand = new Random();
		
		for (int i = 0; i<2; i++)
		{
			int min = sample - 5;
			int num = rand.nextInt(11) + min;
			System.out.print(num + " ");
			avg += num;
		}	
		
		System.out.println("\n" + avg/2 + "\n");
		
		avg=0;
		for (int i = 0; i<2; i++)
		{
			int min = sample - 7;
			int num = rand.nextInt(15) + min;
			System.out.print(num + " ");
			avg += num;
		}	
		
		System.out.println("\n" + avg/2 + "\n");
		
		avg=0;
		for (int i = 0; i<2; i++)
		{
			int min = sample - 10;
			int num = rand.nextInt(21) + min;
			System.out.print(num + " ");
			avg += num;
		}	
		
		System.out.println("\n" + avg/2 + "\n");
	}
}
