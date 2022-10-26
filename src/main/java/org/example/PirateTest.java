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
        p.dice[6].face = Faces.SKULL;
        p.dice[7].face = Faces.MONKEY;
        int score = p.scoreDice();
        assertEquals(0, score);
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
    @DisplayName("A-TEST ROW 55: 3d, 2sk, 1m, 1sw, 1p score 500")
    void row5(){
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



}
