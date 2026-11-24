package model;

import enums.StatusRobo;
import interfaces.Comunicavel;
import java.io.Serializable;

public abstract class Robo implements Comunicavel, Serializable {

    private static final long serialVersionUID = 7054718344900730182L;

    /** Com este nível (em %) ou menos, o robô é considerado sem bateria. */
    public static final int BATERIA_MINIMA = 20;

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

    /**
     * Coloca o robô em missão e consome a bateria.
     *
     * @return true se a missão foi iniciada; false se o robô não pode executá-la
     */
    public abstract boolean executarMissao();

    /** Percentual de bateria gasto em cada missão. */
    public abstract int getConsumoPorMissao();

    public abstract String getTipo();

    public boolean podeExecutarMissao() {
        return verificarDisponibilidade()
                && nivelBateria > BATERIA_MINIMA
                && nivelBateria >= getConsumoPorMissao();
    }

    /** Libera o robô ao final (conclusão ou cancelamento) de uma missão. */
    public void finalizarMissao() {

        if (status == StatusRobo.EM_MISSAO) {
            atualizarStatusPelaBateria();
        }
    }

    public int consultarBateria() {
        return nivelBateria;
    }

    public boolean recarregar() {

        if (status == StatusRobo.EM_MISSAO) {

            System.out.println("Não é possível recarregar um robô em missão.");
            return false;
        }

        nivelBateria = 100;

        // Recarregar não tira o robô da manutenção.
        if (status != StatusRobo.EM_MANUTENCAO) {
            status = StatusRobo.DISPONIVEL;
        }

        System.out.println("Robô recarregado com sucesso!");
        return true;
    }

    public boolean enviarManutencao() {

        if (status == StatusRobo.EM_MISSAO) {

            System.out.println(
                    "Não é possível enviar para manutenção um robô em missão."
            );
            return false;
        }

        if (status == StatusRobo.EM_MANUTENCAO) {

            System.out.println("O robô já está em manutenção.");
            return false;
        }

        status = StatusRobo.EM_MANUTENCAO;

        System.out.println("Robô enviado para manutenção!");
        return true;
    }

    public boolean finalizarManutencao() {

        if (status != StatusRobo.EM_MANUTENCAO) {

            System.out.println("O robô não está em manutenção.");
            return false;
        }

        atualizarStatusPelaBateria();

        System.out.println("Manutenção finalizada com sucesso!");
        return true;
    }

    protected void consumirBateria(int quantidade) {
        nivelBateria = Math.max(0, nivelBateria - quantidade);
    }

    private void atualizarStatusPelaBateria() {

        status = nivelBateria <= BATERIA_MINIMA
                ? StatusRobo.SEM_BATERIA
                : StatusRobo.DISPONIVEL;
    }

    public boolean verificarDisponibilidade() {
        return status == StatusRobo.DISPONIVEL;
    }

    public void exibirResumo() {

        System.out.println(
                "ID: " + id +
                        " | Tipo: " + getTipo() +
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
}
