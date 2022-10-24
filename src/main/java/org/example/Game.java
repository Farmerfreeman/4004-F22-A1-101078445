package org.example;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    public int scoreDice(Player.Dice[] dice, Cards card){
        int score = 0;


        Dictionary<Faces, Integer> dict = countFaces(dice);
        dict = handleFortuneCard(dict, card);
        //If the player has 3 or more skulls they are dead and score 0
        if (dict.get(Faces.SKULL) >= 3) return 0;
        //Remove the skulls so they aren't scored like the others.

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
                }
            }
        }
        score += dict.get(Faces.DIAMOND) * 100;
        score += dict.get(Faces.COIN) * 100;

        return score;

    }



    private Dictionary<Faces, Integer> countFaces (Player.Dice[] dice){
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

    private Dictionary<Faces, Integer> handleFortuneCard(Dictionary<Faces, Integer> dict, Cards card){

        switch (card){
            case GOLD: dict.put(Faces.COIN, dict.get(Faces.COIN) + 1);
                break;
            case DIAMOND: dict.put(Faces.DIAMOND, dict.get(Faces.DIAMOND) + 1);
                break;
        }
        return dict;
    }
}
