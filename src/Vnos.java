
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucija
 */
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
