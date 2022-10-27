package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.Dictionary;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class PirateTest {

    @Test
    @DisplayName("Dice Roll")
    void rollDice(){
        Player p = new Player("Test");
        p.game.rollDice(p.dice);


        for (Player.Dice die : p.dice){
            for (Faces f : Faces.values()){
                if (f.equals(die.face)){
                    assertEquals(f, die.face);
                }
            }

        }
    }

    @Test
    @DisplayName("ReRoll Dice ")
    void ReRollDice(){
        Player p = new Player("Test");
        p.game.rollDice(p.dice);
        String[] held = {"1", "2", "3", "4", "5"};
        p.game.reRollNotHeld(p.dice, held);

        for (Player.Dice die : p.dice){
            for (Faces f : Faces.values()){
                if (f.equals(die.face)){
                    assertEquals(f, die.face);
                }
            }

        }
    }

    @Test
    @DisplayName("Draw a fortune card")
    void drawCard(){
        Player p = new Player("Test");
        p.draw();

        for (Cards c : Cards.values()){
            if (c.equals(p.card)){
                assertEquals(c, p.card);
            }
        }
    }

    @Test
    @DisplayName("Check if it's players turn or not")
    void isPlayersTurn(){
        Player p = new Player("Test");
        p.playerId = 1;
        p.state = States.PLAYERTURN_2;

        assertEquals(p.isPlayerTurn(), false);
        p.state = States.PLAYERTURN_1;
        assertEquals(p.isPlayerTurn(), true);
    }

    @Test
    @DisplayName("Count the number of each die rolled")
    void countFaces(){
        Player p = new Player("Test");
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        Dictionary<Faces, Integer> dict = p.game.countFaces(p.dice);

        assertEquals(1, dict.get(Faces.SKULL));
        assertEquals(4, dict.get(Faces.PARROT));
        assertEquals(3, dict.get(Faces.SWORD));
    }

    @Test
    @DisplayName("Rolling 4+ skulls on the first roll takes you to skull island.")
    void skullIsland(){
        Player p = new Player("Test");
        p.game.rollDice(p.dice);
        p.draw();
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        //the isdead function returns 2 if the player rolled 4+ skulls on the first roll
        assertEquals(2, p.isDead(true));
    }

    @Test
    @DisplayName("Drawing SEA_BATTLE2 will take you to a 2-sword sea battle.")
    void seaBattle2(){

        Player p = new Player("Test");
        p.game.rollDice(p.dice);
        p.draw();
        p.card = Cards.SEA_BATTLE_2;
        p.dice[0].face = Faces.COIN;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.DIAMOND;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        assertEquals(800 ,p.game.seaBattle(p.dice, p.card, true));

    }


    /*
    ACCEPTANCE TESTS:
    Below are all acceptance tests, starting with row 45
    */
    @Test
    @DisplayName("A-TEST ROW 45")
    void row45(){
        Player p = new Player("test");
        p.draw();
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 46")
    void row46() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2", "3", "4", "5"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SWORD;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);


    }

    @Test
    @DisplayName("A-TEST ROW 47")
    void row47() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2", "3", "4", "5", "6"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SWORD;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 48")
    void row48() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2", "3", "4", "5"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.MONKEY;
        held = new String[]{"1", "2", "3", "4", "5", "6"};
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.MONKEY;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 50")
    void row50() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "4", "5", "6", "7", "8"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;

        held = new String[]{"1", "2", "3", "7", "8"};
        p.game.reRollNotHeld(p.dice, held);
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.COIN;
        int score = p.scoreDice();
        assertEquals(4800, score);
    }

    @Test
    @DisplayName("A-TEST ROW 52")
    void row52() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.CAPTAIN;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.DIAMOND;
        p.dice[5].face = Faces.DIAMOND;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;

        int score = p.scoreDice();
        assertEquals(800, score);
    }

    @Test
    @DisplayName("A-TEST ROW 53")
    void row53() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2","3","4", "5", "6"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.MONKEY;

        int score = p.scoreDice();
        assertEquals(300, score);

    }

    @Test
    @DisplayName("A-TEST ROW 54")
    void row54() {
        Player p = new Player("test");
        p.draw();
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SKULL;


        int score = p.scoreDice();
        assertEquals(300, score);
    }


    @Test
    @DisplayName("A-TEST ROW 55: 3d, 2sk, 1m, 1sw, 1p score 500")
    void row55(){
        Player p = new Player("test");
        p.draw();
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.DIAMOND;
        p.dice[1].face = Faces.DIAMOND;
        p.dice[2].face = Faces.DIAMOND;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.SKULL;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.PARROT;

        int score = p.scoreDice();
        assertEquals(500, score);
    }

    @Test
    @DisplayName("A-TEST ROW 56")
    void row56(){
        Player p = new Player("test");
        p.draw();
        p.card = Cards.DIAMOND;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.COIN;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.SKULL;
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        int score = p.scoreDice();
        assertEquals(700, score);
    }

    @Test
    @DisplayName("A-TEST ROW 57")
    void row57() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SWORD;
        p.dice[1].face = Faces.SWORD;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.SKULL;

        int score = p.scoreDice();
        assertEquals(400, score);
    }

    @Test
    @DisplayName("A-TEST ROW 58")
    void row58() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2", "3", "6", "7", "8"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.SWORD;

        int score = p.scoreDice();
        assertEquals(800, score);
    }

    @Test
    @DisplayName("A-TEST ROW 59")
    void row59() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "2", "3", "6", "7", "8"};
        p.card = Cards.CAPTAIN;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.SWORD;

        int score = p.scoreDice();
        assertEquals(1200, score);
    }

    @Test
    @DisplayName("A-TEST ROW 60")
    void row60() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1", "4", "5", "6", "7", "8"};
        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SWORD;
        held = new String[] {"1", "2", "3", "6", "7", "8"};
        p.game.reRollNotHeld(p.dice, held);
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.MONKEY;

        int score = p.scoreDice();
        assertEquals(600, score);
    }

    @Test
    @DisplayName("A-TEST ROW 62")
    void row62() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.MONKEY;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SKULL;

        int score = p.scoreDice();
        assertEquals(1100, score);
    }

    @Test
    @DisplayName("A-TEST ROW 63")
    void row63() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.PARROT;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.SKULL;

        int score = p.scoreDice();
        assertEquals(2100, score);
    }

    @Test
    @DisplayName("A-TEST ROW 64")
    void row64() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.COIN;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;

        int score = p.scoreDice();
        assertEquals(5400, score);
    }

    @Test
    @DisplayName("A-TEST ROW 65")
    void row65() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.DIAMOND;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.COIN;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;

        int score = p.scoreDice();
        assertEquals(5400, score);
    }

    @Test
    @DisplayName("A-TEST ROW 66")
    void row66() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.CAPTAIN;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SWORD;
        p.dice[1].face = Faces.SWORD;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        int score = p.scoreDice();
        assertEquals(9000, score);
    }

    @Test
    @DisplayName("A-TEST ROW 67")
    void row67() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5", "6"};

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.MONKEY;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.MONKEY;
        int score = p.scoreDice();
        assertEquals(4600, score);
    }

    @Test
    @DisplayName("A-TEST ROW 68")
    void row68() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5", "6"};

        p.card = Cards.DIAMOND;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.DIAMOND;
        p.dice[7].face = Faces.DIAMOND;
        int score = p.scoreDice();
        assertEquals(400, score);
    }

    @Test
    @DisplayName("A-TEST ROW 69")
    void row69() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"3", "4", "5", "6", "7","8" };

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.DIAMOND;
        p.dice[7].face = Faces.PARROT;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[0].face = Faces.DIAMOND;
        p.dice[1].face = Faces.DIAMOND;
        int score = p.scoreDice();
        assertEquals(500, score);
    }

    @Test
    @DisplayName("A-TEST ROW 70")
    void row70() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5"};

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.PARROT;
        int score = p.scoreDice();
        assertEquals(600, score);
    }

    @Test
    @DisplayName("A-TEST ROW 71")
    void row71() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5"};

        p.card = Cards.DIAMOND;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.COIN;
        p.dice[2].face = Faces.COIN;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.PARROT;
        int score = p.scoreDice();
        assertEquals(500, score);
    }

    @Test
    @DisplayName("A-TEST ROW 72")
    void row72() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SKULL;

        int score = p.scoreDice();
        assertEquals(600, score);
    }

    /*
    PART 2
    Below are Misc fortune card and Full Chest Acceptance tests
     */
    @Test
    @DisplayName("A-TEST ROW 77")
    void row77() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5"};

        p.card = Cards.SORCERESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.DIAMOND;
        p.dice[1].face = Faces.DIAMOND;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.MONKEY;
        p.dice[5] = p.game.useSorcress(p.dice[5], p.card);
        p.dice[5].face = Faces.MONKEY;
        int score = p.scoreDice();
        assertEquals(500, score);
    }

    @Test
    @DisplayName("A-TEST ROW 78")
    void row78() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5", "6"};

        p.card = Cards.SORCERESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;
        p.dice[0] = p.game.useSorcress(p.dice[0], p.card);
        p.dice[0].face = Faces.PARROT;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;

        int score = p.scoreDice();
        assertEquals(1000, score);
    }

    @Test
    @DisplayName("A-TEST ROW 79")
    void row79() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "3", "4", "5"};

        p.card = Cards.SORCERESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.MONKEY;
        p.dice[7].face = Faces.MONKEY;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;
        p.dice[5] = p.game.useSorcress(p.dice[5], p.card);
        p.dice[5].face = Faces.PARROT;

        int score = p.scoreDice();
        assertEquals(2000, score);
    }

    @Test
    @DisplayName("A-TEST ROW 82")
    void row82() {
        Player p = new Player("test");
        p.draw();


        p.card = Cards.MONKEY_BUSINESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.COIN;


        int score = p.scoreDice();
        assertEquals(1100, score);
    }

    @Test
    @DisplayName("A-TEST ROW 83")
    void row83() {
        Player p = new Player("test");
        p.draw();
        String[] held = {"1","2", "5", "6", "7", "8"};

        p.card = Cards.MONKEY_BUSINESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.PARROT;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;
        p.game.reRollNotHeld(p.dice, held);
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.PARROT;
        int score = p.scoreDice();
        assertEquals(1700, score);
    }

    @Test
    @DisplayName("A-TEST ROW 84")
    void row84() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.MONKEY_BUSINESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.MONKEY;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.PARROT;
        p.dice[7].face = Faces.PARROT;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 87")
    void row87() {
        Player p = new Player("test");
        p.draw();
        String[] chest = {"6", "7", "8"};
        String[] held = {"1","2", "3","6","7", "8"};


        p.card = Cards.TREASURE_CHEST;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.PARROT;
        p.dice[1].face = Faces.PARROT;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.DIAMOND;
        p.dice[6].face = Faces.DIAMOND;
        p.dice[7].face = Faces.COIN;
        p.placeInChest(chest);
        p.game.reRollNotHeld(p.dice, held);
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.removeFromChest(chest);
        chest = new String[]{"1", "2", "3", "4", "5"};
        p.placeInChest(chest);
        held = new String[]{"1", "2", "3", "4", "5"};
        p.game.reRollNotHeld(p.dice, held);
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.PARROT;



        int score = p.scoreDice();
        assertEquals(1100, score);
    }

    @Test
    @DisplayName("A-TEST ROW 92")
    void row92() {
        Player p = new Player("test");
        p.draw();
        String[] chest = {"6", "7", "8"};
        String[] held = {"1","2", "6","7", "8"};


        p.card = Cards.TREASURE_CHEST;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.PARROT;
        p.dice[4].face = Faces.PARROT;
        p.dice[5].face = Faces.COIN;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;
        p.placeInChest(chest);
        p.game.reRollNotHeld(p.dice, held);
        p.dice[2].face = Faces.DIAMOND;
        p.dice[3].face = Faces.DIAMOND;
        p.dice[4].face = Faces.COIN;
        chest = new String[] {"5"};
        p.placeInChest(chest);
        held = new String[]{"1", "2", "5", "6", "7", "8"};

        p.game.reRollNotHeld(p.dice, held);
        p.dice[2].face = Faces.SKULL;
        p.dice[3].face = Faces.DIAMOND;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice(p.chest);
        assertEquals(600, score);
    }

    @Test
    @DisplayName("A-TEST ROW 97")
    void row97() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.DIAMOND;
        p.dice[7].face = Faces.PARROT;

        int score = p.scoreDice();
        assertEquals(400, score);
    }

    @Test
    @DisplayName("A-TEST ROW 98")
    void row98() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.CAPTAIN;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.COIN;
        p.dice[7].face = Faces.COIN;

        int score = p.scoreDice();
        assertEquals(1800, score);
    }

    @Test
    @DisplayName("A-TEST ROW 99")
    void row99() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.GOLD;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.DIAMOND;

        int score = p.scoreDice();
        assertEquals(1000, score);
    }

    @Test
    @DisplayName("A-TEST ROW 103")
    void row103() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.MONKEY_BUSINESS;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.PARROT;
        p.dice[3].face = Faces.COIN;
        p.dice[4].face = Faces.COIN;
        p.dice[5].face = Faces.DIAMOND;
        p.dice[6].face = Faces.DIAMOND;
        p.dice[7].face = Faces.DIAMOND;

        int score = p.scoreDice();
        assertEquals(1200, score);
    }

    @Test
    @DisplayName("A-TEST ROW 106")
    void row106() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.SKULL_2;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SWORD;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;

        //isDead is a function that takes 1 boolean parameter(firstRoll) and returns 2 if the player reached skull island,
        //1 if the player died, and 0 otherwise. Reaching skull island is only possible when firstRoll is true
        //isDead is used in the normal game loop on every roll to determine if the player is alive or not.
        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 107")
    void row107() {
        Player p = new Player("test");
        p.draw();

        p.card = Cards.SKULL_1;
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SWORD;
        p.dice[3].face = Faces.SWORD;
        p.dice[4].face = Faces.SWORD;
        p.dice[5].face = Faces.SWORD;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.SWORD;


        assertEquals(1, p.isDead(true));
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 108")
    /*This test involves using the playTurn(Dice[][]) function to test skull island functionality
    This means that the interface will be present but the test will feed input to the scanner on its own, no input user is
    necessary. This is because the Skull Island code is not a function in itself, but essentially a block in the playTurn
    function. I also disabled all flavour text to make this more readable when the full suite is run.
     This involves generating all the dice sets for each roll individually beforehand, and then feeding them into the function.*/
    void row108(){
        byte[] in = "Y\nY\nN".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(in);


        Player p = new Player("Test");
        Player.Dice[][] diceset = new Player.Dice[3][8];
        diceset[0] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.PARROT), new Player.Dice(Faces.PARROT), new Player.Dice(Faces.PARROT),
                new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY)};
        diceset[1] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SWORD),
                new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY)};
        diceset[2] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SWORD)};
        System.setIn(input);
        int deduction = p.playTurn(diceset, Cards.SKULL_2);

        assertEquals(-900, deduction);
        assertEquals(0, p.score);
    }

    @Test
    @DisplayName("A-TEST ROW 110")
    /*This test involves using the playTurn(Dice[][]) function to test skull island functionality
    This means that the interface will be present but the test will feed input to the scanner on its own, no input user is
    necessary. This is because the Skull Island code is not a function in itself, but essentially a block in the playTurn
    function. I also disabled all flavour text to make this more readable when the full suite is run.
     This involves generating all the dice sets for each roll individually beforehand, and then feeding them into the function.*/
    void row110(){
        byte[] in = "Y\nN".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(in);


        Player p = new Player("Test");
        Player.Dice[][] diceset = new Player.Dice[2][8];
        diceset[0] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY), new Player.Dice(Faces.MONKEY)};
        diceset[1] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL), new Player.Dice(Faces.COIN)};
        System.setIn(input);
        int deduction = p.playTurn(diceset, Cards.CAPTAIN);

        assertEquals(-1400, deduction);
        assertEquals(0, p.score);
    }

    @Test
    @DisplayName("A-TEST ROW 111")
    /*This test involves using the playTurn(Dice[][]) function to test skull island functionality
    This means that the interface will be present but the test will feed input to the scanner on its own, no input user is
    necessary. This is because the Skull Island code is not a function in itself, but essentially a block in the playTurn
    function.
     This involves generating all the dice sets for each roll individually beforehand, and then feeding them into the function.*/
    void row111(){
        byte[] in = "Y\nN".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(in);


        Player p = new Player("Test");
        Player.Dice[][] diceset = new Player.Dice[2][8];
        diceset[0] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SWORD), new Player.Dice(Faces.SWORD),
                new Player.Dice(Faces.SWORD), new Player.Dice(Faces.SWORD), new Player.Dice(Faces.SWORD)};
        diceset[1] = new Player.Dice[] {new Player.Dice(Faces.SKULL), new Player.Dice(Faces.SKULL),
                new Player.Dice(Faces.SKULL), new Player.Dice(Faces.COIN), new Player.Dice(Faces.COIN),
                new Player.Dice(Faces.COIN), new Player.Dice(Faces.COIN), new Player.Dice(Faces.COIN)};
        System.setIn(input);
        int deduction = p.playTurn(diceset, Cards.SKULL_2);

        assertEquals(-500, deduction);
        assertEquals(0, p.score);
    }

    @Test
    @DisplayName("A-TEST ROW 114")
    void row114(){
        Player p = new Player("Test");
        p.game.rollDice(p.dice);
        p.draw();
        p.card = Cards.SEA_BATTLE_2;
        p.dice[0].face = Faces.MONKEY;
        p.dice[1].face = Faces.MONKEY;
        p.dice[2].face = Faces.MONKEY;
        p.dice[3].face = Faces.MONKEY;
        p.dice[4].face = Faces.SKULL;
        p.dice[5].face = Faces.SKULL;
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.SWORD;

        p.score = p.game.seaBattle(p.dice, p.card, true);
        assertEquals(-300, p.score);
    }


}
