package dados;

import dados.interfaces.IRepositorioCaminhao;
import negocio.Caminhao;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCaminhao implements IRepositorioCaminhao {
    private ArrayList<Caminhao> caminhoes = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "caminhoes.csv";


    public RepositorioCaminhao() {
        carregar();
    }

    @Override
    public boolean cadastrar(Caminhao caminhao) {
        if (this.caminhoes.add(caminhao)) {
            System.out.println("Caminhão cadastrado: " + caminhao.getPlaca());
            salvar();
            return true;
        }
        return false;
    }

    @Override
    public Caminhao buscarPorPlaca(String placa) {
        for (Caminhao c : this.caminhoes) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Caminhao> listarTodos() {
        return new ArrayList<>(this.caminhoes);
    }

    @Override
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
        List<String> linhas = new ArrayList<>();
        linhas.add("placa,capacidade,status,cadastrado");

        for (Caminhao caminhao : caminhoes) {
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
        this.caminhoes.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for(String linha : linhas){
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 4) {
                    Caminhao caminhao = new Caminhao(dados[0], Integer.parseInt(dados[1]), dados[2]);
                    caminhao.setCadastrado(Boolean.parseBoolean(dados[3]));
                    this.caminhoes.add(caminhao);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}