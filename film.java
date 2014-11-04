import java.util.ArrayList;
public class film {
	public static final int NUM_OF_THREADS = 4;
	point[][][] points;
	int xPoints;
	int yPoints;
	int zPoints;
	double initSpacing;
	ArrayList<spring> allSprings;

	public film(double initSpacing, double xLength, double yLength, double zLength) {
		this.xPoints = (int) (xLength / initSpacing);
		this.yPoints = (int) (yLength / initSpacing);
		this.zPoints = (int) (zLength / initSpacing);	
		this.points = new point[this.xPoints][this.yPoints][this.zPoints];
		this.allSprings = new ArrayList<spring>();
		
		for (int i = 0; i < this.xPoints; i++) {
			for (int j = 0; j < this.yPoints; j++) {
				for (int k = 0; k < this.zPoints; k++) {
					this.points[i][j][k] = new point(i*initSpacing, j*initSpacing, k*initSpacing, 1);
				}
			}
		}	
	
		for (int i = 0; i < this.xPoints; i++) {
			for (int j = 0; j < this.yPoints; j++) {
				for (int k = 0; k < this.zPoints; k++) {
					point currPoint = this.points[i][j][k];
					ArrayList<point> neighbors = new ArrayList<point>();
					if (i < this.xPoints - 1) {
						neighbors.add(this.points[i+1][j][k]);
					}
					if (j < this.yPoints - 1) {
						neighbors.add(this.points[i][j+1][k]);
					}
					if (k < this.zPoints - 1) {	
						neighbors.add(this.points[i][j][k+1]);
					}
					for (point p : neighbors) {
						p.addNeighbor(currPoint);			
						currPoint.addNeighbor(p);
						spring toAdd = new spring(20, 1, currPoint, p);
						p.addSpring(toAdd);
						currPoint.addSpring(toAdd);	
						this.allSprings.add(toAdd);
					}				
				}
			}
			//System.out.println(i);
		}	
	}

	public double totalPE() {
		/*double total = 0;/*
		for (int i = 0; i < this.xPoints-1; i++) {
			for (int j = 0; j < this.yPoints-1; j++) {
				for (int k = 0; k < this.zPoints-1; k++) {
					ArrayList<spring> currSprings = new ArrayList<spring>();				
					this.points[i+1][j][k]
					this.points[i][j+1][k]
					this.points[i][j][k+1]
				}
			}
		}*/
		//System.out.println(this.allSprings.size());
/*
		for (spring s : this.allSprings) {
			total += s.getEnergy();
		}
*/
		double total = 0;
		double[] totals = new double[NUM_OF_THREADS];
		Thread[] thrArray = new Thread[NUM_OF_THREADS];
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			thrArray[i] = new Thread(new Worker3(totals, this.allSprings, i, NUM_OF_THREADS));	
			thrArray[i].start();	
		}

		for (int i = 0; i < NUM_OF_THREADS; i++) {
			try {
				thrArray[i].join();
			} catch (InterruptedException e) {
				System.err.println("lol");
			}
		}

		for (int i = NUM_OF_THREADS*(this.allSprings.size()/NUM_OF_THREADS); i < this.allSprings.size(); i++) {
			total += this.allSprings.get(i).getEnergy();
		}

		for (int i = 0 ; i < totals.length; i++) {
			total += totals[i];
		}

		return total;
	}

	public double totalKE() {
		double total = 0;
		double[] totals = new double[NUM_OF_THREADS];
		Thread[] thrArray = new Thread[NUM_OF_THREADS];
		for (int i = 0; i < NUM_OF_THREADS; i++) {
			thrArray[i] = new Thread(new Worker4(totals, this.points, i, NUM_OF_THREADS));
			thrArray[i].start();
		/*	
			for (int j = 0; j < this.yPoints; j++) {
				for (int k = 0; k < this.zPoints; k++) {
					total += this.points[i][j][k].getKE();		
				}
			}*/	
		}	

		for (int i = 0; i < NUM_OF_THREADS; i++) {
			try {
				thrArray[i].join();
			} catch (InterruptedException e) {
				System.err.println("lololol");
			}
		}

		for (int i = 0; i < NUM_OF_THREADS; i++) {
			total += totals[i];
		}
		return total;
	}

	public void equilibrate(int maxNIter, double dt, vector[][][] forceExt) {
		vector[][][] forceTot = new vector[this.xPoints][this.yPoints][this.zPoints];

		for (int i = 0; i < this.xPoints; i++) {
			for (int j = 0; j < this.yPoints; j++) {
				for (int k = 0; k < this.zPoints; k++) {
					vector currVec = forceExt[i][j][k];
					forceTot[i][j][k] = new vector(currVec.x, currVec.y, currVec.z);
				}
			}
		}
		
		long k = System.nanoTime();
		int counter = 1;
		while (counter <= maxNIter) {
			Thread[] thrArray = new Thread[NUM_OF_THREADS];
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				thrArray[i] = new Thread(new Worker(forceTot, this.points, dt, i, NUM_OF_THREADS));
				thrArray[i].start();	

				/*for (int j = 0; j < this.yPoints; j++) {
					for (int k = 0; k < this.zPoints; k++) {
						point currPoint = this.points[i][j][k];
						vector currForce = forceTot[i][j][k].mult(dt/currPoint.getMass());
						currPoint.getVel().add2(currForce);	
						currPoint.getPos().add2(currPoint.getVel().mult(dt));	
						//currPoint.setVel(newVel);
						//currPoint.setPos(newPos);
					}
				}*/
			}
			
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				try {
					thrArray[i].join();
				} catch (InterruptedException e) {
					System.err.println("hellp");
				}
			}
		

			for (int i = 0; i < NUM_OF_THREADS; i++) {
				//Thread[] thrArray = new Thread[this.xPoints];
				/*try {
					thrArray[i].join();
				} catch (InterruptedException e) {
					System.err.println("hellp");
				}*/
				thrArray[i] = new Thread(new Worker2(forceTot, this.points, forceExt, dt, i, NUM_OF_THREADS));
				thrArray[i].start();
				/*for (int j = 0; j < this.yPoints; j++) {
					for (int k = 0; k < this.zPoints; k++) {
						forceTot[i][j][k] = this.points[i][j][k].sumForces();
						forceTot[i][j][k].add2(forceExt[i][j][k]);
					}
				}*/
			}
			for (int i = 0; i < NUM_OF_THREADS; i++) {
				try {
					thrArray[i].join();
				} catch (InterruptedException e) {
					System.err.println("hellp");
				}
			}		
			
			System.out.println("Iteration " + counter);	
			System.out.println("Potential energy: " + this.totalPE());
			System.out.println("Kinetic energy: " + this.totalKE());
		/*	System.out.println("Force external: " + forceExt[0][0][0]);
			System.out.println("Force vector: " + forceTot[0][0][0]);
			System.out.println("Force external: " + forceExt[this.xPoints-1][this.yPoints-1][this.zPoints-1]);
			System.out.println("Force vector: " + forceTot[this.xPoints-1][this.yPoints-1][this.zPoints-1]);
			System.out.println("Velocity: " + this.points[0][0][0].getVel().magnitude());*/
			counter++;
		}		
		System.out.println(System.nanoTime() - k);
	}

	public String toString() {
		String toReturn = "";
		String[] totals = new String[this.xPoints];
		Thread[] thrArray = new Thread[this.xPoints];
		for (int i = 0; i < this.xPoints; i++) {
			/*for (int j = 0; j < this.yPoints; j++) {
				for (int k = 0; k < this.zPoints; k++) {
					point currPoint = this.points[i][j][k];	
					toReturn += i + " " + j + " " + k + " " + currPoint.pos + "\n"; 
				}
			}*/
			thrArray[i] = new Thread(new Worker5(totals, this.points[i], i, this.xPoints));
			thrArray[i].start();	
		}

		for (int i = 0; i < this.xPoints; i++) {
			//System.out.println(i);
			try { 
				thrArray[i].join();
			} catch (InterruptedException e) {
				System.out.println("halp");
			}
			toReturn += totals[i];
		}		

		return toReturn;
	}

	public static void main(String[] args) {
		long t = System.nanoTime();
		film f = new film(1, 100, 100, 100);
		System.out.println(System.nanoTime()-t);	
		System.out.println(f.totalPE());
		System.out.println(f.totalKE());
		double maxForce = 1;
		vector[][][] forceExt = new vector[f.xPoints][f.yPoints][f.zPoints];		
		for (int i = 0; i < f.xPoints; i++) {
			for (int j = 0; j < f.yPoints; j++) {
				for (int k = 0; k < f.zPoints; k++) {
					forceExt[i][j][k] = new vector((-1/(double)f.xPoints)*(double)(f.xPoints - 1 - i)
										 + (1/(double)f.xPoints)*((double)i), 0, 0);
				}
			}
		}		

		/*for (int i = 0; i < f.xPoints; i++) {
			System.out.println(forceExt[i][0][0]);
		}*/

		System.out.println(f);
		System.out.println("Starting equilibration process");
		//long k = System.nanoTime();
		f.equilibrate(100000, 0.0001, forceExt);
		System.out.println(f);
		//System.out.println(System.nanoTime() - k);
	}

	public class Worker implements Runnable { 
		private vector[][][] forceTot;
		private point[][][] points;
		private double dt;
		private int num;
		private int numThreads;

		public Worker(vector[][][] forceTot, point[][][] points, double dt, int num, int numThreads) {
			this.forceTot = forceTot;
			this.points = points;
			this.dt = dt;
			this.num = num;
			this.numThreads = numThreads;
		}
		public void run() {	
			//System.out.println(num);
			for (int i = points.length*(num/numThreads); i < points.length*((num+1)/numThreads); i++) {
				for (int j = 0; j < points[0].length; j++) {
					for (int k = 0; k < this.points[0][0].length; k++) {
						point currPoint = points[i][j][k];
						//vector currForce = forceTot[j][k].mult(dt/currPoint.getMass());
						forceTot[i][j][k].mult2(dt/currPoint.getMass());
						vector currForce = forceTot[i][j][k];
						vector vel = currPoint.getVel();
						//currPoint.getVel().add2(currForce);
						//currPoint.getPos().add2(currPoint.getVel().mult(dt));	
						vel.add2(currForce);
						vel.mult2(dt);
						currPoint.getPos().add2(vel);
						vel.mult2(1/dt);
					}
				}
			}
		}
	}

	public class Worker2 implements Runnable { 
		private vector[][][] forceTot;
		private point[][][] points;
		private vector[][][] forceExt;
		private double dt;
		private int num;
		private int numThreads;

		public Worker2(vector[][][] forceTot, point[][][] points, vector[][][] forceExt, double dt, int num, int numThreads) {
			this.forceTot = forceTot;
			this.points = points;
			this.forceExt = forceExt;
			this.dt = dt;
			this.num = num;
			this.numThreads = numThreads;
		}
		public void run() {	
			//System.out.println(num);
			for (int i = points.length*num/numThreads; i < points.length*(num+1)/numThreads; i++) {
				for (int j = 0; j < points[0].length; j++) {
					for (int k = 0; k < points[0][0].length; k++) {
						//forceTot[i][j][k] = points[i][j][k].sumForces();
						points[i][j][k].sumForces(forceTot[i][j][k]);
						forceTot[i][j][k].add2(forceExt[i][j][k]);
					}
				}
			}
		}
	}

	public class Worker3 implements Runnable { 
		private double[] total;
		private ArrayList<spring> allSprings;
		private int num;
		private int numThreads;

		public Worker3(double[] total, ArrayList<spring> allSprings, int num, int numThreads) {
			this.total = total;
			this.allSprings = allSprings;
			this.num = num;
			this.numThreads = numThreads;
		}
		public void run() {	
			//System.out.println(num);
			double localTotal = 0;
			for (int i = num*(allSprings.size()/numThreads); i < (num+1)*(allSprings.size()/numThreads); i++) {
				localTotal += this.allSprings.get(i).getEnergy();
			}
			total[num] = localTotal;
		}
	}

	public class Worker4 implements Runnable {
		double[] total;
		point[][][] points;
		int num;
		int numThreads;

		public Worker4(double[] total, point[][][] points, int num, int numThreads) {
			this.total = total;
			this.points = points;
			this.num = num;
			this.numThreads = numThreads;
		}

		public void run() {
			double localTotal = 0;
			for (int i = points.length*num/numThreads; i < points.length*(num+1)/numThreads; i++) {
				for (int j = 0; j < this.points[0].length; j++) {
					for (int k = 0; k < this.points[0][0].length; k++) {
						localTotal += this.points[i][j][k].getKE();
					}
				}		
			}
			this.total[num] = localTotal;
		}
	}
	
	public class Worker5 implements Runnable {
		String[] total;
		point[][] points;	
		int num;
		int numThreads;

		public Worker5(String[] total, point[][] points, int num, int numThreads) {
			this.total = total;
			this.points = points;
			this.num = num;
			this.numThreads = numThreads;
		}

		public void run() {
			StringBuilder localTotal = new StringBuilder(this.points.length*this.points[0].length * 20);
			for (int j = 0; j < this.points.length; j++) {
				for (int k = 0; k < this.points[0].length; k++) {
					point currPoint = this.points[j][k];
					//localTotal += num + " " + j + " " + k + " " + currPoint.pos + "\n";
					localTotal.append(num).append(" ").append(j).append(" ").append(k).append(" ")
										.append(currPoint.pos.toString()).append("\n");
				}
			}
			this.total[num] = new String(localTotal);
		}
	}
}
