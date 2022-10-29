package utils;

public class GeradorDeHash {

    public static String getGerarStringAleatoria(int stringTamanho) {

        String nomeAleatorio = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(stringTamanho);

        for (int i = 0; i < stringTamanho; i++) {
            int index = (int) (nomeAleatorio.length()*Math.random());

            sb.append(nomeAleatorio.charAt(index));
        }

        return sb.toString();
    }
}
