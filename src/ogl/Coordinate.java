package ogl;

public class Coordinate implements Comparable<Coordinate> {
	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "["+x+";"+y+"]";
	}
	
	@Override
	public int compareTo(Coordinate a) {
		if(this.x != a.x)
			return  Integer.compare(this.x, a.x);
		else 
			return  Integer.compare(this.y, a.y);
					
	
	}
	
}