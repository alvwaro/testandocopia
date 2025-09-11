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
    private final IRepositorioAgendamento repositorioAgendamento; // Adicionado
    private final NotaFiscal notaFiscal;
    private final Estoque estoque;

    public DistribuidoraFachada() {
        this.repositorioCliente = new RepositorioCliente();
        this.repositorioCaminhao = new RepositorioCaminhao();
        this.repositorioEstoque = new RepositorioEstoque();
        this.repFuncionario = new RepositorioFuncionario();
        this.repPatio = new RepositorioPatio();
        this.repositorioAgendamento = new RepositorioAgendamento(); // Adicionado
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
    public void cadastrarCliente(Cliente cliente) {
        cliente.setCadastrado(true);
        this.repositorioCliente.cadastrar(cliente);
    }
    public Cliente buscarClientePorCpf(String cpf) { return this.repositorioCliente.buscarPorCpf(cpf); }
    public boolean removerCliente(String cpf) { return this.repositorioCliente.remover(cpf); }
    public void cadastrarProduto(Produto produto, Funcionario usuarioLogado) { checarPermissaoAdmin(usuarioLogado); this.estoque.cadastrarProduto(produto); this.repositorioEstoque.salvar(this.estoque.getProdutos());}
    public ArrayList<Produto> listarProdutos() { return this.estoque.listarProdutos(); }
    public Produto buscarProdutoPorCodigo(String codigo) { return this.estoque.consultarProduto(codigo); }
    public boolean removerProduto(String codigo, Funcionario usuarioLogado) {
        checarPermissaoAdmin(usuarioLogado);
        Produto p = this.estoque.consultarProduto(codigo);
        if (p != null) {
            this.estoque.removerProduto(p);
            this.repositorioEstoque.salvar(this.estoque.getProdutos());
            return true;
        }
        return false;
    }
    public void atualizarPreco(Produto produto, double novoPreco, Funcionario usuarioLogado){
        checarPermissaoAdmin(usuarioLogado);
        produto.setPreco(novoPreco);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }
    public Pedido criarPedido(Cliente cliente, ArrayList<Produto> produtosDesejados) throws EstoqueInsuficienteException {
        Pedido pedido = cliente.realizarPedido(produtosDesejados, this.estoque);
        System.out.println("Gerando nota fiscal...");
        notaFiscal.gerarNotaFiscal(pedido.getProdutos(), pedido);

        // CORREÇÃO: Salva o estado do cliente (com o novo pedido) no repositório.
        this.repositorioCliente.atualizar(cliente);

        return pedido;
    }
    public void pagarPedido(Cliente cliente, Pedido pedido, double valorPago) {
        cliente.realizarPagamento(pedido, valorPago, this.estoque);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());

        // CORREÇÃO: Salva o estado do cliente (com o pedido pago) no repositório.
        this.repositorioCliente.atualizar(cliente);
    }
    public void cadastrarCaminhao(Caminhao caminhao, Funcionario usuarioLogado) { checarPermissaoAdmin(usuarioLogado); this.repositorioCaminhao.cadastrar(caminhao); }
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
    public List<Pedido> gerarRelatorioVendasHoje() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioDoDia = agora.toLocalDate().atStartOfDay();
        return GeradorRelatorios.filtrarPedidosPorPeriodo(getTodosPedidosPagos(), inicioDoDia, agora);
    }
    public List<Pedido> gerarRelatorioVendasSemana() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioDaSemana = agora.toLocalDate().minusDays(agora.getDayOfWeek().getValue() - 1).atStartOfDay();
        return GeradorRelatorios.filtrarPedidosPorPeriodo(getTodosPedidosPagos(), inicioDaSemana, agora);
    }
    public List<Pedido> gerarRelatorioVendasMes() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioDoMes = agora.toLocalDate().withDayOfMonth(1).atStartOfDay();
        return GeradorRelatorios.filtrarPedidosPorPeriodo(getTodosPedidosPagos(), inicioDoMes, agora);
    }
    public Map<String, Integer> gerarRelatorioProdutosMaisVendidos() {
        return GeradorRelatorios.calcularProdutosMaisVendidos(getTodosPedidosPagos());
    }
    public Map<String, Double> gerarRelatorioClientesQueMaisCompram() {
        return GeradorRelatorios.calcularClientesQueMaisCompram(this.repositorioCliente.listarTodos());
    }
    public List<Produto> gerarRelatorioEstoqueBaixo(int limite) {
        return GeradorRelatorios.gerarRelatorioEstoqueBaixo(this.estoque, limite);
    }

    // --- NOVOS MÉTODOS DE GESTÃO DE ENTREGAS ---
    public void criarAgendamento(Pedido pedido) {
        if (pedido == null || !"PAGO".equalsIgnoreCase(pedido.getStatus())) {
            throw new IllegalArgumentException("Só é possível criar agendamento para pedidos PAGOS.");
        }
        if (this.repositorioAgendamento.buscarPorPedido(pedido.getNumero()) != null) {
            throw new IllegalStateException("Este pedido já possui um agendamento.");
        }
        Agendamento agendamento = new Agendamento(pedido, new java.util.Date());
        this.repositorioAgendamento.cadastrar(agendamento);
        System.out.println("Agendamento PENDENTE criado para o pedido Nº" + pedido.getNumero());
    }

    public void confirmarAgendamento(int numeroPedido, String placaCaminhao, String matriculaMotorista) {
        Agendamento agendamento = this.repositorioAgendamento.buscarPorPedido(numeroPedido);
        if (agendamento == null) {
            throw new NullPointerException("Agendamento não encontrado para o pedido Nº" + numeroPedido);
        }
        Caminhao caminhao = this.repositorioCaminhao.buscarPorPlaca(placaCaminhao);
        if (caminhao == null) {
            throw new NullPointerException("Caminhão com placa " + placaCaminhao + " não encontrado.");
        }
        Funcionario func = this.repFuncionario.buscarPorMatricula(matriculaMotorista);
        if (!(func instanceof Motorista)) {
            throw new IllegalArgumentException("A matrícula " + matriculaMotorista + " não pertence a um motorista.");
        }
        Motorista motorista = (Motorista) func;

        agendamento.confirmarAgendamento(caminhao, motorista);
        this.repositorioAgendamento.atualizar(agendamento);
    }

    public void iniciarEntrega(int numeroPedido, Patio patio) {
        Agendamento agendamento = this.repositorioAgendamento.buscarPorPedido(numeroPedido);
        if (agendamento == null) {
            throw new NullPointerException("Agendamento não encontrado para o pedido Nº" + numeroPedido);
        }
        registrarSaidaPatio(agendamento.getCaminhao().getPlaca(), patio);
        agendamento.iniciarEntrega();
        this.repositorioAgendamento.atualizar(agendamento);
    }

    public void finalizarEntrega(int numeroPedido, Patio patio) {
        Agendamento agendamento = this.repositorioAgendamento.buscarPorPedido(numeroPedido);
        if (agendamento == null) {
            throw new NullPointerException("Agendamento não encontrado para o pedido Nº" + numeroPedido);
        }
        registrarEntradaPatio(agendamento.getCaminhao().getPlaca(), patio);
        agendamento.finalizarEntrega();
        this.repositorioAgendamento.atualizar(agendamento);
    }

    public ArrayList<Agendamento> listarTodosAgendamentos() {
        return this.repositorioAgendamento.listarTodos();
    }

    public Pedido buscarPedidoPorNumero(int numeroPedido) {
        ArrayList<Cliente> clientes = this.repositorioCliente.listarTodos();
        for(Cliente c : clientes) {
            for (Pedido p : c.getPedidos()) {
                if (p.getNumero() == numeroPedido) {
                    return p;
                }
            }
        }
        return null;
    }
}