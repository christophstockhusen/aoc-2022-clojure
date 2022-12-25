(ns aoc-2022-clojure.day-25 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input "1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122")

(defn snafu->decimal [snafu]
  (let [digits {\2 2 \1 1 \0 0 \- -1 \= -2}]
    (reduce
     (fn [d s] (+ (* d 5) (get digits s)))
     0
     (seq snafu))))

(defn decimal->snafu [decimal]
  (loop [decimal decimal
         snafu '()]
    (if (zero? decimal)
      (if (empty? snafu) 
        "0" 
        (str/join snafu))
      (let [digits {0 \0 1 \1 2 \2 3 \= 4 \-}
            remainder (rem decimal 5)
            carry (if (< 2 remainder) 1 0)]
        (recur (+ carry (quot decimal 5))
               (conj snafu (get digits remainder)))))))

(defn a
  ([] (a (slurp (io/resource "25.txt"))))
  ([input] (->> input
                str/split-lines
                (map snafu->decimal)
                (reduce +)
                (decimal->snafu))))