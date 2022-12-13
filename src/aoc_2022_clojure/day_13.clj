(ns aoc-2022-clojure.day-13
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [input]
  (map read-string (str/split-lines (str/replace input #"\n\n" "\n"))))

(defn compare-packets [packet-1 packet-2]
  (cond (or (nil? packet-1) (nil? packet-2))
        (compare packet-1 packet-2)

        (and (number? packet-1) (number? packet-2))
        (compare packet-1 packet-2)

        (and (vector? packet-1) (number? packet-2))
        (compare-packets packet-1 [packet-2])

        (and (number? packet-1) (vector? packet-2))
        (compare-packets [packet-1] packet-2)

        (or (empty? packet-1) (empty? packet-2))
        (compare packet-1 packet-2)

        (and (vector? packet-1) (vector? packet-2))
        (let [compared (compare-packets (first packet-1) (first packet-2))]
          (if (zero? compared)
            (compare-packets (subvec packet-1 1) (subvec packet-2 1))
            compared))))

(defn a
  ([] (a (slurp (io/resource "13.txt"))))
  ([input] (->> (parse-input input)
                (partition 2)
                (map #(apply compare-packets %))
                (keep-indexed #(if (= -1 %2) (inc %1)))
                (reduce +))))

(defn b
  ([] (b (slurp (io/resource "13.txt"))))
  ([input] (let [divider-packets #{[[2]] [[6]]}]
             (->> (concat (parse-input input) divider-packets)
                  (sort compare-packets)
                  (keep-indexed #(if (contains? divider-packets %2) (inc %1)))
                  (reduce *)))))
