package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Persistencia {

    private static final Path ARQUIVO_DADOS = Path.of("sistema.dat");
    private static final Path ARQUIVO_TEMPORARIO = Path.of("sistema.dat.tmp");
    private static final Path ARQUIVO_RELATORIO = Path.of("relatorio.txt");

    private Persistencia() {
    }

    public static boolean salvarSistema(CentralResgate central) {

        try {

            // Grava em um arquivo temporário e só depois substitui o original,
            // para que uma falha no meio da gravação não destrua os dados.
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(ARQUIVO_TEMPORARIO))) {

                out.writeObject(central);
            }

            Files.move(
                    ARQUIVO_TEMPORARIO,
                    ARQUIVO_DADOS,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return true;

        } catch (IOException e) {

            System.out.println("Erro ao salvar dados: " + e);
            return false;
        }
    }

    /**
     * Carrega os dados salvos. Sem arquivo, começa um sistema vazio; com um
     * arquivo ilegível, guarda uma cópia dele antes de começar vazio.
     *
     * @throws IOException se o arquivo ilegível não puder ser preservado
     */
    public static CentralResgate carregarSistema() throws IOException {

        if (!Files.exists(ARQUIVO_DADOS)) {
            return new CentralResgate();
        }

        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(ARQUIVO_DADOS))) {

            return (CentralResgate) in.readObject();

        } catch (IOException | ClassNotFoundException | RuntimeException e) {

            System.out.println(
                    "Não foi possível ler " + ARQUIVO_DADOS + ": " + e
            );
        }

        Path copia = Path.of(
                "sistema-corrompido-" + System.currentTimeMillis() + ".dat"
        );

        Files.copy(ARQUIVO_DADOS, copia);

        System.out.println(
                "Uma cópia foi guardada em " + copia
                        + ". Iniciando com o sistema vazio."
        );

        return new CentralResgate();
    }

    public static boolean salvarRelatorio(Relatorio relatorio) {

        try (BufferedWriter writer = Files.newBufferedWriter(
                ARQUIVO_RELATORIO, StandardCharsets.UTF_8)) {

            for (String linha : relatorio.gerarLinhas()) {

                writer.write(linha);
                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println("Erro ao salvar relatório: " + e);
            return false;
        }

        System.out.println(
                "\nRelatório salvo com sucesso em " + ARQUIVO_RELATORIO + "!"
        );
        return true;
    }
}
