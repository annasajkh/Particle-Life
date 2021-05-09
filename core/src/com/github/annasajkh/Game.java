package com.github.annasajkh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Game extends ApplicationAdapter
{
	Particle[] particles;
	ParticleClass[] particleClasses;
	
	ShapeRenderer shapeRenderer;
	
	
	float width, height;
	
	public static float particleRadius = 4;

	@Override
	public void create()
	{
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		particles = new Particle[2000];
		particleClasses = new ParticleClass[5];
		
		shapeRenderer = new ShapeRenderer();
		
		for(int i = 0; i < particleClasses.length; i++)
		{
			particleClasses[i] = new ParticleClass();
		}
		
		for(int i = 0; i < particles.length; i++)
		{
			particles[i] = new Particle(MathUtils.random(particleRadius, Gdx.graphics.getWidth() - particleRadius),
										MathUtils.random(particleRadius, Gdx.graphics.getHeight() - particleRadius),
										particleClasses[MathUtils.random(particleClasses.length - 1)]);
		}
	}
	
	public void getInput()
	{
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			create();
		}
	}
	
	public void update()
	{
		getInput();
		
		for(Particle particle : particles)
		{
			for(Particle otherParticle : particles)
			{
				if(!particle.equals(otherParticle))
				{
					float distance2 = particle.getDistance2(otherParticle);
        			
        			if (distance2 <= particle.particleClass.maxRadius * particle.particleClass.maxRadius && 
        				distance2 >= particle.particleClass.minRadius * particle.particleClass.minRadius)
        			{
        				particle.addForce(otherParticle);
        			}
				}
			}
		}
		
		for(Particle particle : particles)
		{
			particle.update(Gdx.graphics.getDeltaTime());
		}
		for(Particle particle : particles)
		{
			for(Particle otherParticle : particles)
			{
				if(!particle.equals(otherParticle))
				{
					particle.resoveCollision(otherParticle);
				}
			}
			
			//make sure it doesn't go out of bounds
			if(particle.x < -particleRadius)
			{
				particle.x = width + particleRadius;
			}
			else if(particle.x > width + particleRadius)
			{
				particle.x =  -particleRadius;
			}
			else if(particle.y < -particleRadius)
			{
				particle.y = height + particleRadius;
			}
			else if(particle.y > height + particleRadius)
			{
				particle.y =  height + particleRadius;
			}
		}
	}
	
	@Override
	public void render()
	{
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		for(Particle particle : particles)
		{
			particle.draw(shapeRenderer);
		
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
	}
}