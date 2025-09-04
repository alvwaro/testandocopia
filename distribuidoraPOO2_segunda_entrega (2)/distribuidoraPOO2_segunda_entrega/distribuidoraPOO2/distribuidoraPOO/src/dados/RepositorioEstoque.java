package dados;

import negocio.Estoque;
import negocio.Produto;
import java.io.*;
import java.util.ArrayList;

public class RepositorioEstoque {
    private ArrayList<Produto> produtos = new ArrayList<>();
    private static final String ARQUIVO_CSV = "produtos.csv";

    public RepositorioEstoque() {
        carregar(); // Carrega os produtos do CSV na inicialização
    }

    public boolean cadastrarProduto(Produto produto, Estoque estoque) {
        if (this.produtos.add(produto)) {
            estoque.cadastrarProduto(produto);
            salvar();
            return true;
        }
        return false;
    }

    public Produto buscarPorCodigo(String codigo) {
        for (Produto p : this.produtos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Produto> listarTodos() {
        return new ArrayList<>(this.produtos);
    }

    public boolean remover(String codigo) {
        Produto p = this.buscarPorCodigo(codigo);
        if (p != null) {
            this.produtos.remove(p);
            salvar();
            return true;
        }
        return false;
    }

    public boolean atualizar(Produto produto) {
        for (int i = 0; i < this.produtos.size(); i++) {
            if (this.produtos.get(i).getCodigo().equals(produto.getCodigo())) {
                this.produtos.set(i, produto);
                salvar();
                return true;
            }
        }
        return false;
    }

    // --- MÉTODOS DE PERSISTÊNCIA EM CSV ---

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("codigo,nome,descricao,preco,quantidade\n");
            for (Produto produto : produtos) {
                String linha = String.format("\"%s\",\"%s\",\"%s\",%.2f,%d",
                        produto.getCodigo(),
                        produto.getNome(),
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getQuantidade()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.produtos.clear();
            reader.readLine();

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 5) {
                    String codigo = dados[0];
                    String nome = dados[1];
                    String descricao = dados[2];
                    double preco = Double.parseDouble(dados[3].replace(",", ".")); // Lida com formato de moeda
                    int quantidade = Integer.parseInt(dados[4]);

                    this.produtos.add(new Produto(codigo, nome, descricao, preco, quantidade));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar produtos do arquivo CSV: " + e.getMessage());
        }
    }
}