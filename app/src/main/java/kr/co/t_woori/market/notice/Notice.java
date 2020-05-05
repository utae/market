package kr.co.t_woori.market.notice;

import java.io.Serializable;

/**
 * Created by rladn on 2017-10-26.
 */

public class Notice implements Serializable {

    private String noticeNum, time, title, content;
    private boolean hasImg;

    public Notice(String noticeNum, String time, String title, String content, boolean hasImg) {
        this.noticeNum = noticeNum;
        this.time = time;
        this.title = title;
        this.content = content;
        this.hasImg = hasImg;
    }

    public String getNoticeNum() {
        return noticeNum;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean hasImg() {
        return hasImg;
    }
}
