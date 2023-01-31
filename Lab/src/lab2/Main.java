package lab2;

import java.io.File;
import java.io.FileWriter;
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
        // In Java, a try-catch block will prevent errors (such as attempting to dereference a null pointer) from crashing your program
        // Catching a general Exception will catch all types of errors, while catching only IOExceptions will catch only errors of this type
        // The try-catch block below is a little different
        // Anything that implements the Closeable interface can be initialized like this so that it is automatically closed when no longer needed
        // The scope of the variable created is local to the try-catch block and is closed when it falls out of scope
        // This is equivalent to a using statement in C#

        int count = 0;
        int length = 5;
        Integer arr[] = new Integer[length];
        //scanning the infile to get integers and stored in {@code arr}
        try(Scanner fin = new Scanner(new File("files/lab2 - in.txt")))
        {
            while(fin.hasNextInt())
            {
                arr[count] = fin.nextInt();
                count++;
                //In order to store more elements, expands {@code arr} if the length if the array is full
                if(count == length)
                {
                    arr = ExpandArray(arr);
                    length*=2;
                }
            }
        }
        catch(Exception e)
        {System.err.println("Input error: " + e);}



        // When writing to a file, the .close method must be called to ensure that all contents are written
        try(FileWriter fout = new FileWriter(new File("files/lab2 - out.txt")))
        {
            ReverseSumElements(arr);//process the array
            for(int i=0; i<count; i++)//output all integers to the outfile one by one
            {
                fout.write(arr[i]+ " ");
            }
            fout.close();//close file
        }
        catch(Exception e)
        {System.err.println("Output error: " + e);}

        return;
    }

    /**
     * Creates a new array of twice the length of {@code arr}.
     * It then copies the elements of {@code arr} into the front of the new array in the same order that they appear in {@code arr}.
     * For example, an input {1,2,3} returns a new array containing {1,2,3,-,-,-}, where - denotes an null value.
     * @param arr The array to double.
     * @return Returns a new array twice the length of {@code arr} such that all of the elements of arr are copied into the new array.
     */
    public static Integer[] ExpandArray(Integer[] arr)
    {
        int length = arr.length;//the original length
        Integer new_list[] = new Integer[length*2];//create an array with twice the length
        for(int i=0; i<length; i++)//putting all items from the previous array to the new array
        {
            new_list[i] = arr[i];
        }

        return new_list;
    }

    /**
     * Puts the sum of the last {@code i} elements into {@code arr[|arr| - 1 - i]} for each {@code |arr| > i >= 0}.
     * Null values are ignored.
     * For example, the input {1,2,3,-} (- again means null) should change the array to have the values {6,5,3,-}.
     * @param arr The array whose elements we wish to sum.
     */
    public static void ReverseSumElements(Integer[] arr)
    {
        int sum;
        int len = arr.length;
        for(int i=0; i<len; i++)//going over all items in the array that has value
        {
        	if(arr[i] == null)
        		break;
            sum=0;
            for(int j=i; j<len; j++)//adding up integers that has the index j>=i
            {
            	if(arr[j] == null)
            		break;
                sum+=arr[j];
            }
            arr[i]=sum;
        }
        return;
    }
}