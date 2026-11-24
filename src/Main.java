import enums.NivelRisco;
import enums.TipoMissao;
import enums.TipoTerreno;
import model.AreaOperacao;
import model.CentralResgate;
import model.Drone;
import model.Missao;
import model.Persistencia;
import model.Relatorio;
import model.Robo;
import model.RoboTerrestre;
import util.Entrada;

import java.io.IOException;
import java.util.NoSuchElementException;

public class Main {

    private final Entrada entrada = new Entrada();
    private final CentralResgate central;

    private Main(CentralResgate central) {
        this.central = central;
    }

    public static void main(String[] args) {

        CentralResgate central;

        try {

            central = Persistencia.carregarSistema();

        } catch (IOException e) {

            System.out.println(
                    "Não foi possível preservar os dados existentes: " + e
            );
            return;
        }

        new Main(central).executar();
    }

    private void executar() {

        try {

            int opcao;

            do {

                exibirMenu();
                opcao = entrada.lerOpcao(0, 9);

                boolean pausar = processarOpcao(opcao);

                // Salva a cada operação: fechar a janela não perde os dados.
                boolean salvou = Persistencia.salvarSistema(central);

                if (opcao == 0 && salvou) {
                    System.out.println("Dados salvos com sucesso!");
                }

                if (pausar) {
                    entrada.aguardarEnter();
                }

            } while (opcao != 0);

        } catch (NoSuchElementException e) {

            // A entrada foi encerrada (por exemplo, Ctrl+Z ou Ctrl+D). O que
            // foi concluído já está salvo.
            System.out.println();
        }

        System.out.println("\nEncerrando sistema...");
    }

    private void exibirMenu() {

        System.out.println("\n=== CENTRAL DE RESGATE ===");

        System.out.println("\n--- CADASTROS ---");

        System.out.println("1 - Cadastrar Drone");
        System.out.println("2 - Cadastrar Robô Terrestre");
        System.out.println("3 - Cadastrar Área");
        System.out.println("4 - Criar Missão");

        System.out.println("\n--- GERENCIAMENTO ---");

        System.out.println("5 - Gerenciar Robôs");
        System.out.println("6 - Gerenciar Áreas");
        System.out.println("7 - Gerenciar Missões");

        System.out.println("\n--- RELATÓRIOS ---");

        System.out.println("8 - Gerar Relatório");
        System.out.println("9 - Salvar Relatório");

        System.out.println("\n0 - Sair");
    }

    /** @return true se deve esperar ENTER antes de voltar ao menu */
    private boolean processarOpcao(int opcao) {

        switch (opcao) {

            case 1:
                cadastrarDrone();
                return true;

            case 2:
                cadastrarRoboTerrestre();
                return true;

            case 3:
                cadastrarArea();
                return true;

            case 4:
                criarMissao();
                return true;

            case 5:
                return gerenciarRobos();

            case 6:
                return gerenciarAreas();

            case 7:
                return gerenciarMissoes();

            case 8:
                new Relatorio(central).gerarResumo();
                return true;

            case 9:
                Persistencia.salvarRelatorio(new Relatorio(central));
                return true;

            default:
                return false;
        }
    }

    // ----- Cadastros -----

    private void cadastrarDrone() {

        int id = entrada.lerId("ID do Drone: ");

        if (idRoboEmUso(id)) {
            return;
        }

        String nome = entrada.lerTexto("Nome do Drone: ");

        central.cadastrarRobo(new Drone(id, nome));

        System.out.println("\nDrone cadastrado com sucesso!");
    }

    private void cadastrarRoboTerrestre() {

        int id = entrada.lerId("ID do Robô: ");

        if (idRoboEmUso(id)) {
            return;
        }

        String nome = entrada.lerTexto("Nome do Robô: ");
        double carga = entrada.lerDecimalPositivo("Capacidade de carga (kg): ");

        central.cadastrarRobo(new RoboTerrestre(id, nome, carga));

        System.out.println("\nRobô terrestre cadastrado com sucesso!");
    }

    private boolean idRoboEmUso(int id) {

        if (central.buscarRoboPorId(id) == null) {
            return false;
        }

        System.out.println("Já existe um robô com este ID.");
        return true;
    }

    private void cadastrarArea() {

        int id = entrada.lerId("ID da Área: ");

        if (central.buscarAreaPorId(id) != null) {

            System.out.println("Já existe uma área com este ID.");
            return;
        }

        String nome = entrada.lerTexto("Nome da Área: ");
        String localizacao = entrada.lerTexto("Localização: ");
        String descricao = entrada.lerTexto("Descrição: ");

        TipoTerreno terreno =
                entrada.lerEnum("TIPO DE TERRENO", TipoTerreno.class);

        NivelRisco risco =
                entrada.lerEnum("NÍVEL DE RISCO", NivelRisco.class);

        central.cadastrarArea(
                new AreaOperacao(
                        id, nome, localizacao, terreno, risco, descricao
                )
        );

        System.out.println("\nÁrea cadastrada com sucesso!");
    }

    private void criarMissao() {

        if (central.getAreas().isEmpty()) {

            System.out.println("Cadastre uma área primeiro.");
            return;
        }

        if (central.getRobos().isEmpty()) {

            System.out.println("Cadastre um robô primeiro.");
            return;
        }

        int id = entrada.lerId("ID da Missão: ");

        if (central.buscarMissaoPorId(id) != null) {

            System.out.println("Já existe uma missão com este ID.");
            return;
        }

        String descricao = entrada.lerTexto("Descrição da Missão: ");

        TipoMissao tipo =
                entrada.lerEnum("TIPO DE MISSÃO", TipoMissao.class);

        System.out.println();
        central.listarAreas();

        AreaOperacao area = selecionarArea();

        if (area == null) {
            return;
        }

        Missao missao = new Missao(id, descricao, tipo, area);

        central.cadastrarMissao(missao);

        System.out.println("\nMissão criada com sucesso!");

        if (missao.getRoboResponsavel() == null) {

            System.out.println(
                    "Nenhum robô livre no momento. A missão ficará pendente "
                            + "e receberá um robô ao ser executada."
            );

        } else {

            System.out.println(
                    "Robô atribuído automaticamente: "
                            + missao.getRoboResponsavel().getNome()
            );
        }
    }

    // ----- Robôs -----

    private boolean gerenciarRobos() {

        System.out.println("\n=== GERENCIAR ROBÔS ===");
        System.out.println("1 - Consultar Robô por ID");
        System.out.println("2 - Listar Robôs");
        System.out.println("3 - Recarregar Robô");
        System.out.println("4 - Consultar Bateria");
        System.out.println("5 - Enviar para Manutenção");
        System.out.println("6 - Finalizar Manutenção");
        System.out.println("0 - Voltar");

        int opcao = entrada.lerOpcao(0, 6);

        if (opcao == 0) {
            return false;
        }

        if (opcao == 2) {
            return listarRobos();
        }

        Robo robo = selecionarRobo();

        if (robo == null) {
            return true;
        }

        switch (opcao) {

            case 1:
                robo.exibirResumo();
                break;

            case 3:
                robo.recarregar();
                break;

            case 4:
                System.out.println(
                        "Bateria: " + robo.consultarBateria() + "%"
                );
                break;

            case 5:
                robo.enviarManutencao();
                break;

            case 6:
                robo.finalizarManutencao();
                break;
        }

        return true;
    }

    private boolean listarRobos() {

        System.out.println("\n=== LISTAR ROBÔS ===");
        System.out.println("1 - Todos");
        System.out.println("2 - Disponíveis");
        System.out.println("3 - Em Manutenção");
        System.out.println("4 - Sem Bateria");
        System.out.println("5 - Em Missão");
        System.out.println("0 - Voltar");

        switch (entrada.lerOpcao(0, 5)) {

            case 1:
                central.listarRobos();
                return true;

            case 2:
                central.listarRobosDisponiveis();
                return true;

            case 3:
                central.listarRobosManutencao();
                return true;

            case 4:
                central.listarRobosSemBateria();
                return true;

            case 5:
                central.listarRobosEmMissao();
                return true;

            default:
                return false;
        }
    }

    private Robo selecionarRobo() {

        Robo robo = central.buscarRoboPorId(
                entrada.lerInteiro("Informe o ID do robô: ")
        );

        if (robo == null) {
            System.out.println("Robô não encontrado.");
        }

        return robo;
    }

    // ----- Áreas -----

    private boolean gerenciarAreas() {

        System.out.println("\n=== GERENCIAR ÁREAS ===");
        System.out.println("1 - Consultar Área por ID");
        System.out.println("2 - Listar Áreas");
        System.out.println("3 - Filtrar por Risco");
        System.out.println("4 - Filtrar por Terreno");
        System.out.println("5 - Atualizar Nível de Risco");
        System.out.println("0 - Voltar");

        switch (entrada.lerOpcao(0, 5)) {

            case 1:
                AreaOperacao consultada = selecionarArea();

                if (consultada != null) {
                    consultada.exibirInformacoes();
                }
                return true;

            case 2:
                central.listarAreas();
                return true;

            case 3:
                central.listarAreasPorRisco(
                        entrada.lerEnum("FILTRAR POR RISCO", NivelRisco.class)
                );
                return true;

            case 4:
                central.listarAreasPorTerreno(
                        entrada.lerEnum("FILTRAR POR TERRENO", TipoTerreno.class)
                );
                return true;

            case 5:
                AreaOperacao alterada = selecionarArea();

                if (alterada != null) {

                    alterada.atualizarNivelRisco(
                            entrada.lerEnum("NOVO NÍVEL DE RISCO", NivelRisco.class)
                    );

                    System.out.println("Nível de risco atualizado com sucesso!");
                }
                return true;

            default:
                return false;
        }
    }

    private AreaOperacao selecionarArea() {

        AreaOperacao area = central.buscarAreaPorId(
                entrada.lerInteiro("Informe o ID da área: ")
        );

        if (area == null) {
            System.out.println("Área não encontrada.");
        }

        return area;
    }

    // ----- Missões -----

    private boolean gerenciarMissoes() {

        System.out.println("\n=== GERENCIAR MISSÕES ===");
        System.out.println("1 - Consultar Missão por ID");
        System.out.println("2 - Listar Missões");
        System.out.println("3 - Executar Missão");
        System.out.println("4 - Concluir Missão");
        System.out.println("5 - Cancelar Missão");
        System.out.println("0 - Voltar");

        int opcao = entrada.lerOpcao(0, 5);

        if (opcao == 0) {
            return false;
        }

        if (opcao == 2) {
            return listarMissoes();
        }

        if (central.getMissoes().isEmpty()) {

            System.out.println("Nenhuma missão cadastrada.");
            return true;
        }

        if (opcao != 1) {
            central.listarMissoes();
        }

        Missao missao = selecionarMissao();

        if (missao == null) {
            return true;
        }

        switch (opcao) {

            case 1:
                missao.exibirResumo();
                break;

            case 3:
                central.executarMissao(missao);
                break;

            case 4:
                central.concluirMissao(missao);
                break;

            case 5:
                central.cancelarMissao(missao);
                break;
        }

        return true;
    }

    private boolean listarMissoes() {

        System.out.println("\n=== LISTAR MISSÕES ===");
        System.out.println("1 - Todas");
        System.out.println("2 - Pendentes");
        System.out.println("0 - Voltar");

        switch (entrada.lerOpcao(0, 2)) {

            case 1:
                central.listarMissoes();
                return true;

            case 2:
                central.listarMissoesPendentes();
                return true;

            default:
                return false;
        }
    }

    private Missao selecionarMissao() {

        Missao missao = central.buscarMissaoPorId(
                entrada.lerInteiro("\nInforme o ID da missão: ")
        );

        if (missao == null) {
            System.out.println("Missão não encontrada.");
        }

        return missao;
    }
}
