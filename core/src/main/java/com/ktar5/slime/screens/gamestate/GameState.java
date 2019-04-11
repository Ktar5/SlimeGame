package com.ktar5.slime.screens.gamestate;

import com.ktar5.gameengine.statemachine.State;
import com.ktar5.slime.screens.GameScreen;
import lombok.Getter;

public abstract class GameState extends State<GameState> {
    @Getter
    private final GameScreen gameScreen;

    GameState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public final void changeState(Class<? extends GameState> state) {
        gameScreen.getGameState().changeStateAfterUpdate(state);
    }

    public abstract void resize(int width, int height);

}
