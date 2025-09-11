package dados.mappers;

import negocio.Produto;
import java.util.Locale;

public class ProdutoCsvMapper {

    /**
     * Converte um objeto Produto para uma linha no formato CSV.
     * @param produto O objeto Produto a ser convertido.
     * @return Uma string formatada como uma linha CSV.
     */
    public static String toCsvLine(Produto produto) {
        return String.format(Locale.US, "\"%s\",\"%s\",\"%s\",%.2f,%d,%b",
                produto.getCodigo(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.isCadastrado());
    }

    /**
     * Converte uma linha CSV para um objeto Produto.
     * @param linha A string da linha CSV.
     * @return Um novo objeto Produto.
     * @throws IllegalArgumentException se a linha for inválida.
     */
    public static Produto fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length == 6) {
            Produto produto = new Produto(dados[0], dados[1], dados[2], Double.parseDouble(dados[3]), Integer.parseInt(dados[4]));
            produto.setCadastrado(Boolean.parseBoolean(dados[5]));
            return produto;
        }
        throw new IllegalArgumentException("Linha CSV inválida para Produto: " + linha);
    }
}