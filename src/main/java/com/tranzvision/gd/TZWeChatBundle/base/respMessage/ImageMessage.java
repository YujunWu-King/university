package com.tranzvision.gd.TZWeChatBundle.base.respMessage;

/**
 * 图片消息
 * @author tranzvision
 *
 */
public class ImageMessage extends BaseMessage{

	private Image Image;
	 
    public Image getImage() {
        return Image;
    }
 
    public void setImage(Image image) {
        Image = image;
    }
}
