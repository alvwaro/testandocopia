package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Produto;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaEstoque {
    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaEstoque(DistribuidoraFachada fachada, Estoque estoque) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuEstoque() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Estoque e Produtos ---");
            System.out.println("1. Cadastrar Novo Produto");
            System.out.println("2. Listar Todos os Produtos");
            System.out.println("3. Buscar Produto por Código");
            System.out.println("4. Atualizar Preço de um Produto");
            System.out.println("5. Remover Produto");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarProduto();
                        break;
                    case 2:
                        listarTodosOsProdutos(); // Chama o método corrigido
                        break;
                    case 3:
                        buscarProduto();
                        break;
                    case 4:
                        atualizarPreco();
                        break;
                    case 5:
                        removerProduto();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    // **** MÉTODO CORRIGIDO PARA LISTAR PRODUTOS ****
    private void listarTodosOsProdutos() {
        System.out.println("\n--- Lista Completa de Produtos no Estoque ---");
        ArrayList<Produto> produtos = fachada.listarProdutos(); // Busca a lista da fachada

        if (produtos == null || produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado no estoque.");
            return;
        }

        System.out.println("----------------------------------------------------------------");
        for (Produto p : produtos) {
            System.out.printf("Nome: %-20s | Código: %-10s | Preço: R$ %-8.2f | Qtd: %d%n",
                    p.getNome(), p.getCodigo(), p.getPreco(), p.getQuantidade());
        }
        System.out.println("----------------------------------------------------------------");
    }

    private void cadastrarProduto() {
        try {
            System.out.println("\n== Cadastro de Produto ==");
            System.out.print("Código: ");
            String codigo = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            System.out.print("Preço: ");
            double preco = scanner.nextDouble();
            System.out.print("Quantidade em estoque: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            Produto produto = new Produto(codigo, nome, descricao, preco, quantidade);
            fachada.cadastrarProduto(produto); // A fachada agora gerencia o estoque
            System.out.println("Produto cadastrado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro: Preço e quantidade devem ser valores numéricos.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar produto: " + e.getMessage());
        }
    }

    private void buscarProduto() {
        System.out.print("\nDigite o código do produto: ");
        String codigo = scanner.nextLine();
        Produto p = fachada.buscarProdutoPorCodigo(codigo);
        if (p != null) {
            System.out.println("Produto encontrado:");
            System.out.println("Nome: " + p.getNome() + " | Preço: R$" + String.format("%.2f", p.getPreco()) + " | Qtd: " + p.getQuantidade());
        } else {
            System.out.println("Produto com código " + codigo + " não encontrado.");
        }
    }

    private void removerProduto() {
        System.out.print("\nDigite o código do produto a ser removido: ");
        String codigo = scanner.nextLine();
        if (fachada.removerProduto(codigo)) {
            System.out.println("Produto removido com sucesso.");
        } else {
            System.out.println("Não foi possível remover. Produto não encontrado.");
        }
    }

    private void atualizarPreco() {
        try {
            System.out.print("\nDigite o código do produto para atualizar o preço: ");
            String codigo = scanner.nextLine();
            Produto p = fachada.buscarProdutoPorCodigo(codigo);

            if (p == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            System.out.print("Digite o novo preço para " + p.getNome() + ": ");
            double novoPreco = scanner.nextDouble();
            scanner.nextLine();

            fachada.atualizarPreco(p, novoPreco);
            System.out.println("Preço atualizado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro: O preço deve ser um valor numérico.");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro: " + e.getMessage());
        }
    }
}