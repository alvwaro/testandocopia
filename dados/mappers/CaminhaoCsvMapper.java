package dados.mappers;

import negocio.Caminhao;

public class CaminhaoCsvMapper {

    public static String toCsvLine(Caminhao caminhao) {
        return String.format("\"%s\",%d,\"%s\",%b",
                caminhao.getPlaca(),
                caminhao.getCapacidade(),
                caminhao.getStatus(),
                caminhao.getCadastrado());
    }

    public static Caminhao fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length == 4) {
            Caminhao caminhao = new Caminhao(dados[0], Integer.parseInt(dados[1]), dados[2]);
            caminhao.setCadastrado(Boolean.parseBoolean(dados[3]));
            return caminhao;
        }
        throw new IllegalArgumentException("Linha CSV inválida para Caminhao: " + linha);
    }
}