package lab4;

/**
 * 
 * @author Zitan Wang, Mingguang Wang
 *
 */
public class Card {

	/**
	 * This is the constructor of Card
	 * @param s The Suit of the Card
	 * @param v The Value of the Card
	 */
	public Card(Suit s, int v)
	{
		Card_suit = s;
		Value = v;
	}
	
	/**
	 * This gives the Suit of the card
	 * @return Card_suit The Suit of this card
	 */
	public Suit Suit()
	{
		return Card_suit;
	}
	
	/**
	 * This gives the Value of the card
	 * @return Value The Value of this card
	 */
	public int Value()
	{
		return Value;
	}
	
	/**
	 * t. The method should return a positive value if the parameter’s value is smaller
	 * zero if the parameter’s value is equal
	 * and a negative value otherwise (including if the parameter is null)
	 * @param c Another Card to be compared with
	 * @return int Inidicating Value of which Card is larger
	 */
	public int CompareTo(Card c)
	{
		if(c.equals(null))
			return -1;
		else
			return this.Value() - c.Value();
	}
	
	protected int Value;
	protected Suit Card_suit;
}
