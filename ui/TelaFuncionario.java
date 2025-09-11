package ui;

import fachada.DistribuidoraFachada;
import negocio.Funcionario;
import negocio.Motorista;
import negocio.PerfilUsuario;
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

    public void exibirMenuFuncionario(Funcionario usuarioLogado) {
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
                scanner.nextLine();

                switch (opcao) {
                    case 1: cadastrarFuncionario(usuarioLogado); break;
                    case 2: listarTodosFuncionarios(); break;
                    case 3: buscarFuncionario(); break;
                    case 4: removerFuncionario(usuarioLogado); break;
                    case 5: baterPonto(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nErro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private void cadastrarFuncionario(Funcionario usuarioLogado) {
        try {
            System.out.println("\n== Cadastro de Novo Funcionário ==");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine();
            System.out.print("CPF (11 dígitos): ");
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
            scanner.nextLine();
            System.out.print("Matrícula (apenas números): ");
            String matricula = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            System.out.println("Defina o perfil do usuário:");
            System.out.println("1. Administrador\n2. Vendedor\n3. Estoquista");
            System.out.print("Escolha uma opção de perfil: ");
            int perfilOpt = scanner.nextInt();
            scanner.nextLine();
            PerfilUsuario perfil = PerfilUsuario.VENDEDOR; // Padrão
            if (perfilOpt == 1) perfil = PerfilUsuario.ADMINISTRADOR;
            if (perfilOpt == 3) perfil = PerfilUsuario.ESTOQUISTA;

            Funcionario novoFuncionario;
            if (cargo.equalsIgnoreCase("Motorista")) {
                System.out.print("CNH (apenas números): ");
                String cnh = scanner.nextLine();
                novoFuncionario = new Motorista(cnh, null, cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula, senha, perfil);
            } else {
                novoFuncionario = new Funcionario(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula, senha, perfil);
            }

            fachada.cadastrarFuncionario(novoFuncionario, usuarioLogado);
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (InputMismatchException e) {
            System.out.println("\nErro de Entrada: Idade, Salário ou Perfil devem ser um número válido.");
            scanner.nextLine();
        } catch (SecurityException e) {
            System.out.println("\nERRO DE PERMISSÃO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    private void removerFuncionario(Funcionario usuarioLogado) {
        try {
            System.out.print("\nDigite a matrícula do funcionário a ser removido: ");
            String matricula = scanner.nextLine();

            if (fachada.removerFuncionario(matricula, usuarioLogado)) {
                System.out.println("Funcionário removido com sucesso.");
            } else {
                System.out.println("Não foi possível remover. Funcionário não encontrado.");
            }
        } catch (SecurityException e) {
            System.out.println("\nERRO DE PERMISSÃO: " + e.getMessage());
        }
    }

    private void baterPonto() {
        System.out.print("\nDigite a matrícula do funcionário para bater o ponto: ");
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
            System.out.println("Perfil: " + func.getPerfil());
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
}