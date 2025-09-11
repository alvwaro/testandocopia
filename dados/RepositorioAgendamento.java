package dados;

import dados.interfaces.IRepositorioAgendamento;
import dados.mappers.AgendamentoCsvMapper; // <-- Importado
import negocio.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAgendamento implements IRepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "agendamentos.csv";

    public RepositorioAgendamento() {
        // O carregamento (carregar()) é complexo aqui.
        // A melhor prática seria a Fachada orquestrar isso,
        // lendo os IDs do CSV e buscando os objetos completos
        // nos outros repositórios. Para simplificar, o carregamento foi omitido.
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
            // Usa o Mapper para a conversão
            linhas.add(AgendamentoCsvMapper.toCsvLine(agendamento));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }

    // AVISO: O método carregar() aqui é uma simplificação.
    // Ele não foi implementado porque exigiria acesso a outros repositórios,
    // quebrando o isolamento da camada de dados. A Fachada deve gerenciar isso.
    private void carregar() {
        // Em um sistema real, a Fachada leria o CSV e usaria os IDs para
        // buscar Pedido, Caminhao e Motorista de seus respectivos repositórios
        // para então reconstruir o objeto Agendamento completo.
    }
}