(ns aoc-2022-clojure.day-22 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input 
"        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5")

(defn parse-line [row-no line]
  (->> (seq line)
       (map-indexed (fn [col-no c] [[col-no row-no] c]))
       (filter #(#{\# \.} (second %)))
       (map #(vector (first %) (if (= \. (second %)) :open :closed)))
       (into (hash-map))))

(defn parse-map [s]
  (->> (str/split-lines s)
       (map parse-line (range))
       (apply merge)))

(defn parse-moves [moves]
  (map #(cond
          (= % "R") :right
          (= % "L") :left
          :else (parse-long %))
       (re-seq #"[LR]|\d+" moves)))

(defn starting-position [monkey-map]
  (apply min (keep (fn [[[x y] status]] (if (and (= y 0)
                                                 (= status :open)) x)) monkey-map)))
(defn parse-input [input]
  (let [[monkey-map moves] (str/split input #"\n\n")
        monkey-map (parse-map monkey-map)]
    {:monkey-map monkey-map
     :moves (parse-moves moves)
     :starting-position (starting-position monkey-map)}))

(defn turn [position direction]
  (let [directions [:east :south :west :north]
        idx (first (keep-indexed #(if (= %2 (:direction position)) %1) directions))
        idx (if (= :right direction) (inc idx) (dec idx))
        idx (mod idx (count directions))]
    (assoc position :direction (nth directions idx))))

(defn next-coordinate-a [monkey-map {x :x y :y direction :direction :as position}]
  (let [[x' y']
        (case direction
          :east (if (contains? monkey-map [(inc x) y])
                  [(inc x) y]
                  [(apply min (keep (fn [[[x' y'] _]] (if (= y' y) x')) monkey-map)) y])

          :west (if (contains? monkey-map [(dec x) y])
                  [(dec x) y]
                  [(apply max (keep (fn [[[x' y'] _]] (if (= y' y) x')) monkey-map)) y])

          :north (if (contains? monkey-map [x (dec y)])
                   [x (dec y)]
                   [x (apply max (keep (fn [[[x' y'] _]] (if (= x' x) y')) monkey-map))])

          :south (if (contains? monkey-map [x (inc y)])
                   [x (inc y)]
                   [x (apply min (keep (fn [[[x' y'] _]] (if (= x' x) y')) monkey-map))]))]
    (assoc position :x x' :y y')))

(defn wall? [monkey-map {x :x y :y}]
  (= :closed (get monkey-map [x y])))

(defn move-forward [monkey-map next-coordinate-fn position steps] 
  (loop [steps steps
         position position]
    (let [next-position (next-coordinate-fn monkey-map position)] 
      (if (or (zero? steps)
              (wall? monkey-map next-position))
        position
        (recur (dec steps)
               next-position)))))

(defn step [monkey-map next-coordinate-fn position cmd]
  (cond
    (#{:right :left} cmd) (turn position cmd)
    :else (move-forward monkey-map next-coordinate-fn position cmd)))

(defn walk [{monkey-map :monkey-map starting-position :starting-position moves :moves} next-coordinate-fn]
  (let [start {:x starting-position
               :y 0
               :direction :east}]
    (reduce (partial step monkey-map next-coordinate-fn) start moves)))

(defn password [position]
  (let [facings {:east 0 :south 1 :west 2 :north 3}]
    (+ (* 1000 (inc (:y position))) 
       (* 4 (inc (:x position))) 
       (get facings (:direction position)))))

(defn a
  ([] (a (slurp (io/resource "22.txt"))))
  ([input] (let [parsed (parse-input input)]
             (->> (walk parsed next-coordinate-a)
                  password))))

(defn next-coordinate-b [monkey-map {x :x y :y direction :direction :as position}]
  (let [[[x' y'] direction'] 
        (case direction
          :east (let [[x' y'] [(inc x) y]]
                  (cond
                    (and (= 150 x') (<= 0 y' 49)) [[99 (- 149 y')] :west]
                    (and (= 100 x') (<= 50 y' 99)) [[(+ 100 (- y' 50)) 49] :north]
                    (and (= 100 x') (<= 100 y' 149)) [[149 (- 49 (- y' 100))] :west]
                    (and (= 50 x') (<= 150 y' 199)) [[(+ 50 (- y' 150)) 149] :north]
                    :else [[x' y'] :east])) 

          :west (let [[x' y'] [(dec x) y]]
                  (cond
                    (and (= 49 x') (<= 0 y' 49)) [[0 (- 149 y')] :east]
                    (and (= 49 x') (<= 50 y' 99)) [[(- y' 50) 100] :south]
                    (and (= -1 x') (<= 100 y' 149)) [[50 (- 49 (- y' 100))] :east]
                    (and (= -1 x') (<= 150 y' 199)) [[(+ 50 (- y' 150)) 0] :south]
                    :else [[x' y'] :west]))

          :north (let [[x' y'] [x (dec y)]]
                   (cond
                     (and (<= 0 x' 49) (= y' 99)) [[50 (+ 50 x')] :east]
                     (and (<= 50 x' 99) (= -1 y')) [[0 (+ 150 (- x' 50))] :east]
                     (and (<= 100 x' 149) (= -1 y')) [[(- x' 100) 199] :north]
                     :else [[x' y'] :north]))

          :south (let [[x' y'] [x (inc y)]]
                   (cond
                     (and (<= 0 x' 49) (= 200 y')) [[(+ 100 x') 0] :south]
                     (and (<= 50 x' 99) (= 150 y')) [[49 (+ 150 (- x' 50))] :west]
                     (and (<= 100 x' 149) (= y' 50)) [[99 (+ 50 (- x' 100))] :west]
                     :else [[x' y'] :south])))]
    (assoc position :x x' :y y' :direction direction')))

(defn b
  ([] (b (slurp (io/resource "22.txt"))))
  ([input] (let [parsed (parse-input input)
                 walked (walk parsed next-coordinate-b)]
             (password walked))))
