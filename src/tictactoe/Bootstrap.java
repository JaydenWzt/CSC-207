package tictactoe;

/**
 * The bootstrap class that starts a game.  
 * @author Dawn Nye
 */
public class Bootstrap
{
	/**
	 * Loads a game and any related settings and mods.
	 * @param The command line arguments.
	 */
	public static void main(String[] args) throws Exception
	{
		if(args.length == 3)//if three arguments, two human playing
			new Thread(new TicTacToe(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]))).start();
		else if(args.length == 4)//if four arguments, AI and human playing, AI difficulty not specified by arguments
			new Thread(new TicTacToe(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]), !Boolean.parseBoolean(args[3]))).start();
		else if(args.length == 5)//if five arguments, AI and human playing, AI difficulty specified by args[4]
			new Thread(new TicTacToe(Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]), !Boolean.parseBoolean(args[3]), Integer.parseInt(args[4]))).start();
		return;
	}
}
