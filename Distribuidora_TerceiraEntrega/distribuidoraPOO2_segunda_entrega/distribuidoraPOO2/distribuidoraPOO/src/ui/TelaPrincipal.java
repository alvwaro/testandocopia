package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Patio;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaPrincipal {

    public static void main(String[] args) {
        DistribuidoraFachada fachada = new DistribuidoraFachada();
        Estoque estoque = new Estoque();
        Patio patio = new Patio(20);

        TelaCliente telaCliente = new TelaCliente(fachada);
        TelaFuncionario telaFuncionario = new TelaFuncionario(fachada);
        TelaEstoque telaEstoque = new TelaEstoque(fachada, estoque);
        TelaControlePatio telaControlePatio = new TelaControlePatio(fachada, patio);
        TelaVendas telaVendas = new TelaVendas(fachada, estoque);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n===== MENU PRINCIPAL DA DISTRIBUIDORA =====");
                System.out.println("1. Gestão de Clientes");
                System.out.println("2. Gestão de Funcionários");
                System.out.println("3. Gestão de Estoque e Produtos");
                System.out.println("4. Controle de Pátio");
                System.out.println("5. Vendas e Pedidos");
                System.out.println("0. Sair do Sistema");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        telaCliente.exibirMenuCliente();
                        break;
                    case 2:
                        telaFuncionario.exibirMenuFuncionario();
                        break;
                    case 3:
                        telaEstoque.exibirMenuEstoque();
                        break;
                    case 4:
                        telaControlePatio.exibirMenuPatio();
                        break;
                    case 5:
                        telaVendas.exibirMenuVendas();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Opção inválida. Por favor, insira um número correspondente ao menu.");
                scanner.nextLine();
            }
        }
    }
}
