package Game;

import Game.model.Oyun;
import Game.model.OyunKaydi;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author mdemir
 */
class ShutdownHook extends Thread {

    private Oyun oyun;

    ShutdownHook(Oyun oyun) {
        this.oyun = oyun;
    }

    public void run() {
        try {
            String matris = "";
            for (char[] row : oyun.getOyunTahtasi().oyunTahtasiniAl()) {
                String string = Arrays.toString(row);
                string = string.replaceAll("\\[", "");
                string = string.replaceAll("\\]", "");
                matris += string + "|";
            }

            StringBuilder data = new StringBuilder();
            data.append(OyunKaydi.TAHTA + OyunKaydi.AYIRICI + matris);
            data.append("#");
            data.append(OyunKaydi.BOYUT + OyunKaydi.AYIRICI + oyun.getOyunTahtasi().oyunTahtasiniAl().length);
            data.append("#");
            data.append(OyunKaydi.BILGISAYAR_SEMBOL + OyunKaydi.AYIRICI + oyun.getBilgisayar().karakteriAl());
            data.append("#");
            data.append(OyunKaydi.KULLANICI_SEMBOL + OyunKaydi.AYIRICI + oyun.getInsan().karakteriAl());
            data.append("#");
            data.append(OyunKaydi.SIRA_KIMDE + OyunKaydi.AYIRICI + (oyun.isHamleSirasiInsandaMi() ? "INSAN" : "BILGISAYAR"));
            data.append("#");
            data.append(OyunKaydi.KULLANICI_ADI + OyunKaydi.AYIRICI + oyun.getInsan().getIsim());

            byte[] byteArray = data.toString().getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
            OyunaBasla.dosyayaYaz(byteBuffer);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}