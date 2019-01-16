package Game;

import Game.model.Oyun;
import Game.model.OyunKaydi;
import Game.model.OyunTahtasi;
import Game.model.Oyuncu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class OyunaBasla {

    private static final String USER_DIR = "user.dir";
    private static final String OYUN_KLASORU = "/tictactoc/";
    private static final String OYUN_DOSYASI = "oyun.txt";

    private static Thread hook;

    public static void main(String[] args) throws Exception {
        menu();
    }

    public static void shutdown() {
        hook.run();
    }

    /**
     * Menuyu Konsolda gosterir
     */
    private static void menu() throws Exception {
        clearConsole();
        System.out.println("1-YENI OYUN");
        System.out.println("2-KAYITLI OYUNA DEVAM");
        System.out.println("3-OYUN HAKKINDA");
        System.out.println("4-CIKIS");

        int menuDegeri = menuSecimi();
        while (true) {
            if (menuDegeri != -1) {
                break;
            }
            menuDegeri = menuSecimi();
        }
        if (menuDegeri == 1) {
            Oyun oyun = yeniOyunMenu();
            oyunuBaslat(oyun);
            altMenu();
        } else if (menuDegeri == 2) {
            Oyun oyun = dosyayiOku();
            oyunuBaslat(oyun);
            altMenu();
        } else if (menuDegeri == 3) {
            System.out.println("TIC TAC TOE OYUNU");
            System.out.println("1-Oyun java dilinde yazılmıştır");
            System.out.println("2-Bu oyunda kullanıcı kendi sembolünden oyun tahtasının boyutu kadar yanyana getirmeyi amaçlar");
            System.out.println("3-Oyun bilgisayara karşı oynanır");
            System.out.println("4-Oyunda kaydetme özelligi bulunur");
            System.out.println("5-IYI OYUNLAR !!!!");
            altMenu();
        } else if (menuDegeri == 4) {
            System.exit(0);
        }

    }

    private static void oyunuBaslat(Oyun oyun) throws Exception {
        clearConsole();
        System.out.println("HOSGELDINIZ " + oyun.getInsan().getIsim());
        oyun.getOyunTahtasi().oyunTahtasiniYazdir();

        //EGER OYUN KAPANIRSA BURASI CALISSIN
        hook = new ShutdownHook(oyun);
        Runtime.getRuntime().addShutdownHook(hook);

        while (oyun.oyunDevamEdiyorMu()) {
            if (oyun.isHamleSirasiInsandaMi()) {
                String hamle = oyun.getInsan().insanOyuncuHamlesiniKotrol(oyun.getOyunTahtasi().oyunTahtasiniAl());
                boolean hamleyiYaz = oyun.getOyunTahtasi().hamleyiYaz(hamle, oyun.getInsan().karakteriAl());
                if (!hamleyiYaz) {
                    System.err.println("Hamle sirasinda hata olustu");
                } else {
                    //Hamle sirasi degistirlir
                    oyun.hamlesirasiBilgisayarda();
                }
                clearConsole();
                oyun.getOyunTahtasi().oyunTahtasiniYazdir();
            } else {
                String hamle = oyun.getBilgisayar().bilgisayarHamlesiUret(oyun.getOyunTahtasi().oyunTahtasiniAl());
                boolean hamleyiYaz = oyun.getOyunTahtasi().hamleyiYaz(hamle, oyun.getBilgisayar().karakteriAl());
                if (!hamleyiYaz) {
                    System.err.println("Hamle sirasinda hata olustu");
                } else {
                    //Hamle sirasi degistirlir
                    oyun.hamleSirasiInsanda();
                }
                clearConsole();
                oyun.getOyunTahtasi().oyunTahtasiniYazdir();
            }
            System.out.println("Hamle giriniz");
        }

        boolean kazananVar = false;
        if (oyun.getOyunTahtasi().kazanan(oyun.getInsan().karakteriAl())) {
            kazananVar = true;
            System.out.println("TEBRIKLERRRR KAZANDINIZ");
        }

        if (!kazananVar && oyun.getOyunTahtasi().kazanan(oyun.getBilgisayar().karakteriAl())) {
            kazananVar = true;
            System.out.println("BILGISAYAR KAZANDI");
        }

        if (!kazananVar && oyun.getOyunTahtasi().beraberlikKontrol()) {
            System.out.println("OYUN BERABERE BITTI!!!!");
        }

    }

    private static Oyun yeniOyunMenu() {
        Oyun oyun = null;
        Oyuncu insan = null;
        OyunTahtasi oyunTahtasi = null;
        clearConsole();
        try {
            System.out.println("YENI OYUN BASLATILIYOR");
            System.out.println("ISMINIZ:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String isim = br.readLine();
            // Burada oyun boyutunu sayi ile limitlenir
            System.out.println("LUTFEN OYUN BOYUTUNU (3)-(5)-(7) DEN BİRİNİ GIRIN ");
            System.out.println("OYUN BOYUT:");

            Integer n;
            while (true) {
                String deger = br.readLine();
                if (deger == null || deger.trim().isEmpty()) {
                    System.out.println("Bos deger giremezsiniz!");
                    continue;
                }
                try {
                    n = Integer.valueOf(deger);
                } catch (NumberFormatException nm) {
                    System.out.println("Sayi disinda baska bir deger giremezsiniz!");
                    n = null;
                    continue;
                }
                if (n != null && !(n == 3 || n == 5 || n == 7)) {
                    System.out.println("Yanlis Deger Girdiniz!");
                    n = null;
                    continue;
                }
                if (n != null) {
                    break;
                }
            }

            char[][] tahta = new char[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tahta[i][j] = OyunTahtasi.BOS_DEGER;
                }
            }
            oyunTahtasi = new OyunTahtasi(tahta);
            int t = 0;
            String isaret = "X";
            String isaret2 = "O";
            System.out.printf("HARFINIZ:");
            String sembol = br.readLine();
            if (!(sembol.equals(isaret) || sembol.equals(isaret2))) {
                while (t != 1) {
                    System.out.println("Yanlis Deger Girdiniz");
                    sembol = br.readLine();
                    if (sembol.equals(isaret) || sembol.equals(isaret2)) {
                        t = 1;
                    }
                }
            }
            System.out.println(isim + " " + n + " " + sembol);
            insan = new Oyuncu(false, sembol.charAt(0));
            insan.setIsim(isim);
        } catch (Exception ex) {
            System.err.println(ex);
        }

        char bilgisayarSembol = 'O';
        if (insan.karakteriAl() == 'O') {
            bilgisayarSembol = 'X';
        }
        Oyuncu bilgisayar = new Oyuncu(true, bilgisayarSembol);
        oyun = new Oyun(bilgisayar, insan, oyunTahtasi);
        return oyun;
    }

    private static void altMenu() throws Exception {
        System.out.println("(0) GERI | (9) KAPAT");
        Scanner scan = new Scanner(System.in);
        int secim = scan.nextInt();
        if (secim == 9) {

            exit(0);
        } else if (secim == 0) {
            menu();
        }
    }

    private static int menuSecimi() {
        final String gecersizMenuMesaj = "Gecersiz bir menu secimi yaptiniz. Lutfen gecerli bir menu seciniz.";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br.readLine());
            if (i < 1 || i > 4) {
                System.out.println(gecersizMenuMesaj);
                return -1;
            }
            return i;
        } catch (IOException | NumberFormatException nfe) {
            System.out.println(gecersizMenuMesaj);
        }
        return -1;
    }

    /**
     * Konsolu temizler
     */
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void dosyayaYaz(ByteBuffer byteBuffer)
            throws IOException {

        Set options = new HashSet();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.TRUNCATE_EXISTING);
        options.add(StandardOpenOption.WRITE);

        final String oyunPath = System.getProperty(USER_DIR) + OYUN_KLASORU;
        Path path = Paths.get(oyunPath);
        Files.createDirectories(path);

        Path dosya = Paths.get(oyunPath + OYUN_DOSYASI);
        FileChannel fileChannel = FileChannel.open(dosya, options);
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }

    public static Oyun dosyayiOku() throws IOException {

        final String oyunPath = System.getProperty(USER_DIR) + OYUN_KLASORU + OYUN_DOSYASI;
        RandomAccessFile randomAccessFile = new RandomAccessFile(oyunPath, "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        Charset charset = Charset.forName("UTF-8");

        Oyuncu bilgisayar = null;
        Oyuncu insan = null;
        OyunTahtasi oyunTahtasi = null;
        boolean hamleSirasiInsanMi = false;
        int boyut = 0;
        String tahta = "";
        while (fileChannel.read(byteBuffer) > 0) {
            byteBuffer.rewind();
            String data = charset.decode(byteBuffer).toString().trim();
            String[] strings = data.split("#");
            for (String satir : strings) {
                if (satir.contains(OyunKaydi.KULLANICI_ADI)) {
                    if (insan == null) {
                        insan = new Oyuncu(true);
                    }
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    insan.setIsim(split[1].trim());
                } else if (satir.contains(OyunKaydi.KULLANICI_SEMBOL)) {
                    if (insan == null) {
                        insan = new Oyuncu(false);
                    }
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    insan.setKarakter(split[1].charAt(0));
                } else if (satir.contains(OyunKaydi.BILGISAYAR_SEMBOL)) {
                    if (bilgisayar == null) {
                        bilgisayar = new Oyuncu(true);
                    }
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    bilgisayar.setKarakter(split[1].charAt(0));
                } else if (satir.contains(OyunKaydi.SIRA_KIMDE)) {
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    if ("INSAN".equals(split[1])) {
                        hamleSirasiInsanMi = true;
                    }
                } else if (satir.contains(OyunKaydi.TAHTA)) {
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    tahta = split[1];
                } else if (satir.contains(OyunKaydi.BOYUT)) {
                    String[] split = satir.split(OyunKaydi.AYIRICI);
                    boyut = Integer.parseInt(split[1]);
                }
            }
            byteBuffer.flip();
        }
        fileChannel.close();
        randomAccessFile.close();

        char[][] kayitliTahta = new char[boyut][boyut];
        String[] tahtaSatirlari = tahta.split("\\|");

        for (int i = 0; i < tahtaSatirlari.length; i++) {
            String satir = tahtaSatirlari[i];
            String[] degerler = satir.split(",");
            for (int j = 0; j < degerler.length; j++) {
                kayitliTahta[i][j] = degerler[j].trim().charAt(0);
            }
        }
        oyunTahtasi = new OyunTahtasi(kayitliTahta);
        Oyun oyun = new Oyun(bilgisayar, insan, oyunTahtasi);
        oyun.hamleSirasiManuelDegistir(hamleSirasiInsanMi);
        return oyun;
    }

    public static void exit(int status) {
        final Runtime runtime = Runtime.getRuntime();
        try {
            runtime.halt(status);
            runtime.exit(status);
        } catch (Throwable x) {
            runtime.halt(status);
        }
    }


}
