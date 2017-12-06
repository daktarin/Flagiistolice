package eu.huras.marcin.flagiistolice;

/**
 * Created by Marcin on 2017-01-10.
 */

public class ZegarkiDataProvider {

    private String kraj_lista;
    private String stolica_lista;
    private int zegarki_img_res;
    private String zegarki_nazwa;
    private String zegarki_ocena;


    public ZegarkiDataProvider(String kraj_lista, String stolica_lista, int zegarki_img_res, String zegarki_nazwa, String zegarki_ocena) {
        this.setKraj_lista(kraj_lista);
        this.setStolica_lista(stolica_lista);
        this.setMovie_res(zegarki_img_res);
        this.setZegarki_nazwa(zegarki_nazwa);
        this.setZegarki_ocena(zegarki_ocena);


    }

    public int getMovie_res() {
        return zegarki_img_res;
    }

    public void setMovie_res(int zegarki_img_res) {
        this.zegarki_img_res = zegarki_img_res;

    }

    public String getZegarki_nazwa() {
        return zegarki_nazwa;
    }

    public void setZegarki_nazwa(String zegarki_nazwa) {
        this.zegarki_nazwa = zegarki_nazwa;
    }

    public String getZegarki_ocena() {
        return zegarki_ocena;
    }

    public void setZegarki_ocena(String zegarki_ocena) {
        this.zegarki_ocena = zegarki_ocena;
    }

    public String getKraj_lista() {
        return kraj_lista;
    }

    public void setKraj_lista(String kraj_lista) {
        this.kraj_lista = kraj_lista;
    }

    public String getStolica_lista() {
        return stolica_lista;
    }

    public void setStolica_lista(String stolica_lista) {
        this.stolica_lista = stolica_lista;
    }
}

