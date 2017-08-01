package com.morph.engine.graphics;

import java.util.HashMap;

/**
 * Created on 7/31/2017.
 */
public class FontManager {
    private static HashMap<String, FontResource> loadedFonts = new HashMap<>();

    public static FontResource loadFont(String font) {
        if (loadedFonts.containsKey(font)) return loadedFonts.get(font);

        FontResource newFont = new FontResource(new LoadedFont(font));
        loadedFonts.put(font, newFont);
        return newFont;
    }

    public static void unloadFont(String font) {
        if (loadedFonts.get(font).removeReference()) {
            loadedFonts.remove(font);
        }
    }
}
