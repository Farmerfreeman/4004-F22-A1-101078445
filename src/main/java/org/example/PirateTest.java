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


}
