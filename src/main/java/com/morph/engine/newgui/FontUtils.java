package com.morph.engine.newgui;

import com.morph.engine.graphics.Texture;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created on 7/24/2017.
 */
public class FontUtils {
    public static Font createFont(String fontName) throws IOException, FontFormatException {
        String filename = "fonts/" + fontName + ".ttf";
        return Font.createFont(Font.TRUETYPE_FONT, FontUtils.class.getClassLoader().getResourceAsStream(filename));
    }

    public static Font createSystemFont(String fontName) {
        return Font.getFont(fontName);
    }
}
