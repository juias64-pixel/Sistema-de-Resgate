package model;

import enums.StatusMissao;
import enums.StatusRobo;

import java.util.List;

public class Relatorio {

    private final int totalRobos;
    private final int totalAreas;
    private final int totalMissoes;

    private int missoesConcluidas;
    private int missoesEmAndamento;
    private int missoesPendentes;
    private int missoesCanceladas;

    private int robosDisponiveis;
    private int robosEmMissao;
    private int robosManutencao;
    private int robosSemBateria;

    public Relatorio(CentralResgate central) {

        totalRobos = central.getRobos().size();
        totalAreas = central.getAreas().size();
        totalMissoes = central.getMissoes().size();

        for (Missao missao : central.getMissoes()) {
            contarMissao(missao.getStatus());
        }

        for (Robo robo : central.getRobos()) {
            contarRobo(robo.getStatus());
        }
    }

    private void contarMissao(StatusMissao status) {

        switch (status) {

            case CONCLUIDA:
                missoesConcluidas++;
                break;

            case EM_ANDAMENTO:
                missoesEmAndamento++;
                break;

            case PENDENTE:
                missoesPendentes++;
                break;

            case CANCELADA:
                missoesCanceladas++;
                break;
        }
    }

    private void contarRobo(StatusRobo status) {

        switch (status) {

            case DISPONIVEL:
                robosDisponiveis++;
                break;

            case EM_MISSAO:
                robosEmMissao++;
                break;

            case EM_MANUTENCAO:
                robosManutencao++;
                break;

            case SEM_BATERIA:
                robosSemBateria++;
                break;
        }
    }

    /** Conteúdo do relatório, usado tanto na tela quanto no arquivo. */
    public List<String> gerarLinhas() {

        return List.of(
                "=== RELATÓRIO DO SISTEMA ===",
                "",
                "--- RESUMO GERAL ---",
                "Total de Robôs: " + totalRobos,
                "Total de Áreas: " + totalAreas,
                "Total de Missões: " + totalMissoes,
                "",
                "--- MISSÕES ---",
                "Missões Concluídas: " + missoesConcluidas,
                "Missões Em Andamento: " + missoesEmAndamento,
                "Missões Pendentes: " + missoesPendentes,
                "Missões Canceladas: " + missoesCanceladas,
                "",
                "--- ROBÔS ---",
                "Robôs Disponíveis: " + robosDisponiveis,
                "Robôs em Missão: " + robosEmMissao,
                "Robôs em Manutenção: " + robosManutencao,
                "Robôs Sem Bateria: " + robosSemBateria
        );
    }

    public void gerarResumo() {

        System.out.println();
        gerarLinhas().forEach(System.out::println);
    }
}
