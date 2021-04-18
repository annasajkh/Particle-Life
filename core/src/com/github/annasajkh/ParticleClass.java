package com.github.annasajkh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class ParticleClass
{
    float attractForce = MathUtils.random(500, 1000);
    float repulseForce = MathUtils.random(500, 1000);
    float maxRadius = MathUtils.random() * 200;
    float minRadius = MathUtils.random(maxRadius);
    Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
}
