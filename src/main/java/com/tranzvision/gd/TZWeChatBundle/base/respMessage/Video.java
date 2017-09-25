package com.tranzvision.gd.TZWeChatBundle.base.respMessage;

/**
 * 视频消息体
 * @author tranzvision
 *
 */
public class Video {

	private String MediaId;
    private String Title;
    private String Description;
 
    public String getTitle() {
        return Title;
    }
 
    public void setTitle(String title) {
        Title = title;
    }
 
    public String getDescription() {
        return Description;
    }
 
    public void setDescription(String description) {
        Description = description;
    }
 
    public String getMediaId() {
        return MediaId;
    }
 
    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
