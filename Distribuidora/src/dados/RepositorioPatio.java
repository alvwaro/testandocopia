package dados;

import java.io.*;
import negocio.*;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPatio {

    private ArrayList<Caminhao> caminhoesPatio = new ArrayList();
    private static final String ARQUIVO_CSV = "patio.csv";

    public RepositorioPatio() {
        carregar();
    }

    public boolean cadastrarCaminhaoPatio(Caminhao caminhao){
        if(this.caminhoesPatio.add(caminhao)){
            salvar();
            return true;
        }
        return false;
    }
    public Caminhao buscarPorPlaca(String placa) {
        for(Caminhao c : this.caminhoesPatio) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }
    public void listarTodos() {
        if (this.caminhoesPatio.isEmpty()) {
            System.out.println("Não há caminhoes cadastrados no patio.");
            return;
        }
        System.out.println("Caminhoes: ");
        for (Caminhao c : this.caminhoesPatio) {
            System.out.println(c.getPlaca());
        }
    }

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("placa,capacidade,status,cadastrado\n");
            for (Caminhao caminhao : caminhoesPatio) {
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
            System.err.println("Erro ao salvar o pátio no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.caminhoesPatio.clear();
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
                    this.caminhoesPatio.add(caminhao);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar o pátio do arquivo CSV: " + e.getMessage());
        }
    }
}
