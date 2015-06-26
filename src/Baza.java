
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucija
 */
public class Baza implements Serializable {
    
    private List<Vnos> vnosi;
    private List<String> imena;

    public Baza() {
        vnosi = new ArrayList<Vnos>();
        imena = new ArrayList<String>();
    }

    public List<Vnos> getVnosi() {
        return vnosi;
    }

    public void setVnosi(List<Vnos> vnosi) {
        this.vnosi = vnosi;
    }

    public List<String> getImena() {
        return imena;
    }

    public void setImena(List<String> imena) {
        this.imena = imena;
    }
    
    
}
