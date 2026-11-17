package model;

import enums.StatusMissao;
import enums.StatusRobo;

public class Relatorio {

    private int totalRobos;
    private int totalAreas;
    private int totalMissoes;

    private int missoesConcluidas;
    private int missoesPendentes;
    private int missoesCanceladas;

    private int robosDisponiveis;
    private int robosManutencao;
    private int robosSemBateria;

    public Relatorio(CentralResgate central) {

        totalRobos = central.getRobos().size();
        totalAreas = central.getAreas().size();
        totalMissoes = central.getMissoes().size();

        missoesConcluidas = 0;
        missoesPendentes = 0;
        missoesCanceladas = 0;

        robosDisponiveis = 0;
        robosManutencao = 0;
        robosSemBateria = 0;

        for (Missao missao : central.getMissoes()) {

            if (missao.getStatus() == StatusMissao.CONCLUIDA) {

                missoesConcluidas++;

            } else if (missao.getStatus() == StatusMissao.CANCELADA) {

                missoesCanceladas++;

            } else {

                missoesPendentes++;

            }
        }

        for (Robo robo : central.getRobos()) {

            if (robo.getStatus() == StatusRobo.DISPONIVEL) {

                robosDisponiveis++;

            } else if (robo.getStatus() == StatusRobo.EM_MANUTENCAO) {

                robosManutencao++;

            } else if (robo.getStatus() == StatusRobo.SEM_BATERIA) {

                robosSemBateria++;

            }
        }
    }

    public void gerarResumo() {

        System.out.println("\n=== RELATÓRIO DO SISTEMA ===");

        System.out.println("\n--- RESUMO GERAL ---");

        System.out.println("Total de Robôs: " + totalRobos);
        System.out.println("Total de Áreas: " + totalAreas);
        System.out.println("Total de Missões: " + totalMissoes);

        System.out.println("\n--- MISSÕES ---");

        System.out.println("Missões Concluídas: " + missoesConcluidas);
        System.out.println("Missões Pendentes: " + missoesPendentes);
        System.out.println("Missões Canceladas: " + missoesCanceladas);

        System.out.println("\n--- ROBÔS ---");

        System.out.println("Robôs Disponíveis: " + robosDisponiveis);
        System.out.println("Robôs em Manutenção: " + robosManutencao);
        System.out.println("Robôs Sem Bateria: " + robosSemBateria);
    }

    public int getTotalRobos() {
        return totalRobos;
    }

    public int getTotalAreas() {
        return totalAreas;
    }

    public int getTotalMissoes() {
        return totalMissoes;
    }

    public int getMissoesConcluidas() {
        return missoesConcluidas;
    }

    public int getMissoesPendentes() {
        return missoesPendentes;
    }

    public int getMissoesCanceladas() {
        return missoesCanceladas;
    }

    public int getRobosDisponiveis() {
        return robosDisponiveis;
    }

    public int getRobosManutencao() {
        return robosManutencao;
    }

    public int getRobosSemBateria() {
        return robosSemBateria;
    }
}