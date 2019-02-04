(defproject org.clojars.dyne/auxiliary "0.5.0"
  :description "Common auxiliary functions extending clojure basic utilities"
  :url "https://dyne.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.forward/yaml "1.0.6"]
                 ;; logs
                 [com.taoensso/timbre "4.10.0"]
                 ;; env vars used for translation
                 [environ "1.1.0"]]
  :profiles {:dev {:plugins [[lein-marginalia "0.9.0"]]}})
