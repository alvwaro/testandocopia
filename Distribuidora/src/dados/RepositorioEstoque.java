package dados;

import negocio.Produto;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale; // Importar Locale

public class RepositorioEstoque {

    private static final String ARQUIVO_CSV = "produtos.csv";

    public RepositorioEstoque() { //construtor usado na fachada
    }

    public void salvar(ArrayList<Produto> produtos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("codigo,nome,descricao,preco,quantidade,cadastrado\n");
            for (Produto produto : produtos) {
                //Locale.US garente o ponto como separador decimal no preço
                String linha = String.format(Locale.US, "\"%s\",\"%s\",\"%s\",%.2f,%d,%b",
                        produto.getCodigo(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getQuantidade(),
                        produto.isCadastrado()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO SALVAR PRODUTOS: " + e.getMessage());
        }
    }

    public ArrayList<Produto> carregar() {
        ArrayList<Produto> produtosCarregados = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) {
            return produtosCarregados;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            reader.readLine(); // Pula a linha do cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                // Regex ignora vírgulas dentro de aspas.
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
                    // A leitura agora espera um ponto, que é garantido pelo método salvar()
                    double preco = Double.parseDouble(dados[3]);
                    int quantidade = Integer.parseInt(dados[4]);
                    boolean cadastrado = Boolean.parseBoolean(dados[5]);

                    Produto produto = new Produto(codigo, nome, descricao, preco, quantidade);
                    produto.setCadastrado(cadastrado);
                    produtosCarregados.add(produto);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERRO CRÍTICO AO CARREGAR PRODUTOS: " + e.getMessage());
        }
        return produtosCarregados;
    }
}
