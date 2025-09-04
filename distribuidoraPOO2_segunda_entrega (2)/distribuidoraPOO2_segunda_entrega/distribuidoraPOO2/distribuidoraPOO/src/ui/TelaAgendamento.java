package ui;

import fachada.DistribuidoraFachada;
import negocio.Agendamento;
import negocio.Caminhao;
import negocio.Pedido;

import java.util.Date;
import java.util.Scanner;

public class TelaAgendamento {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaAgendamento(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void agendarEntrega() {
        System.out.println("== Agendar Entrega ==");
        System.out.print("Número do Pedido: ");
        int numeroPedido = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Placa do Caminhão: ");
        String placaCaminhao = scanner.nextLine();

        // Criar objetos Pedido e Caminhao (simplificado)
        Pedido pedido = new Pedido();
        pedido.setNumero(numeroPedido);

        Caminhao caminhao = new Caminhao(placaCaminhao);

        Agendamento agendamento = new Agendamento(pedido, caminhao, new Date());
        System.out.println("Agendamento realizado com sucesso!");
    }
}
