FROM clojure:temurin-21-lein-2.11.2-jammy

WORKDIR /app

# Copia os arquivos de dependências primeiro (para melhor cache)
COPY project.clj .

# Baixa as dependências
RUN lein deps

# Copia todo o código da aplicação
COPY . .

# Compila o uberjar
RUN lein uberjar

# Expõe a porta 3000 (pode ser sobrescrita pela variável PORT)
EXPOSE 3000

# Comando para executar a aplicação
# Ajuste o nome do jar conforme o nome do seu projeto no project.clj
CMD ["java", "-jar", "target/uberjar/calculador-trastes-standalone.jar"]
