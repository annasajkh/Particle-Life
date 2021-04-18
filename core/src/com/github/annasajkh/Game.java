package com.github.annasajkh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter
{
    static ShapeRenderer shapeRenderer;
    static Particle[] particles;
    static ParticleClass[] particleClasses;

    @Override
    public void create()
    {
        shapeRenderer = new ShapeRenderer();
        particles = new Particle[1000];
        particleClasses = new ParticleClass[5];

        for (int i = 0; i < particleClasses.length; i++)
        {
            particleClasses[i] = new ParticleClass();
        }

        for (int i = 0; i < particles.length; i++)
        {
            particles[i] = new Particle(
                    new Vector2(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight())),
                    particleClasses[MathUtils.random(particleClasses.length - 1)]);
        }

    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        for (Particle particle : particles)
        {
        	for (Particle otherParticle : particles)
        	{
        		if(!particle.equals(otherParticle))
        		{        			
        			float distance = particle.position.dst2(otherParticle.position);
        			
        			if (distance <= particle.particleClass.maxRadius * particle.particleClass.maxRadius && 
        				distance >= particle.particleClass.minRadius * particle.particleClass.minRadius)
        			{
        				particle.addForce(otherParticle);
        			}
        			
                    if(particle.collide(otherParticle))
                    {
						Vector2 resolveDir = otherParticle.position.cpy().sub(particle.position).nor();
						float collisionDepth = Math.abs(particle.position.dst(otherParticle.position) - particle.radius - otherParticle.radius) * 0.5f;
						
						Vector2 tangent = new Vector2(-resolveDir.y,resolveDir.x);
						
						float dotProductNormal1 = particle.velocity.dot(resolveDir);
						float dotProductNormal2 = otherParticle.velocity.dot(resolveDir);
						float response =  (dotProductNormal1 * (particle.mass - otherParticle.mass) + 2.0f * otherParticle.mass * dotProductNormal2) / (particle.mass + otherParticle.mass);
						float tangentResponse = particle.velocity.cpy().dot(tangent);
						
						particle.velocity = tangent.scl(tangentResponse).add(resolveDir.cpy().scl(response));
						particle.position.sub(resolveDir.scl(collisionDepth));
                    }
        		}
        	}
        }
        
        for (Particle particle : particles)
        {
        	particle.update();
        	
        	
        
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Particle particle : particles)
        {
            particle.render(shapeRenderer);
        }
        shapeRenderer.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            create();
        }
    }

    @Override
    public void dispose()
    {
        shapeRenderer.dispose();
    }
}
