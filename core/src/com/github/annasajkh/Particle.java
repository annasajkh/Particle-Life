package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Particle
{
    Vector2 position;
    Vector2 velocity = new Vector2();
    ParticleClass particleClass;
    float radius = 5;
    float friction = 0.5f;
    float mass = 1;

    public Particle(Vector2 position, ParticleClass particleClass)
    {
        this.position = position;
        this.particleClass = particleClass;
    }
    
    public void addForce(Particle otherParticle)
    {
    	Vector2 collisionDir = otherParticle.position.cpy().sub(position).nor();

    	velocity.add(collisionDir.cpy().scl(particleClass.attractForce));
    	velocity.sub(collisionDir.cpy().scl(particleClass.repulseForce));
    	
    	velocity.scl(friction);
    }


    public void update()
    {
        position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime()));  
        
        if(position.x > Gdx.graphics.getWidth() + radius * 2)
		{
			position.x = -radius * 2;
		}
		else if(position.x < -radius * 2)
		{
			position.x = Gdx.graphics.getWidth() + radius * 2;
		}
		
		if(position.y > Gdx.graphics.getHeight() + radius * 2)
		{
			position.y = -radius * 2;
		}
		else if(position.y < -radius * 2)
		{
			position.y = Gdx.graphics.getHeight() + radius * 2;
		}
    }
    
	public boolean collide(Particle otherParticle)
	{
		float a = radius + otherParticle.radius;
		float dx = position.x - otherParticle.position.x;
		float dy = position.y - otherParticle.position.y;
		
		return a * a > (dx * dx + dy * dy);
	}


    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(particleClass.color);
        shapeRenderer.circle(position.x, position.y, radius);
    }
}
