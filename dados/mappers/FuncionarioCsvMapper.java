package dados.mappers;

import negocio.Funcionario;
import negocio.Motorista;
import negocio.PerfilUsuario;
import java.time.LocalDateTime;
import java.util.Locale;

public class FuncionarioCsvMapper {

    /**
     * Converte um objeto Funcionario (ou Motorista) para uma linha no formato CSV.
     * @param func O objeto Funcionario a ser convertido.
     * @return Uma string formatada como uma linha CSV.
     */
    public static String toCsvLine(Funcionario func) {
        String tipo = (func instanceof Motorista) ? "Motorista" : "Funcionario";
        String cnh = (func instanceof Motorista) ? ((Motorista) func).getCNH() : "N/A";
        String ultimaEntradaStr = func.getUltimaEntrada() != null ? func.getUltimaEntrada().toString() : "null";
        String ultimaSaidaStr = func.getUltimaSaida() != null ? func.getUltimaSaida().toString() : "null";

        return String.format(Locale.US, "\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                tipo, func.getMatricula(), func.getNome(), func.getCpf(), func.getIdade(),
                func.getTelefone(), func.getEndereco(), func.getEmail(), func.getCargo(),
                func.getSalario(), cnh, ultimaEntradaStr, ultimaSaidaStr,
                func.getSenha(), func.getPerfil().name());
    }

    /**
     * Converte uma linha CSV para um objeto Funcionario ou Motorista.
     * @param linha A string da linha CSV.
     * @return Um novo objeto Funcionario ou Motorista.
     * @throws IllegalArgumentException se a linha for inválida.
     */
    public static Funcionario fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length == 15) {
            String senha = dados[13];
            PerfilUsuario perfil = PerfilUsuario.valueOf(dados[14]);

            Funcionario funcionario;
            if ("Motorista".equals(dados[0])) {
                funcionario = new Motorista(dados[10], null, dados[8], Double.parseDouble(dados[9]), dados[2], Integer.parseInt(dados[4]), dados[3], dados[5], dados[6], dados[7], dados[1], senha, perfil);
            } else {
                funcionario = new Funcionario(dados[8], Double.parseDouble(dados[9]), dados[2], Integer.parseInt(dados[4]), dados[3], dados[5], dados[6], dados[7], dados[1], senha, perfil);
            }

            // Carrega os dados de ponto, se existirem
            if (!"null".equals(dados[11])) {
                funcionario.setUltimaEntrada(LocalDateTime.parse(dados[11]));
            }
            if (!"null".equals(dados[12])) {
                funcionario.setUltimaSaida(LocalDateTime.parse(dados[12]));
            }

            return funcionario;
        }
        throw new IllegalArgumentException("Linha CSV inválida para Funcionario: " + linha);
    }
}