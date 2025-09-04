package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Patio;

import java.util.Scanner;

public class TelaPrincipal {

    public static void main(String[] args) {
        DistribuidoraFachada fachada = new DistribuidoraFachada();
        Estoque estoque = new Estoque();
        Patio patio = new Patio(20); // Pátio com 20 vagas

        TelaCadastroCliente telaCadastroCliente = new TelaCadastroCliente(fachada);
        TelaCadastroFuncionario telaCadastroFuncionario = new TelaCadastroFuncionario(fachada);
        TelaCadastroProduto telaCadastroProduto = new TelaCadastroProduto(fachada, estoque);
        TelaControlePatio telaControlePatio = new TelaControlePatio(fachada, patio);
        TelaAgendamento telaAgendamento = new TelaAgendamento(fachada);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n== Menu Principal ==");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Funcionário");
            System.out.println("3. Cadastrar Produto");
            System.out.println("4. Controle de Pátio");
            System.out.println("5. Agendar Entrega");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    telaCadastroCliente.cadastrarCliente();
                    break;
                case 2:
                    telaCadastroFuncionario.cadastrarFuncionario();
                    break;
                case 3:
                    telaCadastroProduto.cadastrarProduto();
                    break;
                case 4:
                    telaControlePatio.controlarPatio();
                    break;
                case 5:
                    telaAgendamento.agendarEntrega();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}