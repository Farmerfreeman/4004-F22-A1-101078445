package org.example;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.*;

import java.io.*;
import java.util.Scanner;

public class MyStepdefs {
    Player p = new Player("p1");
    Player p2 = new Player("p2");
    Player p3 = new Player("p3");
    GameServer g = new GameServer();

    static InputStream in = System.in;

    Thread pt1 = new Thread(p);
    Thread pt2 = new Thread(p2);
    Thread pt3 = new Thread(p3);
    Thread game = new Thread(g);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    String input = "";
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

    @Given("player {int} ends turn")
    public void player_ends_turn(int player){
        switch (player){
            case 1:
                while (p.state == States.PLAYERTURN_1){

                }
                break;
            case 2:
                while (p.state == States.PLAYERTURN_2){

                }
                break;
            case 3:
                while (p.state == States.PLAYERTURN_3){

                }
                break;
        }
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
        //input += "1\n";
    }

    @When("set input {string}")
    public void set_input(String string){
        byte[] b = string.getBytes();
        ByteArrayInputStream s = new ByteArrayInputStream(b);

        System.setIn(s);
    }


    @When("turn ends")
    public void end_turn(){
        try{
            g.closeAll();
        } catch (Exception e){
            e.printStackTrace();
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
                assertEquals(expected, g.players[0].score);
                break;
            case 2:
                assertEquals(expected, g.players[1].score);
                break;
            case 3:
                assertEquals(expected, g.players[2].score);
                break;
        }
    }

    @Then("game ends")
    public void game_ends(){
        assertEquals(States.GAMEOVER, g.state);
    }

    @Then("player {int} won")
    public void player_won(int player){
        boolean check = false;
        try{
            Thread.sleep(500);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (output.toString().contains("Player p" + player + " has won!")) check = true;

        assertEquals(true, check);

    }

    //Networking
    @Before("@Networked")
    public void start_server(){
        try {
            output = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(output);
            System.setOut(ps);
        } catch (Exception e){
            e.printStackTrace();
        }


        game.start();
        while (!g.isRunning){

        }
        //start p1 thread


        try {
            Thread.sleep(200);
            pt1.start();
            while (!p.connected.compareAndSet(true, false));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //wait for p1 to connect




        try {
            pt2.start();
            //wait for p2 to connect
            while (!p2.connected.compareAndSet(true, false));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            pt3.start();
            while (!p3.connected.compareAndSet(true, false));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //wait for p3 to connect



    }

    @Before("@Networked_sp")
    public void start_server_sp() {
        try {
            PrintStream ps = new PrintStream(output);
            System.setOut(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.test = true;
        game.start();

        //start p1 thread
        Thread pt1 = new Thread(p);

        try {
            Thread.sleep(200);
            pt1.start();
            while (!p.connected.compareAndSet(true, false)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("turn {int} ends")
    public void wait_game_end(int player){
        while(!g.gameOver.compareAndSet(true, false)){

        }
    }

    @After("@Networked")
    public void close(){
        try(OutputStream out = new FileOutputStream("Output.txt")){
            output.writeTo(out);
        } catch (Exception e){
            e.printStackTrace();
        }
        p = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
        try {
            g.ss.close();
            g.playerTurn.set(0);
            pt1.stop();
            pt2.stop();
            pt2.stop();
            game.stop();
            Thread.sleep(400);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @After("@Networked_sp")
    public void close_sp(){
        p = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
        try {
            g.ss.close();
            g.playerTurn.set(0);
            pt1.stop();
            pt2.stop();
            pt2.stop();
            game.stop();
            Thread.sleep(200);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Given("WAIT P {int} REROLL {int}")
    public void wait_reroll(int player, int roll){
        switch (player){
            case 1:
                while(!p.roll.compareAndSet(roll, roll));
                break;
            case 2:
                while(!p2.roll.compareAndSet(roll, roll));
                break;
            case 3:
                while(!p3.roll.compareAndSet(roll, roll));
                break;
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

    @When("WAIT GAME END")
    public void wait_game_end(){
        while(!g.gameOver.compareAndSet(true, true)){

        }
    }

    @When("WAIT P {int} TURN")
    public void wait_player_turn(int player){
        switch (player){
            case 1:
                while(!g.playerTurn.compareAndSet(1, 1));
                break;
            case 2:
                while(!g.playerTurn.compareAndSet(2, 2));
                break;
            case 3:
                while(!g.playerTurn.compareAndSet(3, 3));
                break;
        }



    }

    @Then("end test")
    public void end_test(){
        p = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p3");
        try {
            g.ss.close();
            g.playerTurn.set(0);
            pt1.stop();
            pt2.stop();
            pt2.stop();
            game.stop();
        } catch (Exception e){
            e.printStackTrace();
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

