package dados;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCSV {

    private final String basePath = "data_csv/"; // Caminho base para a nova pasta

    public void salvar(String nomeArquivo, List<String> linhas) {
        // Garante que o diretório exista
        File diretorio = new File(basePath);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(basePath + nomeArquivo))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO SALVAR O ARQUIVO " + nomeArquivo + ": " + e.getMessage());
        }
    }

    public List<String> carregar(String nomeArquivo) {
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(basePath + nomeArquivo);
        if (!arquivo.exists()) {
            return linhas; // Retorna lista vazia se o arquivo não existir
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            reader.readLine(); // Pula a linha do cabeçalho
            String linha;
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO CARREGAR O ARQUIVO " + nomeArquivo + ": " + e.getMessage());
        }
        return linhas;
    }
}