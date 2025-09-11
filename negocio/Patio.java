package negocio;

import negocio.exceptions.VagaInsuficienteException;

import java.io.Serializable;
import java.util.ArrayList;

public class Patio implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int capacidadeTotal;
    private final ArrayList<Caminhao> caminhoesEstacionados;

    public Patio(int capacidadeTotal) {
        if (capacidadeTotal <= 0) {
            throw new IllegalArgumentException("A capacidade do pátio deve ser positiva.");
        }
        this.capacidadeTotal = capacidadeTotal;
        this.caminhoesEstacionados = new ArrayList<>();
    }

    public int getVagasDisponiveis() {
        return capacidadeTotal - caminhoesEstacionados.size();
    }

    public int getCapacidadeTotal() {
        return capacidadeTotal;
    }

    public ArrayList<Caminhao> getCaminhoesEstacionados() {
        // Retorna uma cópia para proteger a lista original
        return new ArrayList<>(caminhoesEstacionados);
    }

    public boolean verificarSeCaminhaoEstaNoPatio(Caminhao caminhao) {
        // O método contains() em ArrayList usa o equals() para comparar.
        // Precisamos garantir que Caminhao tenha um método equals() adequado.
        for (Caminhao c : this.caminhoesEstacionados) {
            if (c.getPlaca().equals(caminhao.getPlaca())) {
                return true;
            }
        }
        return false;
    }

    public void registrarEntrada(Caminhao caminhao) throws VagaInsuficienteException {
        if (getVagasDisponiveis() <= 0) {
            throw new VagaInsuficienteException("Não há vagas disponíveis no pátio.");
        }
        if (caminhao == null) {
            throw new IllegalArgumentException("Caminhão não pode ser nulo.");
        }
        if (verificarSeCaminhaoEstaNoPatio(caminhao)) {
            throw new IllegalStateException("Caminhão com placa " + caminhao.getPlaca() + " já está no pátio.");
        }

        this.caminhoesEstacionados.add(caminhao);
        caminhao.setStatus("NO PATIO");
        System.out.println("Caminhão " + caminhao.getPlaca() + " entrou no pátio.");
    }

    public void registrarSaida(Caminhao caminhao) {
        Caminhao caminhaoParaRemover = null;
        if (caminhao == null) {
            throw new IllegalArgumentException("Caminhão não pode ser nulo.");
        }
        for (Caminhao c : this.caminhoesEstacionados) {
            if (c.getPlaca().equals(caminhao.getPlaca())) {
                caminhaoParaRemover = c;
                break;
            }
        }

        if (caminhaoParaRemover == null) {
            throw new IllegalStateException("Caminhão com placa " + caminhao.getPlaca() + " não se encontra no pátio.");
        }

        this.caminhoesEstacionados.remove(caminhaoParaRemover);
        caminhao.setStatus("Disponivel");
        System.out.println("Caminhão " + caminhao.getPlaca() + " saiu do pátio.");
    }

    public void listarCaminhoesNoPatio(){
        System.out.println("\n--- Caminhões Atualmente no Pátio ---");
        if (caminhoesEstacionados.isEmpty()) {
            System.out.println("O pátio está vazio.");
        } else {
            for (Caminhao c : caminhoesEstacionados) {
                System.out.println(c.toString());
            }
        }
        System.out.println("Vagas disponíveis: " + getVagasDisponiveis() + "/" + getCapacidadeTotal());
    }
}