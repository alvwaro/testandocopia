package dados.mappers;

import negocio.Cliente;

public class ClienteCsvMapper {

    /**
     * Converte um objeto Cliente para uma linha no formato CSV.
     * @param cliente O objeto Cliente a ser convertido.
     * @return Uma string formatada como uma linha CSV.
     */
    public static String toCsvLine(Cliente cliente) {
        return String.format("\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%b",
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getIdade(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getEmail(),
                cliente.getTipo(),
                cliente.isCadastrado());
    }

    /**
     * Converte uma linha CSV para um objeto Cliente.
     * @param linha A string da linha CSV.
     * @return Um novo objeto Cliente.
     * @throws IllegalArgumentException se a linha for inválida.
     */
    public static Cliente fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length == 8) {
            Cliente cliente = new Cliente(dados[1], Integer.parseInt(dados[2]), dados[0], dados[3], dados[4], dados[5], dados[6]);
            cliente.setCadastrado(Boolean.parseBoolean(dados[7]));
            return cliente;
        }
        throw new IllegalArgumentException("Linha CSV inválida para Cliente: " + linha);
    }
}