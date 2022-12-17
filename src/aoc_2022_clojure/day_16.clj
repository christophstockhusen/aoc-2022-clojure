(ns aoc-2022-clojure.day-16
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn parse-line [line]
  (let [rate (parse-long (re-find #"\d+" line))
        valves (re-seq #"[A-Z]{2}" line)
        valve (first valves)
        targets (rest valves)]
    {valve {:rate rate
            :targets targets}}))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map parse-line)
       (apply merge)))

(defn flow-value [network open-valves]
  (reduce + (map #(get-in network [% :rate]) open-valves)))

(def max-release
  (memoize
   (fn [network minutes current open]
     (if (zero? minutes)
       {open 0}
       (let [current-valve (get network current)
             current-flow (flow-value network open)
             minutes' (dec minutes)
             next-valves (:targets current-valve)
             results (cond-> []
                       (and (< 0 (:rate current-valve))
                            (not (contains? open current)))
                       (conj (max-release network minutes' current (conj open current)))

                       :else
                       (concat (map #(max-release network minutes' % open) next-valves)))]
         (->> (apply merge-with max results)
              (map #(vector (first %) (+ current-flow (second %))))
              (into (hash-map))))))))

(defn max-non-overlapping-set-value 
  ([valve-sets]
   (let [ordered (reverse (sort-by second valve-sets))
         inspect-set-fn
         (fn [max-so-far valve-set]
           (let [current-value (max-non-overlapping-set-value valve-sets valve-set)]
             (if (< current-value (quot max-so-far 2))
               (reduced max-so-far)
               (max max-so-far current-value))))]
     (reduce inspect-set-fn 0 ordered)))
  ([valve-set-map valve-set]
   (->> valve-set-map
        (filter #(empty? (set/intersection (first valve-set) (first %))))
        (map second)
        (apply max)
        (+ (second valve-set)))))

(defn a
  ([] (a (slurp (io/resource "16.txt"))))
  ([input] (let [network (parse-input input)
                 valve-sets (max-release network 30 "AA" #{})]
             (apply max (map second valve-sets)))))

(defn b
  ([] (b (slurp (io/resource "16.txt"))))
  ([input] (let [network (parse-input input)
                 valve-sets (max-release network 26 "AA" #{})]
             (max-non-overlapping-set-value valve-sets))))

;; (defn distances [network]
;;   (let [nodes (vec (sort (keys network)))
;;         dist (->> (mapcat (fn [node] (->> (get-in network [node :targets])
;;                                           (map #(vector (vector node %) 1)))) nodes)
;;                   (into (hash-map)))]
;;     (->> (reduce (fn [dist k]
;;                    (->> (for [i nodes j nodes] [i j])
;;                         (reduce (fn [dist [i j]]
;;                                   (let [direct (get dist [i j])
;;                                         i-k (get dist [i k])
;;                                         k-j (get dist [k j])
;;                                         indirect (if (seq (filter nil? [i-k k-j]))
;;                                                    nil
;;                                                    (+ i-k k-j))
;;                                         vals (filter some? [direct indirect])]
;;                                     (if (empty? vals)
;;                                       dist
;;                                       (assoc dist [i j] (apply min vals)))))
;;                                 dist)))
;;                  dist
;;                  nodes)
;;          (filter #(= 2 (count (set (first %)))))
;;          (into (hash-map)))))

;; (defn compress-network [network]
;;   (let [dists (distances network)
;;         between-positive-valves? (fn [[[v w] _]] (and (pos? (get-in network [v :rate]))
;;                                                       (pos? (get-in network [w :rate]))))
;;         insert-fn (fn [g [[v w] d]]
;;                     (assoc-in g [v w] d))]
;;     (reduce insert-fn {} (filter between-positive-valves? dists))))


