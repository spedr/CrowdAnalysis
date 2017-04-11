package ogl;

public class Group {
	private int person1, person2, minFrame, maxFrame;
	
	

	public Group(int person1, int person2, int minFrame, int maxFrame) {
		super();
		this.person1 = person1;
		this.person2 = person2;
		this.minFrame = minFrame;
		this.maxFrame = maxFrame;
	}

	public int getPerson1() {
		return person1;
	}

	public void setPerson1(int person1) {
		this.person1 = person1;
	}

	public int getPerson2() {
		return person2;
	}

	public void setPerson2(int person2) {
		this.person2 = person2;
	}

	public int getMinFrame() {
		return minFrame;
	}

	public void setMinFrame(int minFrame) {
		this.minFrame = minFrame;
	}

	public int getMaxFrame() {
		return maxFrame;
	}

	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}
	
	
}
