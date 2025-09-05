package ui;

import fachada.DistribuidoraFachada;
import negocio.Cliente;
import negocio.Estoque;
import negocio.Pedido;
import negocio.Produto;
import negocio.exceptions.ClienteNaoExisteException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaVendas {
    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaVendas(DistribuidoraFachada fachada, Estoque estoque) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuVendas() {
        int opcao;
        do {
            System.out.println("\n--- Vendas e Pedidos ---");
            System.out.println("1. Criar Novo Pedido");
            System.out.println("2. Realizar Pagamento de um Pedido");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        realizarPedido();
                        break;
                    case 2:
                        realizarPagamento();
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

    private void realizarPedido() {
        try {
            System.out.print("\nDigite o CPF do Cliente: ");
            String cpf = scanner.nextLine();
            Cliente cliente = fachada.buscarClientePorCpf(cpf);

            if (cliente == null) {
                throw new ClienteNaoExisteException("Operação cancelada. Cliente com CPF " + cpf + " não encontrado.");
            }

            System.out.println("Iniciando pedido para o cliente: " + cliente.getNome());
            ArrayList<Produto> produtosDesejados = new ArrayList<>();

            while (true) {
                System.out.print("Digite o código do produto para adicionar (ou 'fim' para terminar): ");
                String codigoProduto = scanner.nextLine();

                if (codigoProduto.equalsIgnoreCase("fim")) {
                    break;
                }

                Produto produtoEmEstoque = fachada.buscarProdutoPorCodigo(codigoProduto);

                if (produtoEmEstoque == null) {
                    System.out.println("ALERTA: Produto com código '" + codigoProduto + "' não foi encontrado. Tente novamente.");
                    continue;
                }

                System.out.print("Produto encontrado: " + produtoEmEstoque.getNome() + ". Digite a quantidade desejada: ");
                int quantidade = scanner.nextInt();
                scanner.nextLine();

                produtosDesejados.add(new Produto(
                        produtoEmEstoque.getCodigo(),
                        produtoEmEstoque.getNome(),
                        produtoEmEstoque.getDescricao(),
                        produtoEmEstoque.getPreco(),
                        quantidade
                ));
                System.out.println(">> " + quantidade + "x " + produtoEmEstoque.getNome() + " adicionado ao pedido.");
            }

            if (produtosDesejados.isEmpty()) {
                System.out.println("Nenhum produto foi adicionado. Pedido cancelado.");
                return;
            }

            fachada.criarPedido(cliente, produtosDesejados);

        } catch (InputMismatchException e) {
            System.out.println("\nERRO DE ENTRADA: A quantidade deve ser um número inteiro. Tente novamente.");
            scanner.nextLine();
        } catch (ClienteNaoExisteException e) {
            System.out.println("\nERRO DE VALIDAÇÃO: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\nERRO NOS DADOS DO PEDIDO: " + e.getMessage());
        } catch (RuntimeException e) { // Captura outros erros de execução, como estoque insuficiente
            System.out.println("\nERRO NA LÓGICA DO PEDIDO: " + e.getMessage());
        } catch (Exception e) { // Captura final para qualquer outro erro inesperado
            System.out.println("\nOcorreu um erro inesperado no sistema: " + e.getMessage());
        }
    }

    private void realizarPagamento() {
        try {
            System.out.print("\nDigite o CPF do Cliente para realizar o pagamento: ");
            String cpf = scanner.nextLine();
            Cliente cliente = fachada.buscarClientePorCpf(cpf);

            if (cliente == null || cliente.getPedidos().isEmpty()) {
                throw new ClienteNaoExisteException("Cliente não encontrado ou não possui pedidos pendentes.");
            }

            System.out.println("\nPedidos para o cliente " + cliente.getNome() + ":");
            boolean temPendentes = false;
            for (Pedido p : cliente.getPedidos()) {
                if (!"Pago".equalsIgnoreCase(p.getStatus())) {
                    System.out.printf(" -> Pedido Nº%d | Valor: R$ %.2f | Status: %s%n",
                            p.getNumero(), p.getValorTotal(), p.getStatus());
                    temPendentes = true;
                }
            }

            if (!temPendentes) {
                System.out.println("Este cliente não possui pedidos pendentes de pagamento.");
                return;
            }

            System.out.print("\nDigite o número do pedido que deseja pagar: ");
            int numPedido = scanner.nextInt();
            scanner.nextLine();

            Pedido pedidoAPagar = cliente.getPedidos().stream()
                    .filter(p -> p.getNumero() == numPedido)
                    .findFirst()
                    .orElse(null);

            if (pedidoAPagar == null) {
                System.out.println("Erro: Pedido com número " + numPedido + " não encontrado para este cliente.");
                return;
            }

            System.out.printf("O total do pedido é R$ %.2f. Digite o valor a ser pago: ", pedidoAPagar.getValorTotal());
            double valorPago = scanner.nextDouble();
            scanner.nextLine();

            fachada.pagarPedido(cliente, pedidoAPagar, valorPago);
            System.out.println("Pagamento do pedido Nº" + numPedido + " realizado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro de entrada: O número do pedido e o valor devem ser numéricos.");
            scanner.nextLine();
        } catch (ClienteNaoExisteException e) {
            System.out.println("\nERRO: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) { // Estes podem ficar juntos, pois não são subclasses um do outro
            System.out.println("\nErro ao processar o pagamento: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro inesperado: " + e.getMessage());
        }
    }
}