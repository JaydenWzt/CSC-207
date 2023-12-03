package lab4;

import lab3.DynamicArray;
import java.util.Random;
/**
 * 
 * @author Zitan Wang, Mingguang Wang
 *
 */
public class Deck {
	
	/**
	 * The default constructor
	 * It generates a standard deck of 52 playing cards
	 * This includes a card of each value 1-13 of each suit
	 */
	public Deck()
	{
		Cards = new DynamicArray();
		Card card1, card2, card3, card4;
		for(int i=1; i<=13; i++)//initialize 52 cards and add them all to Cards
		{
			card1 = new Card(Suit.SPADE, i);
			card2 = new Card(Suit.HEART, i);
			card3 = new Card(Suit.CLUB, i);
			card4 = new Card(Suit.DIAMOND, i);
			
			Cards.Add(card1);
			Cards.Add(card2);
			Cards.Add(card3);
			Cards.Add(card4);
		}
	}
	
	/**
	 * constructor
	 * Cards from card_arr are added to the deck and no other cards are added to it
	 * @param card_arr A DynamicArray assumed to contain only Card objects
	 */
	public Deck(DynamicArray card_arr)
	{
		Cards = new DynamicArray();
		this.Cards.AddAll(card_arr);
	}
	
	/**
	 * It returns the int number of cards in the deck
	 * @return int The int number of cards in the desk
	 */
	public int Count()
	{
		return Cards.Count();
	}
	
	/**
	 * It should return true if the deck has no cards left
	 * It should return false if the deck has at least one card left
	 * @return boolean Whether the desk is empty or not 
	 */
	public boolean Empty()
	{
		return Cards.Count() == 0;
	}
	
	/**
	 * It takes a Card and adds it to the deck
	 * @param c Another card to be added
	 */
	public void Add(Card c)
	{
		Cards.Add(c);
	}
	
	/**
	 * It takes a DynamicArray (assumed to contain only Card objects) and adds all of them to the deck
	 * @param card_arr A dynamicArray of cards to be add in to Cards
	 */
	public void AddAll(DynamicArray card_arr)
	{
		Cards.AddAll(card_arr);
	}
	
	/**
	 * It returns a random Card from the deck. 
	 * If no cards remain in the deck, it should return null
	 * @return Card The card picked randomly from Cards
	 */
	public Card Draw()
	{
		if(this.Empty())//return null if no cards in the desk
			return null;
		
		Random ran = new Random();
		int index = ran.nextInt(Cards.Count());//generate a random integer  
		return (Card)Cards.Remove(index);
	}
	
	/**
	 * It takes an int parameter representing the number of cards to draw and 
	 * returns a DynamicArray of those cards drawn
	 * If there are not enough cards left in the deck,
	 * it returns null (and not modify the deck’s contents)
	 * @param num The number of cards to be drawn
	 * @return DynamicArray A DynamicArray including all cards removed
	 */
	public DynamicArray Draw(int num)
	{
		if(num>Cards.Count())//null, if no enough cards
			return null;
		
		DynamicArray cards_drawn = new DynamicArray(num);//the new DynamicArray
		for(int i=0; i<num; i++)//Call the other Draw() to remove num number of cards
		{
			cards_drawn.Add(this.Draw());
		}
		return cards_drawn;
	}
	protected DynamicArray Cards;
}
