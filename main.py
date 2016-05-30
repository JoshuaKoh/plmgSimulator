from config import *
from func_lib import *
import pprint
import json

pp = pprint.PrettyPrinter(indent = 4)

doGame = True
playerTurn = 0

# Generate the player info for the game
players = []
for i in range(0, PLAYERS):
    pData = {}
    playerName = STARTING_PLAYER_NAMES[i]
    pData = {"_id": (i+1),
    "name": playerName,
    "money": STARTING_MONEY,
    "preparation": STARTING_PREP,
    "morale": STARTING_MORALE,
    "evil": STARTING_EVIL,
    "gpa": STARTING_GPA,
    "hand": []}
    players.append(pData)

# Generate the cards for the game
with open('cards.json') as df:
    cards = json.load(df)
cards = build_dict(cards["cards"], "name")

for i in range(HAND_SIZE):
    players[0]["hand"].append(cards[cards.keys()[i]])

while (doGame):
    playerTurn = playerTurn % PLAYERS

    print "%s's cards:" % players[playerTurn]["name"]
    for card in players[playerTurn]["hand"]:
        tempStr = card["name"] + " (" + card["category"] + "): " + card["text"]
        print tempStr
    print "-----"

    getMoreInput = True
    while (getMoreInput):
        print "=== It is %s's turn. ===" % players[playerTurn]["name"]
        print "Enter an action:"
        print "0 Pass to next player."
        print "1. Play a card."
        print "2. Roll a dice."
        print "3. Add money."
        print "4. Add prep."
        print "5. Add morale"
        print "6. Add evil."
        print "7. Add gpa."
        print "x. Exit game."

        cmd = raw_input("> ")

        # TODO Fix all these
        # TODO Add cards
        if (cmd == "0"):
            getMoreInput = False
        elif (cmd == "1"):
            print "Which card to play?"
            index = 1
            for card in players[playerTurn]["hand"]:
                tempStr = str(index) + ". " + card["name"] + " (" + card["category"] + "): " + card["text"]
                print tempStr
                index += 1
            print "Which card to play?"
            cmd = raw_input("> ")
            if (cmd == "1"):
                del players[playerTurn]["hand"][0]
            #TODO Expand this to cover all cmds and error handle

        elif (cmd == "2"):
            roll = roll6()
            print "You roll a %d" % roll
        elif (cmd == "3"):
            print ""
        elif (cmd == "4"):
            print ""
        elif (cmd == "5"):
            print ""
        elif (cmd == "6"):
            print ""
        elif (cmd == "7"):
            print ""
        elif (cmd == "x"):
            getMoreInput = False
            doGame = False
        else:
            print "Invalid input. Please try again."

    playerTurn += 1