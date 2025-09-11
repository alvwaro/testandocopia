package dados.interfaces;

import negocio.Caminhao;
import java.util.ArrayList;

public interface IRepositorioPatio {
    boolean cadastrarCaminhaoPatio(Caminhao caminhao);
    Caminhao buscarPorPlaca(String placa);
    ArrayList<Caminhao> getCaminhoesPatio();
    void listarTodos();
}
