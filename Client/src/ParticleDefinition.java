
/**
 * 
 * @author Allen
 *
 */
public class ParticleDefinition {
	
	private float accelX, accelY, lifespanDec, mass;
	private double velXMax, velXMin, velYMax, velYMin;
	private boolean fade;
	
	public ParticleDefinition(float accelX, float accelY, float lifespanDec, float mass, double velXMax, double velXMin, double velYMax, double velYMin, boolean fade) {
		this.accelX = accelX;
		this.accelY = accelY;
		this.lifespanDec = lifespanDec;
		this.mass = mass;
		this.velXMax = velXMax;
		this.velXMin = velXMin;
		this.velYMax = velYMax;
		this.velYMin = velYMin;
		this.fade = fade;
	}
	
	public float getAccelX() {
		return accelX;
	}
	
	public float getAccelY() {
		return accelY;
	}
	
	public float getLifeSpanDec() {
		return lifespanDec;
	}
	
	public float getMass() {
		return mass;
	}
	
	public double getVelocityXMax() {
		return velXMax;
	}
	
	public double getVelocityXMin() {
		return velXMin;
	}
	
	public double getVelocityYMax() {
		return velYMax;
	}
	
	public double getVelocityYMin() {
		return velYMin;
	}
	
	public boolean isFading() {
		return fade;
	}
	
}
