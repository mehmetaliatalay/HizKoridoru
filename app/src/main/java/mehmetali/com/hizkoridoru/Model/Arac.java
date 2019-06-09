package mehmetali.com.hizkoridoru.Model;

public class Arac {

    private int id;
    private String aracTipi;
    private int hizSiniri;
    private int sinif;


    public Arac(int id, String arac_Tipi, int hiz_Siniri, int sinif) {
        this.id = id;
        aracTipi = arac_Tipi;
        hizSiniri = hiz_Siniri;
        this.sinif = sinif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAracTipi() {
        return aracTipi;
    }

    public void setAracTipi(String aracTipi) {
        this.aracTipi = aracTipi;
    }

    public int getHizSiniri() {
        return hizSiniri;
    }

    public void setHizSiniri(int hizSiniri) {
        this.hizSiniri = hizSiniri;
    }

    public int getSinif() {
        return sinif;
    }

    public void setSinif(int sinif) {
        this.sinif = sinif;
    }


    @Override
    public String toString() {
        return this.aracTipi;
    }
}
