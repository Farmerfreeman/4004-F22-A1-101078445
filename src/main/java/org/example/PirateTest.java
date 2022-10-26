package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.Dictionary;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("A-TEST ROW 45: If a player rolls 3+ skulls, they die and score 0")
    void row45(){
        Player p = new Player("test");
        p.game.rollDice(p.dice);
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
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







}
