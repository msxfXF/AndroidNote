package cn.msxf0.note;

import java.util.Date;

/**
 *  * @author XiaoFei
 *  * @SchoolNum:B17070714 
 *  * @description: 
 *  * @date :19-10-26 上午10:38
 *  
 */
public class MarkDown {
    private String author;
    private String author_face;
    private String background;
    private String content;
    private Date date;
    private String title;

    public MarkDown(String paramString1, String paramString2) {
        this.title = paramString1;
        this.content = paramString2;
        this.background = "";
        this.date = new Date();
        this.author = "匿名用户";
        this.author_face = "";
    }

    public MarkDown(String paramString1, String paramString2, String paramString3, Date paramDate, String paramString4, String paramString5) {
        this.title = paramString1;
        this.content = paramString2;
        this.background = paramString3;
        this.date = paramDate;
        this.author = paramString4;
        this.author_face = paramString5;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getAuthor_face() {
        return this.author_face;
    }

    public String getBackground() {
        return this.background;
    }

    public String getContent() {
        return this.content;
    }

    public Date getDate() {
        return this.date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAuthor(String paramString) {
        this.author = paramString;
    }

    public void setAuthor_face(String paramString) {
        this.author_face = paramString;
    }

    public void setBackground(String paramString) {
        this.background = paramString;
    }

    public void setContent(String paramString) {
        this.content = paramString;
    }

    public void setDate(Date paramDate) {
        this.date = paramDate;
    }

    public void setTitle(String paramString) {
        this.title = paramString;
    }
}