(defproject org.clojars.dyne/auxiliary "0.5.0-SNAPSHOT"
  :description "Common auxiliary functions extending clojure basic utilities"
  :url "https://dyne.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; fix for jdk 10
  :managed-dependencies [[org.clojure/core.rrb-vector "0.0.14"]
                       [org.flatland/ordered "1.5.7"]]

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.forward/yaml "1.0.9"]
                 ;; logs
                 [com.taoensso/timbre "4.10.0"]
                 ;; env vars used for translation
                 [environ "1.1.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.6"]]
                   :plugins [[lein-midje "3.1.3"]
                             [lein-marginalia "0.9.0"]]}})
