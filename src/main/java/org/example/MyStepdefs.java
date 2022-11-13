package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.*;

public class MyStepdefs {
    Player p = new Player("Test");
   @Given("player rolls {string}")
    public void player_rolls(String string){
        Player p = new Player("Test");
       p.game.rollDice(p.dice);
       String[] dice = (string).replaceAll("\\s", "").split(",");
        for (int i = 0; i < 8; i++){
            switch (dice[i].toUpperCase()){
                case "SKULL":
                    p.dice[i].face = Faces.SKULL;
                    break;
                case "SWORD":
                    p.dice[i].face = Faces.SWORD;
                    break;
                case "MONKEY":
                    p.dice[i].face = Faces.MONKEY;
                    break;
                case "DIAMOND":
                    p.dice[i].face = Faces.DIAMOND;
                    break;
                case "PARROT":
                    p.dice[i].face = Faces.PARROT;
                    break;
                case "COIN":
                    p.dice[i].face = Faces.COIN;
                    break;
            }
        }

   }

   @Given("player card is {string}")
    public void player_draws(String string){
       p.draw();
       switch (string.toUpperCase()){
           case "SORCERESS":
                p.card = Cards.SORCERESS;
                break;
           case "TREASURE_CHEST":
               p.card = Cards.TREASURE_CHEST;
               break;
           case "CAPTAIN":
               p.card = Cards.CAPTAIN;
               break;
           case "SEA_BATTLE_2":
               p.card = Cards.SEA_BATTLE_2;
               break;
           case "SEA_BATTLE_3":
               p.card = Cards.SEA_BATTLE_3;
               break;
           case "SEA_BATTLE_4":
               p.card = Cards.SEA_BATTLE_4;
               break;
           case "GOLD":
               p.card = Cards.GOLD;
               break;
           case "DIAMOND":
               p.card = Cards.DIAMOND;
               break;
           case "MONKEY_BUSINESS":
               p.card = Cards.MONKEY_BUSINESS;
               break;
           case "SKULL_2":
               p.card = Cards.SKULL_2;
               break;
           case "SKULL_1":
               p.card = Cards.SKULL_1;
               break;
       }
   }

   @When("player scores")
    public void player_scores(){
        p.score = p.scoreDice();
   }

   @Then("player dies")
    public void player_dies(){
       assertEquals(1, p.isDead(false));
   }

   @Then("player score should be {int}")
    public void player_score_is(int expected){
       assertEquals(expected, p.score);
   }

   //Helper functions

}


