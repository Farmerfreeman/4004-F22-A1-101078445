package org.example;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    boolean captain = false;


    public Player.Dice[] rollDice(Player.Dice[] dice) {

        for (int i = 0; i < 8; i++) {
            dice[i].face = Faces.values()[(int) (Math.random() * 6 )];
            //System.out.println(dice[i].face);
        }

        return dice;
    }

    public Player.Dice[] rerollDie(Player.Dice[] dieRoll, int i) {
        if (dieRoll[i].face == Faces.SKULL || dieRoll[i].inChest == true){
            return dieRoll;
        }
        dieRoll[i].face = Faces.values()[(int) (Math.random() * 6)];
        //System.out.println(dieRoll[i].face);
        return dieRoll;
    }


    public Player.Dice[] reRollNotHeld(Player.Dice[] dieRoll, String[] held) {
        ArrayList<Integer> rolls = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        for (String s : held) {
            int rem = Integer.parseInt(s) - 1;
            rolls.remove(rolls.indexOf(rem));
        }
        // remove the index from the ones to be rolled
        for (int s : rolls) {

            dieRoll = rerollDie(dieRoll, (s));
        }
        return dieRoll;
    }

    public int scoreDice(Player.Dice[] dice, Cards card){
        int score = 0;



        Dictionary<Faces, Integer> dict = countFaces(dice);
        dict = handleFortuneCard(dict, card);
        //If the player has 3 or more skulls they are dead and score 0
        if (dict.get(Faces.SKULL) >= 3){

            return 0;
        }

        for (Faces f : Faces.values()) {
            if (f == Faces.SKULL) continue;

            if (dict.get(f) >= 3){

                switch (dict.get(f)){
                    case 3: score += 100 ;
                        break;
                    case 4: score += 200;
                        break;
                    case 5: score += 500;
                        break;
                    case 6: score += 1000;
                        break;
                    case 7: score += 2000;
                        break;
                    case 8: score += 4000;
                        break;
                    case 9: score += 4000;
                        break;
                }
            }
        }
        score += dict.get(Faces.DIAMOND) * 100;
        score += dict.get(Faces.COIN) * 100;

        if (isFullChest(dict) && dict.get(Faces.SKULL) == 0 && card != Cards.TREASURE_CHEST){
            score += 500;
        }

        if (captain) score = score*2;
        System.out.println("Player scored " + score);
        captain = false;
        return score;

    }

    public Dictionary<Faces, Integer> countFaces (Player.Dice[] dice){
        Dictionary<Faces, Integer> dict = new Hashtable();
        dict.put(Faces.COIN, 0);
        dict.put(Faces.DIAMOND, 0);
        dict.put(Faces.SKULL, 0);
        dict.put(Faces.SWORD, 0);
        dict.put(Faces.PARROT, 0);
        dict.put(Faces.MONKEY, 0);


        for (Player.Dice d : dice){
            dict.put(d.face, dict.get(d.face) + 1);
        }
        return dict;
    }

    public boolean isFullChest(Dictionary<Faces, Integer> dict){
        if (dict.get(Faces.SKULL) > 0) return false;

        if (dict.get(Faces.SWORD) == 0 || dict.get(Faces.SWORD) >= 3){
            if (dict.get(Faces.MONKEY) == 0 || dict.get(Faces.MONKEY) >= 3){
                if (dict.get(Faces.PARROT) == 0 || dict.get(Faces.PARROT) >= 3){
                    return true;
                }
            }
        }
        return false;
    }

    public Dictionary<Faces, Integer> handleFortuneCard(Dictionary<Faces, Integer> dict, Cards card){

        switch (card){
            case GOLD: dict.put(Faces.COIN, dict.get(Faces.COIN) + 1);
                break;
            case DIAMOND: dict.put(Faces.DIAMOND, dict.get(Faces.DIAMOND) + 1);
                break;
            case SKULL_1: dict.put(Faces.SKULL, dict.get(Faces.SKULL) + 1);
                break;
            case SKULL_2: dict.put(Faces.SKULL, dict.get(Faces.SKULL) + 2);
                break;
            case CAPTAIN: captain = true;
                break;
            case MONKEY_BUSINESS:
                int monkeys = dict.get(Faces.MONKEY);
                dict.put(Faces.PARROT, dict.get(Faces.PARROT) + monkeys);
                dict.put(Faces.MONKEY, 0);
                break;

        }
        return dict;
    }

    public Player.Dice useSorcress(Player.Dice die, Cards card){
        if (card == Cards.SORCERESS) {
            die.face = Faces.values()[(int) (Math.random() * 6)];
        }
        return die;

    }
}
