package com.ktar5.slime.entities.arrow;

import com.badlogic.gdx.utils.Pool;

public class ArrowPool extends Pool<Arrow> {

    @Override
    protected Arrow newObject() {
        System.out.println("Pool is creating new arrow!");
        return new Arrow();
    }
}
