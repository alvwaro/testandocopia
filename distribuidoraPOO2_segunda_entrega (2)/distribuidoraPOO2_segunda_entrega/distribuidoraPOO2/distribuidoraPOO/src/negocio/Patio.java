package negocio;

import java.io.Serializable;
import java.util.ArrayList;

public class Patio  implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ArrayList<Caminhao> caminhoesPatioLista;
    private ArrayList<Caminhao> filaEntrada;
    private ArrayList<Caminhao> filaSaida;
    private int vagasDisponiveis;
    private int qtdVagas = 20;

    public Patio(int vagasDisponiveis, int qtdVagas) {
        this.filaEntrada = new ArrayList<>();
        this.filaSaida = new ArrayList<>();
        this.vagasDisponiveis = vagasDisponiveis;
        this.caminhoesPatioLista = new ArrayList<>();
        this.qtdVagas = qtdVagas;
    }

    public Patio() {
        this.filaEntrada = new ArrayList<>();
        this.filaSaida = new ArrayList<>();
        this.caminhoesPatioLista = new ArrayList<>();
    }

    public Patio(int qtdVagas) {
        this.qtdVagas = qtdVagas;
        this.vagasDisponiveis = qtdVagas;
        this.filaEntrada = new ArrayList<>();
        this.filaSaida = new ArrayList<>();
        this.caminhoesPatioLista = new ArrayList<>();
    }

    public ArrayList<Caminhao> getFilaEntrada() {
        return filaEntrada;
    }

    public void setFilaEntrada(ArrayList<Caminhao> filaEntrada) {
        this.filaEntrada = filaEntrada;
    }

    public ArrayList<Caminhao> getFilaSaida() {
        return filaSaida;
    }

    public void setFilaSaida(ArrayList<Caminhao> filaSaida) {
        this.filaSaida = filaSaida;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public int getQtdVagas() {
        return qtdVagas;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Patio{" +
                "filaEntrada=" + filaEntrada +
                ", filaSaida=" + filaSaida +
                ", vagasDisponiveis=" + vagasDisponiveis +
                '}';
    }

    public boolean adicionarCaminhao(Caminhao caminhao) {
        if (caminhao == null) {
            throw new IllegalArgumentException("O caminhao informado eh nulo");
        }

        if (caminhoesPatioLista.size() < vagasDisponiveis) {
            boolean add = caminhoesPatioLista.add(caminhao);
            if (add) {
                vagasDisponiveis--;
            }
            return add;
        } else {
            filaEntrada.add(caminhao);
            return false;
        }
    }

    protected boolean adicionarFilaSaida(Caminhao caminhao){
        if (caminhao == null){
            throw new IllegalArgumentException("caminhao nulo");
        }
        boolean estavaNoPatio = caminhoesPatioLista.remove(caminhao);
        if(!estavaNoPatio){
            // Se não estava no pátio, não pode ser adicionado à fila de saída.
            // Lançar uma exceção pode ser mais informativo aqui.
            throw new IllegalArgumentException("O caminhão com placa " + caminhao.getPlaca() + " não se encontra no pátio.");
        }
        filaSaida.add(caminhao);
        return true;
    }

    protected boolean liberarSaida(Caminhao caminhao) {
        if (caminhao == null) {
            throw new IllegalArgumentException("Caminhao informado eh nulo");
        }
        boolean removido = filaSaida.remove(caminhao);
        if (!removido) {
            throw new IllegalArgumentException("O caminhão placa: " + caminhao.getPlaca() + " não está na fila de saída.");
        }

        vagasDisponiveis++; // Libera a vaga

        // Se houver caminhões na fila de espera, o próximo entra
        if(!filaEntrada.isEmpty()){
            Caminhao proximo = filaEntrada.remove(0);
            caminhoesPatioLista.add(proximo);
            vagasDisponiveis--;
            System.out.println("Caminhão da placa " + proximo.getPlaca() + " saiu da fila de espera e entrou no pátio.");
        }
        return true;
    }

    // **** MÉTODO CORRIGIDO E ATIVADO ****
    public void listarFilas(){
        System.out.println("\n--- FILA DE ENTRADA DE CAMINHÕES ---");
        if (filaEntrada == null || filaEntrada.isEmpty()){
            System.out.println("A fila de entrada do pátio está vazia.");
        }else{
            for(Caminhao caminhao : filaEntrada){
                System.out.println("-> " + caminhao.toString());
            }
        }

        System.out.println("\n--- FILA DE SAÍDA DE CAMINHÕES ---");
        if(filaSaida == null || filaSaida.isEmpty()){
            System.out.println("A fila de saída do pátio está vazia.");
        }else{
            for(Caminhao caminhao : filaSaida){
                System.out.println("-> " + caminhao.toString());
            }
        }
    }

    public ArrayList<Caminhao> getCaminhoesPatioLista() {
        return caminhoesPatioLista;
    }
}