package negocio;

import dados.*;
import negocio.exceptions.CaminhaoNaoCadastradoException;
import negocio.exceptions.CpfJaExistenteException;
import negocio.exceptions.CaminhaoJaExisteException;
import java.util.ArrayList;
import negocio.exceptions.VagaInsuficienteException;

public class AuxiliarAdm extends Funcionario {
    private final String login;
    private final RepositorioCaminhao repCaminhao;
    private final RepositorioEstoque repEstoque;
    private final RepositorioFuncionario repFuncionario; // Removida a inicialização aqui
    private final RepositorioPatio repPatio;
    private final RepositorioCliente repCliente;
    private static final String loginCadastro = "adm2025";

    public AuxiliarAdm(String cargo, double salario, String nome, int idade, String cpf, String telefone, String endereco, String email, String login, String matricula, RepositorioCliente repCliente, RepositorioEstoque repEstoque, RepositorioPatio repPatio, RepositorioCaminhao repCaminhao, RepositorioFuncionario repFuncionario){
        super(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula);
        this.login = login;
        this.repCliente = repCliente;
        this.repEstoque = repEstoque;
        this.repPatio = repPatio;
        this.repCaminhao = repCaminhao;
        this.repFuncionario = repFuncionario; // Utiliza a instância fornecida pela Fachada
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        if (!loginCadastro.equals(this.login)) {
            throw new SecurityException("Apenas o administrador com permissão pode cadastrar funcionários.");
        }
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário a ser cadastrado não pode ser nulo.");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo.");
        }
        for (Funcionario f : repFuncionario.listarTodos()){
            if (f.getCpf().equals(funcionario.getCpf())){
                throw new CpfJaExistenteException("O CPF já está cadastrado.");
            }
        }
        if (repFuncionario.cadastrar(funcionario)){
            funcionario.setCadastrado(true);
        }
    }

    public void cadastrarMotorista(Motorista motorista) {
        this.cadastrarFuncionario(motorista);
    }

    public void cadastrarCaminhao(Caminhao caminhao) {
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas o adminitrador com permissao pode cadastrar caminhoes");
        }
        if(caminhao == null){
            throw new IllegalArgumentException("O caminhao a ser cadastrado nao pode ser null");
        }
        if (repCaminhao.buscarPorPlaca(caminhao.getPlaca()) != null){
            throw new CaminhaoJaExisteException("Caminhao ja cadastrado");
        }
        if(repCaminhao.cadastrar(caminhao)){
            System.out.println("Caminhao cadastrado com sucesso");
        }
    }

    public void cadastrarCliente(Cliente cliente) {
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas o administrador pode cadastrar novos clientes");
        }
        if (cliente == null){
            throw new IllegalArgumentException("Cliente invalido.");
        }
        if (repCliente.buscarPorCpf(cliente.getCpf()) != null) {
            throw new CpfJaExistenteException("Cliente já cadastrado");
        }

        cliente.setCadastrado(true);
        if (repCliente.cadastrar(cliente)) {
            System.out.println("Cliente cadastrado com sucesso!");
        }
    }

    public void cadastrarProduto(Produto produto, Estoque estoque){
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas administradores com permissao podem cadastrar um produto");
        }
        if (produto == null){
            throw new IllegalArgumentException("Produto inválido");
        }
        estoque.cadastrarProduto(produto);
        repEstoque.salvar(estoque.getProdutos());
        produto.setCadastrado(true);
        System.out.println("Produto cadastrado e salvo no estoque.");
    }

    public void permitirEntrada(Caminhao caminhao, Patio patio) throws VagaInsuficienteException {
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas administradores com autorizacao podem permitir a entrada de caminhoes");
        }
        if (caminhao == null){
            throw new IllegalArgumentException("Caminhão inválido.");
        }
        if(patio == null){
            throw new IllegalArgumentException("Patio inválido.");
        }
        if(!caminhao.getCadastrado()){
            throw new CaminhaoNaoCadastradoException("caminhao nao esta cadastrado");
        }
        boolean entrou = patio.adicionarCaminhao(caminhao);

        if(entrou){
            System.out.println("caminhao entrou no patio.");
            caminhao.entrarPatio(patio, caminhao);

        }else{
            System.out.println("o caminhao foi pra fila de espera de entrada no patio");
        }
    }

    public void adicionarNaFilaSaida(Caminhao caminhao, Patio patio){
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas administradores com autorizacao podem permitir a saida de caminhoes");
        }
        if (caminhao == null){
            throw new IllegalArgumentException("Caminhão inválido.");
        }
        if(patio == null){
            throw new IllegalArgumentException("Patio inválido.");
        }
        patio.adicionarFilaSaida(caminhao);
    }

    public void permitirSaida(Caminhao caminhao, Patio patio){
        if(!loginCadastro.equals(this.login)){
            throw new SecurityException("Apenas administradores com autorizacao podem permitir a saida de caminhoes");
        }
        if (caminhao == null){
            throw new IllegalArgumentException("Caminhão inválido.");
        }
        if(patio == null){
            throw new IllegalArgumentException("Patio inválido.");
        }
        if(patio.liberarSaida(caminhao)){
            caminhao.sairPatio(patio);
            System.out.println("caminhao saiu do patio.");

        }
    }

    public void atualizarPreco(Produto produto, double novoPreco){
        if(novoPreco<0){
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        } else if (novoPreco==0){
            throw new IllegalArgumentException("O produto não pode custar 0");
        }
        produto.setPreco(novoPreco);
    }

    public void ponto(String matricula){
        baterPonto(matricula);
    }
}