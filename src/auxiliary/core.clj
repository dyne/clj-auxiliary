(ns auxiliary.core
  (:refer-clojure :exclude [any?]))

;; from suchwow.control-flow

(defmacro branch-on
  "    (branch-on (str \"one\" \"two\")
         vector?   :vector
         string?   :string
         :else     :unknown)

  Evaluates the `value-form` once, then checks that value against
  each predicate in the cond-like body. The value after the first
  matching predicate is returned. If there is no match and an `:else`
  clause is present, its value is returned, otherwise `nil`.
"
  [value-form & body]
  (let [value-sym (gensym "value-form-")
        cond-pairs (mapcat (fn [[branch-pred-form branch-val-form]]
                             (let [test (if (= branch-pred-form :else)
                                          :else
                                          `(~branch-pred-form ~value-sym))]
                             `(~test ~branch-val-form)))
                           (partition 2 body))]
    
    `(let [~value-sym ~value-form]
       (cond ~@cond-pairs))))
 
(defmacro let-maybe
  "Like `let` except that if any symbol would be bound to a `nil`, the
   entire expression immediately short-circuits and returns `nil`.

       (let-maybe [v []
                   f (first v)
                   _ (throw (new Exception))]
          (throw (new Exception)))
       => nil
"
  [bindings & body]
  (if (empty? bindings)
    `(do ~@body)
    `(when-some [~@(take 2 bindings)]
       (let-maybe [~@(drop 2 bindings)] ~@body))))


;; from suchwow.vars


(defprotocol Rootable
  "A protocol to look at \"root\" values of Vars. The root value is
   the value before any `binding` - it's the value altered by `alter-var-root`.
   Defines `has-root-value?` and `root-value`."
  (has-root-value? [this]
    "Does this var have a root value?" )
  (root-value [this]
    "What is the value of the var, ignoring any bindings in effect?"))

(extend-type clojure.lang.Var
  Rootable
  (has-root-value? [var] (.hasRoot var))
  (root-value [var] (alter-var-root var identity)))

(defn name-as-symbol 
  "Unlike symbols and keywords, the \"name\" of a var is a symbol. This function
   returns that symbol. See also [[name-as-string]].
   
        (var/name-as-symbol #'clojure.core/even?) => 'even?)

   Note that the symbol does not have a namespace."
  [var]
  (.sym var))

(defn name-as-string
  "Unlike symbols and keywords, the \"name\" of a var is a symbol. This function
   returns the string name of that symbol. See also [[name-as-symbol]].

        (var/name-as-string #'clojure.core/even?) => \"even?\")
"
  [var]
  (name (name-as-symbol var)))

(defn has-macro?
  "Does the var point to a macro?"
  [v]
  (boolean (:macro (meta v))))

(defn has-function?
  "Does the var point to a function?"
  [v]
  (or (and (boolean (:arglists (meta v)))
           (not (has-macro? v)))
      (= (type (root-value v)) clojure.lang.MultiFn)))

(defn has-plain-value?
  "Does the var point to something not a macro nor a function?"
  [v]
  ( (complement (some-fn has-macro? has-function?)) v))

;; from suchwow.shorthand

(defn any?
  "`any?` provides shorthand for \"containment\" queries that otherwise
   require different functions. Behavior depends on the type of `predlike`.
   
   * A function: `true` iff `predlike` returns a *truthy* value for any value in `coll`.
     
            (any? even? [1 2 3]) => true           ; works best with boolean-valued functions
            (any? inc [1 2 3]) => true             ; a silly example to demo truthiness.
            (any? identity [nil false]) => false   ; also silly
     
   * A collection: `true` iff `predlike` contains any element of `coll`.

            (any? #{1 3} [5 4 1]) => true
            (any? [1 3] [5 4 1]) => true
        
        When `predlike` is a map, it checks key/value pairs:
        
            (any? {:a 1} {:a 1}) => true
            (any? {:a 1} {:a 2}) => false
            (any? {:a 2, :b 1} {:b 1, :c 3}) => true
     
   * A keyword: `true` iff `predlike` is a key in `coll`, which *must* be a map.
     
            (any? :a {:a 1, :b 2}) => true         ; equivalent to:
            (contains? {:a 1, :b 2} :a) => true
"
  [predlike coll]
  (boolean (cond (coll? predlike)
                 (some (set predlike) coll)

                 (keyword? predlike)
                 (contains? coll predlike)

                 :else
                 (some predlike coll))))

(defn not-empty? 
  "Returns `true` if `value` has any values, `false` otherwise. `value` *must* be a collection,
     a String, a native Java array, or something that implements the `Iterable` interface."
  [value]
  (boolean (seq value)))

(defn third 
  "Returns the third element of `coll`. Returns `nil` if there are fewer than three elements."
  [coll]
  (second (rest coll)))

(defn fourth
  "Returns the fourth element of `coll`. Returns `nil` if there are fewer than four elements."
  [coll]
  (third (rest coll)))

(defn find-first
  "Returns the first item of `coll` where `(pred item)` returns a truthy value, `nil` otherwise.
   `coll` is evaluated lazily.
   
        (find-first even? [1 2 3]) => 2
   
   You can apply `find-first` to a map, even though which
   element matches \"first\" is undefined. Note that the item passed to `pred` will
   be a key-value pair:

        (find-first #(even? (second %)) {:a 2, :b 22, :c 222}) => [:c 222]
"
  [pred coll]
  (first (filter pred coll)))

(defn without-nils 
  "A lazy sequence of non-nil values of `coll`."
  [coll]
  (keep identity coll))


(defmacro prog1
  "The `retform` is evaluated, followed by the `body`. The value of the
   form is returned, so the point of `body` should be to have side-effects.
   
       (defn pop! [k]
          (prog1 (top k)
            (alter! k clojure.core/pop)))
   
   The name is a homage to older Lisps.
"
  [retform & body]
  `(let [retval# ~retform]
     ~@body
     retval#))

;; from suchwow.types

(defn regex?
  "Is x a regular expression (a Java Pattern)?"
  [x]
  (instance? java.util.regex.Pattern x))

(defn stringlike?
  "Is x a string or a regex?"
  [x]
  (or (string? x) (regex? x)))

(defn classic-map?
  "`map?` will return true for Records. This returns true only for hashmaps and sorted maps."
  [x]
  (instance? clojure.lang.APersistentMap x))

(defn big-decimal?
  "Is x a Java BigDecimal?"
  [x]
  (instance? java.math.BigDecimal x))

(defn multi? 
  "Was `x` created with `defmulti`?"
  [x]
  (instance? clojure.lang.MultiFn x))

(defn extended-fn?
  "`fn?` does not consider multimethods to be functions. This does."
  [x]
  (or (fn? x) (multi? x)))
  
(defn named?
  "Will `name` work on x? Two cases: It implements the Named protocol OR it's a string."
  [x]
  (or (string? x)
      (instance? clojure.lang.Named x)))

(defn linear-access?
  "Is the collection one where you can't do better than linear access?"
  [x]
  (or (list? x)
      (seq? x)))

(defn namespace?
  "Is x a namespace?"
  [x]
  (instance? clojure.lang.Namespace x))
