package model;

import enums.StatusRobo;

public class RoboTerrestre extends Robo {

    private static final long serialVersionUID = -5670808370081774227L;

    private static final int CONSUMO_POR_MISSAO = 30;

    private double capacidadeCarga;

    public RoboTerrestre(int id, String nome, double capacidadeCarga) {
        super(id, nome);
        this.capacidadeCarga = capacidadeCarga;
    }

    @Override
    public boolean executarMissao() {

        if (!podeExecutarMissao()) {
            return false;
        }

        status = StatusRobo.EM_MISSAO;

        System.out.println(nome + " iniciou uma missão terrestre.");

        enviarMensagem("Missão terrestre iniciada.");

        consumirBateria(CONSUMO_POR_MISSAO);

        return true;
    }

    @Override
    public int getConsumoPorMissao() {
        return CONSUMO_POR_MISSAO;
    }

    @Override
    public String getTipo() {
        return "Robô Terrestre (carga: " + capacidadeCarga + " kg)";
    }

    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }
}
