package com.tranzvision.gd.util.base;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class CompressImageUtil {

  /**
   * 获取文件的大小
   * @param path
   * @return 文件的大小， 单位为byte， 如果出错，则返回-1
   */
  public int getSize(String path) {
    try {
      File imgFile = new File(path);
      if(imgFile.exists() && imgFile.isFile()){
    	@SuppressWarnings("resource")
		FileInputStream fi = new FileInputStream(imgFile);
        return fi.available();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /**
   * 压缩并且复制到新的文件夹
   * @param filePath 来源文件路径
   * @param targetFolder 目标文件夹的路径
   * @param name 新文件命名
   */
  public void compressImage(String filePath, String targetFolder, String name) {
    if (filePath != null && !filePath.equals("") && targetFolder != null
        && !targetFolder.equals("") && name != null && !name.equals("")) {
      File imgFile = new File(filePath);
      try {
        BufferedImage bi = ImageIO.read(imgFile);
        File folder = new File(targetFolder);
        if(folder.exists() && folder.isDirectory()){
          File targetFile = new File(targetFolder.concat("/").concat(name));
          boolean success = targetFile.createNewFile();
          if(!success){
            throw new IOException("创建失败, 文件可能已经存在");
          }
          float rate = this.getRate(bi);
          String imgType =  "jpeg";
          compressAndSave(bi, rate, targetFile, imgType);
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /**
   * 根据文件大小， 获取压缩图片的比率， 目的是让图片在100k-200k左右
   * @param bi 图片
   * @return rate 图片质量，越大越好
   */
  public float getRate(BufferedImage bi){
    //获取图片的大小
    byte[] imageBytes = ((DataBufferByte) bi.getData().getDataBuffer()).getData();
    //和10E5f == 100kb比较，并且这个作为计算的因子
    float rate = 10E5f / imageBytes.length;
    //经验值， 设置rate^2的平方曲线 + 0.009f的偏移量
    rate = rate * rate * 4 + 0.09f;
    return rate > 0.8f ? 0.8f : rate;
  }
  
  private void compressAndSave(BufferedImage image, float quality, File file, String type) throws Exception{
    // Get a ImageWriter for jpeg format.
    Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(type);
    if (!writers.hasNext())
      throw new IllegalStateException("No writers found");
    ImageWriter writer = (ImageWriter) writers.next();
    // Create the ImageWriteParam to compress the image.
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(quality);
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
  }
  
  /*
   * 测试用例
   */ 
//   public static void main(String[] args){
//    CompressImageUtil ci = new CompressImageUtil();
//    String path = "test1.jpg";
//    System.out.println(ci.getSize(path) + "Bytes");
//    ci.compressImage(path, "C:\\Users\\hanming\\workspace", "hello4.jpg");
//  }
}

