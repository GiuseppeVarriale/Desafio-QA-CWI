Desafio resolvido por Giuseppe Varriale 

**Requisitos para rodar o projeto**  
Intellij IDEA instalada  
Ou IDE compatível com java e junit + maven instalado (pode precisar de ações adicionais para rodar)

**Preparar para rodar:**  
faça um fork, abra sua IDE, sincronize o maven, se estiver usando a IntelliJ, para fazer o sync é só clicar na opção que aparece 
no seu pom.xml

**Rodar os testes**  
Escolha a configuração de run/debug com o ícone da suite de testes que quer rodar (AllTests, Acceptance, HealthCheck ou E2e) com o 
ícone do maven no  box run / debug configuration da IDEA, e aperte no run.  

**Rodar Relátorio dos Testes ( ALLURE )**  
Assim como feito para escolher a suíte de testes anteriormente, selecione no box run / debug a configuração Relatório Allure com ícone do maven, depois clique no ícone run.

# ->     Desafio API

Durante as aulas fizemos em conjuntos alguns exercícios e agora chegou a hora de testarmos nossas habilidades com o que aprendemos.

### Abaixo adiciono alguns cenários e suas respectivas suítes:


## **Suite Healthcheck:**

### **/HEALTHCHECK**

* Verificar se API está online.

## **Suite Contract :**

### **/GET**

* Garantir o contrato do retorno da lista de reservas.
  
* Garantir o contrato do retorno de uma reserva específica.

## Suite Acceptance:

### **/DELETE**

* Excluir um reserva com sucesso.

### **/GET**

* Listar IDs das reservas.

* Listar uma reserva específica.

* Listar IDs de reservas utilizando o filtro firstname.

* Listar IDs de reservas utilizando o filtro lastname.

* Listar IDs de reservas utilizando o filtro checkin.

* Listar IDs de reservas utilizando o filtro checkout.

* Listar IDs de reservas utilizando o filtro checkout and checkout.

* Listar IDs de reservas utilizando o filtro name, checkin and checkout date.

### **/POST**

* Criar uma nova reserva.

### **/PUT**

* Alterar uma reserva usando o token.
* Alterar uma reserva usando o Basic auth.

## **Suite E2e :**

### **/DELETE**

* Tentar excluir uma reserva que não existe.

* Tentar excluir uma reserva sem autorização.

### **/GET**

* Visualizar erro de servidor 500 quando enviar filtro mal formatado.

### **/POST**

* Validar retorno 500 quando o payload da reserva estiver inválido.

* Validar a criação de mais de um livro em sequência.

* Criar uma reserva enviando mais parâmetros no payload da reserva.

* Validar retorno 418 quando o header Accept for inválido.

### **/PUT**

* Tentar alterar uma reserva quando o token não for enviado.

* Tentar alterar uma reserva quando o token enviado for inválido.

* Tentar alterar uma reserva que não existe.

Realizar a automação dos serviços utilizar como base a documentação da nossa API: http://treinamento-api.herokuapp.com/apidoc/index.html

**\*\* Será um plus, desafios entregues no Github!**
