import java.lang.Math;
import java.util.ArrayList;

public class point {
	public vector pos;
	public double mass;
	public vector vel;
	public ArrayList<point> neighbors;
	public ArrayList<spring> springs;

	public point(double x, double y, double z, /*point[] neighbors, vector vel,*/ double mass) {
		this.pos = new vector(x, y, z);
		this.neighbors = new ArrayList<point>();
		this.springs = new ArrayList<spring>();
		this.vel = new vector(0, 0, 0);
		this.mass = mass;
	}

	public double dist(point p) {
		return this.pos.dist(p.pos); 
	}

	public ArrayList<spring> getSprings() {
		return this.springs;
	} 
	
	public void addNeighbor(point p) {
		this.neighbors.add(p);
	}	

	public void addSpring(spring s) {
		this.springs.add(s);
	}

	public double getKE() {
		return 0.5*this.mass*Math.pow(this.vel.magnitude(), 2);
	}

	public vector getPos() {
		return this.pos;
	}

	public void setPos(vector pos) {
		this.pos = pos;
	}

	public vector getVel() {
		return this.vel;
	}

	public void setVel(vector vel) {
		this.vel = vel;
	}

	public double getMass() {
		return this.mass;
	} 
	
	public vector sumForces() {
		vector sum = new vector(0, 0, 0);
		for (spring s : this.springs) {
			sum.add2(s.getForceVec(this));
		}
		return sum;	
	}

	public void sumForces(vector v) {
		v.setVector(0, 0, 0);
		for (spring s : this.springs) {
			v.add2(s.getForceVec(this));	
		}
	}
}
