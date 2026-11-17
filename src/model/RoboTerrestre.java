package model;

import enums.StatusRobo;

public class RoboTerrestre extends Robo {

    private double capacidadeCarga;

    public RoboTerrestre(int id, String nome, double capacidadeCarga) {
        super(id, nome);
        this.capacidadeCarga = capacidadeCarga;
    }

    @Override
    public void executarMissao() {

        if (status == StatusRobo.EM_MANUTENCAO) {
            System.out.println("Robô em manutenção.");
            return;
        }

        if (nivelBateria <= 20) {
            System.out.println("Bateria insuficiente.");
            return;
        }

        status = StatusRobo.EM_MISSAO;

        System.out.println(nome + " iniciou uma missão terrestre.");

        enviarMensagem("Missão terrestre iniciada.");

        nivelBateria -= 30;

        if (nivelBateria <= 20) {
            status = StatusRobo.SEM_BATERIA;
        } else {
            status = StatusRobo.DISPONIVEL;
        }
    }

    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }
}