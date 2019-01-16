package Game.model;


public class Oyun {

    private Oyuncu bilgisayar;
    private Oyuncu insan;
    private OyunTahtasi oyunTahtasi;
    private String insanKazanir = null;
    private String bilgisayarKazanir = null;
    private boolean hamleSirasiInsandaMi;

    public Oyun(Oyuncu bilgisayar, Oyuncu insan, OyunTahtasi oyunTahtasi) {
        this.bilgisayar = bilgisayar;
        this.insan = insan;
        this.oyunTahtasi = oyunTahtasi;
        insanKazanir = "" + insan.karakteriAl() + insan.karakteriAl() + insan.karakteriAl();
        bilgisayarKazanir = "" + bilgisayar.karakteriAl() + bilgisayar.karakteriAl() + bilgisayar.karakteriAl();
        hamleSirasiInsandaMi = true;
    }

    //Oyunun bitip bitmedigini kontrol eder
    public boolean oyunDevamEdiyorMu() {
        if (oyunTahtasi.kazanan(insan.karakteriAl())) {
            return false;
        }

        if (oyunTahtasi.kazanan(bilgisayar.karakteriAl())) {
            return false;
        }

        if (oyunTahtasi.beraberlikKontrol()) {
            return false;
        }
        return true;
    }

    public void hamlesirasiBilgisayarda() {
        this.hamleSirasiInsandaMi = false;
    }

    public void hamleSirasiInsanda() {
        this.hamleSirasiInsandaMi = true;
    }

    public void hamleSirasiManuelDegistir(boolean hamleSirasiInsandaMi) {
        this.hamleSirasiInsandaMi = hamleSirasiInsandaMi;
    }

    public Oyuncu getBilgisayar() {
        return bilgisayar;
    }

    public Oyuncu getInsan() {
        return insan;
    }

    public OyunTahtasi getOyunTahtasi() {
        return oyunTahtasi;
    }

    public boolean isHamleSirasiInsandaMi() {
        return hamleSirasiInsandaMi;
    }
}
