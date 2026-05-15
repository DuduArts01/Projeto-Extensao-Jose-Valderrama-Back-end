(ns calculador-trastes.core
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
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
  (GET "/calcular" [escala trastes]
    (let [e (Double/parseDouble escala)
          t (Integer/parseInt (or trastes "22"))]
      {:status 200
       :body {:escala_informada e
              :unidade "mm"
              :mapa_de_trastes (calcular-trastes e t)}}))
  (route/not-found {:error "Rota não encontrada"}))

;; 3. Definição do APP (O "app" precisa vir ANTES do -main)
(def app
  (-> app-routes
      (wrap-json-response)
      (wrap-defaults api-defaults)))

;; 4. Função de Entrada
(defn -main [& args]
  (let [port 3000]
    (println "Servidor de Luthieria iniciado na porta" port)
    (run-server app {:port port})))