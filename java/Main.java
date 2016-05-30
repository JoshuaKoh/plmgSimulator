import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * TODO Add option to view cards and keep cards from deck or discard
 * TODO Handle when attempting to draw from low/empty discard pile
 * TODO Add option to view cards from another player's hand
 */
public class Main {

    // Change this to determine number of players in game.
    String[] playerNames = {"Josh", "Megan", "Joe", "Kait"};

    final int STARTING_HAND_SIZE = 5;

    Player[] players;
    boolean doGame = true;
    boolean newPlayer = true;
    int playerTurn = 0;
    boolean repeatRequest = true;
    int roundCount = 1;

    Scanner scan = new Scanner(System.in);

    /*
    Regex for testing if number is double. Used when working with user input and GPA.
     */
    final String Digits     = "(\\p{Digit}+)";
    final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    final String Exp        = "[eE][+-]?"+Digits;
    final String fpRegex    =
            ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
                    "[+-]?(" + // Optional sign character
                    "NaN|" +           // "NaN" string
                    "Infinity|" +      // "Infinity" string

                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from the Java Language Specification, 2nd
                    // edition, section 3.10.2.

                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                    // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\.("+Digits+")("+Exp+")?)|"+

                    // Hexadecimal strings
                    "((" +
                    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "(\\.)?)|" +

                    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                    ")[pP][+-]?" + Digits + "))" +
                    "[fFdD]?))" +
                    "[\\x00-\\x20]*");// Optional trailing "whitespace"



    public void play() {
        // Initialize deck
        Deck deck = new Deck();

        // Initialize players
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i]);
            for (int j = 0; j < STARTING_HAND_SIZE; j++) {
                players[i].addToHand(deck.deal());
            }
        }

        while (doGame) {
            if (newPlayer) {
                newPlayer = false;
                clearText();
                printPlayers();
                System.out.println("\n=== " + playerNames[playerTurn] + "'s Turn (round " + roundCount + ") ===");
            }

            System.out.println("\n" + playerNames[playerTurn] + ", enter an action:\n" +
                    "[ 0 ] Pass to next player.\n" +
                    "[ 1 ] Play a card.\n" +
                    "[ 2 ] Roll a dice.\n" +
                    "[ 3 ] Add money.\n" +
                    "[ 4 ] Add prep.\n" +
                    "[ 5 ] Add morale\n" +
                    "[ 6 ] Add evil.\n" +
                    "[ 7 ] Add gpa.\n" +
                    "[ 8 ] View cards.\n" +
                    "[ 9 ] Show player stats.\n" +
                    "[ x ] Exit game.");

            repeatRequest = true;
            while (repeatRequest) {
                System.out.print("> ");
                String input = scan.next();

                if (input.equals("x")) { // Exit game
                    repeatRequest = false;
                    doGame = false;

                } else if (input.equals("0")) { // End turn
                    repeatRequest = false;
                    newPlayer = true;

                } else if (input.equals("1")) { // Play a card
                    System.out.println("\nYour cards:");
                    System.out.println(players[playerTurn].printHand());
                    System.out.println("What card to play ('x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input) && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= STARTING_HAND_SIZE) {
                            int inputInt = Integer.parseInt(input);
                            Card discarded = players[playerTurn].discardCard(inputInt); // Remove card from player hand
                            System.out.println("You played " + discarded.getName() + ".");
                            deck.discard(discarded); // Put discarded card in discard pile
                            Card newCard = deck.deal();
                            players[playerTurn].addToHand(newCard); // Add new card to user hand.
                            System.out.println("\nYour drew a new card:\n" + newCard);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("2")) { // Roll a dice
                    System.out.println("What size dice to roll (2, 6, etc.)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            Integer inputInt = Math.abs(Integer.parseInt(input));
                            System.out.println("Used a D" + inputInt + ".\nRolled a [ " + roll(inputInt) + " ].");
                            repeatRequest = false;
                        } else {
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("3")) { // Money
                    System.out.println("\n" + players[playerTurn]);
                    System.out.println("How much money to add (use '-' to subtract, 'x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            int inputInt = Integer.parseInt(input);
                            players[playerTurn].addMoney(inputInt);
                            System.out.println("\n" + inputInt + " money was accounted.\n" + players[playerTurn]);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("4")) { // Prep
                    System.out.println("\n" + players[playerTurn]);
                    System.out.println("How much preparation to add (use '-' to subtract, 'x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            int inputInt = Integer.parseInt(input);
                            players[playerTurn].addPreparation(inputInt);
                            System.out.println("\n" + inputInt + " preparation was accounted.\n" + players[playerTurn]);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("5")) { // Morale
                    System.out.println("\n" + players[playerTurn]);
                    System.out.println("How much morale to add (use '-' to subtract, 'x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            int inputInt = Integer.parseInt(input);
                            players[playerTurn].addMorale(inputInt);
                            System.out.println("\n" + inputInt + " morale was accounted.\n" + players[playerTurn]);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("6")) { // Evil
                    System.out.println("\n" + players[playerTurn]);
                    System.out.println("How much evil to add (use '-' to subtract, 'x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            int inputInt = Integer.parseInt(input);
                            players[playerTurn].addEvil(inputInt);
                            System.out.println("\n" + inputInt + " evil was accounted.\n" + players[playerTurn]);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("7")) { // GPA
                    System.out.println("\n" + players[playerTurn]);
                    System.out.println("How much GPA to add (use '-' to subtract, 'x' to go back)?");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (Pattern.matches(fpRegex, input)) {
                            double inputDouble = Double.parseDouble(input);
                            players[playerTurn].addGPA(inputDouble);
                            System.out.println("\n" + inputDouble + " GPA was accounted.\n" + players[playerTurn]);
                            System.out.println("\nPress any key to continue.");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                            }
                            repeatRequest = false;
                        } else if (input.equals("x")) { // Back to menu
                            repeatRequest = false;
                        } else { // Invalid input
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("8")) { // View cards
                    System.out.println("\nWhich cards to look at?" +
                            "\n[ 1 ] Life cards" +
                            "\n[ 2 ] Top of deck" +
                            "\n[ 3 ] Top of discard pile" +
                            "\n[ 4 ] Opponent's hand");
                    while (repeatRequest) {
                        System.out.print("> ");
                        input = scan.next();
                        if (isInteger(input)) {
                            Integer inputInt = Integer.parseInt(input);
                            if (inputInt == 1) { // Life cards
                                System.out.println("\nHow many life cards to view?");
                                while (repeatRequest) {
                                    System.out.print("> ");
                                    input = scan.next();
                                    if (isInteger(input)) {
                                        inputInt = Math.abs(Integer.parseInt(input));
                                        ArrayList<Card> lookup = deck.viewXFromLife(inputInt);
                                        for (int i = 0; i < lookup.size(); i++) {
                                            System.out.println(lookup.get(i));
                                        }
                                        System.out.println("\nPress any key to continue.");
                                        try {
                                            System.in.read();
                                        } catch (Exception e) {
                                        }
                                        repeatRequest = false;
                                    } else {
                                        System.out.println("\nInvalid input. Please try again.");
                                    }
                                }
                            } else if (inputInt == 2) { // View deck
                                System.out.println("\nHow many cards to view from deck?");
                                while (repeatRequest) {
                                    System.out.print("> ");
                                    input = scan.next();
                                    if (isInteger(input)) {
                                        inputInt = Math.abs(Integer.parseInt(input));
                                        ArrayList<Card> lookup = deck.viewXFromDeck(inputInt);
                                        if (lookup.size() < inputInt) {
                                            System.out.println("There weren't enough cards in the deck. Here's what we have:");
                                        }
                                        for (int i = 0; i < lookup.size(); i++) {
                                            System.out.println(lookup.get(i));
                                        }
                                        System.out.println("\nPress any key to continue.");
                                        try {
                                            System.in.read();
                                        } catch (Exception e) {
                                        }
                                        repeatRequest = false;
                                    } else {
                                        System.out.println("\nInvalid input. Please try again.");
                                    }
                                }
                            } else if (inputInt == 3) { // View discard
                                System.out.println("\nHow many cards to view from discard pile?");
                                while (repeatRequest) {
                                    System.out.print("> ");
                                    input = scan.next();
                                    if (isInteger(input)) {
                                        inputInt = Math.abs(Integer.parseInt(input));
                                        ArrayList<Card> lookup = deck.viewXFromDiscard(inputInt);
                                        if (lookup.size() < inputInt) {
                                            System.out.println("There weren't enough cards in the discard pile. Here's what we have:");
                                        }
                                        for (int i = 0; i < lookup.size(); i++) {
                                            System.out.println(lookup.get(i));
                                        }
                                        System.out.println("\nPress any key to continue.");
                                        try {
                                            System.in.read();
                                        } catch (Exception e) {
                                        }
                                        repeatRequest = false;
                                    } else {
                                        System.out.println("\nInvalid input. Please try again.");
                                    }
                                }
                                repeatRequest = false;
                            } else if (inputInt == 4) { // View opponent's hand
                                System.out.println("\nWhich opponent to view?");
                                for (int i = 0; i < players.length; i++) {
                                    System.out.println("[ " + (i+1) + " ] \n" + players[i]);
                                }
                                while (repeatRequest) {
                                    System.out.print("> ");
                                    input = scan.next();
                                    if (isInteger(input)) {
                                        inputInt = Integer.parseInt(input);
                                        if (inputInt > 0 && inputInt <= players.length) {
                                            System.out.println(playerNames[inputInt-1] + "'s cards: \n" + players[inputInt-1].printHand());
                                            System.out.println("\nPress any key to continue.");
                                            try {
                                                System.in.read();
                                            } catch (Exception e) {
                                            }
                                            repeatRequest = false;
                                        } else {
                                            System.out.println("\nInvalid input. Please try again.");
                                        }
                                    } else {
                                        System.out.println("\nInvalid input. Please try again.");
                                    }
                                }
                            } else {
                                System.out.println("\nInvalid input. Please try again.");
                            }
                        } else {
                            System.out.println("\nInvalid input. Please try again.");
                        }
                    }

                } else if (input.equals("9")) { // View players
                    System.out.println("");
                    printPlayers();
                    repeatRequest = false;
                } else { // Error handling
                    System.out.println("\nInvalid input. Please try again.");
                }
            }
            if (newPlayer) {
                playerTurn = ++playerTurn % playerNames.length;
                if (playerTurn == 0) {
                    roundCount++;
                }
            }
        }
    }

    public void printPlayers() {
        for (Player p : players) {
            System.out.println(p);
        }
    }

    public void clearText() {
        System.out.print(String.format("\033[2J"));
    }

    public int roll(int max) {
        Random r = new Random();
        return r.nextInt(max) + 1;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static void main(String args[]) {
        Main m = new Main();
        m.play();
    }
}