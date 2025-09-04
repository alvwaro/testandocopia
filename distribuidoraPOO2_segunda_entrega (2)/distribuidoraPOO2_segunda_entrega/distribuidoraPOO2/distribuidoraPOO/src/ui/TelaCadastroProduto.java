package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Produto;

import java.util.Scanner;

public class TelaCadastroProduto {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;
    private final Estoque estoque;

    public TelaCadastroProduto(DistribuidoraFachada fachada, Estoque estoque) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.estoque = estoque;
    }

    public void cadastrarProduto() {
        System.out.println("== Cadastro de Produto ==");
        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        Produto produto = new Produto(codigo, nome, descricao, preco, quantidade);
        fachada.cadastrarProduto(produto, estoque);
        System.out.println("Produto cadastrado com sucesso!");
    }
}
