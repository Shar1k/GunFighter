package ru.myitschool.satspaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Player {
    String name;
    long frags;

    public Player(String name, long frags) {
        this.name = name;
        this.frags = frags;
    }

    static String tableOfRecordsToString(Player[] players){
        String s = "";
        for (int i = 0; i < players.length; i++) {
            s += players[i].name + "........" + players[i].frags + "\n";
        }
        return s;
    }

    static void sortTableOfRecords(Player[] players){
        /*for (int i = 0; i < players.length; i++) {
            if(players[i].frags == 0) players[i].frags = Long.MAX_VALUE;
        }*/

        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].frags < players[i+1].frags){
                    flag = true;
                    Player z = players[i];
                    players[i] = players[i+1];
                    players[i+1] = z;
                }
            }
        }

        /*for (int i = 0; i < players.length; i++) {
            if(players[i].frags == Long.MAX_VALUE) players[i].frags = 0;
        }*/
    }

    static void saveTableOfRecords(Player[] players){
        try {
            Preferences pref = Gdx.app.getPreferences("TableOfRecords");
            for (int i = 0; i < players.length; i++) {
                pref.putString("name"+i, players[i].name);
                pref.putLong("time"+i, players[i].frags);
            }
            pref.flush();
        } catch (Exception e){
        }
    }

    static void loadTableOfRecords(Player[] players){
        try {
            Preferences pref = Gdx.app.getPreferences("TableOfRecords");
            for (int i = 0; i < players.length; i++) {
                if(pref.contains("name"+i)) {
                    players[i].name = pref.getString("name"+i, "null");
                }
                if(pref.contains("time"+i)) {
                    players[i].frags = pref.getLong("time"+i, 0);
                }
            }
        } catch (Exception e){
        }
    }
}
