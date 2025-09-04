package ui;

import fachada.DistribuidoraFachada;
import negocio.Cliente;
import negocio.exceptions.ClienteInvalidoException;
import negocio.exceptions.CpfJaExistenteException;

import java.util.ArrayList; // Importe a classe ArrayList
import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaCliente {
    private final DistribuidoraFachada fachada;
    private final Scanner scanner;

    public TelaCliente(DistribuidoraFachada fachada) {
        this.fachada = fachada;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuCliente() {
        int opcao;
        do {
            System.out.println("\n--- Gestão de Clientes ---");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Listar Todos os Clientes (Detalhado)");
            System.out.println("3. Buscar Cliente por CPF");
            System.out.println("4. Remover Cliente");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer

                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        // Chama o novo método de listagem detalhada
                        listarTodosOsClientes();
                        break;
                    case 3:
                        buscarCliente();
                        break;
                    case 4:
                        removerCliente();
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

    /**
     * Novo método para listar todos os clientes com informações completas.
     * Este método assume que a fachada foi ajustada para retornar a lista de clientes.
     */
    private void listarTodosOsClientes() {
        System.out.println("\n--- Lista Completa de Clientes ---");
        // Supondo que a fachada agora retorna uma lista de clientes
        ArrayList<Cliente> clientes = fachada.getClientes(); // Você precisará criar este método na fachada

        if (clientes == null || clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado no sistema.");
            return;
        }

        for (Cliente cliente : clientes) {
            cliente.status(); // O método status() já formata e imprime os detalhes
            System.out.println("--------------------"); // Separador para melhor visualização
        }
    }

    private void cadastrarCliente() {
        try {
            System.out.println("\n== Cadastro de Cliente ==");
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
            System.out.println("\nErro: A idade deve ser um número.");
            scanner.nextLine();
        } catch (ClienteInvalidoException | CpfJaExistenteException | SecurityException e) {
            System.out.println("\nErro ao cadastrar: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        System.out.print("\nDigite o CPF do cliente a ser buscado: ");
        String cpf = scanner.nextLine();
        Cliente c = fachada.buscarClientePorCpf(cpf);
        if (c != null) {
            System.out.println("Cliente encontrado:");
            c.status();
        } else {
            System.out.println("Cliente com CPF " + cpf + " não encontrado.");
        }
    }

    private void removerCliente() {
        System.out.print("\nDigite o CPF do cliente a ser removido: ");
        String cpf = scanner.nextLine();
        if (fachada.removerCliente(cpf)) {
            System.out.println("Cliente removido com sucesso.");
        } else {
            System.out.println("Não foi possível remover. Cliente não encontrado.");
        }
    }
}