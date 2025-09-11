package dados;

import dados.interfaces.IRepositorioAgendamento;
import negocio.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepositorioAgendamento implements IRepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "agendamentos.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public RepositorioAgendamento() {
        // carregar(); // Idealmente, o carregamento dependeria de outros repositórios,
        // então é melhor deixar a fachada orquestrar isso.
    }

    @Override
    public void cadastrar(Agendamento agendamento) {
        this.agendamentos.add(agendamento);
        System.out.println("Agendamento cadastrado para pedido: " + agendamento.getPedido().getNumero());
        salvar();
    }

    @Override
    public Agendamento buscarPorPedido(int numeroPedido) {
        for(Agendamento a : this.agendamentos) {
            if (a.getPedido().getNumero() == numeroPedido) {
                return a;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Agendamento> listarTodos() {
        return new ArrayList<>(this.agendamentos);
    }

    @Override
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

    @Override
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

    // O ideal seria que a camada de dados não acessasse outra camada (como a fachada)
    // para buscar objetos completos. Esta é uma simplificação.
    // O correto seria salvar apenas os IDs e a fachada ser responsável por "montar"
    // os objetos na hora de carregar.

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        // Adicionando matriculaMotorista
        linhas.add("numeroPedido,placaCaminhao,matriculaMotorista,dataHoraPrevista,status");

        for (Agendamento agendamento : agendamentos) {
            String placa = (agendamento.getCaminhao() != null) ? agendamento.getCaminhao().getPlaca() : "N/A";
            String matricula = (agendamento.getMotorista() != null) ? agendamento.getMotorista().getMatricula() : "N/A";

            String linha = String.format("%d,\"%s\",\"%s\",\"%s\",%s",
                    agendamento.getPedido().getNumero(),
                    placa,
                    matricula,
                    DATE_FORMAT.format(agendamento.getDataHoraPrevista()),
                    agendamento.getStatus().name()
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    // AVISO: O método carregar() aqui é uma simplificação.
    // Ele não reconstrói os objetos Pedido, Caminhao e Motorista completamente,
    // apenas usa seus IDs. Para as operações atuais, isso funciona, mas em um
    // sistema mais complexo, a Fachada precisaria orquestrar o carregamento.
    private void carregar() {
        // Esta funcionalidade precisaria ser bem mais robusta para um sistema real.
        // Por enquanto, vamos manter a lógica simplificada para não quebrar o que já existe.
    }
}