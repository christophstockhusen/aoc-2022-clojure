(ns aoc-2022-clojure.day-12 
  (:require [clojure.data.priority-map :refer [priority-map]]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn parse-line [row-idx line]
  (->> (map (fn [col-idx c] [[col-idx row-idx] c]) (range) (seq line))
       (into (hash-map))))

(defn val->number [v]
  (int (case v 
         \S \a 
         \E \z 
         v)))

(defn char-map->number-map [height-map]
  (into (hash-map) (map (fn [[xy h]] [xy (val->number h)]) height-map) ))

(defn start-and-target [char-map]
  (let [find-in-map (fn [v] (first (first (filter #(= v (second %)) char-map))))]
    [(find-in-map \S) (find-in-map \E)]))

(defn at-most-one-higher? [height-map v w]
  (let [v-height (get-in height-map [:vals v])
        w-height (get-in height-map [:vals w])]
    (<= (- w-height v-height) 1)))

(defn inside-height-map? [height-map [x y]]
  (and (< -1 x (:x-size height-map))
       (< -1 y (:y-size height-map))))

(defn neighbors [height-map [x y]]
  (let [deltas  [[-1 0] [0 -1] [1 0] [0 1]]
        neighs (mapv #(mapv + [x y] %) deltas)
        neighs (filter (partial inside-height-map? height-map) neighs)]
    (filter (partial at-most-one-higher? height-map [x y]) neighs)))

(defn add-node-to-graph [height-map graph node]
  (let [neighbors (neighbors height-map node)]
    (reduce (fn [g n] (assoc-in g [node n] 1)) graph neighbors)))

(defn height-map->graph [height-map]
  (let [nodes (keys (:vals height-map))]
    (reduce (partial add-node-to-graph height-map) {} nodes)))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        height-map-lines (map parse-line (range) lines)
        height-map-of-chars (apply merge height-map-lines)
        [start target] (start-and-target height-map-of-chars)
        height-map-of-numbers (char-map->number-map height-map-of-chars)
        height-map {:vals height-map-of-numbers 
                    :x-size (count (first lines)) 
                    :y-size (count lines)}]
    {:start start 
     :target target 
     :graph (height-map->graph height-map)
     :height-map height-map}))

(defn min-direct-dist [graph q v d t]
  (let [s-t-dist (+ d (get-in graph [v t]))]
    (if-let [enqueued-dist (get q t)]
      (min enqueued-dist s-t-dist)
      s-t-dist)))

(defn update-distances-in-queue [q graph visited v d]
  (let [neighbors (set (keys (get graph v)))
        unvisited (set/difference neighbors visited)
        unvisited-dists (map (partial min-direct-dist graph q v d) unvisited)
        unvisited-and-dists (map vector unvisited unvisited-dists)]
    (into q unvisited-and-dists)))

(defn shortest-path [graph start target]
  (-> (loop [dists {}
             visited #{}
             q (priority-map start 0)]
        (if (empty? q)
          dists
          (let [[current-v current-d] (peek q)
                dists (assoc dists current-v current-d)
                visited (conj visited current-v)
                q (update-distances-in-queue (pop q) graph visited current-v current-d)]
            (recur dists visited q))))
      (get target)))

(defn a
  ([] (a (slurp (io/resource "12.txt"))))
  ([input] (let [{start :start target :target graph :graph} (parse-input input)]
             (shortest-path graph start target))))

(defn starting-positions [height-map]
  (let [nodes (:vals height-map)]
    (map first (filter #(= (second %) (int \a)) nodes))))

(defn b
  ([] (b (slurp (io/resource "12.txt"))))
  ([input] (let [parsed (parse-input input)
                 starts (starting-positions (:height-map parsed))
                 graph-with-new-start (reduce #(assoc-in %1 [:start %2] 0) (:graph parsed) starts)]
             (shortest-path graph-with-new-start :start (:target parsed)))))
