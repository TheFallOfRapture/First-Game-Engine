package com.fate.game.tetris.entities;

import com.fate.engine.core.Game;
import com.fate.engine.core.TileWorld;
import com.fate.engine.entities.Entity;
import com.fate.game.tetris.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 2/11/2017.
 */
public class TetrisWorld extends TileWorld {
    private List<Piece> pieces;

    public TetrisWorld(Game game, int width, int height, float tileSize) {
        super(game, width, height, tileSize);
        this.pieces = new ArrayList<>();
    }

    public boolean addPiece(Piece p) {
        pieces.add(p);
        return addEntityGrid(p, p.getX(), p.getY());
    }

    public boolean removePiece(Piece p) {
        pieces.remove(p);
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getEntity(x, y) != null)
                    if (!removeEntity(x + p.getX(), y + p.getY()))
                        return false;
            }
        }

        return true;
    }

    public boolean translatePiece(Piece p, int dx, int dy) {
        if (!pieces.contains(p))
            return false;

        boolean success = true;
        lazyMoveEntityGrid(p, p.getX() + dx, p.getY() + dy);

        p.translate(dx, dy);

        return success;
    }
}
