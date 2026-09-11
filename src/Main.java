import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PDV pdv = new PDV();

    public static void main(String[] args) {
        System.out.println("=== PDV Simples ===");
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();
            try {
                switch (opcao) {
                    case "1" -> cadastrarProduto();
                    case "2" -> listarProdutos();
                    case "3" -> adicionarAoCarrinho();
                    case "4" -> verCarrinho();
                    case "5" -> finalizarVenda();
                    case "6" -> listarVendas();
                    case "7" -> faturamentoNoPeriodo();
                    case "8" -> produtosMaisVendidos();
                    case "0" -> {
                        pdv.salvar();
                        System.out.println("Dados salvos. Até logo!");
                        rodando = false;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("""

                --------------------------------
                1 - Cadastrar produto
                2 - Listar produtos
                3 - Adicionar item ao carrinho
                4 - Ver carrinho
                5 - Finalizar venda (gera recibo)
                6 - Listar vendas
                7 - Faturamento no período
                8 - Produtos mais vendidos
                0 - Salvar e sair
                --------------------------------
                Escolha uma opção:""");
    }

    private static void cadastrarProduto() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Preço: ");
        BigDecimal preco = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
        System.out.print("Estoque inicial: ");
        int estoque = Integer.parseInt(scanner.nextLine().trim());

        pdv.cadastrarProduto(new Produto(codigo, nome, preco, estoque));
        System.out.println("Produto cadastrado!");
    }

    private static void listarProdutos() {
        List<Produto> produtos = pdv.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        produtos.forEach(System.out::println);
    }

    private static void adicionarAoCarrinho() {
        System.out.print("Código do produto: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine().trim());
        pdv.adicionarAoCarrinho(codigo, quantidade);
        System.out.println("Item adicionado ao carrinho!");
    }

    private static void verCarrinho() {
        List<ItemVenda> itens = pdv.verCarrinho();
        if (itens.isEmpty()) {
            System.out.println("Carrinho vazio.");
            return;
        }
        itens.forEach(System.out::println);
    }

    private static void finalizarVenda() {
        System.out.print("Percentual de desconto (0 se não houver): ");
        BigDecimal desconto = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
        Venda venda = pdv.finalizarVenda(desconto);
        System.out.println(venda.gerarRecibo());
    }

    private static void listarVendas() {
        List<Venda> vendas = pdv.listarVendas();
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }
        vendas.forEach(System.out::println);
    }

    private static void faturamentoNoPeriodo() {
        System.out.print("Data início (aaaa-mm-dd): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Data fim (aaaa-mm-dd): ");
        LocalDate fim = LocalDate.parse(scanner.nextLine().trim());
        System.out.printf("Faturamento no período: R$ %.2f%n", pdv.faturamentoNoPeriodo(inicio, fim));
    }

    private static void produtosMaisVendidos() {
        Map<String, Long> ranking = pdv.produtosMaisVendidos();
        if (ranking.isEmpty()) {
            System.out.println("Nenhuma venda registrada ainda.");
            return;
        }
        ranking.forEach((nome, qtd) -> System.out.printf("%-25s %d unidade(s)%n", nome, qtd));
    }
}
