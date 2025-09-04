package dados;

import negocio.Produto;
import java.io.*;
import java.util.ArrayList;

public class RepositorioEstoque {

    private static final String ARQUIVO_CSV = "produtos.csv";

    public RepositorioEstoque() {
        // O construtor agora está vazio. O carregamento é feito na fachada.
    }

    public void salvar(ArrayList<Produto> produtos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("codigo,nome,descricao,preco,quantidade,cadastrado\n");
            for (Produto produto : produtos) {
                String linha = String.format("\"%s\",\"%s\",\"%s\",%.2f,%d,%b",
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
            System.err.println("Erro ao salvar produtos no arquivo CSV: " + e.getMessage());
        }
    }

    public ArrayList<Produto> carregar() {
        ArrayList<Produto> produtosCarregados = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) {
            return produtosCarregados; // Retorna lista vazia se o arquivo não existe
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            reader.readLine(); // Pula o cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 6) {
                    String codigo = dados[0];
                    String nome = dados[1];
                    String descricao = dados[2];
                    double preco = Double.parseDouble(dados[3].replace(",", "."));
                    int quantidade = Integer.parseInt(dados[4]);
                    boolean cadastrado = Boolean.parseBoolean(dados[5]);

                    Produto produto = new Produto(codigo, nome, descricao, preco, quantidade);
                    produto.setCadastrado(cadastrado);
                    produtosCarregados.add(produto);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar produtos do arquivo CSV: " + e.getMessage());
        }
        return produtosCarregados;
    }
}