package dados.mappers;

import negocio.Agendamento;
import negocio.StatusAgendamento;
import negocio.Pedido; // Apenas para criar o objeto base

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgendamentoCsvMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

    public static String getPlacaFromLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (dados.length == 5) {
            String placa = dados[1].replace("\"", "");
            return "N/A".equals(placa) ? null : placa;
        }
        return null;
    }

    public static String getMatriculaFromLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (dados.length == 5) {
            String matricula = dados[2].replace("\"", "");
            return "N/A".equals(matricula) ? null : matricula;
        }
        return null;
    }
}