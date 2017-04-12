package ogl;


import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

public class OpenGLInstance extends JFrame implements GLEventListener {
    private static final long serialVersionUID = 1L;
    final private int width = 800;
    final private int height = 600;
    private static Coordinate matrix[][];
    private int minX, maxX, minY, maxY, personCounter, frameCounter;

    public OpenGLInstance(Coordinate m[][], int minX, int maxX, int minY, int maxY, int personCounter, int frameCounter) {
    	super("Analysis Display");
    	matrix = m;
    	this.minX = minX;
    	this.maxX = maxX;
    	this.minY = minY;
    	this.maxY = maxY;
    	this.personCounter = personCounter;
    	this.frameCounter = frameCounter;
    	
    	GLProfile profile = GLProfile.get(GLProfile.GL2);
    	GLCapabilities capabilities = new GLCapabilities(profile);
    	
    	GLCanvas canvas = new GLCanvas(capabilities);
    	canvas.addGLEventListener(this);
    	
    	this.setName("Minimal OpenGL");
    	this.getContentPane().add(canvas);
    	
    	this.setSize(width, height);
    	this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true);
    	this.setResizable(false);
    	canvas.requestFocusInWindow();
    	}
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	// call your draw code here
    	gl.glPointSize(10);
    	gl.glColor3f(1f, 1f, 0f);
    	
    	
    	//normalized = (x-min(x))/(max(x)-min(x))
    	double x;
    	double y;
    	
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(0.0f,0.0f,1.0f);
        gl.glLineWidth(30);

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

        gl.glBegin(GL2.GL_POLYGON);
            gl.glVertex2d(0.536,0.536);
            gl.glVertex2d(0.536,-0.536);
            gl.glVertex2d(-0.536,-0.536);
            gl.glVertex2d(-0.536,0.536);
        gl.glEnd();

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	double color = 100/frameCounter;
    	gl.glBegin(GL2.GL_POINTS);
    		for(int i = 0; i<personCounter;i++){
    			for(int j = 0; j<frameCounter; j++){
    				if(matrix[i][j].getX() !=0 && matrix[i][j].getY()!=0){
        				x = (((double)matrix[i][j].getX() - (double) minX)/((double) maxX - (double) minX));
        				y = (((double)matrix[i][j].getY() - (double)minY)/((double)maxY - (double)minY));
        				x = x -0.5;
        				y = y-0.5;
        				gl.glPointSize(2);
        				gl.glColor3d(1f-((0.005*j)), 1f-((0.005*j)), 0f);
        				gl.glVertex2d(x, y);
    				}	 
    			}
    		}
    		gl.glEnd();
    	
    	gl.glFlush();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    	GL2 gl = drawable.getGL().getGL2();
    	gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
    	
    	}

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
    }
    
    public void play() {
    }
}
