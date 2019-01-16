package Game.model;

import Game.OyunaBasla;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Oyuncu {

    private String isim;
    private boolean bilgisayar;
    private char karakter;

    private String hamle;


    public Oyuncu() {
        this.bilgisayar = false;
        this.karakter = 'X';
    }

    public Oyuncu(boolean insanmiKontrolu) {
        this.bilgisayar = insanmiKontrolu;
        if (!bilgisayar) {
            this.karakter = 'X';
        } else {
            this.karakter = 'O';
        }
    }

    public Oyuncu(boolean insanmiKontrolu, char karakter) {
        this.bilgisayar = insanmiKontrolu;
        this.karakter = karakter;
    }


    /**
     * Oyuncunun secmis oldugu karakteri doner
     *
     * @return karakter
     */
    public char karakteriAl() {
        return this.karakter;
    }

    /**
     * Oyuncunun turunu doner. Bilgisayar ise true insan ise false
     *
     * @return oyuncu tipi
     */
    public boolean oyuncuTurunuAl() {
        return bilgisayar;
    }

    /**
     * @return
     */
    public String oyuncununHamlesiniAl() throws Exception {
        return hamle;
    }

    /**
     * @return
     */
    public String insanOyuncuHamlesiniKotrol(char[][] tahta) throws Exception {
        System.out.println("KOORDINATLARI SIRASIYLA GIRINIZ (X,Y)");
        System.out.println("Cıkmak Icin (exit) Giriniz");
        //TODO int girme kontrolu eklendi
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("X=?");
        String konsolSatiri = br.readLine();
        if (cikmakMiIstiyor(konsolSatiri)) {
            System.exit(1);
        }

        Integer x = null;
        Integer y = null;
        while (true) {
            if (x == null) {
                try {
                    if (cikmakMiIstiyor(konsolSatiri)) {
                        System.exit(1);
                    }
                    x = Integer.valueOf(konsolSatiri.trim());
                    while (true) {
                        if (x >= 0 && x <= tahta.length - 1) {
                            break;
                        }
                        System.out.println("Cıkmak Icin (exit) Giriniz");
                        System.out.println("Yanlis Deger Girdiniz");
                        konsolSatiri = br.readLine();
                        if (cikmakMiIstiyor(konsolSatiri)) {
                            System.exit(1);
                        }
                        x = Integer.valueOf(konsolSatiri);
                    }
                } catch (NumberFormatException num) {
                    x = null;
                    System.out.println("Lutfen sayi girisi yapiniz! X=?");
                    konsolSatiri = br.readLine();
                }
            }
            if (x != null && y == null) {
                try {
                    System.out.println("X=" + x + ", Y=?");
                    konsolSatiri = br.readLine();
                    if (cikmakMiIstiyor(konsolSatiri)) {
                        System.exit(1);
                    }
                    y = Integer.valueOf(konsolSatiri.trim());
                    while (true) {
                        if (y >= 0 && y <= tahta.length - 1) {
                            break;
                        }
                        System.out.println("Cıkmak Icin (exit) Giriniz");
                        System.out.println("Yanlis Deger Girdiniz");
                        konsolSatiri = br.readLine();
                        if (cikmakMiIstiyor(konsolSatiri)) {
                            System.exit(1);
                        }
                        y = Integer.valueOf(konsolSatiri);
                    }
                    break;
                } catch (NumberFormatException num) {
                    y = null;
                    System.out.println("Lutfen sayi girisi yapiniz!");
                }
            }
        }
        hamle = x + "," + y;
        return hamle;
    }

    public boolean cikmakMiIstiyor(String deger) {
        if (deger != null && "exit".equalsIgnoreCase(deger.trim())) {
            return true;
        }
        return false;
    }

    /**
     * Tahta uzerindeki bos yerler testip edilir ve random bir hucre secilir
     *
     * @return
     */
    public String bilgisayarHamlesiUret(char[][] tahta) {
        List<String> bosHucrelerKoordinatlari = new ArrayList<>();
        for (int i = 0; i < tahta.length; i++) {
            for (int j = 0; j < tahta.length; j++) {
                if (tahta[i][j] == OyunTahtasi.BOS_DEGER) {
                    bosHucrelerKoordinatlari.add(i + "," + j);
                }
            }
        }

        if (!bosHucrelerKoordinatlari.isEmpty() && bosHucrelerKoordinatlari.size() > 0) {
            Random random = new Random();
            int randomDeger = random.nextInt(bosHucrelerKoordinatlari.size() - 1);
            hamle = bosHucrelerKoordinatlari.get(randomDeger);
            return hamle;
        }
        return "";
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public boolean isBilgisayar() {
        return bilgisayar;
    }

    public void setBilgisayar(boolean bilgisayar) {
        this.bilgisayar = bilgisayar;
    }

    public char getKarakter() {
        return karakter;
    }

    public void setKarakter(char karakter) {
        this.karakter = karakter;
    }
}
