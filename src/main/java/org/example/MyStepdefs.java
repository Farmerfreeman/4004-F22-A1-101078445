package org.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MyStepdefs {
    Player p = new Player("Test");
    Player p2 = new Player("Test 2");
    Player p3 = new Player("Test 3");
    GameServer g = new GameServer();
   @Given("player rolls {string}")
    public void player_rolls(String string){
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

    @Given("player {int} rolls {string}")
    public void player_rolls(int player, String string){
        String[] dice = (string).replaceAll("\\s", "").split(",");
        switch (player){
            case 1:
                setDice(dice, p);
                break;
            case 2:
                setDice(dice, p2);
                break;
            case 3:
                setDice(dice, p3);
                break;
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

    @Given("player {int} card is {string}")
    public void player_draws( int player, String string){
        Cards card = Cards.GOLD;
        switch (string.toUpperCase()){
            case "SORCERESS":
                card = Cards.SORCERESS;
                break;
            case "TREASURE_CHEST":
                card = Cards.TREASURE_CHEST;
                break;
            case "CAPTAIN":
                card = Cards.CAPTAIN;
                break;
            case "SEA_BATTLE_2":
                card = Cards.SEA_BATTLE_2;
                break;
            case "SEA_BATTLE_3":
                card = Cards.SEA_BATTLE_3;
                break;
            case "SEA_BATTLE_4":
                card = Cards.SEA_BATTLE_4;
                break;
            case "GOLD":
                card = Cards.GOLD;
                break;
            case "DIAMOND":
                card = Cards.DIAMOND;
                break;
            case "MONKEY_BUSINESS":
                card = Cards.MONKEY_BUSINESS;
                break;
            case "SKULL_2":
                card = Cards.SKULL_2;
                break;
            case "SKULL_1":
                card = Cards.SKULL_1;
                break;
        }
        switch (player){
            case 1:
                p.card = card;
                break;
            case 2:
                p2.card = card;
                break;
            case 3:
                p3.card = card;
                break;
        }
    }

    @Given("player {int} chooses to roll dice {string}")
    public void player_select_roll(int player, String string){
        byte[] in = "string".getBytes();
        ByteArrayInputStream b = new ByteArrayInputStream(in);
        System.setIn(b);
    }

   @Given("player rerolls {int} to {string} with sorceress")
   public void player_rerolls(int slot, String face){
       switch (face.toUpperCase()){
           case "SKULL":
               p.dice[slot - 1].face = Faces.SKULL;
               break;
           case "SWORD":
               p.dice[slot - 1].face = Faces.SWORD;
               break;
           case "MONKEY":
               p.dice[slot - 1].face = Faces.MONKEY;
               break;
           case "DIAMOND":
               p.dice[slot - 1].face = Faces.DIAMOND;
               break;
           case "PARROT":
               p.dice[slot - 1].face = Faces.PARROT;
               break;
           case "COIN":
               p.dice[slot - 1].face = Faces.COIN;
               break;
       }


   }

   @Given("player places {string} in chest")
   public void place_in_chest(String string){
       String[] dice = (string).replaceAll("\\s", "").split(",");
       p.placeInChest(dice);
   }

   @Given("player removes {string} from chest")
   public void remove_from_chest(String string){
       String[] dice = (string).replaceAll("\\s", "").split(",");
       p.removeFromChest(dice);
   }

   @Given("player engages in a {int} sword sea battle")
   public void sea_battle(int swords){

   }

   @Given("player {int} chooses to roll again")
   public void roll_again(int player){
       byte[] in = "Y".getBytes();
       ByteArrayInputStream b = new ByteArrayInputStream(in);
       System.setIn(b);
   }

   @When("player scores")
    public void player_scores(){
       if (p.card == Cards.TREASURE_CHEST && p.isDead(false) == 1){
           p.score = p.scoreDice(p.chest);
       }
       else{
           p.score = p.scoreDice();
       }
   }

    @When("player {int} scores")
    public void playernum_scores(int player){
        switch (player){
            case 1:
                p.score = p.scoreDice();
                break;
            case 2:
                p2.score = p2.scoreDice();
                break;
            case 3:
                p3.score = p3.scoreDice();
                break;
        }
    }



   @Then("player dies")
    public void player_dies(){
       assertEquals(1, p.isDead(false));
   }

   @Then("player score should be {int}")
    public void player_score_is(int expected){
       assertEquals(expected, p.score);
   }

   @Then("player {int} score should be {int}")
   public void player_score_is_specific(int player, int expected){
       switch (player){
           case 1:
               assertEquals(expected, p.score);
               break;
           case 2:
               assertEquals(expected, p2.score);
               break;
           case 3:
               assertEquals(expected, p3.score);
               break;
       }
   }

   //Networking
    @Before("@Networked")
    public void start_server(){
       try {
           PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("out.txt")), true);
           System.setOut(ps);
       } catch (Exception e){
           e.printStackTrace();
       }

       Thread game = new Thread(g);
       game.start();

    }

    @Before("@Networked")
    public void p1_join(){
       Thread pt1 = new Thread(p);

       try {
           pt1.start();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

    @Before("@Networked")
    public void p2_join(){
        Thread pt2 = new Thread(p2);

        try {
            pt2.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Before("@Networked")
    public void p3_join(){
        Thread pt3 = new Thread(p3);

        try {
            pt3.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Given("player {int} joins the game")
    public void join_game(int player){

       switch (player){
           case 1:
               p = new Player("P1");
               Thread pt1 = new Thread(p);
               pt1.start();
               try {
                   wait(500);
               }catch (Exception e){
                   e.printStackTrace();
               }
               break;
           case 2:

               p2 = new Player("P2");
               Thread pt2 = new Thread(p2);
               pt2.start();
               try {
                   wait(500);
               }catch (Exception e){
                   e.printStackTrace();
               }
               break;
           case 3:

               p3 = new Player("P3");
               Thread pt3 = new Thread(p3);
               pt3.start();
               try {
                   wait(500);
               }catch (Exception e){
                   e.printStackTrace();
               }
               break;
       }
    }

    @Given("WAIT")
    public void wait_test(){
       while(true){
       }
    }

    //Helper Functions
    public void setDice(String[] dice, Player p){
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

}


