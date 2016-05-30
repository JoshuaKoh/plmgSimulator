import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Deck {

    private ArrayList<Card> deck;
    private ArrayList<Card> discardPile;
    private ArrayList<Card> lifeDeck;
    final String DECK_FILE_NAME = "/Users/joshuakoh/Desktop/Workspace/plmgSimulator/cards.json";
    final String LIFE_DECK_FILE_NAME = "/Users/joshuakoh/Desktop/Workspace/plmgSimulator/life.json";

    private Random randomGenerator;
    int[] diceVals;


    public Deck() {
        deck = new ArrayList<Card>();
        discardPile = new ArrayList<Card>();
        lifeDeck = new ArrayList<Card>();
        diceVals = new int[]{1, 2, 2, 3, 3, 4, 4, 5, 6};


        randomGenerator = new Random();
        reset();
    }

    public void reset() {
        deck.clear();
        discardPile.clear();
        lifeDeck.clear();

        String jsonStr = "";
        try {
            FileReader fileReader =
                new FileReader(DECK_FILE_NAME);

            BufferedReader bufferedReader =
                new BufferedReader(fileReader);

            String line = null;
            while((line = bufferedReader.readLine()) != null) {
                jsonStr += line;
            }
            // Always close files.
            bufferedReader.close();

            JSONObject obj = new JSONObject(jsonStr);
            JSONArray cards = obj.getJSONArray("cards");
            for (int i = 0; i < cards.length(); i++) {
                int diceVal = diceVals[randomGenerator.nextInt(diceVals.length)];

                // Test for specific cards which need specific movement vals
                if (cards.getJSONObject(i).getString("name").equals("Daily Commute")) { // Daily Commute card is always a 1 movement.
                    diceVal = 1;
                } else if (cards.getJSONObject(i).getString("name").equals("BMOC")) { // BMOC card is always a 5 movement.
                    diceVal = 5;
                }

                Card c = new Card(cards.getJSONObject(i).getString("name"),
                        cards.getJSONObject(i).getString("text"),
                        cards.getJSONObject(i).getString("category"), diceVal);
                for (int j = 0; j < cards.getJSONObject(i).getInt("copies"); j++) {
                    deck.add(c);
                }
            }

            String jsonStrL = "";
            FileReader fileReaderL =
                new FileReader(LIFE_DECK_FILE_NAME);

            BufferedReader bufferedReaderL =
                new BufferedReader(fileReaderL);

            String life_line = null;
            while((life_line = bufferedReaderL.readLine()) != null) {
                jsonStrL += life_line;
            }
            // Always close files.
            bufferedReaderL.close();

            JSONObject lifeObj = new JSONObject(jsonStrL);
            JSONArray lifeCards = lifeObj.getJSONArray("cards");
            for (int i = 0; i < lifeCards.length(); i++) {
                Card c = new Card(lifeCards.getJSONObject(i).getString("name"),
                        lifeCards.getJSONObject(i).getString("text"));
                for (int j = 0; j < lifeCards.getJSONObject(i).getInt("copies"); j++) {
                    lifeDeck.add(c);
                }
            }

            shuffleDeck();
            shuffleLifeDeck();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + DECK_FILE_NAME + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + DECK_FILE_NAME + "'");
        }

    }

     /*
      * Uses Random() to shuffle the cards in the deck
      */
     private void shuffleDeck() {
         long seed = System.nanoTime();
         Collections.shuffle(deck, new Random(seed));
     }
     private void shuffleLifeDeck() {
         long seed = System.nanoTime();
         Collections.shuffle(lifeDeck, new Random(seed));
     }


     /*
      * Returns a card off the top of the deck, assuming there is at least one
      *      card to deal.
      * @return a card if there is a card to deal
      * @return null otherwise
      */
     public Card deal() {
         if (deck.size() > 0) {
             Card c = deck.get(deck.size() - 1);
             deck.remove((deck.size() - 1));
             return c;
         } else if (discardPile.size() == 0) {
             System.out.println("The deck and discard piles are empty. Where did all the cards go?");
             System.exit(0);
         } else {
             deck = new ArrayList<Card>(discardPile);
             discardPile.clear();
             Card c = deck.get(deck.size() - 1);
             deck.remove((deck.size() - 1));
             return c;
         }
         return null;
     }

    public void discard(Card c) {
        discardPile.add(c);
    }

    public ArrayList<Card> viewXFromLife(int x) {
        ArrayList<Card> ret = new ArrayList<Card>();
        while (ret.size() < x) {
            int index = randomGenerator.nextInt(lifeDeck.size());
            Card newCard = lifeDeck.get(index);
            if (!ret.contains(newCard)) {
                ret.add(newCard);
            }
        }
        return ret;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public ArrayList<Card> viewXFromDeck(int x) {
        ArrayList<Card> ret = new ArrayList<Card>();
        if (deck.size() < x) {
            ret = new ArrayList<Card>(deck);
            return ret;
        }
        for (int i = 0; i < x; i++) {
            ret.add(deck.get(deck.size() - i - 1));
        }
        return ret;
    }

    public ArrayList<Card> viewXFromDiscard(int x) {
        ArrayList<Card> ret = new ArrayList<Card>();
        if (discardPile.size() < x) {
            ret = new ArrayList<Card>(discardPile);
            return ret;
        }
        for (int i = 0; i < x; i++) {
            ret.add(discardPile.get(discardPile.size() - i - 1));
        }
        return ret;
    }


}
