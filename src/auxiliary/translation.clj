;; Freecoin - digital social currency toolkit

;; part of Decentralized Citizen Engagement Technologies (D-CENT)
;; R&D funded by the European Commission (FP7/CAPS 610349)

;; Copyright (C) 2016 Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; Denis Roio <jaromil@dyne.org>

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU Affero General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.

(ns auxiliary.translation
  (:require
   [taoensso.timbre :as log]
   [yaml.core :as yaml]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [environ.core :as env]))

(defonce translation (atom {}))

(defn load-translations-from-string [s]
  (yaml/parse-string s))

(defn load-translations-from-file [file-name]
  (if file-name
    (if-let [res (io/resource file-name)]
      (-> res 
          slurp
          load-translations-from-string)
      (-> file-name 
          slurp
          load-translations-from-string))
    (log/error "The translation file could not be read.")))

(defn deep-merge
  "Recursively merges maps. If keys are not maps, the last value wins."
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))

(defn init [& translation-files]
  (reset! translation (apply deep-merge (map #(load-translations-from-file %) translation-files))))

(defn locale [items]
  (when (empty? @translation)
    (log/error "Translations not initialised"))
  (get-in @translation items))
