(ns aoc-2022-clojure.day-10 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-line [line]
  (if-let [digits (re-find #"-?\d+" line)]
    {:cmd :add :value (parse-long digits)}
    {:cmd :noop}))

(defn parse-input [input]
  (map parse-line (str/split-lines input)))

(defn add-cmd [{register :register cycles :cycles} cmd]
  (if (= :noop (:cmd cmd))
    {:register register 
     :cycles (conj cycles register)}
    {:register (+ (:value cmd) register) 
     :cycles (into cycles (repeat 2 register))}))

(defn cycle-values [program]
  (:cycles (reduce add-cmd {:register 1 :cycles []} program)))

(defn signal-values [cycles]
  (->> (take 6 (iterate #(+ 40 %) 20))
       (map #(* % (nth cycles (dec %))))
       (reduce +)))

(defn a
  ([] (a (slurp (io/resource "10.txt"))))
  ([input] (->> (parse-input input)
                (cycle-values)
                (signal-values))))

(defn crt [cycles]
  (->> (take 240 (cycle (range 0 40)))
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
  ([input] (let [image (crt (cycle-values (parse-input input)))]
             (println image)
             image)))
