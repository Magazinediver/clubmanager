package clubmanage.ui.home;

public class Home {
    private int imagedId;
    private String title;
    private String time;
    private String spot;
    private String state;

    public Home (int imagedId,String title,String time,String spot,String state){
        this.imagedId=imagedId;
        this.title=title;
        this.time=time;
        this.spot=spot;
        this.state=state;
    }

    public int getImagedId(){
        return imagedId;
    }
    public String getTitle(){
        return title;
    }
    public String getTime(){
        return time;
    }
    public String getSpot(){
        return spot;
    }
    public String getState(){
        return state;
    }
}
