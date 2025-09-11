package fachada;

import dados.*;
import dados.interfaces.*;
import negocio.*;
import negocio.exceptions.CaminhaoNaoCadastradoException;
import negocio.exceptions.EstoqueInsuficienteException;
import negocio.exceptions.PontoException;
import negocio.exceptions.VagaInsuficienteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistribuidoraFachada {
    private final IRepositorioCliente repositorioCliente;
    private final IRepositorioCaminhao repositorioCaminhao;
    private final IRepositorioEstoque repositorioEstoque;
    private final IRepositorioFuncionario repFuncionario;
    private final IRepositorioPatio repPatio;
    private final NotaFiscal notaFiscal;
    private final Estoque estoque;

    public DistribuidoraFachada() {
        this.repositorioCliente = new RepositorioCliente();
        this.repositorioCaminhao = new RepositorioCaminhao();
        this.repositorioEstoque = new RepositorioEstoque();
        this.repFuncionario = new RepositorioFuncionario();
        this.repPatio = new RepositorioPatio();
        this.notaFiscal = new NotaFiscal();
        this.estoque = new Estoque();

        ArrayList<Produto> produtosCarregados = this.repositorioEstoque.carregar();
        this.estoque.setProdutos(produtosCarregados);

        if (this.repFuncionario.buscarPorMatricula("admin") == null) {
            System.out.println("Administrador padrão não encontrado, cadastrando...");
            Funcionario admin = new Funcionario("Administrador", 0, "Admin User", 30, "11122233344", "00000000", "N/A", "admin@distribuidora.com", "admin", "admin", PerfilUsuario.ADMINISTRADOR);
            this.repFuncionario.cadastrar(admin);
        }
    }

    // --- SISTEMA DE LOGIN E PERMISSÕES ---
    public Funcionario login(String matricula, String senha) {
        Funcionario func = this.repFuncionario.buscarPorMatricula(matricula);
        if (func != null && func.getSenha().equals(senha)) {
            return func;
        }
        return null;
    }

    private void checarPermissaoAdmin(Funcionario usuarioLogado) {
        if (usuarioLogado == null || usuarioLogado.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new SecurityException("Acesso negado. Apenas administradores podem realizar esta operação.");
        }
    }

    // --- MÉTODOS DE FUNCIONÁRIO (COM CONTROLE DE ACESSO) ---
    public void cadastrarFuncionario(Funcionario funcionario, Funcionario usuarioLogado) {
        checarPermissaoAdmin(usuarioLogado);
        if (repFuncionario.buscarPorMatricula(funcionario.getMatricula()) != null) {
            throw new IllegalArgumentException("Matrícula " + funcionario.getMatricula() + " já está em uso.");
        }
        this.repFuncionario.cadastrar(funcionario);
    }

    public boolean removerFuncionario(String matricula, Funcionario usuarioLogado) {
        checarPermissaoAdmin(usuarioLogado);
        if ("admin".equalsIgnoreCase(matricula)) {
            throw new SecurityException("Não é permitido remover o administrador padrão.");
        }
        return this.repFuncionario.remover(matricula);
    }

    // --- MÉTODOS DE PÁTIO (SIMPLIFICADOS) ---
    public void registrarEntradaPatio(String placa, Patio patio) throws VagaInsuficienteException, CaminhaoNaoCadastradoException {
        Caminhao c = this.repositorioCaminhao.buscarPorPlaca(placa);
        if (c == null) {
            throw new CaminhaoNaoCadastradoException("Caminhão com placa " + placa + " não está cadastrado no sistema.");
        }
        patio.registrarEntrada(c);
    }

    public void registrarSaidaPatio(String placa, Patio patio) throws CaminhaoNaoCadastradoException {
        Caminhao c = this.repositorioCaminhao.buscarPorPlaca(placa);
        if (c == null) {
            throw new CaminhaoNaoCadastradoException("Caminhão com placa " + placa + " não está no sistema.");
        }
        patio.registrarSaida(c);
    }

    // --- DEMAIS MÉTODOS ---
    public ArrayList<Cliente> getClientes() { return this.repositorioCliente.listarTodos(); }
    public void cadastrarCliente(Cliente cliente) { this.repositorioCliente.cadastrar(cliente); }
    public Cliente buscarClientePorCpf(String cpf) { return this.repositorioCliente.buscarPorCpf(cpf); }
    public boolean removerCliente(String cpf) { return this.repositorioCliente.remover(cpf); }
    public void cadastrarProduto(Produto produto) { this.estoque.cadastrarProduto(produto); this.repositorioEstoque.salvar(this.estoque.getProdutos());}
    public ArrayList<Produto> listarProdutos() { return this.estoque.listarProdutos(); }
    public Produto buscarProdutoPorCodigo(String codigo) { return this.estoque.consultarProduto(codigo); }
    public boolean removerProduto(String codigo) {
        Produto p = this.estoque.consultarProduto(codigo);
        if (p != null) {
            this.estoque.removerProduto(p);
            this.repositorioEstoque.salvar(this.estoque.getProdutos());
            return true;
        }
        return false;
    }
    public void atualizarPreco(Produto produto, double novoPreco){
        produto.setPreco(novoPreco);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }
    public Pedido criarPedido(Cliente cliente, ArrayList<Produto> produtosDesejados) throws EstoqueInsuficienteException {
        Pedido pedido = cliente.realizarPedido(produtosDesejados, this.estoque);
        System.out.println("Gerando nota fiscal...");
        notaFiscal.gerarNotaFiscal(pedido.getProdutos(), pedido);
        return pedido;
    }
    public void pagarPedido(Cliente cliente, Pedido pedido, double valorPago) {
        cliente.realizarPagamento(pedido, valorPago, this.estoque);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }
    public void cadastrarCaminhao(Caminhao caminhao) { this.repositorioCaminhao.cadastrar(caminhao); }
    public ArrayList<Caminhao> getTodosCaminhoes() { return this.repositorioCaminhao.listarTodos(); }
    public void cadastrarMotorista(Motorista motorista, Funcionario usuarioLogado){
        checarPermissaoAdmin(usuarioLogado);
        if (repFuncionario.buscarPorMatricula(motorista.getMatricula()) != null) {
            throw new IllegalArgumentException("Matrícula " + motorista.getMatricula() + " já está em uso.");
        }
        this.repFuncionario.cadastrar(motorista);
    }
    public ArrayList<Funcionario> getFuncionarios() { return this.repFuncionario.listarTodos(); }
    public Funcionario buscarFuncionarioPorMatricula(String matricula) { return this.repFuncionario.buscarPorMatricula(matricula); }
    public void baterPontoPorMatricula(String matricula) throws PontoException {
        Funcionario funcionario = this.repFuncionario.buscarPorMatricula(matricula);
        if (funcionario == null) {
            throw new NullPointerException("Funcionário com matrícula " + matricula + " não encontrado.");
        }
        funcionario.baterPonto(matricula);
        this.repFuncionario.atualizar(funcionario);
    }
    private ArrayList<Pedido> getTodosPedidosPagos() {
        ArrayList<Pedido> pedidosPagos = new ArrayList<>();
        ArrayList<Cliente> clientes = this.repositorioCliente.listarTodos();
        if (clientes != null) {
            for (Cliente cliente : clientes) {
                if (cliente.getPedidos() != null) {
                    for (Pedido pedido : cliente.getPedidos()) {
                        if ("PAGO".equalsIgnoreCase(pedido.getStatus())) {
                            pedidosPagos.add(pedido);
                        }
                    }
                }
            }
        }
        return pedidosPagos;
    }
    public List<Pedido> gerarRelatorioVendasHoje() { /* ... */ }
    public List<Pedido> gerarRelatorioVendasSemana() { /* ... */ }
    public List<Pedido> gerarRelatorioVendasMes() { /* ... */ }
    public Map<String, Integer> gerarRelatorioProdutosMaisVendidos() { /* ... */ }
    public Map<String, Double> gerarRelatorioClientesQueMaisCompram() { /* ... */ }
    public List<Produto> gerarRelatorioEstoqueBaixo(int limite) { /* ... */ }
}