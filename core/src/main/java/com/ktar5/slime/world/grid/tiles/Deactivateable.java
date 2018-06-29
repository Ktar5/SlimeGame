package com.ktar5.slime.world.grid.tiles;

public interface Deactivateable {
    
    public void activate();
    
    public void deactivate();
    
    public boolean isActivated();
    
    public void setActivated(boolean activated);
    
}
