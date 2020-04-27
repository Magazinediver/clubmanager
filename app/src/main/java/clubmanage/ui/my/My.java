package clubmanage.ui.my;

public class My {
    private int img1;
    private int img2;
    private String text1;
    private String text2;

    public My (int img1,int img2,String text1,String text2){
        this.img1=img1;
        this.img2=img2;
        this.text1=text1;
        this.text2=text2;
    }

    public int getImg1(){
        return img1;
    }
    public int getImg2(){
        return img2;
    }
    public String getText1(){
        return text1;
    }
    public String getText2(){
        return text2;
    }
}
