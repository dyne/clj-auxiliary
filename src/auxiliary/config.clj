;; Auxiliary functions to handle configurations

;; Copyright (C) 2017 Dyne.org foundation

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

(ns auxiliary.config
  (:refer-clojure :exclude [load])
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [yaml.core :as yaml]
            [taoensso.timbre :as log]))

(def default-settings {:webserver
                       {:anti-forgery false
                        :ssl-redirect false}})

(defn yaml-read [path]
  (if (.exists (io/as-file path))
    (-> path yaml/from-file keywordize-keys)))

(defn config-read
  "Read configurations from standard locations, overriding defaults or
  system-wide with user specific paths. Requires the application name
  and optionally default values."
  ([appname] (config-read appname {}))
  ([appname defaults & flags]
   (let [home (System/getenv "HOME")
         pwd  (System/getenv "PWD" )
         file (str appname ".yaml")
         conf (-> {:appname appname
                   :filename file
                   :paths [(str      "/etc/" appname "/" file)
                           (str home "/."    appname "/" file)
                           (str pwd  "/"     file)
                           ;; TODO: this should be resources
                           (str pwd "/resources/"  file)
                           (str pwd "/test-resources/" file)]}
                  (conj defaults))]
     (loop [[p & paths] (:paths conf)
            res defaults]
       (let [res (merge res
                        (if (.exists (io/as-file p))
                          (conj res (yaml-read p))))]
         (if (empty? paths) (conj conf {(keyword appname) res})
             (recur paths res)))))))

(defn load-config [name default]
  (log/info (str "Loading configuration: " name))
  (->> (config-read name default)))
