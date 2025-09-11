package negocio;

import java.io.Serializable;
import negocio.exceptions.VagaInsuficienteException;

public class Caminhao  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String placa;
    private int capacidade;
    private Motorista motorista;
    private String status;
    private Patio patio;
    private boolean cadastrado = false;

    public Caminhao(String placa, String modelo, int capacidade, String status, Patio patio, Motorista motorista) {
        this.placa = placa;
        this.capacidade = capacidade;
        this.status = status;
        this.patio = patio;
        this.motorista = motorista;
    }

    public Caminhao(String placa, int capacidade, String status) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("A placa do caminhão não pode ser vazia.");
        }
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade do caminhão deve ser maior que zero.");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do caminhão não pode ser vazio.");
        }

        this.placa = placa;
        this.capacidade = capacidade;
        this.status = status;
        this.cadastrado = true;
    }

    public Caminhao(String placa){
        this.placa = placa;
    }


    public String getPlaca() {
        return placa;
    }


    public int getCapacidade() {
        return capacidade;
    }


    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getCadastrado() {
        return cadastrado;
    }

    public void setCadastrado(boolean cadastrado) {
        this.cadastrado = cadastrado;
    }

    public void entrarPatio(Patio patio, Caminhao caminhao) throws VagaInsuficienteException {
        if (patio == null) {
            throw new IllegalArgumentException("O pátio não pode ser nulo.");
        }
        if (caminhao == null) {
            throw new IllegalArgumentException("O caminhão não pode ser nulo.");
        }
        if (patio.getVagasDisponiveis() > 0) {
            patio.setVagasDisponiveis(patio.getVagasDisponiveis() - 1);
            caminhao.setStatus("NO PATIO");
        } else {
            throw new VagaInsuficienteException("Não há vagas disponíveis no pátio. O caminhão não pode entrar.");
        }
    }

    public void sairPatio(Patio patio) {
        if (patio == null) {
            throw new IllegalArgumentException("O pátio não pode ser nulo.");
        }

        if (patio.getVagasDisponiveis() >= patio.getQtdVagas()) {
            throw new IllegalStateException("O pátio já está com a capacidade máxima de vagas disponíveis.");
        }

        patio.setVagasDisponiveis(patio.getVagasDisponiveis() + 1);
        this.status = "Disponivel";
    }

    @Override
    public String toString() {
        return "Caminhao{" +
                "placa='" + placa + '\'' +
                ", capacidade=" + capacidade +
                ", status='" + status + '\'' +
                '}';
    }
}