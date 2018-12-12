/**
 * This class instantiate the game.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Maps.Introduction;
import com.mygdx.game.Maps.JungleTransition3;

public class ScreenGame extends Game {
    public void create () {
        this.setScreen(new JungleTransition3(this, new GameSettings()));
    }

    public void render () {
        super.render();
    }

    public void dispose () {
    }
}