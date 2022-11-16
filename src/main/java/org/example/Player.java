package org.example;

import io.cucumber.java.bs.A;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


public class Player implements Serializable, Runnable{


    @Serial
    private static final long serialVersionUID = 1L;
    public String name;
    public States state;

    AtomicBoolean connected = new AtomicBoolean();
    int score = 0;

    int totalScore = 0;
    Game game = new Game();

    int playerId = 0;
    Client clientConnection;
    Player[] players = new Player[3];

    Dice[] dice = new Dice[8];

    List<Dice> chest = new ArrayList<>();

    Cards card = Cards.GOLD;

    boolean sorcUsed = false;

    public Player getPlayer() {
        return this;
    }

    public void setScore(int score){this.score = score;}

    public static class Dice implements Serializable{
        Faces face;

        Boolean inChest = false;
        public Dice(){
            face = Faces.DIAMOND;
        }

        public Dice(Faces i){
            face = i;
        }


    }

    public void draw(){
        card = Cards.values()[(int) (Math.random() * 11)];
    }

    public boolean isPlayerTurn(){
        return switch (playerId) {
            case 1 -> state == States.PLAYERTURN_1;
            case 2 -> state == States.PLAYERTURN_2;
            case 3 -> state == States.PLAYERTURN_3;
            default -> false;
        };
    }

    public int isDead(boolean firstRoll){
        Dictionary<Faces, Integer> dict = game.countFaces(dice);
        dict = game.handleFortuneCard(dict, card);
        if (dict.get(Faces.SKULL) >= 3){
            if (dict.get(Faces.SKULL) >= 4 && firstRoll){

                System.out.println("You have reached skull island!");
                return 2;
            }
            System.out.println("You have died.");
            return 1;
        }
        else return 0;
    }

    public int scoreDice(){
        return game.scoreDice(dice, card);
    }

    public int scoreDice(List<Player.Dice> dice){
        Dice[] dArr = dice.toArray(new Dice[dice.size()]);

        return game.scoreDice(dArr, card);}


    public void placeInChest(String[] placed){
        if (card != Cards.TREASURE_CHEST){
            System.out.println("You don't have the treasure chest card.");
            return;
        }
        ArrayList<Integer> d = new ArrayList<>();
        for (String s : placed){
            d.add(Integer.parseInt(s) - 1);
        }

        for (int s : d){
            chest.add(dice[s]);
            this.dice[s].inChest = true;
        }

    }

    public void removeFromChest(String[] removed){
        ArrayList<Integer> d = new ArrayList<>();
        for (String s : removed){
            d.add(Integer.parseInt(s) - 1);
        }

        for (int s : d){
            chest.remove(dice[s]);
            dice[s].inChest = false;

        }
    }



    /*
     * ----------Networking Code------------
     */


    //region Client Methods
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void sendStateToServer(States state){
        clientConnection.sendState(state);
    }

    public void connectToClient() {
        clientConnection = new Client();
    }

    public void connectToClient(int port) {
        clientConnection = new Client(port);
    }

    public void killClient() {
        clientConnection.terminate();
    }


    public void initializePlayers() {
        for (int i = 0; i < 3; i++) {
            players[i] = new Player(" ");
        }
    }
    //endregion



    public void startGame() {
        players = clientConnection.receivePlayer();
        System.out.printf("Three players have connected: %s, %s and %s%n", players[0].name, players[1].name, players[2].name);
        System.out.println("The game will now begin.");


        while (true){
            clientConnection.recieveState();
            if (state == States.GAMEOVER) break;
                if (isPlayerTurn()){
                    System.out.println("It is your turn!");
                    this.score = playTurn();
                    if (state != States.SKULL_ISLAND){
                        System.out.printf("You scored %d%n", score);
                        totalScore += score;
                        if (totalScore < 0) totalScore = 0;
                        System.out.println("Your total score is now " + totalScore);
                    }
                    clientConnection.sendState(state);
                    clientConnection.sendScore();

                }
            else{
                if (state == States.PLAYERTURN_1){
                    System.out.printf("%s is currently playing. Please wait...%n", players[0].name);
                }
                else if (state == States.PLAYERTURN_2){
                    System.out.printf("%s is currently playing. Please wait...%n", players[1].name);
                }
                else if (state == States.PLAYERTURN_3){
                    System.out.printf("%s is currently playing. Please wait...%n", players[2].name);
                }
                else if (state == States.SKULL_ISLAND){
                    System.out.println("A player has reached skull island, oh no!");
                    players = clientConnection.receivePlayer();
                    System.out.println("You have received a deduction of " + (totalScore - players[playerId - 1].score));
                    totalScore = players[playerId - 1].score;

                }
            }
        }

        //TODO: temp wins for some reason here, but not in gameserver.
        players = clientConnection.receivePlayer();
        System.out.println("The game is over!");
        Player winner = new Player("temp");
        winner.score = 0;
        for (Player p : players){
            if (p.score > winner.score){
                winner = p;

            }
            System.out.println(String.format("Player %s scored %d", p.name, p.score));
        }
        System.out.println("Player " + winner.name + " has won!");


    }

    public void startGame(boolean test) {
        players = clientConnection.receivePlayer();
        System.out.println(String.format("PLAYER %d: Three players have connected: %s, %s and %s", playerId, players[0].name, players[1].name, players[2].name));
        System.out.println("The game will now begin.");


        while (true){
            clientConnection.recieveState();

            if (state == States.GAMEOVER) break;
            if (isPlayerTurn()){
                System.out.println("It is your turn!");
                this.score = playTurn(true);
                if (state != States.SKULL_ISLAND){
                    System.out.println(String.format("You scored %d", score));
                    totalScore += score;
                    if (totalScore < 0) totalScore = 0;
                    System.out.println("Your total score is now " + totalScore);
                }

                clientConnection.sendState(state);
                clientConnection.sendScore();

            }
            else{
                if (state == States.PLAYERTURN_1){
                    System.out.printf("%s is currently playing. Please wait...%n", players[0].name);
                }
                else if (state == States.PLAYERTURN_2){
                    System.out.printf("%s is currently playing. Please wait...%n", players[1].name);
                }
                else if (state == States.PLAYERTURN_3){
                    System.out.printf("%s is currently playing. Please wait...%n", players[2].name);
                }
                else if (state == States.SKULL_ISLAND){
                    System.out.println("A player has reached skull island, oh no!");
                    int[] scores = clientConnection.receiveScore();
                    System.out.println("You have received a deduction of " + (totalScore - scores[playerId - 1]));
                    totalScore = scores[playerId - 1];

                }
            }
        }

    }

    public int playTurn(){
        Scanner scan = new Scanner(System.in);
        int score = 0;

        //Hardcoded dice if needed.
        //dice = new Dice[] {new Dice(Faces.SKULL), new Dice(Faces.SKULL), new Dice(Faces.SKULL), new Dice(Faces.SKULL), new Dice(Faces.PARROT), new Dice(Faces.PARROT), new Dice(Faces.PARROT), new Dice(Faces.PARROT)};

        dice = game.rollDice(dice);
        draw();

        if (card == Cards.SEA_BATTLE_2 || card == Cards.SEA_BATTLE_3 || card == Cards.SEA_BATTLE_4){
            System.out.println("Avast! Ye have engaged in a sea battle.");
            return game.seaBattle(dice, card);
        }

        System.out.println(String.format("You have rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
        System.out.println(String.format("You have drawn the %s fortune card.", card.name()));
        int dead = isDead(true);
        if (dead == 2){
            //Code for skull island
            sendStateToServer(States.SKULL_ISLAND);
            state = States.SKULL_ISLAND;
            Dictionary<Faces, Integer> dict = game.countFaces(dice);
            dict = game.handleFortuneCard(dict, card);
            boolean rollSkull=true;
            System.out.println("Welcome to Skull Island! You can roll as long as you roll at least 1 more skull each time.");
            System.out.println("Once you don't roll a skull, other players lose 100 points x number of skulls you rolled!");

            int numSkulls = dict.get(Faces.SKULL);
            while (rollSkull){

                System.out.println("So how bout it, would you like to roll? (You currently have " + numSkulls + " skulls. (Y/N)");
                String choice = scan.next();
                switch (choice.toUpperCase()) {
                    case "Y":
                        String[] held = {};
                        dice = game.reRollSelected(dice, held);
                        dict = game.countFaces(dice);
                        dict = game.handleFortuneCard(dict, card);
                        if (numSkulls == dict.get(Faces.SKULL)) {
                            System.out.println("Sorry, you didn't get anymore skulls!");
                            rollSkull = false;
                            break;
                        }
                        System.out.println("You got another skull, you can keep going.");
                        numSkulls = dict.get(Faces.SKULL);
                        break;
                    case "N":
                        System.out.println("Have it your way!");
                        rollSkull = false;
                        break;
                    default:
                        System.out.println("You have to type Y or N to choose, numbskull!");
                }
            }
            System.out.println("You're done here! Other players will lose " + numSkulls * 100 + " points.");
            return numSkulls * -100;
        }
        else if (dead == 1){
            //This code handles if a player would have died on roll 1, but held a Sorc card.
            if (card == Cards.SORCERESS){
                while (true){
                    System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                    String choice = scan.nextLine();
                    if (choice.toUpperCase() == "Y") {

                        for (int i = 0; i <= dice.length; i++){
                            if (dice[i].face == Faces.SKULL){
                                dice[i] = game.useSorcress(dice[i], card);
                                sorcUsed = true;
                                System.out.println("You rerolled a skull into a " + dice[i].face);
                                break;
                            }
                        }

                        if(isDead(false) == 1){
                            System.out.println("Bad luck.. You got another skull and died.");
                            return 0;
                        }
                        else{

                            while (true) {
                                System.out.println("Select an action:");
                                System.out.println("(1) Score with currently held dice.");
                                System.out.println("(2) Choose specific dice to reroll.");
                                int act = scan.nextInt();
                                switch (act){
                                    case 1:
                                        return scoreDice();
                                    case 2:
                                        while (true) {
                                            System.out.println("Select which die you wish to roll: (1,2,4..)");
                                            String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                                            if (die.length <= 1) {
                                                if (die[0] == "0"){
                                                    dice= game.reRollSelected(dice, die);
                                                    break;
                                                }
                                                System.out.println("You must reroll at least two dice.");
                                                continue;
                                            }
                                            else{
                                                dice = game.reRollSelected(dice, die);
                                                break;
                                            }
                                        }

                                        System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                                }

                                return score;
                            }

                        }
                    }
                    else return 0;
                }
            }
            return 0;
        }
        //main loop
        while (true) {
            if (isDead(false) == 1){
                switch (card){
                    case SORCERESS:
                        System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                        String choice = scan.nextLine();
                        if (choice.toUpperCase() == "Y") {
                            for (int i = 0; i <= dice.length; i++){
                                if (dice[i].face == Faces.SKULL){
                                    dice[i] = game.useSorcress(dice[i], card);
                                    System.out.println("You rerolled a skull into a " + dice[i].face);
                                    sorcUsed =true;
                                    break;
                                }
                            }
                            dead = isDead(false);
                            if(dead == 1) {
                                System.out.println("Bad luck.. You got another skull and died.");
                                return 0;
                            }
                        }
                    case TREASURE_CHEST:
                        if (!chest.isEmpty()){
                            System.out.println("But you had dice stored in your chest!");
                            return scoreDice(chest);
                        }
                    default:
                        return 0;
                }
            }
            System.out.println("Select an action:");
            System.out.println("(1) Score with currently held dice.");
            System.out.println("(2) Choose specific dice to reroll.");
            if (card == Cards.SORCERESS && sorcUsed == false){
                System.out.println("(3) Reroll a skull using your Sorceress card.");
            }
            if (card == Cards.TREASURE_CHEST){
                System.out.println("(3) Place di(c)e into your treasure chest for safekeeping.");
                if (!chest.isEmpty()){
                    System.out.println("(4) Remove di(c)e from your treasure chest.");
                }
            }
            int act = Integer.parseInt(scan.next());
            switch (act){
                case 1:
                    return scoreDice();
                case 2:
                    while (true) {
                        System.out.println("Select which dice you wish to roll: (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");

                        if (die.length <= 1) {
                            System.out.println("You must reroll at least two dice.");
                            continue;
                        }
                        else{
                            dice = game.reRollSelected(dice, die);
                            break;
                        }
                    }

                    System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                    break;
                case 3:
                    if (card == Cards.SORCERESS) {
                        for (int i = 0; i <= dice.length; i++) {
                            if (dice[i].face == Faces.SKULL) {
                                dice[i] = game.useSorcress(dice[i], card);
                                System.out.println("You rerolled a skull into a" + dice[i].face);
                                sorcUsed = true;
                                break;
                            }
                        }
                        if (sorcUsed == false) {
                            System.out.println("You do not currently have a skull to reroll.");
                            continue;
                        }
                        break;
                    }
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to place in the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        placeInChest(die);
                    }
                    break;
                case 4:
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to remove from the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        removeFromChest(die);
                    }
                    break;
                default:
                    System.out.println("You must enter one of the above options.");
            }


        }

    }

    //This playTurn function is only for some of the A/U-Tests which wouldn't work with the normal method I've been using
    //This function takes a 2D array of dice, and uses the next set of dice everytime dice are rolled.
    public int playTurn(Dice[][] inDice, Cards card){
        Scanner scan = new Scanner(System.in);
        int score = 0;
        int rollCount = 0;

        dice = game.rollDice(dice);
        dice = inDice[rollCount];
        rollCount++;
        this.card = card;

        int dead = isDead(true);
        if (dead == 2){
            //Code for skull island

            state = States.SKULL_ISLAND;
            Dictionary<Faces, Integer> dict = game.countFaces(dice);
            dict = game.handleFortuneCard(dict, card);
            boolean rollSkull=true;

            int numSkulls = dict.get(Faces.SKULL);
            while (rollSkull){


                String choice = scan.next();
                switch (choice.toUpperCase()) {
                    case "Y":
                        String[] held = {};
                        dice = game.reRollSelected(dice, held);
                        dice = inDice[rollCount];
                        rollCount++;
                        dict = game.countFaces(dice);
                        dict = game.handleFortuneCard(dict, card);
                        if (numSkulls == dict.get(Faces.SKULL)) {
                            rollSkull = false;
                            break;
                        }
                        numSkulls = dict.get(Faces.SKULL);
                        break;
                    case "N":
                        rollSkull = false;
                        break;
                    default:
                }
            }
            if (card == Cards.CAPTAIN) numSkulls = numSkulls*2;
            System.out.println("You're done here! Other players will lose " + numSkulls * 100 + " points.");

            return numSkulls * -100;
        }
        else if (dead == 1){
            //This code handles if a player would have died on roll 1, but held a Sorc card.
            if (card == Cards.SORCERESS){
                while (true){
                    System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                    String choice = scan.nextLine();
                    if (choice.toUpperCase() == "Y") {

                        for (int i = 0; i <= dice.length; i++){
                            if (dice[i].face == Faces.SKULL){
                                dice[i] = game.useSorcress(dice[i], card);
                                sorcUsed = true;
                                System.out.println("You rerolled a skull into a " + dice[i].face);
                                break;
                            }
                        }

                        if(isDead(false) == 1){
                            System.out.println("Bad luck.. You got another skull and died.");
                            return 0;
                        }
                        else{

                            while (true) {
                                System.out.println("Select an action:");
                                System.out.println("(1) Score with currently held dice.");
                                System.out.println("(2) Choose specific dice to reroll.");
                                int act = scan.nextInt();
                                switch (act){
                                    case 1:
                                        return scoreDice();
                                    case 2:
                                        while (true) {
                                            System.out.println("Select which dice you wish to roll: (1,2,4..)");
                                            String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                                            if (die.length <= 1) {
                                                System.out.println("You must reroll at least two dice.");
                                                continue;
                                            }
                                            else{
                                                dice = game.reRollSelected(dice, die);
                                                break;
                                            }
                                        }

                                        System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                                }

                                return score;
                            }

                        }
                    }
                    else return 0;
                }
            }
            return 0;
        }
        //main loop
        while (true) {
            if (isDead(false) == 1){
                switch (card){
                    case SORCERESS:
                        System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                        String choice = scan.nextLine();
                        if (choice.toUpperCase() == "Y") {
                            for (int i = 0; i <= dice.length; i++){
                                if (dice[i].face == Faces.SKULL){
                                    dice[i] = game.useSorcress(dice[i], card);
                                    System.out.println("You rerolled a skull into a " + dice[i].face);
                                    sorcUsed =true;
                                    break;
                                }
                            }
                            dead = isDead(false);
                            if(dead == 1) {
                                System.out.println("Bad luck.. You got another skull and died.");
                                return 0;
                            }
                        }
                    case TREASURE_CHEST:
                        if (!chest.isEmpty()){
                            System.out.println("But you had dice stored in your chest!");
                            return scoreDice(chest);
                        }
                    default:
                        return 0;
                }
            }
            System.out.println("Select an action:");
            System.out.println("(1) Score with currently held dice.");
            System.out.println("(2) Choose specific dice to reroll.");
            if (card == Cards.SORCERESS && sorcUsed == false){
                System.out.println("(3) Reroll a skull using your Sorceress card.");
            }
            if (card == Cards.TREASURE_CHEST){
                System.out.println("(3) Place di(c)e into your treasure chest for safekeeping.");
                if (!chest.isEmpty()){
                    System.out.println("(4) Remove di(c)e from your treasure chest.");
                }
            }
            int act = scan.nextInt();
            switch (act){
                case 1:
                    return scoreDice();
                case 2:
                    while (true) {
                        System.out.println("Select which dice you wish to roll: (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        if (die.length <= 1) {
                            System.out.println("You must reroll at least two dice.");
                            continue;
                        }
                        else{
                            dice = game.reRollSelected(dice, die);
                            dice = inDice[rollCount];
                            rollCount++;
                            break;
                        }
                    }

                    System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                    break;
                case 3:
                    if (card == Cards.SORCERESS) {
                        for (int i = 0; i <= dice.length; i++) {
                            if (dice[i].face == Faces.SKULL) {
                                dice[i] = game.useSorcress(dice[i], card);
                                System.out.println("You rerolled a skull into a" + dice[i].face);
                                sorcUsed = true;
                                break;
                            }
                        }
                        if (sorcUsed == false) {
                            System.out.println("You do not currently have a skull to reroll.");
                            continue;
                        }
                        break;
                    }
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to place in the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        placeInChest(die);
                    }
                    break;
                case 4:
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to remove from the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        removeFromChest(die);
                    }
                    break;
            }


        }

    }

    public int playTurn(Boolean test){
        Scanner scan = new Scanner(System.in);
        int score = 0;
        int count = 1;

        if (card == Cards.SEA_BATTLE_2 || card == Cards.SEA_BATTLE_3 || card == Cards.SEA_BATTLE_4){
            System.out.println("Avast! Ye have engaged in a sea battle.");
            return game.seaBattle(dice, card);
        }

        System.out.println(String.format("You have rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
        System.out.println(String.format("You have drawn the %s fortune card.", card.name()));
        int dead = isDead(true);
        if (dead == 2){
            //Code for skull island
            sendStateToServer(States.SKULL_ISLAND);
            state = States.SKULL_ISLAND;
            Dictionary<Faces, Integer> dict = game.countFaces(dice);
            dict = game.handleFortuneCard(dict, card);
            boolean rollSkull=true;
            System.out.println("Welcome to Skull Island! You can roll as long as you roll at least 1 more skull each time.");
            System.out.println("Once you don't roll a skull, other players lose 100 points x number of skulls you rolled!");

            int numSkulls = dict.get(Faces.SKULL);
            while (rollSkull){

                System.out.println("So how bout it, would you like to roll? (You currently have " + numSkulls + " skulls. (Y/N)");
                String choice = scan.next();
                switch (choice.toUpperCase()) {
                    case "Y":
                        String[] held = {};
                        dice = game.reRollSelected(dice, held);
                        dict = game.countFaces(dice);
                        dict = game.handleFortuneCard(dict, card);
                        if (numSkulls == dict.get(Faces.SKULL)) {
                            System.out.println("Sorry, you didn't get anymore skulls!");
                            rollSkull = false;
                            break;
                        }
                        System.out.println("You got another skull, you can keep going.");
                        numSkulls = dict.get(Faces.SKULL);
                        break;
                    case "N":
                        System.out.println("Have it your way!");
                        rollSkull = false;
                        break;
                    default:
                        System.out.println("You have to type Y or N to choose, numbskull!");
                }
            }
            if (card == Cards.CAPTAIN) {
                System.out.println("You're done here! Other players will lose " + numSkulls * 200 + " points.");
                return numSkulls * -200;
            }
            else{
                System.out.println("You're done here! Other players will lose " + numSkulls * 100 + " points.");
                return numSkulls * -100;
            }
        }
        else if (dead == 1){
            //This code handles if a player would have died on roll 1, but held a Sorc card.
            if (card == Cards.SORCERESS){
                while (true){
                    System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                    String choice = scan.nextLine();
                    if (choice.toUpperCase() == "Y") {

                        for (int i = 0; i <= dice.length; i++){
                            if (dice[i].face == Faces.SKULL){
                                dice[i] = game.useSorcress(dice[i], card);
                                sorcUsed = true;
                                System.out.println("You rerolled a skull into a " + dice[i].face);
                                break;
                            }
                        }

                        if(isDead(false) == 1){
                            System.out.println("Bad luck.. You got another skull and died.");
                            return 0;
                        }
                        else{

                            while (true) {
                                System.out.println("Select an action:");
                                System.out.println("(1) Score with currently held dice.");
                                System.out.println("(2) Choose specific dice to reroll.");
                                int act = scan.nextInt();
                                switch (act){
                                    case 1:
                                        return scoreDice();
                                    case 2:
                                        while (true) {
                                            System.out.println("Select which die you wish to roll: (1,2,4..)");
                                            String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                                            if (die.length <= 1) {
                                                if (die[0] == "0"){
                                                    dice= game.reRollSelected(dice, die);
                                                    break;
                                                }
                                                System.out.println("You must reroll at least two dice.");
                                                continue;
                                            }
                                            else{
                                                dice = game.reRollSelected(dice, die);
                                                break;
                                            }
                                        }

                                        System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                                }

                                return score;
                            }

                        }
                    }
                    else return 0;
                }
            }
            return 0;
        }
        //main loop
        while (true) {
            if (isDead(false) == 1){
                switch (card){
                    case SORCERESS:
                        System.out.println("But you have a Sorceress card! Would you like to use it to reroll a skull? Y/N");
                        String choice = scan.nextLine();
                        if (choice.toUpperCase() == "Y") {
                            for (int i = 0; i <= dice.length; i++){
                                if (dice[i].face == Faces.SKULL){
                                    dice[i] = game.useSorcress(dice[i], card);
                                    System.out.println("You rerolled a skull into a " + dice[i].face);
                                    sorcUsed =true;
                                    break;
                                }
                            }
                            dead = isDead(false);
                            if(dead == 1) {
                                System.out.println("Bad luck.. You got another skull and died.");
                                return 0;
                            }
                        }
                    case TREASURE_CHEST:
                        if (!chest.isEmpty()){
                            System.out.println("But you had dice stored in your chest!");
                            return scoreDice(chest);
                        }
                    default:
                        return 0;
                }
            }
            System.out.println("Select an action:");
            System.out.println("(1) Score with currently held dice.");
            System.out.println("(2) Choose specific dice to reroll.");
            if (card == Cards.SORCERESS && sorcUsed == false){
                System.out.println("(3) Reroll a skull using your Sorceress card.");
            }
            if (card == Cards.TREASURE_CHEST){
                System.out.println("(3) Place di(c)e into your treasure chest for safekeeping.");
                if (!chest.isEmpty()){
                    System.out.println("(4) Remove di(c)e from your treasure chest.");
                }
            }
            int act = Integer.parseInt(scan.next());
            try{
                System.in.reset();
                //System.in.skipNBytes(count*2);
                count+=1;
            } catch (Exception e){
                e.printStackTrace();
            }

            switch (act){
                case 1:
                    return scoreDice();
                case 2:
                    while (true) {
                        System.out.println("Select which dice you wish to roll: (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");

                        if (die.length <= 1) {
                            System.out.println("You must reroll at least two dice.");
                            continue;
                        }
                        else{
                            dice = game.reRollSelected(dice, die);
                            break;
                        }
                    }

                    System.out.println(String.format("You have now rolled %s, %s, %s, %s, %s, %s, %s and %s", dice[0].face, dice[1].face, dice[2].face, dice[3].face, dice[4].face, dice[5].face, dice[6].face, dice[7].face));
                    break;
                case 3:
                    if (card == Cards.SORCERESS) {
                        for (int i = 0; i <= dice.length; i++) {
                            if (dice[i].face == Faces.SKULL) {
                                dice[i] = game.useSorcress(dice[i], card);
                                System.out.println("You rerolled a skull into a" + dice[i].face);
                                sorcUsed = true;
                                break;
                            }
                        }
                        if (sorcUsed == false) {
                            System.out.println("You do not currently have a skull to reroll.");
                            continue;
                        }
                        break;
                    }
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to place in the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        placeInChest(die);
                    }
                    break;
                case 4:
                    if (card == Cards.TREASURE_CHEST){
                        System.out.println("Select which dice you wish to remove from the chest (1,2,4..)");
                        String[] die = (scan.next()).replaceAll("\\s", "").split(",");
                        removeFromChest(die);
                    }
                    break;
                default:
                    System.out.println("You must enter one of the above options.");
            }


        }

    }

    private class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;

        public Client() {
            try {
                socket = new Socket("localhost", Config.GAME_SERVER_PORT_NUMBER);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                connected.set(true);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public Client(int portId) {
            try {
                socket = new Socket("localhost", portId);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }


        public void sendPlayer() {
            try {
                dOut.writeObject(getPlayer());
                dOut.flush();
            } catch (IOException e) {
                System.out.println("Player not sent");
                e.printStackTrace();
            }
        }

        public Player[] receivePlayer() {
            Player[] pl = new Player[3];
            try {
                Player p = (Player) dIn.readObject();
                pl[0] = p;
                p = (Player) dIn.readObject();
                pl[1] = p;
                p = (Player) dIn.readObject();
                pl[2] = p;
                return pl;

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                e.printStackTrace();
            }
            return pl;
        }

        public int[] receiveScore(){
            int[] scores = new int[3];
            try{
                int s = dIn.readInt();
                scores[0] = s;
                s = dIn.readInt();
                scores[1] = s;
                s = dIn.readInt();
                scores[2] = s;
            } catch (Exception e){
                e.printStackTrace();
            }
            return scores;
        }



        /*
         * function to send strings
         */
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("String not sent");
                ex.printStackTrace();
            }
        }

        public void sendState(States state){
            try{
                dOut.writeObject(state);
            }catch (IOException e){
                System.out.println("State could not be sent.");
                e.printStackTrace();
            }
        }

        public void recieveState(){
            try{
                state = (States) dIn.readObject();
            } catch (ClassNotFoundException e){
                System.out.println("Class not found");
                e.printStackTrace();
            } catch (IOException e){
                System.out.println("State not recieved");
                e.printStackTrace();
            }

        }

        public void sendScore(){
            try{
                dOut.writeInt(score);
                dOut.flush();
            }catch (IOException e){
                System.out.println("Could not send score.");
                e.printStackTrace();
            }
        }


        void terminate() {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
        }

    }

    /*
     * ---------Constructor and Main class-----------
     */

    /*
     * constructor takes the name of the player and sets the score to 0
     */
    public Player(String n) {
        name = n;
        for (int i = 0; i < dice.length; i++){
            dice[i] = new Dice();
        }
    }

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();
        Player p = new Player(name);
        p.initializePlayers();
        p.connectToClient();
        p.startGame();
        myObj.close();
    }

    public void run(){
        initializePlayers();
        connectToClient();
        startGame(true);
    }

}
