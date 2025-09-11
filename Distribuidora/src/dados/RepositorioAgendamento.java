package dados;

import negocio.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "agendamentos.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public RepositorioAgendamento() {
        carregar();
    }

    public void cadastrar(Agendamento agendamento) {
        this.agendamentos.add(agendamento);
        System.out.println("Agendamento cadastrado para pedido: " + agendamento.getPedido().getNumero());
        salvar();
    }

    public Agendamento buscarPorPedido(int numeroPedido) {
        for(Agendamento a : this.agendamentos) {
            if (a.getPedido().getNumero() == numeroPedido) {
                return a;
            }
        }
        return null;
    }

    public ArrayList<Agendamento> listarTodos() {
        return new ArrayList<>(this.agendamentos);
    }

    public boolean atualizar(Agendamento agendamento) {
        for(int i = 0; i < this.agendamentos.size(); ++i) {
            if (this.agendamentos.get(i).getPedido().getNumero() == agendamento.getPedido().getNumero()) {
                this.agendamentos.set(i, agendamento);
                salvar();
                return true;
            }
        }
        return false;
    }

    public boolean remover(int numeroPedido) {
        Agendamento a = this.buscarPorPedido(numeroPedido);
        if (a != null) {
            this.agendamentos.remove(a);
            salvar();
            return true;
        } else {
            return false;
        }
    }


    private void salvar() {
        List<String> linhas = new ArrayList<>();
        linhas.add("numeroPedido,placaCaminhao,dataHoraPrevista,status");

        for (Agendamento agendamento : agendamentos) {
            String linha = String.format("%d,\"%s\",\"%s\",%s",
                    agendamento.getPedido().getNumero(),
                    agendamento.getCaminhao().getPlaca(),
                    DATE_FORMAT.format(agendamento.getDataHoraPrevista()),
                    agendamento.getStatus().name()
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    private void carregar() {
        this.agendamentos.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 4) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    String placaCaminhao = dados[1];
                    Date dataHoraPrevista = DATE_FORMAT.parse(dados[2]);
                    StatusAgendamento status = StatusAgendamento.valueOf(dados[3]);

                    Pedido pedido = new Pedido();
                    pedido.setNumero(numeroPedido);
                    Caminhao caminhao = new Caminhao(placaCaminhao);

                    Agendamento agendamento = new Agendamento(pedido, caminhao, dataHoraPrevista);
                    agendamento.setStatus(status);
                    this.agendamentos.add(agendamento);
                }
            } catch (ParseException | NumberFormatException e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}