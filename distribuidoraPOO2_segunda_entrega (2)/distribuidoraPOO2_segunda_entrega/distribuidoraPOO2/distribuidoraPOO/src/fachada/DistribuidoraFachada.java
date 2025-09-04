package fachada;

import dados.*;
import negocio.*;
import negocio.exceptions.VagaInsuficienteException;
import java.util.ArrayList;

public class DistribuidoraFachada {
    private final RepositorioCliente repositorioCliente;
    private final RepositorioEstoque repositorioEstoque;
    private final RepositorioFuncionario repFuncionario;
    private final RepositorioPatio repPatio;
    private final NotaFiscal notaFiscal;
    private final AuxiliarAdm adm;
    private final Estoque estoque; // A fachada agora gerencia o objeto de Estoque

    public DistribuidoraFachada() {
        this.repositorioCliente = new RepositorioCliente();
        this.repositorioEstoque = new RepositorioEstoque();
        this.repFuncionario = new RepositorioFuncionario();
        this.repPatio = new RepositorioPatio();
        this.notaFiscal = new NotaFiscal();

        // Cria e carrega o estoque a partir do repositório
        this.estoque = new Estoque();
        ArrayList<Produto> produtosCarregados = this.repositorioEstoque.carregar();
        this.estoque.setProdutos(produtosCarregados);

        this.adm = new AuxiliarAdm("adm", 800.00, "Lucas", 35, "789549", "879985664",
                "Rua F", "licas@gmail.com", "adm2025", "1234", repositorioCliente, this.repositorioEstoque, repPatio);
    }

    // --- MÉTODOS DE PRODUTO E ESTOQUE CORRIGIDOS ---

    public void cadastrarProduto(Produto produto) {
        this.estoque.cadastrarProduto(produto);
        this.repositorioEstoque.salvar(this.estoque.getProdutos()); // Salva o estado atualizado do estoque
    }

    // Sobrecarga para manter compatibilidade com a chamada antiga
    public void cadastrarProduto(Produto produto, Estoque estoqueLegado) {
        this.cadastrarProduto(produto);
    }

    public Produto buscarProdutoPorCodigo(String codigo) {
        return this.estoque.consultarProduto(codigo);
    }

    // **** MÉTODO CORRIGIDO ****
    // Agora retorna a lista de produtos para a UI
    public ArrayList<Produto> listarProdutos() {
        return this.estoque.listarProdutos();
    }

    public boolean removerProduto(String codigo) {
        Produto p = this.estoque.consultarProduto(codigo);
        if (p != null) {
            this.estoque.removerProduto(p);
            this.repositorioEstoque.salvar(this.estoque.getProdutos()); // Salva o estado atualizado
            return true;
        }
        return false;
    }

    public void atualizarPreco(Produto produto, double novoPreco){
        adm.atualizarPreco(produto, novoPreco);
        this.repositorioEstoque.salvar(this.estoque.getProdutos()); // Salva o estado atualizado
    }

    // --- DEMAIS MÉTODOS DA FACHADA ---

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

    public void listarClientes() {
        ArrayList<Cliente> clientes = this.repositorioCliente.listarTodos();
        if(clientes.isEmpty()){
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for(Cliente c : clientes){
            c.status();
            System.out.println("-----------");
        }
    }

    public Pedido criarPedido(Cliente cliente, ArrayList<Produto> produtosDesejados, Estoque estoque) {
        Pedido pedido = cliente.realizarPedido(produtosDesejados, this.estoque);
        System.out.println("Gerando nota fiscal...");
        notaFiscal.gerarNotaFiscal(pedido.getProdutos(), pedido);
        return pedido;
    }

    public void pagarPedido(Cliente cliente, Pedido pedido, double valorPago, Estoque estoque) {
        cliente.realizarPagamento(pedido, valorPago, this.estoque);
        this.repositorioEstoque.salvar(this.estoque.getProdutos()); // Salva o estoque após a baixa
    }

    public void cadastrarMotorista(Motorista motorista){
        adm.cadastrarMotorista(motorista);
    }

    public void cadastrarCaminhao(Caminhao caminhao, Patio patio){
        adm.cadastrarCaminhao(caminhao);
        adm.cadastrarCaminhaoPatio(caminhao, patio);
    }

    public void listarCaminhoesPatio(){
        this.repPatio.listarTodos();
    }

    public void permitirEntrada(Caminhao caminhao, Patio patio) throws VagaInsuficienteException {
        adm.permitirEntrada(caminhao, patio);
    }

    public void adicionarFilaSaida(Caminhao caminhao, Patio patio){
        adm.adicionarNaFilaSaida(caminhao, patio);
    }

    public void permirSaida(Caminhao caminhao, Patio patio){
        adm.permitirSaida(caminhao, patio);
    }

    public void baterPonto(Funcionario funcionario){
        funcionario.baterPonto(funcionario.getMatricula());
    }
}