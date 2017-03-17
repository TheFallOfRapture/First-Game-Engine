package com.morph.engine.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.morph.engine.core.Game;
import com.morph.engine.core.GameSystem;
import com.morph.engine.entities.Component;
import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.math.Vector4f;
import com.morph.engine.physics.components.Transform;

@Deprecated
public class RenderingEngine extends GameSystem {
	private HashMap<Integer, Vertex> vertices;
	private List<Fragment> fragments;
	private List<Triangle> triangles;
	
	private Matrix4f projectionMatrix;
	
	private AtomicInteger vertexIDGenerator;
	
	public RenderingEngine(Game game) {
		super(game);
		vertices = new HashMap<Integer, Vertex>();
		fragments = new ArrayList<Fragment>();
		triangles = new ArrayList<Triangle>();
		vertexIDGenerator = new AtomicInteger(0);
	}
	
	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(RenderData.class, Transform.class);
	}
	
	public void render(CustomDisplay display, List<Entity> entities) {
		display.clearScreen();
		
		triangles.addAll(assembleFaces(entities));
		
		for (Triangle tri : triangles)
			fillTriangle(display, tri);
		
		
		for (Fragment f : fragments) {
			display.render(f);
		}
		
		triangles.clear();
		fragments.clear();
	}
	
	public List<Triangle> assembleFaces(List<Entity> entities) {
		List<Entity> renderEntities = entities.stream().filter(e -> e.hasComponent(RenderData.class) && e.hasComponent(Transform.class))
				.collect(Collectors.toCollection(ArrayList::new));
		
		List<Vertex> verts = new ArrayList<Vertex>();
		for (Entity e : renderEntities) {
			RenderData data = e.getComponent(RenderData.class);
			Transform transform = e.getComponent(Transform.class);
//			for (int i : data.getRenderIndices()) {
//				Vertex v = vertices.get(i);
//				Vector3f transformPos = transform.getTransformationMatrix().mul(new Vector4f(v.getPosition(), 1)).getXYZ();
//				verts.add(new Vertex(transformPos, v.getColor()));
//			}
		}
		
		for (int i = 0; i < verts.size(); i += 3) {
			triangles.add(new Triangle(verts.get(i), verts.get(i + 1), verts.get(i + 2)));
		}
		
		return triangles;
	}
	
	public void fillTriangle(CustomDisplay display, Triangle tri) {
		Vertex a = tri.getA();
		Vertex b = tri.getB();
		Vertex c = tri.getC();
		
		Vector2f v1 = a.getPosition().getXY();
		Vector2f v2 = b.getPosition().getXY();
		Vector2f v3 = c.getPosition().getXY();
		
		int minX = (int) Math.max(Math.floor(Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()))), 0);
		int minY = (int) Math.max(Math.floor(Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()))), 0);
		int maxX = (int) Math.min(Math.ceil(Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()))), display.getWidth());
		int maxY = (int) Math.min(Math.ceil(Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()))), display.getHeight());
		
		for (float y = minY; y <= maxY; y++) {
			for (float x = minX; x <= maxX; x++) {
				Vector2f p = new Vector2f(x, y);
				
				float edge1 = edge(v1, v2, p);
				float edge2 = edge(v2, v3, p);
				float edge3 = edge(v3, v1, p);
				
				boolean inTriangle = edge1 >= 0 && edge2 >= 0 && edge3 >= 0;
				
				if (inTriangle) {
					float totalArea = edge(v1, v2, v3);
					
					float weight1 = edge1 / totalArea;
					float weight2 = edge2 / totalArea;
					float weight3 = edge3 / totalArea;
					
					Color color = c.getColor().scale(weight1).add(a.getColor().scale(weight2)).add(b.getColor().scale(weight3));
					fillPoint(display, (int) Math.floor(x), (int) Math.floor(y), color, 0);
				}
			}
		}
	}
	
	public float edge(Vector2f v0, Vector2f v1, Vector2f p) {
		return (p.getX() - v0.getX())*(v1.getY() - v0.getY()) - (p.getY() - v0.getY())*(v1.getX() - v0.getX());
	}
	
	public void fillPoint(CustomDisplay display, int x, int y, Color color, float z) {
		fragments.add(new Fragment(x, display.getHeight() - y, color, z));
	}
	
	public void fillLine(CustomDisplay display, Vertex a, Vertex b) {
		Vector3f v1 = a.getPosition();
		Vector3f v2 = b.getPosition();
		fillPoint(display, (int) v1.getX(), (int) v1.getY(), a.getColor(), 0);
		
		int dx = (int) (v2.getX() - v1.getX());
		int dy = (int) (v2.getY() - v1.getY());
		
		int curX = (int) v1.getX();
		int curY = (int) v1.getY();
		
		float m = (float) dy / (float) dx;
		
		if (m > 1) {
			
		}
		
		int p0 = (2 * dy) - dx;
		for (int i = 0; i < dx; i++) {
			if (p0 < 0) {
				curX += 1;
				Vector2f pos = new Vector2f(curX, curY);
				float aRatio = 1 - ((pos.sub(v1.getXY()).getLength()) / (v2.getXY().sub(v1.getXY()).getLength()));
				float bRatio = 1 - ((pos.sub(v2.getXY()).getLength()) / (v2.getXY().sub(v1.getXY()).getLength()));
				
				Color c = a.getColor().scale(aRatio).add(b.getColor().scale(bRatio)).clamp();
				
				fillPoint(display, curX, curY, c, 0);
				
				p0 += 2 * dy;
			} else {
				curX += 1;
				curY += 1;
				Vector2f pos = new Vector2f(curX, curY);
				float aRatio = 1 - ((pos.sub(v1.getXY()).getLength()) / (v2.getXY().sub(v1.getXY()).getLength()));
				float bRatio = 1 - ((pos.sub(v2.getXY()).getLength()) / (v2.getXY().sub(v1.getXY()).getLength()));
				
				Color c = a.getColor().scale(aRatio).add(b.getColor().scale(bRatio)).clamp();
				
				fillPoint(display, curX, curY, c, 0);
				
				p0 += (2*dy) - (2*dx);
			}
		}
	}
	
	public void setProjectionMatrix(Matrix4f mat) {
		this.projectionMatrix = mat;
	}

//	@Override
//	public void init(RenderData data) {
//		List<Integer> result = new ArrayList<Integer>();
//		
//		for (int i : data.getIndices()) {
//			Vertex v = data.getVertices().get(i);
//			Vector3f projectedPos = projectionMatrix.mul(new Vector4f(v.getPosition(), 1)).getXYZ();
//			int ID = vertexIDGenerator.getAndIncrement();
//			vertices.put(ID, new Vertex(projectedPos, v.getColor()));
//			result.add(ID);
//		}
//		
////		data.setRenderIndices(result);
//	}

//	@Override
//	public void destroy(RenderData data) {
////		for (int i : data.getRenderIndices()) {
////			this.vertices.remove(i);
////		}
//	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
