package cn.hand.tech.ui.weight.bean;

/**
 * Created by hand-hitech2 on 2018-03-22.
 */

public class ChannelDetectModel {
    private boolean isChannelNormal;
    private String ChannelName;

    public boolean isChannelNormal() {
        return isChannelNormal;
    }

    public void setChannelNormal(boolean channelNormal) {
        isChannelNormal = channelNormal;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }
}
