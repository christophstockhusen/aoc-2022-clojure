(ns aoc-2022-clojure.day-06 
  (:require [clojure.java.io :as io]))

(defn find-marker-pos [n input]
  (->> (partition n 1 (seq input))
       (map (comp count set))
       (keep-indexed #(if (= n %2) (+ n %1)))
       (first)))

(defn main 
  ([n] (main n (slurp (io/resource "06.txt"))))
  ([n input] (find-marker-pos n input)))

(defn a
  ([] (main 4))
  ([input] (main 4 input)))

(defn b
  ([] (main 14))
  ([input] (main 14 input)))
