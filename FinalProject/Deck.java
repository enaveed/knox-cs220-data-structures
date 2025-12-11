package game.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck
{
    private List<Card> cards = new ArrayList<>();

    public Deck()
    {
        for (Card card : Card.values())
        {
            cards.add(card);
        }
    }

    public void shuffle()
    {
        // shuffle the deck
        // randomize the order of the cards
        Collections.shuffle(cards);
    }

    public Card draw()
    {
        // remove the top card from the deck
        return cards.remove(cards.size() - 1);
    }

}
