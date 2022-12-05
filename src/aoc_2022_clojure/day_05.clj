(ns aoc-2022-clojure.day-05
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn trim-stack [stack] (vec (take-while #(Character/isAlphabetic (int %)) stack)))

(defn parse-stacks [stacks]
  (let [rows (str/split-lines stacks)
        reversed (map seq (drop 1 (reverse rows)))]
    (->> (apply map vector reversed)
         (filter #(Character/isAlphabetic (int (first %))))
         (map trim-stack)
         (vec))))

(defn parse-commands [cmds]
  (->> (str/split-lines cmds)
       (map #(re-seq #"\d+" %))
       (map #(map parse-long %))
       (map (fn [[a b c]] {:count a :source (dec b) :destination (dec c)}))))

(defn parse-input [input]
  (let [[stacks cmds] (str/split input #"\n\n")]
    {:stacks (parse-stacks stacks) :commands (parse-commands cmds)}))

(defn execute-command [mode-fn
                       stacks
                       {cnt :count source :source destination :destination}]
  (let [source-stack (nth stacks source)
        height (count source-stack)
        [lower upper] (map vec (split-at (- height cnt) source-stack))
        dest-stack (nth stacks destination)]
    (assoc stacks
           source lower
           destination (into dest-stack (mode-fn upper)))))

(defn rearrange [f {stacks :stacks commands :commands}]
  (reduce f stacks commands))

(defn tops [stacks] (str/join (map peek stacks)))

(defn main
  ([part] (main part (slurp (io/resource "05.txt"))))
  ([part input] (let [f (cond (= part :a) (partial execute-command reverse)
                              (= part :b) (partial execute-command identity))]
                  (tops (rearrange f (parse-input input))))))

(defn a
  ([] (main :a))
  ([input] (main :a input)))

(defn b
  ([] (main :b))
  ([input] (main :b input)))
