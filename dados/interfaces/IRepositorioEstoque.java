package dados.interfaces;

import negocio.Produto;
import java.util.ArrayList;

public interface IRepositorioEstoque {
    void salvar(ArrayList<Produto> produtos);
    ArrayList<Produto> carregar();
}
