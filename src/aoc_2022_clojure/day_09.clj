(ns aoc-2022-clojure.day-09 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map #(vector (first %) (parse-long (second %))))))

(defn next-coordinate [xy direction]
  (let [deltas {"U" [0 1]
                "R" [1 0]
                "D" [0 -1]
                "L" [-1 0]}]
    (vec (map + xy (get deltas direction)))))

(defn too-distant? [v w]
  (< 1 (reduce max (map #(abs (- %1 %2)) v w))))

(defn compare-positions [v w]
  (map compare v w))

(defn move [start-new next-knot]
  (let [last-new (peek start-new)
        compared (compare-positions last-new next-knot)]
    (if (too-distant? last-new next-knot)
      (conj start-new (vec (map + next-knot compared)))
      (conj start-new next-knot))))

(defn simulate-step [state [direction steps]]
  (loop [state state
         steps steps]
    (if (zero? steps)
      state
      (let [next-head (next-coordinate (first (:rope state)) direction)
            next-rope (reduce move [next-head] (subvec (:rope state) 1))]
        (recur (assoc state
                      :rope next-rope
                      :visited (conj (:visited state) (peek next-rope)))
               (dec steps))))))

(defn simulate [length lines]
  (reduce 
   (fn [state cmd] (simulate-step state cmd))
   {:rope (vec (repeat length [0 0])) :visited #{[0 0]}}
   lines))

(defn main [input length]
  (->> (parse-input input)
       (simulate length)
       (:visited)
       (count)))

(defn a
  ([] (a (slurp (io/resource "09.txt"))))
  ([input] (main input 2)))

(defn b
  ([] (b (slurp (io/resource "09.txt"))))
  ([input] (main input 10)))
