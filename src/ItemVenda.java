import java.math.BigDecimal;

/**
 * Representa um item dentro de uma venda: um produto, a quantidade e o preço
 * unitário praticado no momento da venda (preservado mesmo se o preço do
 * produto mudar depois).
 */
public class ItemVenda {

    private String codigoProduto;
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemVenda(String codigoProduto, String nomeProduto, int quantidade, BigDecimal precoUnitario) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal subtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    @Override
    public String toString() {
        return String.format("%-25s %2dx R$ %7.2f = R$ %8.2f", nomeProduto, quantidade, precoUnitario, subtotal());
    }

    public String toCsvField() {
        // formato: codigo:quantidade:precoUnitario:nome  (itens de uma venda separados por '|' no CSV da venda)
        return codigoProduto + ":" + quantidade + ":" + precoUnitario.toPlainString() + ":" + nomeProduto;
    }

    public static ItemVenda fromCsvField(String campo) {
        String[] c = campo.split(":", 4);
        return new ItemVenda(c[0], c[3], Integer.parseInt(c[1]), new java.math.BigDecimal(c[2]));
    }
}
