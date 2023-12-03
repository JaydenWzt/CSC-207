In order to run the game, you will execute the following command on the terminal.
	java -jar Scouts.jar w h dw dh n players...

The parameters are the following:
	w - The width of the board
	h - The height of the board
	dw - the drawn width of the board
	dh - the drawn height of the board
	n - the number of players
	players - a list of players in the lib folder given by their jar files

More than n players may be provided.
If a player cannot be loaded, the next player in line will be used in their place.
This process repeats until the full n players are loaded.
If less than n players can be loaded, default robots will be loaded to fill empty seats.

Note that the assets folder and the lib folder must be in the working directory.
The student assets folder will be extracted into the working directory as well.