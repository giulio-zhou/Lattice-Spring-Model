import java.lang.Math;
public class spring {
	public double k;
	public double eq;
	public point p1;
	public point p2;
	public vector v0;

	public spring(double k, double eq, point p1, point p2) {
		this.k = k;
		this.eq = eq;
		this.p1 = p1;
		this.p2 = p2;
		this.v0 = vector.point2Vec(p2, p1);
	}

	public double getEnergy() {
		return 0.5*k*Math.pow(Math.abs(p1.dist(p2) - eq), 2);
	}

	public double getForce() {
		return -k*(p1.dist(p2) - eq);
	}

	public vector getForceVec(point p) {
		if (p == this.p1) {
			//vector toReturn = vector.point2Vec(this.p1, this.p2);
			//toReturn.unit2();
			//toReturn.mult2(this.getForce());
			this.v0.setVector(this.p1.pos.x - this.p2.pos.x, this.p1.pos.y - this.p2.pos.y, this.p1.pos.z - this.p2.pos.z);
			this.v0.mult2(this.getForce()/this.v0.magnitude());
			return this.v0;
		} else if (p == this.p2) { 
			//vector toReturn = vector.point2Vec(this.p2, this.p1);
			//toReturn.unit2();
			//toReturn.mult2(this.getForce());
			this.v0.setVector(this.p2.pos.x - this.p1.pos.x, this.p2.pos.y - this.p1.pos.y, this.p2.pos.z - this.p1.pos.z);
			this.v0.mult2(this.getForce()/this.v0.magnitude());
			return this.v0;
		} else {
			throw new IllegalArgumentException("Must put in proper point corresponding to spring");
		}
	}
}
