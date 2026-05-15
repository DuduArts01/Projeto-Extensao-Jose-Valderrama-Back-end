(defproject calculador-trastes "0.1.0-SNAPSHOT"
  :description "API para cálculo de trastes de instrumentos de corda"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.6.2"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-defaults "0.3.3"]
                 [http-kit "2.5.3"]]
  :main ^:skip-aot calculador-trastes.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})