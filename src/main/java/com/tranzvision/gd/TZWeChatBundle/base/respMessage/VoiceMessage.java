package com.tranzvision.gd.TZWeChatBundle.base.respMessage;

/**
 * 语音消息
 * @author tranzvision
 *
 */
public class VoiceMessage extends BaseMessage{

	private Voice Voice;
	 
    public Voice getVoice() {
        return Voice;
    }
 
    public void setVoice(Voice voice) {
        Voice = voice;
    }
}
