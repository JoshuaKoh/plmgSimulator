
/*
 * Provides structure for the cards to be used in PLMG.
 * @author Joshua Koh
 * @version 1.0
 */
public class Card {


    static int idCount = 1;
    int id;
    String name;
    String text;
    String location;
    int movement;

    public Card(String name, String text, String location, int movement) {
        id = idCount++;
        this.name = name;
        this.text = text;
        this.location = location;
        this.movement = movement;
    }

    public Card(String name, String text) {
        id = idCount++;
        this.name = name;
        this.text = text;
        this.location = null;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        if (location == null) {
            return name + ": " + text;
        } else {
            return name + " (" + movement + ", " + location + "): " + text;
        }
    }

    /*
    Return 0 if cards are the same, 1 otherwise.
     */
    public int compareTo(Card o) {
        return this.name == o.getName() ? 0 : 1;

    }

}
