package clubmanage.ui.manage;

public class Manage {
    private int img1;
    private int img2;
    private int img3;
    private int img4;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String text5;

    public Manage (int img1,int img2,int img3,int img4,String text1,String text2,String text3,String text4,String text5){
        this.img1=img1;
        this.img2=img2;
        this.img3=img3;
        this.img4=img4;
        this.text1=text1;
        this.text2=text2;
        this.text3=text3;
        this.text4=text4;
        this.text5=text5;
    }

    public int getImg1(){
        return img1;
    }
    public int getImg2(){
        return img2;
    }
    public int getImg3(){
        return img3;
    }
    public int getImg4(){
        return img4;
    }
    public String getText1(){
        return text1;
    }
    public String getText2(){
        return text2;
    }
    public String getText3(){
        return text3;
    }
    public String getText4(){
        return text4;
    }
    public String getText5(){
        return text5;
    }

}
