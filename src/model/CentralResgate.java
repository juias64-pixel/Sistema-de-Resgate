package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.io.Serializable;

public class CentralResgate implements Serializable {

    private ArrayList<Robo> robos;
    private ArrayList<AreaOperacao> areas;
    private ArrayList<Missao> missoes;
    private Queue<Missao> missoesPendentes;
    private HashMap<Integer, Robo> mapaRobos;

    public CentralResgate() {

        robos = new ArrayList<>();
        areas = new ArrayList<>();
        missoes = new ArrayList<>();
        missoesPendentes = new LinkedList<>();
        mapaRobos = new HashMap<>();
    }

    public void cadastrarRobo(Robo robo) {

        robos.add(robo);

        mapaRobos.put(
                robo.getId(),
                robo
        );
    }

    public void cadastrarArea(AreaOperacao area) {
        areas.add(area);
    }

    public void cadastrarMissao(Missao missao) {

        missoes.add(missao);

        if (missao.getStatus().toString().equals("PENDENTE")) {
            missoesPendentes.add(missao);
        }
    }

    public void listarRobosDisponiveis() {

        List<Robo> robosDisponiveis =
                robos.stream()
                        .filter(Robo::verificarDisponibilidade)
                        .collect(Collectors.toList());

        if (robosDisponiveis.isEmpty()) {

            System.out.println("Nenhum robô disponível.");
            return;
        }

        robosDisponiveis.forEach(Robo::exibirResumo);
    }

    public void listarRobosSemBateria() {

        List<Robo> robosSemBateria =
                robos.stream()
                        .filter(robo -> robo.getNivelBateria() <= 20)
                        .collect(Collectors.toList());

        if (robosSemBateria.isEmpty()) {

            System.out.println("Nenhum robô sem bateria.");
            return;
        }

        robosSemBateria.forEach(Robo::exibirResumo);
    }

    public void listarRobosManutencao() {

        List<Robo> robosManutencao =

                robos.stream()
                        .filter(robo ->
                                robo.getStatus() ==
                                        enums.StatusRobo.EM_MANUTENCAO)
                        .collect(Collectors.toList());

        if (robosManutencao.isEmpty()) {

            System.out.println(
                    "Nenhum robô em manutenção."
            );

            return;
        }

        robosManutencao.forEach(
                Robo::exibirResumo
        );
    }

    public Robo buscarRoboPorId(int id) {
        return mapaRobos.get(id);
    }

    public AreaOperacao buscarAreaPorId(int id) {

        for (AreaOperacao area : areas) {
            if (area.getId() == id) {
                return area;
            }
        }

        return null;
    }

    public Missao buscarMissaoPorId(int id) {

        for (Missao missao : missoes) {
            if (missao.getId() == id) {
                return missao;
            }
        }

        return null;
    }

    public void listarRobos() {

        for (Robo robo : robos) {
            robo.exibirResumo();
        }

    }

    public void listarAreas() {

        for (AreaOperacao area : areas) {
            area.exibirInformacoes();
        }

    }

    public void listarMissoes() {

        for (Missao missao : missoes) {
            missao.exibirResumo();
        }

    }

    public void listarMissoesPendentes() {

        System.out.println("\n=== MISSÕES PENDENTES ===");

        if (missoesPendentes.isEmpty()) {

            System.out.println("Nenhuma missão pendente.");
            return;
        }

        for (Missao missao : missoesPendentes) {
            missao.exibirResumo();
        }
    }

    public void removerMissaoPendente(Missao missao) {
        missoesPendentes.remove(missao);
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }

    public ArrayList<AreaOperacao> getAreas() {
        return areas;
    }

    public ArrayList<Missao> getMissoes() {
        return missoes;
    }

    public Queue<Missao> getMissoesPendentes() {
        return missoesPendentes;
    }

    public HashMap<Integer, Robo> getMapaRobos() {
        return mapaRobos;
    }
}