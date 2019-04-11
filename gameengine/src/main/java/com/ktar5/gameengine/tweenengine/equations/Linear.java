package com.ktar5.gameengine.tweenengine.equations;

import com.ktar5.gameengine.tweenengine.TweenEquation;

/**
 * Easing equation based on Robert Penner's work:
 * http://robertpenner.com/easing/
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public abstract class Linear extends TweenEquation {
	public static final Linear INOUT = new Linear() {
		@Override
		public float compute(float t) {
			return t;
		}

		@Override
		public String toString() {
			return "Linear.INOUT";
		}
	};
}
