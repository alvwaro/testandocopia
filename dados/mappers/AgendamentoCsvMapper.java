package dados.mappers;

import negocio.Agendamento;
import negocio.StatusAgendamento;
import negocio.Pedido; // Apenas para criar o objeto base

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgendamentoCsvMapper {

    // Formato de data consistente para salvar e carregar
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Converte um objeto Agendamento para uma linha no formato CSV.
     * Salva apenas os IDs das entidades relacionadas.
     * @param agendamento O objeto Agendamento a ser convertido.
     * @return Uma string formatada como uma linha CSV.
     */
    public static String toCsvLine(Agendamento agendamento) {
        String placa = (agendamento.getCaminhao() != null) ? agendamento.getCaminhao().getPlaca() : "N/A";
        String matricula = (agendamento.getMotorista() != null) ? agendamento.getMotorista().getMatricula() : "N/A";

        return String.format("%d,\"%s\",\"%s\",\"%s\",%s",
                agendamento.getPedido().getNumero(),
                placa,
                matricula,
                DATE_FORMAT.format(agendamento.getDataHoraPrevista()),
                agendamento.getStatus().name());
    }

    /**
     * Converte uma linha CSV para um objeto Agendamento.
     * ATENÇÃO: Este método cria um Agendamento com objetos Pedido, Caminhao e
     * Motorista incompletos (apenas com seus IDs). A Fachada deve ser
     * responsável por carregar os objetos completos usando esses IDs.
     * @param linha A string da linha CSV.
     * @return Um novo objeto Agendamento com dados parciais.
     * @throws IllegalArgumentException se a linha for inválida.
     * @throws RuntimeException para erro de parse de data.
     */
    public static Agendamento fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length == 5) {
            try {
                int numeroPedido = Integer.parseInt(dados[0]);
                Date dataPrevista = DATE_FORMAT.parse(dados[3]);
                StatusAgendamento status = StatusAgendamento.valueOf(dados[4]);

                // Cria um objeto Pedido "dummy" apenas com o número
                Pedido pedidoBase = new Pedido();
                pedidoBase.setNumero(numeroPedido);

                Agendamento agendamento = new Agendamento(pedidoBase, dataPrevista);
                agendamento.setStatus(status);

                // A Fachada precisará usar os IDs dados[1] e dados[2] para
                // buscar e setar os objetos Caminhao e Motorista completos.

                return agendamento;

            } catch (ParseException e) {
                throw new RuntimeException("Erro ao parsear data na linha CSV de Agendamento: " + linha, e);
            }
        }
        throw new IllegalArgumentException("Linha CSV inválida para Agendamento: " + linha);
    }
}