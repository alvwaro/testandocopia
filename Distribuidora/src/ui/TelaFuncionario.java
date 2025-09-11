package ui;

import fachada.DistribuidoraFachada;
import negocio.Funcionario;
import negocio.Motorista;
import negocio.exceptions.CpfJaExistenteException;
import negocio.exceptions.PontoException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaFuncionario {
    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaFuncionario(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuFuncionario() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Funcionários ---");
            System.out.println("1. Cadastrar Novo Funcionário");
            System.out.println("2. Listar Todos os Funcionários");
            System.out.println("3. Buscar Funcionário por Matrícula");
            System.out.println("4. Remover Funcionário");
            System.out.println("5. Bater Ponto");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do scanner

                switch (opcao) {
                    case 1:
                        cadastrarFuncionario();
                        break;
                    case 2:
                        listarTodosFuncionarios();
                        break;
                    case 3:
                        buscarFuncionario();
                        break;
                    case 4:
                        removerFuncionario();
                        break;
                    case 5:
                        baterPonto();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void cadastrarFuncionario() {
        try {
            System.out.println("\n== Cadastro de Funcionário ==");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine(); // ESSENCIAL: Limpa o buffer após ler um número

            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Cargo: ");
            String cargo = scanner.nextLine();

            System.out.print("Salário: ");
            double salario = scanner.nextDouble();
            scanner.nextLine(); // ESSENCIAL: Limpa o buffer após ler um número

            System.out.print("Matrícula: ");
            String matricula = scanner.nextLine();

            if (cargo.equalsIgnoreCase("Motorista")) {
                System.out.print("CNH: ");
                String cnh = scanner.nextLine(); // Esta leitura agora é segura
                Motorista motorista = new Motorista(cnh, null, cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                fachada.cadastrarMotorista(motorista);
            } else {
                Funcionario funcionario = new Funcionario(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                fachada.cadastrarFuncionario(funcionario);
            }
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro: Idade ou Salário inválido. Por favor, insira um número.");
            scanner.nextLine(); // Limpa o buffer em caso de erro
        } catch (CpfJaExistenteException | IllegalArgumentException | SecurityException e) {
            // Captura qualquer erro de validação da camada de negócio (incluindo a CNH)
            System.out.println("\nErro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    private void baterPonto() {
        System.out.print("\nDigite sua matrícula para bater o ponto: ");
        String matricula = scanner.nextLine();
        try {
            fachada.baterPontoPorMatricula(matricula);
        } catch (PontoException | NullPointerException e) {
            System.err.println("Erro ao bater ponto: " + e.getMessage());
        }
    }

    private void listarTodosFuncionarios() {
        System.out.println("\n--- Lista Completa de Funcionários ---");
        ArrayList<Funcionario> funcionarios = fachada.getFuncionarios();

        if (funcionarios == null || funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado no sistema.");
            return;
        }

        for (Funcionario func : funcionarios) {
            System.out.println("----------------------------------------");
            System.out.println("Nome: " + func.getNome());
            System.out.println("Matrícula: " + func.getMatricula());
            System.out.println("Cargo: " + func.getCargo());
            if (func instanceof Motorista) {
                System.out.println("CNH: " + ((Motorista) func).getCNH());
            }
        }
        System.out.println("----------------------------------------");
    }

    private void buscarFuncionario() {
        System.out.print("\nDigite a matrícula do funcionário a ser buscado: ");
        String matricula = scanner.nextLine();
        Funcionario func = fachada.buscarFuncionarioPorMatricula(matricula);

        if (func != null) {
            System.out.println("--- Funcionário Encontrado ---");
            System.out.println("Nome: " + func.getNome());
            System.out.println("Matrícula: " + func.getMatricula());
            System.out.println("Cargo: " + func.getCargo());
            System.out.println("----------------------------");
        } else {
            System.out.println("Funcionário com matrícula " + matricula + " não encontrado.");
        }
    }

    private void removerFuncionario() {
        System.out.print("\nDigite a matrícula do funcionário a ser removido: ");
        String matricula = scanner.nextLine();
        if (fachada.removerFuncionario(matricula)) {
            System.out.println("Funcionário removido com sucesso.");
        } else {
            System.out.println("Não foi possível remover. Funcionário não encontrado.");
        }
    }
}
