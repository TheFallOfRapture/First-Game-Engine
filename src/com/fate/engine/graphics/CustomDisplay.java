package com.fate.engine.graphics;

import java.awt.Canvas;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.fate.engine.graphics.gui.GUI;

public class CustomDisplay extends Canvas {
	private static final long serialVersionUID = 1L;
	private int[] pixels;
	private BufferedImage image;
	private List<GUI> guis;
	private Color clearColor;
	private JFrame windowReference;
	
	public CustomDisplay(int width, int height) {
		this.guis = new ArrayList<GUI>();
		setSize(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		setIgnoreRepaint(true);
	}
	
	public void render(int x, int y, int color) {
		if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
			pixels[x + y * getWidth()] = color;
		}
	}
	
	public void render(int x, int y, Color color) {
		if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
			Color dst = new Color(pixels[x + y * getWidth()]);
			Color result = color.alphaBlend(dst);
			pixels[x + y * getWidth()] = result.getRGBInteger();
		}
	}
	
	public void render(Fragment f) {
		render(f.getX(), f.getY(), f.getColor());
	}
	
	public void clearScreen() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = clearColor.getARGBInteger();
		}
	}
	
	public void addGUI(GUI gui) {
		guis.add(gui);
	}
	
	/**
	 * Note to self: solution to flickering? Use BufferStrategy, and don't use paint(Graphics)! EVER!
	 * In fact, setIgnoreRepaint(true)!
	 */
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		guis.sort(null);
		for (GUI gui: guis) {
			gui.render(image.getGraphics());
		}
		
		Graphics graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
		
		graphics.dispose();
		bs.show();
		
		clearScreen();
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		System.out.println("Resized! Display Size: " + width + ", " + height);
		System.out.println("Render Size: " + getWidth() + ", " + getHeight());
	}
	
	public boolean isFullscreen() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() != null;
	}
	
	public void setFullscreen() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		DisplayMode displayMode = device.getDisplayMode();
		device.setFullScreenWindow(windowReference);
		setSize(displayMode.getWidth(), displayMode.getHeight());
		System.out.println("New Size: " + displayMode.getWidth() + ", " + displayMode.getHeight() + "; Verify: " + getWidth() + ", " + getHeight());
	}
	
	public void setWindowedSize(int width, int height) {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		if (isFullscreen())
			device.setFullScreenWindow(null);
		
		System.out.println("Size 1: " + width + ", " + height + "; Verify: " + getWidth() + ", " + getHeight());
		
		setSize(width, height);
		System.out.println("Size 2: " + width + ", " + height + "; Verify: " + getWidth() + ", " + getHeight());
	}
	
	public JFrame getEnclosingWindow() {
		return windowReference;
	}
	
	public void setEnclosingWindow(JFrame frame) {
		this.windowReference = frame;
	}
}
