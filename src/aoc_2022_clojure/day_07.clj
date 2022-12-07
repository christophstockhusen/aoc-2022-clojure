(ns aoc-2022-clojure.day-07 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.zip :as zip]))

(defn dir-zip [root]
  (zip/zipper (fn [node] (= :dir (:type node)))
              (fn [node] (:children node))
              (fn [node children] (assoc node :children (vec children)))
              root))

(defn parse-line [line]
  (cond
    (= line "$ cd ..") {:command :up}
    (str/starts-with? line "$ cd") {:command :down :name (last (str/split line #" "))}
    (= line "$ ls") {:command :ls}
    (str/starts-with? line "dir") {:type :dir :name (last (str/split line #" "))}
    :else {:type :file 
           :name (last (str/split line #" ")) 
           :size (parse-long (first (str/split line #" ")))}))

(defn parse-lines [lines] (map parse-line lines))

(defn parse-tree [lines]
  (let [loc (dir-zip {:type :dir :name "/"})]
    (-> (loop [loc loc
               lines lines] 
          (if (empty? lines)
            loc
            (let [line (first lines)]
              (cond
                (= :up (:command line)) (recur (zip/up loc) (rest lines))
                (= :down (:command line)) (recur (->> {:type :dir
                                                       :name (:name line)
                                                       :children []}
                                                      (zip/append-child loc)
                                                      zip/down
                                                      zip/rightmost) 
                                                 (rest lines))
                (= :ls (:command line)) (recur loc (rest lines))
                (= :file (:type line)) (recur (zip/append-child loc line) (rest lines))
                :else (recur loc (rest lines))))))
        (zip/root))))

(defn parse-input [input]
  (parse-tree (parse-lines (rest (str/split-lines input)))))

(defn sizes [loc]
  (cond
    (= :file (:type loc)) {:size (:size loc) :sub-dir-sizes []}
    (= :dir (:type loc)) (let [dirs (map sizes (:children loc))
                               size (reduce + (map :size dirs))
                               sub-dir-sizes (conj (reduce concat (map :sub-dir-sizes dirs)) 
                                                   size)]
                           {:size size :sub-dir-sizes sub-dir-sizes})))

(defn a
  ([] (a (slurp (io/resource "07.txt"))))
  ([input] (->> (parse-input input)
                (sizes)
                (:sub-dir-sizes)
                (filter #(<= % 100000))
                (reduce +))))

(defn b
  ([] (b (slurp (io/resource "07.txt"))))
  ([input] (let [sizes (:sub-dir-sizes (sizes (parse-input input)))
                 largest (apply max sizes)
                 available (- 70000000 largest)
                 required (- 30000000 available)]
             (first (sort (filter #(<= required %) sizes))))))