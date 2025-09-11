package dados;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCSV {

    private final String basePath = "data_csv/";

    public void salvar(String nomeArquivo, List<String> linhas) {
        salvar(nomeArquivo, linhas, null);
    }

    public void salvar(String nomeArquivo, List<String> linhas, String cabecalho) {
        File diretorio = new File(basePath);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(basePath + nomeArquivo))) {
            if (cabecalho != null && !cabecalho.isEmpty()) {
                writer.write(cabecalho);
                writer.newLine();
            }
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
            return linhas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            reader.readLine();
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