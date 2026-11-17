import enums.NivelRisco;
import enums.TipoMissao;
import enums.TipoTerreno;
import model.*;

import java.util.Scanner;

public class Main {

    public static void voltarMenu(Scanner scanner) {

        System.out.print("\nPressione ENTER para voltar ao menu...");

        while (true) {

            String entrada = scanner.nextLine();

            if (entrada.isEmpty()) {
                break;
            }

            System.out.print("Apenas ENTER para voltar ao menu...");
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CentralResgate central = Persistencia.carregarSistema();

        int opcao = -1;

        do {

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

            System.out.print("\nEscolha uma opção: ");

            String entradaMenu = scanner.nextLine();

            if (entradaMenu.isBlank()) {
                continue;
            }

            try {

                opcao = Integer.parseInt(entradaMenu);

            } catch (NumberFormatException e) {

                System.out.println("\nOpção inválida.");

                voltarMenu(scanner);

                continue;
            }
            switch (opcao) {

                case 1:

                    System.out.print("ID do Drone: ");
                    int idDrone =
                            Integer.parseInt(scanner.nextLine());

                    System.out.print("Nome do Drone: ");
                    String nomeDrone = scanner.nextLine();

                    Drone drone =
                            new Drone(idDrone, nomeDrone);

                    central.cadastrarRobo(drone);

                    System.out.println("\nDrone cadastrado com sucesso!");

                    voltarMenu(scanner);
                    break;

                case 2:

                    System.out.print("ID do Robô: ");
                    int idRobo =
                            Integer.parseInt(scanner.nextLine());

                    System.out.print("Nome do Robô: ");
                    String nomeRobo =
                            scanner.nextLine();

                    System.out.print("Capacidade de carga: ");
                    double carga =
                            Double.parseDouble(scanner.nextLine());

                    RoboTerrestre robo =
                            new RoboTerrestre(
                                    idRobo,
                                    nomeRobo,
                                    carga
                            );

                    central.cadastrarRobo(robo);

                    System.out.println("\nRobô terrestre cadastrado com sucesso!");

                    voltarMenu(scanner);
                    break;

                case 5:

                    System.out.println("\n=== GERENCIAR ROBÔS ===");
                    System.out.println("1 - Consultar Robô por ID");
                    System.out.println("2 - Listar Robôs");
                    System.out.println("3 - Recarregar Robô");
                    System.out.println("4 - Consultar Bateria");
                    System.out.println("5 - Enviar para Manutenção");
                    System.out.println("6 - Finalizar Manutenção");
                    System.out.println("0 - Voltar");

                    System.out.print("\nEscolha uma opção: ");

                    int opcaoRobos =
                            Integer.parseInt(scanner.nextLine());

                    switch (opcaoRobos) {

                        case 1:

                            System.out.print("Informe o ID do robô: ");

                            int idConsultaRobo =
                                    Integer.parseInt(scanner.nextLine());

                            Robo roboConsulta =
                                    central.buscarRoboPorId(idConsultaRobo);

                            if (roboConsulta == null) {

                                System.out.println("Robô não encontrado.");

                            } else {

                                roboConsulta.exibirResumo();
                            }

                            break;

                        case 2:

                            System.out.println("\n=== LISTAR ROBÔS ===");
                            System.out.println("1 - Todos");
                            System.out.println("2 - Disponíveis");
                            System.out.println("3 - Em Manutenção");
                            System.out.println("4 - Sem Bateria");

                            System.out.print("\nEscolha uma opção: ");

                            int opcaoLista =
                                    Integer.parseInt(scanner.nextLine());

                            switch (opcaoLista) {

                                case 1:

                                    if (central.getRobos().isEmpty()) {

                                        System.out.println("Nenhum robô cadastrado.");

                                    } else {

                                        central.listarRobos();
                                    }

                                    break;

                                case 2:

                                    central.listarRobosDisponiveis();

                                    break;

                                case 3:

                                    central.listarRobosManutencao();

                                    break;

                                case 4:

                                    central.listarRobosSemBateria();

                                    break;

                                default:

                                    System.out.println("Opção inválida.");
                            }

                            break;

                        case 3:

                            System.out.print("Informe o ID do robô: ");

                            int idRecarregar =
                                    Integer.parseInt(scanner.nextLine());

                            Robo roboRecarregar =
                                    central.buscarRoboPorId(idRecarregar);

                            if (roboRecarregar == null) {

                                System.out.println("Robô não encontrado.");

                            } else {

                                roboRecarregar.recarregar();

                                System.out.println(
                                        "Robô recarregado com sucesso!"
                                );
                            }

                            break;

                        case 4:

                            System.out.print("Informe o ID do robô: ");

                            int idBateria =
                                    Integer.parseInt(scanner.nextLine());

                            Robo roboBateria =
                                    central.buscarRoboPorId(idBateria);

                            if (roboBateria == null) {

                                System.out.println("Robô não encontrado.");

                            } else {

                                System.out.println(
                                        "Bateria: "
                                                + roboBateria.consultarBateria()
                                                + "%"
                                );
                            }

                            break;

                        case 5:

                            System.out.print("Informe o ID do robô: ");

                            int idManutencao =
                                    Integer.parseInt(scanner.nextLine());

                            Robo roboManutencao =
                                    central.buscarRoboPorId(idManutencao);

                            if (roboManutencao == null) {

                                System.out.println("Robô não encontrado.");

                            } else {

                                roboManutencao.enviarManutencao();

                                System.out.println(
                                        "Robô enviado para manutenção!"
                                );
                            }

                            break;

                        case 6:

                            System.out.print("Informe o ID do robô: ");

                            int idFinalizar =
                                    Integer.parseInt(scanner.nextLine());

                            Robo roboFinalizar =
                                    central.buscarRoboPorId(idFinalizar);

                            if (roboFinalizar == null) {

                                System.out.println("Robô não encontrado.");

                            } else {

                                roboFinalizar.finalizarManutencao();

                                System.out.println(
                                        "Manutenção finalizada com sucesso!"
                                );
                            }

                            break;

                        case 0:

                            break;

                        default:

                            System.out.println("Opção inválida.");
                    }

                    voltarMenu(scanner);

                    break;

                case 3:

                    System.out.print("ID da Área: ");

                    int idArea =
                            Integer.parseInt(scanner.nextLine());

                    System.out.print("Nome da Área: ");

                    String nomeArea =
                            scanner.nextLine();

                    System.out.print("Localização: ");

                    String localizacao =
                            scanner.nextLine();

                    System.out.println("\n=== TIPO DE TERRENO ===");
                    System.out.println("1 - URBANO");
                    System.out.println("2 - FLORESTA");
                    System.out.println("3 - MONTANHA");
                    System.out.println("4 - DESERTO");

                    System.out.print("Escolha: ");

                    int opcaoTerrenoArea =
                            Integer.parseInt(scanner.nextLine());

                    TipoTerreno tipoTerreno;

                    switch (opcaoTerrenoArea) {

                        case 1:
                            tipoTerreno = TipoTerreno.URBANO;
                            break;

                        case 2:
                            tipoTerreno = TipoTerreno.FLORESTA;
                            break;

                        case 3:
                            tipoTerreno = TipoTerreno.MONTANHA;
                            break;

                        case 4:
                            tipoTerreno = TipoTerreno.DESERTO;
                            break;

                        default:
                            tipoTerreno = TipoTerreno.URBANO;
                    }

                    System.out.println("\n=== NÍVEL DE RISCO ===");
                    System.out.println("1 - BAIXO");
                    System.out.println("2 - MEDIO");
                    System.out.println("3 - ALTO");

                    System.out.print("Escolha: ");

                    int opcaoRiscoArea =
                            Integer.parseInt(scanner.nextLine());

                    NivelRisco nivelRisco;

                    switch (opcaoRiscoArea) {

                        case 1:
                            nivelRisco = NivelRisco.BAIXO;
                            break;

                        case 2:
                            nivelRisco = NivelRisco.MEDIO;
                            break;

                        case 3:
                            nivelRisco = NivelRisco.ALTO;
                            break;

                        default:
                            nivelRisco = NivelRisco.MEDIO;
                    }

                    AreaOperacao novaArea =
                            new AreaOperacao(
                                    idArea,
                                    nomeArea,
                                    localizacao,
                                    tipoTerreno,
                                    nivelRisco,
                                    "Área cadastrada pelo usuário"
                            );

                    central.cadastrarArea(novaArea);

                    System.out.println("\nÁrea cadastrada com sucesso!");

                    voltarMenu(scanner);

                    break;

                case 6:

                    System.out.println("\n=== GERENCIAR ÁREAS ===");
                    System.out.println("1 - Consultar Área por ID");
                    System.out.println("2 - Listar Áreas");
                    System.out.println("3 - Filtrar por Risco");
                    System.out.println("4 - Filtrar por Terreno");
                    System.out.println("0 - Voltar");

                    System.out.print("\nEscolha uma opção: ");

                    int opcaoArea =
                            Integer.parseInt(scanner.nextLine());

                    switch (opcaoArea) {

                        case 1:

                            System.out.print("Informe o ID da área: ");

                            int idConsultaArea =
                                    Integer.parseInt(scanner.nextLine());

                            AreaOperacao areaConsulta =
                                    central.buscarAreaPorId(idConsultaArea);

                            if (areaConsulta == null) {

                                System.out.println("Área não encontrada.");

                            } else {

                                areaConsulta.exibirInformacoes();
                            }

                            break;

                        case 2:

                            if (central.getAreas().isEmpty()) {

                                System.out.println("Nenhuma área cadastrada.");

                            } else {

                                central.listarAreas();
                            }

                            break;

                        case 3:

                            System.out.println("\n=== FILTRAR POR RISCO ===");
                            System.out.println("1 - BAIXO");
                            System.out.println("2 - MEDIO");
                            System.out.println("3 - ALTO");

                            System.out.print("\nEscolha uma opção: ");

                            int opcaoRisco =
                                    Integer.parseInt(scanner.nextLine());

                            NivelRisco riscoSelecionado = null;

                            switch (opcaoRisco) {

                                case 1:
                                    riscoSelecionado = NivelRisco.BAIXO;
                                    break;

                                case 2:
                                    riscoSelecionado = NivelRisco.MEDIO;
                                    break;

                                case 3:
                                    riscoSelecionado = NivelRisco.ALTO;
                                    break;

                                default:
                                    System.out.println("Opção inválida.");
                            }

                            if (riscoSelecionado != null) {

                                boolean encontrou = false;

                                for (AreaOperacao area : central.getAreas()) {

                                    if (area.getNivelRisco() == riscoSelecionado) {

                                        area.exibirInformacoes();
                                        encontrou = true;
                                    }
                                }

                                if (!encontrou) {

                                    System.out.println(
                                            "Nenhuma área encontrada com este nível de risco."
                                    );
                                }
                            }

                            break;

                        case 4:

                            System.out.println("\n=== FILTRAR POR TERRENO ===");
                            System.out.println("1 - URBANO");
                            System.out.println("2 - FLORESTA");
                            System.out.println("3 - MONTANHA");
                            System.out.println("4 - DESERTO");

                            System.out.print("\nEscolha uma opção: ");

                            int opcaoTerreno =
                                    Integer.parseInt(scanner.nextLine());

                            TipoTerreno terrenoSelecionado = null;

                            switch (opcaoTerreno) {

                                case 1:
                                    terrenoSelecionado = TipoTerreno.URBANO;
                                    break;

                                case 2:
                                    terrenoSelecionado = TipoTerreno.FLORESTA;
                                    break;

                                case 3:
                                    terrenoSelecionado = TipoTerreno.MONTANHA;
                                    break;

                                case 4:
                                    terrenoSelecionado = TipoTerreno.DESERTO;
                                    break;

                                default:
                                    System.out.println("Opção inválida.");
                            }

                            if (terrenoSelecionado != null) {

                                boolean encontrou = false;

                                for (AreaOperacao area : central.getAreas()) {

                                    if (area.getTipoTerreno() == terrenoSelecionado) {

                                        area.exibirInformacoes();
                                        encontrou = true;
                                    }
                                }

                                if (!encontrou) {

                                    System.out.println(
                                            "Nenhuma área encontrada com este tipo de terreno."
                                    );
                                }
                            }

                            break;

                        case 0:

                            break;

                        default:

                            System.out.println("Opção inválida.");
                    }

                    voltarMenu(scanner);

                    break;

                case 4:

                    if (central.getAreas().isEmpty()) {

                        System.out.println("Cadastre uma área primeiro.");

                        voltarMenu(scanner);

                        break;
                    }

                    if (central.getRobos().isEmpty()) {

                        System.out.println("Cadastre um robô primeiro.");

                        voltarMenu(scanner);

                        break;
                    }

                    System.out.print("ID da Missão: ");

                    int idMissao =
                            Integer.parseInt(scanner.nextLine());

                    System.out.print("Descrição da Missão: ");

                    String descricao =
                            scanner.nextLine();

                    System.out.print("ID da Área: ");

                    int areaId =
                            Integer.parseInt(scanner.nextLine());

                    AreaOperacao areaSelecionada =
                            central.buscarAreaPorId(areaId);

                    if (areaSelecionada == null) {

                        System.out.println("Área não encontrada.");
                        voltarMenu(scanner);

                        break;
                    }

                    Missao missao =
                            new Missao(
                                    idMissao,
                                    descricao,
                                    TipoMissao.BUSCA,
                                    areaSelecionada
                            );

                    Robo roboSelecionado = null;

                    for (Robo roboDisponivel : central.getRobos()) {

                        if (roboDisponivel.verificarDisponibilidade()) {

                            roboSelecionado = roboDisponivel;
                            break;
                        }

                    }

                    if (roboSelecionado == null) {

                        System.out.println(
                                "Nenhum robô disponível para esta missão."
                        );

                        voltarMenu(scanner);

                        break;
                    }

                    missao.atribuirRobo(roboSelecionado);

                    central.cadastrarMissao(missao);

                    System.out.println("\nMissão criada com sucesso!");

                    System.out.println(
                            "Robô atribuído automaticamente: "
                                    + roboSelecionado.getNome()
                    );

                    voltarMenu(scanner);

                    break;

                case 7:

                    System.out.println("\n=== GERENCIAR MISSÕES ===");
                    System.out.println("1 - Consultar Missão por ID");
                    System.out.println("2 - Listar Missões");
                    System.out.println("3 - Executar Missão");
                    System.out.println("4 - Concluir Missão");
                    System.out.println("5 - Cancelar Missão");
                    System.out.println("0 - Voltar");

                    System.out.print("\nEscolha uma opção: ");

                    int opcaoMissao =
                            Integer.parseInt(scanner.nextLine());

                    switch (opcaoMissao) {

                        case 1:

                            System.out.print("\nInforme o ID da missão: ");

                            int idConsultaMissao =
                                    Integer.parseInt(scanner.nextLine());

                            Missao missaoConsulta =
                                    central.buscarMissaoPorId(idConsultaMissao);

                            if (missaoConsulta == null) {

                                System.out.println("Missão não encontrada.");

                            } else {

                                missaoConsulta.exibirResumo();
                            }

                            break;

                        case 2:

                            System.out.println("\n=== LISTAR MISSÕES ===");
                            System.out.println("1 - Todas");
                            System.out.println("2 - Pendentes");

                            System.out.print("\nEscolha uma opção: ");

                            int opcaoListaMissao =
                                    Integer.parseInt(scanner.nextLine());

                            switch (opcaoListaMissao) {

                                case 1:

                                    if (central.getMissoes().isEmpty()) {

                                        System.out.println(
                                                "Nenhuma missão cadastrada."
                                        );

                                    } else {

                                        central.listarMissoes();
                                    }

                                    break;

                                case 2:

                                    central.listarMissoesPendentes();

                                    break;

                                default:

                                    System.out.println("Opção inválida.");
                            }

                            break;

                        case 3:

                            System.out.print(
                                    "\nInforme o ID da missão: "
                            );

                            int idExecutar =
                                    Integer.parseInt(scanner.nextLine());

                            Missao missaoExecutar =
                                    central.buscarMissaoPorId(idExecutar);

                            if (missaoExecutar == null) {

                                System.out.println(
                                        "Missão não encontrada."
                                );

                            } else {

                                missaoExecutar.iniciarMissao();
                            }

                            break;

                        case 4:

                            if (central.getMissoes().isEmpty()) {

                                System.out.println(
                                        "Nenhuma missão cadastrada."
                                );

                                break;
                            }

                            central.listarMissoes();

                            System.out.print(
                                    "\nInforme o ID da missão: "
                            );

                            int idConcluir =
                                    Integer.parseInt(scanner.nextLine());

                            Missao missaoConcluir =
                                    central.buscarMissaoPorId(idConcluir);

                            if (missaoConcluir == null) {

                                System.out.println(
                                        "Missão não encontrada."
                                );

                            } else {

                                missaoConcluir.concluirMissao();

                                central.removerMissaoPendente(
                                        missaoConcluir
                                );

                                System.out.println(
                                        "Missão concluída com sucesso!"
                                );
                            }

                            break;

                        case 5:

                            if (central.getMissoes().isEmpty()) {

                                System.out.println(
                                        "Nenhuma missão cadastrada."
                                );

                                break;
                            }

                            central.listarMissoes();

                            System.out.print(
                                    "\nInforme o ID da missão para cancelar: "
                            );

                            int idMissaoCancelar =
                                    Integer.parseInt(scanner.nextLine());

                            Missao missaoParaCancelar =
                                    central.buscarMissaoPorId(
                                            idMissaoCancelar
                                    );

                            if (missaoParaCancelar == null) {

                                System.out.println(
                                        "Missão não encontrada."
                                );

                            } else {

                                missaoParaCancelar.cancelarMissao();

                                central.removerMissaoPendente(
                                        missaoParaCancelar
                                );

                                System.out.println(
                                        "Missão cancelada com sucesso!"
                                );
                            }

                            break;

                        case 0:

                            break;

                        default:

                            System.out.println("Opção inválida.");
                    }

                    voltarMenu(scanner);

                    break;

                case 8:

                    Relatorio relatorio =
                            new Relatorio(central);

                    relatorio.gerarResumo();

                    voltarMenu(scanner);

                    break;


                case 9:

                    Relatorio relatorioArquivo =
                            new Relatorio(central);

                    Persistencia.salvarRelatorio(
                            relatorioArquivo
                    );

                    System.out.println(
                            "\nRelatório salvo com sucesso!"
                    );

                    voltarMenu(scanner);

                    break;


                case 0:

                    Persistencia.salvarSistema(central);

                    System.out.println(
                            "\nEncerrando sistema..."
                    );

                    break;

                default:

                    System.out.println(
                            "\nOpção inválida."
                    );

                    voltarMenu(scanner);
            }

        } while (opcao != 0);

        scanner.close();
    }
}