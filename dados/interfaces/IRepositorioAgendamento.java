package dados.interfaces;

import negocio.Agendamento;
import java.util.ArrayList;

public interface IRepositorioAgendamento {
    void cadastrar(Agendamento agendamento);
    Agendamento buscarPorPedido(int numeroPedido);
    ArrayList<Agendamento> listarTodos();
    boolean atualizar(Agendamento agendamento);
    boolean remover(int numeroPedido);
}
