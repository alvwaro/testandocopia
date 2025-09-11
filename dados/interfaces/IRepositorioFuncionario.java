package dados.interfaces;

import negocio.Funcionario;
import java.util.ArrayList;

public interface IRepositorioFuncionario {
    boolean cadastrar(Funcionario funcionario);
    Funcionario buscarPorMatricula(String matricula);
    ArrayList<Funcionario> listarTodos();
    boolean remover(String matricula);
    boolean atualizar(Funcionario funcionario);
}
