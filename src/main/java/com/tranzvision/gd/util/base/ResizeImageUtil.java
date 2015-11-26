package com.tranzvision.gd.util.base;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

@Service("com.tranzvision.gd.util.base.ResizeImageUtil")
public class ResizeImageUtil {
  /**
   * 压缩并且复制到新的文件夹
   * @param filePath 来源文件路径
   * @param targetFolder 目标文件夹的路径
   * @param name 新文件命名
   */
  public String resize(String filePath, String targetFolder, String name, int width) {
	  String strFlag="N";
    if (filePath != null && !filePath.equals("") && targetFolder != null
        && !targetFolder.equals("") && name != null && !name.equals("")) {
      File imgFile = new File(filePath);
      try {
        BufferedImage bi = ImageIO.read(imgFile);
        if(bi != null && (bi.getWidth() > width)){
          File folder = new File(targetFolder);
          if(folder.exists() && folder.isDirectory()){
            File targetFile = new File(targetFolder.concat("/").concat(name));
            boolean success = targetFile.createNewFile();
            if(!success){
              throw new IOException("创建失败, 文件可能已经存在");
            }
            bi = Scalr.resize(bi, width);
            String imgType =  "jpeg";
            this.save(bi, targetFile, imgType);
          }
          strFlag="Y";
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
	return strFlag;
  }
  

  
  private void save(BufferedImage image, File file, String type) throws Exception{
    // Get a ImageWriter for jpeg format.
    Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(type);
    if (!writers.hasNext())
      throw new IllegalStateException("No writers found");
    ImageWriter writer = (ImageWriter) writers.next();
    // Create the ImageWriteParam to compress the image.
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(1f);
    // The output will be a ByteArrayOutputStream (in memory)
    ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
    ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
    writer.setOutput(ios);
    writer.write(null, new IIOImage(image, null, null), param);
    ios.flush(); 
    // otherwise the buffer size will be zero!
    // From the ByteArrayOutputStream create a RenderedImage.
    FileImageOutputStream output = new FileImageOutputStream(file);
    writer.setOutput(output);
    writer.write(null, new IIOImage(image, null,null), param);
    output.close();
  }
  
  /*
   * 测试用例
   */ 
  /*
   public static void main(String[] args){
    ResizeImageUtil ci = new ResizeImageUtil();
    String path = "D:\\testimage\\3.jpg";
    String ss=ci.resize(path, "D:\\testimage", "min_xxxx.jpg", 100);
    String ss1=ci.resize(path, "D:\\testimage", "max_xxxx.jpg", 1000);
    System.out.print(ss+"--->"+ss1);
  }
  */
}

