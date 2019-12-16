import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;

public class LipDetector implements PlugInFilter
{
  ImagePlus inputImage;

  public int setup(String args, ImagePlus im)
  {
    inputImage = im;
    return DOES_RGB;
  }

  public void run(ImageProcessor inputIp)
  {
    int[] centroid = getCentroid(inputIp);
    inputIp.putPixel(centroid[0], centroid[1], 0);


    drawLipBoundingBox(centroid[0], centroid[1], inputIp);

    (new ImagePlus("centroid_" + inputImage.getShortTitle(), inputIp)).show();
  }

  private int[] getCentroid(ImageProcessor inputIp)
  {
    int xPos = 0, yPos = 0, pixelsWithWeight = 0;
    int height = inputIp.getHeight(), width = inputIp.getWidth();

    int[] centroid = new int[2];

    for (int u = 0; u < inputIp.getWidth(); u++) {
      for (int v = 0; v < inputIp.getHeight(); v++) {
        int pixel = inputIp.getPixel(u, v);
        if (pixel == 0) {
          xPos += u;
          yPos += v;
          pixelsWithWeight++;
        }
      }
    }

    centroid[0] = (int) (xPos / (pixelsWithWeight));
    centroid[1] = (int) (yPos / (pixelsWithWeight));

    return centroid;
  }



  private void drawLipBoundingBox(int x, int y, ImageProcessor inputIp)
  {
    for (int u = x - 30; u < x + 30; u++)
    {
      inputIp.putPixel(u, y + 30, 40);
      inputIp.putPixel(u, y - 30, 40);
    }

    for (int v = y - 30; v < y + 30; v++)
    {

      inputIp.putPixel(x - 30, v, 40);
      inputIp.putPixel(x + 30, v, 40);
    }
  }
}

//I used Edvard Avagyans code as base to develope my version of code 
