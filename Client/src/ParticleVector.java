
/**
 * 
 * @author allen
 *
 */
public class ParticleVector {

	float x;
	float y;
	
	public ParticleVector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(ParticleVector pv) {
		this.y += pv.y;
		this.x += pv.x;
	}
	
	public void subtract(ParticleVector pv) {
		this.y -= pv.y;
		this.x -= pv.x;
	}
	
	public void scale(float n) {
		x *= n;
		y *= n;
	}
	
	public void divide(float n) {
		x /= n;
		y /= n;
	}
	
	public ParticleVector get() {
		return new ParticleVector(x,y);
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt((x*x) + (y*y));
	}
	
	public void normalize() {
		float m = getMagnitude();
		this.divide(m);
	}
	
	
	
}
