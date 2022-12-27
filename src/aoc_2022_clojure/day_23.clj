(ns aoc-2022-clojure.day-23 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [row-no line]
  (->> line
       seq
       (keep-indexed #(if (= \# %2) %1))
       (map #(vector % row-no))))

(defn parse-input [input]
  (into #{} cat (map parse-line (range) (str/split-lines input))))

(defn neighbors [elf direction]
  (let [directions 
        {:north (for [x [-1 0 1]] [x -1])
         :east (for [y [-1 0 1]] [1 y])
         :south (for [x [-1 0 1]] [x 1])
         :west (for [y [-1 0 1]] [-1 y])}]
    (map #(mapv + elf %) (get directions direction))))

(defn free? [elves elf direction]
  (->> (neighbors elf direction)
       (map #(contains? elves %))
       (every? false?)))

(defn move [elf direction]
  (mapv + elf (case direction
                :north [0 -1]
                :east [1 0]
                :south [0 1]
                :west [-1 0])))

(defn proposed-direction [elves directions elf]
  (let [directions-free (map #(free? elves elf %) directions)]
    (if (every? true? directions-free)
      nil
      (first (keep #(if (second %) (first %)) (map vector directions directions-free))))))

(defn proposed-coordinates [elves directions]
  (let [proposed-directions (pmap #(proposed-direction elves directions %) elves)]
    (pmap #(if (some? %2) (move %1 %2) %1) elves proposed-directions)))

(defn move-all-elves [elves directions]
  (let [proposed-coordinates (proposed-coordinates elves directions)
        proposed-coordinates-freq (frequencies proposed-coordinates)]
    (set (pmap #(if (<= (get proposed-coordinates-freq %2) 1) %2 %1)
               elves
               proposed-coordinates))))

(defn rotate [directions]
  (into [] cat [(subvec directions 1) [(first directions)]]))

(defn simulate [elves]
  (loop [elves elves
         steps 10
         directions [:north :south :west :east]]
    (if (zero? steps)
      elves
      (recur (move-all-elves elves directions)
             (dec steps)
             (rotate directions)))))

(defn min-max [elves]
  (let [xs (map first elves)
        min-x (apply min xs)
        max-x (apply max xs)
        ys (map second elves)
        min-y (apply min ys)
        max-y (apply max ys)]
    {:min-x min-x
     :max-x max-x
     :min-y min-y
     :max-y max-y}))

(defn empty-ground-tiles [elves]
  (let [{min-x :min-x max-x :max-x
         min-y :min-y max-y :max-y} (min-max elves)]
    (- (* (inc (- max-x min-x)) (inc (- max-y min-y))) (count elves))))

(defn a
  ([] (a (slurp (io/resource "23.txt"))))
  ([input] (->> input
                parse-input
                simulate
                empty-ground-tiles)))

(defn steps-until-done [elves]
  (loop [elves elves
         steps 0
         directions [:north :south :west :east]]
    (let [moved (move-all-elves elves directions)]
      (if (= elves moved)
        (inc steps)
        (recur moved
               (inc steps)
               (rotate directions))))))

(defn b
  ([] (b (slurp (io/resource "23.txt"))))
  ([input] (->> input
                parse-input
                steps-until-done)))
