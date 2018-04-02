package com.morph.engine.graphics;

import com.morph.engine.math.Vector2f;
import com.morph.engine.util.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.util.stream.IntStream;

/**
 * Created on 7/30/2017.
 */
public class LoadedFont {
    private Texture textureAtlas;
    private static final int BITMAP_WIDTH = 1024, BITMAP_HEIGHT = 1024;
    public static final int SIZE = 64;
    public static final int CHAR_FIRST = 32, CHAR_LAST = 128;
    public static final int CHARSET_RANGE = CHAR_LAST - CHAR_FIRST;
    private String fontName;
    private Integer[][] charIndices;
    private LoadedCharacter[] characters;
    private float scale;
    private float ascent;
    private int yAdvance;
    private int size;

    private float[][] kerningTable;

    public static final String CHARSET;

    static {
        StringBuilder temp = new StringBuilder();
        for (int i = CHAR_FIRST; i < CHAR_LAST; i++) {
            temp.append((char)i);
        }
        CHARSET = temp.toString();
    }

    public LoadedFont(String font) {
        this.fontName = font;
        this.charIndices = new Integer[CHARSET_RANGE][6];
        this.characters = new LoadedCharacter[CHARSET_RANGE];

        ByteBuffer pixels;

        try (STBTTPackContext context = STBTTPackContext.malloc(); STBTTPackedchar.Buffer packedChars = STBTTPackedchar.malloc(CHARSET_RANGE)) {
            pixels = BufferUtils.createByteBuffer(LoadedFont.BITMAP_WIDTH * LoadedFont.BITMAP_HEIGHT);

            ByteBuffer fontBuffer = IOUtils.INSTANCE.getFileAsByteBuffer(font, 160 * 1024);

            STBTruetype.stbtt_PackSetOversampling(context, 2, 2);

            STBTruetype.stbtt_PackBegin(context, pixels, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, 0, 1);
            STBTruetype.stbtt_PackFontRange(context, fontBuffer, 0, LoadedFont.SIZE, 32, packedChars);

            STBTruetype.stbtt_PackEnd(context);

            this.textureAtlas = new Texture(font, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, pixels);
            this.kerningTable = new float[CHARSET_RANGE][CHARSET_RANGE];

            preloadPackedChars(packedChars);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void preloadPackedChars(STBTTPackedchar.Buffer packedChars) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            STBTTFontinfo fontInfo = STBTTFontinfo.mallocStack(stack);
            ByteBuffer fontBuffer = IOUtils.INSTANCE.getFileAsByteBuffer(fontName, 160 * 1024);

            int[] ascent = new int[1], descent = new int[1], lineGap = new int[1];

            STBTruetype.stbtt_InitFont(fontInfo, fontBuffer);

            scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, LoadedFont.SIZE);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

            yAdvance = ascent[0] - descent[0] + lineGap[0];

            this.ascent = ascent[0] * scale;

            for (int i = 0; i < CHARSET_RANGE; i++) {
                char c = CHARSET.charAt(i);

                int[] xAdvance = new int[1], leftSideBearing = new int[1];

                STBTruetype.stbtt_GetPackedQuad(packedChars, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, i, x, y, q, false);
                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, c, xAdvance, leftSideBearing);

                Vector2f[] texCoords = new Vector2f[]{
                        new Vector2f(q.s0(), q.t0()),
                        new Vector2f(q.s1(), q.t0()),
                        new Vector2f(q.s1(), q.t1()),
                        new Vector2f(q.s0(), q.t1())
                };

                int[] indices = IntStream.of(0, 1, 3, 1, 2, 3).map(j -> j * 4).toArray();

                STBTTPackedchar pc = packedChars.get(i);
                float[] offsetData = new float[] {
                        pc.xoff(), pc.yoff(),
                        pc.xoff2(), pc.yoff2()
                };

                characters[c - CHAR_FIRST] = new LoadedCharacter(c, texCoords, indices, (float) xAdvance[0], offsetData);

                CharBuffer.wrap(CHARSET.toCharArray()).chars().forEach(c2 -> kerningTable[c - CHAR_FIRST][c2 - CHAR_FIRST] = STBTruetype.stbtt_GetCodepointKernAdvance(fontInfo, c, c2) * scale);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float kerningLookup(char c1, char c2) {
        if (c1 == '\n' || c2 == '\n') return 0;

        return kerningTable[c1 - 32][c2 - 32];
    }

    public Integer[] getCharData(char c) {
        return charIndices[c - CHAR_FIRST];
    }

    public LoadedCharacter getCharacter(char c) {
        return characters[c - CHAR_FIRST];
    }

    public Texture getTextureAtlas() {
        return textureAtlas;
    }

    public String getFontName() {
        return fontName;
    }

    public float getYAdvance() {
        return yAdvance;
    }

    public void destroy() {
        textureAtlas.destroy();
    }

    public float getScale() {
        return scale;
    }

    public float getAscent() {
        return ascent;
    }

    public static boolean isValidChar(char c) {
        return CHARSET.contains(Character.toString(c));
    }
}
