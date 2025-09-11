package dados;

import negocio.Cliente;
import java.io.*;
import java.util.ArrayList;

public class RepositorioCliente {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private static final String ARQUIVO_CSV = "clientes.csv";

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


    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("cpf,nome,idade,telefone,endereco,email,tipo,cadastrado\n");
            for (Cliente cliente : clientes) {
                String linha = String.format("\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%b",
                        cliente.getCpf(),
                        cliente.getNome(),
                        cliente.getIdade(),
                        cliente.getTelefone(),
                        cliente.getEndereco(),
                        cliente.getEmail(),
                        cliente.getTipo(),
                        cliente.isCadastrado()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO AO SALVAR CLIENTES: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.clientes.clear();
            reader.readLine(); // Pula a linha do cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for (int i = 0; i < dados.length; i++) {
                    if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                        dados[i] = dados[i].substring(1, dados[i].length() - 1);
                    }
                }

                if (dados.length == 8) {
                    String cpf = dados[0];
                    String nome = dados[1];
                    int idade = Integer.parseInt(dados[2]);
                    String telefone = dados[3];
                    String endereco = dados[4];
                    String email = dados[5];
                    String tipo = dados[6];
                    boolean cadastrado = Boolean.parseBoolean(dados[7]);

                    Cliente cliente = new Cliente(nome, idade, cpf, telefone, endereco, email, tipo);
                    cliente.setCadastrado(cadastrado);
                    this.clientes.add(cliente);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERRO CRÍTICO AO CARREGAR CLIENTES: " + e.getMessage());
        }
    }
}
