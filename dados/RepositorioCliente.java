package dados;

import dados.interfaces.IRepositorioCliente;
import dados.mappers.ClienteCsvMapper; // <-- Importado
import negocio.Cliente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCliente implements IRepositorioCliente {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private final PersistenciaCSV persistencia = new PersistenciaCSV();
    private static final String NOME_ARQUIVO = "clientes.csv";

    public RepositorioCliente() {
        carregar();
    }

    @Override
    public boolean cadastrar(Cliente cliente) {
        if (this.clientes.add(cliente)) {
            salvar();
            return true;
        }
        return false;
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        for (Cliente c : this.clientes) {
            if (c.getCpf() != null && c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Cliente> listarTodos() {
        return new ArrayList<>(this.clientes);
    }

    @Override
    public boolean remover(String cpf) {
        Cliente c = this.buscarPorCpf(cpf);
        if (c != null) {
            this.clientes.remove(c);
            salvar();
            return true;
        }
        return false;
    }

    @Override
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

    private void salvar() {
        List<String> linhas = new ArrayList<>();
        String cabecalho = "cpf,nome,idade,telefone,endereco,email,tipo,cadastrado";

        for (Cliente cliente : clientes) {
            // Usa o Mapper para a conversão
            linhas.add(ClienteCsvMapper.toCsvLine(cliente));
        }
        persistencia.salvar(NOME_ARQUIVO, linhas, cabecalho);
    }

    private void carregar() {
        this.clientes.clear();
        List<String> linhas = persistencia.carregar(NOME_ARQUIVO);

        for (String linha : linhas) {
            try {
                // Usa o Mapper para a conversão
                Cliente cliente = ClienteCsvMapper.fromCsvLine(linha);
                this.clientes.add(cliente);
            } catch (Exception e) {
                System.err.println("ERRO AO PROCESSAR LINHA DO ARQUIVO " + NOME_ARQUIVO + ": " + linha);
            }
        }
    }
}