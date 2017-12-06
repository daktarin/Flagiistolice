package eu.huras.marcin.flagiistolice;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static eu.huras.marcin.flagiistolice.R.string.text_ocen_apke_dialog_pozniej;
import static eu.huras.marcin.flagiistolice.R.string.text_ocen_apke_dialog_tak;
import static eu.huras.marcin.flagiistolice.R.string.text_ocen_apke_dialog_tresc;
import static eu.huras.marcin.flagiistolice.R.string.text_ocen_apke_dialog_tytul;


public class MainActivity extends BaseActivity implements View.OnTouchListener, OnMapReadyCallback {
    String kraj_lista;
    String stolica_lista;
    int v;

    GestureDetector gs = null;


    Map<String, String> map;
    //mapa konturowa /////////////////////////////////////
    WebView web_wiev;
    WebAppInterface wap;
    private TextView myTextView;
    final Handler myHandler = new Handler();
    ///////////////////////////////////////////////////////

    int xCoOrdinate;
    int yCoOrdinate;

    String sciezka;

    ImageView iv_flaga_test;

    int lastImageRef;

    RelativeLayout rl_nic_do_powtorki;
    private static final int ILOSC_PANSTW = 214;

    ///////////////////////////////////////////zminne uzyte do pojedynku
    int l_losowa_z_zakresu_0_3, pktGraczA, pktGraczB;
    String[] arrayNazwyPanstwPojedynek = new String[4];
    String[] arrayNazwyStolicPojedynek = new String[4];
    int[] arrayFlagiPanstwPojedynek = new int[4];
    boolean flaga11Powtorki = false;
    int licznikPetliWhilePowtorki = 0;
    Random randomizePowtorki = new Random();
    int liczbaLosowa1Powtorki;

    Handler handlerPowtorkiLay = new Handler();
    Runnable myRunablePowtorkiLay;

    boolean b_czy_odgadnieto_w_pojedynku = false;
    boolean b_czy_koniec_pojedynku = false;

    private static final int ILOSC_PYTAN_W_POJEDYNKU = 10;

    //////////////////////////////

    String[] arrayNumber;
    String[] arrayNazwyPanstw;
    String[] arrayStolicePanstw;
    int[] arrayFlagiPanst;

    ViewPager viewPager;
    CustoSwype customSwype;


    boolean czy_wybor_testu;

    //tak naprawdę to bazy sqlite z wynikami testów zapisywane są te wartości
    String[] array_uniwersalne_nazwy_kontynentow = {"Cały świat", "Europa", "Ameryka Północna", "Ameryka Południowa", "Afryka", "Azja", "Australia"};
    //zmienna pomocnicza służąca do wyświetlania filtrowanych wyników dla poszcezgólnych kontynentów
    int licznikFiltrowania = 7;

    String kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[0];

    LinearLayout btn_obszar_lista_wynikow;

    //deklaracja zmiennej globalnej odpowiedzialnej za to, czy wyniki mają być sortowane czy też nie
    //zmienna moze przyjmować wartości "nie_sortuj" - wartość domyślna, "sortuj_malejaco", "sortuj_rosnaco"
    //wartość zmiennej zmienia się wraz z kliknięciami tekstu "POPRAWNE" na ekranie z wynikami testów
    String jak_sortowac = "nie_sortuj";

    Circle circleMapGoogleShape;

    DatabaseReference myRef;
    String wybranePanstwo;

    CountDownTimer countDownTimer;

    //linie odpowiedzialne za pojawienie się okna oceny aplikacji
    public static final String preference = "pref";
    public static final String saveIt = "saveKey";
    SharedPreferences sp;
    Boolean f_okno_ocen_aplikacje;

    public static final String preferenceInt = "prefInt";
    public static final String saveItInt = "saveKeyInt";
    SharedPreferences spInt;
    int iloscUruchomien;

    AlertDialog.Builder okno;

    Boolean boolean_przewodnik;

    Boolean isFirstRun;

    private ViewPager view_pager;
    private ViewPager view_pager_powtorki;
    private ViewPageAdapter viewPageAdapter;
    private LinearLayout layout_dots;
    private TextView[] dots;
    private int[] layouts;
    private TextView btn_next, btn_skip;

    ImageView btn_napisz_do_mnie;
    ImageView btn_caly_swiat;
    ImageView btn_europa;
    ImageView btn_a_polnocna;
    ImageView btn_a_poludniowa;
    ImageView btn_afryka;
    ImageView btn_azja;
    ImageView btn_australia;
    ImageView btn_wersja_polska;
    ImageView btn_wersja_angielska;
    ImageView btn_wersja_niemiecka;
    ImageView btn_inne_apki;
    ImageView btn_ocen_aplikacje;
    ImageView btn_przewodnik;

    ImageView mapa_swiata;
    RelativeLayout lay_mapa;


    private Rect rect;

    private static final int CZAS_TRWANIA_SPLASH_SCREENU = 2000;

    int global_ilosc_pytan_w_tescie = 20;
    int global_nr_aktualnego_pytania_w_tescie = 1;

    public String jezyk;

    int dlugoscTablicy = ILOSC_PANSTW;
    int[] flagi_img_array = new int[dlugoscTablicy];
    int[] array_flagi_wybranych_panstw_losowo = new int[dlugoscTablicy];

    int[] array_flagi_wybranych_panstw_alfabetycznie = new int[dlugoscTablicy];
    String[] array_nazwy_panstw;
    String[] array_panstwa_wybrane_alfabetycznie = new String[dlugoscTablicy];
    String[] array_panstwa_wybrane_losowo = new String[dlugoscTablicy];
    String[] array_nazwy_stolic;
    String[] array_stolice_wybrane_alfabetycznie = new String[dlugoscTablicy];
    String[] array_stolice_wybrane_losowo = new String[dlugoscTablicy];
    String[] array_nazwy_kontynentow;
    ZegarkiAdapter adapter;

    int[] array_flagi_wybranych_panstw_test = new int[dlugoscTablicy];
    String[] array_panstwa_test = new String[dlugoscTablicy];
    String[] array_stolice_test = new String[dlugoscTablicy];

    String[] array_pomocnicza_do_testu = {"xyz", "xyz", "xyz", "xyz"};

    int globaldobrze = 0;
    int globalzle = 0;
    String rodzaj_testu = "test-flagi";

    // [1] - splash screen || [2] - wybor || [3] - nauka wybor obszaru || [4] - nauka  || [5] - test wybor obszaru || [6] - test || [7] - przewodnik po aplikacji
    // [8] - - wybór testu [11] - gdy kończmy test [1] - jak wejdziemy w testy i powtórki
    //  [13] - gdy wybor trybu nauki
    int ekran;
    private static long back_pressed;
    //1 - generuje naukę 2 - generuje test 3 - generuje pojedynek
    int generuj_widok_wyboru_nauka;

    Boolean flaga_losowo = false;

    TextView txt_Ekran_Nauka_tytul;

    Random rand = new Random();
    int temp = 0;

    Handler handler = new Handler();
    Runnable myRunable;

    ListView listView;
    ListView listViewWyniki;
    WynikiAdapter adapter_wyniki;

    View view_layout_splash_screen = null;
    View view_layout_wybor_nauka_test_koniec = null;
    View view_layout_nauka_wybor_obszaru = null;
    View view_layout_nauka = null;
    View view_layout_test = null;
    View view_layout_wyniki_testu = null;
    View view_layout_twoje_wyniki = null;
    View view_layout_powiadomienie_o_nowszej_wersji = null;
    View view_layout_ustawienia = null;
    View view_layout_intro = null;
    View view_layout_opis_panstwa = null;
    View view_layout_wybor_testu = null;
    View view_layout_powtorki = null;
    View view_layout_pojedynek = null;
    View view_layout_wynik_pojedynku = null;
    View view_layout_wybor_nauka_mapa_lista = null;
    View view_layout_podglad_mapy = null;

    public DataBaseHelper myDb;
    public DataBaseHelperPowtorki_PL myDbPowtorki_PL;
    public DataBaseHelperPowtorki_EN myDbPowtorki_EN;
    public DataBaseHelperPowtorki_DE myDbPowtorki_DE;
    public Cursor respowtorki;

    Cursor res;

    double procent;
    String kontynent;

    boolean f_przeczytano_database = false;
    ProgressDialog progressDialog;

    int licznik_database = 0;

//    int verCode;
//    String version;
//    boolean polaczenieZSiecia;

//    String path = "http://huras.eu/numer_wersji.txt";

    Locale myLocale;

    GoogleMap mGoogleMap;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ustawienie Full Screena aplikacji
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //zabezpieczenie przed zrobieniem screen shota
        //nie ma to rzadnego aspektu praktycznego w przypadku tej apki, ale chodziło o to żeby się nauczyć jak to robić ;)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        Locale current = getResources().getConfiguration().locale;
//        String temp = current.toString();
//      Toast.makeText(MainActivity.this, "PPPPP = " + temp, Toast.LENGTH_LONG).show();


        setContentView(R.layout.activity_main);
        myRef = FirebaseDatabase.getInstance().getReference("kraj");

        czy_wybor_testu = false;

        //pobieram wartość logiczną z sp
        sp = getSharedPreferences(preference, MainActivity.MODE_PRIVATE);
        f_okno_ocen_aplikacje = sp.getBoolean(saveIt, false);
        //

        //pobieram wartość ilości uruchomień aplikacji z spInt i zapisuje ją do zmiennej iloscUruchomien
        spInt = getSharedPreferences(preferenceInt, MainActivity.MODE_PRIVATE);
        iloscUruchomien = spInt.getInt(saveItInt, 1);

        okno = new AlertDialog.Builder(MainActivity.this);
        okno.setMessage(text_ocen_apke_dialog_tresc);
        okno.setCancelable(true);
        okno.setTitle(text_ocen_apke_dialog_tytul);

        okno.setPositiveButton(text_ocen_apke_dialog_tak, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp = getSharedPreferences(preference, MainActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(saveIt, true);
                editor.commit();
                f_okno_ocen_aplikacje = true;

                ocen_aplikacje();
                finish();

            }
        });


        okno.setNeutralButton(text_ocen_apke_dialog_pozniej, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp = getSharedPreferences(preference, MainActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(saveIt, false);
                editor.commit();
                f_okno_ocen_aplikacje = false;

                dialog.dismiss();
                finish();
            }
        });

        boolean_przewodnik = false;

        jezyk = getResources().getString(R.string.wersja_jezykowa);
        //     Toast.makeText(MainActivity.this, "TU = " + jezyk, Toast.LENGTH_LONG).show();

        // funkcja odpowiedzialna za zczytywanie obrazków flag z pliku arrays.xml
        TypedArray ar = getResources().obtainTypedArray(R.array.flagi_img_res);
        TypedArray ar1 = getResources().obtainTypedArray(R.array.array_flagi_wybranych_panstw_losowo);
        int len = ar.length();

        for (int i = 0; i < len; i++) {
            flagi_img_array[i] = ar.getResourceId(i, 0);
            array_flagi_wybranych_panstw_losowo[i] = ar1.getResourceId(i, 0);
            // Toast.makeText(MainActivity.this, " = " + flagi_img_array[i], Toast.LENGTH_LONG).show();
        }

        ar.recycle();
        ar1.recycle();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pokaz_i_ukryj_layouty();//to jest tak naprawdę funkcja rozruchowa
        ustawFonty();

        if (jezyk.equals("pl")) {
            btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
            btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
            btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_negatyw);
            btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_normal);
            btn_wersja_polska.setEnabled(false);
            btn_wersja_angielska.setEnabled(true);
            myDbPowtorki_PL = new DataBaseHelperPowtorki_PL(this);
        } else if (jezyk.equals("en")) {
            btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
            btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
            btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_normal);
            btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_negatyw);
            btn_wersja_polska.setEnabled(true);
            btn_wersja_angielska.setEnabled(false);
            myDbPowtorki_EN = new DataBaseHelperPowtorki_EN(this);
        } else if (jezyk.equals("de")) {
            btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
            btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
            btn_wersja_niemiecka = (ImageView) findViewById(R.id.btn_wersja_niemiecka);
            btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_normal);
            btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_normal);
            btn_wersja_niemiecka.setBackgroundResource(R.drawable.btn_lang_de_negatyw);
            btn_wersja_polska.setEnabled(true);
            btn_wersja_angielska.setEnabled(true);
            btn_wersja_niemiecka.setEnabled(false);
            myDbPowtorki_DE = new DataBaseHelperPowtorki_DE(this);
        }

        listView = (ListView) findViewById(R.id.listView);

        //////////////TABLICE Z ZASOBAMI : OBRAZKI : NAZWY STOLIC : NAZWY PAŃSTW
        array_nazwy_kontynentow = getResources().getStringArray(R.array.kontynenty_nazwa);
        array_nazwy_panstw = getResources().getStringArray(R.array.panstwa_nazwa);
        array_nazwy_stolic = getResources().getStringArray(R.array.stolice_nazwa);
        ////////////////////////////////////
        array_panstwa_wybrane_losowo = getResources().getStringArray(R.array.panstwa_nazwa);
        array_stolice_wybrane_losowo = getResources().getStringArray(R.array.stolice_nazwa);

        myDb = new DataBaseHelper(this);


        listViewWyniki = (ListView) findViewById(R.id.lv);

        //informacje potrzebne do sprawdzenia aktualności zainstalowanej wersji aplikacji
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        version = pInfo.versionName;
//        verCode = pInfo.versionCode;

        // wpisy potrzebne do popięcia pod przyciski on touch listenera i funkcji
        // wykonywanych po kliknięciu przycisku
        btn_napisz_do_mnie = (ImageView) findViewById(R.id.btn_napisz_do_mnie);
        btn_caly_swiat = (ImageView) findViewById(R.id.btn_caly_swiat);
        btn_europa = (ImageView) findViewById(R.id.btn_europa);
        btn_a_polnocna = (ImageView) findViewById(R.id.btn_a_polnocna);
        btn_a_poludniowa = (ImageView) findViewById(R.id.btn_a_poludniowa);
        btn_afryka = (ImageView) findViewById(R.id.btn_afryka);
        btn_azja = (ImageView) findViewById(R.id.btn_azja);
        btn_australia = (ImageView) findViewById(R.id.btn_australia);
        btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
        btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
        btn_wersja_niemiecka = (ImageView) findViewById(R.id.btn_wersja_niemiecka);
        btn_inne_apki = (ImageView) findViewById(R.id.btn_inne_apki);
        btn_ocen_aplikacje = (ImageView) findViewById(R.id.btn_ocen_aplikacje);
        btn_przewodnik = (ImageView) findViewById(R.id.btn_przewodnik);

        //mapa_swiata = (ImageView) findViewById(R.id.iv_mapa_swiata);
        //  lay_mapa = (RelativeLayout) findViewById(R.id.lay_mapa);


        btn_napisz_do_mnie.setOnTouchListener(this);
        btn_caly_swiat.setOnTouchListener(this);
        btn_europa.setOnTouchListener(this);
        btn_a_polnocna.setOnTouchListener(this);
        btn_a_poludniowa.setOnTouchListener(this);
        btn_afryka.setOnTouchListener(this);
        btn_azja.setOnTouchListener(this);
        btn_australia.setOnTouchListener(this);
        btn_wersja_polska.setOnTouchListener(this);
        btn_wersja_angielska.setOnTouchListener(this);
        btn_wersja_niemiecka.setOnTouchListener(this);
        btn_inne_apki.setOnTouchListener(this);
        btn_ocen_aplikacje.setOnTouchListener(this);
        btn_przewodnik.setOnTouchListener(this);

        //sprawdzanie czy aplikacja uruchomiona jest po raz pierwszy

        //jeżeli preferencja nie istnieje to zmiennej isFirstRun przypisywana jest wartośc true
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);


        if (isFirstRun) {

            //Toast.makeText(MainActivity.this, "FIRST", Toast.LENGTH_LONG).show();
            //zminnej isFirstRun przypisywana jest wartość false
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isfirstrun", false).commit();
        }

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_next = (TextView) findViewById(R.id.btn_next);


        layouts = new int[]{
                R.layout.activity_screen_walk_1,
                R.layout.activity_screen_walk_2,
                R.layout.activity_screen_walk_3,
                R.layout.activity_screen_walk_4
        };

        addBottomDots(0);

        viewPageAdapter = new ViewPageAdapter();
        view_pager.setAdapter(viewPageAdapter);
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener);

        if (googleServicesAvaliable()) {
//            Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
            initMap();
        }

        btn_obszar_lista_wynikow = (LinearLayout) findViewById(R.id.btn_obszar_lista_wynikow);
        btn_obszar_lista_wynikow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrujDane();
            }
        });

        web_wiev = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = web_wiev.getSettings();

        wap = new WebAppInterface(this);
        web_wiev.addJavascriptInterface(wap, "Android");
        web_wiev.setWebViewClient(new WebViewClient());

        web_wiev.getSettings().setJavaScriptEnabled(true);

        web_wiev.getSettings().setSupportZoom(true);
        web_wiev.getSettings().setBuiltInZoomControls(true);
        web_wiev.getSettings().setDisplayZoomControls(false);
        web_wiev.getSettings().setUseWideViewPort(true);
        web_wiev.getSettings().setLoadWithOverviewMode(true);
        web_wiev.setInitialScale(1);

        //
//        web_wiev.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (gs == null) {
//                    gs = new GestureDetector(
//                            new GestureDetector.SimpleOnGestureListener() {
//                                @Override
//                                public boolean onDoubleTapEvent(MotionEvent e) {
//                                    //MyActivity.this.finish();
//                                    Toast.makeText(MainActivity.this, "DOUBLE TAP", Toast.LENGTH_SHORT).show();
//                                    return false;
//                                }
//                            });
//                }
//                gs.onTouchEvent(event);
//
//                return false;
//            }
//        });
        //

        //web_wiev.loadUrl("file:///android_asset/testhtml2.html");
        web_wiev.loadUrl("file:///android_asset/index.html");

        kodyPanstw();
        // String temp = Resources.getSystem().getConfiguration().locale.toString().substring(0,2);

        //Toast.makeText(MainActivity.this, "Język na fonie to " + temp, Toast.LENGTH_LONG).show();
        //koniec onCreate
    }

    private void kodyPanstw() {
        map = new HashMap<String, String>();
        map.put("CW", "curacao");
        map.put("VI", "wyspydziewicze");
        map.put("YT", "mayotte");
        map.put("GL", "grenlandia");
        map.put("BM", "bermudy");
        map.put("BL", "saintbarthelemy");
        map.put("AX", "wyspyalandzkie");
        map.put("AW", "aruba");
        map.put("AS", "samoaamerykanskie");
        map.put("AI", "anguilla");
        map.put("AF", "afganistan");
        map.put("AL", "albania");
        map.put("DZ", "algieria");
        map.put("AD", "andora");
        map.put("AO", "angola");
        map.put("AG", "antiguaibarbuda");
        map.put("SA", "arabiasaudyjska");
        map.put("AR", "argentyna");
        map.put("AM", "armenia");
        map.put("AU", "australia");
        map.put("AT", "austria");
        map.put("AZ", "azerbejdzan");
        map.put("BS", "bahamy");
        map.put("BH", "bahrajn");
        map.put("BD", "bangladesz");
        map.put("BB", "barbados");
        map.put("BE", "belgia");
        map.put("BZ", "belize");
        map.put("BJ", "benin");
        map.put("BT", "bhutan");
        map.put("BY", "bialorus");
        map.put("BO", "bolivia");
        map.put("BA", "bosniaihercegowina");
        map.put("BW", "botswana");
        map.put("BR", "brazylia");
        map.put("BN", "brunei");
        map.put("BG", "bulgaria");
        map.put("BF", "burkinafaso");
        map.put("BI", "burundi");
        map.put("CL", "chile");
        map.put("CN", "chiny");
        map.put("HR", "chorwacja");
        map.put("CY", "cypr");
        map.put("TD", "czad");
        map.put("ME", "czarnogora");
        map.put("CZ", "czechy");
        map.put("DK", "dania");
        map.put("CD", "kongo");
        map.put("DM", "dominika");
        map.put("DO", "dominikana");
        map.put("DJ", "dzibuti");
        map.put("EG", "egipt");
        map.put("EC", "ekwador");
        map.put("ER", "erytrea");
        map.put("EE", "estonia");
        map.put("ET", "etiopia");
        map.put("FJ", "fidzi");
        map.put("PH", "filipiny");
        map.put("FI", "finlandia");
        map.put("FR", "francja");
        map.put("GA", "gabon");
        map.put("GM", "gambia");
        map.put("GH", "ghana");
        map.put("GR", "grecja");
        map.put("GD", "grenada");
        map.put("GE", "gruzja");
        map.put("GY", "gujana");
        map.put("GT", "gwatemala");
        map.put("GN", "gwinea");
        map.put("GW", "gwineabissau");
        map.put("GQ", "gwinearownikowa");
        map.put("HT", "haiti");
        map.put("ES", "hiszpania");
        map.put("NL", "holandia");
        map.put("HN", "honduras");
        map.put("IN", "indie");
        map.put("ID", "indonezja");
        map.put("IQ", "irak");
        map.put("IR", "iran");
        map.put("IE", "irlandia");
        map.put("IS", "islandia");
        map.put("IL", "izrael");
        map.put("JP", "japonia");
        map.put("JM", "jamajka");
        map.put("YE", "jemen");
        map.put("JO", "jordania");
        map.put("KH", "kambodza");
        map.put("CM", "kamerun");
        map.put("CA", "kanada");
        map.put("QA", "katar");
        map.put("KZ", "kazachstan");
        map.put("KE", "kenia");
        map.put("KG", "kirgistan");
        map.put("KI", "kiribati");
        map.put("CO", "kolumbia");
        map.put("KM", "komory");
        map.put("CG", "kongo");
        map.put("KR", "koreapoludniowa");
        map.put("KP", "koreapolnocna");
        map.put("XK", "kosowo");
        map.put("CR", "kostaryka");
        map.put("CU", "kuba");
        map.put("KW", "kuwejt");
        map.put("LA", "laos");
        map.put("LS", "lesoto");
        map.put("LB", "liban");
        map.put("LR", "liberia");
        map.put("LY", "libia");
        map.put("LI", "lichtenstein");
        map.put("LT", "litwa");
        map.put("LU", "luksemburg");
        map.put("LV", "lotwa");
        map.put("MK", "macedonia");
        map.put("MG", "madagaskar");
        map.put("MW", "malawi");
        map.put("MV", "malediwy");
        map.put("MY", "malezja");
        map.put("ML", "mali");
        map.put("MT", "malta");
        map.put("MA", "maroko");
        map.put("MR", "mauretania");
        map.put("MU", "mauritius");
        map.put("MX", "meksyk");
        map.put("FM", "mikronezja");
        map.put("MM", "mjanma");
        map.put("MD", "moldawia");
        map.put("MC", "monako");
        map.put("MN", "mongolia");
        map.put("MZ", "mozambik");
        map.put("NA", "namibia");
        map.put("NR", "nauru");
        map.put("NP", "nepal");
        map.put("DE", "niemcy");
        map.put("NE", "niger");
        map.put("NG", "nigeria");
        map.put("NI", "nikaragua");
        map.put("NZ", "nowazelandia");
        map.put("NO", "norwegia");
        map.put("OM", "oman");
        map.put("PW", "palau");
        map.put("PA", "panama");
        map.put("PK", "pakistan");
        map.put("PG", "papuanowagwinea");
        map.put("PY", "paragwaj");
        map.put("PE", "peru");
        map.put("PL", "polska");
        map.put("ZA", "rpa");
        map.put("PT", "portugalia");
        map.put("CF", "bangi");
        map.put("CV", "repzielonegoprzyladka");
        map.put("RU", "rosja");
        map.put("RO", "rumunia");
        map.put("RW", "rwanda");
        map.put("KN", "saintkittsinevis");
        map.put("LC", "saintlucia");
        map.put("VC", "saintvincentigrenadyny");
        map.put("SV", "salwador");
        map.put("WS", "samoa");
        map.put("SM", "sanmarino");
        map.put("SN", "senegal");
        map.put("RS", "serbia");
        map.put("SC", "seszele");
        map.put("SL", "sierraleone");
        map.put("SG", "singapur");
        map.put("SK", "slowacja");
        map.put("SI", "slowenia");
        map.put("SO", "somalia");
        map.put("LK", "srilanka");
        map.put("US", "usa");
        map.put("SZ", "suazi");
        map.put("SD", "sudan");
        map.put("SS", "sudanpoludniowy");
        map.put("SR", "surinam");
        map.put("SY", "syria");
        map.put("CH", "szwajcaria");
        map.put("SE", "szwecja");
        map.put("TJ", "tadzykistan");
        map.put("TH", "tajlandia");
        map.put("TZ", "tanzania");
        map.put("TL", "timor");
        map.put("TG", "togo");
        map.put("TO", "tonga");
        map.put("TT", "trynidad");
        map.put("TN", "tunezja");
        map.put("TR", "turcja");
        map.put("TM", "turkmenistan");
        map.put("TV", "tuvalu");
        map.put("UG", "uganda");
        map.put("UA", "ukraina");
        map.put("UY", "urugwaj");
        map.put("UZ", "uzbekistan");
        map.put("VU", "vanuatu");
        map.put("VA", "watykan");
        map.put("VE", "wenezuela");
        map.put("HU", "wegry");
        map.put("GB", "wielkabrytania");
        map.put("VN", "wietnam");
        map.put("IT", "wlochy");
        map.put("CI", "wybrzezekoscisloniowej");
        map.put("MH", "wyspymarshalla");
        map.put("SB", "wyspysalomona");
        map.put("ST", "wyspyswtomasza");
        map.put("ZM", "zambia");
        map.put("ZW", "zimbabwe");
        map.put("AE", "zjednoczoneemiraty");
        map.put("", "abchazja");
        map.put("", "cyprpolnocny");
        map.put("", "karabach");
        map.put("", "naddniestrze");
        map.put("", "osetiapoludniowa");
        map.put("PS", "palestyna");
        map.put("EH", "sahara");
        map.put("", "somaliland");
        map.put("TW", "tajwan");

    }

    private void initMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public void btnSkipClick(View v) {
        launchHomeScreen();
    }

    public void btnNextClick(View v) {

        int current = getItem(1);
        if (current < layouts.length) {
            view_pager.setCurrentItem(current);
        } else {
            launchHomeScreen();
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == layouts.length - 1) {
                btn_next.setText(getString(R.string.text_start));
                btn_skip.setVisibility(View.GONE);
            } else {
                btn_next.setText(getString(R.string.text_next));
                btn_skip.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                v.setAlpha(0.7f);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

                }
                break;
            case MotionEvent.ACTION_UP:
                v.setAlpha(1.0f);

                switch (v.getId()) {

                    case R.id.btn_wersja_niemiecka:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            refreshAuto(LocaleManager.LANGUAGE_GERMANY);
                            // ustaw_de();
                        }
                        break;

                    case R.id.btn_wersja_angielska:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            refreshAuto(LocaleManager.LANGUAGE_ENGLISH);
                            //  ustaw_en();
                        }
                        break;

                    case R.id.btn_wersja_polska:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            refreshAuto(LocaleManager.LANGUAGE_POLISH);
                            //  ustaw_pl();
                        }
                        break;

                    case R.id.btn_napisz_do_mnie:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            napisz_do_mnie1();
                        }
                        break;

                    case R.id.btn_inne_apki:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            zobacz_inne_apki();
                        }
                        break;

                    case R.id.btn_ocen_aplikacje:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            ocen_aplikacje();
                        }
                        break;

                    case R.id.btn_przewodnik:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            pokazPrzewodnik();
                        }
                        break;

                    case R.id.btn_caly_swiat:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_europa:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_a_polnocna:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_a_poludniowa:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_afryka:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_azja:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;

                    case R.id.btn_australia:
                        if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            generujListe(v);
                        }

                        break;
                }
                //
                break;

            case MotionEvent.ACTION_CANCEL:
                v.setAlpha(1.0f);
                break;


        }

        return true;
    }

// private void sprawdzCzyPlikIstnieje() {
//
//  CheckFileExistTask task=new CheckFileExistTask();
//  task.execute(path);
//
//
// }

    public void ustaw_pl() {
//        btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
//        btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
//        btn_wersja_niemiecka = (ImageView) findViewById(R.id.btn_wersja_niemiecka);
//
//        btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_negatyw);
//        btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_normal);
//        btn_wersja_niemiecka.setBackgroundResource(R.drawable.btn_lang_de_normal);
//        btn_wersja_polska.setEnabled(false);
//        btn_wersja_angielska.setEnabled(true);
//        btn_wersja_niemiecka.setEnabled(true);
//
//        setLocale("pl");
    }

    public void ustaw_en() {
//        btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
//        btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
//        btn_wersja_niemiecka = (ImageView) findViewById(R.id.btn_wersja_niemiecka);
//
//        btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_normal);
//        btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_negatyw);
//        btn_wersja_niemiecka.setBackgroundResource(R.drawable.btn_lang_de_normal);
//        btn_wersja_polska.setEnabled(true);
//        btn_wersja_angielska.setEnabled(false);
//        btn_wersja_niemiecka.setEnabled(true);
//
//        setLocale("en");
    }

    public void ustaw_de() {
//        btn_wersja_polska = (ImageView) findViewById(R.id.btn_wersja_polska);
//        btn_wersja_angielska = (ImageView) findViewById(R.id.btn_wersja_angielska);
//        btn_wersja_niemiecka = (ImageView) findViewById(R.id.btn_wersja_niemiecka);
//        btn_wersja_polska.setBackgroundResource(R.drawable.btn_lang_pl_normal);
//        btn_wersja_angielska.setBackgroundResource(R.drawable.btn_lang_en_normal);
//        btn_wersja_niemiecka.setBackgroundResource(R.drawable.btn_lang_de_negatyw);
//        btn_wersja_polska.setEnabled(true);
//        btn_wersja_angielska.setEnabled(true);
//        btn_wersja_niemiecka.setEnabled(false);
//
//        setLocale("de");

    }


//    public void setLocale(String lang) {
//
//        myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, MainActivity.class);
//        startActivity(refresh);
//
//        this.finish();
//
//    }

    public void napisz_do_mnie1() {

        Intent intent22 = new Intent(Intent.ACTION_SENDTO);
        intent22.setType("text/plain");
        intent22.setData(Uri.parse("mailto:marcin@huras.eu"));
        intent22.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent22);
    }

    public void ocen_aplikacje() {

        try {

            Uri uri1 = Uri.parse("market://details?id=" + getPackageName());
            Intent gotoMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(gotoMarket1);
        } catch (ActivityNotFoundException e) {
            Uri uri1 = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
            Intent gotoMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(gotoMarket1);
        }
    }

    public void zobacz_inne_apki() {

        try {

            Uri uri1 = Uri.parse("market://search?q=Marcin Huras");
            Intent gotoMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(gotoMarket1);
        } catch (ActivityNotFoundException e) {
            Uri uri1 = Uri.parse("http://play.google.com/store/search?q=Marcin Huras");
            Intent gotoMarket1 = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(gotoMarket1);
        }
    }

//private class CheckFileExistTask extends AsyncTask<String, Void, Boolean>
// {
//  @Override
//  protected void onPreExecute() {
//
//  }
//  @Override
//  protected Boolean doInBackground(String... params) {
//   try {
//    // This connection won't follow redirects returned by the remote server.
//    HttpURLConnection.setFollowRedirects(false);
//    // Open connection to the remote server
//    URL url=new URL(params[0]);
//    HttpURLConnection con =  (HttpURLConnection) url.openConnection();
//    // Set request method
//    con.setRequestMethod("HEAD");
//    // get returned code
//    return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
//
//   }
//   catch (Exception e) {
//    e.printStackTrace();
//
//    return false;
//
//   }
//  }
//  @Override
//  protected void onPostExecute(Boolean result) {
//   // Update status message
//   if (result==true)
//   {
//    //PLIK ISTNIEJE
//    porwonajWersje();
//
//   }
//   else
//   {
//    //PLIK NIE ISTNIEJE
//       // pokaz_layout_wybor_nauka_test_koniec();
//        zdecydujCzyPokazacInstro();
//
//   }
//  }
// }

    private void zdecydujCzyPokazacInstro() {
        view_layout_splash_screen.setVisibility(View.INVISIBLE);


        if (isFirstRun) {
            view_layout_intro.setVisibility(View.VISIBLE);
            ekran = 8;
        } else {
            pokaz_layout_wybor_nauka_test_koniec();
        }


    }
//
//    private void porwonajWersje() {
//
//
//
//        new Thread() {
//            @Override
//            public void run() {
//
//                URL u = null;
//                try {
//                    u = new URL(path);
//                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
//                    c.setRequestMethod("GET");
//                    c.connect();
//                    InputStream in = c.getInputStream();
//                    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[2];
//                    in.read(buffer); // Read from Buffer.
//                    bo.write(buffer); // Write Into Buffer.
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            String tempZainstalowana = version;
//                            String tempNajnowsza =  String.valueOf(bo);
//                            Toast.makeText(MainActivity.this, tempZainstalowana + " : " + tempNajnowsza, Toast.LENGTH_SHORT).show();
//                            if (tempZainstalowana.equals(tempNajnowsza)) {
//                                //Twoja wersja jest aktualna
//                               //pokaz_layout_wybor_nauka_test_koniec();
//                                zdecydujCzyPokazacInstro();
//                            } else {
//                                //Zainstaluj sobie nowszą wersję
//                               view_layout_splash_screen.setVisibility(View.INVISIBLE);
//                               view_layout_powiadomienie_o_nowszej_wersji.setVisibility(View.VISIBLE);
//                               ekran = 5;
//                            }
//
//
//                            try {
//                                bo.close();
//                            } catch (IOException e) {
//
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                } catch (MalformedURLException e) {
//
//                    e.printStackTrace();
//
//                } catch (ProtocolException e) {
//
//                    e.printStackTrace();
//
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//
//            }
//        }.start();
//    }

    private void ustawFonty() {

        TextView txt_flagi_i_stolice_swiata = (TextView) findViewById(R.id.txt_flagi_i_stolice_swiata);
        Typeface myFontBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        txt_flagi_i_stolice_swiata.setTypeface(myFontBold);

        TextView txt_wybierz_jezyk = (TextView) findViewById(R.id.txt_wybierz_jezyk);
        Typeface myFonLight = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");
        txt_wybierz_jezyk.setTypeface(myFonLight);

        TextView btn_next = (TextView) findViewById(R.id.btn_next);
        btn_next.setTypeface(myFonLight);

        TextView btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_skip.setTypeface(myFonLight);

        TextView txt_napisz_do_mnie = (TextView) findViewById(R.id.txt_napisz_do_mnie);
        txt_napisz_do_mnie.setTypeface(myFonLight);

        TextView txt_ocen_aplikacje = (TextView) findViewById(R.id.txt_ocen_aplikacje);
        txt_ocen_aplikacje.setTypeface(myFonLight);

        TextView txt_zobacz_inne_apki = (TextView) findViewById(R.id.txt_zobacz_inne_apki);
        txt_zobacz_inne_apki.setTypeface(myFonLight);

        TextView txt_info = (TextView) findViewById(R.id.txt_info);
        txt_info.setTypeface(myFonLight);

        TextView txt_btn_wstecz_wybor_obszaru = (TextView) findViewById(R.id.txt_btn_wstecz_wybor_obszaru);
        txt_btn_wstecz_wybor_obszaru.setTypeface(myFontBold);

        TextView txt_btn_zamknij = (TextView) findViewById(R.id.txt_btn_zamknij);
        txt_btn_zamknij.setTypeface(myFontBold);

        TextView txt_EkranWyboruNauka = (TextView) findViewById(R.id.txt_EkranWyboruNauka);
        txt_EkranWyboruNauka.setTypeface(myFontBold);

        TextView txt_WybierzTest = (TextView) findViewById(R.id.txt_WybierzTest);
        txt_WybierzTest.setTypeface(myFontBold);

        TextView txt_flagi_i_stolice_swiata_1 = (TextView) findViewById(R.id.txt_flagi_i_stolice_swiata_1);
        txt_flagi_i_stolice_swiata_1.setTypeface(myFontBold);

        TextView tv_btn_nauka = (TextView) findViewById(R.id.tv_btn_nauka);
        tv_btn_nauka.setTypeface(myFontBold);

        TextView tv_btn_pojedynek = (TextView) findViewById(R.id.tv_btn_pojedynek);
        tv_btn_pojedynek.setTypeface(myFontBold);

        View lay1 = findViewById(R.id.lay1);
        TextView txt1 = (TextView) lay1.findViewById(R.id.txt_gracz_A_i_B_nazwa);
        txt1.setTypeface(myFontBold);

        View lay2 = findViewById(R.id.lay2);
        TextView txt2 = (TextView) lay2.findViewById(R.id.txt_gracz_A_i_B_nazwa);
        txt2.setTypeface(myFontBold);

        TextView txt_gracz_A_liczba_punktow = (TextView) findViewById(R.id.txt_gracz_A_liczba_punktow);
        txt_gracz_A_liczba_punktow.setTypeface(myFontBold);

        TextView txt_gracz_B_liczba_punktow = (TextView) findViewById(R.id.txt_gracz_B_liczba_punktow);
        txt_gracz_B_liczba_punktow.setTypeface(myFontBold);

        TextView tv_btn_test = (TextView) findViewById(R.id.tv_btn_test);
        tv_btn_test.setTypeface(myFontBold);

        TextView txt_Ekran_Nauka_tytul = (TextView) findViewById(R.id.txt_Ekran_Nauka_tytul);
        txt_Ekran_Nauka_tytul.setTypeface(myFontBold);

        TextView txt_NazwaPanstwa = (TextView) findViewById(R.id.txt_NazwaPanstwa);
        txt_NazwaPanstwa.setTypeface(myFontBold);

        TextView txt_btn_wstecz_nauka = (TextView) findViewById(R.id.txt_btn_wstecz_nauka);
        txt_btn_wstecz_nauka.setTypeface(myFontBold);

        TextView txt_btn_alfabetycznie_losowo = (TextView) findViewById(R.id.txt_btn_alfabetycznie_losowo);
        txt_btn_alfabetycznie_losowo.setTypeface(myFontBold);

        TextView txt_EkranTestTytul = (TextView) findViewById(R.id.txt_EkranTestTytul);
        txt_EkranTestTytul.setTypeface(myFontBold);

        TextView txt_btn_przerwij_test = (TextView) findViewById(R.id.txt_btn_przerwij_test);
        txt_btn_przerwij_test.setTypeface(myFontBold);

        TextView text_nr_pytania = (TextView) findViewById(R.id.text_nr_pytania);
        text_nr_pytania.setTypeface(myFontBold);

        TextView text_nr_pytania1 = (TextView) findViewById(R.id.text_nr_pytania1);
        text_nr_pytania1.setTypeface(myFontBold);

        TextView txt_EkranPowtorkiTytul = (TextView) findViewById(R.id.txt_EkranPowtorkiTytul);
        txt_EkranPowtorkiTytul.setTypeface(myFontBold);

        TextView txt_btn_powrot_do_menu_powtorki = (TextView) findViewById(R.id.txt_btn_powrot_do_menu_powtorki);
        txt_btn_powrot_do_menu_powtorki.setTypeface(myFontBold);

        TextView txt_btn_nie_wiem = (TextView) findViewById(R.id.txt_btn_nie_wiem);
        txt_btn_nie_wiem.setTypeface(myFontBold);

        TextView txt_btn_wiem = (TextView) findViewById(R.id.txt_btn_wiem);
        txt_btn_wiem.setTypeface(myFontBold);

        TextView txt_test_odp_A = (TextView) findViewById(R.id.txt_test_odp_A);
        txt_test_odp_A.setTypeface(myFontBold);

        TextView txt_test_odp_B = (TextView) findViewById(R.id.txt_test_odp_B);
        txt_test_odp_B.setTypeface(myFontBold);

        TextView txt_test_odp_C = (TextView) findViewById(R.id.txt_test_odp_C);
        txt_test_odp_C.setTypeface(myFontBold);

        TextView txt_test_odp_D = (TextView) findViewById(R.id.txt_test_odp_D);
        txt_test_odp_D.setTypeface(myFontBold);

        TextView txt_dobrze = (TextView) findViewById(R.id.txt_dobrze);
        txt_dobrze.setTypeface(myFontBold);

        TextView txt_zle = (TextView) findViewById(R.id.txt_zle);
        txt_zle.setTypeface(myFontBold);

        TextView txt_wyniki2_procenty = (TextView) findViewById(R.id.txt_wyniki2_procenty);
        txt_wyniki2_procenty.setTypeface(myFontBold);

        TextView tv_btn_nauka_wyniki = (TextView) findViewById(R.id.tv_btn_nauka_wyniki);
        tv_btn_nauka_wyniki.setTypeface(myFontBold);

        TextView tv_btn_koniec_wyniki = (TextView) findViewById(R.id.tv_btn_koniec_wyniki);
        tv_btn_koniec_wyniki.setTypeface(myFontBold);

        TextView txt_wyniki3 = (TextView) findViewById(R.id.txt_wyniki3);
        txt_wyniki3.setTypeface(myFontBold);

        TextView txt_wyniki1 = (TextView) findViewById(R.id.txt_wyniki1);
        txt_wyniki1.setTypeface(myFontBold);

        TextView txt_wyniki_rada = (TextView) findViewById(R.id.txt_wyniki_rada);
        txt_wyniki_rada.setTypeface(myFontBold);

        TextView tv_btn_test_stolice = (TextView) findViewById(R.id.tv_btn_test_stolice);
        tv_btn_test_stolice.setTypeface(myFontBold);

        TextView tv_btn_testy = (TextView) findViewById(R.id.tv_btn_testy);
        tv_btn_testy.setTypeface(myFontBold);

        TextView tv_btn_testy_wyniki = (TextView) findViewById(R.id.tv_btn_testy_wyniki);
        tv_btn_testy_wyniki.setTypeface(myFontBold);

        TextView txt_nazwa_panstwa_test = (TextView) findViewById(R.id.txt_nazwa_panstwa_test);
        txt_nazwa_panstwa_test.setTypeface(myFontBold);

        TextView tv_btn_twoje_wyniki = (TextView) findViewById(R.id.tv_btn_twoje_wyniki);
        tv_btn_twoje_wyniki.setTypeface(myFontBold);

        TextView tv_btn_twoje_wyniki_wyniki = (TextView) findViewById(R.id.tv_btn_twoje_wyniki_wyniki);
        tv_btn_twoje_wyniki_wyniki.setTypeface(myFontBold);

        TextView txt_btn_koniec_1 = (TextView) findViewById(R.id.txt_btn_koniec_1);
        txt_btn_koniec_1.setTypeface(myFontBold);

        TextView tv_btn_ustawienia = (TextView) findViewById(R.id.tv_btn_ustawienia);
        tv_btn_ustawienia.setTypeface(myFontBold);

        TextView tv_btn_baza_powtorek = (TextView) findViewById(R.id.tv_btn_baza_powtorek);
        tv_btn_baza_powtorek.setTypeface(myFontBold);

        TextView tv_btn_baza_powtorek_wyniki = (TextView) findViewById(R.id.tv_btn_baza_powtorek_wyniki);
        tv_btn_baza_powtorek_wyniki.setTypeface(myFontBold);

        TextView txt_twoje_wyniki = (TextView) findViewById(R.id.txt_twoje_wyniki);
        txt_twoje_wyniki.setTypeface(myFontBold);

        TextView txt_btn_wstecz_twoje_wyniki = (TextView) findViewById(R.id.txt_btn_wstecz_twoje_wyniki);
        txt_btn_wstecz_twoje_wyniki.setTypeface(myFontBold);

        TextView txt_btn_wstecz_wybor_testu = (TextView) findViewById(R.id.txt_btn_wstecz_wybor_testu);
        txt_btn_wstecz_wybor_testu.setTypeface(myFontBold);

        TextView tv_twoje_wyniki_naglowek1 = (TextView) findViewById(R.id.tv_twoje_wyniki_naglowek1);
        tv_twoje_wyniki_naglowek1.setTypeface(myFontBold);

        TextView tv_twoje_wyniki_naglowek2 = (TextView) findViewById(R.id.tv_twoje_wyniki_naglowek2);
        tv_twoje_wyniki_naglowek2.setTypeface(myFontBold);

        TextView tv_twoje_wyniki_naglowek3 = (TextView) findViewById(R.id.tv_twoje_wyniki_naglowek3);
        tv_twoje_wyniki_naglowek3.setTypeface(myFontBold);

        TextView tv_btn_pobierz_teraz = (TextView) findViewById(R.id.tv_btn_pobierz_teraz);
        tv_btn_pobierz_teraz.setTypeface(myFontBold);

        TextView tv_btn_przypomnij_pozniej = (TextView) findViewById(R.id.tv_btn_przypomnij_pozniej);
        tv_btn_przypomnij_pozniej.setTypeface(myFontBold);

        TextView txt_dostepna_jest_nowa_wersja_aplikacji = (TextView) findViewById(R.id.txt_dostepna_jest_nowa_wersja_aplikacji);
        txt_dostepna_jest_nowa_wersja_aplikacji.setTypeface(myFontBold);

        TextView txt_EkranWyboruUstawienia = (TextView) findViewById(R.id.txt_EkranWyboruUstawienia);
        txt_EkranWyboruUstawienia.setTypeface(myFontBold);

        TextView txt_btn_wstecz_ustawienia = (TextView) findViewById(R.id.txt_btn_wstecz_ustawienia);
        txt_btn_wstecz_ustawienia.setTypeface(myFontBold);

        TextView tv_pojedynek_gratulacje = (TextView) findViewById(R.id.tv_pojedynek_gratulacje);
        tv_pojedynek_gratulacje.setTypeface(myFontBold);

        TextView tv_pojedynek_wygrales = (TextView) findViewById(R.id.tv_pojedynek_wygrales);
        tv_pojedynek_wygrales.setTypeface(myFontBold);

        TextView tv_pojedynek_wynik_pojedynku_wygrany = (TextView) findViewById(R.id.tv_pojedynek_wynik_pojedynku_wygrany);
        tv_pojedynek_wynik_pojedynku_wygrany.setTypeface(myFontBold);

        TextView tv_pojedynek_niestety = (TextView) findViewById(R.id.tv_pojedynek_niestety);
        tv_pojedynek_niestety.setTypeface(myFontBold);

        TextView tv_pojedynek_przegrales = (TextView) findViewById(R.id.tv_pojedynek_przegrales);
        tv_pojedynek_przegrales.setTypeface(myFontBold);

        TextView tv_pojedynek_wynik_pojedynku_przegrany = (TextView) findViewById(R.id.tv_pojedynek_wynik_pojedynku_przegrany);
        tv_pojedynek_wynik_pojedynku_przegrany.setTypeface(myFontBold);

        TextView txt_powtorki = (TextView) findViewById(R.id.txt_powtorki);
        txt_powtorki.setTypeface(myFontBold);

        TextView tv_btn_nauka_lista = (TextView) findViewById(R.id.tv_btn_nauka_lista);
        tv_btn_nauka_lista.setTypeface(myFontBold);

        TextView tv_btn_nauka_podglad_mapy = (TextView) findViewById(R.id.tv_btn_nauka_podglad_mapy);
        tv_btn_nauka_podglad_mapy.setTypeface(myFontBold);

        TextView txt_btn_wstecz_wybor_trybu_nauki = (TextView) findViewById(R.id.txt_btn_wstecz_wybor_trybu_nauki);
        txt_btn_wstecz_wybor_trybu_nauki.setTypeface(myFontBold);

        TextView txt_WybierzTrybNauki = (TextView) findViewById(R.id.txt_WybierzTrybNauki);
        txt_WybierzTrybNauki.setTypeface(myFontBold);

    }

    private void pokaz_i_ukryj_layouty() {
        RelativeLayout activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //ładujemy layouty używane w projekcie i od razu czynimy je niewidocznymi /////////////////////////////////////
        view_layout_wybor_nauka_test_koniec = inflater.inflate(R.layout.layout_wybor_nauka_test_koniec, null);
        activity_main.addView(view_layout_wybor_nauka_test_koniec);
        view_layout_wybor_nauka_test_koniec.setVisibility(View.INVISIBLE);

        view_layout_nauka_wybor_obszaru = inflater.inflate(R.layout.layout_nauka_wybor_obszaru, null);
        activity_main.addView(view_layout_nauka_wybor_obszaru);
        view_layout_nauka_wybor_obszaru.setVisibility(View.INVISIBLE);

        view_layout_nauka = inflater.inflate(R.layout.layout_nauka, null);
        activity_main.addView(view_layout_nauka);
        view_layout_nauka.setVisibility(View.INVISIBLE);

        view_layout_test = inflater.inflate(R.layout.layout_test, null);
        activity_main.addView(view_layout_test);
        view_layout_test.setVisibility(View.INVISIBLE);

        view_layout_wyniki_testu = inflater.inflate(R.layout.layout_wyniki_testu, null);
        activity_main.addView(view_layout_wyniki_testu);
        view_layout_wyniki_testu.setVisibility(View.INVISIBLE);

        view_layout_twoje_wyniki = inflater.inflate(R.layout.layout_twoje_wyniki, null);
        activity_main.addView(view_layout_twoje_wyniki);
        view_layout_twoje_wyniki.setVisibility(View.INVISIBLE);

        view_layout_powtorki = inflater.inflate(R.layout.layout_powtorki, null);
        activity_main.addView(view_layout_powtorki);
        view_layout_powtorki.setVisibility(View.INVISIBLE);

        view_layout_pojedynek = inflater.inflate(R.layout.layout_pojedynek, null);
        activity_main.addView(view_layout_pojedynek);
        view_layout_pojedynek.setVisibility(View.INVISIBLE);

        view_layout_powiadomienie_o_nowszej_wersji = inflater.inflate(R.layout.layout_powiadomienie_o_nowszej_wersji, null);
        activity_main.addView(view_layout_powiadomienie_o_nowszej_wersji);
        view_layout_powiadomienie_o_nowszej_wersji.setVisibility(View.INVISIBLE);

        view_layout_ustawienia = inflater.inflate(R.layout.layout_ustawienia, null);
        activity_main.addView(view_layout_ustawienia);
        view_layout_ustawienia.setVisibility(View.INVISIBLE);

        view_layout_wybor_testu = inflater.inflate(R.layout.layout_wybor_testu, null);
        activity_main.addView(view_layout_wybor_testu);
        view_layout_wybor_testu.setVisibility(View.INVISIBLE);

        view_layout_intro = inflater.inflate(R.layout.activity_intro, null);
        activity_main.addView(view_layout_intro);
        view_layout_intro.setVisibility(View.INVISIBLE);

        view_layout_opis_panstwa = inflater.inflate(R.layout.layout_opis_panstwa, null);
        activity_main.addView(view_layout_opis_panstwa);
        view_layout_opis_panstwa.setVisibility(View.INVISIBLE);

        view_layout_wynik_pojedynku = inflater.inflate(R.layout.layout_wynik_pojedynku, null);
        activity_main.addView(view_layout_wynik_pojedynku);
        view_layout_wynik_pojedynku.setVisibility(View.INVISIBLE);

        view_layout_wybor_nauka_mapa_lista = inflater.inflate(R.layout.layout_wybor_nauka_mapa_lista, null);
        activity_main.addView(view_layout_wybor_nauka_mapa_lista);
        view_layout_wybor_nauka_mapa_lista.setVisibility(View.INVISIBLE);

        view_layout_podglad_mapy = inflater.inflate(R.layout.layout_podglad_mapy, null);
        activity_main.addView(view_layout_podglad_mapy);
        view_layout_podglad_mapy.setVisibility(View.INVISIBLE);

        //na początku przez kilka sekund widoczny jest splash screen
        view_layout_splash_screen = inflater.inflate(R.layout.layout_splash_screen, null);
        view_layout_ustawienia.setVisibility(View.INVISIBLE);
        activity_main.addView(view_layout_splash_screen);
        ekran = 1;

        myRunable = new Runnable() {
            @Override
            public void run() {

//                sprawdzCzyPlikIstnieje();
                zdecydujCzyPokazacInstro();


            }
        };

        handler.postDelayed(myRunable, CZAS_TRWANIA_SPLASH_SCREENU);
    }


    private void pokaz_layout_wybor_nauka_test_koniec() {
        ukryjLayouty(view_layout_wybor_nauka_test_koniec);
        ekran = 2;
        czy_wybor_testu = false;
        sprawdzCzyWyswietlicTwojeWyniki();
        sprawdzCzyWyswietlicBazePowtorek();
    }

    public void pokaz_layout_wybor_testu(View view) {
        ukryjLayouty(view_layout_wybor_testu);
        czy_wybor_testu = true;
        ekran = 10;
        sciezka = "B";
    }

    public void f_btn_wstecz_wybor_testu(View view) {
        ukryjLayouty(view_layout_wybor_nauka_test_koniec);
        ekran = 2;
        sprawdzCzyWyswietlicTwojeWyniki();
        sprawdzCzyWyswietlicBazePowtorek();
    }


    public void f_btn_wstecz_ustawienia(View view) {
        ukryjLayouty(view_layout_wybor_nauka_test_koniec);
        ekran = 2;
        sprawdzCzyWyswietlicTwojeWyniki();
        sprawdzCzyWyswietlicBazePowtorek();
    }


    public void f_btn_wstecz_twoje_wyniki(View view) {
        ukryjLayouty(view_layout_wybor_nauka_test_koniec);
        ekran = 2;
        sprawdzCzyWyswietlicTwojeWyniki();
        sprawdzCzyWyswietlicBazePowtorek();
    }

    public void f_btn_nauka(View view) {

        ukryjLayouty(view_layout_wybor_nauka_mapa_lista);
        ekran = 3;
        czy_wybor_testu = false;
        sciezka = "A";

    }

    public void f_btn_test(View view) {

        switch (view.getId()) {

            case R.id.l_btn_test:
                rodzaj_testu = "test-flagi";
                break;

            case R.id.l_btn_test_stolice:
                rodzaj_testu = "test-stolice";
                break;


        }

        generuj_widok_wyboru_nauka = 2;
        ukryjLayouty(view_layout_nauka_wybor_obszaru);
        TextView txt_EkranWyboruNauka = (TextView) findViewById(R.id.txt_EkranWyboruNauka);
        txt_EkranWyboruNauka.setText(getResources().getString(R.string.text_test_wybierz_obszar));

        ekran = 3;
    }

    public void f_btn_koniec(View view) {
        iloscUruchomien++;
        spInt = getSharedPreferences(preferenceInt, MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = spInt.edit();
        editor1.putInt(saveItInt, iloscUruchomien);
        editor1.commit();
//        Toast.makeText(MainActivity.this, " " + iloscUruchomien, Toast.LENGTH_LONG).show();

        if (iloscUruchomien == 2 || (f_okno_ocen_aplikacje == false && iloscUruchomien % 5 == 0)) {
            AlertDialog alert = okno.create();
            alert.show();
        } else {
            super.finish();
        }

    }

    public void generujListe(View view) {

        b_czy_koniec_pojedynku = false;

        txt_Ekran_Nauka_tytul = (TextView) findViewById(R.id.txt_Ekran_Nauka_tytul);
        Boolean calyswiat;


        kraj_lista = getResources().getString(R.string.text_kraj);
        stolica_lista = getResources().getString(R.string.text_stolica);
        switch (view.getId()) {
            case R.id.btn_caly_swiat:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[0];
                if (jezyk.equals("pl")) kontynent = "Cały świat";
                if (jezyk.equals("en")) kontynent = "World";
                if (jezyk.equals("de")) kontynent = "Welt";

                calyswiat = true;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_swiata));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_europa:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[1];
                if (jezyk.equals("pl")) kontynent = "Europa";
                if (jezyk.equals("en")) kontynent = "Europe";
                if (jezyk.equals("de")) kontynent = "Europa";
                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_europy));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_a_polnocna:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[2];
                if (jezyk.equals("pl")) kontynent = "Ameryka Północna";
                if (jezyk.equals("en")) kontynent = "North America";
                if (jezyk.equals("de")) kontynent = "Nordamerika";
                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_a_polnocnej));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_a_poludniowa:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[3];
                if (jezyk.equals("pl")) kontynent = "Ameryka Południowa";
                if (jezyk.equals("en")) kontynent = "South America";
                if (jezyk.equals("de")) kontynent = "Südamerika";


                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_a_poludniowej));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_afryka:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[4];
                if (jezyk.equals("pl")) kontynent = "Afryka";
                if (jezyk.equals("en")) kontynent = "Africa";
                if (jezyk.equals("de")) kontynent = "Afrika";
                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_afryki));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_azja:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[5];
                if (jezyk.equals("pl")) kontynent = "Azja";
                if (jezyk.equals("en")) kontynent = "Asia";
                if (jezyk.equals("de")) kontynent = "Asien";
                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_azji));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;

            case R.id.btn_australia:
                kontynent_do_filtrowania = array_uniwersalne_nazwy_kontynentow[6];
                if (jezyk.equals("pl")) kontynent = "Australia";
                if (jezyk.equals("en")) kontynent = "Australia";
                if (jezyk.equals("de")) kontynent = "Australien";

                calyswiat = false;
                if (generuj_widok_wyboru_nauka == 1) {
                    generuj_widok_nauka(kraj_lista, stolica_lista, kontynent, calyswiat);
                    txt_Ekran_Nauka_tytul.setText(getResources().getString(R.string.text_flagi_i_stolice_swiata_nauka) + getResources().getString(R.string.text_australii));
                } else if (generuj_widok_wyboru_nauka == 2) {
                    generuj_widok_test(kontynent, calyswiat);
                } else if (generuj_widok_wyboru_nauka == 3) {
                    generuj_widok_pojedynek(kontynent, calyswiat);
                }

                break;
        }

    }


    private void generuj_widok_test(String kontynent, Boolean calyswiat) {
        globalzle = 0;
        globaldobrze = 0;
        view_layout_nauka_wybor_obszaru.setVisibility(View.INVISIBLE);
        view_layout_test.setVisibility(View.VISIBLE);
        ekran = 6;
        global_nr_aktualnego_pytania_w_tescie = 1;

        //uzupełnij pola tesktowe

        TextView text_nr_pytania = (TextView) findViewById(R.id.text_nr_pytania);
        text_nr_pytania.setText("" + global_nr_aktualnego_pytania_w_tescie);

        TextView text_nr_pytania1 = (TextView) findViewById(R.id.text_nr_pytania1);
        text_nr_pytania1.setText("/" + global_ilosc_pytan_w_tescie);

        TextView txt_dobrze = (TextView) findViewById(R.id.txt_dobrze);
        txt_dobrze.setText(getResources().getText(R.string.text_dobrze) + " " + globaldobrze);

        TextView txt_zle = (TextView) findViewById(R.id.txt_zle);
        txt_zle.setText(getResources().getText(R.string.text_zle) + " " + globalzle);

        generujTest(kontynent, calyswiat);

    }

    private void generujTest(String kontynent, Boolean calyswiat) {

        temp = 0;

        // wyłuskanie państw z wybranego obszaru
        if (calyswiat == false) {
            for (int z = 0; z < dlugoscTablicy; z++) {
                if (array_nazwy_kontynentow[z].equals(kontynent)) {
                    array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                    array_panstwa_wybrane_losowo[temp] = array_nazwy_panstw[z];

                    array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                    array_stolice_wybrane_losowo[temp] = array_nazwy_stolic[z];

                    array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                    array_flagi_wybranych_panstw_losowo[temp] = flagi_img_array[z];
                    temp++;

                }
            }
        } else {
            for (int z = 0; z < dlugoscTablicy; z++) {
                array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                array_panstwa_wybrane_losowo[temp] = array_nazwy_panstw[z];

                array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                array_stolice_wybrane_losowo[temp] = array_nazwy_stolic[z];

                array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                array_flagi_wybranych_panstw_losowo[temp] = flagi_img_array[z];
                temp++;
            }
        }

        //80 cio elementowa tablica z elementami potórzonymi
        for (int z = 0; z < 80; z++) {
            int liczba_losowa_80 = rand.nextInt(temp);
            array_panstwa_test[z] = array_panstwa_wybrane_alfabetycznie[liczba_losowa_80];
            array_stolice_test[z] = array_stolice_wybrane_alfabetycznie[liczba_losowa_80];
            array_flagi_wybranych_panstw_test[z] = array_flagi_wybranych_panstw_alfabetycznie[liczba_losowa_80];
        }

        wygenerujWariantyOdpowiedzi_A_B_C_D(array_panstwa_test);

    }

    private void wygenerujWariantyOdpowiedzi_A_B_C_D(String[] array_panstwa_test) {

        array_pomocnicza_do_testu[0] = array_panstwa_test[global_nr_aktualnego_pytania_w_tescie - 1];

        int licznikPetliWhile = 1;
        Boolean flaga11 = false;
        Random randomize = new Random();
        int liczbaLosowa1;
        int templength = array_pomocnicza_do_testu.length;

        while (licznikPetliWhile < templength) {

            flaga11 = false;
            liczbaLosowa1 = randomize.nextInt(80);

            for (int z = 0; z < templength; z++) {

                if (array_pomocnicza_do_testu[z] == array_panstwa_test[liczbaLosowa1]) {
                    flaga11 = true;
                }
            }

            if (flaga11 == false) {

                array_pomocnicza_do_testu[licznikPetliWhile] = array_panstwa_test[liczbaLosowa1];
                licznikPetliWhile++;
            }

        }

        String[] array_pomocnicza_do_testu_losowa = array_pomocnicza_do_testu;

        int temp = array_pomocnicza_do_testu.length;

        while (0 < temp) {
            int liczba_losowa = rand.nextInt(temp);
            String temp2 = array_pomocnicza_do_testu_losowa[temp - 1];
            String temp1 = array_pomocnicza_do_testu_losowa[liczba_losowa];
            array_pomocnicza_do_testu_losowa[temp - 1] = temp1;
            array_pomocnicza_do_testu_losowa[liczba_losowa] = temp2;
            temp--;
        }

        iv_flaga_test = (ImageView) findViewById(R.id.iv_flaga_test);
        TextView txt_nazwa_stolicy_test = (TextView) findViewById(R.id.txt_nazwa_panstwa_test);

        if (rodzaj_testu == "test-flagi") {
            txt_nazwa_stolicy_test.setVisibility(View.GONE);
            iv_flaga_test.setVisibility(View.VISIBLE);
        } else {

            txt_nazwa_stolicy_test.setVisibility(View.VISIBLE);
            iv_flaga_test.setVisibility(View.GONE);
        }

        iv_flaga_test.setImageResource(array_flagi_wybranych_panstw_test[global_nr_aktualnego_pytania_w_tescie - 1]);
        iv_flaga_test.setTag(array_flagi_wybranych_panstw_test[global_nr_aktualnego_pytania_w_tescie - 1]);
        txt_nazwa_stolicy_test.setText("" + array_stolice_test[global_nr_aktualnego_pytania_w_tescie - 1]);

        TextView txt_test_odp_A = (TextView) findViewById(R.id.txt_test_odp_A);
        txt_test_odp_A.setText(array_pomocnicza_do_testu_losowa[0]);

        TextView txt_test_odp_B = (TextView) findViewById(R.id.txt_test_odp_B);
        txt_test_odp_B.setText(array_pomocnicza_do_testu_losowa[1]);

        TextView txt_test_odp_C = (TextView) findViewById(R.id.txt_test_odp_C);
        txt_test_odp_C.setText(array_pomocnicza_do_testu_losowa[2]);

        TextView txt_test_odp_D = (TextView) findViewById(R.id.txt_test_odp_D);
        txt_test_odp_D.setText(array_pomocnicza_do_testu_losowa[3]);

    }

    private void generuj_widok_nauka(String kraj_lista, String stolica_lista, String kontynent, Boolean calyswiat) {

        adapter = new ZegarkiAdapter(getApplicationContext(), R.layout.layout_row);
        listView.setAdapter(adapter);

        temp = 0;
        view_layout_nauka_wybor_obszaru.setVisibility(View.INVISIBLE);
        view_layout_nauka.setVisibility(View.VISIBLE);

        // wyłuskanie państw z wybranego obszaru
        if (calyswiat == false) {
            for (int z = 0; z < dlugoscTablicy; z++) {
                if (array_nazwy_kontynentow[z].equals(kontynent)) {
                    array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                    array_panstwa_wybrane_losowo[temp] = array_nazwy_panstw[z];

                    array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                    array_stolice_wybrane_losowo[temp] = array_nazwy_stolic[z];

                    array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                    array_flagi_wybranych_panstw_losowo[temp] = flagi_img_array[z];
                    temp++;
                }
            }
        } else {
            for (int z = 0; z < dlugoscTablicy; z++) {
                array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                array_panstwa_wybrane_losowo[temp] = array_nazwy_panstw[z];

                array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                array_stolice_wybrane_losowo[temp] = array_nazwy_stolic[z];

                array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                array_flagi_wybranych_panstw_losowo[temp] = flagi_img_array[z];
                temp++;
            }
        }


        for (int i = 0; i < temp; i++) {
            ZegarkiDataProvider dataProvider = new ZegarkiDataProvider(kraj_lista, stolica_lista, array_flagi_wybranych_panstw_alfabetycznie[i], array_panstwa_wybrane_alfabetycznie[i], array_stolice_wybrane_alfabetycznie[i]);
            adapter.add(dataProvider);
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "Pozycja listy " + array_panstwa_wybrane_alfabetycznie[position], Toast.LENGTH_SHORT).show();
                String nazwaPanstwa = array_panstwa_wybrane_losowo[position];
//                pokazSzczegolowyOpisPanstwa(nazwaPanstwa);
            }
        });

        ekran = 4;

    }

    public void f_btn_wstecz_nauka(View view) {
        ukryjLayouty(view_layout_nauka_wybor_obszaru);
        flaga_losowo = false;
        TextView txt_btn_alfabetycznie_losowo = (TextView) findViewById(R.id.txt_btn_alfabetycznie_losowo);
        txt_btn_alfabetycznie_losowo.setText(R.string.text_losowo);
        ekran = 13;


    }

    public void f_btn_alfabetycznie_losowo(View view) {

        adapter = new ZegarkiAdapter(getApplicationContext(), R.layout.layout_row);
        listView.setAdapter(adapter);

        if (flaga_losowo == true) {
            TextView txt_btn_alfabetycznie_losowo = (TextView) findViewById(R.id.txt_btn_alfabetycznie_losowo);
            txt_btn_alfabetycznie_losowo.setText(R.string.text_losowo);
            flaga_losowo = false;

            for (int i = 0; i < temp; i++) {
                ZegarkiDataProvider dataProvider = new ZegarkiDataProvider(kraj_lista, stolica_lista, array_flagi_wybranych_panstw_alfabetycznie[i], array_panstwa_wybrane_alfabetycznie[i], array_stolice_wybrane_alfabetycznie[i]);
                adapter.add(dataProvider);
                adapter.notifyDataSetChanged();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(MainActivity.this, "Pozycja listy " + array_panstwa_wybrane_alfabetycznie[position], Toast.LENGTH_SHORT).show();
                    String nazwaPanstwa = array_panstwa_wybrane_losowo[position];
//                   pokazSzczegolowyOpisPanstwa (nazwaPanstwa);
                }
            });


        } else {
            TextView txt_btn_alfabetycznie_losowo = (TextView) findViewById(R.id.txt_btn_alfabetycznie_losowo);
            txt_btn_alfabetycznie_losowo.setText(R.string.text_alfabetycznie);
            flaga_losowo = true;
            int tempo = temp;

            while (0 < tempo) {
                int liczba_losowa = rand.nextInt(tempo);

                int temp2 = array_flagi_wybranych_panstw_losowo[tempo - 1];
                int temp1 = array_flagi_wybranych_panstw_losowo[liczba_losowa];
                array_flagi_wybranych_panstw_losowo[tempo - 1] = temp1;
                array_flagi_wybranych_panstw_losowo[liczba_losowa] = temp2;

                String temp3 = array_panstwa_wybrane_losowo[tempo - 1];
                String temp4 = array_panstwa_wybrane_losowo[liczba_losowa];
                array_panstwa_wybrane_losowo[tempo - 1] = temp4;
                array_panstwa_wybrane_losowo[liczba_losowa] = temp3;

                String temp5 = array_stolice_wybrane_losowo[tempo - 1];
                String temp6 = array_stolice_wybrane_losowo[liczba_losowa];
                array_stolice_wybrane_losowo[tempo - 1] = temp6;
                array_stolice_wybrane_losowo[liczba_losowa] = temp5;

                ZegarkiDataProvider dataProvider = new ZegarkiDataProvider(kraj_lista, stolica_lista, array_flagi_wybranych_panstw_losowo[tempo - 1], array_panstwa_wybrane_losowo[tempo - 1], array_stolice_wybrane_losowo[tempo - 1]);
                adapter.add(dataProvider);
                adapter.notifyDataSetChanged();
                tempo--;

            }

//dodano
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(MainActivity.this, "TEMP = " + temp + "POZ = " + array_panstwa_wybrane_losowo[position], Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Pozycja listy los " + array_panstwa_wybrane_losowo[temp-position-1], Toast.LENGTH_SHORT).show();
                    String nazwaPanstwa = array_panstwa_wybrane_losowo[temp - position - 1];
//                  pokazSzczegolowyOpisPanstwa(nazwaPanstwa);

                }
            });

        }
    }

    public void onBackPressed() {

        switch (ekran) {
            case 1:
                if (back_pressed + 2000 > System.currentTimeMillis())
                    super.onBackPressed();
                else
                    Toast.makeText(getBaseContext(), "" + getResources().getString(R.string.text_exit_komunikat), Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
                //finish();
                break;

            case 2:
                if (back_pressed + 2000 > System.currentTimeMillis())
                    super.onBackPressed();
                else
                    Toast.makeText(getBaseContext(), "" + getResources().getString(R.string.text_exit_komunikat), Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
                ukryjLayouty(view_layout_splash_screen);
                ekran = 1;
                myRunable = new Runnable() {
                    @Override
                    public void run() {
                        pokaz_layout_wybor_nauka_test_koniec();

                    }
                };

                handler.postDelayed(myRunable, CZAS_TRWANIA_SPLASH_SCREENU);

                break;

            case 3:

                if (czy_wybor_testu == true) {
                    ukryjLayouty(view_layout_wybor_testu);
                    ekran = 10;
                } else if (sciezka.equals("A1")) {
                    ukryjLayouty(view_layout_wybor_nauka_mapa_lista);
                    ekran = 20;
                    czy_wybor_testu = false;

                } else if (sciezka.equals("A")) {
                    ukryjLayouty(view_layout_wybor_nauka_test_koniec);
                    ekran = 2;
                    sprawdzCzyWyswietlicTwojeWyniki();
                    sprawdzCzyWyswietlicBazePowtorek();
                    czy_wybor_testu = false;

                } else {
                    ukryjLayouty(view_layout_wybor_nauka_test_koniec);
                    ekran = 2;
                    sprawdzCzyWyswietlicTwojeWyniki();
                    sprawdzCzyWyswietlicBazePowtorek();
                    czy_wybor_testu = false;
                }


                break;

            case 4:
                ukryjLayouty(view_layout_nauka_wybor_obszaru);
                ekran = 3;
                break;

            case 5:
                //warunek spełniony gdy pojawia się ekran z przyciskiem do pobrania najnowszej wersji aplikacji
                pokaz_layout_wybor_nauka_test_koniec();


                break;

            case 6:
                ukryjLayouty(view_layout_nauka_wybor_obszaru);
                ekran = 3;
                global_nr_aktualnego_pytania_w_tescie = 1;
                globalzle = 0;
                globaldobrze = 0;
                break;

            case 7:
                //warunek spełniony, kiedy uruchomiony jest przewodnik po aplikacji
                ukryjLayouty(view_layout_ustawienia);
                ekran = 3;
                break;

            case 8:
                //warunek spełniony TYLKO I WYLOCZNIE WTEDY gdy intro pokazuje się przy PIERWSZYM uruchomieniu aplikacji
                pokaz_layout_wybor_nauka_test_koniec();
                break;

            case 9:
                //warunek spełniony kiedy klikamy wybrane państwo i zapoznajemy się z szczegółowymi inormacjami na jego temat
                ukryjSzczegolowyOpisPanstwa();
                break;


            case 10:
                //warunek spełniony kiedy jesteśmy na ekranie wyboru testu
                pokaz_layout_wybor_nauka_test_koniec();
                break;

            case 11:
                //warunek spełniony kiedy kończymy test i widzimy jego wyniki
                pokaz_layout_wybor_nauka_test_koniec();
                break;

            case 13:
                f_btn_wstecz_nauka_wybor_obszaru();
                break;

            case 20:
                pokaz_layout_wybor_nauka_test_koniec();
                break;

            case 21:
                //jeśli jestem w pamie konturowej i naciskam wstecz
                ukryjLayouty(view_layout_wybor_nauka_mapa_lista);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ekran = 3;
                czy_wybor_testu = false;
                sciezka = "A";
                break;

        }

    }

    public void f_btn_przerwij_test(View view) {

        ukryjLayouty(view_layout_nauka_wybor_obszaru);
        ekran = 3;
        global_nr_aktualnego_pytania_w_tescie = 1;
        globalzle = 0;
        globaldobrze = 0;
    }


    public void f_odp(View view) {

        if (globaldobrze + globalzle < global_ilosc_pytan_w_tescie) {
//Toast.makeText(MainActivity.this, "")
            TextView tv = (TextView) ((RelativeLayout) view).getChildAt(0);
            String sTemp = tv.getText().toString();
            if (sTemp.equals(array_panstwa_test[global_nr_aktualnego_pytania_w_tescie - 1])) {
                globaldobrze++;
            } else {
                globalzle++;
                zapisz_do_bazy_powtorek(
                        array_flagi_wybranych_panstw_test[global_nr_aktualnego_pytania_w_tescie - 1],
                        array_panstwa_test[global_nr_aktualnego_pytania_w_tescie - 1],
                        array_stolice_test[global_nr_aktualnego_pytania_w_tescie - 1]
                );
            }

            TextView txt_dobrze = (TextView) findViewById(R.id.txt_dobrze);
            txt_dobrze.setText(getResources().getText(R.string.text_dobrze) + " " + globaldobrze);

            TextView txt_zle = (TextView) findViewById(R.id.txt_zle);
            txt_zle.setText(getResources().getText(R.string.text_zle) + " " + globalzle);
        }

        if (global_nr_aktualnego_pytania_w_tescie < global_ilosc_pytan_w_tescie) {
            global_nr_aktualnego_pytania_w_tescie++;

            TextView text_nr_pytania = (TextView) findViewById(R.id.text_nr_pytania);
            text_nr_pytania.setText("" + global_nr_aktualnego_pytania_w_tescie);

            TextView text_nr_pytania1 = (TextView) findViewById(R.id.text_nr_pytania1);
            text_nr_pytania1.setText("/" + global_ilosc_pytan_w_tescie);

            wygenerujWariantyOdpowiedzi_A_B_C_D(array_panstwa_test);

        } else {

            TextView text_nr_pytania = (TextView) findViewById(R.id.text_nr_pytania);
            text_nr_pytania.setText("" + global_nr_aktualnego_pytania_w_tescie);

            TextView text_nr_pytania1 = (TextView) findViewById(R.id.text_nr_pytania1);
            text_nr_pytania1.setText("/" + global_ilosc_pytan_w_tescie);

            //KONIEC TESTÓW: pokazanie wyników, stosowny komunikat itp
            //i wpis do bazy danych
            gdy_konec_testu();

        }

    }

    private void gdy_konec_testu() {
        sprawdzCzyWyswietlicBazePowtorek();
        ukryjLayouty(view_layout_wyniki_testu);
        ekran = 11;
        procent = Math.floor((double) globaldobrze / global_ilosc_pytan_w_tescie * 100);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        TextView txt_wyniki2_procenty = (TextView) findViewById(R.id.txt_wyniki2_procenty);
        txt_wyniki2_procenty.setText(decimalFormat.format(procent) + "%");

        TextView txt_wyniki1 = (TextView) findViewById(R.id.txt_wyniki1);
        TextView txt_wyniki_rada = (TextView) findViewById(R.id.txt_wyniki_rada);

        if (procent < 20) {
            txt_wyniki1.setText(getResources().getString(R.string.text_fatalnie));
            txt_wyniki_rada.setText(getResources().getString(R.string.text_rada_20));
        }

        if (procent >= 20 && procent < 40) {
            txt_wyniki1.setText(getResources().getString(R.string.text_slabo));
            txt_wyniki_rada.setText(getResources().getString(R.string.text_rada_40));
        }

        if (procent >= 40 && procent < 60) {
            txt_wyniki1.setText(getResources().getString(R.string.text_dobrze_));
            txt_wyniki_rada.setText(getResources().getString(R.string.text_rada_60));
        }

        if (procent >= 60 && procent < 80) {
            txt_wyniki1.setText(getResources().getString(R.string.text_swietnie));
            txt_wyniki_rada.setText(getResources().getString(R.string.text_rada_80));
        }

        if (procent >= 80 && procent <= 100) {
            txt_wyniki1.setText(getResources().getString(R.string.text_brawo));
            txt_wyniki_rada.setText(getResources().getString(R.string.text_rada_100));
        }


        //zapis wyniku do bazy danych
        //  zapisz_do_bazy(kontynent, procent); po staremu
        zapisz_do_bazy(kontynent_do_filtrowania, procent);
    }

    private void zapisz_do_bazy(String kontynent, double procent) {

        String s_procent = String.valueOf(procent);

        boolean isInserted = myDb.insertData(

                kontynent,
                s_procent

        );


    }

    private void zapisz_do_bazy_powtorek(int flaga, String panstwo, String stolica) {
        String temp = String.valueOf(flaga);

        if (jezyk.equals("pl")) res = myDbPowtorki_PL.getAllData();
        if (jezyk.equals("en")) res = myDbPowtorki_EN.getAllData();
        if (jezyk.equals("de")) res = myDbPowtorki_DE.getAllData();


        boolean czy_zapisac_do_bazy_sqlite_powtorek = true;
        while (res.moveToNext()) {

            if (res.getString(2).equals(panstwo)) {
                czy_zapisac_do_bazy_sqlite_powtorek = false;
            }

        }

        if (czy_zapisac_do_bazy_sqlite_powtorek == true) {

            if (jezyk.equals("pl")) {
                boolean isInserted = myDbPowtorki_PL.insertData(
                        temp,
                        panstwo,
                        stolica

                );
            }

            if (jezyk.equals("en")) {
                boolean isInserted = myDbPowtorki_EN.insertData(
                        temp,
                        panstwo,
                        stolica

                );
            }

            if (jezyk.equals("de")) {
                boolean isInserted = myDbPowtorki_DE.insertData(
                        temp,
                        panstwo,
                        stolica

                );
            }

        }


    }

    public void sprawdzCzyWyswietlicTwojeWyniki() {


        RelativeLayout l_btn_twoje_wyniki = (RelativeLayout) findViewById(R.id.l_btn_twoje_wyniki);

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            l_btn_twoje_wyniki.setVisibility(View.GONE);
            return;
        } else if (res.getCount() != 0) {
            l_btn_twoje_wyniki.setVisibility(View.VISIBLE);
            //Baza danych NIE jest pusta!
            return;
        }
    }

    public void sprawdzCzyWyswietlicBazePowtorek() {
        RelativeLayout l_btn_baza_powtorek = (RelativeLayout) findViewById(R.id.l_btn_baza_powtorek);
        //RelativeLayout l_btn_baza_powtorek_wyniki = (RelativeLayout) findViewById(R.id.l_btn_baza_powtorek_wyniki);

        if (jezyk.equals("pl")) respowtorki = myDbPowtorki_PL.getAllData();
        if (jezyk.equals("en")) respowtorki = myDbPowtorki_EN.getAllData();
        if (jezyk.equals("de")) respowtorki = myDbPowtorki_DE.getAllData();

        if (respowtorki.getCount() == 0) {
            //pokaz info że jest pusta baza
            l_btn_baza_powtorek.setVisibility(View.GONE);
            //l_btn_baza_powtorek_wyniki.setVisibility(View.GONE);
            return;
        } else if (respowtorki.getCount() != 0) {
            l_btn_baza_powtorek.setVisibility(View.VISIBLE);
            // l_btn_baza_powtorek_wyniki.setVisibility(View.VISIBLE);
            //Baza danych NIE jest pusta!
            return;
        }
    }


    public void pokaz_wszystkie_dane_z_bazy() {


        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);

            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);


            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }


    }

    public void pokaz_wszystkie_dane_z_bazy_rosnaco() {


        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        Cursor res = myDb.sortujRosnąco();

        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);

            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);


            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }


    }

    public void pokaz_wszystkie_dane_z_bazy_malejaco() {


        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        Cursor res = myDb.sortujMalejaco();

        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);
            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);


            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }


    }

    private String wyswietlWynikiWWybranymJezyku(Cursor res) {
        String temple = res.getString(1);
        //wpisy w kolejności [PL] [EN] [DE]
        if (jezyk.equals("pl")) {

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[0])) {
                temple = "Cały świat";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[1])) {
                temple = "Europa";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[2])) {
                temple = "Ameryka Północna";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[3])) {
                temple = "Ameryka Południowa";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[4])) {
                temple = "Afryka";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[5])) {
                temple = "Azja";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[6])) {
                temple = "Australia";
            }


        } else if (jezyk.equals("en")) {
            if (temple.equals(array_uniwersalne_nazwy_kontynentow[0])) {
                temple = "World";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[1])) {
                temple = "Europe";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[2])) {
                temple = "North America";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[3])) {
                temple = "South America";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[4])) {
                temple = "Africa";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[5])) {
                temple = "Asia";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[6])) {
                temple = "Australia";
            }


        } else if (jezyk.equals("de")) {

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[0])) {
                temple = "Welt";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[1])) {
                temple = "Europa";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[2])) {
                temple = "Nordamerika";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[3])) {
                temple = "Südamerika";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[4])) {
                temple = "Afrika";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[5])) {
                temple = "Asien";
            }

            if (temple.equals(array_uniwersalne_nazwy_kontynentow[6])) {
                temple = "Australien";
            }

        }
        return temple;
    }

    public void f_btn_twoje_wyniki(View view) {

        ukryjLayouty(view_layout_twoje_wyniki);
        ekran = 3;
        jak_sortowac = "nie_sortuj";
        licznikFiltrowania = 7;
        pokaz_wszystkie_dane_z_bazy();
        czy_wybor_testu = false;
        sciezka = "C";
    }

    public void f_btn_ustawienia(View view) {
        ukryjLayouty(view_layout_ustawienia);
        ekran = 3;
        sciezka = "E";

    }

    public void f_btn_pobierz_teraz(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=eu.huras.marcin.flagiistolice"));
        startActivity(intent);

    }

    public void f_btn_przypomnij_pozniej(View view) {
        view_layout_powiadomienie_o_nowszej_wersji.setVisibility(View.INVISIBLE);
        //pokaz_layout_wybor_nauka_test_koniec();
        zdecydujCzyPokazacInstro();
        ekran = 2;

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        layout_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
        }

    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        if (boolean_przewodnik == true) {
            view_layout_intro.setVisibility(View.INVISIBLE);
            view_layout_ustawienia.setVisibility(View.VISIBLE);
            boolean_przewodnik = false;
            ekran = 3;
        } else {
            view_layout_intro.setVisibility(View.INVISIBLE);
            pokaz_layout_wybor_nauka_test_koniec();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }


    public class ViewPageAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        public ViewPageAdapter() {


        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            //kod odpowiedzialny za zmianę czcionki w polach tekstowych w layoutach view pagera
            Typeface myFontBold = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
            Typeface myFonLight = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Light.ttf");

            TextView tv_intro_tytul = (TextView) view.findViewById(R.id.tv_intro_tytul);
            TextView tv_intro_opis = (TextView) view.findViewById(R.id.tv_intro_opis);

            tv_intro_tytul.setTypeface(myFontBold);
            tv_intro_opis.setTypeface(myFonLight);


            container.addView(view);
            return view;
        }


        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {

            return view == obj;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void pokazPrzewodnik() {

        //przewodnik
        boolean_przewodnik = true;

        addBottomDots(0);

        viewPageAdapter = new ViewPageAdapter();
        view_pager.setAdapter(viewPageAdapter);

        btn_next.setText(getString(R.string.text_next));
        btn_skip.setVisibility(View.VISIBLE);

        view_layout_intro.setVisibility(View.VISIBLE);
        ekran = 7;

    }

    private void pokazSzczegolowyOpisPanstwa(String this_nazwaPanstwa) {

        TextView txt_NazwaPanstwa = (TextView) findViewById(R.id.txt_NazwaPanstwa);
        txt_NazwaPanstwa.setText(this_nazwaPanstwa);

        wybranePanstwo = this_nazwaPanstwa.trim();

        //zczytywanie danych z bazy firebase database
        TextView txt_opis_panstwa = (TextView) findViewById(R.id.txt_opis_panstwa);
        txt_opis_panstwa.setText("");
        f_przeczytano_database = false;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.text_trwa_ladowanie_danych));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        licznik_database = 0;


        zaczytajDaneZFirebase();
        odliczanka();

    }

    private void zaczytajDaneZFirebase() {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView txt_opis_panstwa = (TextView) findViewById(R.id.txt_opis_panstwa);

                for (DataSnapshot panstwoSnapshot : dataSnapshot.getChildren()) {
                    Panstwo panstwo = panstwoSnapshot.getValue(Panstwo.class);
                    String nazwa = panstwo.getNazwa();

                    //wyłuskanie wybranego przez użytkownika Państwa

                    if (nazwa.equals(wybranePanstwo)) {
                        String liczbaMieszkancow = panstwo.getLiczbaMieszkancow();
                        String jezyk = panstwo.getJezyk();
                        String opis = panstwo.getOpis();
                        String powierzchnia = panstwo.getPowierzchnia();
                        String religia = panstwo.getReligia();
                        String stolica = panstwo.getStolica();
                        String ustroj = panstwo.getUstroj();
                        String waluta = panstwo.getWaluta();

                        txt_opis_panstwa.append(

                                "Stolica: " + stolica + "\n" +
                                        "Powierzchnia całkowita: " + powierzchnia + "\n" +
                                        "Liczba mieszkańców: " + liczbaMieszkancow + "\n" +
                                        "Jednostka monetarna: " + waluta + "\n" +
                                        "Ustrój: " + ustroj + "\n" +
                                        "Język urzędowy: " + jezyk + "\n" +
                                        "Religia dominująca: " + religia + "\n" +
                                        "Opis: " + opis + "\n"


                        );
                        f_przeczytano_database = true;
                        pokazNaMapie(nazwa);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.text_nie_udalo_sie_zaladowac_danych), Toast.LENGTH_LONG).show();
                f_przeczytano_database = false;

            }
        });


    }

    private void pokazNaMapie(String this_nazwaPanstwa) {


        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(this_nazwaPanstwa, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        android.location.Address address = list.get(0);
        String mLokalizacja = address.getLocality();


        double lat = address.getLatitude();
        double lng = address.getLongitude();

        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 4);
        mGoogleMap.moveCamera(update);
//ustawienia markera
        if (marker != null) {
            removeCircleAndMarker();
        }

        MarkerOptions options = new MarkerOptions()
                .title(this_nazwaPanstwa)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))

                .position(new LatLng(lat, lng));

        marker = mGoogleMap.addMarker(options);

        circleMapGoogleShape = drawCircle(new LatLng(lat, lng));


    }

    private Circle drawCircle(LatLng latLng) {

        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(500000)
                .fillColor(Color.parseColor("#4Dff0000"))
                .strokeColor(Color.BLUE)
                .strokeWidth(3);

        return mGoogleMap.addCircle(options);
    }

    private void removeCircleAndMarker() {
        marker.remove();
        circleMapGoogleShape.remove();
        circleMapGoogleShape = null;

    }

    private void ukryjSzczegolowyOpisPanstwa() {

        view_layout_opis_panstwa.setVisibility(View.INVISIBLE);
        view_layout_nauka.setVisibility(View.VISIBLE);
        ekran = 4;
    }

    public void f_btn_zamknij_opis_panstwa(View view) {


        ukryjSzczegolowyOpisPanstwa();

    }

    private void odliczanka() {


        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


//                if (f_przeczytano_database == true) {
//                    countDownTimer.cancel();
//                    countDownTimer = null;
//                    progressDialog.dismiss();
//
//                    view_layout_opis_panstwa.setVisibility(View.VISIBLE);
//                    view_layout_nauka.setVisibility(View.INVISIBLE);
//                    ekran = 9;
//                }
            }

            @Override
            public void onFinish() {

                if (f_przeczytano_database == true) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    progressDialog.dismiss();
                    ukryjLayouty(view_layout_opis_panstwa);
                    ekran = 9;
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.text_nie_udalo_sie_zaladowac_danych), Toast.LENGTH_LONG).show();
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                //poniższe dwie linie będą wykonywane warunkowo gdy dane nie zostaną załadowane

                if (f_przeczytano_database == false) {
                    ukryjSzczegolowyOpisPanstwa();
                    progressDialog.dismiss();
                }

            }
        };

        countDownTimer.start();
    }

    public boolean googleServicesAvaliable() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvaliable = api.isGooglePlayServicesAvailable(this);
        if (isAvaliable == ConnectionResult.SUCCESS) {

            return true;
        } else if (api.isUserResolvableError(isAvaliable)) {

            Dialog dialog = api.getErrorDialog(this, isAvaliable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Nie można się połączyć z serwisem Play", Toast.LENGTH_LONG).show();

        }

        return false;
    }

    public void sortujDane(View view) {

        switch (jak_sortowac) {

            case "nie_sortuj":
                jak_sortowac = "sortuj_malejaco";

                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy_malejaco();
                } else {
                    //czyli są filtry
                    //wówczas sprawdzam kierunek sortowania i w zależności od tego wyświetlam
                    filtruj_i_sortuj_malejaco();
                }

                break;

            case "sortuj_malejaco":
                jak_sortowac = "sortuj_rosnaco";

                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy_rosnaco();
                } else {
                    filtruj_i_sortuj_rosnaco();
                }
                break;

            case "sortuj_rosnaco":
                jak_sortowac = "nie_sortuj";

                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy();
                } else {
                    filtruj_bez_sortowania();
                }

                break;

        }

    }

    private void filtruj_i_sortuj_malejaco() {

        Cursor res;
        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        res = myDb.filtrujDane_sortujMalejaco(array_uniwersalne_nazwy_kontynentow[licznikFiltrowania]);
        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);

            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);
            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }
    }

    private void filtruj_i_sortuj_rosnaco() {

        Cursor res;
        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        res = myDb.filtrujDane_sortujRosnaco(array_uniwersalne_nazwy_kontynentow[licznikFiltrowania]);
        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);

            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);

            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }
    }

    private void filtruj_bez_sortowania() {

        Cursor res;
        adapter_wyniki = new WynikiAdapter(getApplicationContext(), R.layout.row);
        listViewWyniki.setAdapter(adapter_wyniki);
        res = myDb.filtrujDane(array_uniwersalne_nazwy_kontynentow[licznikFiltrowania]);
        if (res.getCount() == 0) {
            //pokaz info że jest pusta baza
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        //tutaj w pętli while będę wypełnieł listę
        while (res.moveToNext()) {

            double dblPrzelicznik = Double.parseDouble(res.getString(2));
            double procent = Math.floor(dblPrzelicznik);
            String floor_procent_poprawnych = decimalFormat.format(procent) + "%";
            String temple = wyswietlWynikiWWybranymJezyku(res);

            WynikiDataProvider dataProviderWyniki = new WynikiDataProvider(res.getString(0) + ".", temple, floor_procent_poprawnych);


            adapter_wyniki.add(dataProviderWyniki);
            adapter_wyniki.notifyDataSetChanged();

        }
    }


    public void wyczyscBazeDanych(View view) {
        myDb.wyczyscBaze();
        pokaz_wszystkie_dane_z_bazy();
    }


    private void filtrujDane() {

        if (licznikFiltrowania == 6) {
            licznikFiltrowania = 7;

        } else if (licznikFiltrowania == 7) {
            licznikFiltrowania = 0;

        } else {
            licznikFiltrowania++;
        }

        switch (jak_sortowac) {

            case "nie_sortuj":
                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy();
                } else {
                    filtruj_bez_sortowania();
                }

                break;

            case "sortuj_malejaco":
                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy_malejaco();
                } else {
                    filtruj_i_sortuj_malejaco();
                }
                break;

            case "sortuj_rosnaco":
                if (licznikFiltrowania == 7) {
                    pokaz_wszystkie_dane_z_bazy_rosnaco();
                } else {
                    filtruj_i_sortuj_rosnaco();
                }
                break;
        }
    }

    public void f_btn_twoje_powtorki(View view) {

        rl_nic_do_powtorki = (RelativeLayout) findViewById(R.id.rl_nic_do_powtorki);
        rl_nic_do_powtorki.setVisibility(View.GONE);

        RelativeLayout btnNieWiem = (RelativeLayout) findViewById(R.id.btnNieWiem);
        RelativeLayout btnWiem = (RelativeLayout) findViewById(R.id.btnWiem);
        btnNieWiem.setVisibility(View.VISIBLE);
        btnWiem.setVisibility(View.VISIBLE);

        ukryjLayouty(view_layout_powtorki);
        czy_wybor_testu = false;


        if (jezyk.equals("pl")) respowtorki = myDbPowtorki_PL.getAllData();
        if (jezyk.equals("en")) respowtorki = myDbPowtorki_EN.getAllData();
        if (jezyk.equals("de")) respowtorki = myDbPowtorki_DE.getAllData();

        view_pager_powtorki = (ViewPager) findViewById(R.id.viewPagerPowtorki);

        arrayNumber = new String[respowtorki.getCount()];
        arrayNazwyPanstw = new String[respowtorki.getCount()];
        arrayStolicePanstw = new String[respowtorki.getCount()];
        arrayFlagiPanst = new int[respowtorki.getCount()];

        int i = 0;
        while (respowtorki.moveToNext()) {
            int flaga = respowtorki.getInt(1);

            String number = "" + i;
            String nazwaPanstwa = respowtorki.getString(2);
            String stolicaPanstwa = respowtorki.getString(3);
            arrayNumber[i] = number;
            arrayNazwyPanstw[i] = nazwaPanstwa;
            arrayStolicePanstw[i] = stolicaPanstwa;
            arrayFlagiPanst[i] = flaga;
            i++;
        }

        customSwype = new CustoSwype(this, arrayNumber, arrayNazwyPanstw, arrayStolicePanstw, arrayFlagiPanst);
        view_pager_powtorki.setAdapter(customSwype);
        customSwype.notifyDataSetChanged();

        ekran = 10;
        sciezka = "D";

    }

    public void f_btn_twoje_powtorki_2(int numer) {

        rl_nic_do_powtorki = (RelativeLayout) findViewById(R.id.rl_nic_do_powtorki);
        rl_nic_do_powtorki.setVisibility(View.GONE);

        RelativeLayout btnNieWiem = (RelativeLayout) findViewById(R.id.btnNieWiem);
        RelativeLayout btnWiem = (RelativeLayout) findViewById(R.id.btnWiem);

        ukryjLayouty(view_layout_powtorki);
        czy_wybor_testu = false;

        if (jezyk.equals("pl")) respowtorki = myDbPowtorki_PL.getAllData();
        if (jezyk.equals("en")) respowtorki = myDbPowtorki_EN.getAllData();
        if (jezyk.equals("de")) respowtorki = myDbPowtorki_DE.getAllData();

        view_pager_powtorki = (ViewPager) findViewById(R.id.viewPagerPowtorki);

        arrayNumber = new String[respowtorki.getCount()];
        arrayNazwyPanstw = new String[respowtorki.getCount()];
        arrayStolicePanstw = new String[respowtorki.getCount()];
        arrayFlagiPanst = new int[respowtorki.getCount()];

        int i = 0;
        while (respowtorki.moveToNext()) {
            int flaga = respowtorki.getInt(1);

            String number = "" + i;
            String nazwaPanstwa = respowtorki.getString(2);
            String stolicaPanstwa = respowtorki.getString(3);
            arrayNumber[i] = number;
            arrayNazwyPanstw[i] = nazwaPanstwa;
            arrayStolicePanstw[i] = stolicaPanstwa;
            arrayFlagiPanst[i] = flaga;
            i++;
        }

        if (arrayNumber.length == 0) {
            rl_nic_do_powtorki.setVisibility(View.VISIBLE);
            btnNieWiem.setVisibility(View.GONE);
            btnWiem.setVisibility(View.GONE);

        }

        customSwype = new CustoSwype(this, arrayNumber, arrayNazwyPanstw, arrayStolicePanstw, arrayFlagiPanst);
        view_pager_powtorki.setAdapter(customSwype);

        view_pager_powtorki.setCurrentItem(numer, true);
        customSwype.notifyDataSetChanged();

        ekran = 10;

    }

    //tu należy dopisywać wszytskie layouty jeśli pojawiaą sie jakieś nowe
    // funkcja ukrywa wszytskie layouty prócz tego przekazanego w argumencie
    public void ukryjLayouty(View view) {

        view_layout_splash_screen.setVisibility(View.INVISIBLE);
        view_layout_wybor_nauka_test_koniec.setVisibility(View.INVISIBLE);
        view_layout_nauka_wybor_obszaru.setVisibility(View.INVISIBLE);
        view_layout_nauka.setVisibility(View.INVISIBLE);
        view_layout_test.setVisibility(View.INVISIBLE);
        view_layout_wyniki_testu.setVisibility(View.INVISIBLE);
        view_layout_twoje_wyniki.setVisibility(View.INVISIBLE);
        view_layout_powiadomienie_o_nowszej_wersji.setVisibility(View.INVISIBLE);
        view_layout_ustawienia.setVisibility(View.INVISIBLE);
        view_layout_intro.setVisibility(View.INVISIBLE);
        view_layout_opis_panstwa.setVisibility(View.INVISIBLE);
        view_layout_wybor_testu.setVisibility(View.INVISIBLE);
        view_layout_powtorki.setVisibility(View.INVISIBLE);
        view_layout_pojedynek.setVisibility(View.INVISIBLE);
        view_layout_wynik_pojedynku.setVisibility(View.INVISIBLE);
        view_layout_wybor_nauka_mapa_lista.setVisibility(View.INVISIBLE);
        view_layout_podglad_mapy.setVisibility(View.INVISIBLE);
        view.setVisibility(View.VISIBLE);

    }

    public void pokazLayout(View view) {

        view.setVisibility(View.VISIBLE);
        view.bringToFront();
    }

    public void btn_nie_wiem_powtorki_click(View view) {

        int iloscPanstDoPowtorki = respowtorki.getCount();

        if (view_pager_powtorki.getCurrentItem() == iloscPanstDoPowtorki - 1) {

            view_pager_powtorki.setCurrentItem(0, true);

        } else {
            view_pager_powtorki.setCurrentItem(view_pager_powtorki.getCurrentItem() + 1, true);
        }

    }

    public void f_usun_widok_z_bazy_powtorek(View view) {
        int temp = view_pager_powtorki.getCurrentItem();
        if (jezyk.equals("pl")) myDbPowtorki_PL.deleteRow(arrayNazwyPanstw[temp]);
        if (jezyk.equals("en")) myDbPowtorki_EN.deleteRow(arrayNazwyPanstw[temp]);
        if (jezyk.equals("de")) myDbPowtorki_DE.deleteRow(arrayNazwyPanstw[temp]);

        f_btn_twoje_powtorki_2(temp);

    }

    public void f_btn_pojedynek() {


        handlerPowtorkiLay.removeCallbacks(myRunablePowtorkiLay);

        View rl_czerwony_lay1 = (View) findViewById(R.id.rl_czerwony_lay1);
        rl_czerwony_lay1.setVisibility(View.INVISIBLE);

        View rl_czerwony_lay2 = (View) findViewById(R.id.rl_czerwony_lay2);
        rl_czerwony_lay2.setVisibility(View.INVISIBLE);

        View rl_zielony_lay1 = (View) findViewById(R.id.rl_zielony_lay1);
        rl_zielony_lay1.setVisibility(View.INVISIBLE);

        View rl_zielony_lay2 = (View) findViewById(R.id.rl_zielony_lay2);
        rl_zielony_lay2.setVisibility(View.INVISIBLE);

        ukryjLayouty(view_layout_pojedynek);
        ekran = 6;//było ekran = 3

        pktGraczA = 0;
        pktGraczB = 0;

        f_zaktualizuj_Punkty_Pojedynku(pktGraczA, pktGraczB);
        f_generuj_runde_pojedynku();

    }

    private void f_zaktualizuj_Punkty_Pojedynku(int pktGraczA, int pktGraczB) {
        TextView txt1 = (TextView) findViewById(R.id.txt_gracz_A_liczba_punktow);
        TextView txt2 = (TextView) findViewById(R.id.txt_gracz_B_liczba_punktow);
        txt1.setText(String.valueOf(pktGraczA));
        txt2.setText(String.valueOf(pktGraczB));
    }

    public void f_btn_pojedynek_btn(View view) {
        //lay1 albo lay2
        int parentId = ((View) view.getParent().getParent().getParent().getParent().getParent()).getId();
        String idStr = getResources().getResourceEntryName(parentId);

        String tag = (String) view.getTag();

        TextView txt_gracz_A_i_B_nazwa = (TextView) findViewById(R.id.txt_gracz_A_i_B_nazwa);
        String temp = txt_gracz_A_i_B_nazwa.getText().toString();

        if (tag.equals(temp)) {

            b_czy_odgadnieto_w_pojedynku = true;

            //zablokuj wszytskie przyciski
            f_zablokuj_wszytskie_przyciski_pojedynek();

            if (idStr.equals("lay1")) {
                View rl_czerwony_lay1 = (View) findViewById(R.id.rl_czerwony_lay1);
                rl_czerwony_lay1.setVisibility(View.INVISIBLE);

                View rl_zielony_lay1 = (View) findViewById(R.id.rl_zielony_lay1);
                rl_zielony_lay1.setVisibility(View.VISIBLE);


                pktGraczA++;
                f_zaktualizuj_Punkty_Pojedynku(pktGraczA, pktGraczB);
                if (pktGraczA >= ILOSC_PYTAN_W_POJEDYNKU) {
                    //  Toast.makeText(MainActivity.this, "KONIEC GRY (A)", Toast.LENGTH_LONG).show();
                    RelativeLayout view_layout_wynik_pojedynku = (RelativeLayout) findViewById(R.id.rl_wynik_pojedynku);
                    view_layout_wynik_pojedynku.setScaleX(1.0f);
                    view_layout_wynik_pojedynku.setScaleY(1.0f);
                    b_czy_koniec_pojedynku = true;

                } else {
                    b_czy_koniec_pojedynku = false;
                }


            } else if (idStr.equals("lay2")) {
                View rl_czerwony_lay2 = (View) findViewById(R.id.rl_czerwony_lay2);
                rl_czerwony_lay2.setVisibility(View.INVISIBLE);

                View rl_zielony_lay2 = (View) findViewById(R.id.rl_zielony_lay2);
                rl_zielony_lay2.setVisibility(View.VISIBLE);

                pktGraczB++;
                f_zaktualizuj_Punkty_Pojedynku(pktGraczA, pktGraczB);
                if (pktGraczB >= ILOSC_PYTAN_W_POJEDYNKU) {
                    // Toast.makeText(MainActivity.this, "KONIEC GRY (B)", Toast.LENGTH_LONG).show();
                    RelativeLayout view_layout_wynik_pojedynku = (RelativeLayout) findViewById(R.id.rl_wynik_pojedynku);
                    view_layout_wynik_pojedynku.setScaleX(-1.0f);
                    view_layout_wynik_pojedynku.setScaleY(-1.0f);
                    b_czy_koniec_pojedynku = true;

                } else {
                    b_czy_koniec_pojedynku = false;
                }

            }

            //COLBACK wywoływany tylko w przypadku gdy udzielona odpowiedź jest prawidłowa
            handlerPowtorkiLay.removeCallbacks(myRunablePowtorkiLay);

            myRunablePowtorkiLay = new Runnable() {
                @Override
                public void run() {

                    handlerPowtorkiLay.removeCallbacks(myRunablePowtorkiLay);
                    if (!b_czy_koniec_pojedynku) {
                        f_generuj_runde_pojedynku();
                    } else if (b_czy_koniec_pojedynku) {

                        //zmniejszenie rozmiaru czcionki w polu tekstowym tylko jeśli jest użyta wersja niemiecka
                        Locale current = getResources().getConfiguration().locale;
                        String temp = current.toString();
//                            Toast.makeText(MainActivity.this, "Język = " + current, Toast.LENGTH_LONG).show();
                        if ((temp.equals("de")) || (temp.equals("de_DE"))) {
                            TextView tv_pojedynek_wygrales = (TextView) findViewById(R.id.tv_pojedynek_wygrales);
                            tv_pojedynek_wygrales.setTextSize(30f);
                            TextView tv_pojedynek_przegrales = (TextView) findViewById(R.id.tv_pojedynek_przegrales);
                            tv_pojedynek_przegrales.setTextSize(30f);
                        }

                        //

                        pokazLayout(view_layout_wynik_pojedynku);
                        f_zresetuj_kolory_layoutow_pojedynek();
                        TextView tv_pojedynek_wynik_pojedynku_przegrany = (TextView) findViewById(R.id.tv_pojedynek_wynik_pojedynku_przegrany);
                        tv_pojedynek_wynik_pojedynku_przegrany.setText(pktGraczA + " : " + pktGraczB);

                        TextView tv_pojedynek_wynik_pojedynku_wygrany = (TextView) findViewById(R.id.tv_pojedynek_wynik_pojedynku_wygrany);
                        tv_pojedynek_wynik_pojedynku_wygrany.setText(pktGraczB + " : " + pktGraczA);

                    }
                }
            };

            handlerPowtorkiLay.postDelayed(myRunablePowtorkiLay, 1500);
            //

        } else if (!tag.equals(temp)) {

            b_czy_odgadnieto_w_pojedynku = false;

            if (idStr.equals("lay1")) {

                View rl_czerwony_lay1 = (View) findViewById(R.id.rl_czerwony_lay1);
                Animation animacja1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                rl_czerwony_lay1.startAnimation(animacja1);
                rl_czerwony_lay1.setVisibility(View.INVISIBLE);

                if (pktGraczA > 0) {
                    pktGraczA--;
                }

                f_zaktualizuj_Punkty_Pojedynku(pktGraczA, pktGraczB);


            } else if (idStr.equals("lay2")) {

                View rl_czerwony_lay2 = (View) findViewById(R.id.rl_czerwony_lay2);
                Animation animacja2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                rl_czerwony_lay2.startAnimation(animacja2);
                rl_czerwony_lay2.setVisibility(View.INVISIBLE);

                if (pktGraczB > 0) {
                    pktGraczB--;
                }
                f_zaktualizuj_Punkty_Pojedynku(pktGraczA, pktGraczB);
            }

        }

    }

    private void f_zresetuj_kolory_layoutow_pojedynek() {

        View rl_czerwony_lay1 = (View) findViewById(R.id.rl_czerwony_lay1);
        rl_czerwony_lay1.setVisibility(View.INVISIBLE);
        View rl_czerwony_lay2 = (View) findViewById(R.id.rl_czerwony_lay2);
        rl_czerwony_lay2.setVisibility(View.INVISIBLE);

        View rl_zielony_lay1 = (View) findViewById(R.id.rl_zielony_lay1);
        rl_zielony_lay1.setVisibility(View.INVISIBLE);
        View rl_zielony_lay2 = (View) findViewById(R.id.rl_zielony_lay2);
        rl_zielony_lay2.setVisibility(View.INVISIBLE);

    }

    private void f_zablokuj_wszytskie_przyciski_pojedynek() {
        View lay1 = findViewById(R.id.lay1);
        View lay2 = findViewById(R.id.lay2);

        ImageView iv1A = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_A);
        ImageView iv2A = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_A);

        iv1A.setEnabled(false);
        iv2A.setEnabled(false);

        ImageView iv1B = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_B);
        ImageView iv2B = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_B);

        iv1B.setEnabled(false);
        iv2B.setEnabled(false);

        ImageView iv1C = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_C);
        ImageView iv2C = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_C);

        iv1C.setEnabled(false);
        iv2C.setEnabled(false);

        ImageView iv1D = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_D);
        ImageView iv2D = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_D);

        iv1D.setEnabled(false);
        iv2D.setEnabled(false);
    }

    private void f_odblokuj_wszytskie_przyciski_pojedynek() {
        View lay1 = findViewById(R.id.lay1);
        View lay2 = findViewById(R.id.lay2);

        ImageView iv1A = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_A);
        ImageView iv2A = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_A);

        iv1A.setEnabled(true);
        iv2A.setEnabled(true);

        ImageView iv1B = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_B);
        ImageView iv2B = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_B);

        iv1B.setEnabled(true);
        iv2B.setEnabled(true);

        ImageView iv1C = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_C);
        ImageView iv2C = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_C);

        iv1C.setEnabled(true);
        iv2C.setEnabled(true);

        ImageView iv1D = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_D);
        ImageView iv2D = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_D);

        iv1D.setEnabled(true);
        iv2D.setEnabled(true);
    }

    private void f_generuj_runde_pojedynku() {

        b_czy_odgadnieto_w_pojedynku = false;

        f_zresetuj_kolory_layoutow_pojedynek();
        f_odblokuj_wszytskie_przyciski_pojedynek();

        int[] arrayLiczyLosowe_0_3_Pojedynek = {1000, 1000, 1000, 1000};
        licznikPetliWhilePowtorki = 0;

        while (licznikPetliWhilePowtorki < 4) {
            flaga11Powtorki = false;

            liczbaLosowa1Powtorki = randomizePowtorki.nextInt(temp);

            for (int z = 0; z < arrayLiczyLosowe_0_3_Pojedynek.length; z++) {
                if (arrayLiczyLosowe_0_3_Pojedynek[z] == liczbaLosowa1Powtorki) {
                    flaga11Powtorki = true;
                }
            }

            if (flaga11Powtorki == false) {

                arrayLiczyLosowe_0_3_Pojedynek[licznikPetliWhilePowtorki] = liczbaLosowa1Powtorki;
                licznikPetliWhilePowtorki++;
            }

            if (licznikPetliWhilePowtorki == 4) {

                przypiszDoTablicPojedynek(arrayLiczyLosowe_0_3_Pojedynek, array_panstwa_wybrane_alfabetycznie, array_stolice_wybrane_alfabetycznie, array_flagi_wybranych_panstw_alfabetycznie);

            }

        }

    }

    private void przypiszDoTablicPojedynek(int[] arrayLiczyLosowe_0_3_Pojedynek, String[] array_nazwy_panstw, String[] array_nazwy_stolic, int[] zegarki_img_res) {

        arrayNazwyPanstwPojedynek = new String[4];
        arrayNazwyStolicPojedynek = new String[4];
        arrayFlagiPanstwPojedynek = new int[4];

        for (int z = 0; z < 4; z++) {
            //
            // Toast.makeText(MainActivity.this, " ::: " + arrayLiczyLosowe_0_3_Pojedynek[z], Toast.LENGTH_LONG).show();
            arrayNazwyPanstwPojedynek[z] = array_nazwy_panstw[arrayLiczyLosowe_0_3_Pojedynek[z]];
            arrayNazwyStolicPojedynek[z] = array_nazwy_stolic[arrayLiczyLosowe_0_3_Pojedynek[z]];
            arrayFlagiPanstwPojedynek[z] = zegarki_img_res[arrayLiczyLosowe_0_3_Pojedynek[z]];
        }

        View lay1 = findViewById(R.id.lay1);
        View lay2 = findViewById(R.id.lay2);

        TextView txtA = (TextView) lay1.findViewById(R.id.txt_gracz_A_i_B_nazwa);
        TextView txtB = (TextView) lay2.findViewById(R.id.txt_gracz_A_i_B_nazwa);

        ImageView iv1A = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_A);
        ImageView iv2A = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_A);

        ImageView iv1B = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_B);
        ImageView iv2B = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_B);

        ImageView iv1C = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_C);
        ImageView iv2C = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_C);

        ImageView iv1D = (ImageView) lay1.findViewById(R.id.img_pojedynek_wariant_D);
        ImageView iv2D = (ImageView) lay2.findViewById(R.id.img_pojedynek_wariant_D);

        iv1A.setImageResource(arrayFlagiPanstwPojedynek[0]);
        iv2A.setImageResource(arrayFlagiPanstwPojedynek[0]);
        iv1B.setImageResource(arrayFlagiPanstwPojedynek[1]);
        iv2B.setImageResource(arrayFlagiPanstwPojedynek[1]);
        iv1C.setImageResource(arrayFlagiPanstwPojedynek[2]);
        iv2C.setImageResource(arrayFlagiPanstwPojedynek[2]);
        iv1D.setImageResource(arrayFlagiPanstwPojedynek[3]);
        iv2D.setImageResource(arrayFlagiPanstwPojedynek[3]);

        iv1A.setTag(arrayNazwyPanstwPojedynek[0]);
        iv2A.setTag(arrayNazwyPanstwPojedynek[0]);
        iv1B.setTag(arrayNazwyPanstwPojedynek[1]);
        iv2B.setTag(arrayNazwyPanstwPojedynek[1]);
        iv1C.setTag(arrayNazwyPanstwPojedynek[2]);
        iv2C.setTag(arrayNazwyPanstwPojedynek[2]);
        iv1D.setTag(arrayNazwyPanstwPojedynek[3]);
        iv2D.setTag(arrayNazwyPanstwPojedynek[3]);
        Random randomizePojedynek = new Random();
        l_losowa_z_zakresu_0_3 = randomizePojedynek.nextInt(4);

        txtA.setText(arrayNazwyPanstwPojedynek[l_losowa_z_zakresu_0_3]);
        txtB.setText(arrayNazwyPanstwPojedynek[l_losowa_z_zakresu_0_3]);
    }

    public void f_btn_pojedynek_wybierz_obszar(View view) {
        generuj_widok_wyboru_nauka = 3;
        ukryjLayouty(view_layout_nauka_wybor_obszaru);
        TextView txt_EkranWyboruNauka = (TextView) findViewById(R.id.txt_EkranWyboruNauka);
        txt_EkranWyboruNauka.setText(getResources().getString(R.string.text_pojedynek_wybierz_obszar));
        ekran = 3;
        b_czy_koniec_pojedynku = false;

    }

    private void generuj_widok_pojedynek(String kontynent, Boolean calyswiat) {
        temp = 0;

        // wyłuskanie państw z wybranego obszaru
        if (calyswiat == false) {
            for (int z = 0; z < dlugoscTablicy; z++) {
                if (array_nazwy_kontynentow[z].equals(kontynent)) {
                    array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                    array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                    array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                    temp++;
                }

                if (z == dlugoscTablicy - 1) f_btn_pojedynek();
            }

        } else {
            for (int z = 0; z < dlugoscTablicy; z++) {
                array_panstwa_wybrane_alfabetycznie[temp] = array_nazwy_panstw[z];
                array_stolice_wybrane_alfabetycznie[temp] = array_nazwy_stolic[z];
                array_flagi_wybranych_panstw_alfabetycznie[temp] = flagi_img_array[z];
                temp++;

                if (z == dlugoscTablicy - 1) f_btn_pojedynek();
            }

        }
    }

    public void f_btn_nowy_pojedynek(View view) {

        f_btn_pojedynek();


    }

    public void f_btn_powrot_do_menu(View view) {
        pokaz_layout_wybor_nauka_test_koniec();

    }

    public void f_btn_wstecz_wybor_trybu_nauki(View view) {

        if (czy_wybor_testu == true) {
            ukryjLayouty(view_layout_wybor_testu);
            ekran = 10;

        } else {
            ukryjLayouty(view_layout_wybor_nauka_test_koniec);
            ekran = 2;
            czy_wybor_testu = false;
            sprawdzCzyWyswietlicTwojeWyniki();
            sprawdzCzyWyswietlicBazePowtorek();
        }

    }

    public void f_btn_wstecz_nauka_wybor_obszaru(View view) {

        f_btn_wstecz_nauka_wybor_obszaru();


    }

    public void f_btn_wstecz_nauka_wybor_obszaru() {

//        Toast.makeText(MainActivity.this, "ekran = " + ekran, Toast.LENGTH_LONG).show();
        if (czy_wybor_testu == true) {
            ukryjLayouty(view_layout_wybor_testu);
            ekran = 10;
        } else if ((czy_wybor_testu == false) && (ekran == 13)) {
            ukryjLayouty(view_layout_wybor_nauka_mapa_lista);
            ekran = 3;
            czy_wybor_testu = false;
            sciezka = "A";

        } else if ((czy_wybor_testu == true) && (ekran != 13)) {
            ukryjLayouty(view_layout_wybor_nauka_test_koniec);
            ekran = 2;
            czy_wybor_testu = false;
            sprawdzCzyWyswietlicTwojeWyniki();
            sprawdzCzyWyswietlicBazePowtorek();
        } else if ((czy_wybor_testu == false) && (ekran != 13)) {
            ukryjLayouty(view_layout_wybor_nauka_mapa_lista);
            ekran = 3;
            czy_wybor_testu = false;
            sciezka = "A";
        }


    }

    public void f_btn_lista_wybor_obszaru_do_nauki(View view) {
        generuj_widok_wyboru_nauka = 1;
        ukryjLayouty(view_layout_nauka_wybor_obszaru);
        TextView txt_EkranWyboruNauka = (TextView) findViewById(R.id.txt_EkranWyboruNauka);
        txt_EkranWyboruNauka.setText(getResources().getString(R.string.text_nauka_wybierz_obszar));
        ekran = 13;
        czy_wybor_testu = false;
        sciezka = "A1";

    }

    public void f_btn_pokaz_mape(View view) {

        ukryjLayouty(view_layout_podglad_mapy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ekran = 21;
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String webMessage) {
            final String msgeToast = webMessage;
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    // This gets executed on the UI thread so it can safely modify Views

                    String temp = map.get(msgeToast);
                    int len = flagi_img_array.length;
                    int id = getResources().getIdentifier("eu.huras.marcin.flagiistolice:drawable/" + temp, null, null);

                    v = 0;

                    for (int i = 0; i < len; i++) {
                        v++;
                        if (id == flagi_img_array[i]) {

                            ImageView iv_widok_mapy_flaga = (ImageView) findViewById(R.id.iv_widok_mapy_flaga);
                            iv_widok_mapy_flaga.setImageResource(flagi_img_array[i]);

                            TextView tv_widok_mapy_nazwa_panstwa = (TextView) findViewById(R.id.tv_widok_mapy_nazwa_panstwa);
                            tv_widok_mapy_nazwa_panstwa.setText(array_nazwy_panstw[i]);

                            TextView tv_widok_mapy_nazwa_stolicy = (TextView) findViewById(R.id.tv_widok_mapy_nazwa_stolicy);
                            tv_widok_mapy_nazwa_stolicy.setText(getResources().getString(R.string.text_stolica) + " " + array_nazwy_stolic[i]);
                            animacjaSlideMapa();

                            break;
                        }
                    }

                }
            });

        }

    }

    public void animacjaSlideMapa() {

        RelativeLayout rl_slide = (RelativeLayout) findViewById(R.id.rl_slide);
        int imgWidth = rl_slide.getWidth();

        Animation animation = new TranslateAnimation(-imgWidth, 0, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(600);
        Interpolator customInterpolator = PathInterpolatorCompat.create(0.000f, 1.265f, 0.540f, 1.165f);
        animation.setInterpolator(customInterpolator);
        rl_slide.startAnimation(animation);

    }

    public void refreshAuto(String lang) {
        LocaleManager.setNewLocale(this, lang);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }




}
