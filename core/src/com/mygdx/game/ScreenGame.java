/**
 * This class instantiate the game.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Maps.DungeonMaze;
import com.mygdx.game.Maps.DungeonTransition1;
import com.mygdx.game.Maps.FuturisticBlock;
import com.mygdx.game.Maps.FuturisticFinal;
import com.mygdx.game.Maps.FuturisticTransition1;
import com.mygdx.game.Maps.FuturisticTransition2;
import com.mygdx.game.Maps.Introduction;
import com.mygdx.game.Maps.JungleMaze;
import com.mygdx.game.Maps.JungleTransition1;

public class ScreenGame extends Game {
    public void create () {
        this.setScreen(new Introduction(this));
    }

    public void render () {
        super.render();
    }

    public void dispose () {
    }
}