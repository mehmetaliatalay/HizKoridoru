package mehmetali.com.hizkoridoru.Model;

public class MesafeUcret {

    private int id;
    private float mesafe;
    private String ucret;


    public MesafeUcret(int id, float mesafe, String ucret) {
        this.id = id;
        this.mesafe = mesafe;
        this.ucret = ucret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMesafe() {
        return mesafe;
    }

    public void setMesafe(float mesafe) {
        this.mesafe = mesafe;
    }

    public String getUcret() {
        return ucret;
    }

    public void setUcret(String ucret) {
        this.ucret = ucret;
    }
}



