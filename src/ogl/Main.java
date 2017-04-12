package ogl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) throws IOException {
		//Initialize parser with the filepath
		Main parser = new Main("\\AT01_Paths_D.txt");
		
		//Run parser
		parser.processLineByLine();
		
		//Operate on matrix data structures
        distanceMatrix = new double[personCounter][frameCounter][personCounter];
        Coordinate cleanMatrix[][] = new Coordinate[personCounter][frameCounter];
        normalizedDistanceMatrix = new double[personCounter][frameCounter][personCounter];
        cleanDMatrix();
        cleanMatrix(cleanMatrix);
        fillDistanceMatrix();
        fillNormalizedDistanceMatrix();
        check.clear();
        O_ALGORITMO();
        dAuxStructSorting();
        navigateMap();
        //System.out.println(output);
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter( "output.txt"));
            writer.write("----------------------------------------------------GRUPOS ENCONTRADOS----------------------------------------------------\n");
            writer.write( output);
            writer.write("--------------------------------------------------------------------------------------------------------------------------\n");

        }
        catch ( IOException e)
        {
        }
        finally
        {
            try
            {
                if ( writer != null)
                writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
        
        OpenGLInstance game = new OpenGLInstance(cleanMatrix, minX, maxX, minY, maxY, personCounter, frameCounter);
        //game.play();
	}
	
	//Constructor method
    public Main(String aFileName){
        for(int q = 0; q<matrix.length; q++){
            for(int w = 0; w<matrix[q].length; w++){
                matrix[q][w] = new Coordinate(0,0);
            }
        }
        fFilePath = Paths.get(aFileName);
    }

    //Matrix manipulation functions
    public static void cleanDMatrix(){
        for(int q = 0; q<personCounter; q++){
            for(int w = 0; w<frameCounter; w++){
                for(int e = 0; e<personCounter; e++){
                    distanceMatrix[q][w][e] = 0;
                }
            }
        }
    }
    
    public static void cleanMatrix(Coordinate cleanMatrix[][]){
        for(int q = 0; q<personCounter;q++)
            for(int w = 0;w<frameCounter;w++)
                cleanMatrix[q][w] = matrix[q][w];
    }
    
    public static boolean skipDoubles(int x, int y){
        for(Coordinate i : check){
            if ((i.getX() == x && i.getY() == y) || (i.getX() == y && i.getY() == x)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean skipDoubles2(int x, int y){
        for(Coordinate i : check){
            if ((i.getX() == y && i.getY() == x)){
                return true;
            }
        }
        return false;
    }
    
    public static void fillDistanceMatrix(){
        for(int q = 0; q<personCounter; q++){
            for(int w = 0; w<frameCounter; w++){
                for(int e = 0; e<personCounter; e++){
                    if(!skipDoubles(q, e)){
                        check.add(new Coordinate(q, e));
                    }
                    distanceMatrix[q][w][e] = calculateDistancev2(q, e, w);
                }
            }
        }
    }
    
    public static void fillNormalizedDistanceMatrix(){
        for(int q = 0; q<personCounter; q++){
            for(int w = 0; w<frameCounter; w++){
                for(int e = 0; e<personCounter; e++){
                    if(!skipDoubles(q, e)){
                        check.add(new Coordinate(q, e));
                    }else {
                    	normalizedDistanceMatrix[q][w][e] = calculateDistanceNormalized(q, e, w);
                    }
                }
            }
        }
    }
    
    //Aux functions
    public static double calculateDistancev2(int q, int e, int y){
        double x1 = (double) matrix[q][y].getX();
        double y1 = (double) matrix[q][y].getY();
        double x2 = (double) matrix[e][y].getX();
        double y2 = (double) matrix[e][y].getY();
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    
    public static double calculateDistanceNormalized(int q, int e, int y){
    	if(matrix[q][y].getX()==0 || matrix[q][y].getY()==0){
    		return 0.0;
    	}else{
            double x1 = normalizeX((double) matrix[q][y].getX());
            double y1 = normalizeY((double) matrix[q][y].getY());
            double x2 = normalizeX((double) matrix[e][y].getX());
            double y2 = normalizeY((double) matrix[e][y].getY());
            return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    	}
    }
    
    public static double normalizeX(double value){
    	return (((double)value - (double) minX)/((double) maxX - (double) minX));
    }
    
    public static double normalizeY(double value){
    	return (((double)value - (double) minY)/((double) maxY - (double) minY));
    }
    
    public static void O_ALGORITMO(){
        for(int q = 0; q<personCounter; q++){
            for(int w = 0; w<frameCounter; w++){
                for(int e = 0; e<personCounter; e++){
                    if(skipDoubles2(q, e)){
                    	//DO NOTHING
                        
                    }else if(q==e || normalizedDistanceMatrix[q][w][e]==0.0){
                		//DO NOTHING
                	}else if(normalizedDistanceMatrix[q][w][e] <= groupRange){
                		//DO STUFF
                		//output+="Pessoa " + q + " e pessoa " + e + " formaram um grupo na frame " + w +"\n";
                		distanceAuxList.add(new DistanceAuxStruct(q, w, e));
                		check.add(new Coordinate(q, e));
                	}
                }
            }
        }
    }
    
    public static void dAuxStructSorting(){
    	for(DistanceAuxStruct i : distanceAuxList){
    		addGroup(i.getPerson1(), i.getPerson2(),i.getFrame());
    	}
    	
    }
    
    public static void addGroup(int p1, int p2, int frame){
    	if(groupMap.containsKey(p1+"-"+p2)){
    		//DO STUFF
    		Group aux = (Group) groupMap.get(p1+"-"+p2);
    		if(aux.getMinFrame()>frame) aux.setMinFrame(frame);
    		if(aux.getMaxFrame()<frame) aux.setMaxFrame(frame);
    		groupMap.put(p1+"-"+p2, aux);
    		
    	}else{
    		groupMap.put(p1+"-"+p2, new Group(p1, p2, frame, frame));
    	}
    }
    
    public static void navigateMap(){
    	Set set = groupMap.entrySet();
    	Iterator i = set.iterator();
    	while(i.hasNext()){
    		Map.Entry me = (Map.Entry)i.next();
    		Group aux = (Group) groupMap.get(me.getKey());
    		if((aux.getMaxFrame()-aux.getMinFrame()) >= 60){
    			output+="Pessoa " + aux.getPerson1() + " e pessoa "+aux.getPerson2() + "\nFrame inicial: " + aux.getMinFrame()+ "\nFrame final: "+aux.getMaxFrame() + "\n\n";
    		}
    	}
    }
    /*
    public static boolean addGroup(Group value){
    	if(value.getPerson1())
    	
    	return false;
    }*/
    
    //Parsing functions
    public final void processLineByLine() throws IOException {
        try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
            scanner.nextLine();
                while (scanner.hasNextLine()){
                    personCounter++;
                    processLine(scanner.nextLine());
                }      
        }
    }
    
    protected void processLine(String aLine){
        //use a second Scanner to parse the content of each line
        int i = 0;
        Scanner scanner = new Scanner(aLine);
        //Pattern pattern = Pattern.compile("(.*?)");
        //System.out.println(scanner.nextInt());
        scanner.useDelimiter("\\(");
        Matcher m = Pattern.compile("\\(([0-9]+,[0-9]+,[0-9]+)\\)").matcher(aLine);
        String[] parts = new String[500];
        ArrayList<String> pholder = new ArrayList<String>();
            while(m.find()) {
                //System.out.println(m.group(1));
                pholder.add(m.group(1));
            }
           
            ArrayList<String[]> pholder2 = new ArrayList<String[]>();
 
            for(int j = 0; j<pholder.size(); j++){
                String pholder3[] = pholder.get(j).split(",");
                if(Integer.parseInt(pholder3[0])>maxX) maxX = Integer.parseInt(pholder3[0]);
                else if(Integer.parseInt(pholder3[0])<minX && Integer.parseInt(pholder3[0]) != 0) minX = Integer.parseInt(pholder3[0]);
                if(Integer.parseInt(pholder3[1])>maxY) maxY = Integer.parseInt(pholder3[1]);
                else if(Integer.parseInt(pholder3[1])<minY && Integer.parseInt(pholder3[1]) != 0) minY = Integer.parseInt(pholder3[1]);
                matrix[p][t] = new Coordinate(Integer.parseInt(pholder3[0]), Integer.parseInt(pholder3[1]));
                if(frameCounter<Integer.parseInt(pholder3[2])){
                    frameCounter = Integer.parseInt(pholder3[2]);
                }
                t++;
            }
 
            pholder.clear();
            pholder2.clear();
            p++;
            t = 0;
    }
    
    //Misc functions
    public Coordinate[][] getMatrix() {
        return matrix;
    }
    
    public static void printArray(Coordinate m[][]) {
        for (int row = 0; row < m.length; row++) {
            for (int column = 0; column < m[row].length; column++) {
                //System.out.print(m[row][column].getX() + " ");
                System.out.print("("+ m[row][column].getX() + "," + m[row][column].getY() + ") ");
            }
            System.out.println();
        }
    }
	
	//Var declarations
    private static Coordinate matrix[][] = new Coordinate[200][600];
    private static double distanceMatrix[][][]; //= new double[100][100][100];
    private static double normalizedDistanceMatrix[][][];
    private static List<DistanceAuxStruct> distanceAuxList = new ArrayList<>();
    //private static List<Group> groupList = new ArrayList<>();
    private static HashMap groupMap = new HashMap();
    private static final double groupRange = 0.07;
    private static int personCounter = 0, frameCounter = 0;
    private static List<Coordinate> check = new ArrayList<>();
    private static int minX = 5000, maxX = 0, minY = 5000, maxY = 0;
    private int p = 0, t = 0;
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private static String output = "";
    

}
