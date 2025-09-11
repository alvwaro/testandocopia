package dados;

import dados.interfaces.IRepositorioAgendamento;
import dados.mappers.AgendamentoCsvMapper;
import negocio.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAgendamento implements IRepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "agendamentos.csv";

    public RepositorioAgendamento() {
        // O carregamento agora é feito pela fachada, que tem acesso aos outros repositórios.
    }

    // Método para a fachada adicionar os agendamentos carregados e montados
    public void adicionarAgendamentoCarregado(Agendamento agendamento) {
        if (agendamento != null) {
            this.agendamentos.add(agendamento);
        }
    }

    // Método para a fachada ler as linhas brutas do arquivo CSV
    public List<String> carregarLinhas() {
        return persistencia.carregar(NOME_ARQUIVO);
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

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        String cabecalho = "numeroPedido,placaCaminhao,matriculaMotorista,dataHoraPrevista,status";

        for (Agendamento agendamento : agendamentos) {
            linhas.add(AgendamentoCsvMapper.toCsvLine(agendamento));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }
}