package com.fate.game.tetris;

import com.fate.engine.core.Game;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.input.Keyboard;
import com.fate.engine.math.MatrixUtils;
import com.fate.engine.util.Timer;
import com.fate.game.tetris.entities.TetrisWorld;
import com.fate.game.tetris.pieces.Piece;
import com.fate.game.tetris.pieces.PieceFactory;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Fernando on 2/10/2017.
 */
public class TetrisGame extends Game {
    private static final int WIDTH = 20, HEIGHT = 10;
    private static final float TILE_SIZE = 2;
    private static final int WORLD_SIZE = 20;
    private Piece nextPiece;
    private TetrisWorld w;

    private float dropInterval = 1.0f; // Time in seconds that a piece takes to drop one level
    private Timer dropTimer;

    public TetrisGame(int width, int height, float fps, boolean fullscreen) {
        super(width, height, "Tetris", fps, fullscreen);
        this.world = new TetrisWorld(this, WIDTH, HEIGHT, TILE_SIZE);
    }

    public TetrisWorld getWorld() {
        return (TetrisWorld) world;
    }

    @Override
    public void initGame() {
        renderingEngine.setClearColor(new Color(0.1f, 0.1f, 0.1f, 1));
        float ratio = (float) width / (float) height;
        GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));
        w = getWorld();

//        EntityGrid[] pieces = new EntityGrid[7];
//        for (int i = 0; i < 7; i++) {
//            Piece p = PieceFactory.getPiece(i);
//            p.setPosition(2, i * 3);
//            w.addPiece(p);
//        }

        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
        nextPiece.setPosition(5, 5);
        w.addPiece(nextPiece);

        dropTimer = new Timer(dropInterval, () -> {
            if (w.areEmpty(nextPiece.getExposedBlockLocationsWithOffset(0, 1))) {
                w.translatePiece(nextPiece, 0, 1);
            } else {
                System.out.println("Bottom!");
                nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM);
                w.addPiece(nextPiece);
            }
        });
    }

    @Override
    public void preGameUpdate() {

    }

    @Override
    public void fixedGameUpdate(float dt) {
        dropTimer.step(dt);
    }

    @Override
    public void postGameUpdate() {

    }

    @Override
    public void handleInput() {
        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
            w.translatePiece(nextPiece, -1, 0);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
            w.translatePiece(nextPiece, 1, 0);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            w.removePiece(nextPiece);
            nextPiece = nextPiece.rotateLeft();
            w.addPiece(nextPiece);
        }

        if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            w.removePiece(nextPiece);
            nextPiece = nextPiece.rotateRight();
            w.addPiece(nextPiece);
        }
    }
}
