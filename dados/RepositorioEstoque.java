package dados;

import dados.interfaces.IRepositorioEstoque;
import dados.mappers.ProdutoCsvMapper;
import negocio.Produto;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEstoque implements IRepositorioEstoque {
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "produtos.csv";

    @Override
    public void salvar(ArrayList<Produto> produtos) {
        List<String> linhas = new ArrayList<>();
        String cabecalho = "codigo,nome,descricao,preco,quantidade,cadastrado";

        for (Produto produto : produtos) {
            linhas.add(ProdutoCsvMapper.toCsvLine(produto));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }

    @Override
    public ArrayList<Produto> carregar() {
        ArrayList<Produto> produtosCarregados = new ArrayList<>();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                Produto produto = ProdutoCsvMapper.fromCsvLine(linha);
                produtosCarregados.add(produto);
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
        return produtosCarregados;
    }
}