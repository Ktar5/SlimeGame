
package com.ktar5.gameengine.camera.smoothcam;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ktar5.gameengine.tweenengine.Timeline;
import com.ktar5.gameengine.tweenengine.Tween;
import com.ktar5.gameengine.tweenengine.TweenManager;
import com.ktar5.gameengine.tweenengine.equations.Elastic;
import com.ktar5.gameengine.tweenengine.equations.Quad;

public class SmoothCamTest implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SmoothCamSubject player;
	private SmoothCamWorld scw;
	private SmoothCamDebugRenderer scDebug;
	private TweenManager tweenManager;
	private Timeline timeline;
	private float w;
	private float h;
	public boolean isTweening;

	@Override
	public void create () {
		w = Gdx.graphics.getWidth() / 2;
		h = Gdx.graphics.getHeight() / 2;

		isTweening = false;

		camera = new OrthographicCamera(w, h);

		/* Initializing SmoothCam DebugLog Renderer */
		scDebug = new SmoothCamDebugRenderer();

		/* Creating the subject for the SmoothCamWorld */
		player = new SmoothCamSubject();

		/*
		 * Set the velocity radius for the subject. At max velocity, the camera will shift that much in the direction of the
		 * movement.
		 */
		player.setVelocityRadius(30f);

		/*
		 * Set the aiming radius for the subject.
		 */
		player.setAimingRadius(50f);

		/* Creating the SmoothCamWorld with the subject */
		scw = new SmoothCamWorld(player);

		/* Set the bounding box */
		scw.setBoundingBox(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.8f);

		/* Initialize the TweenAccessor and TweenManager */
		Tween.registerAccessor(SmoothCamWorld.class, new SmoothCamAccessor());
		tweenManager = new TweenManager();

		/* Point of interest #1 */
		SmoothCamPoint testpoi = new SmoothCamPoint();
		testpoi.setPosition(0f, -50f);
		testpoi.setInnerRadius(70f);
		testpoi.setOuterRadius(200f);
		testpoi.setPolarity(SmoothCamPoint.ATTRACT);
		scw.addPoint(testpoi);

		/* Point of interest #2 */
		SmoothCamPoint testpoi2 = new SmoothCamPoint();
		testpoi2.setPosition(500f, 100f);
		testpoi2.setInnerRadius(50f);
		testpoi2.setOuterRadius(250f);
		testpoi2.setPolarity(SmoothCamPoint.ATTRACT);
		testpoi2.setZoom(-0.5f);
		scw.addPoint(testpoi2);

		/* Point of interest #3 */
		SmoothCamPoint testpoi3 = new SmoothCamPoint();
		testpoi3.setPosition(-30f, 400f);
		testpoi3.setInnerRadius(100f);
		testpoi3.setOuterRadius(140f);
		testpoi3.setPolarity(SmoothCamPoint.ATTRACT);
		scw.addPoint(testpoi3);

		/* Point of interest #4 */
		SmoothCamPoint testpoi4 = new SmoothCamPoint();
		testpoi4.setPosition(280f, 400f);
		testpoi4.setInnerRadius(60f);
		testpoi4.setOuterRadius(140f);
		testpoi4.setPolarity(SmoothCamPoint.ATTRACT);
		scw.addPoint(testpoi4);

		/* Point of interest #5 */
		SmoothCamPoint testpoi5 = new SmoothCamPoint();
		testpoi5.setPosition(-500f, 300f);
		testpoi5.setInnerRadius(260f);
		testpoi5.setOuterRadius(340f);
		testpoi5.setPolarity(SmoothCamPoint.ATTRACT);
		testpoi5.setZoom(0.5f);
		scw.addPoint(testpoi5);

		/* Point of interest #6 */
		SmoothCamPoint testpoi6 = new SmoothCamPoint();
		testpoi6.setPosition(-400f, -300f);
		testpoi6.setInnerRadius(160f);
		testpoi6.setOuterRadius(210f);
		testpoi6.setPolarity(SmoothCamPoint.REPULSE);
		scw.addPoint(testpoi6);

		batch = new SpriteBatch();

		/*
		 * start tween with "T"-key
		 */
		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown (int key) {
				if (key == Keys.T) {
					isTweening = !isTweening;
					if (isTweening) {
						/*
						 * Example Tween-Sequence: Zoom to 120%, Pan to point of interest #1 (0, -50), Wait 1 second, Pan back to the
						 * starting position, Zoom back to the initial value
						 */
						timeline = Timeline.createSequence()
							.push(Tween.to(scw, SmoothCamAccessor.ZOOM, 0.5f).target(1.2f).ease(Quad.OUT))
							.push(Tween.to(scw, SmoothCamAccessor.PAN, 1.5f).target(0, -50).ease(Elastic.INOUT)).pushPause(1.0f)
							.push(Tween.to(scw, SmoothCamAccessor.PAN, 1.5f).target(scw.getX(), scw.getY()).ease(Elastic.INOUT))
							.push(Tween.to(scw, SmoothCamAccessor.ZOOM, 0.5f).target(scw.getZoom()).ease(Quad.OUT)).start(tweenManager);
					} else {
						tweenManager.killAll();
					}
					return true;
				}
				if (key == Keys.X) {
					if (scw.fixedX) {
						scw.freeFixedX();
					} else {
						scw.setFixedX(scw.getX());
					}
					return true;
				}
				if (key == Keys.Y) {
					if (scw.fixedY) {
						scw.freeFixedY();
					} else {
						scw.setFixedY(scw.getY());
					}
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void render () {

		if (isTweening && timeline.isFinished()) {
			isTweening = false;
		}

		if (!isTweening) {
			if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
				float accelX = Gdx.input.getAccelerometerX();
				float accelY = Gdx.input.getAccelerometerY();
			} else {

				if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.A)) {
					if (Gdx.input.isKeyPressed(Keys.D)) {
						player.setAiming(1, player.getAimingY());
					}
					if (Gdx.input.isKeyPressed(Keys.A)) {
						player.setAiming(-1, player.getAimingY());
					}
				}
				else {
					player.setAiming(0, player.getAimingY());
				}
				if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
					if (Gdx.input.isKeyPressed(Keys.W)) {
						player.setAiming(player.getAimingX(), 1);
					}
					if (Gdx.input.isKeyPressed(Keys.S)) {
						player.setAiming(player.getAimingX(), -1);
					}
				}
				else {
					player.setAiming(player.getAimingX(), 0);
				}
			}
			/*
			 * Updating the position and velocity of the SmoothCamSubject using Box2D. In this example, maximum velocity of the body
			 * is around 122, so we have to divide by that value to get the relative value between -1 and 1 that we need for
			 * SmoothCamWorld. After that, update the SmoothCamWorld.
			 */
			scw.update();
		} else {
			/*
			 * Updating the Tween-Timeline
			 */
			tweenManager.update(Gdx.graphics.getDeltaTime());
		}

		/*
		 * Center the libGDX camera using the coordinates of the SmoothCamWorld
		 */
		camera.position.set(scw.getX(), scw.getY(), 0);
		camera.viewportWidth = w * scw.getZoom();
		camera.viewportHeight = h * scw.getZoom();
		camera.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/* Rendering the debug shapes for the SmoothCamWorld */
		scDebug.render(scw, camera.combined);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

}
