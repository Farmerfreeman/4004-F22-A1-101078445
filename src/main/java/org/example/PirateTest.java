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
}
