/**
 * This class instantiate the game.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Maps.FuturisticTransition1;
import com.mygdx.game.Maps.FuturisticTransition2;

public class ScreenGame extends Game {
    public void create () {
        this.setScreen(new FuturisticTransition2(this, new GameSettings()));
    }

    public void render () {
        super.render();
    }

    public void dispose () {
    }
}