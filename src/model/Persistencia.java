package model;

import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Persistencia {

    public static void salvarSistema(
            CentralResgate central) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    "sistema.dat"
                            )
                    );

            out.writeObject(central);

            out.close();

            System.out.println(
                    "Dados salvos com sucesso!"
            );

        } catch (IOException e) {

            System.out.println(
                    "Erro ao salvar dados."
            );
        }
    }

    public static CentralResgate carregarSistema() {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(
                                    "sistema.dat"
                            )
                    );

            CentralResgate central =
                    (CentralResgate) in.readObject();

            in.close();

            return central;

        } catch (Exception e) {

            return new CentralResgate();
        }
    }

    public static void salvarRelatorio(Relatorio relatorio) {

        try {

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter("relatorio.txt"));

            writer.write("=== RELATORIO DO SISTEMA ===");
            writer.newLine();

            writer.newLine();
            writer.write("--- RESUMO GERAL ---");
            writer.newLine();

            writer.write("Total de Robos: " +
                    relatorio.getTotalRobos());
            writer.newLine();

            writer.write("Total de Areas: " +
                    relatorio.getTotalAreas());
            writer.newLine();

            writer.write("Total de Missoes: " +
                    relatorio.getTotalMissoes());
            writer.newLine();

            writer.newLine();
            writer.write("--- MISSOES ---");
            writer.newLine();

            writer.write("Missoes Concluidas: " +
                    relatorio.getMissoesConcluidas());
            writer.newLine();

            writer.write("Missoes Pendentes: " +
                    relatorio.getMissoesPendentes());
            writer.newLine();

            writer.write("Missoes Canceladas: " +
                    relatorio.getMissoesCanceladas());
            writer.newLine();

            writer.newLine();
            writer.write("--- ROBOS ---");
            writer.newLine();

            writer.write("Robos Disponiveis: " +
                    relatorio.getRobosDisponiveis());
            writer.newLine();

            writer.write("Robos em Manutencao: " +
                    relatorio.getRobosManutencao());
            writer.newLine();

            writer.write("Robos Sem Bateria: " +
                    relatorio.getRobosSemBateria());
            writer.newLine();

            writer.close();

            System.out.println(
                    "Relatorio salvo com sucesso!"
            );

        } catch (IOException e) {

            System.out.println(
                    "Erro ao salvar arquivo."
            );
        }
    }
}