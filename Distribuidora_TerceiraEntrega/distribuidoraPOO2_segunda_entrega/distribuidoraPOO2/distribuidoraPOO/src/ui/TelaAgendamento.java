package ui;

import fachada.DistribuidoraFachada;
import negocio.Agendamento;
import negocio.Caminhao;
import negocio.Pedido;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaAgendamento {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaAgendamento(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void agendarEntrega() {
        try {
            System.out.println("== Agendar Entrega ==");
            System.out.print("Número do Pedido: ");
            int numeroPedido = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Placa do Caminhão: ");
            String placaCaminhao = scanner.nextLine();

            Pedido pedido = new Pedido();
            pedido.setNumero(numeroPedido);

            Caminhao caminhao = new Caminhao(placaCaminhao);

            Agendamento agendamento = new Agendamento(pedido, caminhao, new Date());

            System.out.println("Agendamento realizado com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Erro: Por favor, insira um número válido para o pedido.");
            scanner.nextLine(); // Limpa o buffer do scanner
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao agendar a entrega: " + e.getMessage());
        }
    }
}