(ns aoc-2022-clojure.day-20 
  (:require [clojure.java.io :as io]))

(defn parse-input [input]
  (->> (re-seq #"-?\d+" input)
       (map-indexed #(hash-map :current-position %1
                               :starting-position %1
                               :value (parse-long %2)))
       (vec)))

(defn new-index [old delta length]
  (let [res (mod (+ old delta) (dec length))]
    (cond
      (zero? res) (dec length)
      (= (dec length) res) 0
      :else res)))

(defn update-positions [numbers start end f]
  (vec
   (map-indexed
    (fn [idx val] (if (<= start idx (dec end)) (update val :current-position f) val))
    numbers)))

(defn insert [vs pos val]
  (let [[before after] (split-at pos vs)]
    (into [] cat [before [val] after])))

(defn delete [vs pos]
  (let [before (subvec vs 0 pos)
        after (subvec vs (inc pos))]
    (into [] cat [before after])))

(defn move [mixed number]
  (let [n (:value number)
        pos (:current-position number)
        new-idx (new-index pos n (count mixed))
        first-pos (min pos new-idx)
        last-pos (max pos new-idx)
        direction (if (< new-idx pos) :left :right)]
    (cond-> (delete mixed pos)
      (= direction :left)
      (-> (insert new-idx (assoc number :current-position new-idx))
          (update-positions (inc first-pos) (inc last-pos) inc))

      (= direction :right)
      (-> (insert new-idx (assoc number :current-position new-idx))
          (update-positions first-pos last-pos dec)))))

(defn step [numbers numbers-idx mixed]
  (if (zero? (:value (nth numbers numbers-idx)))
    [numbers mixed]
    (let [number (nth numbers numbers-idx)
          moved-mixed (move mixed number)
          n (:value number)
          pos (:current-position number)
          new-idx (new-index pos n (count mixed))
          first-pos (min pos new-idx)
          last-pos (max pos new-idx)
          updated-numbers (into (hash-map) (map #(vector (:starting-position %) %)
                                                (subvec moved-mixed first-pos (inc last-pos))))
          new-numbers (mapv #(let [starting-pos (:starting-position %)]
                               (if (contains? updated-numbers starting-pos)
                                 (assoc % :current-position
                                        (:current-position (get updated-numbers starting-pos)))
                                 %))
                            numbers)]
      [new-numbers moved-mixed])))

(defn mix [iterations numbers]
  (let [mixed (into (vector) numbers)]
    (loop [iterations iterations
           numbers numbers
           idx 0
           mixed mixed]
      (if (zero? iterations)
        mixed
        (let [[numbers mixed] (step numbers idx mixed)
              next-idx (mod (inc idx) (count numbers))
              next-iterations (if (zero? next-idx) (dec iterations) iterations)]
          (if (< next-iterations iterations)
            (println "remaining iteration" iterations))
          (recur next-iterations
                 numbers
                 next-idx
                 mixed))))))

(defn grove-coordinates [numbers]
  (let [zero-pos (first (keep-indexed #(if (zero? (:value %2)) %1) numbers))
        pos [1000 2000 3000]]
    (map #(:value (nth numbers (mod (+ zero-pos %) (count numbers)))) pos)))

(defn a
  ([] (a (slurp (io/resource "20.txt"))))
  ([input] (->> input
                parse-input
                (mix 1)
                grove-coordinates
                (reduce +))))

(defn b
  ([] (b (slurp (io/resource "20.txt"))))
  ([input] (->> input
                parse-input
                (map (fn [number] (update number :value #(* 811589153 %))))
                (mix 10)
                grove-coordinates
                (reduce +))))
