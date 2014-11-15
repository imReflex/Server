import java.util.Random;

/**
 * 
 * @author allen
 *
 */

public class Particle {

	private ParticleVector location;
	private ParticleVector velocity;
	private ParticleVector acceleration;
	float lifespan;
	private static Random random;
	
	private ParticleDefinition definition;
	
	Particle(ParticleVector location, ParticleDefinition definition) {
		random = new Random();
		this.location = location;
		this.definition = definition;
		/*acceleration = new ParticleVector(0.00001f, 0.00001f);
		velocity = new ParticleVector(random(0.2, -0.1), random(0.2, -0.1));*/
		acceleration = new ParticleVector(definition.getAccelX(), definition.getAccelY());
		velocity = new ParticleVector(random(definition.getVelocityXMax(), definition.getVelocityXMin()), 
				random(definition.getVelocityYMax(), definition.getVelocityYMin()));
		lifespan = 255;
	}
	
	public static float random(double max , double min) {
	    float val = (float) (random.nextFloat() * max - (-min));
	    return val;
	}
	
	public void cycle() {
		//System.out.println("Particle Life: " + lifespan);
		update();
		render();
	}
	
	public void update() {
		velocity.add(acceleration);
		location.add(velocity);
		//acceleration.scale(0);
		lifespan -= definition.getLifeSpanDec();
	}

	private void render() {
		if(!definition.isFading())
			SpriteCache.get(693).drawAdvancedSprite((int)location.x, (int)location.y);
		else
			SpriteCache.get(693).drawSprite3((int)location.x, (int)location.y, lifespan < 0.0 ? 0 : (int)lifespan);
	}
	
	public void applyForce(ParticleVector force) {
		force.divide(definition.getMass());
		acceleration.add(force);
	}
	
	public boolean isDead() {
		if(lifespan < 0.0)
			return true;
		else
			return false;
	}
	
}
