package dados.interfaces;

import negocio.Caminhao;
import java.util.ArrayList;

public interface IRepositorioCaminhao {
    boolean cadastrar(Caminhao caminhao);
    Caminhao buscarPorPlaca(String placa);
    ArrayList<Caminhao> listarTodos();
    boolean remover(String placa);
}