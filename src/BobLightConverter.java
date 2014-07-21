import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
 
public class BobLightConverter {
 
	// step through the measure points
	private final static int STEP = 12;
	// how wide is the picture
	private final static int WIDTH = 1024;
	// how much rows do we print (-1 for automatic)
	private final static int HEIGHT = -1;
	// point height and width
	private final static int P_WIDTH = 1;
	private final static int P_HEIGHT = 50;
 
	private final static String SRC = "dark.knight.out";
 
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String data = FileUtils.readFileToString(new File("/Users/jos/Desktop/" + SRC));
 
 
		String[] rows = data.split("\n");
		List<String> filteredRows = new ArrayList<String>();
 
		System.out.println("Total number of points: " + rows.length);
		// first filter the rows based on the steps
		int stepCount = 0;
		for (String row : rows) {
			stepCount++;
			if (stepCount%STEP==0) {
				filteredRows.add(row);
			}
		}
 
		System.out.println("Filtered number of points: " + filteredRows.size());
		// next calculate how many elements we can store into the width
		int nWidth = (int) Math.ceil(WIDTH/P_WIDTH);
		System.out.println(nWidth);
		int nHeight = HEIGHT;
		if (nHeight == -1) {
			// calculate the height based on the image width, the P_WIDTH and P_HEIGHT
			nHeight = (int) Math.ceil(((filteredRows.size())/nWidth)+1)*P_HEIGHT;
		}
 
 
		BufferedImage image = new BufferedImage(WIDTH,nHeight, BufferedImage.TYPE_INT_RGB);
 
		int x = 0;
		int y = -P_HEIGHT;
		for (String row : filteredRows) {
			String[] rgb = row.split(" ");
			int r = Math.round(Float.valueOf(rgb[0])*255);
			int g = Math.round(Float.valueOf(rgb[1])*255);
			int b = Math.round(Float.valueOf(rgb[2])*255);
 
			// the size of each line
			if (x%WIDTH==0) {
				x=0;
				y+=P_HEIGHT;
			}			
 
			for (int i = 0 ; i < P_WIDTH ; i++) {
				for (int j = 0 ; j < P_HEIGHT ; j++) {
					image.setRGB(x+i, y+j, 65536 * r + 256 * g + b);		
				}
			}
			x+=P_WIDTH;
		}
 
		File f = new File("/Users/jos/" + SRC + ".png");
		ImageIO.write(image, "PNG", f);
	}
}
