package com.ktar5.slime.engine.cooldown;

import com.badlogic.gdx.utils.Pool;
import lombok.Getter;

@Getter
public class Cooldown implements Pool.Poolable {
    private long timeStart, timeEnd;
    private Runnable callback;

    public boolean isFinished() {
        if (timeEnd <= System.currentTimeMillis()) {
            if (callback != null) {
                callback.run();
            }
            return true;
        }
        return false;
    }

    public int timeLeft() {
        return (int) (System.currentTimeMillis() - timeEnd);
    }

    public void start(int miliseconds) {
        start(miliseconds, null);
    }

    public void start(int miliseconds, Runnable callback) {
        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + miliseconds;
        this.callback = callback;
    }

    @Override
    public void reset() {

    }

}
