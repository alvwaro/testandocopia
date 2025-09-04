package dados;

import negocio.Cliente;
import java.io.*;
import java.util.ArrayList;

public class RepositorioCliente {
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private static final String ARQUIVO_CSV = "clientes.csv";

    public RepositorioCliente() {
        carregar(); // Carrega os dados do CSV ao iniciar
    }

    public boolean cadastrar(Cliente cliente) {
        if (this.clientes.add(cliente)) {
            salvar(); // Salva a lista atualizada no CSV
            return true;
        }
        return false;
    }

    public Cliente buscarPorCpf(String cpf) {
        for (Cliente c : this.clientes) {
            if (c.getCpf().equals(cpf)) {
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
            salvar(); // Salva as alterações no CSV
            return true;
        }
        return false;
    }

    public boolean atualizar(Cliente cliente) {
        for (int i = 0; i < this.clientes.size(); i++) {
            if (this.clientes.get(i).getCpf().equals(cliente.getCpf())) {
                this.clientes.set(i, cliente);
                salvar(); // Salva as alterações no CSV
                return true;
            }
        }
        return false;
    }

    // --- MÉTODOS DE PERSISTÊNCIA EM CSV ---

    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            // Escreve o cabeçalho do CSV
            writer.write("cpf,nome,idade,telefone,endereco,email,tipo\n");
            // Itera sobre a lista e escreve cada cliente em uma nova linha
            for (Cliente cliente : clientes) {
                // Usar aspas para evitar problemas com vírgulas nos dados
                String linha = String.format("\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\"",
                        cliente.getCpf(),
                        cliente.getNome(),
                        cliente.getIdade(),
                        cliente.getTelefone(),
                        cliente.getEndereco(),
                        cliente.getEmail(),
                        cliente.getTipo()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) {
            return; // Se o arquivo não existe, não há nada a carregar
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.clientes.clear(); // Limpa a lista atual para carregar do arquivo
            String linha;
            reader.readLine(); // Pula a linha do cabeçalho

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 7) {
                    // Extrai os dados das colunas
                    String cpf = dados[0];
                    String nome = dados[1];
                    int idade = Integer.parseInt(dados[2]);
                    String telefone = dados[3];
                    String endereco = dados[4];
                    String email = dados[5];
                    String tipo = dados[6];

                    // Cria e adiciona o cliente à lista
                    Cliente cliente = new Cliente(nome, idade, cpf, telefone, endereco, email, tipo);
                    this.clientes.add(cliente);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar clientes do arquivo CSV: " + e.getMessage());
        }
    }
}