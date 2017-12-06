package eu.huras.marcin.flagiistolice;

/**
 * Created by Marcin on 2017-01-30.
 */

public class WynikiDataProvider {

    private String id_res;
    private String obszar_res;
    private String poprawne_res;


    public WynikiDataProvider (String id_res, String obszar_res, String poprawne_res) {

        this.setId_res(id_res);
        this.setObszar_res(obszar_res);
        this.setPoprawne_res(poprawne_res);

    }

    public String getId_res() {
        return id_res;
    }

    public void setId_res(String id_res) {
        this.id_res = id_res;
    }

    public String getObszar_res() {
        return obszar_res;
    }

    public void setObszar_res(String obszar_res) {
        this.obszar_res = obszar_res;
    }

    public String getPoprawne_res() {
        return poprawne_res;
    }

    public void setPoprawne_res(String poprawne_res) {
        this.poprawne_res = poprawne_res;
    }



}
