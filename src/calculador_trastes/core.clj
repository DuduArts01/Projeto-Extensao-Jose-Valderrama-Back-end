(ns calculador-trastes.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]] ;; <-- middleware de CORS
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

;; 1. Lógica de Cálculo
(def constante-traste 17.817)

(defn calcular-trastes [escala-total num-trastes]
  (loop [n 1
         pos-anterior-pestana 0.0
         resultado []]
    (if (> n num-trastes)
      resultado
      (let [dist-ate-pestana (+ pos-anterior-pestana (/ (- escala-total pos-anterior-pestana) constante-traste))
            tamanho-traste (if (= n 1) 
                             dist-ate-pestana 
                             (- dist-ate-pestana pos-anterior-pestana))
            dist-ate-ponte (- escala-total dist-ate-pestana)
            dados-traste {:traste n
                          :distancia_pestana (float dist-ate-pestana)
                          :tamanho_traste (float tamanho-traste)
                          :distancia_ponte (float dist-ate-ponte)}]
        (recur (inc n) dist-ate-pestana (conj resultado dados-traste))))))

;; 2. Definição das Rotas
(defroutes app-routes
  (GET "/" []
    {:status 200
      :body {:message "API Calculador de Trastes"
             :endpoint "/calcular?escala=650&trastes=22"}})
  (GET "/calcular" [escala trastes]
    (try
      (let [e (Double/parseDouble escala)
            t (Integer/parseInt (or trastes "22"))]
        {:status 200
         :body {:escala_informada e
                :unidade "mm"
                :mapa_de_trastes (calcular-trastes e t)}})
      (catch Exception ex
        {:status 400
         :body {:error "Parâmetros inválidos. Use: /calcular?escala=650&trastes=22"}})))
  (route/not-found {:status 404
                    :body {:error "Rota não encontrada"}}))

;; 3. Definição do APP com liberação de CORS
(def app
  (-> app-routes
      (wrap-json-response)
      ;; 2. Configurado para aceitar requisições do seu servidor local do Vite
      (wrap-cors :access-control-allow-origin [#规律"http://localhost:5173"]
                 :access-control-allow-methods [:get])))

;; 4. Função de Entrada
(defn -main [& args]
  ;; Render injeta a porta na variável PORT
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "🎸 Servidor de Luthieria iniciado na porta " port))
    (println (str "📍 Acesse: http://0.0.0.0:" port "/calcular?escala=650&trastes=22"))
    (run-server app {:port port})))