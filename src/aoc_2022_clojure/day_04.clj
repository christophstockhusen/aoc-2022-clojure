(ns aoc-2022-clojure.day-04 
  (:require [clojure.java.io :as io]))

(defn parse-input [input]
  (->> (re-seq #"\d+" input)
       (map parse-long)
       (partition 2)
       (partition 2)))

(defn fully-contained? [[a b] [c d]]
  (let [overlap [(max a c) (min b d)]]
    (or (= overlap [a b])
        (= overlap [c d]))))

(defn overlap? [[a b] [c d]]
  (<= (max a c) (min b d)))

(defn main [input pred]
  (count (filter pred (parse-input input))))

(defn a
  ([] (a (slurp (io/resource "04.txt"))))
  ([input] (main input #(apply fully-contained? %))))

(defn b
  ([] (b (slurp (io/resource "04.txt"))))
  ([input] (main input #(apply overlap? %))))
