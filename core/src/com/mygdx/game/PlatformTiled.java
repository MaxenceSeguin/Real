/**
 * This class holds the coordinates of the two points that platforms have to travel from and to.
 */

package com.mygdx.game;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

public class PlatformTiled {
    private RectangleMapObject platform1;
    private RectangleMapObject platform2;


    public PlatformTiled(RectangleMapObject platform1, RectangleMapObject platform2) {
        this.platform1 = platform1;
        this.platform2 = platform2;
    }

}
