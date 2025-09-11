package negocio;

import negocio.exceptions.EstoqueInsuficienteException;
import negocio.exceptions.ProdutoNaoEncontradoException;
import java.io.Serializable;
import java.util.ArrayList;

public class Estoque implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Produto> produtos;

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public void cadastrarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (consultarProduto(produto.getCodigo()) == null) {
            this.produtos.add(produto);
        } else {
        }
    }

    public void removerProduto(Produto produto) {
        if (produto != null) {
            this.produtos.remove(produto);
        }
    }

    public Produto consultarProduto(String codigo) {
        for (Produto p : this.produtos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Produto> listarProdutos() {
        return new ArrayList<>(this.produtos); // Retorna uma cópia da lista
    }

    public void darBaixaEstoque(ArrayList<Produto> produtosDoPedido) {
        if (produtosDoPedido == null || produtosDoPedido.isEmpty()) {
            throw new IllegalArgumentException("A lista de produtos do pedido não pode ser vazia.");
        }

        for (Produto produtoPedido : produtosDoPedido) {
            Produto produtoEstoque = consultarProduto(produtoPedido.getCodigo());

            if (produtoEstoque == null) {
                throw new ProdutoNaoEncontradoException("Produto " + produtoPedido.getNome() + " (cód: " + produtoPedido.getCodigo() + ") não encontrado no estoque.");
            }

            if (produtoEstoque.getQuantidade() < produtoPedido.getQuantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + produtoPedido.getNome() + ". Disponível: " + produtoEstoque.getQuantidade() + ", Pedido: " + produtoPedido.getQuantidade());
            }

            produtoEstoque.setQuantidade(produtoEstoque.getQuantidade() - produtoPedido.getQuantidade());
        }
    }
}