package util;

import java.io.Console;
import java.nio.charset.Charset;
import java.util.Scanner;

/** Leitura do teclado: repete a pergunta até receber um valor válido. */
public class Entrada {

    private final Scanner scanner;

    public Entrada() {

        // No terminal do Windows a codificação pode não ser UTF-8; sem isto,
        // nomes com acento seriam lidos errado.
        Console console = System.console();

        Charset charset = console != null
                ? console.charset()
                : Charset.defaultCharset();

        scanner = new Scanner(System.in, charset);
    }

    public String lerTexto(String mensagem) {

        while (true) {

            System.out.print(mensagem);

            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    public int lerInteiro(String mensagem) {

        while (true) {

            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    /** Lê o ID de um novo cadastro. */
    public int lerId(String mensagem) {

        while (true) {

            int id = lerInteiro(mensagem);

            if (id > 0) {
                return id;
            }

            System.out.println("O ID deve ser maior que zero.");
        }
    }

    /** Aceita vírgula ou ponto como separador decimal. */
    public double lerDecimalPositivo(String mensagem) {

        while (true) {

            try {

                double valor = Double.parseDouble(
                        lerTexto(mensagem).replace(',', '.')
                );

                if (valor > 0 && Double.isFinite(valor)) {
                    return valor;
                }

            } catch (NumberFormatException e) {
                // Cai na mensagem abaixo e pergunta de novo.
            }

            System.out.println("Digite um número maior que zero.");
        }
    }

    public int lerOpcao(int minimo, int maximo) {

        while (true) {

            int opcao = lerInteiro("\nEscolha uma opção: ");

            if (opcao >= minimo && opcao <= maximo) {
                return opcao;
            }

            System.out.println("Opção inválida.");
        }
    }

    /** Mostra os valores de um enum numerados e devolve o escolhido. */
    public <E extends Enum<E>> E lerEnum(String titulo, Class<E> tipo) {

        E[] valores = tipo.getEnumConstants();

        System.out.println("\n=== " + titulo + " ===");

        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + " - " + valores[i]);
        }

        return valores[lerOpcao(1, valores.length) - 1];
    }

    public void aguardarEnter() {

        System.out.print("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }
}
