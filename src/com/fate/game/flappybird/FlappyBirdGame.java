package com.fate.game.flappybird;

import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.*;

import com.fate.engine.collision.CollisionEngine;
import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.collision.components.CollisionComponent;
import com.fate.engine.core.Game;
import com.fate.engine.core.OpenGame;
import com.fate.engine.entities.Entity;
import com.fate.engine.entities.EntityRectangle;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.MatrixUtils;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.RigidBody;
import com.fate.engine.physics.components.Transform2D;

public class FlappyBirdGame extends OpenGame {
	private final float timeBetweenObstacles = 1.5f; // Time in seconds between obstacles.
	private int obstacleSpace = 110;
	private int score;
	private float previousObstacleSpeed;
	private float obstacleSpeed;
	private float obstacleTimeAccumulator;
	private boolean hasLost;
	
	private BoundingBox2D gameBoundingBox;
	private Player player;
	private ScoreGUI fbGUI;
	
	private boolean isPaused;
	
	public FlappyBirdGame(int width, int height, float fps, boolean fullscreen) {
		super(width, height, "Flappy Bird", fps, fullscreen);
	}
	
	public void initGame() {
 		gameBoundingBox = new BoundingBox2D(new Vector2f(width / 2.0f, height / 2.0f), new Vector2f(width / 2.0f, height / 2.0f));
		
		obstacleTimeAccumulator = 0;
		hasLost = false;
		score = 0;
		obstacleSpeed = 250f;
		
		// Yes, I know the syntax is weird. I saw it somewhere and it's really useful, so why not?
		addEntity(player = new Player((width / 4) - 25, 500, 30, 30, new Color(1, 1, 0, 1), 1.0f, new BasicShader()));
		
		fbGUI = new ScoreGUI(this, 0);
		EventDispatcher.INSTANCE.addEventHandler(this);
		
		renderingEngine.setClearColor(0.733f, 0.733f, 1f, 0f);
		GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(height, 0, 0, width, -1, 1));
		
		addSystem(new CollisionEngine(this));
		addSystem(new ObstacleCollisionSystem(this));
		addSystem(new ScoreDetectorTriggerSystem(this));
	}
	
	public void generateObstaclePair(float space) {
		float obsWidth = 40;
		float obsHeight = (int) (Math.random() * 351) + 50;
		float obsHeight2 = this.height - obsHeight - space;
		// if height = 400 and space = 50, top pipe height = 400 and bottom pipe height = 150. bottom pipe starts at 450.
		Obstacle ob1 = new Obstacle(this.width + 50, obsHeight / 2f, obsWidth, obsHeight, new BasicShader());
		ScoreDetector sc = new ScoreDetector(this, this.width + 50, obsHeight + (space / 2f), obsWidth, space, new BasicShader());
		Obstacle ob2 = new Obstacle(this.width + 50, obsHeight + space + (obsHeight2 / 2f), obsWidth, obsHeight2, new BasicShader());
		
		addEntity(ob1);
		addEntity(ob2);
		addEntity(sc);
	}
	
	@Override
	public void handleInput() {
		if (Keyboard.isKeyPressed(GLFW_KEY_SPACE) && !hasLost && !isPaused)
			player.bounce(300, dt);
		
		if (Keyboard.isKeyPressed(GLFW_KEY_ENTER) && hasLost)
			restart();
		
		if (Keyboard.isKeyPressed(GLFW_KEY_ESCAPE)) {
			if (isPaused)
				unpause();
			else
				pause();
		}
	}
	
	@Override
	public void fixedGameUpdate(float dt) {
		if (!hasLost && !isPaused)
			generateObstacles();
		
		if (!isPaused) {
			physicsEngine.update(this, getEntities(), dt);
		}
		
		if (!hasLost) {
			Iterator<Entity> eIterator = getEntities().iterator();
			while (eIterator.hasNext()) {
				Entity e = eIterator.next();
				if (e instanceof Obstacle || e instanceof ScoreDetector) {
					EntityRectangle rect = (EntityRectangle) e;
					Transform2D t2D = rect.getComponent(Transform2D.class);
					t2D.translate(new Vector2f(-obstacleSpeed * dt, 0));
				}
				// Remove invisible objects.
				Transform2D t2D = e.getComponent(Transform2D.class);
				if (t2D != null && (t2D.getPosition().getX() < -200 || t2D.getPosition().getX() >= width + 200)) {
					e.destroy(); 
					eIterator.remove();
				}
			}
			
			checkLoss();
		}
	}

	// TODO: Remove if working
//	@Override
//	public void render() {
//		// Render the scene to the display.
//		renderingEngine.render(display, entities);
//	}
	
	public void pause() {
		isPaused = true;
		previousObstacleSpeed = obstacleSpeed;
		obstacleSpeed = 0;
	}
	
	public void unpause() {
		isPaused = false;
		obstacleSpeed = previousObstacleSpeed;
	}
	
	public void restart() {
		Transform2D playerTransform = player.getComponent(Transform2D.class);
		playerTransform.setPosition(new Vector2f((width / 4) - 25, 500));
		player.getComponent(BoundingBox2D.class).setCenter(new Vector2f((width / 4) - 25, 500));
		player.getComponent(RigidBody.class).clear();
		
		obstacleTimeAccumulator = 0;
		score = 0;
		
		obstacleSpeed = 250f;
		
		Iterator<Entity> eIterator = getEntities().iterator();
		while (eIterator.hasNext()) {
			Entity e = eIterator.next();
			if (e instanceof Obstacle || e instanceof ScoreDetector) {
				e.destroy();
				eIterator.remove();
			}
		}
		
		hasLost = false;
	}

	public void generateObstacles() {
		obstacleTimeAccumulator += dt;
		if (obstacleTimeAccumulator >= timeBetweenObstacles) {
			generateObstaclePair(obstacleSpace);
			obstacleTimeAccumulator = 0;
		}
	}
	
	public void checkLoss() {
		if (!hasLost && !gameBoundingBox.encloses(player.getComponent(BoundingBox2D.class))) {
			envokeLoss();
		}
		
		if (hasLost) {
			obstacleSpeed = 0;
			System.out.println("u lost lol");
		}
	}
	
	public void incrementScore() {
		if (!hasLost) score++;
		System.out.println(score);
	}
	
	public void envokeLoss() {
		hasLost = true;
	}
	
	public int getScore() {
		return score;
	}

	public boolean hasLost() {
		return hasLost;
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
