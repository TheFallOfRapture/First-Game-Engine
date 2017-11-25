package com.morph.engine.graphics;

import com.morph.engine.graphics.components.TextRenderData;
import com.morph.engine.math.Vector2f;
import com.morph.engine.util.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created on 7/30/2017.
 */
public class LoadedFont {
    private Texture textureAtlas;
    public static final int BITMAP_WIDTH = 1024, BITMAP_HEIGHT = 1024;
    public static final int SIZE = 64;
    private STBTTBakedChar.Buffer chars;
    private STBTTPackedchar.Buffer packedChars;
    private String fontName;
    private HashMap<Character, Integer[]> charIndices;
    private HashMap<Character, LoadedCharacter> characters;
    private float scale;
    private float ascent;
    private int yAdvance;
    private int size;

    private float[][] kerningTable;

    public static final String CHARSET;

    static {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < 96; i++) {
            temp.append((char) (i + 32));
        }
        CHARSET = temp.toString();
    }

    public LoadedFont(String font) {
        this.fontName = font;
        this.packedChars = STBTTPackedchar.malloc(96);
        this.charIndices = new HashMap<>();
        this.characters = new HashMap<>();

        ByteBuffer pixels;

        try (STBTTPackContext context = STBTTPackContext.malloc()) {
            pixels = BufferUtils.createByteBuffer(LoadedFont.BITMAP_WIDTH * LoadedFont.BITMAP_HEIGHT);

            ByteBuffer fontBuffer = IOUtils.getFileAsByteBuffer(font, 160 * 1024);

            STBTruetype.stbtt_PackBegin(context, pixels, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, 0, 1);
            STBTruetype.stbtt_PackFontRange(context, fontBuffer, 0, LoadedFont.SIZE, 32, packedChars);

            STBTruetype.stbtt_PackEnd(context);

            this.textureAtlas = new Texture(font, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, pixels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(CHARSET);

        kerningTable = new float[CHARSET.length()][CHARSET.length()];

        preloadPackedChars();
    }

    public void preloadChars(TextRenderData data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            for (int i = 0; i < CHARSET.length(); i++) {
                char c = CHARSET.charAt(i);
                if (c == '\n') {
                    y.put(0, y.get(0) - LoadedFont.SIZE);
                    x.put(0, 0);
                } else if (c < 32 || c >= 128)
                    continue;

                STBTruetype.stbtt_GetBakedQuad(chars, LoadedFont.BITMAP_WIDTH, LoadedFont.BITMAP_HEIGHT, c - 32, x, y, q, true);

                data.addVertex(new Vertex(new Vector2f(q.x0(), -q.y0()), new Vector2f(q.s0(), q.t0())));
                data.addVertex(new Vertex(new Vector2f(q.x1(), -q.y0()), new Vector2f(q.s1(), q.t0())));
                data.addVertex(new Vertex(new Vector2f(q.x1(), -q.y1()), new Vector2f(q.s1(), q.t1())));
                data.addVertex(new Vertex(new Vector2f(q.x0(), -q.y1()), new Vector2f(q.s0(), q.t1())));

                charIndices.put(c, new Integer[]{
                        0 + (i * 4),
                        1 + (i * 4),
                        3 + (i * 4),
                        1 + (i * 4),
                        2 + (i * 4),
                        3 + (i * 4)
                });
            }
        }
    }

    public void preloadPackedChars() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            STBTTFontinfo fontInfo = STBTTFontinfo.mallocStack(stack);
            ByteBuffer fontBuffer = IOUtils.getFileAsByteBuffer(fontName, 160 * 1024);

            int[] ascent = new int[1], descent = new int[1], lineGap = new int[1];

            STBTruetype.stbtt_InitFont(fontInfo, fontBuffer);

            scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, LoadedFont.SIZE);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascent, descent, lineGap);

            yAdvance = ascent[0] - descent[0] + lineGap[0];

            this.ascent = ascent[0] * scale;

            for (int i = 0; i < CHARSET.length(); i++) {
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

                int[] indices = new int[]{
                        0 + (i * 4),
                        1 + (i * 4),
                        3 + (i * 4),
                        1 + (i * 4),
                        2 + (i * 4),
                        3 + (i * 4)
                };

                STBTTPackedchar pc = packedChars.get(i);
                float[] offsetData = new float[] {
                        pc.xoff(), pc.yoff(),
                        pc.xoff2(), pc.yoff2()
                };

                characters.put(c, new LoadedCharacter(c, texCoords, indices, (float) xAdvance[0], offsetData));

                CharBuffer.wrap(CHARSET.toCharArray()).chars().forEach(c2 -> kerningTable[c - 32][c2 - 32] = STBTruetype.stbtt_GetCodepointKernAdvance(fontInfo, c, c2) * scale);
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
        return charIndices.get(c);
    }

    public LoadedCharacter getCharacter(char c) {
        return characters.get(c);
    }

    public Texture getTextureAtlas() {
        return textureAtlas;
    }

    public String getFontName() {
        return fontName;
    }

    public STBTTBakedChar.Buffer getChars() {
        return chars;
    }

    public float getYAdvance() {
        return yAdvance;
    }

    public void destroy() {
        textureAtlas.destroy();
        packedChars.free();
    }

    public float getScale() {
        return scale;
    }

    public float getAscent() {
        return ascent;
    }
}
