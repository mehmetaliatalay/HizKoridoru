package mehmetali.com.hizkoridoru.Model;

public class Gise {

    private int id;
    private String giseAdi;
    private int otoyolId;

    public Gise(int id, String giseAdi, int otoyolId) {
        this.id = id;
        this.giseAdi = giseAdi;
        this.otoyolId = otoyolId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGiseAdi() {
        return giseAdi;
    }

    public void setGiseAdi(String giseAdi) {
        this.giseAdi = giseAdi;
    }

    public int getOtoyolId() {
        return otoyolId;
    }

    public void setOtoyolId(int otoyolId) {
        this.otoyolId = otoyolId;
    }


    @Override
    public String toString() {
        return this.giseAdi;
    }
}
