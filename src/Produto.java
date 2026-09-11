import java.math.BigDecimal;

public class Produto {

    private String codigo;
    private String nome;
    private BigDecimal preco;
    private int estoque;

    public Produto(String codigo, String nome, BigDecimal preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade > estoque) {
            throw new IllegalStateException("Estoque insuficiente para " + nome + " (disponível: " + estoque + ")");
        }
        estoque -= quantidade;
    }

    public void repor(int quantidade) {
        estoque += quantidade;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-25s R$ %8.2f | Estoque: %d", codigo, nome, preco, estoque);
    }

    public String toCsvLine() {
        return codigo + ";" + nome + ";" + preco.toPlainString() + ";" + estoque;
    }

    public static Produto fromCsvLine(String linha) {
        String[] c = linha.split(";");
        return new Produto(c[0], c[1], new BigDecimal(c[2]), Integer.parseInt(c[3]));
    }
}
