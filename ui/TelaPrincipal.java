package ui;

import fachada.DistribuidoraFachada;
import negocio.Estoque;
import negocio.Funcionario;
import negocio.Patio;
import negocio.PerfilUsuario;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TelaPrincipal {

    private static DistribuidoraFachada fachada;
    private static TelaCliente telaCliente;
    private static TelaFuncionario telaFuncionario;
    private static TelaEstoque telaEstoque;
    private static TelaControlePatio telaControlePatio;
    private static TelaVendas telaVendas;
    private static TelaAgendamento telaAgendamento; // Adicionado
    private static TelaRelatorios telaRelatorios;
    private static Scanner scanner;
    private static Funcionario usuarioLogado;

    public static void main(String[] args) {
        inicializarSistema();
        executarLogin();
        if (usuarioLogado != null) {
            exibirMenuPrincipal();
        }
        System.out.println("Sistema encerrado.");
    }

    private static void inicializarSistema() {
        fachada = new DistribuidoraFachada();
        Estoque estoque = new Estoque();
        Patio patio = new Patio(20);

        telaCliente = new TelaCliente(fachada);
        telaFuncionario = new TelaFuncionario(fachada);
        telaEstoque = new TelaEstoque(fachada, estoque);
        telaControlePatio = new TelaControlePatio(fachada, patio);
        telaVendas = new TelaVendas(fachada, estoque);
        telaAgendamento = new TelaAgendamento(fachada, patio); // Adicionado
        telaRelatorios = new TelaRelatorios(fachada);
        scanner = new Scanner(System.in);
    }

    private static void executarLogin() {
        System.out.println("===== BEM-VINDO AO SISTEMA DA DISTRIBUIDORA =====");
        while (usuarioLogado == null) {
            System.out.print("Matrícula: ");
            String matricula = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            usuarioLogado = fachada.login(matricula, senha);

            if (usuarioLogado == null) {
                System.out.println("\nMatrícula ou senha incorreta. Tente novamente.");
            } else {
                System.out.println("\nLogin bem-sucedido! Bem-vindo, " + usuarioLogado.getNome() + ".");
            }
        }
    }

    private static void exibirMenuPrincipal() {
        int opcao = -1;
        do {
            try {
                System.out.println("\n===== MENU PRINCIPAL =====");

                PerfilUsuario perfil = usuarioLogado.getPerfil();
                if (perfil == PerfilUsuario.ADMINISTRADOR) {
                    exibirMenuAdministrador();
                } else if (perfil == PerfilUsuario.VENDEDOR) {
                    exibirMenuVendedor();
                } else if (perfil == PerfilUsuario.ESTOQUISTA) {
                    exibirMenuEstoquista();
                }

                System.out.println("0. Sair do Sistema (Logout)");
                System.out.print("Escolha uma opção: ");

                opcao = scanner.nextInt();
                scanner.nextLine();

                if (perfil == PerfilUsuario.ADMINISTRADOR) {
                    tratarOpcaoAdministrador(opcao);
                } else if (perfil == PerfilUsuario.VENDEDOR) {
                    tratarOpcaoVendedor(opcao);
                } else if (perfil == PerfilUsuario.ESTOQUISTA) {
                    tratarOpcaoEstoquista(opcao);
                }

            } catch (InputMismatchException e) {
                System.out.println("\nErro: Opção inválida. Por favor, insira um número.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
        System.out.println("Fazendo logout...");
    }

    // --- MENUS E TRATAMENTO DE OPÇÕES ---
    private static void exibirMenuAdministrador() {
        System.out.println("1. Gestão de Clientes");
        System.out.println("2. Gestão de Funcionários");
        System.out.println("3. Gestão de Estoque e Produtos");
        System.out.println("4. Controle de Pátio e Caminhões");
        System.out.println("5. Vendas e Pedidos");
        System.out.println("6. Gestão de Entregas");
        System.out.println("7. Relatórios Gerenciais");
    }

    private static void tratarOpcaoAdministrador(int opcao) {
        switch (opcao) {
            case 1: telaCliente.exibirMenuCliente(); break;
            case 2: telaFuncionario.exibirMenuFuncionario(usuarioLogado); break;
            case 3: telaEstoque.exibirMenuEstoque(usuarioLogado); break;
            case 4: telaControlePatio.exibirMenuPatio(usuarioLogado); break;
            case 5: telaVendas.exibirMenuVendas(); break;
            case 6: telaAgendamento.exibirMenuAgendamento(); break;
            case 7: telaRelatorios.exibirMenuRelatorios(); break;
            case 0: break;
            default: System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static void exibirMenuVendedor() {
        System.out.println("1. Gestão de Clientes");
        System.out.println("2. Vendas e Pedidos");
        System.out.println("3. Consultar Estoque");
    }

    private static void tratarOpcaoVendedor(int opcao) {
        switch (opcao) {
            case 1: telaCliente.exibirMenuCliente(); break;
            case 2: telaVendas.exibirMenuVendas(); break;
            case 3: telaEstoque.listarTodosOsProdutos(); break;
            case 0: break;
            default: System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static void exibirMenuEstoquista() {
        System.out.println("1. Gestão de Estoque e Produtos");
        System.out.println("2. Controle de Pátio");
    }

    private static void tratarOpcaoEstoquista(int opcao) {
        switch (opcao) {
            case 1: telaEstoque.exibirMenuEstoque(usuarioLogado); break;
            case 2: telaControlePatio.exibirMenuPatio(usuarioLogado); break;
            case 0: break;
            default: System.out.println("Opção inválida. Tente novamente.");
        }
    }
}