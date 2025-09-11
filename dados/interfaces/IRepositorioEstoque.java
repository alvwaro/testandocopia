package dados.interfaces;

import negocio.Produto;
import java.util.ArrayList;

public interface IRepositorioEstoque {
    // Define o contrato para persistência de produtos
    void salvar(ArrayList<Produto> produtos);
    ArrayList<Produto> carregar();
}
