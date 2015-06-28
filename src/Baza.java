
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
