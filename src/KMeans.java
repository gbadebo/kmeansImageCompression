/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
 

public class KMeans {
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }
    
    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k){
    	//pick k centroid 
    	//for each point allocate point to each centroid
    	//update centroid
    	//loop until converge
    	//set each point to new centroid value
    	
    	int[] centroid = new int[k];
    	centPoint[] myPoints = new centPoint[rgb.length];
    	for(int i=0; i< k; i++){
    		int Min =i;
    		int Max = rgb.length-1;
    		int ranNum = Min + (int)(Math.random() * ((Max - Min) + 1));
    		centroid[i] = rgb[ranNum];
    	}
    	for(int numInteration = 0; numInteration < 200; numInteration++){
    	
		    	//find distance 
		    	for(int j=0; j < rgb.length; j++ ){
		    		
		    			myPoints[j] = new centPoint();
		    			myPoints[j].centIndex  = euclidDistance(rgb[j], centroid);
		    			myPoints[j].point = rgb[j];
		    			myPoints[j].cent = centroid[myPoints[j].centIndex];
		    		
		    		
		    		
		    	}
		    	
		    	//for each cluster recalculate centroid
		    	for(int clusterIndex=0; clusterIndex< k; clusterIndex++){
		    		int sumR = 0;
		    		int sumB = 0;
		    		int sumG = 0;
		    	
		    		int count = 1;
		    		
		    		for(int j = 0; j < myPoints.length; j++ ){
		    			
		    			if(myPoints[j].centIndex == clusterIndex){
		    				//convert the point to individual rgb to eliminate overflow
		    				Color mpoint = new Color(myPoints[j].point, true);
		    				sumR += mpoint.getRed();
		    				sumB += mpoint.getBlue();
		    				sumG += mpoint.getGreen();
		    				
		    				
		    				count++;
		    			}
		    			
		    		}
		    		int recrgb = (((int) (sumR/count)&0x0ff)<<16)|(((int) (sumG/count)&0x0ff)<<8)|((int) (sumB/count)&0x0ff);
		    		centroid[ clusterIndex ] = recrgb;
		    		
		    	}
		    	
    	}
    	for(int i=0; i< rgb.length; i++){
    		
    		rgb[i] = myPoints[i].cent;
    	}
    	
    }
    
    private static int euclidDistance(int point, int[] centroid){
    	
    	Double mindistance = Double.MAX_VALUE;
    	Double distance = 0.0;
    	int tempCentroidIndex = 0;
    	for(int i=0; i< centroid.length; i++){
    		
    		Color mpoint = new Color(point, true);
    		Color cpoint = new Color(centroid[i], true);
    		//use the individual color to cluster
    		distance =  Math.sqrt( Math.pow(mpoint.getBlue() - cpoint.getBlue(),2.0) + Math.pow(mpoint.getGreen() - cpoint.getGreen(),2.0) +Math.pow(mpoint.getRed() - cpoint.getRed(),2.0)+Math.pow(mpoint.getAlpha() - cpoint.getAlpha(),2.0));
    		
    		if(distance < mindistance){
    			
    			mindistance = distance;
    			tempCentroidIndex = i;
    		}
    		
    	}
    	return tempCentroidIndex;
    }

}

class centPoint{
	
	public int point;
	public int cent;
	public int centIndex;
	
}
