package dados;

import dados.interfaces.IRepositorioPatio;
import dados.mappers.CaminhaoCsvMapper;
import negocio.Caminhao;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPatio implements IRepositorioPatio {
    private ArrayList<Caminhao> caminhoesPatio = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "patio.csv";

    public RepositorioPatio() {
        carregar();
    }

    @Override
    public boolean cadastrarCaminhaoPatio(Caminhao caminhao){
        if(this.caminhoesPatio.add(caminhao)){
            salvar();
            return true;
        }
        return false;
    }

    @Override
    public Caminhao buscarPorPlaca(String placa) {
        for(Caminhao c : this.caminhoesPatio) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Caminhao> getCaminhoesPatio() {
        return new ArrayList<>(this.caminhoesPatio);
    }

    @Override
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
        for (Caminhao caminhao : caminhoesPatio) {
            linhas.add(CaminhaoCsvMapper.toCsvLine(caminhao));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, "placa,capacidade,status,cadastrado");
    }

    private void carregar() {
        this.caminhoesPatio.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                Caminhao caminhao = CaminhaoCsvMapper.fromCsvLine(linha);
                this.caminhoesPatio.add(caminhao);
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha + " - " + e.getMessage());
            }
        }
    }
}