package fachada;

import dados.*;
import negocio.*;
import negocio.exceptions.CaminhaoNaoCadastradoException;
import negocio.exceptions.PontoException;
import negocio.exceptions.VagaInsuficienteException;
import java.util.ArrayList;

public class DistribuidoraFachada {
    private final RepositorioCliente repositorioCliente;
    private final RepositorioCaminhao repositorioCaminhao;
    private final RepositorioEstoque repositorioEstoque;
    private final RepositorioFuncionario repFuncionario;
    private final RepositorioPatio repPatio;
    private final NotaFiscal notaFiscal;
    private final AuxiliarAdm adm;
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


        this.adm = new AuxiliarAdm("adm", 800.00, "Lucas", 35, "789549", "879985664",
                "Rua F", "licas@gmail.com", "adm2025", "1234",
                repositorioCliente, this.repositorioEstoque, this.repPatio,
                this.repositorioCaminhao, this.repFuncionario);
    }

    // --- MÉTODOS DE CLIENTE ---
    public ArrayList<Cliente> getClientes() {
        return this.repositorioCliente.listarTodos();
    }
    public void cadastrarCliente(Cliente cliente) {
        adm.cadastrarCliente(cliente);
    }
    public Cliente buscarClientePorCpf(String cpf) {
        return this.repositorioCliente.buscarPorCpf(cpf);
    }
    public boolean removerCliente(String cpf) {
        return this.repositorioCliente.remover(cpf);
    }

    // --- MÉTODOS DE PRODUTO E ESTOQUE ---
    public void cadastrarProduto(Produto produto) {
        this.estoque.cadastrarProduto(produto);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }
    public Produto buscarProdutoPorCodigo(String codigo) {
        return this.estoque.consultarProduto(codigo);
    }
    public ArrayList<Produto> listarProdutos() {
        return this.estoque.listarProdutos();
    }
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
        adm.atualizarPreco(produto, novoPreco);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }

    // --- MÉTODOS DE VENDAS ---
    public Pedido criarPedido(Cliente cliente, ArrayList<Produto> produtosDesejados) {
        Pedido pedido = cliente.realizarPedido(produtosDesejados, this.estoque);
        System.out.println("Gerando nota fiscal...");
        notaFiscal.gerarNotaFiscal(pedido.getProdutos(), pedido);
        return pedido;
    }
    public void pagarPedido(Cliente cliente, Pedido pedido, double valorPago) {
        cliente.realizarPagamento(pedido, valorPago, this.estoque);
        this.repositorioEstoque.salvar(this.estoque.getProdutos());
    }

    // --- MÉTODOS DE CAMINHÃO E PÁTIO ---
    public void cadastrarCaminhao(Caminhao caminhao) {
        adm.cadastrarCaminhao(caminhao);
    }
    public ArrayList<Caminhao> getTodosCaminhoes() {
        return this.repositorioCaminhao.listarTodos();
    }
    public void permitirEntrada(String placa, Patio patio) throws VagaInsuficienteException {
        Caminhao c = this.repositorioCaminhao.buscarPorPlaca(placa);
        if (c == null) {
            throw new CaminhaoNaoCadastradoException("Caminhão com placa " + placa + " não está cadastrado no sistema.");
        }
        adm.permitirEntrada(c, patio);
    }
    public void registrarSaida(String placa, Patio patio) {
        Caminhao c = this.repositorioCaminhao.buscarPorPlaca(placa);
        if (c == null) {
            throw new CaminhaoNaoCadastradoException("Caminhão com placa " + placa + " não está cadastrado no sistema.");
        }
        adm.adicionarNaFilaSaida(c, patio);
        adm.permitirSaida(c, patio);
    }

    // --- MÉTODOS DE FUNCIONÁRIO ---
    public void cadastrarFuncionario(Funcionario funcionario) {
        adm.cadastrarFuncionario(funcionario);
    }
    public void cadastrarMotorista(Motorista motorista){
        adm.cadastrarMotorista(motorista);
    }
    public ArrayList<Funcionario> getFuncionarios() {
        return this.repFuncionario.listarTodos();
    }
    public Funcionario buscarFuncionarioPorMatricula(String matricula) {
        return this.repFuncionario.buscarPorMatricula(matricula);
    }
    public boolean removerFuncionario(String matricula) {
        return this.repFuncionario.remover(matricula);
    }
    public void baterPontoPorMatricula(String matricula) throws PontoException {
        Funcionario funcionario = this.repFuncionario.buscarPorMatricula(matricula);
        if (funcionario == null) {
            throw new NullPointerException("Funcionário com matrícula " + matricula + " não encontrado.");
        }
        funcionario.baterPonto(matricula);
        this.repFuncionario.atualizar(funcionario);
    }
}