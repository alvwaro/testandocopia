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
    private LocalDateTime ultimaEntrada;
    private LocalDateTime ultimaSaida;
    private boolean cadastrado = false;

    public Funcionario(String cargo, double salario, String nome, int idade, String cpf, String telefone, String endereco, String email, String matricula) {
        super(nome, idade, cpf, telefone, endereco, email);
        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
    }

    public Funcionario(String matricula){
        super(); // Chama o construtor vazio da classe Pessoa
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
        ultimaSaida = null; // Garante que a saída anterior seja limpa para um novo ciclo
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