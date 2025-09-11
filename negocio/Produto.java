package negocio;

import java.io.Serializable;

public class Produto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private boolean cadastrado = false;

    public Produto(String codigo, String nome, String descricao, double preco, int quantidade) {
        if (codigo == null || !codigo.matches("\\d+")) {
            throw new IllegalArgumentException("Código inválido. O código do produto deve conter apenas números.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public boolean isCadastrado() {
        return cadastrado;
    }

    public void setCadastrado(boolean cadastrado) {
        this.cadastrado = cadastrado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco){
        if (preco < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }
        this.quantidade = quantidade;
    }
}