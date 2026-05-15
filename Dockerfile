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

# Lista os arquivos gerados para debug (temporário)
RUN ls -la target/uberjar/

# Comando para executar a aplicação
# Usa wildcard para pegar o arquivo *-standalone.jar
CMD java -jar target/uberjar/*-standalone.jar
