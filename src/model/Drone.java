package model;

import enums.StatusRobo;

public class Drone extends Robo {

    private static final long serialVersionUID = 4026269054462051556L;

    private static final int CONSUMO_POR_MISSAO = 20;

    public Drone(int id, String nome) {
        super(id, nome);
    }

    @Override
    public boolean executarMissao() {

        if (!podeExecutarMissao()) {
            return false;
        }

        status = StatusRobo.EM_MISSAO;

        System.out.println(nome + " iniciou uma missão aérea.");

        enviarMensagem("Missão aérea iniciada.");

        consumirBateria(CONSUMO_POR_MISSAO);

        return true;
    }

    @Override
    public int getConsumoPorMissao() {
        return CONSUMO_POR_MISSAO;
    }

    @Override
    public String getTipo() {
        return "Drone";
    }
}
