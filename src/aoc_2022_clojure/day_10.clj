(ns aoc-2022-clojure.day-10 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn add-cmd [{register :register cycles :cycles} cmd]
  (if-let [digits (re-find #"-?\d+" cmd)]
    {:register (+ (parse-long digits) register)
     :cycles (into cycles (repeat 2 register))}
    {:register register 
     :cycles (conj cycles register)})) 

(defn cycle-values [program]
  (:cycles (reduce add-cmd {:register 1 :cycles []} program)))

(defn signal-values [cycles]
  (->> (take 6 (iterate #(+ 40 %) 20))
       (map #(* % (nth cycles (dec %))))
       (reduce +)))

(defn a
  ([] (a (slurp (io/resource "10.txt"))))
  ([input] (signal-values (cycle-values (str/split-lines input)))))

(defn crt [cycles]
  (->> (cycle (range 0 40))
       (map vector cycles)
       (map (fn [[reg pos]]
              (if (<= (dec reg) pos (inc reg))
                "#"
                ".")))
       (partition 40)
       (map str/join)
       (str/join "\n")))

(defn b
  ([] (b (slurp (io/resource "10.txt"))))
  ([input] (let [image (crt (cycle-values (str/split-lines input)))]
             (println image)
             image)))