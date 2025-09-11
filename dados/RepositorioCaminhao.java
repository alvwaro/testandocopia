package dados;

import dados.interfaces.IRepositorioCaminhao;
import dados.mappers.CaminhaoCsvMapper; // <-- Importado
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
        String cabecalho = "placa,capacidade,status,cadastrado";

        for (Caminhao caminhao : caminhoes) {
            linhas.add(CaminhaoCsvMapper.toCsvLine(caminhao));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }

    private void carregar() {
        this.caminhoes.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for(String linha : linhas){
            try {
                Caminhao caminhao = CaminhaoCsvMapper.fromCsvLine(linha);
                this.caminhoes.add(caminhao);
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}