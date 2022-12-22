(ns aoc-2022-clojure.day-21 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [line]
  (let [splitted (str/split line #":? ")
        label (first splitted)
        operation (rest splitted)
        ops {"+" + "-" - "*" * "/" quot}]
    (if (= 3 (count operation))
      [label
       {:op1 (first operation)
        :operator-fn (get ops (second operation))
        :operator-name (second operation)
        :op2 (nth operation 2)}]
      [label
       {:constant (parse-long (first operation))}])))

(defn parse-input [input]
  (->> (map parse-line (str/split-lines input))
       (into (hash-map))))

(defn tree-results
  ([tree] (tree-results tree "root"))
  ([tree node-name]
   (let [node (get tree node-name)]
     (cond
       (nil? node)
       {}
       
       (:constant node)
       {node-name (:constant node)}

       :else
       (let [a (tree-results tree (:op1 node))
             a-value (get a (:op1 node))
             b (tree-results tree (:op2 node))
             b-value (get b (:op2 node))
             operator (:operator-fn node)
             result (if (or (nil? a-value) 
                            (nil? b-value))
                      nil
                      (operator a-value b-value))]
         (-> (merge a b)
             (assoc node-name result)))))))

(defn a
  ([] (a (slurp (io/resource "21.txt"))))
  ([input] (-> input
               parse-input
               tree-results
               (get "root"))))

(defn solve-riddle
  ([tree]
   (let [results (tree-results tree)
         {left :op1 right :op2} (get tree "root")
         left-value (get results left)
         right-value (get results right)
         [node target-value] (if (nil? left-value)
                               [left right-value]
                               [right left-value])]
     (solve-riddle tree node results target-value)))
  ([tree node-name results target-value]
   (if (= node-name "humn")
     target-value
     (let [node (get tree node-name)
           operator (:operator-name node)
           left (:op1 node)
           left-value (get results left)
           right (:op2 node)
           right-value (get results right)
           partial-solve-fn (partial solve-riddle
                                     tree
                                     (if (nil? left-value) left right)
                                     results)]
       (cond
         (= "+" operator)
         (partial-solve-fn
          (if (nil? left-value)
            (- target-value right-value)
            (- target-value left-value)))

         (= "-" operator)
         (partial-solve-fn
          (if (nil? left-value)
            (+ target-value right-value)
            (- left-value target-value)))

         (= "*" operator)
         (partial-solve-fn
          (if (nil? left-value)
            (/ target-value right-value)
            (/ target-value left-value)))

         (= "/" operator)
         (partial-solve-fn
          (if (nil? left-value)
            (* target-value right-value)
            (/ left-value right-value)))
         
         :else (println "lol"))))))

(defn b
  ([] (b (slurp (io/resource "21.txt"))))
  ([input] (let [parsed (parse-input input)
                 tree (assoc-in parsed ["humn" :constant] nil)
                 target (solve-riddle tree)]
             target)))
