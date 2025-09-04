package ui;

import fachada.DistribuidoraFachada;
import negocio.Caminhao;
import negocio.Patio;

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
        }
    }

    private void registrarEntrada() {
        System.out.print("Placa do Caminhão: ");
        String placa = scanner.nextLine();
        Caminhao caminhao = new Caminhao(placa);
        try {
            fachada.permitirEntrada(caminhao, patio);
        } catch (Exception e) {
            System.out.println("Erro ao registrar entrada: " + e.getMessage());
        }
    }

    private void registrarSaida() {
        System.out.print("Placa do Caminhão: ");
        String placa = scanner.nextLine();
        Caminhao caminhao = new Caminhao(placa);
        fachada.adicionarFilaSaida(caminhao, patio);
        fachada.permirSaida(caminhao, patio);
    }
}