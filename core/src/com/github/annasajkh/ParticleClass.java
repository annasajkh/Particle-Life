package com.github.annasajkh;

import com.badlogic.gdx.math.MathUtils;

public class ParticleClass
{
    float attractForce = MathUtils.random(0, 500);
    float repulseForce = MathUtils.random(0, 500);
    float maxRadius = MathUtils.random() * 200;
    float minRadius = MathUtils.random(maxRadius);
    
    float r = MathUtils.random();
    float g = MathUtils.random();
    float b = MathUtils.random();
}
