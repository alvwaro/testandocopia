package dados;

import negocio.Produto;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RepositorioEstoque {
    // A lista de produtos agora será gerenciada pela classe Estoque em memória.
    // Este repositório servirá apenas como a camada de persistência.
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "produtos.csv";

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
                    String codigo = dados[0];
                    String nome = dados[1];
                    String descricao = dados[2];
                    double preco = Double.parseDouble(dados[3]);
                    int quantidade = Integer.parseInt(dados[4]);
                    boolean cadastrado = Boolean.parseBoolean(dados[5]);

                    Produto produto = new Produto(codigo, nome, descricao, preco, quantidade);
                    produto.setCadastrado(cadastrado);
                    produtosCarregados.add(produto);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
        return produtosCarregados;
    }
}