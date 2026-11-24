package model;

import enums.NivelRisco;
import enums.TipoTerreno;
import java.io.Serializable;

public class AreaOperacao implements Serializable {

    private static final long serialVersionUID = 2527111315753526991L;

    private int id;
    private String nome;
    private String localizacao;
    private TipoTerreno tipoTerreno;
    private NivelRisco nivelRisco;
    private String descricao;

    public AreaOperacao(
            int id,
            String nome,
            String localizacao,
            TipoTerreno tipoTerreno,
            NivelRisco nivelRisco,
            String descricao) {

        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.tipoTerreno = tipoTerreno;
        this.nivelRisco = nivelRisco;
        this.descricao = descricao;
    }

    public void atualizarNivelRisco(NivelRisco novoNivel) {
        this.nivelRisco = novoNivel;
    }

    public void exibirInformacoes() {

        System.out.println(
                "ID: " + id +
                        " | Nome: " + nome +
                        " | Localização: " + localizacao +
                        " | Terreno: " + tipoTerreno +
                        " | Risco: " + nivelRisco +
                        " | Descrição: " + descricao
        );
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public String getDescricao() {
        return descricao;
    }
}
