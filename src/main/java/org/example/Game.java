package org.example;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    public int scoreDice(Player.Dice[] dice){
        if (isDead(dice)) return 0;
        return -1;
    }

    private boolean isDead(Player.Dice[] dice){
        Dictionary<Faces, Integer> dict = countFaces(dice);
        System.out.println(dict.get(Faces.DIAMOND));
        if (dict.get(Faces.SKULL) >= 3) return true;
        else return false;
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
}
