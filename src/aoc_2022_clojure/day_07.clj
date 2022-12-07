(ns aoc-2022-clojure.day-07
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn sizes [lines]
  (loop [lines lines
         stack []
         sizes []]
    (if (empty? lines)
      (concat sizes (reductions + (reverse stack)))
      (let [[line remaining] [(first lines) (rest lines)]]
        (cond (= line "$ cd ..") (recur remaining
                                        (update (pop stack)
                                                (dec (count (pop stack)))
                                                #(+ (peek stack) %))
                                        (conj sizes (peek stack)))
              (str/starts-with? line "$ cd") (recur remaining (conj stack 0) sizes)
              (or (str/starts-with? line "dir")
                  (= line "$ ls")) (recur remaining stack sizes)
              :else (recur remaining
                           (update stack
                                   (dec (count stack))
                                   #(+ % (parse-long (re-find #"\d+" line))))
                           sizes))))))

(defn a
  ([] (a (slurp (io/resource "07.txt"))))
  ([input] (reduce + (filter #(<= % 100000) (sizes (str/split-lines input))))))

(defn b
  ([] (b (slurp (io/resource "07.txt"))))
  ([input] (let [sizes (sizes (str/split-lines input))
                 largest (reduce max sizes)
                 required (- 30000000 (- 70000000 largest))]
             (first (sort (filter #(<= required %) sizes))))))
