(ns aoc-2022-clojure.day-01 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split input #"\n\n")
       (map str/split-lines)
       (map #(map parse-long %))))

(defn a
  ([] (a (slurp (io/resource "01.txt"))))
  ([input] 
   (->> (parse-input input)
        (map #(reduce + %))
        (reduce max))))

(defn b
  ([] (b (slurp (io/resource "01.txt"))))
  ([input]
   (->> (parse-input input)
        (map #(reduce + %))
        (sort)
        (reverse)
        (take 3)
        (reduce +))))
