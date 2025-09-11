package negocio;

import java.io.Serializable;
import java.util.Objects;

public class Caminhao  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String placa;
    private int capacidade;
    private Motorista motorista;
    private String status;
    private boolean cadastrado = false;

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

    // Construtor simplificado para uso interno
    public Caminhao(String placa){
        this.placa = placa;
    }

    // Getters e Setters
    public String getPlaca() { return placa; }
    public int getCapacidade() { return capacidade; }
    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean getCadastrado() { return cadastrado; }
    public void setCadastrado(boolean cadastrado) { this.cadastrado = cadastrado; }

    @Override
    public String toString() {
        return String.format("Placa: %s | Capacidade: %d toneladas | Status: %s",
                placa, capacidade, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caminhao caminhao = (Caminhao) o;
        return Objects.equals(placa, caminhao.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
}