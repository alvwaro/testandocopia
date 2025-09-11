package ui;

import fachada.DistribuidoraFachada;
import negocio.Cliente;
import negocio.Pedido;
import negocio.Produto;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TelaRelatorios {
    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaRelatorios(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuRelatorios() {
        int opcao;
        do {
            System.out.println("\n--- Menu de Relatórios ---");
            System.out.println("1. Vendas do Dia");
            System.out.println("2. Vendas da Semana");
            System.out.println("3. Vendas do Mês");
            System.out.println("4. Produtos Mais Vendidos");
            System.out.println("5. Clientes que Mais Compram");
            System.out.println("6. Produtos com Estoque Baixo");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1: exibirVendasPorPeriodo("hoje"); break;
                    case 2: exibirVendasPorPeriodo("semana"); break;
                    case 3: exibirVendasPorPeriodo("mes"); break;
                    case 4: exibirProdutosMaisVendidos(); break;
                    case 5: exibirClientesQueMaisCompram(); break;
                    case 6: exibirEstoqueBaixo(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void exibirVendasPorPeriodo(String periodo) {
        List<Pedido> pedidos;
        String titulo = "Relatório de Vendas - ";
        if ("hoje".equals(periodo)) {
            pedidos = fachada.gerarRelatorioVendasHoje();
            titulo += "Hoje";
        } else if ("semana".equals(periodo)) {
            pedidos = fachada.gerarRelatorioVendasSemana();
            titulo += "Esta Semana";
        } else {
            pedidos = fachada.gerarRelatorioVendasMes();
            titulo += "Este Mês";
        }

        System.out.println("\n" + titulo);
        System.out.println("==========================================");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhuma venda registrada no período.");
        } else {
            double total = 0;
            for (Pedido p : pedidos) {
                System.out.printf("Pedido Nº: %d | Data: %s | Valor: R$ %.2f\n",
                        p.getNumero(), p.getDataPagamento().toLocalDate(), p.getValorTotal());
                total += p.getValorTotal();
            }
            System.out.println("------------------------------------------");
            System.out.printf("TOTAL VENDIDO NO PERÍODO: R$ %.2f\n", total);
        }
        System.out.println("==========================================");
    }

    private void exibirProdutosMaisVendidos() {
        Map<String, Integer> relatorio = fachada.gerarRelatorioProdutosMaisVendidos();
        System.out.println("\nRelatório de Produtos Mais Vendidos");
        System.out.println("==========================================");
        if (relatorio.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            relatorio.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> System.out.printf("Produto: %-20s | Quantidade Vendida: %d\n", entry.getKey(), entry.getValue()));
        }
        System.out.println("==========================================");
    }

    private void exibirClientesQueMaisCompram() {
        Map<String, Double> relatorio = fachada.gerarRelatorioClientesQueMaisCompram();
        System.out.println("\nRelatório de Clientes que Mais Compram (por valor)");
        System.out.println("==========================================");
        if (relatorio.isEmpty()) {
            System.out.println("Nenhum cliente realizou compras.");
        } else {
            relatorio.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> System.out.printf("Cliente: %-20s | Total Gasto: R$ %.2f\n", entry.getKey(), entry.getValue()));
        }
        System.out.println("==========================================");
    }

    private void exibirEstoqueBaixo() {
        System.out.print("Exibir produtos com quantidade menor ou igual a: ");
        int limite = scanner.nextInt();
        scanner.nextLine();

        List<Produto> relatorio = fachada.gerarRelatorioEstoqueBaixo(limite);
        System.out.println("\nRelatório de Produtos com Estoque Baixo");
        System.out.println("==========================================");
        if (relatorio.isEmpty()) {
            System.out.println("Nenhum produto com estoque baixo encontrado.");
        } else {
            for (Produto p : relatorio) {
                System.out.printf("Produto: %-20s | Quantidade Restante: %d\n", p.getNome(), p.getQuantidade());
            }
        }
        System.out.println("==========================================");
    }
}
