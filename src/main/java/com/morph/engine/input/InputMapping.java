package com.morph.engine.input;

import com.morph.engine.core.Game;
import com.morph.engine.util.Pair;
import io.reactivex.Observable;

import java.util.HashMap;

public class InputMapping {
    private HashMap<Integer, Runnable> keyPressed = new HashMap<>();
    private HashMap<Integer, Runnable> keyReleased = new HashMap<>();
    private HashMap<Integer, Runnable> keyTyped = new HashMap<>();

    private HashMap<Integer, Runnable> mousePressed = new HashMap<>();
    private HashMap<Integer, Runnable> mouseReleased = new HashMap<>();

    private HashMap<Integer, Runnable> keyUp = new HashMap<>();
    private HashMap<Integer, Runnable> keyDown = new HashMap<>();

    private HashMap<Integer, Runnable> mouseUp = new HashMap<>();
    private HashMap<Integer, Runnable> mouseDown = new HashMap<>();

    public void link(Game game) {
        Keyboard.getStandardKeyEvents().subscribe(this::acceptStd);
        Mouse.getStandardMouseEvents().subscribe(this::acceptStd);

        Observable.combineLatest(game.getEvents().filter(e -> e == Game.GameAction.UPDATE), Mouse.getBinaryMouseEvents(), Pair::new).subscribe(pair -> acceptLong(pair.getSecond()));
        Observable.combineLatest(game.getEvents().filter(e -> e == Game.GameAction.UPDATE), Keyboard.getBinaryKeyEvents(), Pair::new).subscribe(pair -> acceptLong(pair.getSecond()));
    }

    public void mapButton(int button, Mouse.StdMouseAction mouseAction, Runnable action) {
        getMapByAction(mouseAction).put(button, action);
    }

    public void mapKey(int key, Keyboard.StdKeyAction keyAction, Runnable action) {
        getMapByAction(keyAction).put(key, action);

        if (keyAction == Keyboard.StdKeyAction.REPEAT) keyPressed.put(key, action);
    }

    public void mapButton(int button, Mouse.BinMouseAction mouseAction, Runnable action) {
        if (mouseAction == Mouse.BinMouseAction.UP) mouseUp.put(button, action);
        else mouseDown.put(button, action);
    }

    public void mapKey(int key, Keyboard.BinKeyAction keyAction, Runnable action) {
        if (keyAction == Keyboard.BinKeyAction.UP) keyUp.put(key, action);
        else keyDown.put(key, action);
    }

    private HashMap<Integer, Runnable> getMapByAction(Mouse.StdMouseAction action) {
        switch (action) {
            case PRESS:
                return mousePressed;
            case RELEASE:
                return mouseReleased;
        }
        return null;
    }

    private HashMap<Integer, Runnable> getMapByAction(Keyboard.StdKeyAction action) {
        switch (action) {
            case PRESS:
                return keyPressed;
            case REPEAT:
                return keyTyped;
            case RELEASE:
                return keyReleased;
        }
        return null;
    }

    private HashMap<Integer, Runnable> getMapByLongAction(Mouse.BinMouseAction action) {
        if (action == Mouse.BinMouseAction.UP) return mouseUp;
        return mouseDown;
    }

    private HashMap<Integer, Runnable> getMapByLongAction(Keyboard.BinKeyAction action) {
        if (action == Keyboard.BinKeyAction.UP) return keyUp;
        return keyDown;
    }

    private void acceptStd(Mouse.StdMouseEvent mouseEvent) {
        getMapByAction(mouseEvent.getAction()).getOrDefault(mouseEvent.getButton(), () -> {}).run();
    }

    private void acceptStd(Keyboard.StdKeyEvent keyEvent) {
        switch (keyEvent.getAction()) {
            case PRESS:
                keyPressed.getOrDefault(keyEvent.getKey(), () -> {}).run();
                keyTyped.getOrDefault(keyEvent.getKey(), () -> {}).run();
                break;
            case REPEAT:
                keyTyped.getOrDefault(keyEvent.getKey(), () -> {}).run();
                break;
            case RELEASE:
                keyReleased.getOrDefault(keyEvent.getKey(), () -> {}).run();
                break;
        }
    }

    private void acceptLong(Mouse.BinMouseEvent mouseEvent) {
        getMapByLongAction(mouseEvent.getAction()).getOrDefault(mouseEvent.getButton(), () -> {}).run();
    }

    private void acceptLong(Keyboard.BinKeyEvent keyEvent) {
        getMapByLongAction(keyEvent.getAction()).getOrDefault(keyEvent.getKey(), () -> {}).run();
    }
}
