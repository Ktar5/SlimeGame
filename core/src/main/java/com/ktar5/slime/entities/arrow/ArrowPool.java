package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.utils.Pool;
import org.tinylog.Logger;

public class ArrowPool extends Pool<Arrow> {

    @Override
    protected Arrow newObject() {
        Logger.debug("Pool is creating new arrow!");
        return new Arrow();
    }
}
