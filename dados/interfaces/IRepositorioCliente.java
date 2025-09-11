package dados.interfaces;

import negocio.Cliente;
import java.util.ArrayList;

public interface IRepositorioCliente {
    boolean cadastrar(Cliente cliente);
    Cliente buscarPorCpf(String cpf);
    ArrayList<Cliente> listarTodos();
    boolean remover(String cpf);
    boolean atualizar(Cliente cliente);
}