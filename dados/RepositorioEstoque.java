package dados;

import dados.interfaces.IRepositorioEstoque;
import negocio.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RepositorioEstoque implements IRepositorioEstoque {
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "produtos.csv";

    @Override
    public void salvar(ArrayList<Produto> produtos) {
        List<String> linhas = new ArrayList<>();
        linhas.add("codigo,nome,descricao,preco,quantidade,cadastrado");

        for (Produto produto : produtos) {
            String linha = String.format(Locale.US, "\"%s\",\"%s\",\"%s\",%.2f,%d,%b",
                    produto.getCodigo(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.isCadastrado()
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    @Override
    public ArrayList<Produto> carregar() {
        ArrayList<Produto> produtosCarregados = new ArrayList<>();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 6) {
                    Produto produto = new Produto(dados[0], dados[1], dados[2], Double.parseDouble(dados[3]), Integer.parseInt(dados[4]));
                    produto.setCadastrado(Boolean.parseBoolean(dados[5]));
                    produtosCarregados.add(produto);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
        return produtosCarregados;
    }
}