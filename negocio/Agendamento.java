package negocio;

import negocio.enums.StatusAgendamento;

import java.io.Serializable;
import java.util.Date;

public class Agendamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private Pedido pedido;
    private Caminhao caminhao;
    private Motorista motorista; // Novo atributo
    private Date dataHoraPrevista;
    private StatusAgendamento status;

    public Agendamento(Pedido pedido, Date dataHoraPrevista) {
        this.pedido = pedido;
        this.dataHoraPrevista = dataHoraPrevista;
        this.status = StatusAgendamento.PENDENTE;
    }

    public void confirmarAgendamento(Caminhao caminhao, Motorista motorista) {
        if (this.status != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException("O agendamento só pode ser confirmado se estiver PENDENTE.");
        }
        if (caminhao == null || motorista == null) {
            throw new IllegalArgumentException("Caminhão e Motorista são obrigatórios para confirmar.");
        }
        this.caminhao = caminhao;
        this.motorista = motorista;
        this.status = StatusAgendamento.CONFIRMADO;
        System.out.println("Agendamento do pedido Nº" + pedido.getNumero() + " confirmado.");
    }


    public void setCaminhao(Caminhao caminhao) {
        this.caminhao = caminhao;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public void iniciarEntrega() {
        if (this.status != StatusAgendamento.CONFIRMADO) {
            throw new IllegalStateException("A entrega só pode ser iniciada se o agendamento estiver CONFIRMADO.");
        }
        this.status = StatusAgendamento.EM_ROTA;
        this.pedido.setStatus("EM ROTA DE ENTREGA"); // Atualiza status do pedido também
        System.out.println("Pedido Nº" + pedido.getNumero() + " saiu para entrega.");
    }

    public void finalizarEntrega() {
        if (this.status != StatusAgendamento.EM_ROTA) {
            throw new IllegalStateException("A entrega só pode ser finalizada se estiver EM ROTA.");
        }
        this.status = StatusAgendamento.CONCLUIDO;
        this.pedido.setStatus("ENTREGUE"); // Atualiza status final do pedido
        System.out.println("Pedido Nº" + pedido.getNumero() + " entregue com sucesso.");
    }

    public void cancelarAgendamento() {
        if (this.status == StatusAgendamento.CONCLUIDO || this.status == StatusAgendamento.EM_ROTA) {
            throw new IllegalStateException("Não é possível cancelar um agendamento que está em rota ou já foi concluído.");
        }
        this.status = StatusAgendamento.CANCELADO;
        System.out.println("Agendamento do pedido Nº" + pedido.getNumero() + " foi cancelado.");
    }

    // Getters e Setters
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public Date getDataHoraPrevista() {
        return dataHoraPrevista;
    }

    public void setDataHoraPrevista(Date dataHoraPrevista) {
        this.dataHoraPrevista = dataHoraPrevista;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}