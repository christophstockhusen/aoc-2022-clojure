(ns aoc-2022-clojure.day-02-test 
  (:require [aoc-2022-clojure.day-02 :refer [a b]]
            [clojure.test :refer [deftest is testing]]))

(def input "A Y
B X
C Z")

(deftest rock-paper-scissors-a-test
      (testing "Day 02 - Part 1"
        (is (= 15 (a input))))) 

(deftest rock-paper-scissors-b-test
  (testing "Day 02 - Part 2"
    (is (= 12 (b input)))))
