package ui;

import fachada.DistribuidoraFachada;
import negocio.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaAgendamento {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;
    private final Patio patio;

    public TelaAgendamento(DistribuidoraFachada fachada, Patio patio) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.patio = patio;
    }

    public void exibirMenuAgendamento() {
        int opcao = -1; // CORREÇÃO AQUI: Inicialize a variável
        do {
            System.out.println("\n--- Gestão de Entregas e Agendamentos ---");
            System.out.println("1. Criar Agendamento para um Pedido Pago");
            System.out.println("2. Listar Todos os Agendamentos");
            System.out.println("3. Confirmar Agendamento (Vincular Caminhão e Motorista)");
            System.out.println("4. Iniciar Entrega (Saída do Pátio)");
            System.out.println("5. Finalizar Entrega (Retorno ao Pátio)");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1: criarAgendamento(); break;
                    case 2: listarAgendamentos(); break;
                    case 3: confirmarAgendamento(); break;
                    case 4: iniciarEntrega(); break;
                    case 5: finalizarEntrega(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            } catch (Exception e) {
                System.out.println("\nOcorreu um erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void criarAgendamento() {
        System.out.print("Digite o número do pedido PAGO para criar o agendamento: ");
        int numPedido = scanner.nextInt();
        scanner.nextLine();

        Pedido p = fachada.buscarPedidoPorNumero(numPedido);
        if (p == null) {
            System.out.println("Erro: Pedido não encontrado.");
            return;
        }
        fachada.criarAgendamento(p);
    }

    private void listarAgendamentos() {
        System.out.println("\n--- Lista de Agendamentos ---");
        ArrayList<Agendamento> agendamentos = fachada.listarTodosAgendamentos();
        if (agendamentos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
        } else {
            for (Agendamento a : agendamentos) {
                System.out.println("---------------------------------");
                System.out.println("Pedido Nº: " + a.getPedido().getNumero());
                System.out.println("Status: " + a.getStatus());
                System.out.println("Data Prevista: " + a.getDataHoraPrevista());
                if(a.getCaminhao() != null) {
                    System.out.println("Caminhão: " + a.getCaminhao().getPlaca());
                }
                if(a.getMotorista() != null) {
                    System.out.println("Motorista: " + a.getMotorista().getNome());
                }
            }
            System.out.println("---------------------------------");
        }
    }

    private void confirmarAgendamento() {
        System.out.print("Digite o número do pedido do agendamento a confirmar: ");
        int numPedido = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite a placa do caminhão: ");
        String placa = scanner.nextLine();

        System.out.print("Digite a matrícula do motorista: ");
        String matricula = scanner.nextLine();

        fachada.confirmarAgendamento(numPedido, placa, matricula);
    }

    private void iniciarEntrega() {
        System.out.print("Digite o número do pedido para iniciar a entrega: ");
        int numPedido = scanner.nextInt();
        scanner.nextLine();
        fachada.iniciarEntrega(numPedido, this.patio);
    }

    private void finalizarEntrega() {
        System.out.print("Digite o número do pedido para finalizar a entrega: ");
        int numPedido = scanner.nextInt();
        scanner.nextLine();
        fachada.finalizarEntrega(numPedido, this.patio);
    }
}