import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Venda {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private int id;
    private LocalDateTime dataHora;
    private List<ItemVenda> itens;
    private BigDecimal percentualDesconto; // 0 a 100

    public Venda(int id, LocalDateTime dataHora, List<ItemVenda> itens, BigDecimal percentualDesconto) {
        this.id = id;
        this.dataHora = dataHora;
        this.itens = itens;
        this.percentualDesconto = percentualDesconto;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public BigDecimal subtotal() {
        return itens.stream().map(ItemVenda::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal valorDesconto() {
        return subtotal().multiply(percentualDesconto).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal total() {
        return subtotal().subtract(valorDesconto());
    }

    public String gerarRecibo() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO - VENDA #").append(id).append(" =====\n");
        sb.append("Data: ").append(dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        sb.append("-------------------------------------------\n");
        itens.forEach(i -> sb.append(i).append("\n"));
        sb.append("-------------------------------------------\n");
        sb.append(String.format("Subtotal:  R$ %.2f%n", subtotal()));
        if (percentualDesconto.compareTo(BigDecimal.ZERO) > 0) {
            sb.append(String.format("Desconto (%s%%): -R$ %.2f%n", percentualDesconto, valorDesconto()));
        }
        sb.append(String.format("TOTAL:     R$ %.2f%n", total()));
        sb.append("=============================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Venda #%d | %s | %d item(ns) | Total: R$ %.2f",
                id, dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), itens.size(), total());
    }

    public String toCsvLine() {
        String itensStr = itens.stream().map(ItemVenda::toCsvField).collect(Collectors.joining("|"));
        return id + ";" + dataHora.format(FMT) + ";" + percentualDesconto.toPlainString() + ";" + itensStr;
    }

    public static Venda fromCsvLine(String linha) {
        String[] c = linha.split(";", 4);
        List<ItemVenda> itens = new ArrayList<>();
        if (c.length > 3 && !c[3].isBlank()) {
            for (String campo : c[3].split("\\|")) {
                itens.add(ItemVenda.fromCsvField(campo));
            }
        }
        return new Venda(
                Integer.parseInt(c[0]),
                LocalDateTime.parse(c[1], FMT),
                itens,
                new BigDecimal(c[2])
        );
    }
}
