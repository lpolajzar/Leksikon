
import java.io.Serializable;


public class Vnos implements Serializable {
    
    private String ime;
    private String opis;

    public Vnos() {
    }
    
    public Vnos(String ime, String opis) {
        this.ime = ime;
        this.opis= opis;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    
}
