package eu.huras.marcin.flagiistolice;

/**
 * Created by Marcin on 2017-07-24.
 */

public class Panstwo {

    String jezyk;
    String liczbaMieszkancow;
    String nazwa;
    String opis;
    String powierzchnia;
    String religia;
    String stolica;
    String ustroj;
    String waluta;




    public Panstwo() {

    }

    public Panstwo(String jezyk, String liczbaMieszkancow, String nazwa, String opis, String powierzchnia, String religia, String stolica, String ustroj, String waluta) {
        this.jezyk = jezyk;
        this.liczbaMieszkancow = liczbaMieszkancow;
        this.nazwa = nazwa;
        this.opis = opis;
        this.powierzchnia = powierzchnia;
        this.religia = religia;
        this.stolica = stolica;
        this.ustroj = ustroj;
        this.waluta = waluta;
    }

    public String getNazwa() {

        return nazwa;
    }

    public String getLiczbaMieszkancow() {
        return liczbaMieszkancow;
    }

    public String getJezyk() {
        return jezyk;
    }

    public String getWaluta() {
        return waluta;
    }

    public String getUstroj() {
        return ustroj;
    }

    public String getStolica() {
        return stolica;
    }

    public String getReligia() {
        return religia;
    }

    public String getPowierzchnia() {
        return powierzchnia;
    }

    public String getOpis() {
        return opis;
    }
}