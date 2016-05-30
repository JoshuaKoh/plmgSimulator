import java.util.ArrayList;

public class Player {

    static int idCount = 1;
    int id;
    String name;
    int money;
    int preparation;
    int morale;
    int evil;
    double gpa;
    ArrayList<Card> hand;

    public Player(String name) {
        id = idCount++;
        this.name = name;
        money = 20;
        preparation = 0;
        morale = 5;
        evil = 0;
        gpa = 4.0;
        hand = new ArrayList<Card>();
    }

    public void addMoney(int add) {
        money += add;
        if (money < 0) {
            money = 0;
        }
    }

    public void addPreparation(int add) {
        preparation+= add;
        if (preparation < 0) {
            preparation = 0;
        }
    }

    public void addMorale(int add) {
        morale += add;
        if (morale < 0) {
            morale = 0;
        } else if (morale > 10) {
            morale = 10;
        }
    }
    public void addEvil(int add) {
        evil += add;
        if (evil < 0) {
            evil = 0;
        }
    }

    public void addGPA(double add) {
        gpa += add;
        if (gpa > 4.0) {
            gpa = 4.0;
        } else if (gpa < 0) {
            gpa = 0;
        }
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public String toString() {
        return "--- " + id + ". " + name + " (GPA: " + gpa + ") ---\n" +
                "Money:\t" + money +
                "\t\tPrep:\t" + preparation +
                "\nEvil:\t" + evil +
                "\t\tMorale:\t" + morale +
                "\n--------------------------";
    }

    public String printHand() {
        String ret = "";
        int index = 1;
        for (Card c : hand) {
            ret += "[ " + index++ + " ] " + c.toString() + "\n";
        }
        return ret;
    }

    // index comes in as a number 1 through STARTING_HAND_SIZE. It is decremented to adjust for ArrayList starting at index 0.
    public Card discardCard(int index) {
        Card c = hand.get(index - 1);
        hand.remove(index - 1);
        return c;
    }

    // index comes in as a number 1 through STARTING_HAND_SIZE. It is decremented to adjust for ArrayList starting at index 0.
    public Card getCardFromHand(int index) {
        return hand.get(index - 1);
    }

}