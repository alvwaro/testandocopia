package ui;

import fachada.DistribuidoraFachada;
import negocio.Cliente;
import negocio.exceptions.ClienteInvalidoException;
import negocio.exceptions.CpfJaExistenteException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaCadastroCliente {

    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaCadastroCliente(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void cadastrarCliente() {
        try {
            System.out.println("== Cadastro de Cliente ==");
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

            System.out.print("Tipo (ex: Varejo, Atacado): ");
            String tipo = scanner.nextLine();

            Cliente cliente = new Cliente(nome, idade, cpf, telefone, endereco, email, tipo);
            fachada.cadastrarCliente(cliente);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (InputMismatchException e) {
            System.out.println("Erro: Idade inválida. Por favor, insira um número.");
            scanner.nextLine();
        } catch (ClienteInvalidoException | CpfJaExistenteException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}