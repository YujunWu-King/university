package com.tranzvision.gd.TZWeChatBundle.base.respMessage;

/**
 * 视频消息
 * @author tranzvision
 *
 */
public class VideoMessage extends BaseMessage{

	private Video Video;
	 
    public Video getVideo() {
        return Video;
    }
 
    public void setVideo(Video video) {
        Video = video;
    }
}
