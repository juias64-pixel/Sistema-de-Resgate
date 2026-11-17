package model;

import enums.StatusRobo;

public class Drone extends Robo {

    public Drone(int id, String nome) {
        super(id, nome);
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

        System.out.println(nome + " iniciou uma missão aérea.");

        enviarMensagem("Missão aérea iniciada.");

        nivelBateria -= 20;

        if (nivelBateria <= 20) {
            status = StatusRobo.SEM_BATERIA;
        } else {
            status = StatusRobo.DISPONIVEL;
        }
    }
}