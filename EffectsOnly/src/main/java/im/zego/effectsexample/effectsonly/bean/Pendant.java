package im.zego.effectsexample.effectsonly.bean;

public class Pendant {

    private int imageRes;
    private String name;
    private String path;

    public Pendant(int imageRes,String name,String path)
    {
        this.imageRes = imageRes;
        this.name = name;
        this.path = path;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
