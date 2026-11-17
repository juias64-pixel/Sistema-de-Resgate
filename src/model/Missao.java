package model;

import enums.StatusMissao;
import enums.TipoMissao;
import java.io.Serializable;

public class Missao implements Serializable {

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

    public void iniciarMissao() {

        if (roboResponsavel == null) {

            System.out.println("Nenhum robô atribuído.");
            return;
        }

        if (status == StatusMissao.CONCLUIDA) {

            System.out.println("Esta missão já foi concluída.");
            return;
        }

        status = StatusMissao.EM_ANDAMENTO;

        roboResponsavel.enviarMensagem(
                "Missão iniciada: " + descricao
        );

        roboResponsavel.executarMissao();

        roboResponsavel.receberMensagem(
                "Central confirma execução da missão."
        );

        roboResponsavel.consumirBateria(20);

        System.out.println("Missão iniciada com sucesso.");

        System.out.println(
                "Bateria restante: "
                        + roboResponsavel.getNivelBateria()
                        + "%"
        );
    }

    public void concluirMissao() {

        if (status == StatusMissao.CONCLUIDA) {

            System.out.println("Esta missão já está concluída.");
            return;

        }

        if (roboResponsavel != null) {

            roboResponsavel.enviarMensagem(
                    "Missão concluída: " + descricao
            );
        }


        status = StatusMissao.CONCLUIDA;

        System.out.println("Missão concluída.");
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

        if (roboResponsavel != null) {
            System.out.println("Robô: " + roboResponsavel.getNome());
        }

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

    public void cancelarMissao() {

        if (status == StatusMissao.CONCLUIDA) {

            System.out.println("Não é possível cancelar uma missão concluída.");
            return;
        }

        status = StatusMissao.CANCELADA;

        if (roboResponsavel != null) {

            roboResponsavel.receberMensagem(
                    "Missão cancelada pela central."
            );
        }

        System.out.println("Missão cancelada.");
    }
}