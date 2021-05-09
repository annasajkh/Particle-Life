package com.github.annasajkh;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Particle
{
	float x, y;
	float vx, vy;
	
    float friction = 0.5f;
    
    ParticleClass particleClass;
	
	public Particle(float x, float y,ParticleClass particleClass)
	{
		this.x = x;
		this.y = y;
		this.particleClass = particleClass;
	}
	
	public static float invSqrt(float x) 
	{
	    final float xhalf = 0.5f * x;
	    
	    int i = Float.floatToIntBits(x);
	    i = 0x5f3759df - (i >> 1);
	    
	    x = Float.intBitsToFloat(i);
	    x *= (1.5f - xhalf * x * x);
	    
	    return x;
	}
	
    public void addForce(Particle other)
    {
    	float length = getDistance(other);
    	float collisionDirX = (x - other.x) / length;
    	float collisionDirY = (y - other.y) / length;
    	
    	if(!intersects(other))
    	{    		
    		vx += collisionDirX * particleClass.attractForce;
    		vy += collisionDirY * particleClass.attractForce;
    	}

    	vx -= collisionDirX * particleClass.repulseForce;
    	vy -= collisionDirY * particleClass.repulseForce;
    	
    	vx *= friction;
    	vy *= friction;
    }
	
	
    public float getDistance(Particle other)
	{
		final float dx = other.x - x;
		final float dy = other.y - y;
		
		return 1 / invSqrt(dx * dx + dy * dy);
	}
    
	public float getDistance2(Particle other)
	{
		final float dx = other.x - x;
		final float dy = other.y - y;
		
		return dx * dx + dy * dy;
	}
	
	public boolean intersects(Particle other)
	{
		final float dx = other.x - x;
		final float dy = other.y - y;
		
		return (Game.particleRadius * 2) * (Game.particleRadius * 2) > (dx * dx) + (dy * dy);
	}
	
	public void applyTangent(Particle other,float dirX, float dirY)
	{
		
		float tangentX = -dirY;
		float tangentY = dirX;

		float dotProductNormal1 = Vector2.dot(vx,vy,dirX,dirY);
		float dotProductNormal2 = Vector2.dot(other.vx,other.vy,dirX,dirY);

		float response = (dotProductNormal1 * (1f - 1f) + 2.0f * 1f * dotProductNormal2) / (1f + 1f);
		float tangentResponse = Vector2.dot(vx,vy,tangentX,tangentY);
		
		vx = tangentX * tangentResponse + dirX * response;
		vy = tangentY * tangentResponse + dirY * response;

		other.vx = (tangentX * tangentResponse + dirX * response);
		other.vy = (tangentY * tangentResponse + dirY * response);
	}
	
	public void resoveCollision(Particle other)
	{
		if(intersects(other))
		{
			
			//length from this particle to other particle
			float length = 1 / invSqrt(getDistance2(other));
			
			//direction from this particle to other particle
			float dirX = (other.x - x) / length;
			float dirY = (other.y - y) / length;
			
			applyTangent(other,dirX,dirY);
			
			float halfCollisionDepth = Math.abs(length - Game.particleRadius - Game.particleRadius) * 0.5f;
			
			//resolve this particle collision
			x -= dirX * halfCollisionDepth;
			y -= dirY * halfCollisionDepth;
			
			//resolve other particle collision
			other.x += dirX * halfCollisionDepth;
			other.y += dirY * halfCollisionDepth;
		}
	}
	
	public void update(float delta)
	{
		x += vx * delta;
		y += vy * delta;
	}
	
	
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.setColor(particleClass.r,particleClass.g,particleClass.b,1);
		shapeRenderer.circle(x, y, Game.particleRadius);
	}
}
