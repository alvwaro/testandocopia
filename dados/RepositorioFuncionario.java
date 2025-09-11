package dados;

import dados.interfaces.IRepositorioFuncionario;
import dados.mappers.FuncionarioCsvMapper; // <-- Importado
import negocio.Funcionario;
import java.util.ArrayList;
import java.util.List;

public class RepositorioFuncionario implements IRepositorioFuncionario {
    private ArrayList<Funcionario> funcionarios = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "funcionarios.csv";

    public RepositorioFuncionario() {
        carregar();
    }

    @Override
    public boolean cadastrar(Funcionario funcionario) {
        if (this.funcionarios.add(funcionario)) {
            salvar();
            return true;
        }
        return false;
    }

    @Override
    public Funcionario buscarPorMatricula(String matricula) {
        for (Funcionario f : this.funcionarios) {
            if (f.getMatricula().equals(matricula)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Funcionario> listarTodos() {
        return new ArrayList<>(this.funcionarios);
    }

    @Override
    public boolean remover(String matricula) {
        Funcionario f = this.buscarPorMatricula(matricula);
        if (f != null) {
            this.funcionarios.remove(f);
            salvar();
            return true;
        }
        return false;
    }

    @Override
    public boolean atualizar(Funcionario funcionario) {
        for (int i = 0; i < this.funcionarios.size(); i++) {
            if (this.funcionarios.get(i).getMatricula().equals(funcionario.getMatricula())) {
                this.funcionarios.set(i, funcionario);
                salvar();
                return true;
            }
        }
        return false;
    }

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        String cabecalho = "tipo,matricula,nome,cpf,idade,telefone,endereco,email,cargo,salario,cnh,ultimaEntrada,ultimaSaida,senha,perfil";

        for (Funcionario func : funcionarios) {
            // Usa o Mapper para a conversão
            linhas.add(FuncionarioCsvMapper.toCsvLine(func));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }

    private void carregar() {
        this.funcionarios.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                // Usa o Mapper para a conversão
                Funcionario funcionario = FuncionarioCsvMapper.fromCsvLine(linha);
                this.funcionarios.add(funcionario);
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha + " - " + e.getMessage());
            }
        }
    }
}