# API Calculador de Trastes

API em Clojure para calcular posições de trastes de instrumentos musicais.

## 🚀 Deploy no Render

### Pré-requisitos
- Conta no GitHub
- Conta no Render (gratuita)

### Passos

1. **Adicione os arquivos ao repositório:**
   ```bash
   git add Dockerfile .dockerignore project.clj render.yaml
   git commit -m "Adiciona configuração para deploy no Render"
   git push origin main
   ```

2. **Configure no Render:**
   - Acesse https://render.com
   - Clique em "New +" → "Web Service"
   - Conecte seu repositório do GitHub
   - Selecione o repositório
   - O Render detectará automaticamente o `render.yaml`
   - Clique em "Create Web Service"

3. **Aguarde o deploy:**
   - O build leva cerca de 3-5 minutos
   - Você receberá uma URL tipo: `https://calculador-trastes-api.onrender.com`

## 📡 Endpoints

### GET /
Retorna informações sobre a API

### GET /calcular
Calcula as posições dos trastes

**Parâmetros:**
- `escala` (obrigatório): Escala do instrumento em milímetros (ex: 650)
- `trastes` (opcional): Número de trastes (padrão: 22)

**Exemplo:**
```
GET /calcular?escala=650&trastes=24
```

**Resposta:**
```json
{
  "escala_informada": 650.0,
  "unidade": "mm",
  "mapa_de_trastes": [
    {
      "traste": 1,
      "distancia_pestana": 36.48,
      "tamanho_traste": 36.48,
      "distancia_ponte": 613.52
    },
    ...
  ]
}
```

## 🧪 Testar localmente com Docker

```bash
# Build
docker build -t calculador-trastes .

# Run
docker run -p 3000:3000 calculador-trastes

# Teste
curl "http://localhost:3000/calcular?escala=650&trastes=22"
```

## 🛠️ Desenvolvimento local

```bash
# Instalar dependências
lein deps

# Executar
lein run

# Ou REPL
lein repl
```
