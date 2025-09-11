package dados;

import negocio.Caminhao;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPatio {

    private ArrayList<Caminhao> caminhoesPatio = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "patio.csv";

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
        System.out.println("Caminhoes no pátio: ");
        for (Caminhao c : this.caminhoesPatio) {
            System.out.println(c.getPlaca());
        }
    }

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        linhas.add("placa,capacidade,status,cadastrado");

        for (Caminhao caminhao : caminhoesPatio) {
            String linha = String.format("\"%s\",%d,\"%s\",%b",
                    caminhao.getPlaca(),
                    caminhao.getCapacidade(),
                    caminhao.getStatus(),
                    caminhao.getCadastrado()
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    private void carregar() {
        this.caminhoesPatio.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 4) {
                    String placa = dados[0];
                    int capacidade = Integer.parseInt(dados[1]);
                    String status = dados[2];
                    boolean cadastrado = Boolean.parseBoolean(dados[3]);

                    Caminhao caminhao = new Caminhao(placa, capacidade, status);
                    caminhao.setCadastrado(cadastrado);
                    this.caminhoesPatio.add(caminhao);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}