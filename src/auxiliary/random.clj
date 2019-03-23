;; Copyright (C) 2015-2019 Dyne.org foundation

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

(ns auxiliary.random
  (:require [alphabase.base58 :as b58])
  (:import  java.security.SecureRandom))

(defmacro wrap-ignore-exception [& body]
  `(try
     ~@body
     (catch Throwable e#)))

(defn- ^SecureRandom new-random
  "Try to create a appropriate SecureRandom.
   http://www.cigital.com/justice-league-blog/2009/08/14/proper-use-of-javas-securerandom/ "
  []
  (doto
      (or
       (wrap-ignore-exception (SecureRandom/getInstance "SHA1PRNG" "SUN"))
       (wrap-ignore-exception (SecureRandom/getInstance "SHA1PRNG"))
       (wrap-ignore-exception (SecureRandom.)))
    (.nextBytes (byte-array 16))))

(defonce ^ThreadLocal threadlocal-random (proxy [ThreadLocal] []
                                           (initialValue [] (new-random))))

(defn bytes
  "Returns a secure random byte array of the specified size."
  [size]
  (let [bs (byte-array size)]
    (.nextBytes ^SecureRandom (.get threadlocal-random) bs)
    bs))

(defn generate
  "Generates a random byte sequence of a certain size."
  [size] (-> size bytes b58/encode))
