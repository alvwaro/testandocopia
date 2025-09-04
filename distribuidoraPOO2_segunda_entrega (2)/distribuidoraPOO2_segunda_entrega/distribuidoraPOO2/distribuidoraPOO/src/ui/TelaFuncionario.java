package ui;

import fachada.DistribuidoraFachada;
import negocio.Funcionario;
import negocio.Motorista;
import negocio.exceptions.CpfJaExistenteException;

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
        // Implementar menu similar aos outros (cadastrar, listar, buscar, remover, bater ponto)
        System.out.println("\n[Módulo de Funcionários em construção]");
        cadastrarFuncionario(); // Exemplo
    }

    private void cadastrarFuncionario() {
        try {
            System.out.println("== Cadastro de Funcionário ==");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine();
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
            scanner.nextLine();
            System.out.print("Matrícula: ");
            String matricula = scanner.nextLine();

            if (cargo.equalsIgnoreCase("Motorista")) {
                System.out.print("CNH: ");
                String cnh = scanner.nextLine();
                Motorista motorista = new Motorista(cnh, null, cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                fachada.cadastrarMotorista(motorista);
            } else {
                // Supondo que exista um método para outros funcionários
                // Funcionario funcionario = new Funcionario(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
                // fachada.cadastrarFuncionario(funcionario);
            }
            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Erro: Idade ou Salário inválido. Por favor, insira um número.");
            scanner.nextLine();
        } catch (CpfJaExistenteException | IllegalArgumentException | SecurityException e) {
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }
}