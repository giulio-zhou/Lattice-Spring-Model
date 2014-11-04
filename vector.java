import java.lang.Math;
public class vector {

	double x;
	double y;
	double z;

	public vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double dist(vector v2) {
		return Math.sqrt(Math.pow((double)(this.x - v2.x), 2) + Math.pow((double)(this.y - v2.y), 2) +
											 Math.pow((double)(this.z - v2.z), 2));
	}	

	public double dot(vector v2) {
		return this.x*v2.x + this.y*v2.y + this.z*v2.z;
	}

	public vector cross(vector v2) {
		return new vector(this.y*v2.z - this.z*v2.y, this.x*v2.z - this.z*v2.x, this.x*v2.y - this.y*v2.x);
	}

	public double magnitude() {
		return Math.sqrt(Math.pow((double)(this.x), 2) + Math.pow((double)(this.y), 2) + Math.pow((double)(this.z), 2));
	}

	public vector add(vector v2) {
		return new vector(this.x + v2.x, this.y + v2.y, this.z + v2.z);
	}

	public void add2(vector v2) {
		this.x += v2.x;
		this.y += v2.y;
		this.z += v2.z;
	}

	public String toString() {
		return this.x + " " + this.y + " " + this.z;
	}

	public vector mult(double constant) {
		return new vector(this.x*constant, this.y*constant, this.z*constant);
	}

	public void mult2(double constant) {
		this.x *= constant;
		this.y *= constant;
		this.z *= constant;
	}

	public vector sub(vector v2) {
		return new vector(this.x - v2.x, this.y - v2.y, this.z - v2.z);
	}

	public void sub2(vector v2) {
		this.x -= v2.x;
		this.y -= v2.y;
		this.z -= v2.z;
	}

	public vector unit() {
		double mag = this.magnitude();
		return new vector(this.x/mag, this.y/mag, this.z/mag);
	}

	public void unit2() {
		double mag = this.magnitude();
		this.x /= mag;
		this.y /= mag;
		this.z /= mag;
	}
	
	public static vector point2Vec(point p1, point p2) {
		return p1.pos.sub(p2.pos);
	}
}
