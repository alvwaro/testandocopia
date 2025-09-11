package dados;

import negocio.Cliente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCliente {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "clientes.csv";

    public RepositorioCliente() {
        carregar();
    }

    public boolean cadastrar(Cliente cliente) {
        if (this.clientes.add(cliente)) {
            salvar();
            return true;
        }
        return false;
    }

    // ... (os métodos buscarPorCpf, listarTodos, remover, atualizar continuam IGUAIS) ...
    public Cliente buscarPorCpf(String cpf) {
        for (Cliente c : this.clientes) {
            if (c.getCpf() != null && c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Cliente> listarTodos() {
        return new ArrayList<>(this.clientes);
    }

    public boolean remover(String cpf) {
        Cliente c = this.buscarPorCpf(cpf);
        if (c != null) {
            this.clientes.remove(c);
            salvar();
            return true;
        }
        return false;
    }

    public boolean atualizar(Cliente cliente) {
        for (int i = 0; i < this.clientes.size(); i++) {
            if (this.clientes.get(i).getCpf().equals(cliente.getCpf())) {
                this.clientes.set(i, cliente);
                salvar();
                return true;
            }
        }
        return false;
    }


    // --- MÉTODOS DE PERSISTÊNCIA MODIFICADOS ---

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        // Adiciona o cabeçalho
        linhas.add("cpf,nome,idade,telefone,endereco,email,tipo,cadastrado");

        for (Cliente cliente : clientes) {
            String linha = String.format("\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%b",
                    cliente.getCpf(), cliente.getNome(), cliente.getIdade(), cliente.getTelefone(),
                    cliente.getEndereco(), cliente.getEmail(), cliente.getTipo(), cliente.isCadastrado()
            );
            linhas.add(linha);
        }
        persistencia.salvar(NOME_ARQUIVO, linhas);
    }

    private void carregar() {
        this.clientes.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 8) {
                    Cliente cliente = new Cliente(dados[1], Integer.parseInt(dados[2]), dados[0], dados[3], dados[4], dados[5], dados[6]);
                    cliente.setCadastrado(Boolean.parseBoolean(dados[7]));
                    this.clientes.add(cliente);
                }
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}