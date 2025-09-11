package dados;

import dados.interfaces.IRepositorioFuncionario;
import negocio.Funcionario;
import negocio.Motorista;
import java.time.LocalDateTime;
import negocio.PerfilUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        // Adicionar novos campos ao cabeçalho
        linhas.add("tipo,matricula,nome,cpf,idade,telefone,endereco,email,cargo,salario,cnh,ultimaEntrada,ultimaSaida,senha,perfil");

        for (Funcionario func : funcionarios) {
            String tipo = (func instanceof Motorista) ? "Motorista" : "Funcionario";
            String cnh = (func instanceof Motorista) ? ((Motorista) func).getCNH() : "N/A";
            String ultimaEntradaStr = func.getUltimaEntrada() != null ? func.getUltimaEntrada().toString() : "null";
            String ultimaSaidaStr = func.getUltimaSaida() != null ? func.getUltimaSaida().toString() : "null";

            // Adicionar novos campos ao format
            String linha = String.format(Locale.US, "\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                    tipo, func.getMatricula(), func.getNome(), func.getCpf(), func.getIdade(),
                    func.getTelefone(), func.getEndereco(), func.getEmail(), func.getCargo(),
                    func.getSalario(), cnh, ultimaEntradaStr, ultimaSaidaStr,
                    func.getSenha(), func.getPerfil().name() // Salva a senha e o nome do enum
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    private void carregar() {
        this.funcionarios.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                // Agora esperamos 15 colunas
                if (dados.length == 15) {
                    // ... (leitura dos dados existentes)
                    String senha = dados[13];
                    PerfilUsuario perfil = PerfilUsuario.valueOf(dados[14]); // Converte a string de volta para o enum

                    Funcionario funcionario;
                    if ("Motorista".equals(dados[0])) {
                        // Passa os novos parâmetros
                        funcionario = new Motorista(dados[10], null, dados[8], Double.parseDouble(dados[9]), dados[2], Integer.parseInt(dados[4]), dados[3], dados[5], dados[6], dados[7], dados[1], senha, perfil);
                    } else {
                        // Passa os novos parâmetros
                        funcionario = new Funcionario(dados[8], Double.parseDouble(dados[9]), dados[2], Integer.parseInt(dados[4]), dados[3], dados[5], dados[6], dados[7], dados[1], senha, perfil);
                    }

                    // ... (carregamento de ultimaEntrada/Saida)
                    this.funcionarios.add(funcionario);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha + " - " + e.getMessage());
            }
        }
    }
}
