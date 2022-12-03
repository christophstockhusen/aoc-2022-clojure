(ns aoc-2022-clojure.day-03 
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn parse-input [input] (map seq (str/split-lines input)))

(defn split-half [s] (partition (/ (count s) 2) s))

(defn char-value [c]
  (let [c (int c)]
    (if (<= 97 c 122) 
      (- c 96)
      (+ 26 (- c 64)))))

(defn a
  ([] (a (slurp (io/resource "03.txt"))))
  ([input] 
   (->> (parse-input input)
        (map split-half)
        (map #(set/intersection (set (first %)) (set (second %))))
        (map first)
        (map char-value)
        (reduce +))))

(defn b
  ([] (b (slurp (io/resource "03.txt"))))
  ([input]
   (->> (parse-input input)
        (partition 3)
        (map #(map set %))
        (map #(apply set/intersection %))
        (map first)
        (map char-value)
        (reduce +))))
