package com.ktar5.gameengine.tweenengine;

import com.ktar5.gameengine.tweenengine.paths.CatmullRom;
import com.ktar5.gameengine.tweenengine.paths.Linear;

/**
 * Collection of built-in paths.
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public interface TweenPaths {
	public static final Linear linear = new Linear();
	public static final CatmullRom catmullRom = new CatmullRom();
}
