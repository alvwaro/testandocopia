package ui;

import fachada.DistribuidoraFachada;
import negocio.Caminhao;
import negocio.Funcionario;
import negocio.Patio;
import negocio.exceptions.CaminhaoNaoCadastradoException;
import negocio.exceptions.VagaInsuficienteException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaControlePatio {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;
    private final Patio patio;

    public TelaControlePatio(DistribuidoraFachada fachada, Patio patio) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
        this.patio = patio;
    }

    public void exibirMenuPatio(Funcionario usuarioLogado) {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Pátio e Caminhões ---");
            System.out.println("1. Cadastrar Novo Caminhão no Sistema");
            System.out.println("2. Registrar Entrada de Caminhão no Pátio");
            System.out.println("3. Registrar Saída de Caminhão do Pátio");
            System.out.println("4. Listar Caminhões DENTRO do Pátio");
            System.out.println("5. Listar TODOS os Caminhões do Sistema");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarCaminhao(usuarioLogado);
                        break;
                    case 2:
                        registrarEntrada();
                        break;
                    case 3:
                        registrarSaida();
                        break;
                    case 4:
                        patio.listarCaminhoesNoPatio();
                        break;
                    case 5:
                        listarTodosCaminhoesSistema();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Opção inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void cadastrarCaminhao(Funcionario usuarioLogado) {
        try {
            System.out.println("\n== Cadastro de Caminhão ==");
            System.out.print("Placa: ");
            String placa = scanner.nextLine();
            System.out.print("Capacidade (em toneladas): ");
            int capacidade = scanner.nextInt();
            scanner.nextLine();

            Caminhao caminhao = new Caminhao(placa, capacidade, "Disponivel");
            fachada.cadastrarCaminhao(caminhao, usuarioLogado);
            System.out.println("Caminhão cadastrado com sucesso no sistema!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro: A capacidade deve ser um valor numérico.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar caminhão: " + e.getMessage());
        }
    }

    private void registrarEntrada() {
        try {
            System.out.print("Digite a Placa do caminhão para ENTRADA: ");
            String placa = scanner.nextLine();
            fachada.registrarEntradaPatio(placa, this.patio);
        } catch (VagaInsuficienteException | CaminhaoNaoCadastradoException | IllegalStateException e) {
            System.out.println("\nERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro inesperado: " + e.getMessage());
        }
    }

    private void registrarSaida() {
        try {
            System.out.print("Digite a Placa do caminhão para SAÍDA: ");
            String placa = scanner.nextLine();
            fachada.registrarSaidaPatio(placa, this.patio);
        } catch (CaminhaoNaoCadastradoException | IllegalStateException e) {
            System.out.println("\nERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro inesperado: " + e.getMessage());
        }
    }

    private void listarTodosCaminhoesSistema() {
        System.out.println("\n--- Todos os Caminhões Cadastrados no Sistema ---");
        ArrayList<Caminhao> todos = fachada.getTodosCaminhoes();
        if (todos.isEmpty()) {
            System.out.println("Nenhum caminhão cadastrado no sistema.");
        } else {
            for (Caminhao c : todos) {
                System.out.println(c.toString());
            }
        }
    }
}