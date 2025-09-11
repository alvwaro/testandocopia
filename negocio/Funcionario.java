package negocio;

import negocio.exceptions.PontoException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Funcionario extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cargo;
    private double salario;
    private String matricula;
    private String senha;
    private PerfilUsuario perfil;
    private LocalDateTime ultimaEntrada;
    private LocalDateTime ultimaSaida;
    private boolean cadastrado = false;

    public Funcionario(String cargo, double salario, String nome, int idade, String cpf, String telefone, String endereco, String email, String matricula) {
        super(nome, idade, cpf, telefone, endereco, email);

        if (nome == null || nome.trim().isEmpty() || !nome.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Nome inválido. O nome deve conter apenas letras e espaços.");
        }

        if (cpf == null || !cpf.matches("\\d+")) {
            throw new IllegalArgumentException("CPF inválido. O CPF deve conter apenas números.");
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido. O CPF deve ter 11 dígitos.");
        }

        if (telefone == null || !telefone.matches("\\d+")) {
            throw new IllegalArgumentException("Telefone inválido. O telefone deve conter apenas números.");
        }

        if (matricula == null || !matricula.matches("\\d+")) {
            throw new IllegalArgumentException("Matrícula inválida. A matrícula deve conter apenas números.");
        }

        if (salario < 0) {
            throw new IllegalArgumentException("O salário não pode ser negativo.");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("O cargo não pode ser vazio.");
        }

        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
    }

    public Funcionario(String cargo, double salario, String nome, int idade, String cpf, String telefone, String endereco, String email, String matricula, String senha, PerfilUsuario perfil) {
        super(nome, idade, cpf, telefone, endereco, email);

        // CORREÇÃO: Permite a matrícula "admin" como uma exceção à regra de "apenas números"
        if (!"admin".equalsIgnoreCase(matricula) && (matricula == null || !matricula.matches("\\d+"))) {
            throw new IllegalArgumentException("Matrícula inválida. A matrícula deve conter apenas números.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            // Corrigido o texto da exceção que estava com caracteres estranhos
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }

        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
        this.senha = senha;
        this.perfil = perfil;
    }

    // Construtor antigo simplificado (usado internamente pela persistência)
    public Funcionario(String matricula){
        super();
        this.matricula = matricula;
    }

    public boolean getCadastrado() {
        return cadastrado;
    }

    public void setCadastrado(boolean cadastrado) {
        this.cadastrado = cadastrado;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDateTime getUltimaEntrada() {
        return ultimaEntrada;
    }

    public LocalDateTime getUltimaSaida() {
        return ultimaSaida;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    // Setters necessários para a persistência em CSV
    public void setUltimaEntrada(LocalDateTime ultimaEntrada) {
        this.ultimaEntrada = ultimaEntrada;
    }

    public void setUltimaSaida(LocalDateTime ultimaSaida) {
        this.ultimaSaida = ultimaSaida;
    }

    public boolean baterEntrada(String matricula) throws PontoException {
        if(ultimaEntrada != null && ultimaSaida == null){
            throw new PontoException("Já existe uma ENTRADA registrada sem uma SAÍDA correspondente.");
        }
        ultimaEntrada = LocalDateTime.now();
        ultimaSaida = null;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("ENTRADA: " + getNome() + " (mat. " + matricula + ") bateu o ponto às: " + ultimaEntrada.format(fmt));
        return true;
    }

    public boolean baterPontoSaida(String matricula) throws PontoException {
        if(ultimaEntrada == null){
            throw new PontoException("Não é possível registrar uma SAÍDA sem uma ENTRADA prévia.");
        }
        ultimaSaida = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("SAÍDA: " + getNome() + " (mat. " + matricula + ") bateu o ponto às: " + ultimaSaida.format(fmt));

        Duration duracao = Duration.between(ultimaEntrada, ultimaSaida);
        long horas = duracao.toHours();
        long minutos = duracao.toMinutes() % 60;

        System.out.println("Tempo trabalhado no período: " + horas + "h " + minutos + "min");

        // Reseta os pontos para o próximo ciclo de trabalho
        ultimaEntrada = null;
        ultimaSaida = null;
        return true;
    }

    public boolean baterPonto(String matricula) throws PontoException {
        if(ultimaEntrada == null){
            return baterEntrada(matricula);
        }
        else{
            return baterPontoSaida(matricula);
        }
    }
}