package com.fate.game.shooting.controller;

import com.fate.engine.entities.Component;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.Vector2f;
import com.fate.game.shooting.controller.WalkCommand.Direction;

public class KeyboardController extends Component {
	private MovementCommand jumpCommand;
	private MovementCommand downCommand;
	private MovementCommand leftCommand;
	private MovementCommand rightCommand;
	private int jumpKey, downKey, leftKey, rightKey;
	private float speed;
	private float accel;
	private float reactivity;
	private float jumpHeight;
	
	private Vector2f velocity;
	private Vector2f frameVelocity;
	
	private boolean grounded;
	private boolean moving;
	private boolean stopped;
	
	private KeyCombo currentCombo;
	
	public KeyboardController(float speed, float accel, float reactivity, float jumpHeight, int jumpKey, int downKey, int leftKey, int rightKey) {
		this.speed = speed;
		this.accel = accel;
		this.reactivity = reactivity;
		this.jumpHeight = jumpHeight;
		
		this.jumpKey = jumpKey;
		this.downKey = downKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		
		currentCombo = new KeyCombo(0.2f);
		
//		jumpCommand = new JumpCommand(350f);
		
		jumpCommand = new WalkCommand(Direction.UP, jumpHeight);
		downCommand = new WalkCommand(Direction.DOWN, accel);
		leftCommand = new WalkCommand(Direction.LEFT, accel);
		rightCommand = new WalkCommand(Direction.RIGHT, accel);
		
		velocity = new Vector2f(); // irrelevant
		frameVelocity = new Vector2f();
	}
	
	public KeyboardController clone() {
		return new KeyboardController(speed, accel, reactivity, jumpHeight, jumpKey, downKey, leftKey, rightKey);
	}
	
	public void addVelocity(Vector2f velocity) {
		this.velocity = this.velocity.add(velocity); // irrelevant
	}
	
	public boolean isGrounded() {
		return grounded;
	}
	
	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}
	
	public Vector2f getVelocity() {
		return velocity; // irrelevant
	}
	
	public Vector2f getFrameVelocity() {
		return frameVelocity;
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity; // irrelevant
	}
	
	public void setJumpKey(int key) {
		this.jumpKey = key;
	}
	
	public void setDownKey(int key) {
		this.downKey = key;
	}
	
	public void setLeftKey(int key) {
		this.leftKey = key;
	}
	
	public void setRightKey(int key) {
		this.rightKey = key;
	}

	public int getJumpKey() {
		return jumpKey;
	}

	public int getDownKey() {
		return downKey;
	}

	public int getLeftKey() {
		return leftKey;
	}

	public int getRightKey() {
		return rightKey;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}
	
	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
		((WalkCommand)jumpCommand).setVelocity(jumpHeight);
	}
	
	public float getJumpHeight() {
		return jumpHeight;
	}

	public boolean isMoving() {
		return moving;
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	public float getReactivity() {
		return reactivity;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public MovementCommand getJumpCommand() {
		return jumpCommand;
	}

	public MovementCommand getDownCommand() {
		return downCommand;
	}

	public MovementCommand getLeftCommand() {
		return leftCommand;
	}

	public MovementCommand getRightCommand() {
		return rightCommand;
	}

	public KeyCombo getCurrentCombo() {
		return currentCombo;
	}
}
