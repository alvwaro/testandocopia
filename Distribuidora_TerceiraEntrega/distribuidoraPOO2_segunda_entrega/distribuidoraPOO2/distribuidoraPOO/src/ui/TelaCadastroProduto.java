package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Produto;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaCadastroProduto {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;
    // O campo 'estoque' foi removido pois agora é gerenciado pela fachada.

    public TelaCadastroProduto(DistribuidoraFachada fachada, Estoque estoque) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void cadastrarProduto() {
        try {
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

            fachada.cadastrarProduto(produto);

            System.out.println("Produto cadastrado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("Erro: Preço ou Quantidade inválida. Por favor, insira um número.");
            scanner.nextLine();
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}