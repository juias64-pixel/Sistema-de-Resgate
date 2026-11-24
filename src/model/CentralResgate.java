package model;

import enums.NivelRisco;
import enums.StatusMissao;
import enums.StatusRobo;
import enums.TipoTerreno;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CentralResgate implements Serializable {

    private static final long serialVersionUID = -7137067463068276401L;

    private ArrayList<Robo> robos;
    private ArrayList<AreaOperacao> areas;
    private ArrayList<Missao> missoes;

    // A implementação usada (LinkedList) é serializável.
    @SuppressWarnings("serial")
    private Queue<Missao> missoesPendentes;

    private HashMap<Integer, Robo> mapaRobos;

    public CentralResgate() {

        robos = new ArrayList<>();
        areas = new ArrayList<>();
        missoes = new ArrayList<>();
        missoesPendentes = new LinkedList<>();
        mapaRobos = new HashMap<>();
    }

    /** Corrige a fila de arquivos salvos por versões antigas do sistema. */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        missoesPendentes.removeIf(
                missao -> missao.getStatus() != StatusMissao.PENDENTE
        );
    }

    // ----- Cadastros (retornam false se o ID já existir) -----

    public boolean cadastrarRobo(Robo robo) {

        if (mapaRobos.containsKey(robo.getId())) {
            return false;
        }

        robos.add(robo);
        mapaRobos.put(robo.getId(), robo);
        return true;
    }

    public boolean cadastrarArea(AreaOperacao area) {

        if (buscarAreaPorId(area.getId()) != null) {
            return false;
        }

        areas.add(area);
        return true;
    }

    /**
     * Cadastra a missão e reserva um robô livre para ela. Se não houver robô
     * livre, a missão fica na fila e recebe um robô no momento da execução.
     */
    public boolean cadastrarMissao(Missao missao) {

        if (buscarMissaoPorId(missao.getId()) != null) {
            return false;
        }

        if (missao.getRoboResponsavel() == null) {
            missao.atribuirRobo(buscarRoboLivre());
        }

        missoes.add(missao);

        if (missao.getStatus() == StatusMissao.PENDENTE) {
            missoesPendentes.add(missao);
        }

        return true;
    }

    // ----- Operações de missão -----

    public boolean executarMissao(Missao missao) {

        if (missao.getStatus() == StatusMissao.PENDENTE) {

            Robo atual = missao.getRoboResponsavel();

            if (atual == null || !atual.podeExecutarMissao()) {

                Robo substituto = buscarRoboLivre();

                if (substituto != null) {

                    missao.atribuirRobo(substituto);

                    System.out.println(
                            "Robô atribuído automaticamente: "
                                    + substituto.getNome()
                    );
                }
            }
        }

        boolean iniciou = missao.iniciarMissao();

        if (iniciou) {
            missoesPendentes.remove(missao);
        }

        return iniciou;
    }

    public boolean concluirMissao(Missao missao) {
        return missao.concluirMissao();
    }

    public boolean cancelarMissao(Missao missao) {

        boolean cancelou = missao.cancelarMissao();

        if (cancelou) {
            missoesPendentes.remove(missao);
        }

        return cancelou;
    }

    /** Robô apto a executar uma missão e sem outra missão ativa reservada. */
    private Robo buscarRoboLivre() {

        return robos.stream()
                .filter(Robo::podeExecutarMissao)
                .filter(robo -> missoes.stream().noneMatch(
                        missao -> missao.estaAtiva()
                                && missao.getRoboResponsavel() == robo))
                .findFirst()
                .orElse(null);
    }

    // ----- Buscas -----

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

    // ----- Listagens -----

    public void listarRobos() {
        exibirRobos(robo -> true, "Nenhum robô cadastrado.");
    }

    public void listarRobosDisponiveis() {
        exibirRobos(
                Robo::verificarDisponibilidade,
                "Nenhum robô disponível."
        );
    }

    public void listarRobosSemBateria() {
        exibirRobos(
                robo -> robo.getStatus() == StatusRobo.SEM_BATERIA,
                "Nenhum robô sem bateria."
        );
    }

    public void listarRobosManutencao() {
        exibirRobos(
                robo -> robo.getStatus() == StatusRobo.EM_MANUTENCAO,
                "Nenhum robô em manutenção."
        );
    }

    public void listarRobosEmMissao() {
        exibirRobos(
                robo -> robo.getStatus() == StatusRobo.EM_MISSAO,
                "Nenhum robô em missão."
        );
    }

    private void exibirRobos(Predicate<Robo> filtro, String mensagemVazia) {

        List<Robo> encontrados = robos.stream()
                .filter(filtro)
                .collect(Collectors.toList());

        if (encontrados.isEmpty()) {

            System.out.println(mensagemVazia);
            return;
        }

        encontrados.forEach(Robo::exibirResumo);
    }

    public void listarAreas() {
        exibirAreas(area -> true, "Nenhuma área cadastrada.");
    }

    public void listarAreasPorRisco(NivelRisco risco) {
        exibirAreas(
                area -> area.getNivelRisco() == risco,
                "Nenhuma área encontrada com este nível de risco."
        );
    }

    public void listarAreasPorTerreno(TipoTerreno terreno) {
        exibirAreas(
                area -> area.getTipoTerreno() == terreno,
                "Nenhuma área encontrada com este tipo de terreno."
        );
    }

    private void exibirAreas(
            Predicate<AreaOperacao> filtro,
            String mensagemVazia) {

        List<AreaOperacao> encontradas = areas.stream()
                .filter(filtro)
                .collect(Collectors.toList());

        if (encontradas.isEmpty()) {

            System.out.println(mensagemVazia);
            return;
        }

        encontradas.forEach(AreaOperacao::exibirInformacoes);
    }

    public void listarMissoes() {

        if (missoes.isEmpty()) {

            System.out.println("Nenhuma missão cadastrada.");
            return;
        }

        missoes.forEach(Missao::exibirResumo);
    }

    public void listarMissoesPendentes() {

        System.out.println("\n=== MISSÕES PENDENTES ===");

        if (missoesPendentes.isEmpty()) {

            System.out.println("Nenhuma missão pendente.");
            return;
        }

        missoesPendentes.forEach(Missao::exibirResumo);
    }

    // ----- Consultas somente leitura: alterações passam pelos métodos acima -----

    public List<Robo> getRobos() {
        return Collections.unmodifiableList(robos);
    }

    public List<AreaOperacao> getAreas() {
        return Collections.unmodifiableList(areas);
    }

    public List<Missao> getMissoes() {
        return Collections.unmodifiableList(missoes);
    }
}
