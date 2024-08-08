# Star Wars API - Automated Testing Course

Este projeto é parte do curso de **Testes Automatizados na Prática com Spring Boot**. O objetivo é demonstrar como criar testes de unidade, integração e subcutâneos usando diversas ferramentas e bibliotecas do ecossistema Java e Spring.

## Ferramentas e Tecnologias

- **Spring Boot Test**: Framework para testes de Spring Boot.
- **JUnit 5**: Framework de testes para Java.
- **Mockito**: Biblioteca para criação de mocks e stubs em testes.
- **AssertJ**: Framework de assertions fluentes para testes.
- **Hamcrest**: Framework para construção de matchers em testes.
- **JsonPath**: Biblioteca para navegação e verificação de JSON.
- **Jacoco**: Ferramenta para medição da cobertura de testes.
- **Pitest**: Ferramenta para avaliação da qualidade dos testes através de testes de mutação.

## Estrutura do Projeto

- **Testes de Unidade**: Validam comportamentos específicos de classes individuais.
- **Testes de Integração**: Verificam a integração entre diferentes componentes da aplicação.
- **Testes Subcutâneos**: Garantem que componentes individuais funcionem corretamente com seus dependentes reais.

## Configurações de Plugins e Dependências

### Jacoco - Cobertura de Testes

O plugin Jacoco é usado para medir a cobertura dos testes.

```
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>com/test_api_course/starwars/StarwarsApplication.class</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

# Projeto de Testes Automatizados com Spring Boot

Este projeto utiliza diversas ferramentas para garantir a qualidade e cobertura dos testes, como Jacoco e Pitest. Abaixo estão as configurações e comandos úteis para executar e avaliar os testes.

## Dependências

Adicione a seguinte dependência ao seu arquivo `pom.xml` para utilizar o Jacoco:

```
<dependency>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
</dependency>
````

Maven Failsafe - Testes de Integração
Para configurar o Maven Failsafe e executar testes de integração, adicione o seguinte plugin ao seu pom.xml:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
Comandos Úteis
Executar todos os testes:

```
./mvnw clean verify
```
Executar apenas testes de unidade:


```
./mvnw clean test
```
Executar apenas testes de integração:

```
./mvnw clean verify -Dsurefire.skip=true
```
Análise de Qualidade dos Testes
Jacoco
O Jacoco é utilizado para gerar relatórios de cobertura de código. Isso ajuda a identificar partes do código que não estão sendo cobertas pelos testes.

Pitest - Testes de Mutação
O Pitest é usado para avaliar a qualidade dos seus testes. Ele introduz mutações no código e verifica se os testes conseguem detectar essas alterações. Se os testes falharem ao detectar uma mutação, isso pode indicar que os testes não estão cobrindo todas as situações possíveis.
