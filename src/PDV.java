import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PDV {

    private static final String ARQ_PRODUTOS = "dados/produtos.csv";
    private static final String ARQ_VENDAS = "dados/vendas.csv";

    private final Map<String, Produto> produtos = new LinkedHashMap<>();
    private final List<Venda> vendas = new ArrayList<>();
    private int proximoIdVenda = 1;

    // Carrinho da venda em andamento
    private final List<ItemVenda> carrinho = new ArrayList<>();

    public PDV() {
        ArquivoUtil.carregar(ARQ_PRODUTOS, Produto::fromCsvLine).forEach(p -> produtos.put(p.getCodigo(), p));
        vendas.addAll(ArquivoUtil.carregar(ARQ_VENDAS, Venda::fromCsvLine));
        proximoIdVenda = vendas.stream().mapToInt(Venda::getId).max().orElse(0) + 1;
    }

    public void salvar() {
        new java.io.File("dados").mkdirs();
        ArquivoUtil.salvar(ARQ_PRODUTOS, new ArrayList<>(produtos.values()), Produto::toCsvLine);
        ArquivoUtil.salvar(ARQ_VENDAS, vendas, Venda::toCsvLine);
    }

    // ---------- Produtos ----------

    public void cadastrarProduto(Produto produto) {
        if (produtos.containsKey(produto.getCodigo())) {
            throw new IllegalArgumentException("Já existe um produto com o código " + produto.getCodigo());
        }
        produtos.put(produto.getCodigo(), produto);
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos.values());
    }

    // ---------- Carrinho / Venda ----------

    public void adicionarAoCarrinho(String codigoProduto, int quantidade) {
        Produto produto = buscarOuFalhar(codigoProduto);
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
        int jaNoCarrinho = carrinho.stream()
                .filter(i -> i.getCodigoProduto().equals(codigoProduto))
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
        if (jaNoCarrinho + quantidade > produto.getEstoque()) {
            throw new IllegalStateException("Estoque insuficiente. Disponível: " + produto.getEstoque());
        }
        carrinho.add(new ItemVenda(produto.getCodigo(), produto.getNome(), quantidade, produto.getPreco()));
    }

    public List<ItemVenda> verCarrinho() {
        return new ArrayList<>(carrinho);
    }

    public void limparCarrinho() {
        carrinho.clear();
    }

    public Venda finalizarVenda(BigDecimal percentualDesconto) {
        if (carrinho.isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }
        // Baixa o estoque de cada produto vendido
        for (ItemVenda item : carrinho) {
            buscarOuFalhar(item.getCodigoProduto()).baixarEstoque(item.getQuantidade());
        }
        Venda venda = new Venda(proximoIdVenda++, LocalDateTime.now(), new ArrayList<>(carrinho), percentualDesconto);
        vendas.add(venda);
        carrinho.clear();
        return venda;
    }

    // ---------- Relatórios ----------

    public List<Venda> listarVendas() {
        return new ArrayList<>(vendas);
    }

    public BigDecimal faturamentoNoPeriodo(LocalDate inicio, LocalDate fim) {
        return vendas.stream()
                .filter(v -> {
                    LocalDate data = v.getDataHora().toLocalDate();
                    return !data.isBefore(inicio) && !data.isAfter(fim);
                })
                .map(Venda::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, Long> produtosMaisVendidos() {
        return vendas.stream()
                .flatMap(v -> v.getItens().stream())
                .collect(Collectors.groupingBy(ItemVenda::getNomeProduto, Collectors.summingLong(ItemVenda::getQuantidade)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    private Produto buscarOuFalhar(String codigo) {
        Produto produto = produtos.get(codigo);
        if (produto == null) {
            throw new NoSuchElementException("Produto não encontrado: " + codigo);
        }
        return produto;
    }
}
