package im.zego.effectsexample.effectsonly.bean;

public class Resource {

    private int imageRes;
    private String name;
    private String path;
    private int effectStrength;

    public Resource(int imageRes, String name, String path,int effectStrength)
    {
        this.imageRes = imageRes;
        this.name = name;
        this.path = path;
        this.effectStrength = effectStrength;
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

    public int getEffectStrength() {
        return effectStrength;
    }

    public void setEffectStrength(int effectStrength) {
        this.effectStrength = effectStrength;
    }
}
