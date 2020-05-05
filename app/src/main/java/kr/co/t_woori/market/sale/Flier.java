package kr.co.t_woori.market.sale;

import java.io.Serializable;

import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-26.
 */

public class Flier implements Serializable {

    private String id, title, time, content;

    public Flier(String id, String title, String time, String content) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
