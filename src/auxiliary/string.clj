(ns auxiliary.string)

(defn strcasecmp
  "case insensitive comparison of two strings"
  [str1 str2]
  (some? (re-matches (java.util.regex.Pattern/compile
                      (str "(?i)" str1)) str2)))
;; parse numbers safely
(def regex-parsenum (re-pattern #"[0-9]*"))
(defn parse-int [s]    (if (empty? s) 0 (Integer. (re-find regex-parsenum s))))
(defn parse-double [s] (if (empty? s) 0 (Double.  (re-find regex-parsenum s))))
