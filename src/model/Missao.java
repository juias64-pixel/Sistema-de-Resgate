package model;

import enums.StatusMissao;
import enums.TipoMissao;
import java.io.Serializable;

public class Missao implements Serializable {

    private static final long serialVersionUID = 3514207644041144457L;

    private int id;
    private String descricao;
    private TipoMissao tipoMissao;
    private AreaOperacao area;
    private Robo roboResponsavel;
    private StatusMissao status;

    public Missao(int id,
                  String descricao,
                  TipoMissao tipoMissao,
                  AreaOperacao area) {

        this.id = id;
        this.descricao = descricao;
        this.tipoMissao = tipoMissao;
        this.area = area;
        this.status = StatusMissao.PENDENTE;
    }

    public void atribuirRobo(Robo robo) {
        this.roboResponsavel = robo;
    }

    /** Uma missão ativa (pendente ou em andamento) mantém o robô reservado. */
    public boolean estaAtiva() {
        return status == StatusMissao.PENDENTE
                || status == StatusMissao.EM_ANDAMENTO;
    }

    public boolean iniciarMissao() {

        if (status != StatusMissao.PENDENTE) {

            System.out.println(
                    "Apenas missões pendentes podem ser executadas. Status atual: "
                            + status
            );
            return false;
        }

        if (roboResponsavel == null) {

            System.out.println("Nenhum robô disponível para esta missão.");
            return false;
        }

        if (!roboResponsavel.executarMissao()) {

            System.out.println(
                    roboResponsavel.getNome()
                            + " não pode executar a missão. Status: "
                            + roboResponsavel.getStatus()
                            + " | Bateria: "
                            + roboResponsavel.getNivelBateria() + "%"
            );
            return false;
        }

        status = StatusMissao.EM_ANDAMENTO;

        roboResponsavel.receberMensagem(
                "Central confirma execução da missão: " + descricao
        );

        System.out.println("Missão iniciada com sucesso!");

        System.out.println(
                "Bateria restante: "
                        + roboResponsavel.getNivelBateria()
                        + "%"
        );

        return true;
    }

    public boolean concluirMissao() {

        if (status != StatusMissao.EM_ANDAMENTO) {

            System.out.println(
                    "Apenas missões em andamento podem ser concluídas. Status atual: "
                            + status
            );
            return false;
        }

        status = StatusMissao.CONCLUIDA;

        if (roboResponsavel != null) {

            roboResponsavel.enviarMensagem("Missão concluída: " + descricao);
            roboResponsavel.finalizarMissao();
        }

        System.out.println("Missão concluída com sucesso!");
        return true;
    }

    public boolean cancelarMissao() {

        if (!estaAtiva()) {

            System.out.println(
                    "Esta missão não pode ser cancelada. Status atual: " + status
            );
            return false;
        }

        boolean estavaEmAndamento = status == StatusMissao.EM_ANDAMENTO;

        status = StatusMissao.CANCELADA;

        if (roboResponsavel != null) {

            roboResponsavel.receberMensagem("Missão cancelada pela central.");

            if (estavaEmAndamento) {
                roboResponsavel.finalizarMissao();
            }
        }

        System.out.println("Missão cancelada com sucesso!");
        return true;
    }

    public void exibirResumo() {

        System.out.println("\n=== MISSÃO ===");

        System.out.println("ID: " + id);
        System.out.println("Descrição: " + descricao);
        System.out.println("Tipo: " + tipoMissao);
        System.out.println("Status: " + status);

        if (area != null) {
            System.out.println("Área: " + area.getNome());
        }

        System.out.println(
                "Robô: "
                        + (roboResponsavel == null
                        ? "não atribuído"
                        : roboResponsavel.getNome())
        );

        System.out.println("-------------------------");
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoMissao getTipoMissao() {
        return tipoMissao;
    }

    public AreaOperacao getArea() {
        return area;
    }

    public Robo getRoboResponsavel() {
        return roboResponsavel;
    }

    public StatusMissao getStatus() {
        return status;
    }
}
