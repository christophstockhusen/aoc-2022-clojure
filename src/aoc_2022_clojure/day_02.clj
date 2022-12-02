(ns aoc-2022-clojure.day-02 
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))))

(defn translate [x]
  (get {"A" :rock
        "B" :paper
        "C" :scissors
        "X" :rock
        "Y" :paper
        "Z" :scissors} x))

(defn score [x]
  (get {:rock 1
        :paper 2
        :scissors 3} x))

(def win {:rock :paper
          :paper :scissors
          :scissors :rock})

(defn evaluate [[a b]]
  (let [a (translate a)
        b (translate b)]
    (cond-> (score b)
      (= a b) (+ 3)
      (= b (get win a)) (+ 6)
      :else (identity))))

(defn a
  ([] (a (slurp (io/resource "02.txt"))))
  ([input] (->> (parse-input input)
                (map evaluate)
                (reduce +))))

(defn cmd [x]
  (get {"X" :lose
        "Y" :draw
        "Z" :win} x))

(defn evaluate2 [[a b]]
  (let [a (translate a)
        b (cmd b)]
    (cond
      (= :draw b) (+ 3 (score a))
      (= :win b) (+ 6 (score (get win a)))
      (= :lose b) (score (get (set/map-invert win) a)))))

(defn b
  ([] (b (slurp (io/resource "02.txt"))))
  ([input] (->> (parse-input input)
                (map evaluate2)
                (reduce +))))
