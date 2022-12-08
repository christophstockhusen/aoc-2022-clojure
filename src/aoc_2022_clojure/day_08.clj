(ns aoc-2022-clojure.day-08
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [row-no line]
  (->>  (seq line)
        (map (comp parse-long str))
        (map (fn [col-no height] [[col-no row-no] height]) (range))
        (into (hash-map))))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        size (count lines)
        trees (apply merge (map parse-line (range) lines))]
   {:trees trees :size size}))

(defn visible? [{height :height :as tree}]
  (let [directions [:west :north :east :south]
        distances (map #(keys (get tree %)) directions)]
    (not (and (every? some? distances)
              (every? (fn [ds] (not-every? #(< % height) ds)) distances)))))

(defn count-visible-trees [trees]
  (count (filter visible? (vals trees))))

(defn coordinates [size direction [idx1 idx2]]
  (case direction
    :west [idx1 idx2]
    :north [(- size idx2 1) idx1]
    :east [(- size idx1 1) (- size idx2 1)]
    :south [idx2 (- size idx1 1)]))

(defn prev-coordinates [size direction [idx1 idx2]]
  (coordinates size direction [(dec idx1) idx2]))

(defn scan-from-direction [{trees :trees size :size} direction]
  (->>
   (range size) 
   (reduce ;; Move scanline along first direction
    (fn [heights idx1]
      (->>
       (reduce ;; Collect results along scanline
        (fn [heights idx2]
          (let [cs (coordinates size direction [idx1 idx2])
                prev-cs (prev-coordinates size direction [idx1 idx2])
                tmp (->
                     (reduce ;; Set height dists for all heights
                      (fn [m h]
                        (if-let [prev-height-dist (get-in heights [prev-cs direction h])]
                          (assoc-in m [cs direction h] (inc prev-height-dist))
                          m))
                      heights
                      (range 10)))]
            (-> (if-let [prev-height (get trees prev-cs)]
                  (assoc-in tmp [cs direction prev-height] 1)
                  tmp)
                (assoc-in [cs :height] (get trees cs)))))
        heights (range size)))) {})))

(defn add-heights [trees]
  (->> [:north :east :south :west]
       (map (partial scan-from-direction trees))
       (apply merge-with merge)))

(defn a
  ([] (a (slurp (io/resource "08.txt"))))
  ([input]
   (->> (parse-input input)
        (add-heights)
        (filter #(visible? (second %)))
        (count-visible-trees))))

(defn view-distance [size [[x y] props] direction]
  (let [blocking (keep #(if (<= (:height props) (first %)) (second %)) 
                       (get props direction))
        fallback (case direction
                   :west x
                   :north y
                   :east (- size x 1)
                   :south (- size y 1))]
    (if (seq blocking) (apply min blocking) fallback)))

(defn scenic-score [size tree]
  (reduce * (map #(view-distance size tree %) [:north :east :south :west])))

(defn b
  ([] (b (slurp (io/resource "08.txt"))))
  ([input]
   (let [parsed (parse-input input)
         size (:size parsed)]
     (->> parsed
          (add-heights)
          (map (partial scenic-score size))
          (apply max)))))
