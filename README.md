# 🎸 Calculador de Trastes — API REST em Clojure

> Projeto de Extensão Universitária — Instituto Mauá de Tecnologia  
> Parceria com **José Valderrama Luthier**

---

## 🏢 A Empresa Parceira

**José Valderrama** é um luthier especializado na construção artesanal de violões clássicos de alta performance, reconhecido pela precisão acústica e pela identidade sonora singular de cada instrumento que produz. Cada violão é desenvolvido em sua oficina com seleção criteriosa de madeiras, atenção à tocabilidade e ajuste fino para projeção, equilíbrio e clareza sonora.

Além dos instrumentos, Valderrama produz **Tarrachas Valderrama** — ferragens artesanais desenvolvidas para máxima precisão, estabilidade e estética clássica, disponíveis para violões de 6, 7 e 8 cordas sob encomenda.

A empresa tem planos de lançar em breve um **curso de formação em Lutheria**, voltado a músicos, estudantes e entusiastas da construção de instrumentos musicais.

| Contato            | Informação                        |
|--------------------|-----------------------------------|
| 📞 WhatsApp        | (11) 97334-4545                   |
| 📧 E-mail          | contato@valderramaluthier.com.br  |
| 📷 Instagram       | @valderrama_luthier               |
| 🌐 Site            | [qualitsys.com.br/valderrama](https://qualitsys.com.br/valderrama/) |

---

## 💡 Motivação do Projeto

Em instrumentos de cordas trasteados — como violões, guitarras e baixos — a correta posição dos trastes é **essencial** para garantir a precisão da afinação ao longo de todo o braço do instrumento.

Essa disposição **não segue uma progressão linear**, mas sim uma **progressão exponencial**, determinada pela relação física entre o comprimento vibrante da corda e a frequência sonora produzida. Calcular esses valores manualmente para cada instrumento é um processo repetitivo e suscetível a erros.

O objetivo estratégico deste projeto é disponibilizar um **calculador de trastes no site de José Valderrama**, com duas finalidades:

1. **Atrair e engajar** potenciais estudantes para o futuro curso de formação em Lutheria.
2. **Despertar o interesse** de clientes nos instrumentos artesanais, evidenciando de forma prática os fundamentos da construção e da precisão dos instrumentos.

Assim, o calculador atua tanto como **ferramenta funcional** quanto como **recurso de divulgação**, ampliando a visibilidade do trabalho artesanal de Valderrama.

---

## ⚙️ Como a API Funciona

### Conceito: O que é a Escala?

A **escala** de um instrumento é a distância, em milímetros, entre a **pestana (nut)** — onde as cordas começam — e a **ponte (cavalete)** — onde as cordas são fixadas. Esse valor é o dado de entrada para todos os cálculos.

```
PESTANA ←————————— ESCALA (mm) ————————————→ PONTE
  (nut)                                      (bridge)
```

### Fórmula Principal

Para cada traste `n`, a distância da pestana até aquele traste é dada por:

```
d_n = L × (1 - 1 / 2^(n / 12))
```

| Variável | Significado                                |
|----------|--------------------------------------------|
| `d_n`    | Distância do traste n até a pestana (mm)   |
| `L`      | Escala do instrumento em milímetros        |
| `n`      | Número do traste (1 a 22)                  |

A lógica se baseia no fato de que, na **afinação temperada**, a oitava é dividida em 12 semitons iguais, e cada traste reduz o comprimento vibrante da corda segundo uma progressão exponencial.

### Grandezas Calculadas

Para cada traste, a API calcula e retorna três valores:

**1. Distância à pestana (`distancia_pestana`)**
```
distancia_pestana = d_n = L × (1 - 1 / 2^(n / 12))
```

**2. Distância à ponte (`distancia_ponte`)**
```
distancia_ponte = L - d_n
```

**3. Tamanho do espaço do traste (`tamanho_traste`)**
```
tamanho_traste_1 = d_1                  (primeiro traste)
tamanho_traste_n = d_n - d_(n-1)       (demais trastes)
```

### ✅ Verificação

Como validação dos resultados, o **12º traste** deve estar exatamente na metade da escala:

```
Para L = 650 mm → d_12 = 325 mm da pestana e 325 mm da ponte
```

---

### 🔗 Endpoint da API

#### `GET /trastes`

Calcula e retorna as posições de todos os trastes para uma dada escala.

**Query Parameter:**

| Parâmetro | Tipo    | Obrigatório | Descrição                        |
|-----------|---------|-------------|----------------------------------|
| `escala`  | number  | ✅ Sim       | Comprimento da escala em mm      |

**Exemplo de Requisição:**
```
GET /trastes?escala=650
```

**Exemplo de Resposta (`200 OK`):**
```json
{
  "escala_mm": 650.0,
  "formula": "d_n = L * (1 - 1 / 2^(n / 12))",
  "trastes": [
    {
      "traste": 1,
      "tamanho_mm": 36.48,
      "distancia_ponte_mm": 613.52,
      "distancia_pestana_mm": 36.48
    },
    {
      "traste": 2,
      "tamanho_mm": 34.43,
      "distancia_ponte_mm": 579.08,
      "distancia_pestana_mm": 70.92
    },
    {
      "traste": 12,
      "tamanho_mm": 19.47,
      "distancia_ponte_mm": 325.0,
      "distancia_pestana_mm": 325.0
    }
  ]
}
```

**Validações e Erros:**

| Situação                        | Status | Mensagem                                    |
|---------------------------------|--------|---------------------------------------------|
| Parâmetro `escala` ausente      | `400`  | `"Parâmetro 'escala' é obrigatório"`        |
| Valor não numérico              | `400`  | `"Valor inválido para 'escala'"`            |
| Valor menor ou igual a zero     | `400`  | `"A escala deve ser um valor positivo"`     |
| Processamento bem-sucedido      | `200`  | Retorna o JSON com todos os trastes         |

---

## 🌐 Exemplo de Integração no Frontend (React + TypeScript + Tailwind)

A seguir, um exemplo completo de como integrar a API num componente React com TypeScript e Tailwind CSS, no estilo visual escuro do site de José Valderrama.

### Tipo dos Dados

```typescript
// types/frets.ts

export interface Traste {
  traste: number;
  tamanho_mm: number;
  distancia_ponte_mm: number;
  distancia_pestana_mm: number;
}

export interface FretResponse {
  escala_mm: number;
  formula: string;
  trastes: Traste[];
}
```

### Hook de Busca

```typescript
// hooks/useFretCalculator.ts
import { useState } from "react";
import { FretResponse } from "../types/frets";

const API_URL = "http://localhost:3000"; // troque pela URL de produção

export function useFretCalculator() {
  const [data, setData] = useState<FretResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const calcular = async (escala: number) => {
    setLoading(true);
    setError(null);
    setData(null);

    try {
      const res = await fetch(`${API_URL}/trastes?escala=${escala}`);

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message ?? "Erro ao consultar a API");
      }

      const json: FretResponse = await res.json();
      setData(json);
    } catch (e: unknown) {
      setError(e instanceof Error ? e.message : "Erro desconhecido");
    } finally {
      setLoading(false);
    }
  };

  return { data, loading, error, calcular };
}
```

### Componente Principal

```tsx
// components/FretCalculator.tsx
import { useState } from "react";
import { useFretCalculator } from "../hooks/useFretCalculator";

export default function FretCalculator() {
  const [escala, setEscala] = useState<string>("650");
  const { data, loading, error, calcular } = useFretCalculator();

  const handleCalcular = () => {
    const valor = parseFloat(escala);
    if (!isNaN(valor) && valor > 0) {
      calcular(valor);
    }
  };

  return (
    <section className="bg-[#1a0f0a] text-white min-h-screen px-6 py-12 font-sans">
      <div className="max-w-4xl mx-auto">

        {/* Cabeçalho */}
        <h2 className="text-3xl font-bold text-amber-400 mb-2">
          Cálculo de Trastes
        </h2>
        <p className="text-stone-400 mb-8 text-sm">
          Informe a escala (mm). Ex.: 650
        </p>

        {/* Input + Botão */}
        <div className="flex gap-4 mb-10">
          <input
            type="number"
            value={escala}
            onChange={(e) => setEscala(e.target.value)}
            placeholder="Escala em milímetros"
            className="
              bg-[#2c1a10] border border-stone-600 rounded-lg
              px-4 py-3 text-white w-56 text-sm
              focus:outline-none focus:border-amber-400
            "
          />
          <button
            onClick={handleCalcular}
            disabled={loading}
            className="
              bg-[#3b5e3a] hover:bg-[#4a7348] text-white
              font-semibold tracking-widest uppercase
              px-6 py-3 rounded-lg text-sm transition-colors
              disabled:opacity-50 disabled:cursor-not-allowed
            "
          >
            {loading ? "Calculando..." : "Calcular"}
          </button>
        </div>

        {/* Erro */}
        {error && (
          <p className="text-red-400 text-sm mb-6">⚠️ {error}</p>
        )}

        {/* Resultado */}
        {data && (
          <>
            <p className="text-stone-400 text-sm mb-4">
              <span className="text-white font-semibold">
                Escala: {data.escala_mm.toFixed(2)} mm
              </span>
              {" — "}
              <span className="font-mono text-amber-300">{data.formula}</span>
            </p>

            <div className="overflow-x-auto rounded-xl border border-stone-700">
              <table className="w-full text-sm text-center">
                <thead className="bg-[#3b2010] text-amber-300 uppercase tracking-wider text-xs">
                  <tr>
                    <th className="py-3 px-4">Traste N°</th>
                    <th className="py-3 px-4">Espaçamento (mm)</th>
                    <th className="py-3 px-4">Dist. à Ponte (mm)</th>
                    <th className="py-3 px-4">Dist. à Pestana (mm)</th>
                  </tr>
                </thead>
                <tbody>
                  {data.trastes.map((t, i) => (
                    <tr
                      key={t.traste}
                      className={`
                        border-t border-stone-800
                        ${i % 2 === 0 ? "bg-[#1a0f0a]" : "bg-[#221408]"}
                        ${t.traste === 12 ? "text-amber-300 font-semibold" : "text-stone-300"}
                      `}
                    >
                      <td className="py-3 px-4">{t.traste}</td>
                      <td className="py-3 px-4">{t.tamanho_mm.toFixed(2)}</td>
                      <td className="py-3 px-4">{t.distancia_ponte_mm.toFixed(2)}</td>
                      <td className="py-3 px-4">{t.distancia_pestana_mm.toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <p className="text-stone-500 text-xs mt-3">
              * O 12° traste é destacado — deve estar exatamente na metade da escala.
            </p>
          </>
        )}
      </div>
    </section>
  );
}
```

### Como Usar o Componente

```tsx
// App.tsx ou a página de construção do site
import FretCalculator from "./components/FretCalculator";

export default function ConstrucaoPage() {
  return (
    <main>
      {/* ... demais seções da página ... */}
      <FretCalculator />
    </main>
  );
}
```

> **Dica de CORS:** certifique-se de que a API Clojure está configurada para aceitar requisições do domínio do site em produção. Com a biblioteca Ring/Compojure, adicione o middleware `ring.middleware.cors/wrap-cors`.

---

## 🏗️ Arquitetura do Sistema

```
┌─────────────────────────────────────────────────┐
│               Frontend (React / TS)             │
│         Site José Valderrama — Construção       │
└──────────────────────┬──────────────────────────┘
                       │  HTTP GET /trastes?escala=650
                       ▼
┌─────────────────────────────────────────────────┐
│             API REST — Clojure                  │
│                                                 │
│  ┌─────────────┐   ┌────────────┐   ┌────────┐ │
│  │  Validação  │──▶│  Cálculo   │──▶│  JSON  │ │
│  │  de entrada │   │ dos Trastes│   │Response│ │
│  └─────────────┘   └────────────┘   └────────┘ │
└─────────────────────────────────────────────────┘
```

**Fluxo operacional:**
1. O usuário informa a escala em mm no frontend.
2. O frontend envia um `GET /trastes?escala={valor}`.
3. A API valida o parâmetro recebido.
4. O motor de cálculo aplica a fórmula temperada para cada traste (1 a 22).
5. O resultado é retornado em JSON e exibido na interface.

---

## 🛠️ Tecnologias Utilizadas

| Camada     | Tecnologia                            |
|------------|---------------------------------------|
| Backend    | Clojure + Leiningen                   |
| HTTP       | Ring + Compojure                      |
| Serialização | Cheshire (JSON)                     |
| CORS       | ring-cors                             |
| Frontend   | React + TypeScript + Tailwind CSS     |
| Deploy     | Docker Compose / AWS ou Integrator    |

---

## 🚀 Infraestrutura e Instalação

### Requisitos

- [Java JDK 11+](https://adoptium.net/)
- [Leiningen](https://leiningen.org/)
- [Docker](https://www.docker.com/) (opcional, para deploy)

### Executando localmente

```bash
# 1. Clone o repositório
git clone https://github.com/DuduArts01/Projeto-Extensao-Jose-Valderrama-Back-end.git
cd Projeto-Extensao-Jose-Valderrama-Back-end

# 2. Instale as dependências e execute
lein run
```

A API ficará disponível em `http://localhost:3000`.

### Com Docker Compose

```bash
docker-compose up --build
```

---

## 🔭 Futuras Melhorias (Roadmap)

- [ ] Suporte a número variável de trastes (ex.: 19, 20, 24)
- [ ] Exportação dos resultados em PDF ou planilha
- [ ] Histórico de cálculos com banco de dados
- [ ] Interface mobile (Android)
- [ ] Suporte a instrumentos sem trastes (cálculo de referência para marcação)
- [ ] Internacionalização (EN / ES)

---

## 📚 Referências

- Instituto Mauá de Tecnologia — Especificação do Projeto de Extensão (Prof. Aparecido V. de Freitas)
- Site oficial José Valderrama Luthier: [qualitsys.com.br/valderrama](https://qualitsys.com.br/valderrama/)
- Teoria do temperamento igual: relação entre frequência e comprimento de corda vibrante
- [Clojure Docs](https://clojuredocs.org/)
- [Ring — Clojure HTTP server abstraction](https://github.com/ring-clojure/ring)
- [Compojure — A concise routing library](https://github.com/weavejester/compojure)

---

## 👥 Equipe

| Função              | Nome                        |
|---------------------|-----------------------------|
| Orientador          | Prof. Aparecido V. de Freitas |
| Empresa Parceira    | José Valderrama Luthier     |
| Desenvolvedores     | [Componentes do Grupo]      |

> Projeto de Extensão — Programação Funcional com Clojure  
> Instituto Mauá de Tecnologia — 2026
