package appengine.parser.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by anand.kurapati on 19/06/17.
 */
public class ImageUtils {

    public static BufferedImage createResizedMediumCopy(BufferedImage originalImage) {

        int scaledWidth = (int)originalImage.getWidth(null);
        int scaledHeight= (int)originalImage.getHeight(null);
        System.out.print("original image "+scaledHeight+" "+scaledHeight);
        scaledWidth=scaledWidth/2;
        scaledHeight=scaledHeight/2;

        while(scaledHeight > 300 || scaledWidth>300){
            scaledWidth=scaledWidth/2;
            scaledHeight=scaledHeight/2;
        }
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledWidth,scaledHeight, null);
        g.dispose();

        return resizedImage;

    }

    public static String getImageFormatFromUrl(String url){

        int lastIndex= url.lastIndexOf(".");

        return url.substring(lastIndex+1,url.length());

    }
}
