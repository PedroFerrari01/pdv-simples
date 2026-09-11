# 🛒 PDV Simples — Sistema de Frente de Caixa e Vendas (CLI)

Um sistema de Ponto de Venda ágil, construído inteiramente em Java puro, focado na simulação de um caixa comercial. O projeto garante precisão financeira e persistência leve operando 100% via terminal (linha de comando).

---

## 💡 Visão Geral do Produto

No varejo, a velocidade de atendimento e a exatidão financeira são inegociáveis. Sistemas lentos, complexos demais ou com arredondamentos incorretos geram filas e prejuízos no fechamento de caixa. O **PDV Simples** foi concebido para resolver essa dor estrutural, simulando o "motor" real de um caixa comercial rápido.

Por rodar diretamente no terminal (CLI), a aplicação elimina a sobrecarga de interfaces gráficas pesadas, focando no que importa: processamento ágil. O sistema permite ao operador cadastrar produtos, montar um carrinho de compras, aplicar descontos percentuais e gerar um recibo detalhado. Tudo isso integrado a um sistema de estoque que realiza a baixa automática dos itens assim que a venda é finalizada, garantindo que o inventário esteja sempre atualizado.

---

## 🧠 Engenharia e Decisões de Design

* **Precisão Monetária:** Uso exclusivo da classe `BigDecimal` para manipular dinheiro, eliminando as clássicas falhas de arredondamento de variáveis como `double` ou `float`.
* **Persistência Inteligente:** Não requer banco de dados. Ao encerrar o sistema corretamente, os dados são salvos em arquivos `.csv` dentro de uma pasta `dados/` gerada automaticamente.
* **Imutabilidade Histórica:** O sistema congela o preço unitário do produto no momento da venda. Relatórios do passado não sofrem alterações caso um produto mude de preço no futuro.

---

## 🚀 Como Rodar o Sistema

*(Este guia assume que você já tem o Java / JDK instalado no computador).*

### PASSO 1: Preparar os Arquivos
Faça o clone deste repositório ou baixe o arquivo `.zip` e extraia no seu computador. Você terá uma pasta chamada `pdv-simples`.

### PASSO 2: Abrir o Terminal na pasta `src`
Navegue até a pasta `pdv-simples` e entre na subpasta `src`.
* **No Windows:** Na barra de endereço do Explorador de Arquivos, digite `cmd` e aperte Enter. O terminal já abrirá na pasta certa.
* **No macOS / Linux:** Abra o Terminal e use o comando `cd` para navegar até a pasta `src` (ex: `cd Downloads/pdv-simples/src`).

### PASSO 3: Compilar o Código
Com o terminal aberto **DENTRO da pasta `src`**, digite o comando abaixo:
```bash
javac -d ../out *.java
```
*Se não aparecer nenhuma mensagem, deu certo! Ele criou uma pasta `out` (um nível acima) com os arquivos prontos para rodar.*

### PASSO 4: Rodar a Aplicação
Ainda no terminal, navegue para a pasta compilada e inicie o sistema:
```bash
cd ../out
java Main
```
O menu interativo do sistema aparecerá imediatamente na sua tela.

---

## 🧪 Roteiro Prático de Testes

Para ver o motor do sistema funcionando na prática, siga a ordem abaixo digitando o número da opção no menu e apertando *Enter*:

1. **Cadastrar Produto:** Digite `1`
   * Código: `P1` | Nome: `Refrigerante 2L` | Preço: `8.50` | Estoque: `20`
2. **Cadastrar Outro Produto:** Digite `1`
   * Código: `P2` | Nome: `Salgadinho` | Preço: `6.00` | Estoque: `30`
3. **Adicionar ao Carrinho:** Digite `3`
   * Código do produto: `P1` | Quantidade: `2`
4. **Adicionar Mais um Item:** Digite `3`
   * Código do produto: `P2` | Quantidade: `1`
5. **Ver Carrinho:** Digite `4`
   * *(O sistema mostrará os 2 itens e o subtotal de cada um).*
6. **Finalizar Venda:** Digite `5`
   * Percentual de desconto (0 se não houver): `10`
   * *(O recibo completo aparecerá na tela, já com o desconto aplicado em BigDecimal).*
7. **Verificar Baixa de Estoque:** Digite `2` para Listar Produtos
   * *(Repare que o estoque do P1 e P2 diminuiu automaticamente).*
8. **Salvar e Sair (MUITO IMPORTANTE):** Digite `0`
   * *⚠️ SEMPRE use essa opção para sair. É ela que cria a pasta `dados/` e salva os arquivos `.csv`. Se você fechar a janela do terminal no "X", perderá os dados.*

---

## ⚠️ Solução de Problemas (Troubleshooting)

* **`javac: comando não encontrado`**
  O Java (JDK) não está instalado ou não está configurado nas variáveis de ambiente do seu sistema.
* **`Error: Could not find or load main class Main`**
  Você está tentando rodar `java Main` na pasta errada. Confirme que está dentro da pasta `out` (e não dentro da `src`).
* **Mensagem "Estoque insuficiente" ao adicionar no carrinho**
  Comportamento correto e proposital: o sistema barra vendas que ultrapassem o saldo físico. Cadastre mais quantidade ou peça um volume menor.
* **Mensagem "O carrinho está vazio" ao finalizar venda**
  Você precisa usar a opção `3` para adicionar itens antes de tentar fechar o pedido no caixa.
