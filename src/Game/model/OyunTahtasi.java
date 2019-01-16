package Game.model;


/**
 * Oyun Tahtasini temsil eder
 */
public class OyunTahtasi {

    public static final char BOS_DEGER = 'E';

    private char[][] tahta;

    public OyunTahtasi() {
    }

    public OyunTahtasi(char[][] tahta) {
        this.tahta = tahta;
    }

    /**
     * Oyun tahtasini doner
     *
     * @return Tahta bilgileri
     */
    public char[][] oyunTahtasiniAl() {
        return this.tahta;
    }

    /**
     * Oyun tahtasindaki bilgileri konsol ekranina yazar
     */
    public void oyunTahtasiniYazdir() {
        /*if (tahta != null) {
            for (char[] chars : tahta) {
                for (char aChar : chars) {
                    //Eger E yani empty ise bos char yaziyouz degilse karakter yazilir
                    /*if (aChar == BOS_DEGER) {
                        System.out.print(" " + " \t");
                    } else {
                        System.out.print(aChar + " \t");
                    }
                    System.out.print(aChar + "\t");
                }
                System.out.println("\n");
            }
        }*/
        int deger=0;
        while(deger<tahta.length){
            System.out.print(" " + deger + "\t");
            deger++;
        }
        System.out.println("");
        for (int i = 0; i < tahta.length; i++) {
            System.out.print(i);
            for (int j = 0; j < tahta.length; j++) {
                if(tahta[i][j]=='E'){
                    System.out.print("|   " + " " + "   ");
                }
                else
                System.out.print("|   " + tahta[i][j] + "   ");
            }
            System.out.print("\n");
            if(tahta.length==3){
                System.out.println("-----------------------");
            }
            else if(tahta.length==5){
                System.out.println("---------------------------------------");
            }
            else if(tahta.length==7){
                System.out.println("-------------------------------------------------------");
            }
        }
    }


    /**
     * Oyunu tarafindan ilgili koordinata hamle yapilir
     *
     * @param koordinat Koordinat bilgisi
     * @param sembol    Oyuncu Bilgisi
     * @return Koordinat hamleye musait ise true degil ise false
     */
    public boolean hamleyiYaz(String koordinat, char sembol) {
        if (koordinat != null && !koordinat.isEmpty()) {
            String[] koor = koordinat.split(",");
            int x = Integer.parseInt(koor[0]);
            int y = Integer.parseInt(koor[1]);

            if (tahta[x][y] != BOS_DEGER) {
                return false;
            }
            tahta[x][y] = sembol;
            return true;
        }
        return false;
    }

    /**
     * Oyuncunun oyunu kazanip kazanmadigini kontrol eder.
     *
     * @param oyuncu Oyuncu id bilgisi
     * @return Oyunu kazandiysa true kazanamadiysa false
     */
    public boolean kazanan(char oyuncu) {
        String kazananMetin = "" + oyuncu + oyuncu + oyuncu;
        int boyut = tahta.length;
        if(boyut==5){
           kazananMetin = "" + oyuncu + oyuncu + oyuncu + oyuncu + oyuncu;
        }
        if(boyut==7){
            kazananMetin = "" + oyuncu + oyuncu + oyuncu + oyuncu + oyuncu + oyuncu + oyuncu;
        }
        //Satir satir kontrol yapilir
        for (int satir = 0; satir < boyut; satir++) {
            StringBuilder satirMetini = new StringBuilder();
            for (int i = 0; i < boyut; i++) {
                satirMetini.append(tahta[satir][i]);
            }
            if (satirMetini.toString().contains(kazananMetin)) {
                return true;
            }
        }

        //Kolon kolon kontrol yapilir
        for (int kolon = 0; kolon < boyut; kolon++) {
            StringBuilder kolonMetini = new StringBuilder();
            for (int j = 0; j < boyut; j++) {
                kolonMetini.append(tahta[j][kolon]);
            }
            if (kolonMetini.toString().contains(kazananMetin)) {
                return true;
            }
        }

        StringBuilder koseMetini = new StringBuilder();
        //Capraz kontrol yapilir
        for (int kose = 0; kose < boyut; kose++) {
            koseMetini.append(tahta[kose][kose]);
        }
        if (koseMetini.toString().contains(kazananMetin)) {
            return true;
        }

        StringBuilder tersKoseMetini = new StringBuilder();
        int index = 0;
        for (int tersKose = boyut - 1; tersKose >= 0; tersKose--) {
            tersKoseMetini.append(tahta[index][tersKose]);
            index++;
        }

        if (tersKoseMetini.toString().contains(kazananMetin)) {
            return true;
        }
        return false;
    }

    /**
     * Oyunu kontrol eder berabere ise true doner
     *
     * @return Berabere true aksi durumlarda false
     */
    public boolean beraberlikKontrol() {
        for (char[] satir : tahta) {
            for (char hucre : satir) {
                if (hucre == 'E') {
                    return false;
                }
            }
        }
        return true;
    }

}
