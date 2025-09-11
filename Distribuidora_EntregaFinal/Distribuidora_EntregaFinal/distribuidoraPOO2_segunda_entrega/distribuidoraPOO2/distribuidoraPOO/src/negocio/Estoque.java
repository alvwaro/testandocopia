package negocio;

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

    public void atualizarEstoquePedido(Pedido pedido) {
        if (pedido == null || pedido.getProdutos() == null) {
            throw new IllegalArgumentException("Pedido ou lista de produtos inválidos.");
        }
        if (!"Pago".equalsIgnoreCase(pedido.getStatus())) {
            throw new RuntimeException("Pedido não pode ser processado: ainda não foi pago.");
        }

        for (Produto produtoPedido : pedido.getProdutos()) {
            Produto estoqueProduto = consultarProduto(produtoPedido.getCodigo());
            if (estoqueProduto == null) {
                throw new ProdutoNaoEncontradoException("Produto " + produtoPedido.getNome() + " não encontrado no estoque.");
            }

            if (estoqueProduto.getQuantidade() < produtoPedido.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produtoPedido.getNome());
            }
            estoqueProduto.setQuantidade(estoqueProduto.getQuantidade() - produtoPedido.getQuantidade());
        }
    }
}