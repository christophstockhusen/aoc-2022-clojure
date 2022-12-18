(ns aoc-2022-clojure.day-18 
  (:require [clojure.java.io :as io]))

(defn parse-input [input]
  (->> input
       (re-seq #"\d+")
       (map parse-long)
       (partition 3)
       (map vec)))

(defn faces [cube]
  (for [i (range (count cube)) 
        f [inc dec]] 
    (update cube i f)))

(defn add-cube [{cubes :cubes counter :counter} cube]
  (let [free-sides (filter (complement cubes) (faces cube))]
    {:cubes (conj cubes cube)
     :counter (- (+ counter (* 2 (count free-sides))) 6)}))

(defn uncovered-faces [cubes]
  (:counter (reduce add-cube {:cubes #{} :counter 0} cubes)))

(defn a
  ([] (a (slurp (io/resource "18.txt"))))
  ([input] (uncovered-faces (parse-input input))))

(defn in-box? [min-v max-v vs]
  (every? #(<= min-v % max-v) vs ))

(defn surface-area [cubes]
  (let [min-v (dec (apply min (flatten cubes)))
        max-v (inc (apply max (flatten cubes)))
        cubes (set cubes)
        start [min-v min-v min-v]]
    (loop [q (conj clojure.lang.PersistentQueue/EMPTY start)
           enqueued #{start}
           face-cnt 0]
      (if (empty? q)
        face-cnt
        (let [neighbors (filter (partial in-box? min-v max-v)
                                (faces (peek q)))
              unconsidered (->> neighbors
                                (filter (complement enqueued))
                                (filter (complement cubes)))]
          (recur (into (pop q) unconsidered)
                 (into enqueued unconsidered)
                 (+ face-cnt (count (filter cubes neighbors)))))))))
(defn b
  ([] (b (slurp (io/resource "18.txt"))))
  ([input] (surface-area (parse-input input))))
