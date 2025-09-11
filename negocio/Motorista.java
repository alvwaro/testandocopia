package negocio;

import java.io.Serializable;

public class Motorista extends Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String CNH;
    private Caminhao caminhaoResponsavel;
    private boolean cadastrado = false;

    public Motorista(String CNH, Caminhao caminhaoResponsavel, String cargo, double salario, String nome, int idade, String cpf, String telefone, String endereco, String email, String matricula, String senha, PerfilUsuario perfil){
        super(cargo, salario, nome, idade, cpf, telefone, endereco, email, matricula, senha, perfil);

        if (CNH == null || !CNH.matches("\\d+")) {
            throw new IllegalArgumentException("CNH inválida. A CNH deve conter apenas números.");
        }
        this.CNH = CNH;
        this.caminhaoResponsavel = caminhaoResponsavel;
    }


    public Caminhao getCaminhaoResponsavel() {
        return caminhaoResponsavel;
    }

    public void setCaminhaoResponsavel(Caminhao caminhaoResponsavel) {
        this.caminhaoResponsavel = caminhaoResponsavel;
    }

    public String getCNH() {
        return CNH;
    }
    public boolean isCadastrado() {
        return cadastrado;
    }
    public void setCadastrado(boolean cadastrado) {
        this.cadastrado = cadastrado;
    }


    public void finalizarViagem(Caminhao caminhaoResponsavel){
        this.caminhaoResponsavel.setStatus("Disponivel");
    }
    public void consertarCaminhao(Caminhao caminhaoResponsavel){
        this.caminhaoResponsavel.setStatus("Em conserto");
    }
    public void ponto(String matricula){
        baterPonto(matricula);
    }

}
