package mehmetali.com.hizkoridoru.Model;

public class Otoyol {

    private int id;
    private String otoyolAdi;

    public Otoyol(int id, String otoyolAdi) {
        this.id = id;
        this.otoyolAdi = otoyolAdi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtoyolAdi() {
        return otoyolAdi;
    }

    public void setOtoyolAdi(String otoyolAdi) {
        this.otoyolAdi = otoyolAdi;
    }

    @Override
    public String toString() {
        return this.otoyolAdi;
    }
}
