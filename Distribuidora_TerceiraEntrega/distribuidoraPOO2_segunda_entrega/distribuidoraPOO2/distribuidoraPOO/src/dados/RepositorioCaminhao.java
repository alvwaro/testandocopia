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


    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("placa,capacidade,status,cadastrado\n");
            for (Caminhao caminhao : caminhoes) {
                String linha = String.format("\"%s\",%d,\"%s\",%b",
                        caminhao.getPlaca(),
                        caminhao.getCapacidade(),
                        caminhao.getStatus(),
                        caminhao.getCadastrado()
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
            reader.readLine(); // Pula cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 4) {
                    String placa = dados[0];
                    int capacidade = Integer.parseInt(dados[1]);
                    String status = dados[2];
                    boolean cadastrado = Boolean.parseBoolean(dados[3]);

                    Caminhao caminhao = new Caminhao(placa, capacidade, status);
                    caminhao.setCadastrado(cadastrado);
                    this.caminhoes.add(caminhao);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar caminhões do arquivo CSV: " + e.getMessage());
        }
    }
}