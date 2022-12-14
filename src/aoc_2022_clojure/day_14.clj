(ns aoc-2022-clojure.day-14
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-coordinate [c]
  (map parse-long (str/split c #",")))

(defn parse-line [line]
  (map parse-coordinate (re-seq #"\d+,\d+" line)))

(defn add-segment-to-cave [cave [[x1 y1] [x2 y2]]]
  (cond
    (= x1 x2) (->> (range (min y1 y2) (inc (max y1 y2)))
                   (map vector (repeat x1))
                   (into cave))
    (= y1 y2) (->> (range (min x1 x2) (inc (max x1 x2)))
                   (map vector (repeat y1))
                   (map reverse)
                   (into cave))))

(defn add-path-to-cave [cave path]
  (let [segments (partition 2 1 path)]
    (reduce add-segment-to-cave cave segments)))

(defn parse-input [input]
  (let [paths (map parse-line (str/split-lines input))
        blocked (reduce add-path-to-cave (hash-set) paths)]
    {:max-y (reduce max (map second blocked))
     :min-x (reduce min (map first blocked))
     :max-x (reduce max (map first blocked))
     :left 0
     :right 0
     :blocked blocked}))

(defn triangular-numbers-sequence []
  (reductions + (range)))

(defn height-of-triangle-with-n-elements [n]
  (dec (first (keep-indexed #(if (< n %2) %1) 
                            (triangular-numbers-sequence)))))

(defn next-sand-pos [cave [x y]]
  (let [{blocked :blocked
         max-y :max-y
         left :left
         right :right
         min-x :min-x
         max-x :max-x} cave]
    (cond
      (< max-y y)
      nil

      (not (contains? blocked [x (inc y)]))
      [x (inc y)]

      (and (not (contains? blocked [(dec x) (inc y)]))
           (not (and (= x (dec min-x))
                     (<= (- max-y (height-of-triangle-with-n-elements left)) (inc y)))))
      [(dec x) (inc y)]

      (and (not (contains? blocked [(inc x) (inc y)]))
           (not (and (= x (inc max-x))
                     (<= (- max-y (height-of-triangle-with-n-elements right)) (inc y)))))
      [(inc x) (inc y)]

      :else [x y])))

(defn add-sand-to-cave [cave _]
  (loop [sand-pos [500 0]]
    (let [next-pos (next-sand-pos cave sand-pos)]
      (cond
        (nil? next-pos)
        (assoc cave :inserted false)
        
        (contains? (:blocked cave) [500 0])
        (assoc cave :inserted false)
        
        (= sand-pos next-pos)
        (-> (update cave :blocked #(conj % sand-pos))
            (assoc :inserted true))

        (< (first sand-pos) (dec (:min-x cave)))
        (-> (update cave :left inc)
            (assoc :inserted true))

        (< (inc (:max-x cave)) (first sand-pos))
        (-> (update cave :right inc)
            (assoc :inserted true))

        :else (recur next-pos))
      )))

(defn print-cave [cave]
  (let [min-x (dec (:min-x cave))
        max-x (inc (:max-x cave))
        min-y 0
        max-y (:max-y cave)]
    (->> (range min-y (inc max-y))
         (map (fn [row-no]
                (map #(cond
                        (= [% row-no] [500 0]) "+"
                        (contains? (:blocked cave) [% row-no]) "#"
                        :else ".")
                     (range min-x (inc max-x)))))
         (map str/join)
         (str/join "\n"))))

(defn a
  ([] (a (slurp (io/resource "14.txt"))))
  ([input] (->> (range)
                (reductions add-sand-to-cave 
                            (assoc (parse-input input) :inserted true))
                (keep-indexed #(if (not (:inserted %2)) %1))
                first
                dec)))

(defn add-floor [cave]
  (let [min-x (:min-x cave)
        max-x (:max-x cave)
        floor-y (+ 2 (:max-y cave))]
    (-> (update cave :blocked #(add-segment-to-cave % [[(dec min-x) floor-y] 
                                                       [(inc max-x) floor-y]]))
        (update :max-y #(+ 2 %)))))

(defn b
  ([] (b (slurp (io/resource "14.txt"))))
  ([input] (let [cave (add-floor (parse-input input))]
             (->> (range)
                  (reductions add-sand-to-cave (assoc cave :inserted true))
                  (keep-indexed #(if (not (:inserted %2)) %1))
                  first
                  dec))))
