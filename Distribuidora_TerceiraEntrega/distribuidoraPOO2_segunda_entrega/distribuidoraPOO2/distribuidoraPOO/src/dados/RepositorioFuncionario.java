package dados;

import negocio.Funcionario;
import negocio.Motorista;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepositorioFuncionario {
    private ArrayList<Funcionario> funcionarios = new ArrayList<>();
    private static final String ARQUIVO_CSV = "funcionarios.csv";

    public RepositorioFuncionario() {
        carregar();
    }

    public boolean cadastrar(Funcionario funcionario) {
        if (this.funcionarios.add(funcionario)) {
            salvar();
            return true;
        }
        return false;
    }

    public Funcionario buscarPorMatricula(String matricula) {
        for (Funcionario f : this.funcionarios) {
            if (f.getMatricula().equals(matricula)) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<Funcionario> listarTodos() {
        return new ArrayList<>(this.funcionarios);
    }

    public boolean remover(String matricula) {
        Funcionario f = this.buscarPorMatricula(matricula);
        if (f != null) {
            this.funcionarios.remove(f);
            salvar();
            return true;
        }
        return false;
    }

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            // Cabeçalho do CSV
            writer.write("tipo,matricula,nome,cpf,idade,telefone,endereco,email,cargo,salario,cnh,ultimaEntrada,ultimaSaida\n");

            for (Funcionario func : funcionarios) {
                // Prepara os dados com tratamento de nulos
                String tipo = (func instanceof Motorista) ? "Motorista" : "Funcionario";
                String cnh = (func instanceof Motorista) ? ((Motorista) func).getCNH() : "N/A";
                String ultimaEntradaStr = func.getUltimaEntrada() != null ? func.getUltimaEntrada().toString() : "null";
                String ultimaSaidaStr = func.getUltimaSaida() != null ? func.getUltimaSaida().toString() : "null";

                // Constrói a linha de forma segura, evitando NullPointerException
                String linha = String.format("\"%s\",\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\"",
                        tipo, func.getMatricula(), func.getNome(), func.getCpf(), func.getIdade(),
                        func.getTelefone(), func.getEndereco(), func.getEmail(), func.getCargo(),
                        func.getSalario(), cnh, ultimaEntradaStr, ultimaSaidaStr
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionários no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.funcionarios.clear();
            reader.readLine(); // Pula cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 13) {
                    String tipo = dados[0];
                    String matricula = dados[1];
                    String nome = dados[2];
                    String cpf = dados[3];
                    int idade = Integer.parseInt(dados[4]);
                    String telefone = dados[5];
                    String endereco = dados[6];
                    String email = dados[7];
                    String cargo = dados[8];
                    double salario = Double.parseDouble(dados[9].replace(",","."));
                    String cnh = dados[10];
                    String ultimaEntradaStr = dados[11];
                    String ultimaSaidaStr = dados[12];

                    Funcionario funcionario;
                    if ("Motorista".equals(tipo)) {
                        funcionario = new Motorista(cnh, null, cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                    } else {
                        funcionario = new Funcionario(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                    }

                    if (!"null".equals(ultimaEntradaStr)) {
                        funcionario.setUltimaEntrada(LocalDateTime.parse(ultimaEntradaStr));
                    }
                    if (!"null".equals(ultimaSaidaStr)) {
                        funcionario.setUltimaSaida(LocalDateTime.parse(ultimaSaidaStr));
                    }

                    this.funcionarios.add(funcionario);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar funcionários do arquivo CSV: " + e.getMessage());
        }
    }
}