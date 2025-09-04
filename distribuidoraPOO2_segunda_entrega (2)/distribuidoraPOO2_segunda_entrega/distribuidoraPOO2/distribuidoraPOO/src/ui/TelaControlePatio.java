package ui;

import fachada.DistribuidoraFachada;
import negocio.Caminhao;
import negocio.Patio;
import negocio.exceptions.CaminhaoNaoCadastradoException;
import negocio.exceptions.VagaInsuficienteException;

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

    public void controlarPatio() {
        while (true) {
            try {
                System.out.println("\n== Controle do Pátio ==");
                System.out.println("1. Registrar Entrada de Caminhão");
                System.out.println("2. Registrar Saída de Caminhão");
                System.out.println("3. Listar Caminhões no Pátio");
                System.out.println("0. Voltar");
                System.out.print("Escolha uma opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        registrarEntrada();
                        break;
                    case 2:
                        registrarSaida();
                        break;
                    case 3:
                        fachada.listarCaminhoesPatio();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Opção inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }

    private void registrarEntrada() {
        try {
            System.out.print("Placa do Caminhão: ");
            String placa = scanner.nextLine();
            Caminhao caminhao = new Caminhao(placa);
            fachada.permitirEntrada(caminhao, patio);
        } catch (VagaInsuficienteException | CaminhaoNaoCadastradoException | IllegalArgumentException | SecurityException e) {
            System.out.println("Erro ao registrar entrada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    private void registrarSaida() {
        try {
            System.out.print("Placa do Caminhão: ");
            String placa = scanner.nextLine();
            Caminhao caminhao = new Caminhao(placa);
            fachada.adicionarFilaSaida(caminhao, patio);
            fachada.permirSaida(caminhao, patio);
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Erro ao registrar saída: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}