package com.waterfairy.zero;

import android.media.MediaPlayer;

/**
 * Created by Leon on 2017/4/21.
 */

public class MediaPlayerManger {
    private static final MediaPlayerManger MEDIA_PLAYER_MANGER = new MediaPlayerManger();
    private MediaPlayer mediaPlayer;
    private MediaPlayerManger(){
        mediaPlayer=new MediaPlayer();
    }

    public static MediaPlayerManger getInstance() {
        return MEDIA_PLAYER_MANGER;
    }

    /**
     * 设置播放文件路径
     *
     * @param path
     */
    public void setDataPath(String path) {

    }
    public void prepare(){

    }
    public void play(){

    }
    public void pause(){

    }
    public void stop(){

    }
    public void release(){

    }
}
