package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PirateTest {

    @Test
    @DisplayName("Dice Roll")
    void rollDice(){
        Player p = new Player("Test");
        p.roll("12368");


        for (Player.Dice die : p.dice){
            for (Faces f : Faces.values()){
                if (f.equals(die.face)){
                    assertEquals(f, die.face);
                }
            }

        }
    }

    @Test
    @DisplayName("All Dice Roll")
    void rollAllDice(){
        Player p = new Player("Test");
        p.roll("12345678");


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
    @DisplayName("A-TEST ROW 45: If a player rolls 3+ skulls, they die and score 0")
    void row45(){
        Player p = new Player("test");
        p.roll("12345678");
        p.dice[0].face = Faces.SKULL;
        p.dice[1].face = Faces.SKULL;
        p.dice[2].face = Faces.SKULL;
        int score = p.scoreDice();
        assertEquals(0, score);
    }

    @Test
    @DisplayName("A-TEST ROW 55: 3d, 2sk, 1m, 1sw, 1p score 500")
    void row55(){
        Player p = new Player("test");
        p.roll("12345678");
        p.dice[0].face = Faces.DIAMOND;
        p.dice[1].face = Faces.DIAMOND;
        p.dice[2].face = Faces.DIAMOND;
        p.dice[3].face = Faces.SKULL;
        p.dice[4].face = Faces.SKULL;
        p.dice[5].face = Faces.MONKEY;
        p.dice[6].face = Faces.SWORD;
        p.dice[7].face = Faces.PARROT;
        p.draw();
        p.card = Cards.GOLD;
        int score = p.scoreDice();
        assertEquals(500, score);
    }




}
