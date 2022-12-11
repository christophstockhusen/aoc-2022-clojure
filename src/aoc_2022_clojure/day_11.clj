(ns aoc-2022-clojure.day-11 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-operation [line]
  (let [operator (case (re-find #"[+*]" line)
                   "+" :addition
                   "*" :multiplication)
        operands (re-seq #"old|\d+" line)
        parse-operand (fn [x] (if (= "old" x) :old (parse-long x)))]
    {:operator operator
     :op1 (parse-operand (first operands))
     :op2 (parse-operand (second operands))}))

(defn parse-monkey [s]
  (let [[no items operation divisor true-case false-case] (str/split-lines s)]
    {:monkey (parse-long (re-find #"\d+" no))
     :inspections 0
     :items (into (clojure.lang.PersistentQueue/EMPTY) 
                  (map parse-long (re-seq #"\d+" items)))
     :operation (parse-operation operation)
     :test {:divisor (parse-long (re-find #"\d+" divisor))
            :true (parse-long (re-find #"\d+" true-case))
            :false (parse-long (re-find #"\d+" false-case))}}))

(defn parse-input [input]
  (mapv parse-monkey (str/split input #"\n\n")))

(defn worry-level [{operator :operator op1 :op1 op2 :op2} item]
  (let [op1 (if (number? op1) op1 item)
        op2 (if (number? op2) op2 item)
        f (if (= operator :addition) + *)]
    (f op1 op2)))

(defn simulate-step [part monkey-idx monkeys item]
  (let [worry-level (worry-level (get-in monkeys [monkey-idx :operation]) item)
        product-of-tests (reduce * (map #(get-in % [:test :divisor]) monkeys)) 
        worry-level (if (= part :a) 
                      (quot worry-level 3) 
                      (mod worry-level product-of-tests))
        test (get-in monkeys [monkey-idx :test])
        target (if (zero? (mod worry-level (:divisor test)))
                 (:true test)
                 (:false test))]
    (-> monkeys
        (update-in [target :items] #(conj % worry-level))
        (update-in [monkey-idx :inspections] inc))))

(defn simulate-turn [part monkeys monkey-idx]
  (let [monkey (nth monkeys monkey-idx)
        items (:items monkey)]
    (-> (reduce (partial simulate-step part monkey-idx) monkeys items)
        (assoc-in [monkey-idx :items] (clojure.lang.PersistentQueue/EMPTY)))))

(defn simulate-round [part monkeys _]
  (reduce (partial simulate-turn part) monkeys (range (count monkeys))))

(defn monkey-business [part monkeys]
  (let [rounds (if (= part :a) 20 10000)]
    (->> (reduce (partial simulate-round part) monkeys (range rounds))
         (map :inspections)
         sort
         reverse
         (take 2)
         (reduce *))))

(defn a
  ([] (a (slurp (io/resource "11.txt"))))
  ([input] (monkey-business :a (parse-input input))))

(defn b
  ([] (b (slurp (io/resource "11.txt"))))
  ([input] (monkey-business :b (parse-input input))))
