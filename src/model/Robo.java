package model;

import enums.StatusRobo;
import interfaces.Comunicavel;
import java.io.Serializable;

public abstract class Robo implements Comunicavel, Serializable {

    protected int id;
    protected String nome;
    protected int nivelBateria;
    protected StatusRobo status;

    public Robo(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.nivelBateria = 100;
        this.status = StatusRobo.DISPONIVEL;
    }

    public int consultarBateria() {
        return nivelBateria;
    }

    public void recarregar() {

        nivelBateria = 100;

        status = StatusRobo.DISPONIVEL;

        System.out.println(
                "Robô recarregado com sucesso."
        );
    }

    public boolean verificarDisponibilidade() {
        return status == StatusRobo.DISPONIVEL;
    }

    public void exibirResumo() {

        System.out.println(
                "ID: " + id +
                        " | Nome: " + nome +
                        " | Bateria: " + nivelBateria +
                        "% | Status: " + status
        );
    }

    @Override
    public void enviarMensagem(String mensagem) {
        System.out.println(nome + " enviou: " + mensagem);
    }

    @Override
    public void receberMensagem(String mensagem) {
        System.out.println(nome + " recebeu: " + mensagem);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNivelBateria() {
        return nivelBateria;
    }

    public StatusRobo getStatus() {
        return status;
    }

    public abstract void executarMissao();

    public void enviarManutencao() {
        status = StatusRobo.EM_MANUTENCAO;
    }

    public void consumirBateria(int quantidade) {

        nivelBateria -= quantidade;

        if (nivelBateria <= 0) {

            nivelBateria = 0;

            status = StatusRobo.SEM_BATERIA;
        }
    }


    public void verificarBateria() {

        if (nivelBateria <= 0) {

            nivelBateria = 0;

            status = StatusRobo.SEM_BATERIA;
        }
    }

    public void finalizarManutencao() {
        status = StatusRobo.DISPONIVEL;
    }
}
