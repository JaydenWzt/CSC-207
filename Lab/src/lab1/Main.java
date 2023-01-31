package lab1;

import java.util.Scanner;

public class Main
{
	/**
	 * The entry point into your code.
	 * This is where your program begins.
	 * Unlike C, the return type of your main function MUST be void, and it must have a single String[] argument.
	 * @param args The command line arguments given to your program.
	 * @author Zitan Wang
	 */
	public static void main(String[] args)
	{
		
		Scanner in = new Scanner(System.in);
		String selection = "A";
		int count = 0;
		
		//looping until Q is entered 
		while(!selection.equals("Q"))
		{
			//instructions on expected inputs
			System.out.println("Please select whether you what do the Fibonacci sequence recursuvely(R) or iteratively(I).");
			System.out.println("Or press S or m F characters");
			System.out.println("Then enter a positive integer representing the nth Finonacci number you want");
			System.out.println("Press Q to exit");
		
			selection = in.next();//Reading in the letter
				
			if(selection.equals("Q"))//terminate when Q is entered
			{
				continue;
			}
			if(!(selection.equals("R")||selection.equals("I")||selection.equals("S")))//dealing with invalid inputs
			{
				System.out.println("Invalid input");
				continue;
			}
			count = in.nextInt();//Reading in the integer
			in.nextLine();
			
			if((count<0))//dealing with invalid inputs
			{
				System.out.println("Invalid input");
				continue;
			}
			
			if(selection.equals("R"))//calculate and print the {@code count}th Fibonacci number recursively
			{
				System.out.println(RFibonacci(count));
			}
			else if(selection.equals("I"))//calculate iteratively
			{
				System.out.println(Fibonacci(count));
			}
			else if(selection.equals("S"))//print the correspondent number of F character
			{
				System.out.println(SFibonacci(count));
			}
		}
		System.out.println("Thanks for using our program");
		return;
	}
	
	/**
	 * Computes the {@code n}th Fibonacci number iteratively.
	 * Here we define f<sub>0</sub> = 0 and f<sub>1</sub> = 1.
	 * Further, recall that f<sub>m + 2</sub> = f<sub>m + 1</sub> + f<sub>m</sub> for a natural number m.
	 * @param n The fibinnochi number to return.
	 * @return Returns the {@code n}th Fibonacci number f<sub>n</sub>.
	 */
	public static int Fibonacci(int n)
	{
		int num = 1;
		int last_num = 1;
		//return 1 directly if n == 1 or n == 2
		if(n == 1 || n == 2)
			return 1;
		else if (n == 0)
			return 0;
		//for larger input, calculate m iteratively
		else
		{
			for(int i=2; i<n; i++)
			{
				num = last_num + num;
				last_num = num - last_num;
			}
		}
		return num;
	}

	/**
	 * Computes the {@code n}th Fibonacci number recursively.
	 * Here we define f<sub>0</sub> = 0 and f<sub>1</sub> = 1.
	 * Further, recall that f<sub>m + 2</sub> = f<sub>m + 1</sub> + f<sub>m</sub> for a natural number m.
	 * @param n The fibinnochi number to return.
	 * @return Returns the {@code n}th Fibonacci number f<sub>n</sub>.
	 */
	public static int RFibonacci(int n)
	{
		//reaches edge cases
		if(n == 1 || n == 2)
			return 1;
		else if (n == 0)
			return 0;
		//keep the recursion working
		else 
			return RFibonacci(n - 1) + RFibonacci(n - 2);
	}
	
	/**
	 * Computes the {@code n}th Fibonacci number recursively by calling RFibonacci(),
	 * then print out m continuous F characters.
	 * Here we define f<sub>0</sub> = 0 and f<sub>1</sub> = 1.
	 * Further, recall that f<sub>m + 2</sub> = f<sub>m + 1</sub> + f<sub>m</sub> for a natural number m.
	 * @param n The fibinnochi number to return.
	 * @return Returns the String of F characters
	 */
	public static String SFibonacci(int n)
	{
		//get the nth Fibunacci number recursively
		int count_F = RFibonacci(n);
		String str = "";
		//print out the correspondent number of F character
		for (int i = 0; i < count_F; i++)
		{
			str += "F";
		}
		return str;
	}
}