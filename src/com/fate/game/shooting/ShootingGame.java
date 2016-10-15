package com.fate.game.shooting;

import com.fate.engine.collision.CollisionEngine;
import com.fate.engine.core.Game;
import com.fate.engine.events.EventListener;
import com.fate.engine.events.ResizeEvent;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.Matrix4f;
import com.fate.engine.math.MatrixUtils;
import com.fate.engine.math.Vector2f;
import com.fate.engine.math.Vector4f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.game.shooting.controller.Action;
import com.fate.game.shooting.entities.Player;
import com.fate.game.shooting.entities.PuzzlePlayer;
import com.fate.game.shooting.graphics.shaders.BasicTexturedShader;
import com.fate.game.shooting.levels.FirstPrototypeLevel;
import com.fate.game.shooting.levels.Level;
import com.fate.game.shooting.levels.PlatformerLevel;
import com.fate.game.shooting.levels.TestEntityLevel;
import com.fate.game.shooting.levels.TestLevel;
import com.fate.game.shooting.levels.TestPuzzleLevel;
import com.fate.game.shooting.systems.BlockInputSystem;
import com.fate.game.shooting.systems.BlockStateSystem;
import com.fate.game.shooting.systems.CollisionSystem;
import com.fate.game.shooting.systems.ControllerSystem;
import com.fate.game.shooting.systems.PhysicsWorld;
import com.fate.game.shooting.systems.PlayerHealthSystem;
import com.fate.game.shooting.systems.PowerUpTriggerSystem;
import com.fate.game.shooting.systems.VelocitySystem;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class ShootingGame extends Game {
	private Player player;
	private PuzzlePlayer puzzlePlayer;
	private PlatformerLevel currentLevel;
	private static final float WORLD_SIZE = 50f;

	public ShootingGame(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Platformer Project: Prototype 1", fps, fullscreen);
	}

	@Override
	public void initGame() {
		renderingEngine.setClearColor(new Color(0.1f, 0.1f, 0.1f, 1));
		float ratio = (float) width / (float) height;
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));

		player = new Player(5, 20, 1, 1, new Color(1, 1, 1), new BasicTexturedShader());
		System.out.println("xd");
//		addEntity(player);

		addEntity(puzzlePlayer = new PuzzlePlayer(33.333f, 50, 3, 3, new BasicTexturedShader()));

//		currentLevel = new TestLevel(this);

//		addEntities(currentLevel.getEntities());

		Level testLevel = new FirstPrototypeLevel();
		Level testPuzzleLevel = new TestPuzzleLevel();

		addEntities(testPuzzleLevel.getEntities());

		addSystem(new ControllerSystem(this));
		addSystem(new PhysicsWorld(this));
		addSystem(new CollisionEngine(this));
		addSystem(new CollisionSystem(this));
		addSystem(new VelocitySystem(this));
		addSystem(new PowerUpTriggerSystem(this));
		addSystem(new PlayerHealthSystem(this));
		addSystem(new BlockInputSystem(this));
		addSystem(new BlockStateSystem(this));
	}

	@EventListener(ResizeEvent.class)
	public void handleResize(ResizeEvent e) {
		int width = e.getWidth();
		int height = e.getHeight();

		glViewport(0, 0, width, height);
		float ratio = (float) width / (float) height;
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));
	}

	@Override
	public void fixedGameUpdate(float dt) {
		Transform2D t2d = puzzlePlayer.getComponent(Transform2D.class);
	}

	// TODO: Remove if working
//	@Override
//	public void render() {
//		// TODO Auto-generated method stub
//		renderingEngine.render(display, entities);
//	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if (Keyboard.isKeyDown(GLFW_KEY_SPACE)) {
			Action.TEST_ACTION.executeIfMatch("Move Up", puzzlePlayer);
		}
	}

	@Override
	public void preGameUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGameUpdate() {
		// TODO Auto-generated method stub

	}

}
