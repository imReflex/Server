import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author allen
 *
 */

public class ParticleEmitter {

	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	private ParticleVector origin;
	
	/**
	 * These are used to make the emitter not look so cluttered in one x,y coord!
	 */
	private int rxOffset;
	private int ryOffset;
	
	private ParticleDefinition definition;
	
	public ParticleEmitter(int x, int y, int total) {
		this(x,y,total,0,0, new ParticleDefinition(0.001f, 0.001f, 2.0f, 1.0f, 1.5, -1.0, 1.5, -1.0, true));
	}
	
	public ParticleEmitter(int x, int y, int total, int rxOffset, int ryOffset, ParticleDefinition definition) {
		this.rxOffset = rxOffset;
		this.ryOffset = ryOffset;
		this.definition = definition;
		origin = new ParticleVector(x,y);
		for(int i = 0; i < total; i++) {
			particleList.add(new Particle(origin.get(), definition));
		}
	}
	
	public void cycle() {
		particleList.add(new Particle(new ParticleVector(origin.x + (float)(Math.random() * rxOffset), origin.y + (float)(Math.random() * ryOffset)), definition));
		Iterator<Particle> it = particleList.iterator();
		while(it.hasNext()) {
			Particle p = (Particle) it.next();
			p.cycle();
			if(p.isDead())
				it.remove();
		}
	}
	
	public void applyForce(ParticleVector force) {
		for(Particle p : particleList)
			p.applyForce(force);
	}
	
	public void setOrigin(ParticleVector origin) {
		this.origin = origin;
	}
	
	public void addToOrigin(ParticleVector toAdd) {
		this.origin.add(toAdd);
	}
	
}
