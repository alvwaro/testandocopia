package dados;

import negocio.Caminhao;
import java.io.*;
import java.util.ArrayList;

public class RepositorioCaminhao {
    private ArrayList<Caminhao> caminhoes = new ArrayList<>();
    private static final String ARQUIVO_CSV = "caminhoes.csv";

    public RepositorioCaminhao() {
        carregar();
    }

    public boolean cadastrar(Caminhao caminhao) {
        if (this.caminhoes.add(caminhao)) {
            System.out.println("Caminhão cadastrado: " + caminhao.getPlaca());
            salvar();
            return true;
        }
        return false;
    }

    public Caminhao buscarPorPlaca(String placa) {
        for (Caminhao c : this.caminhoes) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Caminhao> listarTodos() {
        return new ArrayList<>(this.caminhoes);
    }

    public boolean remover(String placa) {
        Caminhao c = this.buscarPorPlaca(placa);
        if (c != null) {
            this.caminhoes.remove(c);
            salvar();
            return true;
        }
        return false;
    }

    public boolean atualizar(Caminhao caminhao) {
        for (int i = 0; i < this.caminhoes.size(); i++) {
            if (this.caminhoes.get(i).getPlaca().equals(caminhao.getPlaca())) {
                this.caminhoes.set(i, caminhao);
                salvar();
                return true;
            }
        }
        return false;
    }

    // --- MÉTODOS DE PERSISTÊNCIA EM CSV ---

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("placa,capacidade,status\n");
            for (Caminhao caminhao : caminhoes) {
                String linha = String.format("\"%s\",%d,\"%s\"",
                        caminhao.getPlaca(),
                        caminhao.getCapacidade(),
                        caminhao.getStatus()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar caminhões no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.caminhoes.clear();
            reader.readLine();

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 3) {
                    String placa = dados[0];
                    int capacidade = Integer.parseInt(dados[1]);
                    String status = dados[2];

                    Caminhao caminhao = new Caminhao(placa, null, capacidade, status, null, null);
                    this.caminhoes.add(caminhao);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar caminhões do arquivo CSV: " + e.getMessage());
        }
    }
}