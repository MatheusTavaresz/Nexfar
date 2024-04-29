# Relatório de Pedidos - Spring Boot API

Este projeto implementa uma API usando o framework Spring Boot, que permite a geração de relatórios dinâmicos de pedidos. A API recebe requisições com parâmetros específicos para gerar relatórios nos formatos XLS e CSV.

## Descrição

A API permite gerar dois tipos de relatórios: `PedidoResumido` e `PedidoDetalhado`. Estes podem ser solicitados através de um payload JSON, que especifica os filtros e o formato desejado para o relatório. Os formatos disponíveis são XLS e CSV, que podem ser facilmente manipulados e analisados.

## Funcionalidades

- Geração de relatórios dinâmicos com base em filtros fornecidos.
- Suporte para relatórios no formato XLS e CSV.
- Filtragem por parâmetros dinâmicos.

## Como Configurar
### Instalação
1. Clone o repositório:
   ```bash
   git clone https://github.com/MatheusTavaresz/Nexfar.git
    ```
    
2. Abra com o Intelij ou outra IDE de sua preferência:
3. Execute o projeto
4. Se tudo estiver correto, o projeto estrá em execução e pronto para receber os Endpoints para gerar os relatórios
5. Endereço padrão para as requisições (POST) 
     ```bash 
     http://localhost:8080/report/generate 
    ```
5. Exemplo de uso do Payload
    ```bash
        {
          "key": "ORDER_SIMPLE",
          "format": "csv",
          "filters": [
            {
              "key": "cnpj",
              "operation": "EQ",
              "value1": "92584796000276"
            },
            {
              "key": "createdAt",
              "operation": "INTERVAL",
              "value1": "21/09/2021 20:57",
              "value2": "29/09/2021 21:01"
            }
          ]
        }
    ```
