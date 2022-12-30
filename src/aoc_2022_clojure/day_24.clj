(ns aoc-2022-clojure.day-24 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-row [row-no row-seq]
  (let [translations {\^ :up \> :right \v :down \< :left \. nil}]
    (->> (map #(get translations %) row-seq)
         (map (fn [col-no c] [[col-no row-no] c]) (range))
         (filter #(some? (second %)))
         (into (hash-map)))))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        height (- (count lines) 2)
        width (- (count (first lines)) 2)
        points (->> lines
                    rest
                    drop-last
                    (map #(rest (drop-last (seq %))))
                    (map parse-row (range))
                    (apply merge))
        directions [:up :right :down :left]
        blizzards (->> directions
                       (map (fn [direction] (filter #(= direction (second %)) points)))
                       (map #(map first %))
                       (map #(into (hash-set) %))
                       (map vector directions)
                       (into (hash-map)))]
    (assoc blizzards :height height :width width :directions directions)))

(defn move-blizzard [direction width height [x y]]
  (case direction
    :up [x (mod (dec y) height)]
    :right [(mod (inc x) width) y]
    :down [x (mod (inc y) height)]
    :left [(mod (dec x) width) y]))

(defn next-blizzard [blizzards]
  (let [width (:width blizzards)
        height (:height blizzards)
        directions (:directions blizzards)
        blizzards (->> (map (fn [d] (map #(move-blizzard d width height %)
                                         (get blizzards d)))
                            directions)
                       (map #(into (hash-set) %))
                       (map vector directions)
                       (into (hash-map)))]
    (assoc blizzards :width width :height height :directions directions)))

(def blizzard
  (memoize
   (fn [b i]
     (if (zero? i) 
       b
       (next-blizzard (blizzard b (dec i)))))))

(defn neighbor-positions [pos width height]
  (let [deltas [[-1 0] [0 -1] [1 0] [0 1]]
        candidates (map #(mapv + pos %) deltas)
        start-pos [0 -1]
        target-pos [(dec width) height]]
    (filter #(or (and (< -1 (first %) width)
                      (< -1 (second %) height))
                 (= % start-pos)
                 (= % target-pos)) candidates)))

(defn no-blizzard? [blizzard [x y]]
  (not-any? #(contains? (get blizzard %) [x y])
            (:directions blizzard)))

(defn fewest-steps [initial-blizzard start target initial-step]
  (let [start {:pos start :step initial-step}
        target-pos target]
    (loop [enqueued #{start}
           q (conj clojure.lang.PersistentQueue/EMPTY start)]
      (let [current (peek q)]
        (if (= (:pos current) target-pos)
          current
          (let [next-step (inc (:step current))
                next-blizzard (blizzard initial-blizzard next-step)
                position-candidates (conj
                                     (neighbor-positions
                                      (:pos current)
                                      (:width next-blizzard)
                                      (:height next-blizzard))
                                     (:pos current))
                reachable-positions (filter #(no-blizzard? next-blizzard %) position-candidates)
                reachable-confs (map #(hash-map :pos % :step next-step) reachable-positions)
                new-confs (filter #(not (contains? enqueued %)) reachable-confs)]
            (recur (into enqueued new-confs)
                   (into (pop q) new-confs))))))))

(defn a
  ([] (a (slurp (io/resource "24.txt"))))
  ([input] (let [initial-blizzard (parse-input input)
                 start [0 -1]
                 target [(dec (:width initial-blizzard)) (:height initial-blizzard)]] 
             (:step (fewest-steps initial-blizzard start target 0)))))

(defn b
  ([] (b (slurp (io/resource "24.txt"))))
  ([input] (let [initial-blizzard (parse-input input)
                 start [0 -1]
                 target [(dec (:width initial-blizzard)) (:height initial-blizzard)]
                 trip-1 (fewest-steps initial-blizzard start target 0)
                 trip-2 (fewest-steps initial-blizzard target start (:step trip-1))
                 trip-3 (fewest-steps initial-blizzard start target (:step trip-2))]
             (:step trip-3))))
