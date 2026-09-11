import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArquivoUtil {

    public static <T> List<T> carregar(String caminho, Function<String, T> parser) {
        List<T> lista = new ArrayList<>();
        Path path = Paths.get(caminho);
        if (!Files.exists(path)) {
            return lista;
        }
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) {
                    lista.add(parser.apply(linha));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo " + caminho + ": " + e.getMessage());
        }
        return lista;
    }

    public static <T> void salvar(String caminho, List<T> lista, Function<T, String> serializer) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminho))) {
            for (T item : lista) {
                bw.write(serializer.apply(item));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo " + caminho + ": " + e.getMessage());
        }
    }
}
