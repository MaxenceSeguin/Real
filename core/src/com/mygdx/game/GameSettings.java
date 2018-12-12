/**
 * This class allows to keep track of the settings of the game throughout the levels.
 * The setting are : the hero state, the sound/music settings.
 */


package com.mygdx.game;

public class GameSettings {

    public Hero hero = null;
    public boolean gameMusic = true;
    public boolean soundEffects = true;

    public GameSettings(){}

    /**
     * This allows to change the hero in the settings.
     * This is basically a setter but we changed the name for better understanding of it use.
     */
    public void refresh(Hero hero){
        this.hero = hero;
    }
}
